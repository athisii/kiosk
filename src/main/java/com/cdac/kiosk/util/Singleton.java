package com.cdac.kiosk.util;

import com.fasterxml.jackson.core.Base64Variants;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.json.JsonMapper;


/**
 * @author athisii, CDAC
 * Created on 20/12/22
 */
public class Singleton {
    private static ObjectMapper objectMapper;

    public static ObjectMapper getObjectMapper() {
        if (objectMapper == null) {
            objectMapper = JsonMapper.builder()
                    .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                    .configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true)
                    .propertyNamingStrategy(PropertyNamingStrategies.LOWER_CAMEL_CASE) // to change property naming automatically
                    .build();
            objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
            objectMapper.setBase64Variant(Base64Variants.MIME_NO_LINEFEEDS);
        }
        return objectMapper;
    }

    //Suppress default constructor for noninstantiability
    private Singleton() {
        throw new AssertionError("The Singleton methods must be accessed statically.");
    }


}
