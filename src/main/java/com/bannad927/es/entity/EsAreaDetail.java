package com.bannad927.es.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serializable;
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
public class EsAreaDetail implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 流水号
     */
    @Id()
    private Integer id;

    /**
     * 合同ID
     */
    @Field(type=FieldType.Integer)
    private Integer contractId;

    /**
     * 地区码
     */
    @Field
    private String areaCode;

    /**
     * 楼层
     */
    @Field(type=FieldType.Integer)
    private Integer floor;

    /**
     * 宿舍号
     */
    @Field
    private String roomNum;

    /**
     * 宿舍人数
     */
    @Field(type=FieldType.Integer)
    private Integer humanNum;

    /**
     * 水电改造批次号(空为未改造)
     */
    @Field
    private String constructionBatchCode;

    /**
     * 安装批次号(空为未安装)
     */
    @Field
    private String installBatchCode;

    /**
     * 单间水电改造花费
     */
    @Field
    private BigDecimal constructionCost;

    /**
     * 是否私有?0:公有1:私有
     */
    @Field
    private Integer isPrivate;

    /**
     * 是否额外设备?0:非额外设备1:额外设备
     */
    @Field
    private Integer isExtra;

    /**
     * 状态0:正常1:禁用
     */
    @Field
    private Integer state;

    /**
     * 设备ID
     */
    @Field
    private Integer deviceId;

    @Field
    private Integer operator;

    /**
     * 是否删除?0:未删除1:已删除
     */
    @Field
    private Integer isDel;

    /**
     * 更新时间
     */
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @Field
    private Date updateTime;

    /**
     * 创建时间
     */
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @Field
    private Date createTime;

    @Field
    private Long washerId;

    @Field
    private Long logId;


}
