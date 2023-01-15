package io.smart.utils.xpath;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XpathBuilder {
    private static Logger logger = LoggerFactory.getLogger(XpathBuilder.class);

    public XpathBuilder() {
    }

    public static String xpathGenerator(String text) {
        String value;
        if (text.contains("->")) {
            String[] xpath = text.split("->");
            if (xpath[0].equalsIgnoreCase("text") && text.toLowerCase().contains("tag=")) {
                if (xpath[0].contains("@")) {
                    if (xpath.length == 4) {
                        value = Xpath.attribute(xpath[0], xpath[1]).tag(xpath[3].replace("tag=", "")).path(xpath[2]).build();
                    } else {
                        value = Xpath.attribute(xpath[0], xpath[1]).tag(xpath[2].replace("tag=", "")).build();
                    }
                } else {
                    if (xpath.length == 4) {
                        value = Xpath.text(xpath[1]).tag(xpath[3].replace("tag=", "")).path(xpath[2]).build();
                    } else {
                        value = Xpath.text(xpath[1]).tag(xpath[2].replace("tag=", "")).build();
                    }
                }
            } else if (xpath[0].equalsIgnoreCase("text")) {
                if (xpath.length == 3) {
                    value = Xpath.text(xpath[1]).rightPath(xpath[2]).build();
                } else {
                    value = Xpath.text(xpath[1]).build();
                }
            } else if (xpath[0].contains("@")) {
                if (xpath.length == 3) {
                    value = Xpath.attribute(xpath[0], xpath[1]).rightPath(xpath[2]).build();
                } else {
                    value = Xpath.attribute(xpath[0], xpath[1]).build();
                }
            } else {
                value = text;
            }
        } else {
            value = text;
        }
        logger.debug("the wrapped xpath is: " + value);
        return value;
    }


    public static String xpathGenerator(String text, String index) {
        String value;
        if (text.contains("->")) {
            String[] xpath = text.split("->");
            if (xpath[0].equalsIgnoreCase("text") && text.toLowerCase().contains("tag=")) {
                if (xpath[0].contains("@")) {
                    if (xpath.length == 4) {
                        value = Xpath.attribute(xpath[0], xpath[1]).tag(xpath[3].replace("tag=", "")).path(xpath[2]).index(index).build();

                    } else {
                        value = Xpath.attribute(xpath[0], xpath[1]).tag(xpath[2].replace("tag=", "")).index(index).build();
                    }
                } else {
                    if (xpath.length == 4) {
                        value = Xpath.text(xpath[1]).tag(xpath[3].replace("tag=", "")).path(xpath[2]).index(index).build();
                    } else {
                        value = Xpath.text(xpath[1]).tag(xpath[2].replace("tag=", "")).index(index).build();

                    }
                }
            } else if (xpath[0].equalsIgnoreCase("text")) {
                if (xpath.length == 3) {
                    value = Xpath.text(xpath[1]).path(xpath[2]).index(index).build();
                } else {
                    value = Xpath.text(xpath[1]).index(index).build();

                }
            } else if (xpath[0].contains("@")) {
                if (xpath.length == 3) {
                    value = Xpath.attribute(xpath[0], xpath[1]).rightPath(xpath[2]).index(index).build();
                } else {
                    value = Xpath.attribute(xpath[0], xpath[1]).index(index).build();
                }
            } else {
                value = text;
            }
        } else {
            value = "(" + text + ")[" + index + "]";
        }
        logger.debug("the wrapped xpath is: " + value);
        return value;
    }


    public static String xpathContainsGenerator(String text) {
        String value = null;
        if (text.contains("->")) {
            String[] xpath = text.split("->");
            if (xpath[0].equalsIgnoreCase("text")) {
                if (xpath.length == 3) {
                    value = Xpath.text(xpath[1]).ignoreCaseContains().path(xpath[2]).build();
                } else {
                    value = Xpath.text(xpath[1]).ignoreCaseContains().build();

                }
            } else if (xpath[0].equals("*")) {
                if (xpath.length == 3) {
                    value = Xpath.attribute("*", xpath[1]).path(xpath[2]).contains().build();
                } else {
                    value = Xpath.attribute("*", xpath[1]).contains().build();
                }
            } else {
                if (xpath.length == 3) {
                    value = Xpath.attribute(xpath[0], xpath[1]).path(xpath[2]).contains().build();
                } else {
                    value = Xpath.attribute(xpath[0], xpath[1]).contains().build();
                }
            }
        }
        logger.debug("the wrapped xpath is: " + value);
        return value;
    }

    public static String xpathContainsGenerator(String text, int index) {
        if (index <= 0) {
            throw new IllegalArgumentException("index must be greater than 0");
        }
        String value = null;
        if (text.contains("->")) {
            String[] xpath = text.split("->");
            if (xpath[0].equalsIgnoreCase("text")) {
                if (xpath.length == 3) {
                    value = "((//*[*[text()[contains(translate(., \"" + xpath[1].toUpperCase() + "\",\"" + xpath[1].toLowerCase() + "\"), \"" + xpath[1].toLowerCase() + "\")]]])" + xpath[2] + ")[" + index + "]";
                } else {
                    value = "(//*[*[text()[contains(translate(., \"" + xpath[1].toUpperCase() + "\",\"" + xpath[1].toLowerCase() + "\"), \"" + xpath[1].toLowerCase() + "\")]]])" + "[" + index + "]";
                }
            } else if (xpath[0].contains("@")) {
                if (xpath.length == 3) {
                    value = "//*[" + xpath[0] + "=\"" + xpath[1] + "\"]" + xpath[2];
                } else {
                    value = "//*[" + xpath[0] + "=\"" + xpath[1] + "\"]";
                }
            } else {
                if (xpath.length == 3) {
                    value = "((//*[contains(@" + xpath[0] + ",\"" + xpath[1] + "\")])" + xpath[2] + ")[" + index + "]";
                } else {
                    value = "(//*[contains(@" + xpath[0] + ",\"" + xpath[1] + "\")])" + "[" + index + "]";
                }
            }
        }
        logger.debug("the wrapped xpath is: " + value);
        return value;
    }
}
