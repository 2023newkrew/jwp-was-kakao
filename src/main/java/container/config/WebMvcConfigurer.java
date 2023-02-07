package container.config;

import webserver.ResourceResolver;

public interface WebMvcConfigurer {
    void addResourceHandlers(final ResourceResolver resourceResolver);
}
