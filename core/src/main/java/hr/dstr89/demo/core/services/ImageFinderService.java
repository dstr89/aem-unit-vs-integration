package hr.dstr89.demo.core.services;

import com.day.cq.wcm.api.Page;

public interface ImageFinderService {

    String getFirstImagePath(String rendition, Page articlePage);

}
