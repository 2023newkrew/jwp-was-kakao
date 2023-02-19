package model;

import lombok.Getter;

@Getter
public class HandlebarsView {

    private final String suffix;
    private final String prefix;
    private final String viewName;

    public HandlebarsView(MyModelAndView mav) {
        String[] parsed = mav.getViewName().split("\\.");
        this.suffix = "." + parsed[parsed.length - 1];
        this.viewName = parsed[0].substring(1);
        this.prefix = mav.getPrefix().substring(1);
    }
}
