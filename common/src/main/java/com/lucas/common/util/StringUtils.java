package com.lucas.common.util;

/**
 * @author: ren
 * @program: small-spider-rpc
 * @description: String 工具类
 * @create: 2021-08-30 08:21
 **/
public class StringUtils {
    public static boolean isEmpty(String str){
        if(str==null || str.length() == 0)
        {
            return false;
        }
        return true;
    }
}
