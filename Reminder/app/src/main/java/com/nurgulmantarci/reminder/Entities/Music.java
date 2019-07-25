package com.nurgulmantarci.reminder.Entities;

public class Music {
    private long id;
    private String name;
    public static long selectedId=5000;

    public Music() {
        this.id=0;
        this.name="";
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
