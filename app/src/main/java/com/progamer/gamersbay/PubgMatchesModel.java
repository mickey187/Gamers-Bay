package com.progamer.gamersbay;

public class PubgMatchesModel {

    private String match_description;
    private String game_map;
    private String match_date;
    private int reward;
    private String match_time;
    private String type;
    private int maximum_number_of_players;
    private int players_that_joined;
    private int entrance_fee;
    private String match_name;
    private String match_status;


    private PubgMatchesModel(){};
    private PubgMatchesModel(String match_description, String game_map, String match_date, int reward, String match_time,
                             String type, int  players_that_joined, int maximum_number_of_players, int entrance_fee, String match_name, String match_status ){

        this.match_description = match_description;
        this.game_map = game_map;
        this.match_date = match_date;
        this.match_date = match_date;
        this.reward = reward;
        this.match_time = match_time;
        this.type = type;
        this.players_that_joined = players_that_joined;
        this.maximum_number_of_players = maximum_number_of_players;
        this.entrance_fee = entrance_fee;
        this.match_name = match_name;
        this.match_status = match_status;



    }

    public String getMatch_status() {
        return match_status;
    }

    public void setMatch_status(String match_status) {
        this.match_status = match_status;
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

    public int getReward() {
        return reward;
    }

    public void setReward(int reward) {
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


    public int getPlayers_that_joined() {
        return players_that_joined;
    }

    public void setPlayers_that_joined(int players_that_joined) {
        this.players_that_joined = players_that_joined;
    }


    public int getMaximum_number_of_players() {
        return maximum_number_of_players;
    }

    public void setMaximum_number_of_players(int maximum_number_of_players) {
        this.maximum_number_of_players = maximum_number_of_players;
    }

    public int getEntrance_fee() {
        return entrance_fee;
    }

    public void setEntrance_fee(int entrance_fee) {
        this.entrance_fee = entrance_fee;
    }


    public String getMatch_name() {
        return match_name;
    }

    public void setMatch_name(String match_name) {
        this.match_name = match_name;
    }
}
