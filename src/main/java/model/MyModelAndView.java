package model;

import java.util.HashMap;
import java.util.Map;
import lombok.Getter;

@Getter
public class MyModelAndView {

    private final String viewName;

    private final Map<String, Object> model = new HashMap<>();

    public MyModelAndView() {
        this("");
    }

    public MyModelAndView(String viewName) {
        this.viewName = viewName;
    }

    public void addAttribute(String name, Object value) {
        this.model.put(name, value);
    }
}
