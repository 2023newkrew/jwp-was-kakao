package webserver.constants.extension;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum ImageType {
    JPG("jpg"),
    JPEG("jpeg"),
    GIF("gif"),
    PNG("png");

    private final String extension;
    private static final Map<String, ImageType> reverseMap = new HashMap<>();

    static {
        for (ImageType imageType : EnumSet.allOf(ImageType.class)) {
            reverseMap.put(imageType.getExtension(), imageType);
        }
    }

    ImageType(String extension) {
        this.extension = extension;
    }

    public String getExtension() {
        return extension;
    }

    public static Boolean isImageByExtension(String extension) {
        if (reverseMap.containsKey(extension)) {
            return true;
        }

        return false;
    }
}