package com.fineelyframework.log.utils;

import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Objects;

public abstract class IpUtil {

    private static final String IPV4 = "ipv4";

    private static final String IPV6 = "ipv6";

    private IpUtil() {

    }

    public static long ipToLong(String ipv4) {
        String[] splits = ipv4.split("\\.");
        long l = 0;
        l = l + (Long.valueOf(splits[0], 10)) << 24;
        l = l + (Long.valueOf(splits[1], 10)) << 16;
        l = l + (Long.valueOf(splits[2], 10)) << 8;
        l = l + (Long.valueOf(splits[3], 10));
        return l;
    }

    public static BigInteger ipv6ToInt(String ipv6) {
        int compressIndex = ipv6.indexOf("::");
        if (compressIndex != -1) {
            String part1s = ipv6.substring(0, compressIndex);
            String part2s = ipv6.substring(compressIndex + 1);
            BigInteger part1 = ipv6ToInt(part1s);
            BigInteger part2 = ipv6ToInt(part2s);
            int part1hasDot = 0;
            char ch[] = part1s.toCharArray();
            for (char c : ch) {
                if (c == ':') {
                    part1hasDot++;
                }
            }
            return part1.shiftLeft(16 * (7 - part1hasDot)).add(part2);
        }
        String[] str = ipv6.split(":");
        BigInteger big = BigInteger.ZERO;
        for (int i = 0; i < str.length; i++) {
            //::1
            if (str[i].isEmpty()) {
                str[i] = "0";
            }
            big = big.add(BigInteger.valueOf(Long.valueOf(str[i], 16)).shiftLeft(16 * (str.length - i - 1)));
        }
        return big;
    }

    public static String getIpAddress(HttpServletRequest request) {
        if (Objects.isNull(request)) {
            return "";
        }
        String ip = request.getHeader("x-forwarded-for");
        if (StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        if (ip != null && ip.contains(",")) {
            String[] split = ip.split(",");
            ip = Arrays.stream(split).filter(s -> !"unknown".equalsIgnoreCase(s)).findFirst().orElse(ip);
        }
        if (StringUtils.isBlank(ip)) {
            ip = "127.0.0.1";
        }
        return ip;
    }

    public static String checkIpv4OrIpv6(String ip) {
        if (StringUtils.isBlank(ip)) {
            return null;
        }
        if (ip.contains(":")) {
            return IPV6;
        }
        return IPV4;
    }

}
