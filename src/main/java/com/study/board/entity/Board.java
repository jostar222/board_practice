package com.study.board.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity //DB의 테이블
@Data
public class Board {

    //DB 테이블의 컬럼
    @Id //primarykey를 의미
    @GeneratedValue(strategy = GenerationType.IDENTITY) //전략의 IDENTITY: mysql mariadb에서 사용
    private Integer id;

    private String title;

    private String content;

    private String filename;

    private String filepath;



}
