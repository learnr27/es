package com.bannad927.es.service;

import com.bannad927.es.entity.Result;

/**
 * @author chengbb@xmulife.com
 * @date 2020.3.4
 */
public interface EsAreaService {

    Result findById(Integer id);

    Result findByAll();

    Result findByAreaCode(String areaCode);
}
