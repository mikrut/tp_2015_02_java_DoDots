package servlets;

import freemarker.cache.FileTemplateLoader;
import freemarker.cache.MultiTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.util.Map;

/**
 * Created by Михаил on 01.03.2015.
 */
public class TemplateGenerator {
    private static final String TEMPL_DIR = "srv_tmpl";
    private static Configuration CFG;

    public TemplateGenerator() {
        CFG = new Configuration();
        TemplateLoader[] loaders = null;
        try {
             loaders = new TemplateLoader[]{
                    new FileTemplateLoader(new File("./srv_tmpl")),
                    new FileTemplateLoader(new File("./public_html"))
            };
        } catch (Exception e) {
            e.printStackTrace();
        }
        MultiTemplateLoader mtl = new MultiTemplateLoader(loaders);
        CFG.setTemplateLoader(mtl);
    }

    public void generate(Writer writer, String path, Map<String,Object> data) {
        try {
            if(path == "/") path = "/index.html";
            Template template = CFG.getTemplate(path);
            template.process(data, writer);
        } catch (IOException | TemplateException e) {
            e.printStackTrace();
        }
    }
}
