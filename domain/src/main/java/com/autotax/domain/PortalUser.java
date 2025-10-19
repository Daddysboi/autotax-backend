package com.autotax.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "portal_user")
@EqualsAndHashCode(callSuper = true)
public class PortalUser extends StatusEntity {

    @Column(name = "first_name", nullable = false, length = 1024)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 1024)
    private String lastName;

    @Column(name = "other_names", length = 1024)
    private String otherNames;

    @Column(name = "email", nullable = false, unique = true, length = 1024)
    private String email;

    @Column(name = "phone_number", length = 1024)
    private String phoneNumber;

    @Column(name = "user_id", unique = true, nullable = false, length = 1024)
    private String userId;

    @Column(name = "generated_password", length = 1024)
    private String generatedPassword;

    @Column(name = "username", nullable = false, unique = true, length = 1024)
    private String username;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Column(name = "display_name", nullable = false, length = 1024)
    private String displayName;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    private GenderConstant gender;

    @Column(name = "setup_complete")
    private Boolean setupComplete;

    @Column(name = "email_verified")
    private Boolean emailVerified;

    @Column(name = "preferred_name", length = 1024)
    private String preferredName;

    private Boolean userVerified;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "image_id")
    private BwBinaryData image;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "address_id")
    private Address address;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "signature_id")
    private BwBinaryData signature;
}
