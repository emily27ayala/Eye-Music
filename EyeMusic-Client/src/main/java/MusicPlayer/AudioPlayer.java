package MusicPlayer;
import java.io.File;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 * Cette classe permet de lancer un lecteur de fichier .wav permettant de jouer un fichier audio
 * en utilisant SourceDataLine de Java Sound API.
 *
 *
 */

public class AudioPlayer implements Observer {

    // size of the byte buffer used to read/write the audio stream
    private static final int BUFFER_SIZE = 4096;
    private String audioPathFile;
    private AudioPathObservable audioPathObservable;

    /**
     * Joue un fichier audio donné.
     * @param audioFilePath Path of the audio file.
     */
    void play(String audioFilePath) {
        File audioFile = new File(audioFilePath);
        try {
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);

            AudioFormat format = audioStream.getFormat();

            DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);

            SourceDataLine audioLine = (SourceDataLine) AudioSystem.getLine(info);

            audioLine.open(format);

            audioLine.start();

            System.out.println("Playback started.");

            byte[] bytesBuffer = new byte[BUFFER_SIZE];
            int bytesRead = -1;

            while ((bytesRead = audioStream.read(bytesBuffer)) != -1) {
                audioLine.write(bytesBuffer, 0, bytesRead);
            }
            audioLine.drain();
            audioLine.close();
            audioStream.close();
            System.out.println("Playback completed.");

        } catch (UnsupportedAudioFileException ex) {
            System.out.println("The specified audio file is not supported.");
            ex.printStackTrace();
        } catch (LineUnavailableException ex) {
            System.out.println("Audio line for playing back is unavailable.");
            ex.printStackTrace();
        } catch (IOException ex) {
            System.out.println("Error playing the audio file.");
            ex.printStackTrace();
        }
    }

    /**
     * Fonction controlant l'observer qui lorsque qu'il est notifié du changement
     * de la variable audioPathMusicFile de la classe observable AudioPathObservable
     * lance le lecteur audio pour jouer le fichier audio choisi.
     * @param obj
     * @param arg
     */
    public void update(Observable obj, Object arg) {
        if (arg instanceof String) {
            audioPathFile = (String) arg;
            System.out.println("NameObserver: Name changed to " + audioPathFile);
            play(audioPathFile);
        } else {
            System.out.println("NameObserver: Some other change to subject!");
        }
    }

}