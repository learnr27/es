package com.bannad927.es.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 *
 * </p>
 *
 * @author chengbinbin
 * @since 2019-12-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_area_detail")
public class AreaDetail extends Model<AreaDetail> {

    private static final long serialVersionUID = 1L;

    /**
     * 流水号
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 合同ID
     */
    private Integer contractId;

    /**
     * 地区码
     */
    private String areaCode;

    /**
     * 楼层
     */
    private Integer floor;

    /**
     * 宿舍号
     */
    private String roomNum;

    /**
     * 宿舍人数
     */
    private Integer humanNum;

    /**
     * 水电改造批次号(空为未改造)
     */
    private String constructionBatchCode;

    /**
     * 安装批次号(空为未安装)
     */
    private String installBatchCode;

    /**
     * 单间水电改造花费
     */
    private BigDecimal constructionCost;

    /**
     * 是否私有?0:公有1:私有
     */
    private Integer isPrivate;

    /**
     * 是否额外设备?0:非额外设备1:额外设备
     */
    private Integer isExtra;

    /**
     * 状态0:正常1:禁用
     */
    private Integer state;

    /**
     * 设备ID
     */
    private Integer deviceId;

    private Integer operator;

    /**
     * 是否删除?0:未删除1:已删除
     */
    private Integer isDel;

    /**
     * 更新时间
     */
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private LocalDateTime updateTime;

    /**
     * 创建时间
     */
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private LocalDateTime createTime;

    private Long washerId;

    private Long logId;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
