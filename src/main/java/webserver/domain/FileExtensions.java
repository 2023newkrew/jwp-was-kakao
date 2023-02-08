package webserver.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Getter
@AllArgsConstructor
public enum FileExtensions {
    JPG(MediaType.IMAGE_JPEG),
    JPEG(MediaType.IMAGE_JPEG),
    PNG(MediaType.IMAGE_PNG),
    GIF(MediaType.IMAGE_GIF),
    ICO(MediaType.IMAGE_X_ICON),
    TXT(MediaType.TEXT_PLAIN),
    HTML(MediaType.TEXT_HTML),
    CSS(MediaType.TEXT_CSS),
    JS(MediaType.TEXT_JAVASCRIPT),
    XML(MediaType.APPLICATION_XML),
    JSON(MediaType.APPLICATION_JSON),
    TTF(MediaType.APPLICATION_X_FONT_TTF),
    WOFF(MediaType.APPLICATION_X_FONT_WOFF),
    WOFF2(MediaType.APPLICATION_X_FONT_WOFF),
    EOT(MediaType.APPLICATION_VND_MS_FONDOBJECT),
    SVG(MediaType.IMAGE_SVG_XML),
    ZIP(MediaType.APPLICATION_ZIP),
    ;

    private static final Map<String, FileExtensions> enumMapper = new HashMap<>();

    static {
        Arrays.stream(FileExtensions.values())
                .forEach(item -> enumMapper.putIfAbsent(item.toString(), item));
    }

    private final MediaType mediaType;

    public static FileExtensions of(String fileExtension) {
        return enumMapper.getOrDefault(fileExtension.toUpperCase(), FileExtensions.TXT);
    }
}
