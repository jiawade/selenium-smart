package io.smart.utils;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Slf4j
public class JacksonUtils {

    private static final ObjectMapper mapper;

    static {
        mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

	private JacksonUtils() {

	}


    public static Map<String, Object> convertJsonToMap(String jsonString) {
        Map map = new HashMap<>();
        try {
            map = mapper.readValue(jsonString, Map.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return map;
    }

    public static JsonNode parseStringToJsonNode(String jsonStr) {
        try {
            return mapper.readTree(jsonStr);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T> T fromJson(String json, Class<T> clazz) {
        T object = null;
        try {
            object = mapper.readValue(json, clazz);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return object;
    }

    public static <T> T fromJson(JsonNode json, Class<T> clazz) {
        T object = null;
        try {
            object = mapper.readValue(json.toString(), clazz);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return object;
    }

    public static <T> T convertJsonNodeToList(JsonNode jsonNode, Class<T> type) {
		ArrayList<T> list = new ArrayList<T>();
        try {
            if (jsonNode == null) {
                return null;
            }
            list = mapper.readValue(jsonNode.toString(), mapper.getTypeFactory().constructParametricType(ArrayList.class, type));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return (T) list;


    }


    public static <T> T convertJsonNodeToList(String jsonString, Class<T> type) {
        ArrayList<T> list = new ArrayList<T>();
        try {
            list = mapper.readValue(jsonString, mapper.getTypeFactory().constructParametricType(ArrayList.class, type));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return (T) list;
    }

    public static <T> String toJson(T obj) {
        try {
            return mapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }


    public static <T> T fromJson(String json, TypeReference<T> reference) {
        try {
            return mapper.readValue(json, reference);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    public static <T> TreeNode fromTree(String json) {
        JsonFactory factory = new JsonFactory();
        try {
            JsonParser parser = factory.createParser(json);
            return mapper.readTree(parser);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }

        return null;
    }

}
