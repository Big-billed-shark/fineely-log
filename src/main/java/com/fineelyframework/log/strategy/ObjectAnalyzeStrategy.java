package com.fineelyframework.log.strategy;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 对象解析策略
 * 通过参数map中的对象中所有的属性 构建成 可替换map，进行替换
 * 测试在属性层级3层中 6个属性值为 8-9ms
 * 在属性少的情况下时间越短
 * 暂时不支持数组不推荐使用
 */
public class ObjectAnalyzeStrategy extends ReplaceStrategy implements Termination {

    @Override
    public Termination execute(String content) {
        this.content = content;
        return this;
    }

    @Override
    public void appendParamMap(String key, Object obj) {
        handleLevel(key, paramMap, new HashMap<>((JSONObject) obj));
    }

    @Override
    public void appendParamMap(Map<String, Object> paramMap) {
        handleLevel("", this.paramMap, paramMap);
    }

    @Override
    public String complete() {
        return handleContent(paramMap, content);
    }

    /**
     * 递归通过参数map构建可替换的map
     */
    private Map<String, Object> handleLevel(String placeholder, Map<String, Object> parameter, Map<String, Object> map) {
        if (Objects.nonNull(map) && !map.isEmpty()) {
            map.forEach((key, value) -> {
                String pla = StringUtils.isBlank(placeholder) ? key : placeholder + "." + key;
                parameter.put(pla, value);
                if (value instanceof JSONObject) {
                    handleLevel(pla, parameter, new HashMap<>(((JSONObject) value)));
                }
            });
        }
        return parameter;
    }
}
