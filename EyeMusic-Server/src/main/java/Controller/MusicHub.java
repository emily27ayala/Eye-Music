package Controller;

import Model.*;

import java.util.*;
import org.w3c.dom.*;

/**
 * permet le trie de la liste des albums (Date)
 */
class SortByDate implements Comparator<Album>
{
    public int compare(Album a1, Album a2) {
        return a1.getDate().compareTo(a2.getDate());
    }
}

/**
 * permet le trie de la liste des albums (Genre)
 */
class SortByGenre implements Comparator<Song>
{
    public int compare(Song s1, Song s2) {
        return s1.getGenre().compareTo(s2.getGenre());
    }
}

/**
 * permet le trie de la liste des albums (auteur)
 */
class SortByAuthor implements Comparator<AudioElement>
{
    public int compare(AudioElement e1, AudioElement e2) {
        return e1.getArtist().compareTo(e2.getArtist());
    }
}

/**
 * cette classe permet de gerer les differents fonctionnalité de l application tels que la supression, creation et modification des elements
 */
public class MusicHub {

    private final List<Album> albums = new LinkedList<>();
    private final List<PlayList> playlists;
    private final List<AudioElement> elements;

    public static final String DIR = System.getProperty("user.dir");
    public static final String ALBUMS_FILE_PATH = DIR + "\\src\\main\\resources\\albums.xml";
    public static final String PLAYLISTS_FILE_PATH = DIR + "\\src\\main\\resources\\playlists.xml";
    public static final String ELEMENTS_FILE_PATH = DIR + "\\src\\main\\resources\\elements.xml";

    private final XMLHandler xmlHandler = new XMLHandler();

    /**
     * constructeur de MusicHub
     */
    public MusicHub () {
        playlists = new LinkedList<>();
        elements = new LinkedList<>();
        this.loadElements();
        this.loadAlbums();
        this.loadPlaylists();
    }

    /**
     * permet de recuperer tous les playlists
     * @return retoune la liste des playlists
     */
    public List<PlayList> getPlaylists() {
        return playlists;
    }

    /**
     * permet de recuperer tous les elements audios
     * @return retourne la liste des elements audios
     */
    public List<AudioElement> getElements() {
        return elements;
    }

    /**
     * recupere la liste des albums
     * @return retourne une liste des albums
     */
    public List<Album> getAlbums() {
        return albums;
    }

    /**
     * recupere la liste des playlists avec leur titre
     * @return retourne la liste des titres des playlists
     */
    public List<String> getListOfPlaylistTitle(){

        List<String> ListOfPlaylistTitle = new LinkedList<>();

        for (PlayList playlist : playlists) {
            ListOfPlaylistTitle.add(playlist.getTitle());
        }

        return ListOfPlaylistTitle;
    }

    /**
     * recupere la liste des albums avec leur titre
     * @return retourne la liste des titres des albums
     */
    public List<String> getListOfAlbumTitle(){

        List<String> ListOfAlbumTitle = new LinkedList<>();

        for (Album album : albums) {
            ListOfAlbumTitle.add(album.getTitle());
        }

        return ListOfAlbumTitle;
    }

    /**
     * recupere la liste des AudioElement avec leur titre
     * @return retourne la liste des titres des AudioElement
     */
    public List<String> getListOfAudioElementTitle(){

        List<String> ListOfAudioElementTitle = new LinkedList<>();

        for (AudioElement audioElement : elements) {
            ListOfAudioElementTitle.add(audioElement.getTitle());
        }

        return ListOfAudioElementTitle;
    }

    /**
     * permet d ajouter un element audio à la liste des elements
     * @param element element audio
     */
    public void addElement(AudioElement element) {
        elements.add(element);
    }

    /**
     * permet d ajouter un album à la liste des albums
     * @param album à besoin d'un objet album
     */
    public void addAlbum(Album album) {
        albums.add(album);
    }

    /**
     * permet d ajouter une playlist à la liste des playlists
     * @param playlist à besoin d'un objet playlist
     */
    public void addPlaylist(PlayList playlist) {
        playlists.add(playlist);
    }

    /**
     * permet de supprimer une playlist
     * @param playListTitle le titre de la playlist à supprimer
     * @throws NoPlayListFoundException exception lorsqu'on ne trouve pas la playlist
     */
    public void deletePlayList(String playListTitle) throws NoPlayListFoundException {

        PlayList thePlayList = null;
        boolean result = false;
        for (PlayList pl : playlists) {
            if (pl.getTitle().equalsIgnoreCase(playListTitle)) {
                thePlayList = pl;
                break;
            }
        }

        if (thePlayList != null)
            result = playlists.remove(thePlayList);
        if (!result) throw new NoPlayListFoundException("Playlist " + playListTitle + " not found!");
    }

    /**
     * permet de faire une loop sur la liste des albums
     * @return une liste Iterator
     */
    public Iterator<Album> albums() {
        return albums.listIterator();
    }

    /**
     * permet de faire une loop sur la liste des playlists
     * @return une liste Iterator
     */
    public Iterator<PlayList> playlists() {
        return playlists.listIterator();
    }

    /**
     * permet de faire une loop sur la liste albums
     * @return une liste Iterator
     */
    public Iterator<AudioElement> elements() {
        return elements.listIterator();
    }

    /**
     * permet de repcuperer la liste des chansons d'un album triée par date
     * @return retourne une Liste d'élement audio triée par date
     */
    public String getAlbumsTitlesSortedByDate() {
        StringBuilder titleList = new StringBuilder();
        albums.sort(new SortByDate());
        for (Album al : albums)
            titleList.append(al.getTitle()).append("\n");
        return titleList.toString();
    }

    /**
     * permet de repcuperer la liste des chansons d'un album triée par auteur
     * @return retourne une Liste d'élement audio triée par auteur
     */
    public String getAudiobooksTitlesSortedByAuthor() {
        StringBuilder titleList = new StringBuilder();
        List<AudioElement> audioBookList = new ArrayList<>();
        for (AudioElement ae : elements)
            if (ae instanceof AudioBook)
                audioBookList.add(ae);
        audioBookList.sort(new SortByAuthor());
        for (AudioElement ab : audioBookList)
            titleList.append(ab.getArtist()).append("\n");
        return titleList.toString();
    }

    /**
     * permet de recuperer la liste des chanson d'un album
     * @param albumTitle c'est le titre de l'album
     * @return retourne la liste des element audio de l'album
     * @throws NoAlbumFoundException exception lorsqu'on ne trouve pas l'album
     */
    public List<AudioElement> getAlbumSongs (String albumTitle) throws NoAlbumFoundException {
        Album theAlbum = null;
        ArrayList<AudioElement> songsInAlbum = new ArrayList<>();
        for (Album al : albums) {
            if (al.getTitle().equalsIgnoreCase(albumTitle)) {
                theAlbum = al;
                break;
            }
        }
        if (theAlbum == null) throw new NoAlbumFoundException("No album with this title in the MusicHub!");

        List<UUID> songIDs = theAlbum.getSongs();
        for (UUID id : songIDs)
            for (AudioElement el : elements) {
                if (el instanceof Song) {
                    if (el.getUUID().equals(id)) songsInAlbum.add(el);
                }
            }
        return songsInAlbum;

    }

    /**
     * permet de recuperer la liste des audio element d'album en string
     * @param ae une liste de audio element
     * @return list de string
     */
    public List<String> getAlbumAudioElementSortedByGenreString(List<AudioElement> ae){
        List<String> string = new LinkedList<>();
        for(AudioElement song : ae){
            string.add(song.getTitle());
        }
        return string;
    }

    /**
     * permet de recuperer la liste des chanson d'album en string
     * @param songs une liste de Song
     * @return list de string
     */
    public List<String> getAlbumSongsSortedByGenreString(List<Song> songs){
        List<String> string = new LinkedList<>();
        for(Song song : songs){
            string.add(song.getTitle());
        }
            return string;
    }

    /**
     * getter qui permet d avoir la Liste des albums triée en Genre
     * @param albumTitle c'est le titre de l'album
     * @return une liste triéé (genre) de l album
     * @throws NoAlbumFoundException exception lorsqu'on ne trouve pas l'album
     */
    public List<Song> getAlbumSongsSortedByGenre (String albumTitle) throws NoAlbumFoundException {
        Album theAlbum = null;
        ArrayList<Song> songsInAlbum = new ArrayList<>();
        for (Album al : albums) {
            if (al.getTitle().equalsIgnoreCase(albumTitle)) {
                theAlbum = al;
                break;
            }
        }
        if (theAlbum == null) throw new NoAlbumFoundException("No album with this title in the MusicHub!");

        List<UUID> songIDs = theAlbum.getSongs();
        for (UUID id : songIDs)
            for (AudioElement el : elements) {
                if (el instanceof Song) {
                    if (el.getUUID().equals(id)) songsInAlbum.add((Song)el);
                }
            }
        songsInAlbum.sort(new SortByGenre());
        return songsInAlbum;

    }

    /**
     * ajoute un nouvel element à l'album
     * @param elementTitle titre du nouvel element audio
     * @param albumTitle titre de l'album
     * @throws NoAlbumFoundException exception lorsqu'on ne trouve pas l'album
     * @throws NoElementFoundException exception lorsqu'on ne trouve pas l'élément audio
     */
    public void addElementToAlbum(String elementTitle, String albumTitle) throws NoAlbumFoundException, NoElementFoundException
    {
        Album theAlbum = null;
        int i;
        boolean found = false;
        for (i = 0; i < albums.size(); i++) {
            if (albums.get(i).getTitle().equalsIgnoreCase(albumTitle)) {
                theAlbum = albums.get(i);
                found = true;
                break;
            }
        }

        if (found) {
            AudioElement theElement = null;
            for (AudioElement ae : elements) {
                if (ae.getTitle().equalsIgnoreCase(elementTitle)) {
                    theElement = ae;
                    break;
                }
            }
            if (theElement != null) {
                theAlbum.addSong(theElement.getUUID());
                //replace the album in the list
                albums.set(i,theAlbum);
            }
            else throw new NoElementFoundException("Element " + elementTitle + " not found!");
        }
        else throw new NoAlbumFoundException("Album " + albumTitle + " not found!");

    }

    /**
     * ajoute un nouvel element à la playlist
     * @param elementTitle titre du nouvel element audio
     * @param playListTitle titre de la playlist
     * @throws NoPlayListFoundException exception lorsqu'on ne trouve pas la playlist
     * @throws NoElementFoundException exception lorsqu'on ne trouve pas l'élément audio
     */
    public void addElementToPlayList(String elementTitle, String playListTitle) throws NoPlayListFoundException, NoElementFoundException
    {
        PlayList thePlaylist = null;
        int i;
        boolean found = false;

        for (i = 0; i < playlists.size(); i++) {
            if (playlists.get(i).getTitle().equalsIgnoreCase(playListTitle)) {
                thePlaylist = playlists.get(i);
                found = true;
                break;
            }
        }

        if (found) {
            AudioElement theElement = null;
            for (AudioElement ae : elements) {
                if (ae.getTitle().equalsIgnoreCase(elementTitle)) {
                    theElement = ae;
                    break;
                }
            }
            if (theElement != null) {
                thePlaylist.addElement(theElement.getUUID());
                //replace the album in the list
                playlists.set(i,thePlaylist);
            }
            else throw new NoElementFoundException("Element " + elementTitle + " not found!");

        } else throw new NoPlayListFoundException("Playlist " + playListTitle + " not found!");

    }

    /**
     * permet de charger les albums à partir d'un fichier XML
     */
    private void loadAlbums () {
        NodeList albumNodes = xmlHandler.parseXMLFile(ALBUMS_FILE_PATH);
        if (albumNodes == null) return;

        for (int i = 0; i < albumNodes.getLength(); i++) {
            if (albumNodes.item(i).getNodeType() == Node.ELEMENT_NODE)   {
                Element albumElement = (Element) albumNodes.item(i);
                if (albumElement.getNodeName().equals("album")) 	{
                    try {
                        this.addAlbum(new Album (albumElement));
                    } catch (Exception ex) {
                        System.out.println ("Something is wrong with the XML album element");
                    }
                }
            }
        }
    }

    /**
     * permet de charger les playlists à partir d'un fichier XML
     */
    private void loadPlaylists () {
        NodeList playlistNodes = xmlHandler.parseXMLFile(PLAYLISTS_FILE_PATH);
        if (playlistNodes == null) return;

        for (int i = 0; i < playlistNodes.getLength(); i++) {
            if (playlistNodes.item(i).getNodeType() == Node.ELEMENT_NODE)   {
                Element playlistElement = (Element) playlistNodes.item(i);
                if (playlistElement.getNodeName().equals("playlist")) 	{
                    try {
                        this.addPlaylist(new PlayList (playlistElement));
                    } catch (Exception ex) {
                        System.out.println ("Something is wrong with the XML playlist element");
                    }
                }
            }
        }
    }

    /**
     * permet de recuperer les element d'un fichier XML
     */
    private void loadElements () {
        NodeList audioelementsNodes = xmlHandler.parseXMLFile(ELEMENTS_FILE_PATH);
        if (audioelementsNodes == null) return;

        for (int i = 0; i < audioelementsNodes.getLength(); i++) {
            if (audioelementsNodes.item(i).getNodeType() == Node.ELEMENT_NODE)   {
                Element audioElement = (Element) audioelementsNodes.item(i);
                if (audioElement.getNodeName().equals("song")) 	{
                    try {
                        AudioElement newSong = new Song (audioElement);
                        this.addElement(newSong);
                    } catch (Exception ex) 	{
                        System.out.println ("Something is wrong with the XML song element");
                    }
                }
                if (audioElement.getNodeName().equals("audiobook")) 	{
                    try {
                        AudioElement newAudioBook = new AudioBook (audioElement);
                        this.addElement(newAudioBook);
                    } catch (Exception ex) 	{
                        System.out.println ("Something is wrong with the XML audiobook element");
                    }
                }
            }
        }
    }

    /**
     * permet de sauvegarder la liste des albums
     */
    public void saveAlbums () {
        Document document = xmlHandler.createXMLDocument();
        if (document == null) return;

        // root element
        Element root = document.createElement("albums");
        document.appendChild(root);

        //save all albums
        for (Iterator<Album> albumsIter = this.albums(); albumsIter.hasNext();) {
            Album currentAlbum = albumsIter.next();
            currentAlbum.createXMLElement(document, root);
        }
        xmlHandler.createXMLFile(document, ALBUMS_FILE_PATH);
    }

    /**
     * permet de sauvegarder la liste des playlists
     */
    public void savePlayLists () {
        Document document = xmlHandler.createXMLDocument();
        if (document == null) return;

        // root element
        Element root = document.createElement("playlists");
        document.appendChild(root);

        //save all playlists
        for (Iterator<PlayList> playlistsIter = this.playlists(); playlistsIter.hasNext();) {
            PlayList currentPlayList = playlistsIter.next();
            currentPlayList.createXMLElement(document, root);
        }
        xmlHandler.createXMLFile(document, PLAYLISTS_FILE_PATH);
    }

    /**
     * permet de sauvegarder les elements audio dans un fichier XML
     */
    public void saveElements() {
        Document document = xmlHandler.createXMLDocument();
        if (document == null) return;

        // root element
        Element root = document.createElement("elements");
        document.appendChild(root);

        //save all AudioElements
        for (AudioElement currentElement : elements) {

            if (currentElement instanceof Song) {
                currentElement.createXMLElement(document, root);
            }
            if (currentElement instanceof AudioBook) {
                currentElement.createXMLElement(document, root);
            }
        }
        xmlHandler.createXMLFile(document, ELEMENTS_FILE_PATH);
    }
}