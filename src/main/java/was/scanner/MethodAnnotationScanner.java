package was.scanner;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MethodAnnotationScanner {
    private final ClassAnnotationScanner classAnnotationScanner = ClassAnnotationScanner.getInstance();
    private static final MethodAnnotationScanner METHOD_ANNOTATION_SCANNER = new MethodAnnotationScanner();

    public List<Method> getMethods(Class<? extends Annotation> classAnnotation, Class<? extends Annotation> methodAnnotation) {
        return classAnnotationScanner.getClasses(classAnnotation).stream()
                .map(Class::getMethods)
                .flatMap(Arrays::stream)
                .filter(it -> it.isAnnotationPresent(methodAnnotation))
                .collect(Collectors.toList());
    }

    public static MethodAnnotationScanner getInstance(){
        return METHOD_ANNOTATION_SCANNER;
    }
}