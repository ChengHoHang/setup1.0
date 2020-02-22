package com.chh.setup.myutils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author chh
 * @date 2020/2/19 15:06
 */
public class MapUtils {

    @SafeVarargs
    public static <K, V> Map<K, V> mergeMaps(Map<K, V>... maps) {
        Map<K, V> map = new HashMap<>();
        for (Map<K, V> kvMap : maps) {
            map.putAll(kvMap);
        }
        return map;
    }
}
