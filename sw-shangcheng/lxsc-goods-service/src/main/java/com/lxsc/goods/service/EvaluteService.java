package com.lxsc.goods.service;

import com.lxsc.PageBean;
import com.lxsc.goods.model.Evalute;

import java.util.List;
import java.util.Map;

public interface EvaluteService {
    PageBean<List<Evalute>> getEvaluteListPage(Long goodsId, String evaluteLevel, Long pageNo, Long pageSize);

    Map countEvaluteNum(Long goodsId);
}
