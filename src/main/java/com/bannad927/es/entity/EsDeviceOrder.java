package com.bannad927.es.entity;

import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Document;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * (TDeviceOrder)实体类
 *
 * @author cbb
 * @since 2020-03-05 11:39:45
 */
@Data
@Document(indexName = "t_device_order", type = "_doc", useServerConfiguration = true, createIndex = false)
public class EsDeviceOrder  {
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
}