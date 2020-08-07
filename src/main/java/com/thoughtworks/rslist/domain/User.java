package com.thoughtworks.rslist.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.*;

public class User {
    @JsonProperty("user_name")
    @NotNull
    @Size(max = 8)
    private String username;
    @NotNull
    @JsonProperty("user_gender")
    private String gender;

    @NotNull
    @Min(18)
    @Max(100)
    @JsonProperty("user_age")
    private int age;
    @Email
    @JsonProperty("user_email")
    private String email;
    @NotNull
    @Pattern(regexp = "1[\\d]{10}")
    @JsonProperty("user_phone")
    private String phone;

    @JsonIgnore
    private int voteNum = 10;

    public User() {
    }

    public User(@NotNull @Size(max = 8) String username, @NotNull String gender, @NotNull @Min(18) @Max(100) int age, @Email String email, @Pattern(regexp = "1[\\d]{10}") String phone) {
        this.username = username;
        this.gender = gender;
        this.age = age;
        this.email = email;
        this.phone = phone;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getVoteNum() {
        return voteNum;
    }

    public void setVoteNum(int voteNum) {
        this.voteNum = voteNum;
    }
}


