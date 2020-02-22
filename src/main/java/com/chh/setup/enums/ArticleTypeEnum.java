package com.chh.setup.enums;

/**
 * @author chh
 * @date 2020/1/10 21:44
 */
public enum ArticleTypeEnum {
    ALL("全部", 0),
    NATIONAL("国内", 1),
    INTERNATIONAL("国际", 2),
    TECHNOLOGY("互联网", 3),
    ENTERTAINMENT("娱乐", 4),
    SPORTS("体育", 5);

    private String name;
    private Integer type;

    ArticleTypeEnum(String name, int type) {
        this.name = name;
        this.type = type;
    }
    
    public String getName() {
        return name;
    }

    public Integer getType() {
        return type;
    }

    /**
     * 根据前端传入的新闻类型字符串参数转换成整型
     * @param name
     * @return
     */
    public static Integer getType(String name) {
        for (ArticleTypeEnum value : ArticleTypeEnum.values()) {
            if (value.name.equals(name)) {
                return value.type;
            }
        }
        return -1;
    }

    /**
     * 根据后端返回的新闻类型整型参数转换成字符串
     * @param type
     * @return
     */
    public static String getName(int type) {
        for (ArticleTypeEnum value : ArticleTypeEnum.values()) {
            if (value.type == type) {
                return value.name;
            }
        }
        return "";
    }
    
    public static boolean isExist(String name) {
        for (ArticleTypeEnum value : ArticleTypeEnum.values()) {
            if (value.name.equals(name)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isExist(Integer type) {
        if (type == null || type < 0) {
            return false;
        }
        for (ArticleTypeEnum value : ArticleTypeEnum.values()) {
            if (value.type.equals(type)) {
                return true;
            }
        }
        return false;
    }
}