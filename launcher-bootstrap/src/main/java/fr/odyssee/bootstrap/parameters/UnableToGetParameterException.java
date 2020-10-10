package fr.odyssee.bootstrap.parameters;

import fr.odyssee.bootstrap.Main;

public class UnableToGetParameterException extends Exception {
    private static final long serialVersionUID = 1L;

    public UnableToGetParameterException(Parameters parameter) {
        super("Impossible de connaitre le paramètre '" + parameter.getName() + "' !\n\nVérifiez votre connexion internet !\nSinon essayez de retélécharger le launcher sur le site " + Main.server_url);
    }
}