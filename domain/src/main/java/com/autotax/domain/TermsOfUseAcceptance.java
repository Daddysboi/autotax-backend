package com.autotax.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "terms_of_use_acceptance")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class TermsOfUseAcceptance extends StatusEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "terms_of_use_id", nullable = false)
    private TermsOfUse termsOfUse;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "portal_user_id", nullable = false)
    private PortalUser portalUser;

    // You might want to add other fields specific to the acceptance here, e.g., acceptanceDate
}
