package com.example.atulsachdeva.ziriez.Models;

/**
 * Created by AtulSachdeva on 26/11/17.
 */

public class Show {
    String name;
    String episode;

    public Show() {     // for using it with firebase
    }

    public Show(String name, String episode) {
        this.name = name;
        this.episode = episode;
    }

    public String getName() {
        return name;
    }

    public String getEpisode() {
        return episode;
    }
}
