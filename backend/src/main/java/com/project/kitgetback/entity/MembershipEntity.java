package com.project.kitgetback.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "membership")
public class MembershipEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String email;       //이메일
    private String password;    //비밀번호
    private String school;      //학교
    private String name;        //이름
    private String code;        //학번
    private String auth;        //권한
}
