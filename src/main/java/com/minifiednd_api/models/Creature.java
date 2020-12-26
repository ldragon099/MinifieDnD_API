package com.minifiednd_api.models;

public class Creature {

    private final long id;
    private final String name;
    private final String source;
    private final String size;
    private final String alignment;
    private final String cr;

    public Creature(long id, String name, String source, String size, String alignment, String cr) {
        this.id = id;
        this.name = name;
        this.source = source;
        this.size = size;
        this.alignment = alignment;
        this.cr = cr;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
    public String getSource() {
        return source;
    }
    public String getSize() {
        return size;
    }
    public String getAlignment() {
        return alignment;
    }
    public String getCr() {
        return cr;
    }
}