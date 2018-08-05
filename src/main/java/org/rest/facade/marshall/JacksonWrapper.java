package org.rest.facade.marshall;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.SerializerProvider;
import org.codehaus.jackson.map.ser.CustomSerializerFactory;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Wrapper for the <tt>org.codehaus.jackson</tt> library.
 *
 * @author A. Rodriguez
 */
public class JacksonWrapper {

    private static final Logger logger = LogManager.getLogger(JacksonWrapper.class);

    public static String toJSON(Object obj) {
        try {
            return createObjectMapper().writeValueAsString(obj);
        } catch (IOException e) {
            logger.error("JacksonWrapper failed to write object as string " + e);
            return "";
        }
    }

    public static <E> E fromJSON(String json, Class<E> clazz) {
        try {
            return createObjectMapper().readValue(json, clazz);
        } catch (Exception e) {
            logger.error("JacksonWrapper failed to convert from json "
                    + clazz.getCanonicalName() + " - " + e.getMessage());
            return null;
        }
    }

    public static ObjectMapper createObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        CustomSerializerFactory csf = new CustomSerializerFactory();
        csf.addSpecificMapping(Date.class, new DateSerializer());
        mapper.setSerializerFactory(csf);
        mapper.configure(SerializationConfig.Feature.INDENT_OUTPUT, true);
        return mapper;
    }

    private static class DateSerializer extends JsonSerializer<Date> {

        @Override
        public void serialize(Date date, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
                throws IOException {
            if (date == null) {
                serializerProvider.defaultSerializeValue(null, jsonGenerator);
                return;
            }
            String isoDateTime = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZZ").format(date);
            serializerProvider.defaultSerializeValue(isoDateTime, jsonGenerator);
            return;
        }
    }

}
