package com.project.kitgetback.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "todo")
public class TodoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;       //이메일
    private String content;     //내용
    private boolean completed;  //달성여부
}
