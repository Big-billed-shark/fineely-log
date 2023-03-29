package com.fineelyframework.log.utils;

import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.math.BigInteger;
import java.util.Arrays;

/**
 * IP地址工具类
 */
public abstract class IpUtil {

    private static final String IPV4 = "ipv4";

    private static final String IPV6 = "ipv6";

    private IpUtil() {

    }

    /**
     * ipv4字符串转为long
     */
    public static long ipToLong(String ipv4) {
        String[] splits = ipv4.split("\\.");
        long l = 0;
        l = l + (Long.valueOf(splits[0], 10)) << 24;
        l = l + (Long.valueOf(splits[1], 10)) << 16;
        l = l + (Long.valueOf(splits[2], 10)) << 8;
        l = l + (Long.valueOf(splits[3], 10));
        return l;
    }

    /**
     * long转为ipv4字符串
     */
    public static String longToIp(long l) {
        String ip = "";
        ip = ip + (l >>> 24);
        ip = ip + "." + ((0x00ffffff & l) >>> 16);
        ip = ip + "." + ((0x0000ffff & l) >>> 8);
        ip = ip + "." + (0x000000ff & l);
        return ip;
    }

    /**
     * ipv6字符串转BigInteger数
     */
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

    /**
     * BigInteger数 转为ipv6字符串
     */
    public static String intToIpv6(BigInteger big) {
        String str = "";
        BigInteger ff = BigInteger.valueOf(0xffff);
        for (int i = 0; i < 8; i++) {
            str = big.and(ff).toString(16) + ":" + str;
            big = big.shiftRight(16);
        }
        //去掉最后的：号
        str = str.substring(0, str.length() - 1);
        return str.replaceFirst("(^|:)(0+(:|$)){2,8}", "::");
    }

    /**
     * 将精简的ipv6地址扩展为全长度的ipv6地址
     */
    public static String completeIpv6(String strIpv6) {
        BigInteger big = ipv6ToInt(strIpv6);
        String str = big.toString(16);
        String completeIpv6Str = "";
        while (str.length() != 32) {
            str = "0" + str;
        }
        for (int i = 0; i <= str.length(); i += 4) {
            completeIpv6Str += str.substring(i, i + 4);
            if ((i + 4) == str.length()) {
                break;
            }
            completeIpv6Str += ":";
        }
        return completeIpv6Str;
    }

    /**
     * 获取用户真实IP地址，不使用request.getRemoteAddr();的原因是有可能用户使用了代理软件方式避免真实IP地址,
     * <p>
     * 可是，如果通过了多级反向代理的话，X-Forwarded-For的值并不止一个，而是一串IP值，究竟哪个才是真正的用户端的真实IP呢？
     * 答案是取X-Forwarded-For中第一个非unknown的有效IP字符串。
     * <p>
     * 如：X-Forwarded-For：192.168.1.110, 192.168.1.120, 192.168.1.130,
     * 192.168.1.100
     * <p>
     * 用户真实IP为： 192.168.1.110
     *
     * @param request
     * @return
     */
    public static String getIpAddress(HttpServletRequest request) {
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

    public static boolean validateIp(String ip, long start, long end) {
        long ipLong = 0;
        String ipType = checkIpv4OrIpv6(ip);
        if (IPV4.equals(ipType)) {
            ipLong = ipToLong(ip);
        }
        if (IPV6.equals(ipType)) {
            ipLong = ipv6ToInt(ip).longValue();
        }
        return ipLong >= start && ipLong <= end;
    }

}
