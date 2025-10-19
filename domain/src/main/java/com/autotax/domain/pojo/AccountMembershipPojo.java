package com.autotax.domain.pojo;

import com.autotax.domain.PermissionTypeConstant;
import com.autotax.domain.PortalAccount;
import com.autotax.domain.PortalAccountTypeConstant;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountMembershipPojo {

    private String accountName;
    private String accountCode;
    private Long accountId;
    private Boolean enableSubscriptions;
    private Boolean acceptedTermsOfUse;
    private TermsOfUsePojo terms;
    private PortalAccountTypeConstant accountType;
    private Set<String> roles;
    private Set<PermissionTypeConstant> permissions;

    private Long accountLogoId;

    public AccountMembershipPojo(PortalAccount portalAccount) {
        accountName = portalAccount.getName();
        accountCode = portalAccount.getCode();
        accountType = portalAccount.getType();
        accountId = portalAccount.getId();
    }
}
