package danazone.com.emailexample;

import java.io.Serializable;

public class Admin implements Serializable {
    private int id;
    private String name;
    private String phone;
    private String code;

    public Admin(){

    }

    public Admin(int id, String name, String phone, String code) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.code = code;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
