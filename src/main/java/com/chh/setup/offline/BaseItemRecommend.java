package com.chh.setup.offline;

import com.chh.setup.model.ArticleModel;
import com.chh.setup.model.RelatedArticleModel;
import com.chh.setup.dao.ArticleDao;
import com.chh.setup.dao.RelatedArticleDao;
import com.chh.setup.service.SearchService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * @author chh
 * @date 2020/4/5 16:51
 */
@Component
public class BaseItemRecommend {

    @Autowired
    private SearchService searchService;

    @Autowired
    private ArticleDao articleDao;

    @Autowired
    private RelatedArticleDao relatedArticleDao;
    
    @Scheduled(cron = "0 0 0/3 * * ? ")
    public void BM25Related() {
        System.out.println("task start at" + new Date());
        
        List<ArticleModel> articles = articleDao.findAll();
        articles.forEach(article -> {
            List<Integer> relatedIds = null;
            try {
                relatedIds = searchService.getRelatedArticle(article.getId(), article.getTitle(), article.getTag());
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (relatedIds != null && relatedIds.size() > 0) {
                String relatedStr = StringUtils.join(relatedIds, ",");
                RelatedArticleModel relatedArticleModel = new RelatedArticleModel();
                relatedArticleModel.setArticleId(article.getId());
                relatedArticleModel.setRelatedIds(relatedStr);
                relatedArticleDao.save(relatedArticleModel);
            }
        });
    }
    
}
