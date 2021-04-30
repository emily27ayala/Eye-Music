package Controller;

import java.lang.Exception;

/**
 * classe qui renvoie une exception lorsque l'element' n'est pas trouvé
 */
public class NoElementFoundException extends Exception {

    public NoElementFoundException (String msg) {
        super(msg);
    }
}