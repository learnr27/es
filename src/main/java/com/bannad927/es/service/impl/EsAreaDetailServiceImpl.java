package com.bannad927.es.service.impl;


import com.bannad927.es.entity.EsAreaDetail;
import com.bannad927.es.entity.Result;
import com.bannad927.es.repository.AreaDetailRepository;
import com.bannad927.es.service.EsAreaDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * <p>
 *
 * </p>
 *
 * @author chengbinbin
 * @since 2019-12-19
 */
@Service
public class EsAreaDetailServiceImpl implements EsAreaDetailService {


    @Autowired
    private AreaDetailRepository areaDetailRepository;

    @Override
    public Result insert(EsAreaDetail areaDetail) {
        return Result.getSuccessResult(areaDetailRepository.save(areaDetail));
    }

    @Override
    public Result update(EsAreaDetail areaDetail) {
        return Result.getSuccessResult(areaDetailRepository.save(areaDetail));
    }

    @Override
    public Result delete(Integer id) {
        areaDetailRepository.deleteById(id);
        return Result.getSuccessResult();
    }

    @Override
    public Result findById(Integer id) {
        Optional<EsAreaDetail> result = areaDetailRepository.findById(id);
        if (result.isPresent()) {
            return Result.getSuccessResult(result.get());
        }
        return Result.getFailResult("查无数据");
    }

    @Override
    public Result findByAll() {
        StopWatch watch = new StopWatch();
        watch.start();
        List<EsAreaDetail> areaDetailList = new ArrayList<>();
        Iterable<EsAreaDetail> areaDetailIterable = areaDetailRepository.findAll();
        areaDetailIterable.forEach(areaDetailList::add);
        watch.stop();
        long millis = watch.getTotalTimeMillis();
        return Result.getSuccessResult(areaDetailList, String.valueOf(millis));
    }
}
