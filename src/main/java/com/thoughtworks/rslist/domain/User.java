package com.thoughtworks.rslist.domain;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.*;

public class User {
    @NotNull
    @Size(max = 8)
    @JsonProperty("user_name")
    @JsonAlias("username")
    private String username;

    @NotNull
    @Max(100)
    @Min(18)
    @JsonProperty("user_age")
    @JsonAlias("age")
    private int age;

    @NotNull
    @JsonProperty("user_gender")
    @JsonAlias("gender")
    private String gender;

    @Email
    @JsonProperty("user_email")
    @JsonAlias("email")
    private String email;

    @NotNull
    @Pattern(regexp = "1[\\d]{10}")
    @JsonProperty("user_phone")
    @JsonAlias("phone")
    private String phone;


    public User() {
    }

    public User(String username, String gender, int age, String email, String phone) {
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

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
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
