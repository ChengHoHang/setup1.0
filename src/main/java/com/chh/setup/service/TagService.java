package com.chh.setup.service;

import com.chh.setup.dto.TagNotes;
import com.chh.setup.enums.tag.EntertainmentTag;
import com.chh.setup.enums.tag.GeneralTag;
import com.chh.setup.enums.tag.InternetTag;
import com.chh.setup.enums.tag.SportTag;
import com.chh.setup.myutils.MapUtils;
import org.apache.commons.lang3.StringUtils;

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

    public static List<TagNotes> showTags() {
        List<TagNotes> list = new ArrayList<>();
        list.add(new TagNotes("general", "通用",  GeneralTag.getDtos()));
        list.add(new TagNotes("internet", "互联网", InternetTag.getDtos()));
        list.add(new TagNotes("entertainment", "娱乐", EntertainmentTag.getDtos()));
        list.add(new TagNotes("sport", "体育", SportTag.getDtos()));
        return list;
    }
    
    public static boolean isInvalid(String tags) {
        Set<String> scheduledTags = TagService.getIdMap().keySet();
        String[] splitTags = StringUtils.split(tags, ",");
        return !scheduledTags.containsAll(Arrays.asList(splitTags));
    }
}
