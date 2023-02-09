package logics.Service;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;

import java.nio.charset.StandardCharsets;

/**
 * HandlebarService contains methods related handlebars, which can make dynamic responses.
 */
public class HandlebarService {
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
