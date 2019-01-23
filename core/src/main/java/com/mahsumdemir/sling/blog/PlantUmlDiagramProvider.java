package com.mahsumdemir.sling.blog;

import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.SourceStringReader;
import net.sourceforge.plantuml.core.DiagramDescription;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;

@Component(service = DiagramProvider.class)
public class PlantUmlDiagramProvider implements DiagramProvider {
    private static final Logger LOG = LoggerFactory.getLogger(PlantUmlDiagramProvider.class);
    private static final HashMap<String, String> cache = new HashMap<>();

    @Override
    public String getSvg(String script) {
        if (cache.containsKey(script)) return cache.get(script);

        try {
            SourceStringReader reader = new SourceStringReader(script);
            final ByteArrayOutputStream os = new ByteArrayOutputStream();
            DiagramDescription des = reader.outputImage(os, new FileFormatOption(FileFormat.SVG));
            os.close();

            String svg = new String(os.toByteArray(), Charset.forName("UTF-8"));
            cache.put(script, svg);
            return svg;
        } catch (IOException e) {
            LOG.error("Error happened while generating SVG", e);
            return "";
        }
    }
}
