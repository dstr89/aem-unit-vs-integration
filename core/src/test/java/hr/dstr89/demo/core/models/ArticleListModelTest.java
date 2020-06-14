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

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.spi.Injector;
import org.apache.sling.settings.SlingSettingsService;
import org.apache.sling.testing.mock.sling.ResourceResolverType;
import org.apache.sling.xss.XSSAPI;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.adobe.acs.commons.models.injectors.impl.AemObjectInjector;
import com.day.cq.commons.Externalizer;
import com.day.cq.wcm.api.Page;

import hr.dstr89.demo.core.data.ArticleItem;
import hr.dstr89.demo.core.mocks.MockExternalizer;
import hr.dstr89.demo.core.mocks.MockSlingSettingsService;
import hr.dstr89.demo.core.mocks.MockXSSAPI;
import hr.dstr89.demo.core.services.ImageFinderService;
import hr.dstr89.demo.core.services.LinkService;
import hr.dstr89.demo.core.services.TextExcerptsService;
import hr.dstr89.demo.core.services.impl.ImageFinderServiceImpl;
import hr.dstr89.demo.core.services.impl.LinkServiceImpl;
import hr.dstr89.demo.core.services.impl.TextExcerptsServiceImpl;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Simple JUnit test verifying the HelloWorldModel
 */
@ExtendWith(AemContextExtension.class)
class ArticleListModelTest {

    private final AemContext context = new AemContext(ResourceResolverType.RESOURCERESOLVER_MOCK);
    private ArticleListModel model;

    @BeforeEach
    void setupContextAndAdaptModel() {
        // Register ASC commons AemObject injector
        this.context.registerInjectActivateService(AemObjectInjector.class, new AemObjectInjector(), new HashMap<>());

        // Register mocked AEM services required by the custom services under test
        this.context.registerService(SlingSettingsService.class, new MockSlingSettingsService(), new HashMap<>());
        this.context.registerService(Externalizer.class, new MockExternalizer(), new HashMap<>());
        this.context.registerService(XSSAPI.class, new MockXSSAPI(), new HashMap<>());

        // Register custom implementation services to be tested as part of the model integration test
        this.context.registerService(TextExcerptsService.class, new TextExcerptsServiceImpl(), new HashMap<>());
        this.context.registerService(ImageFinderService.class, new ImageFinderServiceImpl(), new HashMap<>());
        this.context.registerInjectActivateService(LinkService.class, new LinkServiceImpl(), new HashMap<>());

        // Load test resource pages
        this.context.load().json("/hr/dstr89/demo/core/root/us/en/surfing/climbing.json", "/content/demo-site/us/en/climbing");
        this.context.load().json("/hr/dstr89/demo/core/root/us/en/surfing/surfing.json", "/content/demo-site/us/en/surfing");

        // Adapt model under test
        this.model = this.context.request().adaptTo(ArticleListModel.class);
    }

    @Test
    @DisplayName("GIVEN that ... "
                   + "WHEN asd ... "
                   + "THEN asd ...")
    void testSurfingCategoryArticlePages() {
        final List<ArticleItem> articles = this.model.getArticles();
        final Optional<ArticleItem> surfingArticle = articles.stream().filter(articleItem -> articleItem.getTitle().equals("Surfing")).findFirst();

        assertAll(() -> assertEquals(2, articles.size()));
    }

}
