package AddressBook;

import java.io.File;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import org.w3c.dom.Attr;

import java.time.LocalDate;
import java.util.UUID;

public class AddressBook {
    private static Document document;
    private static NodeList contacts;
    private static File file;
    private static String filename;

    public AddressBook() {
        this("contacts.xml");
    }

    public AddressBook(String filename) {
        try {
            AddressBook.filename = filename;
            createXmlIfNotExists();
            openFile();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ObservableList<Contact> getContacts() {
        ObservableList<Contact> contactsList = FXCollections.observableArrayList();

        int size = contacts.getLength();

        for (int i = 0; i < size; i++) {
            Node nNode = contacts.item(i);

            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element e = (Element) nNode;
                contactsList.add(readContact(e));
            }
        }

        return contactsList;
    }

    private String generateID() {
        return UUID.randomUUID().toString();
    }

    void createXmlIfNotExists() throws Exception {
        AddressBook.file = new File(filename);

        if (!file.exists()) {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.newDocument();

            doc.appendChild(doc.createElement("contacts"));

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(file);
            transformer.transform(source, result);
        }
    }

    void openFile() throws Exception {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        document = dBuilder.parse(file);
        document.getDocumentElement().normalize();

        contacts = document.getFirstChild().getChildNodes();
    }

    String getTextFromElement(Element e, String tagName) {
        return e.getElementsByTagName(tagName).item(0).getTextContent();
    }

    Contact readContact(Element e) {
        String firstname = getTextFromElement(e, "firstname");
        String lastname = getTextFromElement(e, "lastname");
        String address = getTextFromElement(e, "address");
        String city = getTextFromElement(e, "city");
        String state = getTextFromElement(e, "state");
        String zipcode = getTextFromElement(e, "zipcode");
        String contactID = e.getAttribute("id");
        LocalDate date = LocalDate.parse(getTextFromElement(e, "birthday"));

        return new Contact(firstname, lastname, address, city, state, zipcode, contactID, date);
    }

    Element createElement(Contact contact) {
        Element element = document.createElement("contact");

        Element firstname = document.createElement("firstname");
        Element lastname = document.createElement("lastname");
        Element address = document.createElement("address");
        Element city = document.createElement("city");
        Element state = document.createElement("state");
        Element zipcode = document.createElement("zipcode");
        Element birthday = document.createElement("birthday");

        firstname.appendChild(document.createTextNode(contact.getFirstname()));
        lastname.appendChild(document.createTextNode(contact.getLastname()));
        address.appendChild(document.createTextNode(contact.getAddress()));
        city.appendChild(document.createTextNode(contact.getCity()));
        state.appendChild(document.createTextNode(contact.getState()));
        zipcode.appendChild(document.createTextNode(contact.getZipcode()));
        birthday.appendChild(document.createTextNode(contact.getBirthDate().toString()));

        Attr idAttribute = document.createAttribute("id");
        idAttribute.setValue(contact.contactID);

        element.appendChild(firstname);
        element.appendChild(lastname);
        element.appendChild(address);
        element.appendChild(city);
        element.appendChild(state);
        element.appendChild(zipcode);
        element.appendChild(birthday);
        element.setAttributeNode(idAttribute);

        return element;
    }

    void createContact(String first, String last, String address, String city, String state, String zipcode, LocalDate birthday) throws Exception {
        Contact contact = new Contact(first, last, address, city, state, zipcode, generateID(), birthday);
        Element contactElem = createElement(contact);

        document.getFirstChild().appendChild(contactElem);
        updateXML();
    }

    public void removeContact(Contact contactToRemove) throws Exception {
        for (int count = 0; count < contacts.getLength(); count++) {
            Node node = contacts.item(count);

            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element e = (Element) node;

                Contact contactToCompare = readContact(e);

                if (contactToRemove.equals(contactToCompare)) {
                    document.getFirstChild().removeChild(node);
                    break;
                }
            }
        }

        updateXML();
    }

    public void updateContact(Contact contact) throws Exception {

        for (int count = 0; count < contacts.getLength(); count++) {
            Node node = contacts.item(count);

            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element e = (Element) node;

                String contactID_Element = e.getAttribute("id");

                if (contactID_Element == contact.contactID) {
                    // Update all elements inside e

                    Node first = e.getElementsByTagName("firstname").item(0).getFirstChild();
                    Node last = e.getElementsByTagName("lastname").item(0).getFirstChild();
                    Node address = e.getElementsByTagName("address").item(0).getFirstChild();
                    Node city = e.getElementsByTagName("city").item(0).getFirstChild();
                    Node state = e.getElementsByTagName("state").item(0).getFirstChild();
                    Node zipcode = e.getElementsByTagName("zipcode").item(0).getFirstChild();
                    Node birthday = e.getElementsByTagName("birthday").item(0).getFirstChild();

                    first.setNodeValue(contact.getFirstname());
                    last.setNodeValue(contact.getLastname());
                    address.setNodeValue(contact.getAddress());
                    city.setNodeValue(contact.getCity());
                    state.setNodeValue(contact.getState());
                    zipcode.setNodeValue(contact.getZipcode());
                    birthday.setNodeValue(contact.getBirthDate().toString());

                    break;
                }
            }
        }

        updateXML();
    }

    void updateXML() throws Exception {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(document);
        StreamResult result = new StreamResult(file);
        transformer.transform(source, result);
    }

}
