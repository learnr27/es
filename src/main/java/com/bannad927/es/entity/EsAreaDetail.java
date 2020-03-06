package com.bannad927.es.entity;

import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Document;

import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 *
 * </p>
 *
 * @author chengbinbin
 * @since 2019-12-19
 */
@Data
@Document(indexName = "t_area_detail", type = "_doc", useServerConfiguration = true, createIndex = false)
public class EsAreaDetail {

    private Integer id;

    private Integer contractId;

    private String areaCode;

    private Integer floor;

    private String roomNum;

    private Integer humanNum;

    private String constructionBatchCode;

    private String installBatchCode;

    private BigDecimal constructionCost;

    private Integer isPrivate;

    private Integer isExtra;

    private Integer state;

    private Integer deviceId;

    private Integer operator;

    private Integer isDel;

    private Date updateTime;

    private Date createTime;

    private Long washerId;

    private Long logId;


}
