package com.example.veryhomepage.livestream;

import android.util.Log;

import java.io.Serializable;
import java.sql.Date;

public class Livestream implements Serializable {

    //    private String donation_id;
    private String livestream_id;
    private String member_id;
    private Date livestream_date;
    private Integer donation_cost;
    private String introduction;
    private String title;
    private int status;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Livestream(){
        super();
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Livestream(String member_id, Integer donation_cost){
        super();
        this.member_id = member_id;
        this.donation_cost = donation_cost;
        this.title = title;
        this.status = status;
        this.livestream_date = livestream_date;
        this.introduction = introduction;
        this.livestream_id = livestream_id;
    }

    public Livestream(String livestream_id){
        this.livestream_id = livestream_id;
    }


    //    public String getDonation_id() {
//        return donation_id;
//    }
//
//    public void setDonation_id(String donation_id) {
//        this.donation_id = donation_id;
//    }
//
    public String getLivestream_id() {
        return livestream_id;
    }

    public void setLivestream_id(String livestream_id) {
        this.livestream_id = livestream_id;
    }

    public String getMember_id() {
        return member_id;
    }

    public void setMember_id(String member_id) {
        this.member_id = member_id;
    }

    public Date getLivestream_date() {
        return livestream_date;
    }

    public void setLivestream_date(Date livestream_date) {
        this.livestream_date = livestream_date;
        Log.e("livestream_date", String.valueOf(livestream_date));
    }

    public Integer getDonation_cost() {
        return donation_cost;
    }

    public void setDonation_cost(Integer donation_cost) {
        this.donation_cost = donation_cost;
    }
}
