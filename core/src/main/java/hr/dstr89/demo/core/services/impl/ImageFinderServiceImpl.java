package hr.dstr89.demo.core.services.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.osgi.service.component.annotations.Component;

import com.day.cq.wcm.api.Page;

import hr.dstr89.demo.core.services.ImageFinderService;
import hr.dstr89.demo.core.utils.ResourceUtils;

@Component(service = ImageFinderService.class, immediate = true)
public class ImageFinderServiceImpl implements ImageFinderService {

    private static final String DEFAULT_IMAGE = "https://via.placeholder.com/319x212";
    private static final String IMAGE_COMPONENT_RESOURCE_TYPE = "demo-site/components/image";
    private static final String IMAGE_COMPONENT_FILE_REFERENCE = "fileReference";

    @Override
    public String getFirstImagePath(final String rendition, final Page articlePage) {
        final List<Resource> mainContentResources = ResourceUtils.getMainContentResources(articlePage);
        return mainContentResources
                 .stream()
                 .filter(resource -> resource.getResourceType().equals(IMAGE_COMPONENT_RESOURCE_TYPE))
                 .findFirst()
                 .map(Resource::getValueMap)
                 .map(properties -> properties.get(IMAGE_COMPONENT_FILE_REFERENCE, String.class))
                 .map(fileReference -> StringUtils.join(fileReference, rendition))
                 .orElse(DEFAULT_IMAGE);
    }

}