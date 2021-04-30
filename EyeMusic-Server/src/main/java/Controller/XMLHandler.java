package Controller;

import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.xml.sax.SAXException;

import org.w3c.dom.*;
import java.io.IOException;
import java.io.File;


/**
 * classe qui perrmet de gerer les fichiers XML
 */
public class XMLHandler {
    TransformerFactory transformerFactory;
    Transformer transformer;
    DocumentBuilderFactory documentFactory;
    DocumentBuilder documentBuilder;

    /**
     * constructeur de la classe XMLHandler
     */
    public XMLHandler() {
        try {
            transformerFactory = TransformerFactory.newInstance();
            transformer = transformerFactory.newTransformer();
            documentFactory = DocumentBuilderFactory.newInstance();
            documentBuilder = documentFactory.newDocumentBuilder();
        } catch (TransformerException | ParserConfigurationException tfe) {
            tfe.printStackTrace();
        }
    }

    /**
     * permet de créer un fichier XML
     * @param document Nom du Document en question
     * @param filePath chemin d'acces vers le fishier
     */
    public void createXMLFile(Document document, String filePath)
    {
        try {
            // create the xml file
            //transform the DOM Object to an XML File
            DOMSource domSource = new DOMSource(document);
            StreamResult streamResult = new StreamResult(new File(filePath));

            // If you use
            // StreamResult result = new StreamResult(System.out);
            // the output will be pushed to the standard output ...
            // You can use that for debugging

            transformer.transform(domSource, streamResult);

        } catch (TransformerException tfe) {
            tfe.printStackTrace();
        }
    }

    /**
     * creer un document XML
     * @return retourne un Document
     */
    public Document createXMLDocument()
    {
        return documentBuilder.newDocument();
    }

    /**
     * creer un noeud dans l'element
     * @param filePath le chemin d'acces en parametre
     * @return retourne un noeux de l'element
     */
    public NodeList parseXMLFile (String filePath) {
        NodeList elementNodes = null;
        try {
            Document document= documentBuilder.parse(new File(filePath));
            Element root = document.getDocumentElement();

            elementNodes = root.getChildNodes();
        }
        catch (SAXException | IOException e)
        {
            e.printStackTrace();
        }
        return elementNodes;
    }



}