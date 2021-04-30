package Model;

import java.io.Serializable;
import java.util.*;
import org.w3c.dom.*;

/**
 * cette classe permet de créer des objets playlists
 */
public class PlayList implements Serializable {
    private final String title;
    private final UUID uuid;
    private ArrayList<UUID> elementUUIDs;


    /**
     * constructeur de Playlists
     * @param title titre du playlists
     */
    public PlayList (String title) {
        this.title = title;
        this.uuid = UUID.randomUUID();
        this.elementUUIDs = new ArrayList<>();
    }

    /**
     * permet d'ajouter des chansons à la playlists
     * @param element UUID d'un element audio
     */
    public void addElement (UUID element)
    {
        elementUUIDs.add(element);
    }

    /**
     *  getter pour le titre de la playlist
     * @return retourne le titre de la playlist
     */
    public String getTitle() {
        return title;
    }

    /**
     * ceci est un constructeur permettant de convertir les elements xml en Album
     * @param xmlElement prent un element du fichier playlists.xml pour le convertir en objet Playlist
     */
    public PlayList (Element xmlElement) {
        {
            this.title = xmlElement.getElementsByTagName("title").item(0).getTextContent();

            String uuid = null;
            try {
                uuid = xmlElement.getElementsByTagName("UUID").item(0).getTextContent();
            }
            catch (Exception ex) {
                System.out.println ("Empty playlist UUID, will create a new one");
            }
            if ((uuid == null)  || (uuid.isEmpty()))
                this.uuid = UUID.randomUUID();
            else this.uuid = UUID.fromString(uuid);

            //parse list of elements:
            Node elementsElement = xmlElement.getElementsByTagName("elements").item(0);
            NodeList elementUUIDNodes = elementsElement.getChildNodes();
            if (elementUUIDNodes == null) return;

            this.elementUUIDs = new ArrayList<>();


            for (int i = 0; i < elementUUIDNodes.getLength(); i++) {
                if (elementUUIDNodes.item(i).getNodeType() == Node.ELEMENT_NODE)   {
                    Element elementElement = (Element) elementUUIDNodes.item(i);
                    if (elementElement.getNodeName().equals("UUID")) 	{
                        try {
                            this.addElement(UUID.fromString(elementElement.getTextContent()));
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    /**
     * un fonction qui permet de creer un element xml pour pouvoir l' ecrire dans le fichier XML
     * @param document c'est le nom du document à modifier avec son chemin
     * @param parentElement nom de l element qui englobe l'element present pour l'ecrire dans le fichier
     */
    public void createXMLElement(Document document, Element parentElement)
    {
        Element playlistElement = document.createElement("playlist");
        parentElement.appendChild(playlistElement);

        Element nameElement = document.createElement("title");
        nameElement.appendChild(document.createTextNode(title));
        playlistElement.appendChild(nameElement);

        Element UUIDElement = document.createElement("UUID");
        UUIDElement.appendChild(document.createTextNode(uuid.toString()));
        playlistElement.appendChild(UUIDElement);


        Element elementsElement = document.createElement("elements");
        for (UUID currentUUID : this.elementUUIDs) {

            Element elementUUIDElement = document.createElement("UUID");
            elementUUIDElement.appendChild(document.createTextNode(currentUUID.toString()));
            elementsElement.appendChild(elementUUIDElement);
        }
        playlistElement.appendChild(elementsElement);
    }

}