package com.example.fshen.carloudy;

import java.io.Serializable;

/**
 * Created by fshen on 17/2/25.
 */
public class message implements Serializable {
    private static final long serialVersionUID = -7060210544600464481L;
    private String id;
    private String title;
    private String content;
    private String time;
    private String latitude;
    private String longitude;

    public message() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTime() {
        return time;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLongitude() {
        return longitude;
    }

    @Override
    public String toString() {
        return "Message {" + "id=" + id + ", title=" + title + ", content=" + content + ", time=" + time + ", latitude=" + latitude + ", longitude=" + longitude +'}';
    }

}