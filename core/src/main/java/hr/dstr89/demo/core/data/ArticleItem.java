package hr.dstr89.demo.core.data;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ArticleItem {

    private String title;
    private String image;
    private String url;
    private String description;

}