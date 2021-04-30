package Model;

import java.io.Serializable;
import java.util.*;
import org.w3c.dom.*;

/**
 * cette classe permet de créer des objets AudioElements
 */
public abstract class AudioElement implements Serializable {
    protected String  	title;
    protected String 	artist;
    protected int    	lengthInSeconds;
    protected UUID    	uuid;
    protected String	content;

    /**
     *  Ceci est un constructeur pour la classe AudioElement. Il permet au constructeur de créer de nouvel AudioElement
     * @param title titre de l'AudioElement
     * @param artist artiste de l'AudioElement
     * @param lengthInSeconds durée de l'AudioElement (en seconde)
     * @param id l'ID de l'element
     * @param content nom du fichier de l'element audio
     */
    public AudioElement (String title, String artist, int lengthInSeconds, String id, String content) {
        this.title = title;
        this.artist = artist;
        this.lengthInSeconds = lengthInSeconds;
        this.uuid = UUID.fromString(id);
        this.content = content;
    }

    /**
     * Ceci est un constructeur pour la classe AudioElement. Il permet au constructeur de créer de nouvel AudioElement qui na pas de UUID
     * @param title titre de l'AudioElement
     * @param artist artiste de l'AudioElement
     * @param lengthInSeconds durée de l'AudioElement (en seconde)
     * @param content nom du fichier de l'element audio
     */
    public AudioElement (String title, String artist, int lengthInSeconds, String content) {
        this.title = title;
        this.artist = artist;
        this.lengthInSeconds = lengthInSeconds;
        this.content = content;
        this.uuid =  UUID.randomUUID();
    }

    /**
     * ceci est un constructeur permettant de convertir les elements xml en AudioElement
     * @param xmlElement prent un element du fichier elements.xml pour le convertir en objet AudioElement
     * @throws Exception releve une exception
     */
    public AudioElement (Element xmlElement)  throws Exception
    {
        try {
            title = xmlElement.getElementsByTagName("title").item(0).getTextContent();
            artist = xmlElement.getElementsByTagName("artist").item(0).getTextContent();
            lengthInSeconds = Integer.parseInt(xmlElement.getElementsByTagName("length").item(0).getTextContent());
            content = xmlElement.getElementsByTagName("content").item(0).getTextContent();
            String uuid = null;
            try {
                uuid = xmlElement.getElementsByTagName("UUID").item(0).getTextContent();
            }
            catch (Exception ex) {
                System.out.println ("Empty element UUID, will create a new one");
            }
            if ((uuid == null)  || (uuid.isEmpty()))
                this.uuid = UUID.randomUUID();
            else this.uuid = UUID.fromString(uuid);
        } catch (Exception ex) {
            throw ex;
        }
    }

    /**
     * getter permetant de recuperer L'UUID
     * @return retourne un UUID
     */
    public UUID getUUID() {
        return this.uuid;
    }

    /**
     * getter permetant de recuperer L'artiste l'element audio
     * @return retourne un l'artiste de l' audio
     */
    public String getArtist() {
        return this.artist;
    }

    /**
     * getter permetant de recuperer Le titre
     * @return retourne le titre
     */
    public String getTitle() {
        return this.title;
    }

    /**
     * permet d'afficher les informations de l'AudioElement
     * @return retourne les informations en String
     */
    public String toString() {
        return "Title = " + this.title + ", Artist = " + this.artist + ", Length = " + this.lengthInSeconds + ", Content = " + this.content;
    }

    /**
     * un fonction qui permet de creer un element xml pour pouvoir l' ecrire dans le fichier XML
     * @param document c'est le nom du document à modifier avec son chemin
     * @param parentElement nom de l element qui englobe l'element present pour l'ecrire dans le fichier
     */
    public void createXMLElement(Document document, Element parentElement)
    {
        Element nameElement = document.createElement("title");
        nameElement.appendChild(document.createTextNode(title));
        parentElement.appendChild(nameElement);

        Element artistElement = document.createElement("artist");
        artistElement.appendChild(document.createTextNode(artist));
        parentElement.appendChild(artistElement);

        Element lengthElement = document.createElement("length");
        lengthElement.appendChild(document.createTextNode(Integer.valueOf(lengthInSeconds).toString()));
        parentElement.appendChild(lengthElement);

        Element UUIDElement = document.createElement("UUID");
        UUIDElement.appendChild(document.createTextNode(uuid.toString()));
        parentElement.appendChild(UUIDElement);

        Element contentElement = document.createElement("content");
        contentElement.appendChild(document.createTextNode(content));
        parentElement.appendChild(contentElement);

    }

    /**
     * getter permetant de recuperer la contenue de l'element audio
     * @return retourne la contenue
     */
    public String getContent() {
        return content;
    }


}