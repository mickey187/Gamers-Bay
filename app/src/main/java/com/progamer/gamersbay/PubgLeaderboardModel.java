package com.progamer.gamersbay;

public class PubgLeaderboardModel {

    private long ID;
    private int Games;
    private int Wins;
    private int Kills;
    private int Rank;

    PubgLeaderboardModel(){}
    PubgLeaderboardModel(long ID, int Games, int Wins, int Kills, int Rank){

        this.ID = ID;
        this.Games = Games;
        this.Wins = Wins;
        this.Kills = Kills;
        this.Rank = Rank;

    }

    public long getID() {
        return ID;
    }

    public void setID(long ID) {
        this.ID = ID;
    }

    public int getGames() {
        return Games;
    }

    public void setGames(int games) {
        Games = games;
    }

    public int getWins() {
        return Wins;
    }

    public void setWins(int wins) {
        Wins = wins;
    }

    public int getKills() {
        return Kills;
    }

    public void setKills(int kills) {
        Kills = kills;
    }

    public int getRank() {
        return Rank;
    }

    public void setRank(int rank) {
        Rank = rank;
    }
}
