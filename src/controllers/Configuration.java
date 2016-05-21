package controllers;


import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.DoubleSummaryStatistics;

/**
 * Created by HP PC on 5/18/2016.
 */
public class Configuration {
    Document document;
    public Configuration(InputStream is) throws IOException, SAXException, ParserConfigurationException {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory
                .newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        document = documentBuilder.parse(is);
    }

    public String getString(String key) throws XPathExpressionException {
        XPath xPath = XPathFactory.newInstance().newXPath();
        Node node = (Node) (xPath.compile(key).evaluate(document, XPathConstants.NODE));
        return node.getTextContent();
    }

    public int getInteger(String key) throws XPathExpressionException {
        return Integer.parseInt(getString(key));
    }

    public double getDouble(String key) throws XPathExpressionException {
        return Double.parseDouble(getString(key));
    }
}
