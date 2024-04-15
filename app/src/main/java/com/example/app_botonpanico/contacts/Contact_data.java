package com.example.app_botonpanico.contacts;

public class Contact_data {
    int id;
    String first_name;
    String last_name;
    String nickname;
    String phone_number;

    public Contact_data() {

    }

    public Contact_data(String first_name, String last_name, String nickname, String phone_number) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.nickname = nickname;
        this.phone_number = phone_number;
    }

    public Contact_data(int id, String first_name, String last_name, String nickname, String phone_number) {
        this.id = id;
        this.first_name = first_name;
        this.last_name = last_name;
        this.nickname = nickname;
        this.phone_number = phone_number;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

}
