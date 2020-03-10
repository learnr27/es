package com.bannad927.es.controller;

import com.bannad927.es.entity.EsAreaDetail;
import com.bannad927.es.entity.Result;
import com.bannad927.es.service.EsAreaDetailService;
import com.bannad927.es.service.EsAreaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author cbb
 * @date 2020.3.4
 */
@RestController
@RequestMapping("/es/area")
public class EsAreaControllern {


    @Autowired
    private EsAreaService areaService;


    @RequestMapping("/findById")
    public Result findById(@RequestParam("id") Integer id) {
        return areaService.findById(id);
    }

    @RequestMapping("/findByAll")
    public Result findByAll() {
        return areaService.findByAll();
    }

    @RequestMapping("/findByAreaCode")
    public Result findByAreaCode(@RequestParam("areaCode") String areaCode) {
        return areaService.findByAreaCode(areaCode);
    }

}
