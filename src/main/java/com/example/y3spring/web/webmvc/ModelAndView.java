package com.example.y3spring.web.webmvc;

import java.util.Map;

public class ModelAndView {
    private String ViewName;

    private Map<String, ?> model;

    public ModelAndView(String viewName, Map<String, ?> model) {
        ViewName = viewName;
        this.model = model;
    }

    public ModelAndView(String viewName) {
        ViewName = viewName;
    }

    public String getViewName() {
        return ViewName;
    }

    public Map<String, ?> getModel() {
        return model;
    }

}
