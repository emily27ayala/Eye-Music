package Model;

/**
 * Enuramation de Langue comprenant : l'anglais, français, italien, espagnol et allemend
 */
public enum Language {
    FRENCH ("french"), ENGLISH ("english"), ITALIAN ("italian"), SPANISH ("spanish"), GERMAN("german");
    private final String language;

    /**
     * constructeur de Language
     * @param language la langue
     */
    Language(String language) {
        this.language = language;
    }

    /**
     * getter de Language
     * @return retourne l'attribut language
     */
    public String getLanguage() {
        return language;
    }
}