package com.mahsumdemir.sling.blog;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.Model;

import javax.inject.Inject;

@Model(adaptables = Resource.class )
public class DiagramModel {
    @Inject
    @Default(values = "")
    private String script;

    public String getScript() {
        return script;
    }
}
