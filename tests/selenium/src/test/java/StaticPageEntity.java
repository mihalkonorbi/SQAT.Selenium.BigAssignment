package src.test.java;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.openqa.selenium.By;

public class StaticPageEntity {
    private final String urlPath;
    private final List<By> byList;

    public StaticPageEntity(String urlPath, List<By> byList) {
        this.urlPath = urlPath;
        this.byList = new ArrayList<>();
        this.byList.addAll(byList);
    }

    public StaticPageEntity(String urlPath, By... byArray) {
        this.urlPath = urlPath;
        this.byList = new ArrayList<>();
        this.byList.addAll(Arrays.asList(byArray));
    }

    public String getUrlPath() {
        return urlPath;
    }

    public List<By> getByList() {
        return byList;
    }
}
