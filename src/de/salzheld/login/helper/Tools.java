package de.salzheld.login.helper;

/**
 * Created by Joern on 23.01.2016.
 */
public final class Tools {


    /**
     * Erzeugt den LoNet²-Login
     **/
    public static String buildLogin(String firstName, String lastName) {
        return sanitizeName(lastName) + "-" + sanitizeName(firstName);
    }

    /**
     * Bereinigt den als Parameter übergebenen String.
     * Der String wird in Kleinbuchstaben gewandelt, Umlaute und Akzente werden
     * durch Internet-Kompatible Varianten ersetzt (ö->oe etc.)
     * Bei mehr als einem Namensteil wird nur der erste Name zurückgeliefert.
     *
     * @return  Internet-Kompatibler Name
     **/
    public static String sanitizeName(String name) {
        name = name.toLowerCase();
        name = name.replace("dos ", "dos-");
        name = name.replace("la ", "la-");
        name = name.replace("von ", "");
        name = name.replace("van ", "");

        if((int) name.indexOf(' ') > 0) {
            name = name.substring(0, name.indexOf(' '));
        }

        name = name.replaceAll("ä","ae");
        name = name.replaceAll("ö","oe");
        name = name.replaceAll("ü","ue");
        name = name.replaceAll("ß","ss");

        name = name.replaceAll("ç","c");
        name = name.replaceAll("é","e");
        name = name.replaceAll("è","e");
        name = name.replaceAll("š","s");
        name = name.replaceAll("ó","o");

        return name;
    }
}
