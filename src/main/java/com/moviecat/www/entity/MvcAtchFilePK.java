package com.moviecat.www.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Embeddable
@Getter
@Setter
public class MvcAtchFilePK implements Serializable {
    @Column(name = "ATCH_FILE_ID")
    private long atchFileId;

    @Column(name = "SEQ")
    private long seq;
}
