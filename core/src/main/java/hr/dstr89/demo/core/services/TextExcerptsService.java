package hr.dstr89.demo.core.services;

import com.day.cq.wcm.api.Page;

public interface TextExcerptsService {

    String getTextExcerpts(Page articlePage, int maxLength);

}