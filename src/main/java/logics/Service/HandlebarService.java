package logics.Service;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.HandlebarsException;
import com.github.jknack.handlebars.Template;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * HandlebarService contains methods related handlebars, which can make dynamic responses.
 */
public class HandlebarService {
    private HandlebarService(){}

    /**
     * @param originalText to be original text which should be applied handlebars.
     * @param applyObject which object would like to apply to originalText.
     * @return applied originalTest. if there is IOException or HandlebarException, return null.
     */
    public static byte[] applyHandlebars(byte[] originalText, Object applyObject){
        try {
            Handlebars handlebars = new Handlebars();
            Template template = handlebars.compileInline(new String(originalText, StandardCharsets.UTF_8));
            return template.apply(applyObject).getBytes(StandardCharsets.UTF_8);
        } catch(HandlebarsException | IOException e){
            e.printStackTrace();
            return null;
        }
    }
}
