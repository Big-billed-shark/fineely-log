package com.fineelyframework.log.strategy;


import org.apache.commons.text.StringSubstitutor;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class ReplaceStrategy {

    protected Pattern pattern = Pattern.compile("\\$\\{(.*?)}");
    protected Pattern arrayPattern = Pattern.compile(".*?\\[\\d\\]");

    protected String content;

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

    protected String handleContent(Map<String, Object> result, String str) {
        StringSubstitutor stringSubstitutor = new StringSubstitutor(result);
        return stringSubstitutor.replace(str);
    }

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
