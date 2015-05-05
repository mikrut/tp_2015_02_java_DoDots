package servlets;

import freemarker.cache.FileTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.util.Map;

/**
 * Created by Михаил
 * 01.03.2015 9:15
 * Package: ${PACKAGE_NAME}
 */
class TemplateGenerator {
    private static final String TEMPL_DIR = "./srv_tmpl";
    private static Configuration CFG;

    public TemplateGenerator() {
        CFG = new Configuration();

        try {
            CFG.setTemplateLoader(new FileTemplateLoader(new File(TEMPL_DIR)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void generate(Writer writer, @SuppressWarnings("SameParameterValue") String path, Map<String,Object> data) {
        try {
            if(path.equals("/")) path = "/index.html";
            Template template = CFG.getTemplate(path);
            template.process(data, writer);
        } catch (IOException | TemplateException e) {
            e.printStackTrace();
        }
    }
}
