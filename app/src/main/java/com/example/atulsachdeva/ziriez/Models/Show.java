package com.example.atulsachdeva.ziriez.Models;

import java.util.HashMap;

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

    public HashMap<String,Object> toMap(){
        HashMap<String,Object> map = new HashMap<>();
        map.put("name",name);
        map.put("episode",episode);
        return map;
    }
}
