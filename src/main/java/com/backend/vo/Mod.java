package com.backend.vo;

public class Mod {
    private int modId;
    private String modname;
    private int softwareId;
    private int userId;
    private int price;
    private int downloads;
    private int heat;
    private String filepath;

    public Mod(int modId, String modname, int softwareId, int userId, int price, int downloads, int heat, String filepath) {
        this.modId = modId;
        this.modname = modname;
        this.softwareId = softwareId;
        this.userId = userId;
        this.price = price;
        this.downloads = downloads;
        this.heat = heat;
        this.filepath = filepath;
    }
}
