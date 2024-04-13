package com.lxsc.goods.model;

public class EvaluteImg {
    private Long id;

    private String imageUrl;

    private Long evaluteId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Long getEvaluteId() {
        return evaluteId;
    }

    public void setEvaluteId(Long evaluteId) {
        this.evaluteId = evaluteId;
    }
}