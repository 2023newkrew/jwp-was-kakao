package annotation;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ClassAnnotationScanner {
    private List<Class<?>> classes = new ArrayList<>();

    private static final ClassAnnotationScanner CLASS_ANNOTATION_SCANNER = new ClassAnnotationScanner();

    private ClassAnnotationScanner() {
        scan();
    }

    private void scan() {
        ClassLoader classLoader = ClassAnnotationScanner.class.getClassLoader();

        try {
            Enumeration<URL> resources = classLoader.getResources("");
            getFilesFromResources(resources).stream()
                    .filter(File::isDirectory)
                    .forEach(this::findClasses);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private List<File> getFilesFromResources(Enumeration<URL> resources) {
        List<File> files = new ArrayList<>();
        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();
            files.add(new File(resource.getFile()));
        }
        return files;
    }

    private void findClasses(File directory) {
        findClasses(directory, null);
    }

    private void findClasses(File directory, String packageName) {
        if (!directory.exists()) {
            return;
        }
        getDirectory(directory)
                .forEach(it -> findClasses(it, getPackageName(packageName, it.getName())));
        getClass(directory)
                .forEach(it -> addClass(getClassName(packageName, it.getName())));
    }

    private List<File> getFileList(File directory) {
        return List.of(getFileArray(directory).orElse(new File[]{}));
    }

    private Optional<File[]> getFileArray(File directory) {
        return Optional.ofNullable(directory.listFiles());
    }

    private void addClass(String className) {
        try {
            classes.add(Class.forName(className));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private List<File> getDirectory(File directory) {
        return getFileList(directory).stream()
                .filter(File::isDirectory)
                .collect(Collectors.toList());
    }

    private List<File> getClass(File directory) {
        return getFileList(directory).stream()
                .filter(it -> it.getName().endsWith(".class"))
                .collect(Collectors.toList());
    }

    private String getPackageName(String packageName, String fileName) {
        if (packageName == null) {
            return fileName;
        }
        return packageName + "." + fileName;
    }

    private String getClassName(String packageName, String fileName) {
        if (packageName == null) {
            return subClassExt(fileName);
        }
        return packageName + "." + subClassExt(fileName);
    }

    private static String subClassExt(String fileName) {
        return fileName.substring(0, fileName.length() - 6);
    }

    public static ClassAnnotationScanner getInstance() {
        return CLASS_ANNOTATION_SCANNER;
    }

    public List<Class<?>> getClasses(Class<? extends Annotation> annotation) {
        return classes.stream()
                .filter(it -> it.isAnnotationPresent(annotation))
                .collect(Collectors.toList());
    }
}
