package com.fineelyframework.log.strategy;


import org.apache.commons.text.StringSubstitutor;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 替换策略
 */
public abstract class ReplaceStrategy {

    protected Pattern pattern = Pattern.compile("\\$\\{(.*?)}");
    protected Pattern arrayPattern = Pattern.compile(".*?\\[\\d\\]");

    /**
     * 原始描述
     */
    protected String content;

    /**
     * 替换所需的map
     */
    protected Map<String, Object> paramMap = new HashMap<>();

    public abstract Termination execute(String content);

    public abstract void appendParamMap(String key, Object obj);

    public abstract void appendParamMap(Map<String, Object> paramMap);

    public Map<String, Object> getParamMap() {
        return paramMap;
    }

    public Object getParamObject(String key) {
        return paramMap.get(key);
    }

    /**
     * 处理描述，替换内容
     */
    protected String handleContent(Map<String, Object> result, String str) {
        StringSubstitutor stringSubstitutor = new StringSubstitutor(result);
        return stringSubstitutor.replace(str);
    }

    /**
     * 判断是否为数组取值, 并返回数组下标
     *
     * @param value 判断值
     * @return 不为数组返回-1
     */
    protected int getArrayIndex(String value) {
        Matcher matcher = arrayPattern.matcher(value);
        if (matcher.find()) {
            String index = value.replaceAll(".*?\\[", "").replaceAll("]", "");
            return Integer.parseInt(index);
        } else {
            return -1;
        }
    }
}
