package net.qsef1256.dacobot.util;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;

@UtilityClass
public class XmlUtil {

    @SneakyThrows
    public SAXReader getReader() {
        SAXReader reader = new SAXReader();

        reader.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
        return reader;
    }

    public Document read(String filePath) throws IOException, DocumentException {
        return getReader().read(new URL(filePath));
    }

    public void write(Document document, String filePath) throws IOException {
        try (FileWriter fileWriter = new FileWriter(filePath)) {
            OutputFormat format = OutputFormat.createPrettyPrint();
            XMLWriter writer = new XMLWriter(fileWriter, format);
            writer.write(document);
            writer.close();
        }
    }

    public Document parse(URL url) throws DocumentException {
        return getReader().read(url);
    }

    public Document parse(BufferedReader br) throws DocumentException {
        return getReader().read(br);
    }

}
