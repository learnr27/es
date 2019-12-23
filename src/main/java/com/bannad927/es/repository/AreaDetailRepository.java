package com.bannad927.es.repository;

import com.bannad927.es.entity.EsAreaDetail;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * @author chengbb
 * @date 2019.12.20
 */
public interface AreaDetailRepository extends ElasticsearchRepository<EsAreaDetail, Integer> {
}
