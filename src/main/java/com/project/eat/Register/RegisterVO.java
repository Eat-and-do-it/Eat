package com.project.eat.Register;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterVO {

    private int num;
    private String name;
    private String id;
    private String pw;
    private String postcode;
    private String address;
    private String detailAddress;
    private String tel;
    private String email;
    private String nickname;


}