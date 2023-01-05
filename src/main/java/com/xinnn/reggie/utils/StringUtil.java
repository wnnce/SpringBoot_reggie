package com.xinnn.reggie.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class StringUtil {
    /**
     * 根据指定的分隔符将字符串转为数组
     * @param value 需要转为数组的字符串
     * @param split 分隔符
     * @return
     */
    public static List<String> splitString(String value, String split){
        List<String> list = new ArrayList<>();
        if (value.contains(split)){
            String[] values = value.split(split);
            list = Arrays.stream(values).toList();
        }else{
            list.add(value);
        }
        return list;
    }

    /**
     * 生成6位验证码
     * @return
     */
    public static String makeCode(){
        Random random = new Random();
        Integer num = random.nextInt(100000, 999999);
        return String.valueOf(num);
    }
}
