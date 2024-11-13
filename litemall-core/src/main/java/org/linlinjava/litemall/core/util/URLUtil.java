package org.linlinjava.litemall.core.util;

import org.linlinjava.litemall.core.storage.config.StorageProperties;

import java.net.InetAddress;

import static org.linlinjava.litemall.core.consts.AdminConstant.HTTP_COLON;
import static org.linlinjava.litemall.core.consts.AdminConstant.HTTP_PREFIX;

/**
 * URL utility methods
 *
 * @author by <a href="mailto:ligang941012@gmail.com">gang.Li</a>
 * @since 2024/11/13 10:13
 */
public class URLUtil {

    /**
     * 拼接本地文件访问地址
     *
     * @param serverIp 服务器ip
     * @param local    local对象
     * @return 生成存在http的地址
     */
    public static String fullUrl(StorageProperties.Local local) {
        String serverIp = "";
        try {
            serverIp = InetAddress.getLocalHost().getHostAddress();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        return HTTP_PREFIX + serverIp + HTTP_COLON + local.getAddress();
    }

    public static String fullUrl(String url) {
        String serverIp = "";
        try {
            serverIp = InetAddress.getLocalHost().getHostAddress();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        return HTTP_PREFIX + serverIp + HTTP_COLON + url;
    }
}
