package Controller;

import java.lang.Exception;

/**
 * classe qui renvoie une exception lorsque la playlist n'est pas trouvé
 */
public class NoPlayListFoundException extends Exception {

    public NoPlayListFoundException (String msg) {
        super(msg);
    }
}
