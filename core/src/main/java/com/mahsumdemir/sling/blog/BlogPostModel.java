package com.mahsumdemir.sling.blog;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.models.annotations.Model;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

@Model(adaptables = {SlingHttpServletRequest.class, SlingHttpServletResponse.class} )
public class BlogPostModel {

    private SlingHttpServletRequest request;
    private SlingHttpServletResponse response;

    @Inject
    public BlogPostModel(SlingHttpServletRequest request, SlingHttpServletResponse response) {
        this.request = request;
        this.response = response;
    }


    @PostConstruct
    public void postConstruct() {
    }
}
