package View;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

/**
 * cette fonction regroupe les fonction qui permetten d afficher les commandes et fonction de "saisie"
 */
public class MainView {

    /**
     * permet deprendre les information chez l'utilisateur pour creer un audiobook
     * @return retourne une liste contenant les informations pour créer l'audiobook
     */
    public static List<String> createANewAudioBook(){
        Scanner sc = new Scanner(System.in);
        List<String> audioBookDetails = new ArrayList<String>();
        System.out.println("Enter a new audiobook: ");
        System.out.println("AudioBook title: ");
        String bTitle = sc.nextLine();
        audioBookDetails.add(bTitle);
        System.out.println("AudioBook category (youth, novel, theater, documentary, speech)");
        String bCategory = sc.nextLine();
        audioBookDetails.add(bCategory);
        System.out.println("AudioBook artist: ");
        String bArtist = sc.nextLine();
        audioBookDetails.add(bArtist);
        System.out.println ("AudioBook length in seconds: ");
        String bLength = sc.nextLine();
        audioBookDetails.add(bLength);
        System.out.println("AudioBook content: ");
        String bContent = sc.nextLine();
        audioBookDetails.add(bContent);
        System.out.println("AudioBook language (french, english, italian, spanish, german)");
        String bLanguage = sc.nextLine();
        audioBookDetails.add(bLanguage);
        return audioBookDetails;
    }

    /**
     * permet de prendre les informations chez l'utilisateur pour créer une chanson
     * @return retourne une liste contenant les informations pour créer la chanson
     */
    public static List<String> createANewSong(){
        Scanner sc = new Scanner(System.in);
        List<String> songDetails = new ArrayList<String>();
        System.out.println("Enter a new song: ");
        System.out.println("Song title: ");
        String title = sc.nextLine();
        songDetails.add(title);
        System.out.println("Song genre (jazz, classic, hiphop, rock, pop, rap):");
        String genre = sc.nextLine();
        songDetails.add(genre);
        System.out.println("Song artist: ");
        String artist = sc.nextLine();
        songDetails.add(artist);
        System.out.println("Song length in seconds: ");
        String length = sc.nextLine();
        songDetails.add(length);
        System.out.println("Song content: ");
        String content = sc.nextLine();
        songDetails.add(content);
        return songDetails;
    }

    /**
     * permet de prendre les informations chez l'utilisateur pour créer un album
     * @return retourne une liste contenant les informations pour créer l'album
     */
    public static List<String> createANewAlbum(){
        Scanner sc = new Scanner(System.in);
        List<String> albumDetails = new ArrayList<String>();
        System.out.println("Enter a new album: ");
        System.out.println("Album title: ");
        String aTitle = sc.nextLine();
        albumDetails.add(aTitle);
        System.out.println("Album artist: ");
        String aArtist = sc.nextLine();
        albumDetails.add(aArtist);
        System.out.println ("Album length in seconds: ");
        String aLength = sc.nextLine();
        albumDetails.add(aLength);
        System.out.println("Album date as YYYY-DD-MM: ");
        String aDate = sc.nextLine();
        albumDetails.add(aDate);
        return albumDetails;
    }


    /**
     * Cette fonction nous permet d'afficher les différentes commande de l'application
     */
    public static void printAvailableCommands() {
        System.out.println("t: display the album titles, ordered by date");
        System.out.println("g: display songs of an album, ordered by genre");
        System.out.println("d: display songs of an album");
        System.out.println("u: display audiobooks ordered by author");
        System.out.println("c: add a new song");
        System.out.println("a: add a new album");
        System.out.println("+: add a song to an album");
        System.out.println("l: add a new audiobook");
        System.out.println("p: create a new playlist from existing songs and audio books");
        System.out.println("-: delete an existing playlist");
        System.out.println("y: add a song to an existing playlist");
        System.out.println("m: play a song");
        System.out.println("s: save elements, albums, playlists");
        System.out.println("q: quit program");
        System.out.println("Your choice is :");
    }

}

