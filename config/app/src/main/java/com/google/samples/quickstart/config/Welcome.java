package com.google.samples.quickstart.config;

/**
 * Created by yasuhirosuzuki on 2017/03/17.
 */

public class Welcome {

    private String message;
    private boolean caps;

    public Welcome(String message, boolean caps) {
        this.message = message;
        this.caps = caps;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isCaps() {
        return caps;
    }

    public void setCaps(boolean caps) {
        this.caps = caps;
    }
}
