package com.bannad927.es.entity;

import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Document;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 区域表,精确到楼级别(TArea)实体类
 *
 * @author chengbb@xmulife.com
 * @since 2020-03-04 17:44:19
 */

@Data
@Document(indexName = "t_area", type = "_doc", useServerConfiguration = true, createIndex = false)
public class EsArea  {

    //流水号
    private Long id;
    //地区码
    private String areaCode;
    //地区名
    private String areaName;
    //上级地区码
    private String parentCode;
    //地区等级:1:省/直辖市2:地级市3:学校/企业(校区/厂区)4:分校区/分公司5.楼号
    private Integer areaLevel;
    //类型:0地区1:学校，2：工厂/企业，3：酒店, 4：公寓
    private Integer areaType;
    //详细地址
    private String address;
    //经度
    private BigDecimal lng;
    //纬度
    private BigDecimal lat;
    //对应旧库的地址id
    private Integer oldId;
    //是否删除?0:未删除1:已删除
    private Integer isDel;
    //更新时间
    private Date updateTime;
    //创建时间
    private Date createTime;
}