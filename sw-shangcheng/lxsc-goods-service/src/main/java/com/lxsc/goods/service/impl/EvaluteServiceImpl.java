package com.lxsc.goods.service.impl;

import com.lxsc.PageBean;
import com.lxsc.goods.mapper.EvaluteMapper;
import com.lxsc.goods.model.Evalute;
import com.lxsc.goods.service.EvaluteService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class EvaluteServiceImpl implements EvaluteService {
    @Resource
    private EvaluteMapper evaluteMapper;

    @Override
    public PageBean<List<Evalute>> getEvaluteListPage(Long goodsId, String evaluteLevel, Long pageNo, Long pageSize) {
        PageBean pageBean=new PageBean(pageNo,pageSize);
        List<Evalute> list=null;
        //
        if(evaluteLevel.equals("img")){
            long totalNum=evaluteMapper.countEvaluteHaveImgByGoodsId(goodsId);
            pageBean.setTotalNum(totalNum);
            list=evaluteMapper.selectEvaluteHaveImgPage(goodsId,pageBean.getSkipNum(),pageBean.getPageSize());
        }else{
            long totalNum=evaluteMapper.countEvaluteByGoodsId(goodsId,evaluteLevel);
            pageBean.setTotalNum(totalNum);
            list=evaluteMapper.selectEvalutePage(goodsId,pageBean.getSkipNum(),pageBean.getPageSize(),evaluteLevel);
        }
        pageBean.setData(list);
        return pageBean;
    }
    //返回各类评价的数目
    @Override
    public Map countEvaluteNum(Long goodsId) {
        return evaluteMapper.countEvaluteNum(goodsId);
    }


}

