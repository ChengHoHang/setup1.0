package com.chh.setup.service;

import com.chh.setup.dto.res.TagDto;
import com.chh.setup.enums.tag.EntertainmentTag;
import com.chh.setup.enums.tag.GeneralTag;
import com.chh.setup.enums.tag.InternetTag;
import com.chh.setup.enums.tag.SportTag;

import java.util.ArrayList;
import java.util.List;

/**
 * @author chh
 * @date 2020/2/19 18:11
 */
public class TagService {

    private static List<TagDto> tags = new ArrayList<>();

    static {
        tags.add(new TagDto("general", "通用",  GeneralTag.getRemarks()));
        tags.add(new TagDto("internet", "互联网", InternetTag.getRemarks()));
        tags.add(new TagDto("entertainment", "娱乐", EntertainmentTag.getRemarks()));
        tags.add(new TagDto("sport", "体育", SportTag.getRemarks()));
    }
    
    public static List<TagDto> showTags() {
        return tags;
    }
    
}
