package Client;

import AudioPlayer.AudioPlayer;
import View.MainView;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class SimpleClient {
	
	private ObjectOutputStream output;
	private ObjectInputStream input;
	private Socket socket;
	private MainView mainView;

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
			mainView.printAvailableCommands();
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

	public void inputCase(String userChoice, ObjectOutputStream output,ObjectInputStream input) throws IOException, ClassNotFoundException {
		Scanner scan = new Scanner(System.in);
		String albumTitle;
		String delim = "-";
		//System.out.println("text sent to the server: " + userChoice);
		output.writeObject(userChoice);		//serialize and write the String to the stream
		if (userChoice.length() == 0) {
			System.exit(0);
		}
		while (userChoice.charAt(0)!= 'q') {
			switch (userChoice.charAt(0)) {
				case 'h':
					MainView.printAvailableCommands();
					System.out.println("Your choice is :");
					break;
				case 't':
					//album titles, ordered by date
					String list = (String) input.readObject();    //deserialize and read the Student object from the stream
					System.out.println(list);
					MainView.printAvailableCommands();
					System.out.println("Your choice is :");
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
					System.out.println("Your choice is :");
					break;
				case 'd':
					//songs of an album
					System.out.println("Songs of an album will be displayed; \nenter the album name, available albums are:\n");
					String listSongAlbum = (String) input.readObject();
					System.out.println(listSongAlbum);
					albumTitle = scan.nextLine();
					output.writeObject(albumTitle);
					String albumSonglist = (String) input.readObject();
					System.out.println(albumSonglist.replace("-", "\n"));
					MainView.printAvailableCommands();
					System.out.println("Your choice is :");
					break;
				case 'u':
					//audiobooks ordered by author
					String audioBookList = (String) input.readObject();
					System.out.println(audioBookList);
					MainView.printAvailableCommands();
					System.out.println("Your choice is :");
					break;
				case 'c':
					// add a new song
					List<String> newSongList = new ArrayList<String>();
					System.out.println("Enter a new song: ");
					System.out.println("Song title: ");
					String title = scan.nextLine();
					newSongList.add(title);
					System.out.println("Song genre (jazz, classic, hiphop, rock, pop, rap):");
					String genre = scan.nextLine();
					newSongList.add(genre);
					System.out.println("Song artist: ");
					String artist = scan.nextLine();
					newSongList.add(artist);
					System.out.println("Song length in seconds: ");
					String length = scan.nextLine();
					newSongList.add(length);
					System.out.println("Song content: ");
					String content = scan.nextLine();
					newSongList.add(content);
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
					System.out.println(listSongElem.replace("-", "\n"));
					MainView.printAvailableCommands();
					break;
				case 'a':
					// add a new album
					List<String> newAlbumList = new ArrayList<String>();
					System.out.println("Enter a new album: ");
					System.out.println("Album title: ");
					String aTitle = scan.nextLine();
					newAlbumList.add(aTitle);
					System.out.println("Album artist: ");
					String aArtist = scan.nextLine();
					newAlbumList.add(aArtist);
					System.out.println ("Album length in seconds: ");
					String aLength = scan.nextLine();
					newAlbumList.add(aLength);
					System.out.println("Album date as YYYY-DD-MM: ");
					String aDate = scan.nextLine();
					newAlbumList.add(aDate);
					String newAlbum = newAlbumList.stream()
							.map(Object::toString)
							.collect(Collectors.joining(delim));
					System.out.println("Nouvelle album list :"+newAlbum);
					output.writeObject(newAlbum);
					String responseServerAlbumCreated = (String) input.readObject();
					System.out.println(responseServerAlbumCreated);
					System.out.println("New list of albums: ");
					String listAlbElem = (String) input.readObject();
					System.out.println(listAlbElem.replace("-", "\n"));
					MainView.printAvailableCommands();
					break;
				case '+':
					//add a song to an album:
					System.out.println("Add an existing song to an existing album");
					System.out.println("Type the name of the song you wish to add. Available songs: \n");
					String listElem1 = (String) input.readObject();
					System.out.println(listElem1.replace("-", "\n"));
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
					List<String> newAudioBookList = new ArrayList<String>();
					System.out.println("Enter a new audiobook: ");
					System.out.println("AudioBook title: ");
					String bTitle = scan.nextLine();
					newAudioBookList.add(bTitle);
					System.out.println("AudioBook category (youth, novel, theater, documentary, speech)");
					String bCategory = scan.nextLine();
					newAudioBookList.add(bCategory);
					System.out.println("AudioBook artist: ");
					String bArtist = scan.nextLine();
					newAudioBookList.add(bArtist);
					System.out.println ("AudioBook length in seconds: ");
					String bLength = scan.nextLine();
					newAudioBookList.add(bLength);
					System.out.println("AudioBook content: ");
					String bContent = scan.nextLine();
					newAudioBookList.add(bContent);
					System.out.println("AudioBook language (french, english, italian, spanish, german)");
					String bLanguage = scan.nextLine();
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
					System.out.println(listPlay.replace("-", "\n"));
					System.out.println("Type the name of the playlist you wish to create:");
					String playListTitle = scan.nextLine();
					output.writeObject(playListTitle);
					stri = (String) input.readObject() ;
					System.out.println(stri);
					String listElem2 = (String) input.readObject();
					System.out.println(listElem2.replace("-", "\n"));
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
					System.out.println(listPlayElement.replace("-", "\n"));
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
					System.out.println(listPlayAvai.replace("-", "\n"));
					System.out.println("Type the name of the playlist you wish to create:");
					playListTitle = scan.nextLine();
					output.writeObject(playListTitle);
					stri = (String) input.readObject() ;
					System.out.println(stri);
					String listElem3 = (String) input.readObject();
					System.out.println(listElem3.replace("-", "\n"));
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
					String listy = (String) input.readObject();
					System.out.println(listy.replace("-", "\n"));
					userChoice = scan.nextLine();
					output.writeObject(userChoice);

					String audioFilePath = (String) input.readObject();
					AudioPlayer player = new AudioPlayer();
					//player.play(audioFilePath);
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