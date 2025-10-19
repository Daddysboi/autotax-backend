package com.autotax.infrastructure.configuration.security;

import com.bw.cfs.entity.PortalUser;

import java.security.Principal;

/**
 * @author Gibah Joseph
 * email: gibahjoe@gmail.com
 * Aug, 2021
 **/
public class CustomAuthenticationUserPrincipal implements Principal {

    private final String ipAddress;
    private final String authHeader;
    private PortalUser portalUser;

    public CustomAuthenticationUserPrincipal(String ipAddress, String authHeader, PortalUser portalUser) {
        this.ipAddress = ipAddress;
        this.authHeader = authHeader;
        this.portalUser = portalUser;
    }

//    public AccountPermission getAccountPermissions() {
//        AccountPermission accountPermission = new AccountPermission();
//        accountPermission.setAccountName("");
//        accountPermission.setAccountCode(getUserName());
//        accountPermission.setAccountType(PortalAccountTypeConstant.INDIVIDUAL);
//        accountPermission.setRoles(Collections.singleton("ADMIN"));
//        accountPermission.setPermissions(Collections.singleton(PermissionConstant.VIEW_BILL));
//        accountPermission.setSponsorAgencyCode("");
//
//
//        UserDetail userDetail = null;
//        try {
//            userDetail = getUserDetail();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        if (userDetail == null) {
//            return accountPermission;
//        }
//        List<AccountPermission> accounts = userDetail.getAccounts().stream()
//                .filter(acct -> acct.getAccountType().equals(PortalAccountTypeConstant.INDIVIDUAL)).collect(Collectors.toList());
//        if ((accounts.size() > 1) && (getCurrentAccountCode() != null)) {
//            return accounts.stream().findFirst().filter(acct -> acct.getAccountCode().equals(getCurrentAccountCode())).orElse(null);
//        }
//        return accounts.get(0);
//    }

//    public UserDetail getUserDetail() {
//        return userDetail;
//    }

    public String getAuthHeader() {
        return authHeader;
    }

    public PortalUser getPortalUser() {
        return portalUser;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public boolean isAuthenticated() {
        return true;
    }

    @Override
    public String getName() {
        return getPortalUser().getDisplayName();
    }
}
