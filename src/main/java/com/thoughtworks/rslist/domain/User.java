package com.thoughtworks.rslist.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.*;

public class User {
    @JsonProperty("user_name")
    @NotNull
    @Size(max = 8)
    private String username;

    @JsonProperty("user_gender")
    @NotNull
    private String gender;

    @JsonProperty("user_age")
    @NotNull
    @Max(100)
    @Min(18)
    private int age;

    @JsonProperty("user_email")
    @Pattern(regexp = "\\w@\\w")
    private String email;

    @JsonProperty("user_phone")
    @NotNull
    @Pattern(regexp = "1[\\d]{10}")
    private String phone;


    public User() {
    }

    public User(String name, String gender, int age, String email, String phone) {
        this.username = name;
        this.gender = gender;
        this.age = age;
        this.email = email;
        this.phone = phone;
    }

    public String getUsername() {
        return username;
    }

    public void setName(String name) {
        this.username = name;
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
}
