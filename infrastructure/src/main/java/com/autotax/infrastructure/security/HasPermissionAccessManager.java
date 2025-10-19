package com.autotax.infrastructure.security;


import com.autotax.domain.pojo.AccountMembershipPojo;
import com.autotax.domain.principal.RequestPrincipal;
import com.autotax.infrastructure.security.constraint.HasPermission;
import jakarta.inject.Inject; // Corrected import
import jakarta.inject.Named;
import jakarta.inject.Provider;
import jakarta.transaction.Transactional;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Named
public class HasPermissionAccessManager implements AccessStatusSource<HasPermission> {

    @Inject
    private Provider<RequestPrincipal> requestPrincipalProvider;

    @Transactional
    @Override
    public AccessStatus getStatus(HasPermission accessConstraint) {
        List<AccountMembershipPojo> accountPermissionStream = requestPrincipalProvider.get().getAccountPermissions();

        accountPermissionStream = accountPermissionStream.stream().filter(it -> it.getPermissions() != null).collect(Collectors.toList());

        return accountPermissionStream.stream().anyMatch(it -> !Collections.disjoint(Arrays.asList(accessConstraint.value()), it.getPermissions()))
                ? AccessStatus.allowed()
                : AccessStatus.denied(String.format("Required permissions missing: %s", Arrays.asList(accessConstraint.value())));
    }
}
