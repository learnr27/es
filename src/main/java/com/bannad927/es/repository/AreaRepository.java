package com.bannad927.es.repository;

import com.bannad927.es.entity.EsArea;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * @author cbb
 * @date 2020.3.4
 */
public interface AreaRepository extends ElasticsearchRepository<EsArea,Integer> {

    EsArea findByAreaCode(String areaCode);

}
