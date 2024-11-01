package org.linlinjava.litemall.core.enums;

/**
 * 存储类型枚举
 *
 * @author by <a href="mailto:ligang941012@gmail.com">gang.Li</a>
 * @since 2024/11/1 14:42
 */
public enum StorageType {
    BANNER("banner"),
    AD("ad"),
    DEFAULT("default");

    private String storageType;

    // 构造器
    StorageType(String storageType) {
        this.storageType = storageType;
    }

    public String storageType() {
        return storageType;
    }

}
