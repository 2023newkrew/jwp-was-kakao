package utils;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import com.github.jknack.handlebars.io.ClassPathTemplateLoader;
import com.github.jknack.handlebars.io.TemplateLoader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import model.HandlebarsView;
import model.MyModelAndView;

public class FileIoUtils {

    public static byte[] loadFileFromClasspath(String filePath) throws IOException, URISyntaxException {
        Path path = Paths.get(FileIoUtils.class.getClassLoader().getResource(filePath).toURI());
        return Files.readAllBytes(path);
    }

    public static byte[] loadTemplate(MyModelAndView mav) throws IOException, URISyntaxException {
        if (mav.getFullViewName().contains("static")) {
            return loadFileFromClasspath(mav.getFullViewName());
        }

        TemplateLoader loader = new ClassPathTemplateLoader();
        HandlebarsView handlebarsView = new HandlebarsView(mav);

        loader.setPrefix(handlebarsView.getPrefix());
        loader.setSuffix(handlebarsView.getSuffix());

        Handlebars handlebars = new Handlebars(loader);
        Template template = handlebars.compile(handlebarsView.getViewName());

        return template.apply(mav.getModel()).getBytes(StandardCharsets.UTF_8);
    }
}
