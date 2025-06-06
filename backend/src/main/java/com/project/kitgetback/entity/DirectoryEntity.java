package com.project.kitgetback.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "directory")
public class DirectoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String school;  // 학교
    private String name;    // 이름
    private String code;    // 학번
    private String auth;    // 권한
}
