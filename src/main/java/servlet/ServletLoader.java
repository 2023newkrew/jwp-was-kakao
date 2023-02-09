package servlet;

import exception.InternalServerErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class ServletLoader {
    private static final Logger logger = LoggerFactory.getLogger(ServletLoader.class);
    private static final String SERVLET_PACKAGE_PATH = "/Users/kakao/Documents/jwp-was-kakao/src/main/java/servlet";
    private static final String PACKAGE_NAME = "servlet";
    private static final String JAVA_FILE_EXTENSION = ".java";

    public static Map<String, Servlet> load() {
        File folder = new File(SERVLET_PACKAGE_PATH);

        return Arrays.stream(Objects.requireNonNull(folder.listFiles()))
                .filter(ServletLoader::isJavaClassFile)
                .map(ServletLoader::getClassReflection)
                .filter(Objects::nonNull)
                .filter(ServletLoader::hasServletMappingAnnotation)
                .collect(Collectors.toMap(
                                ServletLoader::getUri,
                                ServletLoader::getInstance
                        )
                );
    }

    private static boolean isJavaClassFile(File file) {
        return file.isFile() && file.getName().endsWith(JAVA_FILE_EXTENSION);
    }

    private static Class<?> getClassReflection(File file) {
        String className = PACKAGE_NAME + '.' + file.getName().substring(0, file.getName().length() - JAVA_FILE_EXTENSION.length());
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            logger.error("getClassReflection : {}", e.getMessage());
            return null;
        }
    }

    private static boolean hasServletMappingAnnotation(Class<?> clazz) {
        return clazz.isAnnotationPresent(ServletMapping.class);
    }

    private static String getUri(Class<?> clazz) {
        return clazz.getAnnotation(ServletMapping.class).uri();
    }

    private static Servlet getInstance(Class<?> clazz) {
        try {
            return (Servlet) clazz.getMethod("getInstance").invoke(clazz);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new InternalServerErrorException("Servlet Instance 을 생성할 수 없습니다.");
        }
    }
}
