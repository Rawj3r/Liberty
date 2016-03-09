package com.equidais.mybeacon.model;

/**
 * Created by empirestate on 3/7/16.
 */
public class Company {

    private int id;
    private String cname;
    private String cid;

    public Company(){}

    public Company(int id, String cname){
        this.id = id;
        this.cname = cname;
    }

    public Company(String cid, String cname) {
        this.cid = cid;
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

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }
}
