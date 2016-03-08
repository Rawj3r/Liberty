package com.equidais.mybeacon.model;

/**
 * Created by empirestate on 3/7/16.
 */
public class Company {

    private int id;
    private String cname;

    public Company(){}

    public Company(int id, String cname){
        this.id = id;
        this.cname = cname;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}