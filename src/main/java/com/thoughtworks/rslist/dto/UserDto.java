package com.thoughtworks.rslist.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Generated;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "user")

@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    @Id
    @Generated
    private int id;

    @Column(name="name")
    private String username;
    private String gender;
    private int age;
    private String email;
    private String phone;

    int voteNumber = 10;
}
