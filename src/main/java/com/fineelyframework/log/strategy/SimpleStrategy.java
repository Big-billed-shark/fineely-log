package com.fineelyframework.log.strategy;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;

/**
 * 简单策略
 * 测试 平均8、9ms
 * 综合平均值
 */
public class SimpleStrategy extends ReplaceStrategy implements Termination {

    @Override
    public Termination execute(String content) {
        this.content = content;
        return this;
    }

    @Override
    public void appendParamMap(String key, Object obj) {
        paramMap.put(key, obj);
    }

    @Override
    public void appendParamMap(Map<String, Object> paramMap) {
        this.paramMap.putAll(paramMap);
    }

    @Override
    public String complete() {
        return resolveParamMap();
    }

    private String resolveParamMap() {
        Map<String, Object> result = new HashMap<>();
        Matcher matcher = pattern.matcher(content);
        // 处理匹配到的值
        while (matcher.find()) {
            String group = matcher.group();
            // 移除"${"   和   "}"
            group = group.replace("${", "").replace("}", "");
            if (result.containsKey(group)) {
                break;
            }
            // 以.切割
            String[] split = group.split("\\.");
            Object parent = null;
            StringBuilder parentKey = null;
            for (String s : split) {
                int arrayIndex = getArrayIndex(s);
                String key = arrayIndex != -1 ? s.replaceAll("\\[.*", "") : s;
                // 有序树父节点为空时创建父节点
                if (Objects.isNull(parent)) {
                    // 找树根是否存在过
                    parent = paramMap.get(key);
                    if (arrayIndex != -1 && parent instanceof JSONArray) {
                        parent = ((JSONArray) parent).get(arrayIndex);
                    }
                    parentKey = new StringBuilder(s);
                    result.put(s, parent);
                    // 最顶层没有，意味着里面同样没有
                    if (Objects.isNull(parent))
                        break;
                } else {
                    HashMap<String, Object> parentMap = new HashMap<>((JSONObject) parent);
                    parent = parentMap.get(key);
                    parentKey.append(".").append(s);
                    if (arrayIndex != -1 && parent instanceof JSONArray) {
                        parent = ((JSONArray) parent).get(arrayIndex);
                    }
                    result.put(parentKey.toString(), parent);
                }
            }
        }
        return handleContent(result, content);
    }
}
