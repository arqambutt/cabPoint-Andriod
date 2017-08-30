package com.apps.cabpoint.cabigate.models;

import java.io.Serializable;

/**
 * Created by Abdul Ghani on 7/17/2017.
 */

public class NeedHelp implements Serializable{
    private String id;
    private String heading;
    private String feedBack;
    private String details;

    public NeedHelp() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHeading() {
        return heading;
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }

    public String getFeedBack() {
        return feedBack;
    }

    public void setFeedBack(String feedBack) {
        this.feedBack = feedBack;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    @Override
    public String toString() {
        return "NeedHelp{" +
                "id='" + id + '\'' +
                ", heading='" + heading + '\'' +
                ", feedBack='" + feedBack + '\'' +
                ", details='" + details + '\'' +
                '}';
    }
}
