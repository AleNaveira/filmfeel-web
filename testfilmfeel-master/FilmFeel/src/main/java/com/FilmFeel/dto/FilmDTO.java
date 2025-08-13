package com.FilmFeel.dto;




public class FilmDTO {
    private Long id;
    private String title;
    private Integer year;
    private Integer duration;
    private String synopsis;
    private String posterRoute;



    public FilmDTO() {
    }

    public FilmDTO(Long id) {
        this.id = id;
    }

    public FilmDTO(Long id, String title, Integer year, Integer duration, String synopsis, String posterRoute) {
        this.id = id;
        this.title = title;
        this.year = year;
        this.duration = duration;
        this.synopsis = synopsis;
        this.posterRoute= posterRoute;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    public String getPosterRoute() {
        return posterRoute;
    }

    public void setPosterRoute(String posterRoute) {
        this.posterRoute = posterRoute;
    }
}
