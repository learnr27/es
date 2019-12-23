package com.bannad927.es.controller;


import com.bannad927.es.entity.AreaDetail;
import com.bannad927.es.entity.Result;
import com.bannad927.es.service.AreaDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 *
 * </p>
 *
 * @author chengbinbin
 * @since 2019-12-19
 */
@RestController
@RequestMapping("/areaDetail")
public class AreaDetailControllern {

    @Autowired
    private AreaDetailService areaDetailService;

    @RequestMapping("/insert")
    public Result insert(@RequestBody  AreaDetail areaDetail) {
        return areaDetailService.insert(areaDetail);
    }

    @RequestMapping("/update")
    public Result update(@RequestBody AreaDetail areaDetail) {
        return areaDetailService.update(areaDetail);
    }

    @RequestMapping("/delete")
    public Result delete(@RequestParam("id") Integer id) {
        return areaDetailService.delete(id);
    }


    @RequestMapping("/findById")
    public Result findById(@RequestParam("id")  Integer id) {
        return areaDetailService.findById(id);
    }

    @RequestMapping("/findByAll")
    public Result findByAll() {
        return areaDetailService.findByAll();
    }


}
