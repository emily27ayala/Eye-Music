package MusicPlayer;

import java.util.Observable;

public class AudioPathObservable extends Observable {

    private String audioPathMusicFile;

    /**
     *  Contructeur de la classe AudioPathObservable
     * @param audioPathMusicFile  c'est le chemin vers le fichier audio en .wav
     */
    public AudioPathObservable(String audioPathMusicFile) {
        this.audioPathMusicFile = audioPathMusicFile;
    }

    /**
     * Permet de récupérer la valeur de la variable audioPathMusicFile
     * @return audioPathMusicFile
     */
    public String getAudioPathMusicFile() {
        return audioPathMusicFile;
    }

    /**
     * Modifie la valeur de la variable audioPathMusicFile et notifie l'observateur que la variable a été changé
     * @param audioPathMusicFile le chemin menant vers la chanson ou le livre audio choisi pour la lecture
     */
    public void setAudioPathMusicFile(String audioPathMusicFile) {
        this.audioPathMusicFile = audioPathMusicFile;
        setChanged();
        notifyObservers(audioPathMusicFile);
    }

}
