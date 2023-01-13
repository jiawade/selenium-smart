package com.selenium.utils.xpath;

public interface XpathTemplate {
    public static final String templateTextWords = "//*[text()=\"%s\"]";
    public static final String templateTextFuzzy = "//*[%s(text(),\"%s\")]";
    public static final String templateFuzzyAndSpace = "//*[%s[%s(normalize-space(.), \"%s\")]]";
    public static final String tmplateAttributeWords = "//*[@%s=\"%s\"]";
    public static final String templateAttributeFuzzy = "//*[%s(@%s,\"%s\")]";
    public static final String templateCaseInsensitive = "//*[translate(%s,\"%s\",\"%s\") = \"%s\"]";
    public static final String templateContainsCaseInsensitive ="(//*[*[%s[contains(translate(., \"%s\",\"%s\"), \"%s\")]]])";
    public static final String templateIndex ="(%s)[%s]";

    public static final String templateFollowing="%sfollowing::%s";
    public static final String templatePreceding="%spreceding::%s";

    public static final String templateFollowingSibling="%sfollowing-sibling::%s";
    public static final String templatePrecedingSibling="%spreceding-sibling::%s";

    public static final String templateSelf="%sself::*";

    public static final String templateParent="%sparent::%s";
    public static final String templateAncestor="%sancestor::%s";
    public static final String templateAncestorOrSelf="%sancestor-or-self::%s";

    public static final String templateChild="%schild::%s";
    public static final String templateDescendant="%sdescendant::%s";
    public static final String templateDescendantOrSelf="%sdescendant-or-self::%s";


}
