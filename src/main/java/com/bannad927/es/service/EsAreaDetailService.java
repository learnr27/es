package com.bannad927.es.service;

import com.bannad927.es.entity.EsAreaDetail;
import com.bannad927.es.entity.Result;

/**
 * <p>
 *
 * </p>
 *
 * @author chengbinbin
 * @since 2019-12-19
 */
public interface EsAreaDetailService {

    Result insert(EsAreaDetail areaDetail);


    Result update(EsAreaDetail areaDetail);


    Result delete(Integer id);

    Result findById(Integer id);


    Result findByAll();
}
