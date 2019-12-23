package com.bannad927.es.service;

import com.bannad927.es.entity.AreaDetail;
import com.bannad927.es.entity.Result;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *
 * </p>
 *
 * @author chengbinbin
 * @since 2019-12-19
 */
public interface AreaDetailService extends IService<AreaDetail> {


    Result insert(AreaDetail areaDetail);


    Result update(AreaDetail areaDetail);


    Result delete(Integer id);

    Result findById(Integer id);


    Result findByAll();


}
