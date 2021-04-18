package Model;

import org.w3c.dom.*;

import java.io.Serializable;


public class Song extends AudioElement implements Serializable {
    private Genre genre;


    public Song (String title, String artist, int length, String content, String genre) {
        super (title, artist, length, content);
        this.setGenre(genre);
    }

    public Song (Element xmlElement) {
        super(xmlElement);
        this.setGenre(xmlElement.getElementsByTagName("genre").item(0).getTextContent());
    }

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

    public String getGenre () {
        return genre.getGenre();
    }

    public String toString() {
        return super.toString() + ", Genre = " + getGenre() + "\n";
    }

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