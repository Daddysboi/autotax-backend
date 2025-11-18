package com.autotax.domain;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
public class TermsOfUse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String version;
    private String content;
    private LocalDateTime effectiveDate;
    private String terms;
    @ManyToOne
    @JoinColumn(name = "portal_account_id")
    private PortalAccount portalAccount;
    protected LocalDateTime createdAt;

}
