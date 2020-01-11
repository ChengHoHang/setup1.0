package com.chh.setup.enums;

/**
 * @author chh
 * @date 2020/1/10 21:44
 */
public enum ArticleTypeEnum {
    NATIONAL("国内", 1),
    INTERNATIONAL("国际", 2),
    TECHNOLOGY("科技", 3);

    private String name;
    private int type;

    ArticleTypeEnum(String name, int type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public int getType() {
        return type;
    }

    /**
     * 根据前端传入的新闻类型字符串参数转换成整型
     *
     * @param name
     * @return
     */
    public static int getType(String name) {
        for (ArticleTypeEnum value : ArticleTypeEnum.values()) {
            if (value.name.equals(name)) {
                return value.type;
            }
        }
        return -1;
    }
}