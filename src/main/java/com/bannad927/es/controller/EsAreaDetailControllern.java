package com.bannad927.es.controller;


import com.bannad927.es.entity.EsAreaDetail;
import com.bannad927.es.entity.Result;
import com.bannad927.es.service.EsAreaDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *
 * </p>
 *
 * @author chengbinbin
 * @since 2019-12-19
 */
@RestController
@RequestMapping("/es/areaDetail")
public class EsAreaDetailControllern {

    @Autowired
    private EsAreaDetailService areaDetailService;

    @RequestMapping("/insert")
    public Result insert(@RequestBody  EsAreaDetail areaDetail) {
        return areaDetailService.insert(areaDetail);
    }

    @RequestMapping("/update")
    public Result update(@RequestBody EsAreaDetail areaDetail) {
        return areaDetailService.update(areaDetail);
    }

    @RequestMapping("/delete")
    public Result delete(@RequestParam("id") Integer id) {
        return areaDetailService.delete(id);
    }

    @RequestMapping("/findById")
    public Result findById(@RequestParam("id") Integer id) {
        return areaDetailService.findById(id);
    }

    @RequestMapping("/findByAll")
    public Result findByAll() {
        return areaDetailService.findByAll();
    }


}
