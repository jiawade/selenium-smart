package io.smart.utils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;
import com.jayway.jsonpath.spi.json.GsonJsonProvider;
import net.minidev.json.JSONArray;

import java.util.List;
import java.util.Map;

public class JaywayUtils {

    public static final Configuration cf = Configuration.builder().options(Option.DEFAULT_PATH_LEAF_TO_NULL).build();
    private String json;

    public JaywayUtils(String json) {
        this.json = json;
    }


    public String getValue(String expression) {
        DocumentContext ctx = JsonPath.using(cf).parse(json);
        List<String> text = ctx.read(expression);
        if (!text.isEmpty()) {
            return text.get(0);
        }
        return ctx.read(expression);
    }

    public List<String> getValues(String expression) {
        DocumentContext ctx = JsonPath.using(cf).parse(json);
        return ctx.read(expression);
    }

    public static JsonObject getObject(String jsonBody, String jsonPath) {
        Configuration conf = Configuration.builder().jsonProvider(new GsonJsonProvider()).build();
        return JsonPath.using(conf).parse(jsonBody).read(jsonPath);
    }


    public static Object getArray(String jsonBody, String jsonPath) {
        JSONArray jsonArray = JsonPath.parse(jsonBody).read(jsonPath);
        return jsonArray.get(0);
    }


    public static JSONArray getArrays(String jsonBody, String jsonPath) {
        return JsonPath.parse(jsonBody).read(jsonPath);
    }

    public JaywayUtils addValue(String jsonPath, String key, Object jsonValue) {
        DocumentContext doc = JsonPath.parse(json).put(jsonPath, key, jsonValue);
        JsonObject jsonObject = JsonParser.parseString(doc.jsonString()).getAsJsonObject();
        json = jsonObject.toString();
        return this;
    }

    //add element to root node
    public JaywayUtils addValue(String key, Object jsonValue) {
        DocumentContext doc = JsonPath.parse(json).put("$", key, jsonValue);
        JsonObject jsonObject = JsonParser.parseString(doc.jsonString()).getAsJsonObject();
        json = jsonObject.toString();
        return this;
    }

    public JaywayUtils editValue(String jsonPath, Object jsonValue) {
        DocumentContext doc = JsonPath.parse(json).set(jsonPath, jsonValue);
        JsonObject jsonObject = JsonParser.parseString(doc.jsonString()).getAsJsonObject();
        json=jsonObject.toString();
        return this;
    }

    /*public String editValue(String jsonPath, Object jsonValue) {
        Configuration configuration = Configuration.builder()
                .jsonProvider(new GsonJsonProvider())
                .mappingProvider(new GsonMappingProvider())
                .build();
        JsonObject updatedJson = JsonPath.using(configuration).parse(json).set(jsonPath, jsonValue).json();
        return updatedJson.toString();
    }*/

    public JaywayUtils editValue(Map<String, Object> modify) {
        for (Map.Entry<String, Object> entry : modify.entrySet()) {
            DocumentContext doc = JsonPath.parse(json).set(entry.getKey(), entry.getValue());
            JsonObject jsonObject = JsonParser.parseString(doc.jsonString()).getAsJsonObject();
            json = jsonObject.toString();
        }
        return this;
    }

    public JaywayUtils removeNode(List<String> jsonPath) {
        for (String i : jsonPath) {
            DocumentContext doc = JsonPath.parse(json).delete(i);
            JsonObject jsonObject = JsonParser.parseString(doc.jsonString()).getAsJsonObject();
            json = jsonObject.toString();
        }
        return this;
    }


    public String getJsonString(){
        return this.json;
    }



}
