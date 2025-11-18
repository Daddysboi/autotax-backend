package com.autotax.infrastructure.interceptors;

import com.autotax.infrastructure.security.AccessStatus;
import com.autotax.infrastructure.security.constraint.TrustedIpAddress;
import com.autotax.service.SettingService;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Optional;

/**
 * @author Temitope temitopeahmedyusuf@gmail.com
 */
@Named
public class TrustedIpAddressAccessManager {

    @Inject
    private SettingService settingService;

    @Inject
    private HttpServletRequest request;

    public AccessStatus getStatus(TrustedIpAddress accessConstraint) {
        Optional<String> value = settingService.getString(accessConstraint.value());
        String ipAddress = StringUtils.defaultIfBlank(
                request.getHeader("X-FORWARDED-FOR"),
                request.getRemoteAddr());
        if (value.isPresent()) {
            return Arrays.asList(value.get().split(" *, *")).contains(ipAddress)
                    ? AccessStatus.allowed()
                    : AccessStatus.denied(ipAddress);
        }
        if (accessConstraint.defaultIpAddresses().length > 0) {
            return Arrays.asList(accessConstraint.defaultIpAddresses()).contains(ipAddress)
                    ? AccessStatus.allowed()
                    : AccessStatus.denied(ipAddress);
        }
        return AccessStatus.denied("");
    }
}
