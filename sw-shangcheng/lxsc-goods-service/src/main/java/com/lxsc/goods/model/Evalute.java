package com.lxsc.goods.model;

import java.util.List;

public class Evalute {
    private Long id;

    private String content;

    private Integer score;

    private Long time;

    private Long userId;

    private Long goodsId;

    private String nickName;

    private String headPortrait;

    private List<EvaluteImg> evaluteImgList;

    public List<EvaluteImg> getEvaluteImgList() {
        return evaluteImgList;
    }

    public void setEvaluteImgList(List<EvaluteImg> evaluteImgList) {
        this.evaluteImgList = evaluteImgList;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getHeadPortrait() {
        return headPortrait;
    }

    public void setHeadPortrait(String headPortrait) {
        this.headPortrait = headPortrait;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Long goodsId) {
        this.goodsId = goodsId;
    }
}