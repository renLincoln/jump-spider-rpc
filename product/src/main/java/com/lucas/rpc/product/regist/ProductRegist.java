package com.lucas.rpc.product.regist;

import com.alibaba.fastjson.JSONObject;
import com.lucas.common.constant.RedisKey;
import com.lucas.jedis.util.RedisUtil;
import com.luncas.common.dto.ProductInfoDto;

/**
 * @author: ren
 * @program: jump-spider-rpc
 * @description: 服务注册
 * @create: 2021-09-12 18:24
 **/
public class ProductRegist {
    /**
     * @Author: ren
     * @Description: 向注册中心注册服务
     * @Param: [serviceName, productInfoDto]
     * @return: boolean
     * @Date: 2021/9/12
     */
    public static boolean registProduct(String serviceName, ProductInfoDto productInfoDto) {
        return "OK".equals(RedisUtil.sadd( RedisKey.SERVICE_NAME + serviceName, JSONObject.toJSONString(productInfoDto)));
    }
}
