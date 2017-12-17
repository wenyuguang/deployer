package distributed.deployer.core.utils;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtil {
    private static ObjectMapper mapper = new ObjectMapper();

    public JsonUtil() {
    }

    public static ObjectMapper mapper() {
        return mapper;
    }

    public static <T> T readString(String str, Class<T> clazz) {
        try {
            return mapper.readValue(str, clazz);
        } catch (Exception var3) {
            var3.printStackTrace();
            return null;
        }
    }
}