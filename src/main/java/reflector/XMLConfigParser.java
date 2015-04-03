package reflector;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.SAXException;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 * Created by mihanik
 * 04.04.15 0:54
 * Package: reflector
 */
public class XMLConfigParser extends DefaultHandler {
    private Object result = null;
    private String nodeText = null;

    public Object parseObject(String filePath) {
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();

            SAXParser saxParser = factory.newSAXParser();

            result = null;
            nodeText = null;
            saxParser.parse(filePath, this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public void startElement(String uri, String localName, String qName, Attributes attributes) {
        if(!qName.equals("object")) {
            nodeText = qName;
        } else {
            result = ReflectionHelper.generateObject(attributes.getValue("class"));
        }
    }

    public void endElement(String uri, String localName, String qName) throws SAXException {
        nodeText = null;
    }
    public void characters(char ch[], int start, int length) throws SAXException {
        if(nodeText != null){
            String value = new String(ch, start, length);
            ReflectionHelper.setObjectField(result, nodeText, value);
        }
    }
}
