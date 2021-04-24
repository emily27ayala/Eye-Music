package Controller;

import Model.*;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * This thread is responsible to handle client connection.
 */
public class ServerThread extends Thread {
    private Socket socket;
	private ObjectInputStream input;
	private ObjectOutputStream output;
	private String commande;

    public ServerThread(Socket socket) {
        this.socket = socket;
    }
 
    public void run() {

        try {
			//create the streams that will handle the objects coming through the sockets
			input = new ObjectInputStream(socket.getInputStream());
			output = new ObjectOutputStream(socket.getOutputStream());
 
			commande = (String)input.readObject();  //read the object received through the stream and deserialize it
			System.out.println("server received a command: " + commande);


			commandeCase(commande,output,input);



        } catch (IOException ex) {
            System.out.println("Server exception: " + ex.getMessage());
            ex.printStackTrace();

		} catch (ClassNotFoundException ex) {
            System.out.println("Server exception: " + ex.getMessage());
            ex.printStackTrace();
        } finally {
			try {
				//output.close();
				input.close();
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
		}

    }

    public void commandeCase(String commande,ObjectOutputStream output,ObjectInputStream input) throws IOException, ClassNotFoundException {
		MusicHub theHub = new MusicHub ();
		String albumTitle;

		while (commande.charAt(0)!= 'q') 	{
			switch (commande.charAt(0)) 	{
				case 't':

					//album titles, ordered by date
					output.writeObject(theHub.getAlbumsTitlesSortedByDate());		//serialize and write the Student object to the stream

					break;
				case 'g':

					//songs of an album, sorted by genre

					output.writeObject(theHub.getAlbumsTitlesSortedByDate());
					System.out.println("list of Album title send.");
					albumTitle = (String)input.readObject();
					System.out.println("New Album created!\n");

					try {
						output.writeObject(theHub.getAlbumSongsSortedByGenre(albumTitle));
					} catch (NoAlbumFoundException ex) {
						output.writeObject("No album found with the requested title " + ex.getMessage());
					}

					break;
				case 'd':
					//songs of an album
					System.out.println("Songs of an album will be displayed; enter the album name, available albums are:");
					output.writeObject(theHub.getAlbumsTitlesSortedByDate());
					albumTitle = (String)input.readObject();

					try {
						output.writeObject(theHub.getAlbumSongs(albumTitle));
					} catch (NoAlbumFoundException ex) {
						output.writeObject("No album found with the requested title " + ex.getMessage());
					}

					break;
				case 'u':
					//audiobooks ordered by author
					output.writeObject(theHub.getAudiobooksTitlesSortedByAuthor());

					break;
				case 'c':
					// add a new song

					Song s = (Song)input.readObject();
					theHub.addElement(s);
					System.out.println("New element list: ");
					Iterator<AudioElement> it = theHub.elements();
					while (it.hasNext()) System.out.println(it.next().getTitle());
					output.writeObject("Song created!");
					output.writeObject(theHub.getElements());

					break;
				case 'a':
					// add a new album

					Album a = (Album)input.readObject();
					theHub.addAlbum(a);
					System.out.println("New list of albums: ");
					Iterator<Album> ita = theHub.albums();
					while (ita.hasNext()) System.out.println(ita.next().getTitle());
					output.writeObject("Album created!");
					output.writeObject(theHub.getAlbums());

					break;
				case '+':
					//add a song to an album:
					Iterator<AudioElement> itae = theHub.elements();
					while (itae.hasNext()) {
						AudioElement ae = itae.next();
						if ( ae instanceof Song) System.out.println(ae.getTitle());
					}
					output.writeObject(theHub.getElements());
					String songTitle = (String)input.readObject();

					System.out.println("Type the name of the album you wish to enrich. Available albums: ");
					Iterator<Album> ait = theHub.albums();
					while (ait.hasNext()) {
						Album al = ait.next();
						System.out.println(al.getTitle());
					}
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

					AudioBook b = (AudioBook)input.readObject();
					theHub.addElement(b);
					output.writeObject("Audiobook created! New element list: ");
					Iterator<AudioElement> itl = theHub.elements();
					while (itl.hasNext()) System.out.println(itl.next().getTitle());
					output.writeObject(theHub.getAudiobooksTitlesSortedByAuthor());
					break;
				case 'p':
					//create a new playlist from existing elements
					System.out.println("Existing playlists:");
					Iterator<PlayList> itpl = theHub.playlists();
					while (itpl.hasNext()) {
						PlayList pl = itpl.next();
						System.out.println(pl.getTitle());
					}
					output.writeObject(theHub.getPlaylists());
					System.out.println("Type the name of the playlist you wish to create:");
					String playListTitle = (String)input.readObject();
					PlayList pl = new PlayList(playListTitle);
					theHub.addPlaylist(pl);
					output.writeObject("Available elements: ");

					/*Iterator<AudioElement> itael = theHub.elements();
					while (itael.hasNext()) {
						AudioElement ae = itael.next();
						System.out.println(ae.getTitle());
					}*/
					System.out.println(theHub.getElements());
					output.writeObject(theHub.getElements());
					while (commande.charAt(0)!= 'n') 	{
						System.out.println("Type the name of the audio element you wish to add or 'n' to exit:");
						String elementTitle =  (String)input.readObject();
						try {
							theHub.addElementToPlayList(elementTitle, playListTitle);
							output.writeObject("Success");
						} catch (NoPlayListFoundException | NoElementFoundException ex) {
							output.writeObject(ex.getMessage());
						}

						System.out.println("Type y to add a new one, n to end");
						commande = (String)input.readObject();
					}
					System.out.println("Playlist created!");

					break;
				case '-':
					//delete a playlist
					System.out.println("Delete an existing playlist. Available playlists:");
					Iterator<PlayList> itp = theHub.playlists();
					while (itp.hasNext()) {
						PlayList p = itp.next();
						System.out.println(p.getTitle());
					}
					output.writeObject(theHub.getPlaylists());
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
					//create a new playlist from existing elements
					System.out.println("Existing playlists:");
					itpl = theHub.playlists();
					while (itpl.hasNext()) {
						 pl = itpl.next();
						System.out.println(pl.getTitle());
					}
					output.writeObject(theHub.getPlaylists());
					System.out.println("Type the name of the playlist you wish to create:");
					playListTitle = (String)input.readObject();
					for(int i=0;i<theHub.getPlaylists().size();i++){
						if(playListTitle==theHub.getPlaylists().get(i).getTitle()){
							pl = theHub.getPlaylists().get(i);
						}
					}

					//theHub.addPlaylist(pl);
					output.writeObject("Available elements: ");

					/*Iterator<AudioElement> itael = theHub.elements();
					while (itael.hasNext()) {
						AudioElement ae = itael.next();
						System.out.println(ae.getTitle());
					}*/
					System.out.println(theHub.getElements());
					output.writeObject(theHub.getElements());
					while (commande.charAt(0)!= 'n') 	{
						System.out.println("Type the name of the audio element you wish to add or 'n' to exit:");
						String elementTitle =  (String)input.readObject();
						try {
							theHub.addElementToPlayList(elementTitle, playListTitle);
							output.writeObject("Success");
						} catch (NoPlayListFoundException | NoElementFoundException ex) {
							output.writeObject(ex.getMessage());
						}

						System.out.println("Type y to add a new one, n to end");
						commande = (String)input.readObject();
					}
					System.out.println("New song added!");

					break;
				case 'm':

					output.writeObject(theHub.getElements());
					songTitle = (String)input.readObject();
					String audioFilePath = null;
					List<AudioElement> listElem = theHub.getElements();
					for (int i=0;i<listElem.size();i++) {
						if (listElem.get(i).getTitle().equals(songTitle)) {
							audioFilePath = "..\\EyeMusic-Server\\src\\main\\resources\\" + listElem.get(i).getContent();
						}
					}
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