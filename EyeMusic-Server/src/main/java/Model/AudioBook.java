package Model;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.Serializable;

/**
 * cette classe permet de créer des objets AudioBooks
 */
public class AudioBook extends AudioElement implements Serializable {
    private Language language;
    private Category category;

    /**
     * Ceci est un constructeur pour la classe AudioBook. Il permet au constructeur de créer de nouvel AudioBook
     * @param title titre de l'audioBook
     * @param artist artiste de l'audioBook
     * @param lengthInSeconds durée de l'AudioBook (en seconde)
     * @param content nom du fichier du livre audio
     * @param language la langue de l'audioBook
     * @param category la category du livre audio
     */
    public AudioBook (String title, String artist, int lengthInSeconds, String content, String language, String category) {
        super (title, artist, lengthInSeconds, content);
        this.setLanguage(language);
        this.setCategory(category);
    }

    /**
     * ceci est un constructeur permettant de convertir les elements xml en AudioBook
     * @param xmlElement prent un element du fichier elements.xml pour le convertir en objet Album
     * @throws Exception releve une exception
     */
    public AudioBook (Element xmlElement) throws Exception {
        super(xmlElement);
        this.setLanguage(xmlElement.getElementsByTagName("language").item(0).getTextContent());
        this.setCategory(xmlElement.getElementsByTagName("category").item(0).getTextContent());
    }

    /**
     * permet de recuperer la langue de l'audio
     * @return retourne un objet Language
     */
    public Language getLanguage() {
        return this.language;
    }

    /**
     * permet de recuperer la categorie de l'audio
     * @return retourne un objet Category
     */
    public Category getCategory() {
        return this.category;
    }

    /**
     * setteur pour modifier la langue
     * @param language prend un String en parametre
     */
    public void setLanguage (String language) {
        switch (language.toLowerCase()) {
            default -> this.language = Language.ENGLISH;
            case "french" -> this.language = Language.FRENCH;
            case "german" -> this.language = Language.GERMAN;
            case "spanish" -> this.language = Language.SPANISH;
            case "italian" -> this.language = Language.ITALIAN;
        }
    }

    /**
     * setteur pour modifier la category
     * @param category prend un String en parametre
     */
    public void setCategory (String category) {
        switch (category.toLowerCase()) {
            default -> this.category = Category.YOUTH;
            case "novel" -> this.category = Category.NOVEL;
            case "theater" -> this.category = Category.THEATER;
            case "documentary" -> this.category = Category.DOCUMENTARY;
            case "speech" -> this.category = Category.SPEECH;
        }
    }

    /**
     * permet d'afficher les informations de l'album
     * @return retourne les informations en String
     */
    public String toString() {
        return super.toString() + ", Language = " + getLanguage() + ", Category = " + getCategory() + "\n";
    }

    /**
     * un fonction qui permet de creer un element xml pour pouvoir l' ecrire dans le fichier XML
     * @param document c'est le nom du document à modifier avec son chemin
     * @param parentElement nom de l element qui englobe l'element present pour l'ecrire dans le fichier
     */
    public void createXMLElement(Document document, Element parentElement) {
        // audiobook element
        Element audioBook = document.createElement("audiobook");

        super.createXMLElement(document, audioBook);

        Element languageElement = document.createElement("language");
        languageElement.appendChild(document.createTextNode(language.getLanguage()));
        audioBook.appendChild(languageElement);

        Element categoryElement = document.createElement("category");
        categoryElement.appendChild(document.createTextNode(category.getCategory()));
        audioBook.appendChild(categoryElement);

        parentElement.appendChild(audioBook);
    }
}
