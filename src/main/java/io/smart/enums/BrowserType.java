package io.smart.enums;

public enum BrowserType {
    CHROME("chrome"),
    FIREFOX("firefox"),
    EDGE("edge"),
    SAFARI("safari");


    private String name;

    BrowserType(String name){
        this.name=name;
    }

    public String getName(){
        return this.name;
    }

}
