package com.bannad927.es.service.impl;


import com.bannad927.es.entity.AreaDetail;
import com.bannad927.es.entity.Result;
import com.bannad927.es.mapper.AreaDetailMapper;
import com.bannad927.es.service.AreaDetailService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;

/**
 * <p>
 *
 * </p>
 *
 * @author chengbinbin
 * @since 2019-12-19
 */
@Service
public class AreaDetailServiceImpl extends ServiceImpl<AreaDetailMapper, AreaDetail> implements AreaDetailService {

    @Autowired
    AreaDetailMapper areaDetailMapper;


    @Override
    public Result insert(AreaDetail areaDetail) {
        return Result.getSuccessResult(areaDetailMapper.insert(areaDetail));
    }

    @Override
    public Result update(AreaDetail areaDetail) {
        return Result.getSuccessResult(areaDetailMapper.updateById(areaDetail));
    }

    @Override
    public Result delete(Integer id) {
        return Result.getSuccessResult(areaDetailMapper.deleteById(id));
    }

    @Override
    public Result findById(Integer id) {
        return Result.getSuccessResult(areaDetailMapper.selectById(id));
    }

    @Override
    public Result findByAll() {
        StopWatch watch = new StopWatch();
        watch.start();
        QueryWrapper<AreaDetail> queryWrapper = new QueryWrapper<>();
        Iterable<AreaDetail> all = areaDetailMapper.selectList(queryWrapper);
        watch.stop();
        long millis = watch.getTotalTimeMillis();
        return Result.getSuccessResult(all, String.valueOf(millis));
    }


}
