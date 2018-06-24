package com.example.administrator.essim.response;

public class BookmarkTagResponse{

    public BookmarkTagResponse(String pName, boolean pIs_registered)
    {
        this.name = pName;
        this.is_registered = pIs_registered;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isIs_registered() {
        return is_registered;
    }

    public void setIs_registered(boolean is_registered) {
        this.is_registered = is_registered;
    }

    public String name;
    public boolean is_registered;
}