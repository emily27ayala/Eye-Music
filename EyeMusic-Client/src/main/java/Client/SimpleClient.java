package Client;

import MusicPlayer.AudioPathObservable;
import MusicPlayer.AudioPlayer;
import View.MainView;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

/**
 * C'est dans cette classe que l'on trouve les differentes choix de l'utilisateur qui sera communiqué avec le serveur
 */
public class SimpleClient {
	
	private ObjectOutputStream output;
	private ObjectInputStream input;
	private Socket socket;
	private MainView mainView;
	private AudioPathObservable audioPathObservable;
	private AudioPlayer audioPlayer;

	/**
	 * cette fonction permet d'établir une connection avec le serveur
	 * @param ip "address" de base
	 */
	public void connect(String ip)
	{
		Scanner scan = new Scanner(System.in);
		int port = 6666;
        try  {
			//create the socket; it is defined by an remote IP address (the address of the server) and a port number
			socket = new Socket(ip, port);
			String userChoice;
			//create the streams that will handle the objects coming and going through the sockets
			output = new ObjectOutputStream(socket.getOutputStream());
            input = new ObjectInputStream(socket.getInputStream());
			MainView.printAvailableCommands();
			userChoice = scan.nextLine();
			inputCase(userChoice,output,input);
        } catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		finally {
			try {
				input.close();
				output.close();
				socket.close();
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
		}
	}

	/**
	 *ici on se trouve dans la fonction principale du partie client, on se trouve dans une fonction essentiellement compose
	 * d'un switch qui permet de nous orienter vers les differentes commandes
	 * @param userChoice cette variable correspond aux commandes entrées par l'utilisateur
	 * @param output  est la varible qui permet de connaitre le port de sortie
	 * @param input est la varible qui permet de connaitre le port d'entrée
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public void inputCase(String userChoice, ObjectOutputStream output,ObjectInputStream input) throws IOException, ClassNotFoundException {
		Scanner scan = new Scanner(System.in);
		String albumTitle;
		String delim = "~";
		//System.out.println("text sent to the server: " + userChoice);
		output.writeObject(userChoice);		//serialize and write the String to the stream
		if (userChoice.length() == 0) {
			System.exit(0);
		}
		while (userChoice.charAt(0)!= 'q') {
			switch (userChoice.charAt(0)) {
				case 'h':
					MainView.printAvailableCommands();
					break;
				case 't':
					//album titles, ordered by date
					String list = (String) input.readObject();    //deserialize and read the Student object from the stream
					System.out.println(list);
					mainView.printAvailableCommands();

					break;
				case 'g':
					//songs of an album, sorted by genre
					System.out.println("Songs of an album sorted by genre will be displayed; \nEnter the album name, available albums are:\n");
					String listAlbum = (String) input.readObject();
					System.out.println(listAlbum);
					albumTitle = scan.nextLine();
					output.writeObject(albumTitle);
					String albumlist = (String) input.readObject();
					System.out.println(albumlist.replace("-", "\n"));
					MainView.printAvailableCommands();

					break;
				case 'd':
					//songs of an album
					System.out.println("Songs of an album will be displayed; \nenter the album name, available albums are:\n");
					String listSongAlbum = (String) input.readObject();
					System.out.println(listSongAlbum);
					albumTitle = scan.nextLine();
					output.writeObject(albumTitle);
					String albumSonglist = (String) input.readObject();
					System.out.println(albumSonglist.replace("~", "\n"));
					MainView.printAvailableCommands();

					break;
				case 'u':
					//audiobooks ordered by author
					String audioBookList = (String) input.readObject();
					System.out.println(audioBookList);
					MainView.printAvailableCommands();
					break;
				case 'c':
					// add a new song
					List<String> newSongList = MainView.createANewSong();

					String newSong = newSongList.stream()
							.map(Object::toString)
							.collect(Collectors.joining(delim));
					System.out.println("Nouvelle chanson list :"+newSong);
					//envoie les informations de la nouvelle chanson au serveur
					output.writeObject(newSong);
					String responseServerSongCreated = (String)input.readObject();
					System.out.println(responseServerSongCreated);
					System.out.println("New element list: ");
					String listSongElem = (String) input.readObject();
					System.out.println(listSongElem.replace("~", "\n"));
					MainView.printAvailableCommands();
					break;
				case 'a':
					// add a new album
					List<String> newAlbumList = MainView.createANewAlbum();
					String newAlbum = newAlbumList.stream()
							.map(Object::toString)
							.collect(Collectors.joining(delim));
					System.out.println("Nouvelle album list :"+newAlbum);
					output.writeObject(newAlbum);
					String responseServerAlbumCreated = (String) input.readObject();
					System.out.println(responseServerAlbumCreated);
					System.out.println("New list of albums: ");
					String listAlbElem = (String) input.readObject();
					System.out.println(listAlbElem.replace("~", "\n"));
					MainView.printAvailableCommands();
					break;
				case '+':
					//add a song to an album:
					System.out.println("Add an existing song to an existing album");
					System.out.println("Type the name of the song you wish to add. Available songs: \n");
					String listElem1 = (String) input.readObject();
					System.out.println(listElem1.replace("~", "\n"));
					String songTitle = scan.nextLine();
					output.writeObject(songTitle);
					System.out.println("Type the name of the album you wish to enrich. Available albums: ");
					String stri =(String) input.readObject();
					System.out.println(stri);
					String titleAlbum = scan.nextLine();
					output.writeObject(titleAlbum);
					stri= (String) input.readObject();
					System.out.println(stri);
					MainView.printAvailableCommands();
					break;
				case 'l':
					// add a new audiobook
					List<String> newAudioBookList = MainView.createANewAudioBook();
					String newAudioBook = newAudioBookList.stream()
							.map(Object::toString)
							.collect(Collectors.joining(delim));
					output.writeObject(newAudioBook);
					stri = (String)input.readObject();
					System.out.println(stri);
					stri= (String)input.readObject();
					System.out.println(stri);
					MainView.printAvailableCommands();
					break;
				case 'p':
					//create a new playlist from existing elements
					System.out.println("Add an existing song or audiobook to a new playlist");
					System.out.println("Existing playlists:");
					String listPlay= (String) input.readObject();
					System.out.println(listPlay.replace("~", "\n"));
					System.out.println("Type the name of the playlist you wish to create:");
					String playListTitle = scan.nextLine();
					output.writeObject(playListTitle);
					stri = (String) input.readObject() ;
					System.out.println(stri);
					String listElem2 = (String) input.readObject();
					System.out.println(listElem2.replace("~", "\n"));
					while (userChoice.charAt(0)!= 'n') 	{
						System.out.println("Type the name of the audio element you wish to add or 'n' to exit:");
						String elementTitle = scan.nextLine();
						output.writeObject(elementTitle);
						stri = (String) input.readObject() ;
						System.out.println(stri);
						System.out.println("Type y to add a new one, n to end");
						userChoice = scan.nextLine();
						output.writeObject(userChoice);
					}
					System.out.println("Playlist created!");
					MainView.printAvailableCommands();
					break;
				case '-':
					//delete a playlist
					System.out.println("Delete an existing playlist. Available playlists:");
					String listPlayElement = (String) input.readObject();
					System.out.println(listPlayElement.replace("~", "\n"));
					String plTitle = scan.nextLine();
					output.writeObject(plTitle);
					stri = (String) input.readObject() ;
					System.out.println(stri);
					System.out.println("Playlist deleted!");
					MainView.printAvailableCommands();
					break;
				case 'y':
					//create a new playlist from existing elements
					System.out.println("Add an existing song or audiobook to an existing playlist");
					System.out.println("Playlists:");
					String listPlayAvai = (String) input.readObject();
					System.out.println(listPlayAvai.replace("~", "\n"));
					System.out.println("Type the name of the playlist you wish to modify:");
					playListTitle = scan.nextLine();
					output.writeObject(playListTitle);
					String listElem3 = (String) input.readObject();
					System.out.println(listElem3.replace("~", "\n"));
					while (userChoice.charAt(0)!= 'n') 	{
						System.out.println("Type the name of the audio element you wish to add or 'n' to exit:");
						String elementTitle = scan.nextLine();
						output.writeObject(elementTitle);
						stri = (String) input.readObject() ;
						System.out.println(stri);
						System.out.println("Type y to add a new one, n to end");
						userChoice = scan.nextLine();
						output.writeObject(userChoice);

					}
					System.out.println("Playlist created!");
					MainView.printAvailableCommands();
					break;
				case 'm':
					System.out.println("Select a Song to play:");
					audioPathObservable = new AudioPathObservable("audioPath");
					audioPlayer = new AudioPlayer();
					audioPathObservable.addObserver(audioPlayer);
					String listy = (String) input.readObject();
					System.out.println(listy.replace("~", "\n"));
					userChoice = scan.nextLine();
					output.writeObject(userChoice);
					String audioFilePath = (String) input.readObject();
					audioPathObservable.setAudioPathMusicFile(audioFilePath);
					MainView.printAvailableCommands();
					break;
				case 's':
					//save elements, albums, playlists
					String str = (String)input.readObject();
					MainView.printAvailableCommands();
					break;
				default:
					break;
			}
			userChoice = scan.nextLine();
			output.writeObject(userChoice);
		}
		scan.close();
	}


}