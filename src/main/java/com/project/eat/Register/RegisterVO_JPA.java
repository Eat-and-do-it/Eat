package com.project.eat.Register;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

//@Entity : 자바의 객체와 DB테이블을 매칭시켜주는 기능
//@Table : DB에 테이블을 정의 해준다.

@Data
@Entity
@Table(name = "MEMBER_JPA", uniqueConstraints = { @UniqueConstraint(columnNames = { "user_id" }) })
public class RegisterVO_JPA {

    @Id // pk설정
    @GeneratedValue(strategy = GenerationType.IDENTITY)//auto increment
    @Column(name = "num") // 컬럼이름 설정
    private int num;

    @Column(name = "user_id", nullable = true)
    private String id;

    @Column(name = "user_pw", nullable = true)//false 에서 true 로 변경하면 동작은 되는데 값이 저장이 안된다.
    private String pw;

    @Column(name = "user_salt")
    private String salt;

    @Column(name = "user_name", nullable = false)//false 에서 true 로 변경하면 동작은 되는데 값이 저장이 안된다.
    private String name;

    @Column(name = "user_tel", nullable = false) //false 에서 true 로 변경하면 동작은 되는데 값이 저장이 안된다.
    private String tel;
    //추가 된 항목
    @Column(name = "user_email", nullable = false) //bean 값으로 등록이 안되서 주석을 안 달면 오류
    private String email;

    @Column(name = "user_address", nullable = false)
    private String address;

    @Column(name = "user_postcode", nullable = false)
    private String postcode;

    @Column(name = "user_detailaddress", nullable = false)
    private String detailAddress;

    @Column(name = "user_nickname", nullable = false)
    private String nickname;

    @Column(columnDefinition = "DATETIME(0) default CURRENT_TIMESTAMP",insertable = false)
    private Date regdate;

}
