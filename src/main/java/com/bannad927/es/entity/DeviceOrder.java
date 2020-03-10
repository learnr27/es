package com.bannad927.es.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * (TDeviceOrder)实体类
 *
 * @author cbb
 * @since 2020-03-05 11:39:45
 */
public class DeviceOrder implements Serializable {
    private static final long serialVersionUID = -32796026688310702L;
    //流水号
    private Long id;
    //内部订单号,唯一指定当前订单
    private String orderNumber;
    //订单名称
    private String orderName;
    //下单用户ID
    private Long userId;
    //设备id
    private Long deviceId;
    //设备型号
    private String deviceCode;
    //设备类型;1:洗衣机2:烘干机3:洗鞋机
    private Integer deviceType;
    //设备名称
    private String deviceName;
    //设备识别码,保证通过订单表信息可以直接操作洗衣机,记录下单时的洗衣机
    private String imei;
    //下单时的地区码
    private String areaCode;
    //下单时设备地址
    private String location;
    //对应合同ID
    private Long contractId;
    //是否私有设备?0:共有1:私有
    private Integer isPrivate;
    //下单是设备状态:是否额外设备?0:非额外设备1:额外设备
    private Integer isExtra;
    //模式类型,对应优惠券/营销的适用类型
    private Integer modelType;
    //模式编码,对应设备模式表的唯一建
    private String modelCode;
    //模式名称
    private String modelName;
    //洗衣分钟数
    private Integer workMinute;
    //工作剩余时间,订单异常时记录设备剩余工作时间
    private Integer surplusTime;
    //实际支付价格
    private BigDecimal price;
    //原价
    private BigDecimal originalPrice;
    //折扣金额
    private BigDecimal discountAmount;
    //余额支付金额
    private BigDecimal balanceAmount;
    //平台返利金额使用
    private BigDecimal giftAmount;
    //支付时间
    private Date payTime;
    //参考t_pay_way表
    private Integer payWay;
    //支付渠道方优惠金额
    private BigDecimal payDiscount;
    //支付状态 0：未支付 1：已支付
    private Integer payState;
    //0：app  1：微信h5
    private Integer platform;
    //0:已下单 1:工作中 2:已完成(退款不全退) 3：取消 4：洗衣机启动失败,5: 异常[二代才有的状态]6:退款中7:拒绝退款10:已退款,已退完
    private Integer state;
    //是否有退款记录,如果有退款记录则拉取退款详情,0:没有1:有
    private Integer refundState;
    //发生异常时间
    private Date errorTime;
    //设备异常时的工作状态
    private Integer errorState;
    //代理商展示状态,由合同表参数比例计算判断0:不展示1:展示
    private Integer agentDisplay;
    //优惠券ID
    private Long couponId;
    //冗余字段/优惠券名称:五折券
    private String couponName;
    //优惠券减免部分
    private BigDecimal couponDiscount;
    //参与的活动ID
    private Long activityId;
    //冗余字段,活动名称
    private String activityName;
    //活动减免金额
    private BigDecimal activityDiscount;
    //下单时间,用来做分库分表
    private Date orderTime;
    //洗衣机启动时间,
    private Date startTime;
    //洗衣机计划结束时间,用户点了开始就设置
    private Date endTime;
    //后台操作人员id
    private Integer backupStaff;
    //后台工作人员操作时间
    private Date backupTime;
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

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getOrderName() {
        return orderName;
    }

    public void setOrderName(String orderName) {
        this.orderName = orderName;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(Long deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceCode() {
        return deviceCode;
    }

    public void setDeviceCode(String deviceCode) {
        this.deviceCode = deviceCode;
    }

    public Integer getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(Integer deviceType) {
        this.deviceType = deviceType;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Long getContractId() {
        return contractId;
    }

    public void setContractId(Long contractId) {
        this.contractId = contractId;
    }

    public Integer getIsPrivate() {
        return isPrivate;
    }

    public void setIsPrivate(Integer isPrivate) {
        this.isPrivate = isPrivate;
    }

    public Integer getIsExtra() {
        return isExtra;
    }

    public void setIsExtra(Integer isExtra) {
        this.isExtra = isExtra;
    }

    public Integer getModelType() {
        return modelType;
    }

    public void setModelType(Integer modelType) {
        this.modelType = modelType;
    }

    public String getModelCode() {
        return modelCode;
    }

    public void setModelCode(String modelCode) {
        this.modelCode = modelCode;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public Integer getWorkMinute() {
        return workMinute;
    }

    public void setWorkMinute(Integer workMinute) {
        this.workMinute = workMinute;
    }

    public Integer getSurplusTime() {
        return surplusTime;
    }

    public void setSurplusTime(Integer surplusTime) {
        this.surplusTime = surplusTime;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(BigDecimal originalPrice) {
        this.originalPrice = originalPrice;
    }

    public BigDecimal getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(BigDecimal discountAmount) {
        this.discountAmount = discountAmount;
    }

    public BigDecimal getBalanceAmount() {
        return balanceAmount;
    }

    public void setBalanceAmount(BigDecimal balanceAmount) {
        this.balanceAmount = balanceAmount;
    }

    public BigDecimal getGiftAmount() {
        return giftAmount;
    }

    public void setGiftAmount(BigDecimal giftAmount) {
        this.giftAmount = giftAmount;
    }

    public Date getPayTime() {
        return payTime;
    }

    public void setPayTime(Date payTime) {
        this.payTime = payTime;
    }

    public Integer getPayWay() {
        return payWay;
    }

    public void setPayWay(Integer payWay) {
        this.payWay = payWay;
    }

    public BigDecimal getPayDiscount() {
        return payDiscount;
    }

    public void setPayDiscount(BigDecimal payDiscount) {
        this.payDiscount = payDiscount;
    }

    public Integer getPayState() {
        return payState;
    }

    public void setPayState(Integer payState) {
        this.payState = payState;
    }

    public Integer getPlatform() {
        return platform;
    }

    public void setPlatform(Integer platform) {
        this.platform = platform;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Integer getRefundState() {
        return refundState;
    }

    public void setRefundState(Integer refundState) {
        this.refundState = refundState;
    }

    public Date getErrorTime() {
        return errorTime;
    }

    public void setErrorTime(Date errorTime) {
        this.errorTime = errorTime;
    }

    public Integer getErrorState() {
        return errorState;
    }

    public void setErrorState(Integer errorState) {
        this.errorState = errorState;
    }

    public Integer getAgentDisplay() {
        return agentDisplay;
    }

    public void setAgentDisplay(Integer agentDisplay) {
        this.agentDisplay = agentDisplay;
    }

    public Long getCouponId() {
        return couponId;
    }

    public void setCouponId(Long couponId) {
        this.couponId = couponId;
    }

    public String getCouponName() {
        return couponName;
    }

    public void setCouponName(String couponName) {
        this.couponName = couponName;
    }

    public BigDecimal getCouponDiscount() {
        return couponDiscount;
    }

    public void setCouponDiscount(BigDecimal couponDiscount) {
        this.couponDiscount = couponDiscount;
    }

    public Long getActivityId() {
        return activityId;
    }

    public void setActivityId(Long activityId) {
        this.activityId = activityId;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public BigDecimal getActivityDiscount() {
        return activityDiscount;
    }

    public void setActivityDiscount(BigDecimal activityDiscount) {
        this.activityDiscount = activityDiscount;
    }

    public Date getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(Date orderTime) {
        this.orderTime = orderTime;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Integer getBackupStaff() {
        return backupStaff;
    }

    public void setBackupStaff(Integer backupStaff) {
        this.backupStaff = backupStaff;
    }

    public Date getBackupTime() {
        return backupTime;
    }

    public void setBackupTime(Date backupTime) {
        this.backupTime = backupTime;
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