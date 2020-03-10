package com.bannad927.es.service.impl;

import com.bannad927.es.entity.Result;
import com.bannad927.es.repository.AreaDetailRepository;
import com.bannad927.es.repository.DeviceOrderRepository;
import com.bannad927.es.service.DeviceOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author cbb
 * @date 2020.3.5
 */
@Service
@Slf4j
public class DeviceOrderServiceImpl implements DeviceOrderService {

    @Autowired
    private DeviceOrderRepository deviceOrderRepository;

    @Override
    public Result findById(Integer id) {
        return Result.getSuccessResult(deviceOrderRepository.findById(id));
    }

    @Override
    public Result findByAll() {
        return Result.getSuccessResult(deviceOrderRepository.findAll());
    }

    @Override
    public Result findByOrderNumber(String orderNumber) {
        return Result.getSuccessResult(deviceOrderRepository.findByOrderNumber(orderNumber));
    }

}
