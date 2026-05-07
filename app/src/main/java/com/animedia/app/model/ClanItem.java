package com.animedia.app.model;

import java.io.Serializable;

public class ClanItem implements Serializable {
    private int rank;
    private String name;
    private String rating;
    private String members;
    private String iconColor;

    public ClanItem() {}

    public ClanItem(int rank, String name, String rating, String members, String iconColor) {
        this.rank = rank;
        this.name = name;
        this.rating = rating;
        this.members = members;
        this.iconColor = iconColor;
    }

    public int getRank() { return rank; }
    public void setRank(int rank) { this.rank = rank; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getRating() { return rating; }
    public void setRating(String rating) { this.rating = rating; }

    public String getMembers() { return members; }
    public void setMembers(String members) { this.members = members; }

    public String getIconColor() { return iconColor; }
    public void setIconColor(String iconColor) { this.iconColor = iconColor; }
}
