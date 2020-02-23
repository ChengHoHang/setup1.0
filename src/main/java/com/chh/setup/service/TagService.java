package com.chh.setup.service;

import com.chh.setup.dto.TagDto;
import com.chh.setup.enums.tag.EntertainmentTag;
import com.chh.setup.enums.tag.GeneralTag;
import com.chh.setup.enums.tag.InternetTag;
import com.chh.setup.enums.tag.SportTag;
import com.chh.setup.myutils.MapUtils;

import java.util.*;

/**
 * @author chh
 * @date 2020/2/19 18:11
 */
public class TagService {
    
    public static Map<String, String> getIdMap() {
        return MapUtils.mergeMaps(GeneralTag.getMap(), InternetTag.getMap(),
                EntertainmentTag.getMap(), SportTag.getMap()); //新加的分类要在这后面加上,之后看看怎么重构好一点
    }

    public static Map<String, String> getRemarkMap() {
        Map<String, String> reMarkMap = new HashMap<>();
        getIdMap().forEach((key, value) -> reMarkMap.put(value, key));
        return reMarkMap;
    }

    public static List<TagDto> showTags() {
        List<TagDto> list = new ArrayList<>();
        list.add(new TagDto("general", "通用",  GeneralTag.getRemarks()));
        list.add(new TagDto("internet", "互联网", InternetTag.getRemarks()));
        list.add(new TagDto("entertainment", "娱乐", EntertainmentTag.getRemarks()));
        list.add(new TagDto("sport", "体育", SportTag.getRemarks()));
        return list;
    }
    
    public static boolean isInvalid(String[] tags) {
        Set<String> scheduledTags = new HashSet<>(TagService.getIdMap().values());
        return !scheduledTags.containsAll(Arrays.asList(tags));
    }
}
