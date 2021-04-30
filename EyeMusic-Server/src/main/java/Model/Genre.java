package Model;

/**
 * Enuramation de Genre comprenant : le jazz, classique, l'hip-hop, le rock, le pop et le rap
 */
public enum Genre {
    JAZZ ("jazz"), CLASSIC ("classic"), HIPHOP ("hiphop"), ROCK ("rock"), POP("pop"), RAP("rap");
    private final String genre;

    /**
     * constructeur de Genre
     * @param genre le genre
     */
     Genre (String genre) {
        this.genre = genre;
    }

    /**
     * getter de Genre
     * @return retourne l'attribut genre
     */
    public String getGenre() {
        return genre;
    }
}