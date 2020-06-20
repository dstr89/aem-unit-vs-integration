package hr.dstr89.demo.core.models;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.settings.SlingSettingsService;
import org.apache.sling.testing.mock.sling.ResourceResolverType;
import org.apache.sling.xss.XSSAPI;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.adobe.acs.commons.models.injectors.impl.AemObjectInjector;
import com.day.cq.commons.Externalizer;

import hr.dstr89.demo.core.data.ArticleItem;
import hr.dstr89.demo.core.mocks.MockExternalizer;
import hr.dstr89.demo.core.mocks.MockSlingSettingsService;
import hr.dstr89.demo.core.mocks.MockXSSAPI;
import hr.dstr89.demo.core.services.ImageFinderService;
import hr.dstr89.demo.core.services.TextExcerptsService;
import hr.dstr89.demo.core.services.impl.ImageFinderServiceImpl;
import hr.dstr89.demo.core.services.impl.LinkServiceImpl;
import hr.dstr89.demo.core.services.impl.TextExcerptsServiceImpl;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

@ExtendWith(AemContextExtension.class)
class ArticleListModelTest {

    private final AemContext context = new AemContext(ResourceResolverType.RESOURCERESOLVER_MOCK);
    private ArticleListModel model;

    @BeforeEach
    void setupContextAndAdaptModel() {
        // Register mocked AEM services required by the custom services under test
        this.context.registerService(SlingSettingsService.class, new MockSlingSettingsService(), new HashMap<>());
        this.context.registerService(Externalizer.class, new MockExternalizer(), new HashMap<>());
        this.context.registerService(XSSAPI.class, new MockXSSAPI(), new HashMap<>());

        // Register ASC commons AemObject injector
        this.context.registerInjectActivateService(new AemObjectInjector(), new HashMap<>());

        // Register custom implementation services to be tested as part of the model integration test
        this.context.registerService(TextExcerptsService.class, new TextExcerptsServiceImpl(), new HashMap<>());
        this.context.registerService(ImageFinderService.class, new ImageFinderServiceImpl(), new HashMap<>());
        this.context.registerInjectActivateService(new LinkServiceImpl(), new HashMap<>());

        // Load test resource pages
        this.context.load().json("/hr/dstr89/demo/core/root/us/en/en.json", "/content/demo-site/us/en");
        this.context.load().json("/hr/dstr89/demo/core/root/us/en/climbing/climbing.json", "/content/demo-site/us/en/climbing");
        this.context.load().json("/hr/dstr89/demo/core/root/us/en/surfing/surfing.json", "/content/demo-site/us/en/surfing");

        // Adapt model under test
        this.context.currentPage("/content/demo-site/us/en");
        this.model = this.context.request().adaptTo(ArticleListModel.class);
    }

    @Test
    @DisplayName("GIVEN that two articles are present in the content under the home page, "
                   + "WHEN article list component model is adapted from the home page, "
                   + "THEN the model returns two articles with their corresponding title, description, URL and image.")
    void testSurfingCategoryArticlePages() {
        final List<ArticleItem> articles = this.model.getArticles();
        final Optional<ArticleItem> surfingArticle = this.findArticleByTitle(articles, "Surfing");
        final Optional<ArticleItem> climbingArticle = this.findArticleByTitle(articles, "Climbing");

        if (surfingArticle.isPresent() && climbingArticle.isPresent()) {
            assertAll(
              () -> assertEquals(2, articles.size()),
              () -> assertEquals("Surfing", surfingArticle.get().getTitle()),
              () -> assertTrue(StringUtils.startsWith(surfingArticle.get().getDescription(), "Surfing is a surface water pastime in which the wave rider")),
              () -> assertEquals("https://localhost:4503/content/demo-site/us/en/surfing.html", surfingArticle.get().getUrl()),
              () -> assertEquals("/content/dam/demo-site/surfing.jpg/jcr%3Acontent/renditions/cq5dam.thumbnail.319.319.png", surfingArticle.get().getImage()),
              () -> assertEquals("Climbing", climbingArticle.get().getTitle()),
              () -> assertTrue(StringUtils.startsWith(climbingArticle.get().getDescription(), "Climbing is the activity of using one's hands, feet, or any other part of the body to ascend a steep topographical object")),
              () -> assertEquals("https://localhost:4503/content/demo-site/us/en/climbing.html", climbingArticle.get().getUrl()),
              () -> assertEquals("/content/dam/demo-site/climbing.jpg/jcr%3Acontent/renditions/cq5dam.thumbnail.319.319.png", climbingArticle.get().getImage())
            );
        } else {
            fail("Articles not found");
        }
    }

    private Optional<ArticleItem> findArticleByTitle(final List<ArticleItem> articles, final String surfing) {
        return articles.stream().filter(articleItem -> articleItem.getTitle().equals(surfing)).findFirst();
    }

}
