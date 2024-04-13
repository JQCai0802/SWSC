package com.lxsc.goods.controller;

import com.lxsc.Code;
import com.lxsc.JsonResult;
import com.lxsc.PageBean;
import com.lxsc.goods.model.Evalute;
import com.lxsc.goods.service.EvaluteService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
public class EvaluteController {
    @Resource
    private EvaluteService evaluteService;
    @GetMapping("/getEvaluteListPage")
    public Object getEvaluteListPage(Long goodsId,String evaluteLevel,long pageNo,long pageSize){
        PageBean<List<Evalute>> pageBean=evaluteService.getEvaluteListPage(goodsId,evaluteLevel,pageNo,pageSize);
        return new JsonResult<PageBean>(Code.OK,pageBean);
    }
    @GetMapping("/countEvaluteNum")
    public Object countEvaluteNum(Long goodsId){
        Map map=evaluteService.countEvaluteNum(goodsId);
        return new JsonResult<Map>(Code.OK,map);
    }
}
