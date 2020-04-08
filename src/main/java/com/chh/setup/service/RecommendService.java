package com.chh.setup.service;

import com.chh.setup.dao.ArticleDao;
import com.chh.setup.dao.RecommendDao;
import com.chh.setup.model.RecommendModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author chh
 * @date 2020/4/8 17:58
 */
@Service
public class RecommendService {

    @Autowired
    private ArticleDao articleDao;

    @Autowired
    private RecommendDao recommendDao;

    public List<Object[]> recommendByUserId(Integer userId) {
        if (userId == null) {
            return recommendHot10();
        }
        RecommendModel recommendModel = recommendDao.findById(userId).orElse(null);
        if (recommendModel == null) {
            return recommendHot10();
        } else {
            List<Object[]> recommendList = new ArrayList<>();
            String[] recommendIds = recommendModel.getRecommendIds().split(",");
            for (String id : recommendIds) {
                String title = articleDao.getTitle(Integer.parseInt(id));
                recommendList.add(new Object[]{id, title});
            }
            return recommendList;
        }
    }

    public List<Object[]> recommendHot10() {
        return articleDao.recommendHot10();
    }
    
}
