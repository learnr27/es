package com.bannad927.es.mapper;


import com.bannad927.es.entity.AreaDetail;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *
 * </p>
 *
 * @author chengbinbin
 * @since 2019-12-19
 */
@Mapper
public interface AreaDetailMapper extends BaseMapper<AreaDetail> {

    /**
     * 根据设备id查询数据
     *
     * @param deviceId
     * @return
     */
    AreaDetail findByDeviceId(@Param("deviceId") Integer deviceId);

}
