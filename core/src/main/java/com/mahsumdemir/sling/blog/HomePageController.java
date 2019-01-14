package com.mahsumdemir.sling.blog;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Model;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Model(adaptables = {SlingHttpServletRequest.class, SlingHttpServletResponse.class} )
public class HomePageController {

    private final SlingHttpServletRequest request;
    private final SlingHttpServletResponse response;
    private List<BlogPostModel> blogPosts = new ArrayList<BlogPostModel>();

    @Inject
    public HomePageController(SlingHttpServletRequest request, SlingHttpServletResponse response){
        this.request = request;
        this.response = response;
    }

    @PostConstruct
    public void postConstruct(){
        initializeBlogPostComponents();
    }

    private void initializeBlogPostComponents() {
        String sitePath = getSitePath(request.getResource());

        if (StringUtils.isEmpty(sitePath)){
            System.err.println("Could not find site for " + request.getResource().getPath());
            return;
        }

        Iterator<Resource> resourceIterator = request.getResourceResolver()
                .findResources(String.format("/jcr:root%s//*[sling:resourceType=\"%s\"]", sitePath,
                        Constants.ResourceTypes.BLOG_POST_PAGE), "xpath");
        while (resourceIterator.hasNext()){
            Resource page = resourceIterator.next();
            BlogPostModel blogPostModel = page.adaptTo(BlogPostModel.class);
            if (blogPostModel == null) continue;
            blogPostModel.setUrl(findBlogPostUrl(page));
            blogPosts.add(blogPostModel);
        }
    }

    private String getSitePath(Resource resource) {
        Resource currentResource = resource;
        while (currentResource != null && !isSite(currentResource)){
            currentResource = currentResource.getParent();
        }

        return currentResource == null ? "" : currentResource.getPath();
    }

    private boolean isSite(Resource resource) {
        return Constants.ResourceTypes.SITE.equalsIgnoreCase(resource.getResourceType());
    }

    private String findBlogPostUrl(Resource page) {
        return page.getParent().getPath() + ".html";
    }

    public List<BlogPostModel> getBlogPosts(){
        return blogPosts;
    }
}
