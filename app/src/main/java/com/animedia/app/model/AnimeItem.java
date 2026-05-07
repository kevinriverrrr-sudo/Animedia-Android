package com.animedia.app.model;

import java.io.Serializable;

public class AnimeItem implements Serializable {
    private int id;
    private String title;
    private String posterUrl;
    private String backdropUrl;
    private String timeInfo;
    private String episodeInfo;
    private int episodeNumber;
    private String badge;
    private String description;
    private String year;
    private String status;
    private String rating;
    private String type;
    private String[] genres;
    private boolean isFavorite;
    private int totalEpisodes;

    public AnimeItem() {}

    public AnimeItem(int id, String title, String posterUrl, String timeInfo, String episodeInfo, int episodeNumber) {
        this.id = id;
        this.title = title;
        this.posterUrl = posterUrl;
        this.timeInfo = timeInfo;
        this.episodeInfo = episodeInfo;
        this.episodeNumber = episodeNumber;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getPosterUrl() { return posterUrl; }
    public void setPosterUrl(String posterUrl) { this.posterUrl = posterUrl; }

    public String getBackdropUrl() { return backdropUrl; }
    public void setBackdropUrl(String backdropUrl) { this.backdropUrl = backdropUrl; }

    public String getTimeInfo() { return timeInfo; }
    public void setTimeInfo(String timeInfo) { this.timeInfo = timeInfo; }

    public String getEpisodeInfo() { return episodeInfo; }
    public void setEpisodeInfo(String episodeInfo) { this.episodeInfo = episodeInfo; }

    public int getEpisodeNumber() { return episodeNumber; }
    public void setEpisodeNumber(int episodeNumber) { this.episodeNumber = episodeNumber; }

    public String getBadge() { return badge; }
    public void setBadge(String badge) { this.badge = badge; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getYear() { return year; }
    public void setYear(String year) { this.year = year; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getRating() { return rating; }
    public void setRating(String rating) { this.rating = rating; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String[] getGenres() { return genres; }
    public void setGenres(String[] genres) { this.genres = genres; }

    public boolean isFavorite() { return isFavorite; }
    public void setFavorite(boolean favorite) { isFavorite = favorite; }

    public int getTotalEpisodes() { return totalEpisodes; }
    public void setTotalEpisodes(int totalEpisodes) { this.totalEpisodes = totalEpisodes; }
}
