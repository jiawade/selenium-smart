package io.smart.utils.xpath;

import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public final class Xpath {
    private static Xpath start = null;
    private static String xpath = null;
    private static boolean flag = false;
    private static String key = null;
    private static String value = null;
    private static final String UP = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String LO = "abcdefghijklmnopqrstuvwxyz";

    private Xpath() {
    }

    public static Xpath text(String text) {
        if (start == null) {
            start = new Xpath();
        }
        value = text;
        flag = true;
        if (text.contains("text->")) {
            xpath = String.format(XpathTemplate.templateTextWords, text.split("text->")[1]);
        } else if (text.contains("text=")) {
            xpath = String.format(XpathTemplate.templateTextWords, text.split("text=")[1]);
        } else {
            xpath = String.format(XpathTemplate.templateTextWords, text);
        }
        return start;
    }

    public static Xpath attribute(String attribute, String text) {
        if (start == null) {
            start = new Xpath();
        }
        flag = false;
        key = attribute.replace("@", "");
        value = text;
        xpath = String.format(XpathTemplate.tmplateAttributeWords, key, text);
        return start;
    }

    public static Xpath attribute(String text) {
        if (start == null) {
            start = new Xpath();
        }
        flag = false;
        if (text.contains("@") && text.contains("->")) {
            String[] attrs = text.split("->");
            key = attrs[0].replace("@","");
            value = attrs[1];
            xpath = String.format(XpathTemplate.tmplateAttributeWords, key, value);
        } else if (text.contains("@") && text.contains("=")) {
            String[] attrs = text.split("=");
            key = attrs[0].replace("@","");
            value = attrs[1];
            xpath = String.format(XpathTemplate.tmplateAttributeWords, key, value);
        } else if (!text.contains("@") && text.contains("->")) {
            String[] attrs = text.split("->");
            key = attrs[0];
            value = attrs[1];
            xpath = String.format(XpathTemplate.tmplateAttributeWords, key, value);
        } else if (!text.contains("@") && text.contains("=")) {
            String[] attrs = text.split("=");
            key = attrs[0];
            value = attrs[1];
            xpath = String.format(XpathTemplate.tmplateAttributeWords, key, value);
        } else {
            throw new IllegalArgumentException("Illegal attribute: " + text);
        }
        return start;
    }

    public Xpath normalizeSpace() {
        if (flag) {
            xpath = String.format(XpathTemplate.templateTextFuzzy, "normalize-space", value).replace("),", "))=").replace("\")", "\"");
        } else {
            xpath = String.format(XpathTemplate.templateAttributeFuzzy, "normalize-space", key.replace("@", ""), value).replace(",", ")=").replace("\")", "\"");
        }
        return start;
    }

    public Xpath contains() {
        if (flag) {
            xpath = String.format(XpathTemplate.templateTextFuzzy, "contains", value);
        } else {
            xpath = String.format(XpathTemplate.templateAttributeFuzzy, "contains", key, value);
        }
        return start;
    }

    public Xpath startsWith() {
        if (flag) {
            xpath = String.format(XpathTemplate.templateTextFuzzy, "starts-with", value);
        } else {
            xpath = String.format(XpathTemplate.templateAttributeFuzzy, "starts-with", key, value);
        }
        return start;
    }

    public Xpath containsAndNormalizeSpace() {
        if (flag) {
            xpath = String.format(XpathTemplate.templateFuzzyAndSpace, "text()", "contains", value);
        } else {
            xpath = String.format(XpathTemplate.templateFuzzyAndSpace, "@" + key, "contains", value);
        }
        return start;
    }

    public Xpath startsWithAndNormalizeSpace() {
        if (flag) {
            xpath = String.format(XpathTemplate.templateFuzzyAndSpace, "text()", "starts-with", value);
        } else {
            xpath = String.format(XpathTemplate.templateFuzzyAndSpace, "@" + key, "contains", value);
        }
        return start;
    }

    public Xpath ignoreCase() {
        Matcher m = Pattern.compile("(\\[)(.*?)(])").matcher(xpath);
        if (m.find()) {
            String re = m.group(2);
            if (re.contains("@")) {
                Matcher m1 = Pattern.compile("(@)(.*?)(=)").matcher(xpath);
                Matcher m2 = Pattern.compile("(\")(.*?)(\")").matcher(xpath);
                if (m1.find() && m2.find()) {
                    xpath = xpath.replace(xpath, String.format(XpathTemplate.templateCaseInsensitive, "@" + m1.group(2), UP, LO, m2.group(2).toLowerCase()));
                }
            } else if (re.contains("text")) {
                Matcher m1 = Pattern.compile("(\")(.*?)(\")").matcher(xpath);
                if (m1.find()) {
                    xpath = xpath.replace(xpath, String.format(XpathTemplate.templateCaseInsensitive, "text()", UP, LO, m1.group(2).toLowerCase()));
                }
            }
        }
        return start;
    }

    public Xpath ignoreCaseContains() {
        Matcher m = Pattern.compile("(\\[)(.*?)(])").matcher(xpath);
        if (m.find()) {
            String re = m.group(2);
            if (re.contains("@")) {
                Matcher m1 = Pattern.compile("(@)(.*?)(=)").matcher(xpath);
                Matcher m2 = Pattern.compile("(\")(.*?)(\")").matcher(xpath);
                if (m1.find() && m2.find()) {
                    xpath = xpath.replace(xpath, String.format(XpathTemplate.templateContainsCaseInsensitive, "@" + m1.group(2), UP, LO, m2.group(2).toLowerCase()));
                }
            } else if (re.contains("text")) {
                Matcher m1 = Pattern.compile("(\")(.*?)(\")").matcher(xpath);
                if (m1.find()) {
                    xpath = xpath.replace(xpath, String.format(XpathTemplate.templateContainsCaseInsensitive, "text()", UP, LO, m1.group(2).toLowerCase()));
                }
            }
        }
        return start;
    }


    public Xpath tag(String tag) {
        xpath = xpath.replaceFirst("\\*\\[", tag + "\\[");
        return start;
    }

    public Xpath leftPath(String leftPath) {
        xpath = xpath.replaceFirst("(//)(.*?)(\\[)", leftPath + "[");
        return start;
    }

    public Xpath rightPath(String rightPath) {
        if (xpath.contains("])")) {
            xpath = xpath.replaceFirst("]\\)", "]" + rightPath + ")");
        } else {
            xpath = xpath.replaceFirst("\"]", "\"]" + rightPath);
        }
        return start;
    }

    public Xpath index(int index) {
        xpath = String.format(XpathTemplate.templateIndex, xpath, index);
        return start;
    }

    public Xpath index(String index) {
        xpath = String.format(XpathTemplate.templateIndex, xpath, index);
        return start;
    }

    public Xpath indexFirst() {
        xpath = String.format(XpathTemplate.templateIndex, xpath, "1");
        return start;
    }

    public Xpath indexLast() {
        xpath = String.format(XpathTemplate.templateIndex, xpath, "last()");
        return start;
    }

    public Xpath folSibling(String slash, String tag) {
        buildAxis(XpathTemplate.templateFollowingSibling, slash, tag);
        return start;
    }

    public Xpath folSibling() {
        buildAxis(XpathTemplate.templateFollowingSibling, "/", "*");
        return start;
    }


    public Xpath preSibling(String slash, String tag) {
        buildAxis(XpathTemplate.templatePrecedingSibling, slash, tag);
        return start;
    }

    public Xpath preSibling() {
        buildAxis(XpathTemplate.templatePrecedingSibling, "/", "*");
        return start;
    }

    public Xpath following(String slash, String tag) {
        buildAxis(XpathTemplate.templateFollowing, slash, tag);
        return start;
    }

    public Xpath following() {
        buildAxis(XpathTemplate.templateFollowing, "/", "*");
        return start;
    }

    public Xpath preceding(String slash, String tag) {
        buildAxis(XpathTemplate.templatePreceding, slash, tag);
        return start;
    }

    public Xpath preceding() {
        buildAxis(XpathTemplate.templatePreceding, "/", "*");
        return start;
    }

    public Xpath self(String slash, String tag) {
        buildAxis(XpathTemplate.templateSelf, slash, tag);
        return start;
    }

    public Xpath self() {
        buildAxis(XpathTemplate.templateSelf, "/", "*");
        return start;
    }

    public Xpath parent(String slash, String tag) {
        buildAxis(XpathTemplate.templateParent, slash, tag);
        return start;
    }

    public Xpath parent() {
        buildAxis(XpathTemplate.templateParent, "/", "*");
        return start;
    }

    public Xpath ancestor(String slash, String tag) {
        buildAxis(XpathTemplate.templateAncestor, slash, tag);
        return start;
    }

    public Xpath ancestor() {
        buildAxis(XpathTemplate.templateAncestor, "/", "*");
        return start;
    }

    public Xpath ancestor(String tag) {
        buildAxis(XpathTemplate.templateAncestor, "/", tag);
        return start;
    }

    public Xpath ancestorOrSelf(String slash, String tag) {
        buildAxis(XpathTemplate.templateAncestorOrSelf, slash, tag);
        return start;
    }

    public Xpath ancestorOrSelf() {
        buildAxis(XpathTemplate.templateAncestorOrSelf, "/", "*");
        return start;
    }

    public Xpath child(String slash, String tag) {
        buildAxis(XpathTemplate.templateChild, slash, tag);
        return start;
    }

    public Xpath child() {
        buildAxis(XpathTemplate.templateChild, "/", "*");
        return start;
    }

    public Xpath descendant(String slash, String tag) {
        buildAxis(XpathTemplate.templateDescendant, slash, tag);
        return start;
    }

    public Xpath descendant() {
        buildAxis(XpathTemplate.templateDescendant, "/", "*");
        return start;
    }

    public Xpath descendantOrSelf(String slash, String tag) {
        buildAxis(XpathTemplate.templateDescendantOrSelf, slash, tag);
        return start;
    }

    public Xpath descendantOrSelf() {
        buildAxis(XpathTemplate.templateDescendantOrSelf, "/", "*");
        return start;
    }

    public Xpath path(String path) {
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

    public static void main(String[] args) {
        System.out.println(Xpath.text("customerOrganizationName").ancestor("/", "tr").build() + Xpath.attribute("DETAIL_ICON->223").build());
    }

}
