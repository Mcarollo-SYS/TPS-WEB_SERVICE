package web_service.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;

public class XmlUtils {
    private XmlUtils() {
    }

    public static <T> String marshal(T x) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(x.getClass());
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, false);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        marshaller.marshal(x, baos);
        return baos.toString(StandardCharsets.UTF_8);
    }

    public static <T> T unmarshal(Class<T> klass, String xml) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(klass);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        byte[] bytes = xml.getBytes(StandardCharsets.UTF_8);
        ByteArrayInputStream stream = new ByteArrayInputStream(bytes);
        return (T) unmarshaller.unmarshal(stream);
    }

}