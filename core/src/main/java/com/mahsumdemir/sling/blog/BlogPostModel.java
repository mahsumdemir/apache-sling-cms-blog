package com.mahsumdemir.sling.blog;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Model;

import javax.inject.Inject;

@Model(adaptables = Resource.class )
public class BlogPostModel {
    @Inject
    private Boolean published;

    @Inject
    private String title;

    @Inject
    private String backgroundImage;

    private String pageUrl;

    public Boolean getPublished() {
        return published;
    }

    public String getTitle() {
        return title;
    }

    public String getBackgroundImage() {
        return backgroundImage;
    }

    public void setUrl(String pageUrl) {
        this.pageUrl = pageUrl;
    }

    public String getPageUrl(){
        return pageUrl;
    }

    public boolean isPublished(){
        return published;
    }
}
