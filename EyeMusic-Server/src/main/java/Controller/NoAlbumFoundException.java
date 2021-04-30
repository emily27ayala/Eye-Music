package Controller;

import java.lang.Exception;

/**
 *  classe qui renvoie une exception lorsque l'album n'est pas trouvé
 */
public class NoAlbumFoundException extends Exception {

    public NoAlbumFoundException (String msg) {
        super(msg);
    }
}