package com.example.administrator.essim.response;

public class SingleTag {

    public String count;
    public String name;

    public SingleTag(String paramName) {
        this.name = paramName;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
