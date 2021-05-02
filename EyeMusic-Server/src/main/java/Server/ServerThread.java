package Server;

import Controller.MusicHub;
import Controller.NoAlbumFoundException;
import Controller.NoElementFoundException;
import Controller.NoPlayListFoundException;
import Model.*;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This thread is responsible to handle client connection.
 */

public class ServerThread extends Thread {
    private Socket socket;
	private ObjectInputStream input;
	private ObjectOutputStream output;
	private String commande;

	/**
	 * Constructeur de ServerThread
	 * @param socket le port sur lequel le server va communique avec le client
	 */
    public ServerThread(Socket socket) {
        this.socket = socket;
    }

	/**
	 * fonction qui lance le serverThread
	 */
    public void run() {

        try {
			//create the streams that will handle the objects coming through the sockets
			input = new ObjectInputStream(socket.getInputStream());
			output = new ObjectOutputStream(socket.getOutputStream());
 
			commande = (String)input.readObject();  //read the object received through the stream and deserialize it


			commandeCase(commande,output,input);


        } catch (IOException ex) {
            System.out.println("Server exception: " + ex.getMessage());
            ex.printStackTrace();

		} catch (ClassNotFoundException ex) {
            System.out.println("Server exception: " + ex.getMessage());
            ex.printStackTrace();
        } finally {
			try {
				output.close();
				input.close();
				System.out.println("Server Closing");
				System.exit(0);
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
		}

    }

	/**
	 * fonction qui prend en compte les commandes de l'utilisateur
	 * @param commande  la commande tapez par l'utilisateur
	 * @param output port de sortie
	 * @param input port d'entrée
	 * @throws IOException
	 * @throws ClassNotFoundException exception lorsque l'element n'est pas trouvé
	 */
    public void commandeCase(String commande,ObjectOutputStream output,ObjectInputStream input) throws IOException, ClassNotFoundException {
		MusicHub theHub = new MusicHub ();
		String albumTitle;
		String delim = "~";

		while (commande.charAt(0)!= 'q') 	{
			switch (commande.charAt(0)) 	{
				case 't':
					//album titles, ordered by date
					output.writeObject(theHub.getAlbumsTitlesSortedByDate());		//serialize and write the Student object to the stream
					System.out.println("album list ordered by date send");

					break;
				case 'g':
					//songs of an album, sorted by genre
					output.writeObject(theHub.getAlbumsTitlesSortedByDate());
					System.out.println("list of Album title send.");
					albumTitle = (String)input.readObject();
					System.out.println("Title album received!\n");
					try {
						String songList = theHub.getAlbumSongsSortedByGenreString(theHub.getAlbumSongsSortedByGenre(albumTitle)).stream()
								.map(Object::toString)
								.collect(Collectors.joining(delim));
						output.writeObject(songList);
						System.out.println("Songs of an album send sorted by genre;");
					} catch (NoAlbumFoundException ex) {
						output.writeObject("No album found with the requested title " + ex.getMessage());
					}
					break;
				case 'd':
					//songs of an album

					output.writeObject(theHub.getAlbumsTitlesSortedByDate());
					albumTitle = (String)input.readObject();
					try {
						String songList2 = theHub.getAlbumAudioElementSortedByGenreString(theHub.getAlbumSongs(albumTitle)).stream()
								.map(Object::toString)
								.collect(Collectors.joining(delim));
						output.writeObject(songList2);
						System.out.println("Songs of an album send");
					} catch (NoAlbumFoundException ex) {
						output.writeObject("No album found with the requested title " + ex.getMessage());
					}

					break;
				case 'u':
					//audiobooks ordered by author
					output.writeObject(theHub.getAudiobooksTitlesSortedByAuthor());
					System.out.println("audio Book list send");
					break;
				case 'c':
					// add a new song
					String songReceived = (String)input.readObject();
					List<String> song = new ArrayList<String>(Arrays.asList(songReceived.split(delim)));
					Song songElement = new Song(song.get(0), song.get(2), Integer.parseInt(song.get(3)), song.get(4), song.get(1));
					theHub.addElement(songElement);

					output.writeObject("Song created!");
					String listSong = theHub.getListOfAudioElementTitle().stream()
							.map(Object::toString)
							.collect(Collectors.joining(delim));
					output.writeObject(listSong);
					System.out.println("new song created");
					break;
				case 'a':
					// add a new album
					String albumReceived = (String)input.readObject();
					List<String> album = new ArrayList<String>(Arrays.asList(albumReceived.split(delim)));
					Album albumElement = new Album(album.get(0), album.get(1), Integer.parseInt(album.get(2)), album.get(3) );
					theHub.addAlbum(albumElement);

					output.writeObject("Album created!");
					String listAlbum = theHub.getListOfAlbumTitle().stream()
							.map(Object::toString)
							.collect(Collectors.joining(delim));
					output.writeObject(listAlbum);
					System.out.println("new album created");
					break;
				case '+':
					//add a song to an album:

					String listElem = theHub.getListOfAudioElementTitle().stream()
							.map(Object::toString)
							.collect(Collectors.joining(delim));
					output.writeObject(listElem);
					String songTitle = (String)input.readObject();

					output.writeObject(theHub.getAlbumsTitlesSortedByDate());
					String titleAlbum = (String)input.readObject();
					try {
						theHub.addElementToAlbum(songTitle, titleAlbum);
						output.writeObject("Song added to the album!");
					} catch (NoAlbumFoundException | NoElementFoundException ex){
						output.writeObject(ex.getMessage());
					}
					break;
				case 'l':
					// add a new audiobook
					String audioBookReceived = (String)input.readObject();
					List<String> newAudioBook = new ArrayList<String>(Arrays.asList(audioBookReceived.split(delim)));
					AudioBook audioBookElement = new AudioBook(newAudioBook.get(0), newAudioBook.get(2), Integer.parseInt(newAudioBook.get(3)), newAudioBook.get(4), newAudioBook.get(5), newAudioBook.get(1));
					theHub.addElement(audioBookElement);
					output.writeObject("Audiobook created! New element list: ");
					Iterator<AudioElement> itl = theHub.elements();
					while (itl.hasNext()) System.out.println(itl.next().getTitle());
					output.writeObject(theHub.getAudiobooksTitlesSortedByAuthor());
					break;
				case 'p':
					//create a new playlist from existing elements

					String listElemPlaylist = theHub.getListOfPlaylistTitle().stream()
							.map(Object::toString)
							.collect(Collectors.joining(delim));
					output.writeObject(listElemPlaylist);
					System.out.println("Type the name of the playlist you wish to create:");
					String playListTitle = (String)input.readObject();
					PlayList pl = new PlayList(playListTitle);
					theHub.addPlaylist(pl);
					output.writeObject("Available elements: ");

					String listElemPlaylistAvailable = theHub.getAlbumAudioElementSortedByGenreString(theHub.getElements()).stream()
							.map(Object::toString)
							.collect(Collectors.joining(delim));
					output.writeObject(listElemPlaylistAvailable);
					while (commande.charAt(0)!= 'n') 	{
						String elementTitle =  (String)input.readObject();
						try {
							theHub.addElementToPlayList(elementTitle, playListTitle);
							output.writeObject("Song successly added");
						} catch (NoPlayListFoundException | NoElementFoundException ex) {
							output.writeObject(ex.getMessage());
						}

						commande = (String)input.readObject();
					}
					System.out.println("Playlist created!");
					break;
				case '-':
					//delete a playlist
					System.out.println("Deleting an existing playlist.");
					String listElemPlaylistAvail = theHub.getListOfPlaylistTitle().stream()
							.map(Object::toString)
							.collect(Collectors.joining(delim));
					output.writeObject(listElemPlaylistAvail);
					String plTitle = (String)input.readObject();
					try {
						theHub.deletePlayList(plTitle);
						output.writeObject("Success");
					}	catch (NoPlayListFoundException ex) {
						output.writeObject(ex.getMessage());
					}
					System.out.println("Playlist deleted!");
					break;
				case 'y':
					//add a new song to an existing playlist

					String listElemPlaylistAv = theHub.getListOfPlaylistTitle().stream()
							.map(Object::toString)
							.collect(Collectors.joining(delim));
					output.writeObject(listElemPlaylistAv);

					playListTitle = (String)input.readObject();
					for(int i=0;i<theHub.getPlaylists().size();i++){
						if(playListTitle.equals(theHub.getPlaylists().get(i).getTitle())){
							pl = theHub.getPlaylists().get(i);
						}
					}

					String listElemAv = theHub.getAlbumAudioElementSortedByGenreString(theHub.getElements()).stream()
							.map(Object::toString)
							.collect(Collectors.joining(delim));
					System.out.println(listElemAv);
					output.writeObject(listElemAv);
					while (commande.charAt(0)!= 'n') 	{

						String elementTitle =  (String)input.readObject();
						try {
							theHub.addElementToPlayList(elementTitle, playListTitle);
							output.writeObject("Success");
						} catch (NoPlayListFoundException | NoElementFoundException ex) {
							output.writeObject(ex.getMessage());
						}

						commande = (String)input.readObject();
					}
					System.out.println("New song added!");

					break;
				case 'm':
					String listElemA = theHub.getAlbumAudioElementSortedByGenreString(theHub.getElements()).stream()
							.map(Object::toString)
							.collect(Collectors.joining(delim));
					output.writeObject(listElemA);
					songTitle = (String)input.readObject();
					String audioFilePath = null;
					List<AudioElement> listElemRelou = theHub.getElements();
					for (AudioElement audioElement : listElemRelou) {
						if (audioElement.getTitle().equals(songTitle)) {
							audioFilePath = "..\\EyeMusic-Server\\src\\main\\resources\\" + audioElement.getContent();
						}
					}
					System.out.println(audioFilePath);
					output.writeObject(audioFilePath);
					System.out.println("song playing");
					break;
				case 's':
					//save elements, albums, playlists
					theHub.saveElements();
					theHub.saveAlbums();
					theHub.savePlayLists();
					output.writeObject("Elements, albums and playlists saved!");
					break;
				default:

					break;
			}
			commande = (String)input.readObject();
		}
	}

}