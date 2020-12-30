package com.progamer.gamersbay;

public class PubgMatchesModel {

    private String match_description;
    private String game_map;
    private String match_date;
    private String reward;
    private String match_time;
    private String type;

    private PubgMatchesModel(){};
    private PubgMatchesModel(String match_description, String game_map, String match_date, String reward, String match_time,
                             String type){

        this.match_description = match_description;
        this.game_map = game_map;
        this.match_date = match_date;
        this.match_date = match_date;
        this.reward = reward;
        this.match_time = match_time;
        this.type = type;
    }

    public String getMatch_description() {
        return match_description;
    }

    public void setMatch_description(String match_description) {
        this.match_description = match_description;
    }

    public String getGame_map() {
        return game_map;
    }

    public void setGame_map(String game_map) {
        this.game_map = game_map;
    }

    public String getMatch_date() {
        return match_date;
    }

    public void setMatch_date(String match_date) {
        this.match_date = match_date;
    }

    public String getReward() {
        return reward;
    }

    public void setReward(String reward) {
        this.reward = reward;
    }

    public String getMatch_time() {
        return match_time;
    }

    public void setMatch_time(String match_time) {
        this.match_time = match_time;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
