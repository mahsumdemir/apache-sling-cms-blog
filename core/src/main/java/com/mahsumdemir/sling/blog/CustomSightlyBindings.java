package com.mahsumdemir.sling.blog;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.scripting.api.BindingsValuesProvider;
import org.osgi.service.component.annotations.Component;

import javax.script.Bindings;
@Component(
        property = {"javax.script.name=sightly"}
)
public class CustomSightlyBindings implements BindingsValuesProvider {
    public CustomSightlyBindings(){

    }

    @Override
    public void addBindings(Bindings bindings) {
        if (!bindings.containsKey("isEditor") && bindings.containsKey("request")){
            SlingHttpServletRequest request = ((SlingHttpServletRequest)bindings.get("request"));
            Object cmsEditEnabled = request.getAttribute("cmsEditEnabled");
            bindings.put("isEditor", cmsEditEnabled);
        }
    }
}
