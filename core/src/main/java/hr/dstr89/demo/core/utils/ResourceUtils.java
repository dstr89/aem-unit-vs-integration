package hr.dstr89.demo.core.utils;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.apache.commons.collections4.IteratorUtils;
import org.apache.sling.api.resource.Resource;

import com.day.cq.wcm.api.Page;

public final class ResourceUtils {

    private static final String ROOT_NODE_NAME = "root";
    private static final String CONTAINER_NODE_NAME = "container";

    public static List<Resource> getMainContentResources(final Page articlePage) {
        return Optional
                 .ofNullable(articlePage.getContentResource())
                 .map(pageContent -> pageContent.getChild(ROOT_NODE_NAME))
                 .map(rootResource -> rootResource.getChild(CONTAINER_NODE_NAME))
                 .map(bodyResource -> bodyResource.getChild(CONTAINER_NODE_NAME))
                 .map(mainResource -> IteratorUtils.toList(mainResource.listChildren()))
                 .orElse(Collections.emptyList());
    }

    private ResourceUtils() {
    }

}
