package org.rest.facade.marshall;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.rest.facade.RestArg;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import java.io.StringReader;

/**
 * A wrapper for the JAXB API a utility to go from xml to object.
 */
public class JAXBWrapper {

    private static final Logger logger = LogManager.getLogger(JAXBWrapper.class);

    public RestArg unmarshallArg(String xml, Class<?> clazz) {
        RestArg arg = null;
        try {
            JAXBContext jc = JAXBContext.newInstance(clazz);
            Unmarshaller unmarshaller = jc.createUnmarshaller();
            JAXBElement element = unmarshaller.unmarshal(new StreamSource(new StringReader(xml)), clazz);
            arg = (RestArg) element.getValue();
        } catch (JAXBException e) {
            logger.error(e.getMessage());
        }
        return arg;
    }

}
