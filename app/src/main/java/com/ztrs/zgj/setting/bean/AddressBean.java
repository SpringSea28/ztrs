package com.ztrs.zgj.setting.bean;

public class AddressBean {

    String address;
    String name;
    int number;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    @Override
    public String toString() {
        return "AddressBean{" +
                "address='" + address + '\'' +
                ", name='" + name + '\'' +
                ", number=" + number +
                '}';
    }
}
