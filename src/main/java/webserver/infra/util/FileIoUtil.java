package webserver.infra.util;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;

public class FileIoUtil {
    private static final Set<String> DIRECTORIES = new LinkedHashSet<>();

    static {
        DIRECTORIES.add("./templates");
        DIRECTORIES.add("./static");
    }

    public static byte[] loadFileFromClasspath(String filePath) throws IOException {
        Optional<Path> path = Optional.empty();
        for (String dir : DIRECTORIES) {
            Optional<Path> nowPath = Optional.ofNullable(FileIoUtil.class.getClassLoader().getResource(dir + filePath))
                    .map(URL::getPath)
                    .map(Paths::get);
            if (nowPath.isPresent()) {
                path = nowPath;
                break;
            }
        }

        if (path.isEmpty()) {
            throw new NullPointerException();
        }

        return Files.readAllBytes(path.get());
    }
}
