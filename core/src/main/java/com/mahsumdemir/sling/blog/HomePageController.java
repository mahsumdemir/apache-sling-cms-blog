package com.mahsumdemir.sling.blog;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

@Model(adaptables = {SlingHttpServletRequest.class, SlingHttpServletResponse.class} )
public class HomePageController {
    private static final Logger LOG = LoggerFactory.getLogger(HomePageController.class);

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
            LOG.error("Could not find site for " + request.getResource().getPath());
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
        if (isEditor()) return blogPosts;
        else return getPublishedBlogPosts();
    }

    private List<BlogPostModel> getPublishedBlogPosts() {
        return blogPosts.stream().filter(BlogPostModel::isPublished)
                .collect(Collectors.toList());
    }

    public boolean isEditor(){
        try {
            return Boolean.valueOf(request.getAttribute("cmsEditEnabled").toString());
        } catch (Exception e){
            return false;
        }

    }

}
