package com.autotax.service.impl;

import com.autotax.dao.AppRepository;
import com.autotax.dao.PortalAccountMembershipRepository;
import com.autotax.domain.GenericStatusConstant;
import com.autotax.domain.PortalAccount;
import com.autotax.domain.PortalAccountMembership;
import com.autotax.domain.PortalUser;
import com.autotax.domain.pojo.AccountMembershipPojo;
import com.autotax.integration.exception.ErrorResponse;
import com.autotax.service.PortalAccountMembershipService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PortalAccountMembershipServiceImpl implements PortalAccountMembershipService {

    private final PortalAccountMembershipRepository membershipRepository;
    private final AppRepository appRepository;


    @Transactional
    @Override
    public PortalAccountMembership createMembership(PortalAccount portalAccount, PortalUser user) {
        // Use the derived query method with membershipStatus
        Optional<PortalAccountMembership> existingMembership = membershipRepository.findMembership(portalAccount, user, GenericStatusConstant.ACTIVE);

        if (existingMembership.isPresent()) {
            throw new ErrorResponse(400, String.format("User %s is already a member of account %s",
                    user.getUsername(), portalAccount.getCode()));
        }
        PortalAccountMembership membership = newMembership(portalAccount);
        membership.setPortalUser(user);
        return membershipRepository.save(membership);
    }

    @Transactional
    @Override
    public PortalAccountMembership createOrGetMembership(PortalAccount portalAccount, PortalUser user) {
        // Use the derived query method with membershipStatus
        return membershipRepository.findMembership(portalAccount, user, GenericStatusConstant.ACTIVE).orElseGet(() -> {
            PortalAccountMembership membership = newMembership(portalAccount);
            membership.setPortalUser(user);
            return membershipRepository.save(membership);
        });
    }

    private PortalAccountMembership newMembership(PortalAccount portalAccount) {
        PortalAccountMembership membership = new PortalAccountMembership();
        membership.setCreatedAt(LocalDateTime.now());
        membership.setPortalAccount(portalAccount);
        membership.setStatus(GenericStatusConstant.ACTIVE);
        return membershipRepository.save(membership);
    }

    @Override
    public List<AccountMembershipPojo> getAllMemberships(PortalUser portalUser) {


        List<PortalAccountMembership> memberships = membershipRepository.findByPortalUser(portalUser);

        return memberships.stream().map(membership -> {
            AccountMembershipPojo accountMembershipPojo = new AccountMembershipPojo(membership.getPortalAccount());
            // TODO: Re-implement role and permission logic based on autotax domain model
            return accountMembershipPojo;
        }).collect(Collectors.toList());
    }


    @Override
    public PortalAccountMembership removeMembership(PortalAccountMembership portalAccountMembership) {
        portalAccountMembership.setStatus(GenericStatusConstant.DELETED);
        return membershipRepository.save(portalAccountMembership);
    }
}
