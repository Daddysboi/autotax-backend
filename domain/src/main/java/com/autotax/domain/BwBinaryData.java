package com.autotax.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Lob;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@EqualsAndHashCode(callSuper = true)
public class BwBinaryData extends StatusEntity {

    @Lob
    private byte[] content;

    private String contentType;

    private String originalFilename;
}
