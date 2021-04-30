package Model;

import java.io.Serializable;
import java.util.*;
import org.w3c.dom.*;
import java.text.*;


/**
 * cette classe permet de créer des objets albums
 */
public class Album implements Serializable {
    private final String title;
    private String artist;
    private final int lengthInSeconds;
    private final UUID uuid;
    private Date date;
    private ArrayList<UUID> songsUIDs;

    /**
     * Ceci est un constructeur pour la classe Album. Il permet au constructeur de créer de nouvel album
     * @param title titre de l'album
     * @param artist nom de l'artiste de l'album
     * @param lengthInSeconds durée de l'album en secondes
     * @param date parution de l'album en format YYYY-MM-DD
     */
    public Album (String title, String artist, int lengthInSeconds, String date) {
        this.title = title;
        this.artist = artist;
        this.lengthInSeconds = lengthInSeconds;
        this.uuid = UUID.randomUUID();
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            this.date = sdf.parse(date);
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        this.songsUIDs = new ArrayList<>();
    }

    /**
     * ceci est un constructeur permettant de convertir les elements xml en Album
     * @param xmlElement prent un element du fichier albums.xml pour le convertir en objet Album
     * @throws Exception releve une exception
     */
    public Album (Element xmlElement) throws Exception {
        {
            this.title = xmlElement.getElementsByTagName("title").item(0).getTextContent();
            this.lengthInSeconds = Integer.parseInt(xmlElement.getElementsByTagName("lengthInSeconds").item(0).getTextContent());
            String uuid = null;
            try {
                uuid = xmlElement.getElementsByTagName("UUID").item(0).getTextContent();
            }
            catch (Exception ex) {
                System.out.println ("Empty album UUID, will create a new one");
            }
            if ((uuid == null)  || (uuid.isEmpty()))
                this.uuid = UUID.randomUUID();
            else this.uuid = UUID.fromString(uuid);

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            this.date = sdf.parse(xmlElement.getElementsByTagName("date").item(0).getTextContent());
            //parse list of songs:
            Node songsElement = xmlElement.getElementsByTagName("songs").item(0);
            NodeList songUUIDNodes = songsElement.getChildNodes();
            if (songUUIDNodes == null) return;

            this.songsUIDs = new ArrayList<>();

            for (int i = 0; i < songUUIDNodes.getLength(); i++) {
                if (songUUIDNodes.item(i).getNodeType() == Node.ELEMENT_NODE)   {
                    Element songElement = (Element) songUUIDNodes.item(i);
                    if (songElement.getNodeName().equals("UUID")) 	{
                        try {
                            this.addSong(UUID.fromString(songElement.getTextContent()));
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    /**
     * fonction qui permet de rajouter une nouvel chanson à la liste de l'album
     * @param song prend une Song en parametre
     */
    public void addSong (UUID song)
    {
        songsUIDs.add(song);
    }

    /**
     * permet de recuperer la liste des chansons
     * @return retourne la liste des UUID des chansons
     */
    public List<UUID> getSongs() {
        return songsUIDs;
    }

    /**
     *  getter pour le titre de l'album
     * @return retourne le titre de l'album
     */
    public String getTitle() {
        return title;
    }

    /**
     * getter pour la date de l'album
     * @return retourne la date en string
     */
    public Date getDate() {
        return date;
    }

    /**
     * un fonction qui permet de creer un element xml pour pouvoir l' ecrire dans le fichier XML
     * @param document c'est le nom du document à modifier avec son chemin
     * @param parentElement nom de l element qui englobe l'element present pour l'ecrire dans le fichier
     */
    public void createXMLElement(Document document, Element parentElement)
    {
        Element albumElement = document.createElement("album");
        parentElement.appendChild(albumElement);

        Element nameElement = document.createElement("title");
        nameElement.appendChild(document.createTextNode(title));
        albumElement.appendChild(nameElement);

        Element artistElement = document.createElement("artist");
        artistElement.appendChild(document.createTextNode(artist));
        albumElement.appendChild(artistElement);

        Element lengthElement = document.createElement("lengthInSeconds");
        lengthElement.appendChild(document.createTextNode(Integer.valueOf(lengthInSeconds).toString()));
        albumElement.appendChild(lengthElement);

        Element UUIDElement = document.createElement("UUID");
        UUIDElement.appendChild(document.createTextNode(uuid.toString()));
        albumElement.appendChild(UUIDElement);

        Element dateElement = document.createElement("date");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        dateElement.appendChild(document.createTextNode(sdf.format(date)));
        albumElement.appendChild(dateElement);

        Element songsElement = document.createElement("songs");
        for (UUID currentUUID : this.songsUIDs) {
            Element songUUIDElement = document.createElement("UUID");
            songUUIDElement.appendChild(document.createTextNode(currentUUID.toString()));
            songsElement.appendChild(songUUIDElement);
        }
        albumElement.appendChild(songsElement);

    }
}