package Model;

/**
 * Enuramation de categorie comprenant : : Jeunesse, Roman, Théâtre, Discours et Documentaire
 */
public enum Category {
    YOUTH ("youth"), NOVEL ("novel"), THEATER ("theater"), DOCUMENTARY ("documentary"), SPEECH("speech");
    private final String category;

    /**
     * constructeur de Category
     * @param category la categorie
     */
     Category (String category) {
        this.category = category;
    }

    /**
     * getter de Category
     * @return retourne l'attribut category
     */
    public String getCategory() {
        return category;
    }
}