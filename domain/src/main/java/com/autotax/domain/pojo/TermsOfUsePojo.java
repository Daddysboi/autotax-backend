package com.autotax.domain.pojo;

import com.autotax.domain.TermsOfUse;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TermsOfUsePojo {
    private String termsOfUse;
    private Long id;
    private String version;
    private String portalAccountCode;
    private LocalDateTime createdAt;


    public TermsOfUsePojo(TermsOfUse terms) {
        id = terms.getId();
        termsOfUse = terms.getTerms();
        version = terms.getVersion();
        portalAccountCode = terms.getPortalAccount().getCode();
        createdAt = terms.getCreatedAt();
    }
}
