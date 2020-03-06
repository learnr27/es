package com.bannad927.es.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 区域表,精确到楼级别(TArea)实体类
 *
 * @author chengbb@xmulife.com
 * @since 2020-03-04 17:44:19
 */
public class Area implements Serializable {
    private static final long serialVersionUID = -89111506749570540L;
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


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getParentCode() {
        return parentCode;
    }

    public void setParentCode(String parentCode) {
        this.parentCode = parentCode;
    }

    public Integer getAreaLevel() {
        return areaLevel;
    }

    public void setAreaLevel(Integer areaLevel) {
        this.areaLevel = areaLevel;
    }

    public Integer getAreaType() {
        return areaType;
    }

    public void setAreaType(Integer areaType) {
        this.areaType = areaType;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public BigDecimal getLng() {
        return lng;
    }

    public void setLng(BigDecimal lng) {
        this.lng = lng;
    }

    public BigDecimal getLat() {
        return lat;
    }

    public void setLat(BigDecimal lat) {
        this.lat = lat;
    }

    public Integer getOldId() {
        return oldId;
    }

    public void setOldId(Integer oldId) {
        this.oldId = oldId;
    }

    public Integer getIsDel() {
        return isDel;
    }

    public void setIsDel(Integer isDel) {
        this.isDel = isDel;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

}