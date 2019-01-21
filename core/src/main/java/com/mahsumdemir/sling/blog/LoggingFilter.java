package com.mahsumdemir.sling.blog;

import org.apache.sling.api.SlingHttpServletRequest;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component(service = Filter.class, property = {"sling.filter.scope=request"})
public class LoggingFilter implements Filter{
    private static final Logger LOG = LoggerFactory.getLogger(LoggingFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        SlingHttpServletRequest slingRequest = (SlingHttpServletRequest) request;

        LOG.error("Server name: " + slingRequest.getServerName());
        LOG.error("Server port: " + slingRequest.getServerPort());
        LOG.error("Request URI: " + slingRequest.getRequestURI()) ;

        chain.doFilter(request, response);


        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        LOG.error("Response Status: " + httpServletResponse.getStatus());
    }

    @Override
    public void destroy() {

    }



}
