package Model;

import org.w3c.dom.*;

import java.io.Serializable;

/**
 * cette classe permet de créer des objets Songs
 */
public class Song extends AudioElement implements Serializable {
    private Genre genre;

    /**
     * constructeur de Song avec cinq parametres permettant à l'utilisateur (par le biais du seveur) à créer des chanson
     * @param title titre de la chanson
     * @param artist artiste de la chanson
     * @param length taille de la chanson en seconde
     * @param content nom du fichier de la chanson en .wav
     * @param genre le genre de la chanson (jazz, classic, hiphop, rock, pop, rap)
     */
    public Song (String title, String artist, int length, String content, String genre) {
        super (title, artist, length, content);
        this.setGenre(genre);
    }

    /**
     * constructeur de Song qui sont géneré à partir du fichier elements.xml
     * @param xmlElement un element du fichier elements.xml
     * @throws Exception
     */
    public Song (Element xmlElement) throws Exception {
        super(xmlElement);
        this.setGenre(xmlElement.getElementsByTagName("genre").item(0).getTextContent());
    }

    /**
     * setter pour l'attribut genre
     * @param genre un string qui permettra de reconnaitre le genre
     */
    public void setGenre (String genre) {
        switch (genre.toLowerCase()) {
            default -> this.genre = Genre.JAZZ;
            case "classic" -> this.genre = Genre.CLASSIC;
            case "hiphop" -> this.genre = Genre.HIPHOP;
            case "rock" -> this.genre = Genre.ROCK;
            case "pop" -> this.genre = Genre.POP;
            case "rap" -> this.genre = Genre.RAP;
        }
    }

    /**
     * fonction getter de genre
     * @return recupere le genre de la chanson
     */
    public String getGenre () {
        return genre.getGenre();
    }

    /**
     * permet d'avoir un string des different attribut de l'objet Song
     * @return un string
     */
    public String toString() {
        return super.toString() + ", Genre = " + getGenre() + "\n";
    }

    /**
     * un fonction qui permet de creer un element xml pour pouvoir l' ecrire dans le fichier XML
     * @param document c'est le nom du document à modifier avec son chemin
     * @param parentElement nom de l element qui englobe l'element present pour l'ecrire dans le fichier
     */
    public void createXMLElement(Document document, Element parentElement) {
        // song element
        Element song = document.createElement("song");

        super.createXMLElement(document, song);

        Element genreElement = document.createElement("genre");
        genreElement.appendChild(document.createTextNode(genre.getGenre()));
        song.appendChild(genreElement);

        parentElement.appendChild(song);
    }
}