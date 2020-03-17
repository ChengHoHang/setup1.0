package com.chh.setup.myutils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author chh
 * @date 2020/2/19 15:06
 */
public class SetUtils {

    @SafeVarargs
    public static <T> Set<T> mergeToSet(List<T>... lists) {
        Set<T> set = new HashSet<>();
        for (List<T> ts : lists) {
            set.addAll(ts);
        }
        return set;
    }
}
