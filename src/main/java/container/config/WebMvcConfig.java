package container.config;

import org.springframework.context.annotation.Configuration;
import webserver.ResourceResolver;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer{

    public static final String CLASSPATH_STATIC = "./static";
    public static final String CLASSPATH_STATIC_TEMPLATES = "./templates";
    public static final String CLASSPATH_STATIC_CSS = CLASSPATH_STATIC + "/css";

    @Override
    public void addResourceHandlers(final ResourceResolver resourceResolver) {
        resourceResolver.addResourceHandler("/**/css/**", CLASSPATH_STATIC_CSS);
        resourceResolver.addResourceHandler("/", CLASSPATH_STATIC_TEMPLATES);
        resourceResolver.addResourceHandler("/index.html", CLASSPATH_STATIC_TEMPLATES);
    }
}
