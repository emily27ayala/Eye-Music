package Controller;

import Model.*;
import View.MainView;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

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

		System.out.println("text sent to the server: " + userChoice);
		output.writeObject(userChoice);		//serialize and write the String to the stream


		if (userChoice.length() == 0) System.exit(0);

		while (userChoice.charAt(0)!= 'q') {


			switch (userChoice.charAt(0)) {
				case 'h':
					mainView.printAvailableCommands();
					System.out.println("Your choice is :");
					break;
				case 't':

					//album titles, ordered by date
					String list = (String) input.readObject();    //deserialize and read the Student object from the stream
					System.out.println(list);
					mainView.printAvailableCommands();
					System.out.println("Your choice is :");
					break;

				case 'g':
					//songs of an album, sorted by genre
					System.out.println("Songs of an album sorted by genre will be displayed; enter the album name, available albums are:");
					String listAlbum = (String) input.readObject();
					System.out.println(listAlbum);

					albumTitle = scan.nextLine();
					output.writeObject(albumTitle);
					ArrayList<Song> albumlist = (ArrayList<Song>) input.readObject();
					System.out.println(albumlist);

					mainView.printAvailableCommands();
					System.out.println("Your choice is :");
					break;
				case 'd':
					//songs of an album
					System.out.println("Songs of an album will be displayed; enter the album name, available albums are:");
					String listSongAlbum = (String) input.readObject();
					System.out.println(listSongAlbum);

					albumTitle = scan.nextLine();
					output.writeObject(albumTitle);
					ArrayList<AudioElement> albumSonglist = (ArrayList<AudioElement>) input.readObject();
					System.out.println(albumSonglist);

					mainView.printAvailableCommands();
					System.out.println("Your choice is :");
					break;
				case 'u':
					//audiobooks ordered by author
					String audioBookList = (String) input.readObject();
					System.out.println(audioBookList);
					mainView.printAvailableCommands();
					System.out.println("Your choice is :");
					break;
				case 'c':
					// add a new song
					System.out.println("Enter a new song: ");
					System.out.println("Song title: ");
					String title = scan.nextLine();
					System.out.println("Song genre (jazz, classic, hiphop, rock, pop, rap):");
					String genre = scan.nextLine();
					System.out.println("Song artist: ");
					String artist = scan.nextLine();
					System.out.println("Song length in seconds: ");
					int length = Integer.parseInt(scan.nextLine());
					System.out.println("Song content: ");
					String content = scan.nextLine();
					Song s = new Song(title, artist, length, content, genre);
					output.writeObject(s);

					String stri = (String)input.readObject();
					System.out.println(stri);
					System.out.println("New element list: ");
					List<AudioElement> listElem = (List<AudioElement>) input.readObject();
					for (int i=0;i<listElem.size();i++){
					System.out.println(listElem.get(i).getTitle());
					}
					mainView.printAvailableCommands();
					break;
				case 'a':
					// add a new album
					System.out.println("Enter a new album: ");
					System.out.println("Album title: ");
					String aTitle = scan.nextLine();
					System.out.println("Album artist: ");
					String aArtist = scan.nextLine();
					System.out.println ("Album length in seconds: ");
					int aLength = Integer.parseInt(scan.nextLine());
					System.out.println("Album date as YYYY-DD-MM: ");
					String aDate = scan.nextLine();
					Album a = new Album(aTitle, aArtist, aLength, aDate);
					output.writeObject(a);
					stri= (String) input.readObject();
					System.out.println(stri);
					System.out.println("New list of albums: ");
					List<Album> listAl = (List<Album>) input.readObject();
					for (int i=0;i<listAl.size();i++){
						System.out.println(listAl.get(i).getTitle());
					}
					mainView.printAvailableCommands();
					break;
				case '+':
					//add a song to an album:
					System.out.println("Add an existing song to an existing album");
					System.out.println("Type the name of the song you wish to add. Available songs: ");
					listElem = (List<AudioElement>) input.readObject();
					for (int i=0;i<listElem.size();i++){
						System.out.println(listElem.get(i).getTitle());
					}
					String songTitle = scan.nextLine();
					output.writeObject(songTitle);

					System.out.println("Type the name of the album you wish to enrich. Available albums: ");
					stri =(String) input.readObject();
					System.out.println(stri);

					String titleAlbum = scan.nextLine();
					output.writeObject(titleAlbum);
					stri= (String) input.readObject();
					System.out.println(stri);
					mainView.printAvailableCommands();
					break;
				case 'l':
					// add a new audiobook
					System.out.println("Enter a new audiobook: ");
					System.out.println("AudioBook title: ");
					String bTitle = scan.nextLine();
					System.out.println("AudioBook category (youth, novel, theater, documentary, speech)");
					String bCategory = scan.nextLine();
					System.out.println("AudioBook artist: ");
					String bArtist = scan.nextLine();
					System.out.println ("AudioBook length in seconds: ");
					int bLength = Integer.parseInt(scan.nextLine());
					System.out.println("AudioBook content: ");
					String bContent = scan.nextLine();
					System.out.println("AudioBook language (french, english, italian, spanish, german)");
					String bLanguage = scan.nextLine();
					AudioBook b = new AudioBook(bTitle, bArtist, bLength, bContent, bLanguage, bCategory);
					output.writeObject(b);
					stri = (String)input.readObject();
					System.out.println(stri);
					stri= (String)input.readObject();
					System.out.println(stri);
					mainView.printAvailableCommands();
					break;
				case 'p':
					//create a new playlist from existing elements
					System.out.println("Add an existing song or audiobook to a new playlist");
					System.out.println("Existing playlists:");
					List<PlayList> listPlay= (List<PlayList>) input.readObject();
					for (int i=0;i<listPlay.size();i++){
						System.out.println(listPlay.get(i).getTitle());
					}
					System.out.println("Type the name of the playlist you wish to create:");
					String playListTitle = scan.nextLine();
					output.writeObject(playListTitle);
					stri = (String) input.readObject() ;
					System.out.println(stri);

					listElem = (List<AudioElement>) input.readObject();


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
					mainView.printAvailableCommands();
					break;
				case '-':
					//delete a playlist
					System.out.println("Delete an existing playlist. Available playlists:");

					listPlay= (List<PlayList>) input.readObject();
					for (int i=0;i<listPlay.size();i++){
						System.out.println(listPlay.get(i).getTitle());
					}

					String plTitle = scan.nextLine();
					output.writeObject(plTitle);
					stri = (String) input.readObject() ;
					System.out.println(stri);

					System.out.println("Playlist deleted!");
					mainView.printAvailableCommands();
					break;
				case 's':
					//save elements, albums, playlists
					String str = (String)input.readObject();
					mainView.printAvailableCommands();
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