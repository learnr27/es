package com.bannad927.es.repository;

import com.bannad927.es.entity.EsDeviceOrder;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * @author chengbb@xmulife.com
 * @date 2020.3.5
 */
public interface DeviceOrderRepository extends ElasticsearchRepository<EsDeviceOrder, Integer> {

    EsDeviceOrder findByOrderNumber(String orderNumber);
}
