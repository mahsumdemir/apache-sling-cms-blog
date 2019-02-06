package com.mahsumdemir.sling.blog;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Model;

import javax.inject.Inject;

@Model(adaptables = Resource.class )
public class BlogPostLightModel {
    @Inject
    private Boolean published;

    @Inject
    private String title;

    private String pageUrl;

    public Boolean isPublished() {
        return published;
    }

    public String getTitle() {
        return title;
    }

    public void setPageUrl(String pageUrl) {
        this.pageUrl = pageUrl;
    }

    public String getPageUrl(){
        return pageUrl;
    }
}
