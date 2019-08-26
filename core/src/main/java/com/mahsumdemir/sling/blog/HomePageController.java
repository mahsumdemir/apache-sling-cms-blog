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
    private List<BlogPostLightModel> lightPosts = new ArrayList<>();

    @Inject
    public HomePageController(SlingHttpServletRequest request, SlingHttpServletResponse response){
        this.request = request;
        this.response = response;
    }

    @PostConstruct
    public void postConstruct(){
        initializeLightBlogPostComponents();
    }

    private void initializeLightBlogPostComponents() {
        String sitePath = getSitePath(request.getResource());
        if (StringUtils.isEmpty(sitePath)){
            LOG.error("Could not find site for " + request.getResource().getPath());
            return;
        }

        Iterator<Resource> resourceIterator = request.getResourceResolver()
                .findResources(String.format("/jcr:root%s//*[sling:resourceType=\"%s\"]", sitePath,
                        Constants.ResourceTypes.BLOG_POST_LIGHT), "xpath");
        while (resourceIterator.hasNext()){
            Resource page = resourceIterator.next();
            BlogPostLightModel blogPostModel = page.adaptTo(BlogPostLightModel.class);
            if (blogPostModel == null) continue;
            blogPostModel.setPageUrl(findBlogPostUrl(page));
            lightPosts.add(blogPostModel);
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

    public List<BlogPostLightModel> getLightPosts(){
        if (isEditor()) return lightPosts;
        else return getPublishedLightPosts();
    }

    private List<BlogPostLightModel> getPublishedLightPosts(){
        return lightPosts.stream().filter(BlogPostLightModel::isPublished)
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
