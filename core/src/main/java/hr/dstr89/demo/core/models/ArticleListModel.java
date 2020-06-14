/*
 *  Copyright 2015 Adobe Systems Incorporated
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package hr.dstr89.demo.core.models;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.annotation.PostConstruct;

import org.apache.commons.collections4.IteratorUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;

import com.adobe.acs.commons.models.injectors.annotation.AemObject;
import com.day.cq.wcm.api.Page;

import hr.dstr89.demo.core.data.ArticleItem;
import hr.dstr89.demo.core.services.ImageFinderService;
import hr.dstr89.demo.core.services.LinkService;
import hr.dstr89.demo.core.services.TextExcerptsService;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Model(adaptables = SlingHttpServletRequest.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class ArticleListModel {

    private static final String IMAGE_THUMBNAIL_RENDITION_SUFFIX = "/jcr%3Acontent/renditions/cq5dam.thumbnail.319.319.png";
    private static final int ARTICLE_DESCRIPTION_LENGTH = 250;

    @SlingObject
    private ResourceResolver resourceResolver;

    @AemObject
    private Page currentPage;

    @OSGiService
    private TextExcerptsService textExcerptsService;

    @OSGiService
    private ImageFinderService imageFinderService;

    @OSGiService
    private LinkService linkService;

    @Getter
    private List<ArticleItem> articles;

    @PostConstruct
    public void init() {
        try {
            final List<Page> childPages = IteratorUtils.toList(this.currentPage.listChildren());
            this.articles = IntStream
                              .range(0, childPages.size())
                              .mapToObj(index -> this.createArticleItem(childPages.get(index), index))
                              .collect(Collectors.toList());
        } catch (final Exception e) {
            log.error("Exception in post construct", e);
        }
    }

    private ArticleItem createArticleItem(final Page articlePage, final int index) {
        final String title = articlePage.getTitle();
        final String image = this.imageFinderService.getFirstImagePath(IMAGE_THUMBNAIL_RENDITION_SUFFIX, articlePage);
        final String description = this.textExcerptsService.getTextExcerpts(articlePage, ARTICLE_DESCRIPTION_LENGTH);
        final String link = this.linkService.getExternalLink(this.resourceResolver, articlePage.getPath());
        return ArticleItem.builder().title(title).image(image).url(link).description(description).build();
    }

}
