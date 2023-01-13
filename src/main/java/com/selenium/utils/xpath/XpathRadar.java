package com.selenium.utils.xpath;

import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.selenium.utils.xpath.XpathTemplate.*;


@Deprecated
public final class XpathRadar {
    private static XpathRadar start = null;
    private static String xpath = null;
    private static final String UP = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String LO = "abcdefghijklmnopqrstuvwxyz";

    private XpathRadar() {
    }

    public static XpathRadar wordsText(String text) {
        if (start == null) {
            start = new XpathRadar();
        }
        xpath = String.format(templateTextWords, text);
        return start;
    }

    public static XpathRadar normalizeSpaceText(String text) {
        if (start == null) {
            start = new XpathRadar();
        }
        xpath = String.format(templateTextFuzzy, "normalize-space", text).replace("),", "))=").replace("\")", "\"");
        return start;
    }

    public static XpathRadar containsText(String text) {
        if (start == null) {
            start = new XpathRadar();
        }
        xpath = String.format(templateTextFuzzy, "contains", text);
        return start;
    }

    public static XpathRadar startsWithtext(String text) {
        if (start == null) {
            start = new XpathRadar();
        }
        xpath = String.format(templateTextFuzzy, "starts-with", text);
        return start;
    }

    public static XpathRadar containsAndNormalizeSpaceText(String text) {
        if (start == null) {
            start = new XpathRadar();
        }
        xpath = String.format(templateFuzzyAndSpace, "text()", "contains", text);
        return start;
    }

    public static XpathRadar startsWithAndNormalizeSpaceText(String text) {
        if (start == null) {
            start = new XpathRadar();
        }
        xpath = String.format(templateFuzzyAndSpace, "text()", "starts-with", text);
        return start;
    }

    public static XpathRadar wordsAttribute(String key, String value) {
        if (start == null) {
            start = new XpathRadar();
        }
        xpath = String.format(tmplateAttributeWords, key.replace("@", ""), value);
        return start;
    }

    public static XpathRadar normalizeSpaceAttribute(String key, String value) {
        if (start == null) {
            start = new XpathRadar();
        }
        xpath = String.format(templateAttributeFuzzy, "normalize-space", key.replace("@", ""), value).replace(",", ")=").replace("\")", "\"");
        return start;
    }

    public static XpathRadar containsAttribute(String key, String value) {
        if (start == null) {
            start = new XpathRadar();
        }
        xpath = String.format(templateAttributeFuzzy, "contains", key.replace("@", ""), value);
        return start;
    }

    public static XpathRadar startsWithAttribute(String key, String value) {
        if (start == null) {
            start = new XpathRadar();
        }
        xpath = String.format(templateAttributeFuzzy, "starts-with", key.replace("@", ""), value);
        return start;
    }

    public static XpathRadar containsNormalizeSpaceAttribute(String key, String value) {
        if (start == null) {
            start = new XpathRadar();
        }
        xpath = String.format(templateFuzzyAndSpace, "@" + key.replace("@", ""), "contains", value);
        return start;
    }

    public static XpathRadar startsWithNormalizeSpaceAttribute(String key, String text) {
        if (start == null) {
            start = new XpathRadar();
        }
        xpath = String.format(templateFuzzyAndSpace, "@" + key.replace("@", ""), "starts-with", text);
        return start;
    }

    public XpathRadar caseInsensitive() {
        Matcher m = Pattern.compile("(\\[)(.*?)(])").matcher(xpath);
        if (m.find()) {
            String re = m.group(2);
            if (re.contains("@")) {
                Matcher m1 = Pattern.compile("(@)(.*?)(=)").matcher(xpath);
                Matcher m2 = Pattern.compile("(\")(.*?)(\")").matcher(xpath);
                if (m1.find() && m2.find()) {
                    xpath = xpath.replace(xpath, String.format(templateCaseInsensitive, "@" + m1.group(2), UP, LO, m2.group(2).toLowerCase()));
                }
            } else if (re.contains("text")) {
                Matcher m1 = Pattern.compile("(\")(.*?)(\")").matcher(xpath);
                if (m1.find()) {
                    xpath = xpath.replace(xpath, String.format(templateCaseInsensitive, "text()", UP, LO, m1.group(2).toLowerCase()));
                }
            }
        }
        return start;
    }

    public XpathRadar caseInsensitiveContains() {
        Matcher m = Pattern.compile("(\\[)(.*?)(])").matcher(xpath);
        if (m.find()) {
            String re = m.group(2);
            if (re.contains("@")) {
                Matcher m1 = Pattern.compile("(@)(.*?)(=)").matcher(xpath);
                Matcher m2 = Pattern.compile("(\")(.*?)(\")").matcher(xpath);
                if (m1.find() && m2.find()) {
                    xpath = xpath.replace(xpath, String.format(templateContainsCaseInsensitive, "@" + m1.group(2), UP, LO, m2.group(2).toLowerCase()));
                }
            } else if (re.contains("text")) {
                Matcher m1 = Pattern.compile("(\")(.*?)(\")").matcher(xpath);
                if (m1.find()) {
                    xpath = xpath.replace(xpath, String.format(templateContainsCaseInsensitive, "text()", UP, LO, m1.group(2).toLowerCase()));
                }
            }
        }
        return start;
    }


    public XpathRadar tag(String tag) {
        xpath = xpath.replaceFirst("\\*\\[", tag + "\\[");
        return start;
    }

    public XpathRadar leftPath(String leftPath) {
        xpath = xpath.replaceFirst("(//)(.*?)(\\[)", leftPath + "[");
        return start;
    }

    public XpathRadar rightPath(String rightPath) {
        if (xpath.contains("])")) {
            xpath = xpath.replaceFirst("]\\)", "]" + rightPath + ")");
        } else {
            xpath = xpath.replaceFirst("\"]", "\"]" + rightPath);
        }
        return start;
    }

    public XpathRadar index(int index) {
        xpath = String.format(templateIndex, xpath, index);
        return start;
    }

    public XpathRadar index(String index) {
        xpath = String.format(templateIndex, xpath, index);
        return start;
    }

    public XpathRadar indexFirst() {
        xpath = String.format(templateIndex, xpath, "1");
        return start;
    }

    public XpathRadar indexLast() {
        xpath = String.format(templateIndex, xpath, "last()");
        return start;
    }

    public XpathRadar folSibling(String slash, String tag) {
        buildAxis(templateFollowingSibling, slash, tag);
        return start;
    }

    public XpathRadar folSibling() {
        buildAxis(templateFollowingSibling, "/", "*");
        return start;
    }


    public XpathRadar preSibling(String slash, String tag) {
        buildAxis(templatePrecedingSibling, slash, tag);
        return start;
    }

    public XpathRadar preSibling() {
        buildAxis(templatePrecedingSibling, "/", "*");
        return start;
    }

    public XpathRadar following(String slash, String tag) {
        buildAxis(templateFollowing, slash, tag);
        return start;
    }

    public XpathRadar following() {
        buildAxis(templateFollowing, "/", "*");
        return start;
    }

    public XpathRadar preceding(String slash, String tag) {
        buildAxis(templatePreceding, slash, tag);
        return start;
    }

    public XpathRadar preceding() {
        buildAxis(templatePreceding, "/", "*");
        return start;
    }

    public XpathRadar self(String slash, String tag) {
        buildAxis(templateSelf, slash, tag);
        return start;
    }

    public XpathRadar self() {
        buildAxis(templateSelf, "/", "*");
        return start;
    }

    public XpathRadar parent(String slash, String tag) {
        buildAxis(templateParent, slash, tag);
        return start;
    }

    public XpathRadar parent() {
        buildAxis(templateParent, "/", "*");
        return start;
    }

    public XpathRadar ancestor(String slash, String tag) {
        buildAxis(templateAncestor, slash, tag);
        return start;
    }

    public XpathRadar ancestor() {
        buildAxis(templateAncestor, "/", "*");
        return start;
    }

    public XpathRadar ancestorOrSelf(String slash, String tag) {
        buildAxis(templateAncestorOrSelf, slash, tag);
        return start;
    }

    public XpathRadar ancestorOrSelf() {
        buildAxis(templateAncestorOrSelf, "/", "*");
        return start;
    }

    public XpathRadar child(String slash, String tag) {
        buildAxis(templateChild, slash, tag);
        return start;
    }

    public XpathRadar child() {
        buildAxis(templateChild, "/", "*");
        return start;
    }

    public XpathRadar descendant(String slash, String tag) {
        buildAxis(templateDescendant, slash, tag);
        return start;
    }

    public XpathRadar descendant() {
        buildAxis(templateDescendant, "/", "*");
        return start;
    }

    public XpathRadar descendantOrSelf(String slash, String tag) {
        buildAxis(templateDescendantOrSelf, slash, tag);
        return start;
    }

    public XpathRadar descendantOrSelf() {
        buildAxis(templateDescendantOrSelf, "/", "*");
        return start;
    }

    public XpathRadar path(String path) {
        if (!xpath.matches("(.*)(\\(\\[)([\\d+])(])")) {
            xpath = xpath + path;
        } else if (xpath.contains(")[")) {
            xpath = xpath.replace(")[", path + ")[");
        } else {
            throw new IllegalArgumentException("unable to build xpath: " + xpath);
        }
        return start;
    }

    private void buildAxis(String tem, String slash, String tag) {
        if (!slash.matches("[/]{1,2}")) {
            throw new IllegalArgumentException("the first argument must be 1 or 2 slash.");
        }
        if (!xpath.matches("(.*)(\\(\\[)([\\d+])(])")) {
            xpath = xpath + String.format(tem, slash, tag);
        } else if (xpath.contains(")[")) {
            xpath = xpath.replace(")[", String.format(tem, slash, tag) + ")[");
        } else {
            throw new IllegalArgumentException("unable to build xpath: " + xpath);
        }
    }

    public String build() {
        class validateXpath {
            public void validate(String xpath) {
                try {
                    XPathFactory.newInstance().newXPath().compile(xpath);
                } catch (XPathExpressionException e) {
                    throw new IllegalArgumentException("invalid xpath: " + xpath);
                }
            }
        }

        new validateXpath().validate(xpath);
        if (xpath != null) {
            return xpath;
        } else {
            throw new NullPointerException("failed to create xpath.");
        }
    }


}
