package com.example.lightcontrol;

public class Record {
    private String name;
    private String time;
    private String averagelight;

    public Record(String name,String time,String averagelight) {
        this.name = name;
        this.time = time;
        this.averagelight = averagelight;
    }

    public String getName() {
        return name;
    }
    public String getTime() {
        return time;
    }
    public String getAveragelight() {
        return  averagelight;
    }
}
