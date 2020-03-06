package com.bannad927.es.controller;

import com.bannad927.es.entity.Result;
import com.bannad927.es.service.DeviceOrderService;
import com.bannad927.es.service.EsAreaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author chengbb@xmulife.com
 * @date 2020.3.5
 */
@RestController
@RequestMapping("/es/deviceOrder")
public class DeviceOrderController {

    @Autowired
    private DeviceOrderService deviceOrderService;

    @RequestMapping("/findById")
    public Result findById(@RequestParam("id") Integer id) {
        return deviceOrderService.findById(id);
    }

    @RequestMapping("/findByAll")
    public Result findByAll() {
        return deviceOrderService.findByAll();
    }

    @RequestMapping("/findByOrderNumber")
    public Result findByOrderNumber(@RequestParam("orderNumber") String orderNumber) {
        return deviceOrderService.findByOrderNumber(orderNumber);
    }
}
