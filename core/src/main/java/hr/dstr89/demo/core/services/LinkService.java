package hr.dstr89.demo.core.services;

import org.apache.sling.api.resource.ResourceResolver;

public interface LinkService {

    String getExternalLink(ResourceResolver resourceResolver, String path);

}
