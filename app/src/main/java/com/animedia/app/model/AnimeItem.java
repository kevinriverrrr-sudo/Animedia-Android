package com.animedia.app.model;

import java.io.Serializable;

public class AnimeItem implements Serializable {
    private String id;
    private String title;
    private String posterUrl;
    private String detailUrl;
    private String timeInfo;
    private String episodeInfo;
    private int episodeNumber;
    private String badge;
    private String description;
    private String subTitle;
    private String year;
    private String status;
    private String rating;
    private String type;
    private String studio;
    private String season;
    private String totalEpisodesText;
    private int totalEpisodes;
    private String[] genres;
    private boolean isFavorite;
    private boolean is3D;

    public AnimeItem() {}

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getPosterUrl() { return posterUrl; }
    public void setPosterUrl(String posterUrl) { this.posterUrl = posterUrl; }

    public String getDetailUrl() { return detailUrl; }
    public void setDetailUrl(String detailUrl) { this.detailUrl = detailUrl; }

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

    public String getSubTitle() { return subTitle; }
    public void setSubTitle(String subTitle) { this.subTitle = subTitle; }

    public String getYear() { return year; }
    public void setYear(String year) { this.year = year; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getRating() { return rating; }
    public void setRating(String rating) { this.rating = rating; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getStudio() { return studio; }
    public void setStudio(String studio) { this.studio = studio; }

    public String getSeason() { return season; }
    public void setSeason(String season) { this.season = season; }

    public String getTotalEpisodesText() { return totalEpisodesText; }
    public void setTotalEpisodesText(String text) { this.totalEpisodesText = text; }

    public int getTotalEpisodes() { return totalEpisodes; }
    public void setTotalEpisodes(int totalEpisodes) { this.totalEpisodes = totalEpisodes; }

    public String[] getGenres() { return genres; }
    public void setGenres(String[] genres) { this.genres = genres; }

    public boolean isFavorite() { return isFavorite; }
    public void setFavorite(boolean favorite) { isFavorite = favorite; }

    public boolean is3D() { return is3D; }
    public void set3D(boolean d) { is3D = d; }

    public String getFullPosterUrl() {
        if (posterUrl == null) return "";
        if (posterUrl.startsWith("http")) return posterUrl;
        return "https://amd.online" + posterUrl;
    }

    public String getFullDetailUrl() {
        if (detailUrl == null) return "";
        if (detailUrl.startsWith("http")) return detailUrl;
        return "https://amd.online" + detailUrl;
    }
}
