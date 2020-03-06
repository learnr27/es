package com.bannad927.es.service.impl;

import com.bannad927.es.entity.Result;
import com.bannad927.es.repository.AreaDetailRepository;
import com.bannad927.es.repository.AreaRepository;
import com.bannad927.es.service.EsAreaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author chengbb@xmulife.com
 * @date 2020.3.4
 */
@Service
public class EsAreaServiceImpl implements EsAreaService {

    @Autowired
    private AreaRepository areaRepository;

    @Override
    public Result findById(Integer id) {
        return Result.getSuccessResult(areaRepository.findById(id));
    }

    @Override
    public Result findByAll() {
        return Result.getSuccessResult(areaRepository.findAll());
    }

    @Override
    public Result findByAreaCode(String areaCode) {
        return Result.getSuccessResult(areaRepository.findByAreaCode(areaCode));
    }
}
