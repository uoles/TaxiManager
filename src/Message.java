import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by uoles on 26.04.2016.
 */

public class Message implements Runnable {
    private int dispatchedId;
    private int targetId = 0;
    private String xml;
    private IPerformer performer;

    public Message(int dispatchedId, String xml, ArrayList<IPerformer> performers) {
        this.dispatchedId = dispatchedId;
        this.xml = xml;
        this.performer = performers.get(getTargetId() - 1);
        new Thread(this, "Message").start();
    }

    @Override
    public void run() {
        try {
            updateXML();
            sendMessage();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendMessage() {
        while (!performer.isFree()) {
            //...
        }
        performer.saveMessage(this);
    }

    public int getTargetId() {
        if (targetId == 0) {
            targetId = parseTargetId();
        }
        return targetId;
    }

    public int parseTargetId() {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            Document doc = fromXML(xml);

            NodeList nodeList = doc.getChildNodes();
            Node xmldata = nodeList.item(0);

            NodeList data = xmldata.getChildNodes();
            for (int i = 0; i < data.getLength(); i++) {
                if (data.item(i).getNodeName().equals("target")) {
                    return Integer.parseInt( data.item(i).getAttributes().getNamedItem("id").getTextContent() );
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static String createXML() throws ParserConfigurationException {
        final Random random = new Random();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        Document doc = factory.newDocumentBuilder().newDocument();
        doc.setXmlStandalone(true);
        try {
            Element root = doc.createElement("message");
            Element itemTarget = doc.createElement("target");
            itemTarget.setAttribute("id", Integer.toString(random.nextInt(9) + 1));
            root.appendChild(itemTarget);

            Element sometags = doc.createElement("sometags");
            for (int i = 0; i < 3; i++) {
                Element data = doc.createElement("data");
                sometags.appendChild(data);
            }
            root.appendChild(sometags);

            doc.appendChild(root);
            return toXML(doc);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String toXML(Document document) throws TransformerException, IOException {
        OutputFormat format = new OutputFormat(document);
        Writer out = new StringWriter();
        try {
            format.setLineWidth(65);
            format.setIndenting(true);
            format.setIndent(2);
            XMLSerializer serializer = new XMLSerializer(out, format);
            serializer.serialize(document);

            return out.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Document fromXML(String xml) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(new ByteArrayInputStream(xml.getBytes("UTF-8")));
        doc.setXmlStandalone(true);
        return doc;
    }

    public void updateXML() throws IOException, SAXException, ParserConfigurationException {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            Document doc = fromXML(xml);

            NodeList nodeList = doc.getChildNodes();
            Node xmldata = nodeList.item(0);

            Element dispatched = doc.createElement("dispatched");
            dispatched.setAttribute("id", Integer.toString(dispatchedId));
            xmldata.appendChild(dispatched);

            xml = toXML(doc);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveXML() throws IOException, SAXException, ParserConfigurationException {
        try {
            File folderMrt = new File(System.getProperty("user.dir") + "/Test/" + Integer.toString(getTargetId()) + "/");
            if (!folderMrt.isDirectory()) {
                folderMrt.mkdirs();
            }

            String filepath_out = System.getProperty("user.dir") + "/Test/" + Integer.toString(getTargetId()) + "/" + Integer.toString(dispatchedId) + ".xml";
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            DOMSource source = new DOMSource(fromXML(xml));
            StreamResult result = new StreamResult(new File(filepath_out));
            transformer.transform(source, result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
