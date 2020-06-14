package hr.dstr89.demo.core.services.impl;

import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.osgi.service.component.annotations.Component;

import com.day.cq.wcm.api.Page;

import hr.dstr89.demo.core.services.TextExcerptsService;
import hr.dstr89.demo.core.utils.ResourceUtils;

@Component(service = TextExcerptsService.class, immediate = true)
public class TextExcerptsServiceImpl implements TextExcerptsService {

    private static final String MATCH_HTML_TAGS_REGEX = "<[^>]*>";
    private static final String TEXT_COMPONENT_RESOURCE_TYPE = "demo-site/components/text";
    private static final String TEXT_COMPONENT_TEXT_PROPERTY = "text";

    private static final Pattern htmlTagsPattern = Pattern.compile(MATCH_HTML_TAGS_REGEX);

    @Override
    public String getTextExcerpts(final Page articlePage, final int maxLength) {
        final List<Resource> mainContentResources = ResourceUtils.getMainContentResources(articlePage);
        return mainContentResources
                 .stream()
                 .filter(resource -> resource.getResourceType().equals(TEXT_COMPONENT_RESOURCE_TYPE))
                 .findFirst()
                 .map(Resource::getValueMap)
                 .map(properties -> properties.get(TEXT_COMPONENT_TEXT_PROPERTY, String.class))
                 .map(text -> StringUtils.abbreviate(htmlTagsPattern.matcher(text).replaceAll(StringUtils.EMPTY), maxLength))
                 .orElse(StringUtils.EMPTY);
    }

}
