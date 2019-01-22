package com.mahsumdemir.sling.blog;

import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.SourceStringReader;
import net.sourceforge.plantuml.core.DiagramDescription;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.models.annotations.Model;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;

@Model(adaptables = {SlingHttpServletRequest.class, SlingHttpServletResponse.class} )
public class DiagramController {

    private final SlingHttpServletRequest request;
    private final SlingHttpServletResponse response;
    private DiagramModel model;
    private String svg;

    @Inject
    public DiagramController(SlingHttpServletRequest request, SlingHttpServletResponse response){
        this.request = request;
        this.response = response;
    }

    @PostConstruct
    public void postConstruct() throws IOException {
        model = request.getResource().adaptTo(DiagramModel.class);
        generateSvg();
    }

    private void generateSvg() throws IOException {
        if (model.getScript() == null) return;

        SourceStringReader reader = new SourceStringReader(model.getScript());
        final ByteArrayOutputStream os = new ByteArrayOutputStream();
        DiagramDescription des = reader.outputImage(os, new FileFormatOption(FileFormat.SVG));
        os.close();

        svg = new String(os.toByteArray(), Charset.forName("UTF-8"));
    }

    public String getSvg(){
        return svg;
    }
}
