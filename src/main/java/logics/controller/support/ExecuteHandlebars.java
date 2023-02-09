package logics.controller.support;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;

import java.nio.charset.StandardCharsets;

public class ExecuteHandlebars {
    public static byte[] applyHandlebars(byte[] originalText, Object applyObject){
        try {
            Handlebars handlebars = new Handlebars();
            Template template = handlebars.compileInline(new String(originalText, StandardCharsets.UTF_8));
            return template.apply(applyObject).getBytes(StandardCharsets.UTF_8);
        } catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
