package utils;

import lombok.experimental.UtilityClass;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import static constant.PathConstant.STATIC;

@UtilityClass
public class FileIoUtils {
    public byte[] loadFileFromClasspath(String filePath) throws IOException, URISyntaxException {
        Path path = Paths.get(FileIoUtils.class.getClassLoader().getResource(filePath).toURI());
        return Files.readAllBytes(path);
    }

    public Set<String> getStaticFolderNames() {
        URL resource = Thread.currentThread().getContextClassLoader().getResource(STATIC);
        File[] files = new File(resource.getPath()).listFiles();

        return Arrays.stream(files)
                .map(File::getName)
                .collect(Collectors.toSet());
    }
}
