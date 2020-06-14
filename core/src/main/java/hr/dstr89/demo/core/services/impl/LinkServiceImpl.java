package hr.dstr89.demo.core.services.impl;

import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.settings.SlingSettingsService;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.day.cq.commons.Externalizer;
import com.day.cq.commons.feed.Feed;

import hr.dstr89.demo.core.services.LinkService;

@Component(service = LinkService.class, immediate = true)
public class LinkServiceImpl implements LinkService {

    @Reference
    private SlingSettingsService slingSettingsService;

    @Reference
    private Externalizer externalizer;

    @Override
    public String getExternalLink(final ResourceResolver resourceResolver, final String path) {
        final String link = this.isPublishServer(this.slingSettingsService) ?
                              this.externalizer.publishLink(resourceResolver, path) :
                              this.externalizer.authorLink(resourceResolver, path);
        return this.getLinkWithHtmlSuffix(link);
    }

    private String getLinkWithHtmlSuffix(final String link) {
        return link.endsWith(Feed.SUFFIX_HTML) ? link : StringUtils.join(link, Feed.SUFFIX_HTML);
    }

    private boolean isPublishServer(final SlingSettingsService slingSettingsService) {
        return Optional.ofNullable(slingSettingsService).map(settings -> settings.getRunModes().contains(Externalizer.PUBLISH)).orElse(false);
    }

}