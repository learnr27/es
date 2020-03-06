package com.bannad927.es.service;

import com.bannad927.es.entity.Result;

/**
 * @author chengbb@xmulife.com
 * @date 2020.3.5
 */
public interface DeviceOrderService {

    Result findById(Integer id);

    Result findByAll();

    Result findByOrderNumber(String orderNumber);
}
