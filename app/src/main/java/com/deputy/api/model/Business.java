package com.deputy.api.model;

import com.deputy.activity.BaseActivity;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by srilatha on 25/03/2017.
 */
public class Business{

    private String name;

    private String logo;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

}
