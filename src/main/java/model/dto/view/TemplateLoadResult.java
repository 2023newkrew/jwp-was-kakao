package model.dto.view;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class TemplateLoadResult {
    private String content;

    public static TemplateLoadResult from(String string) {
        return new TemplateLoadResult(string);
    }
}
