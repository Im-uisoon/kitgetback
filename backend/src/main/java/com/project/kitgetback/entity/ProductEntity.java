package com.project.kitgetback.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "product")
public class ProductEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String school;      //학교
    private String className;   //강의명
    private String productName; //상품명
    private Integer price;      //금액
}
