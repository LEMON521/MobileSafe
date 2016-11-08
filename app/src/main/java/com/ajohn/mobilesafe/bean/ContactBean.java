package com.ajohn.mobilesafe.bean;

/**
 * Created by John on 2016/10/22.
 */

public class ContactBean {
    private String id;
    private String name;
    private String phoneNum_1;
    private String phoneNum_2;
    private String phoneNum_3;
    private String phoneNum_4;
    private String address;
    private String email;
    private String company;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNum_1() {
        return phoneNum_1;
    }

    public void setPhoneNum_1(String phoneNum_1) {
        this.phoneNum_1 = phoneNum_1;
    }

    public String getPhoneNum_2() {
        return phoneNum_2;
    }

    public void setPhoneNum_2(String phoneNum_2) {
        this.phoneNum_2 = phoneNum_2;
    }

    public String getPhoneNum_3() {
        return phoneNum_3;
    }

    public void setPhoneNum_3(String phoneNum_3) {
        this.phoneNum_3 = phoneNum_3;
    }

    public String getPhoneNum_4() {
        return phoneNum_4;
    }

    public void setPhoneNum_4(String phoneNum_4) {
        this.phoneNum_4 = phoneNum_4;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ContactBean that = (ContactBean) o;

        if (!id.equals(that.id)) return false;
        if (!name.equals(that.name)) return false;
        return phoneNum_1.equals(that.phoneNum_1);

    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + name.hashCode();
        result = 31 * result + phoneNum_1.hashCode();
        return result;
    }
}
