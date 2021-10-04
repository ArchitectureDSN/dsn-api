package fr.gipmds.dsn.test;

import fr.gipmds.dsn.utils.DateUtils;

import java.util.Calendar;

public class TestUtils {


    public static String getDateDebutFormatted(int plageDeRecherche) {
        final Calendar dateDebut = Calendar.getInstance();
        dateDebut.add(Calendar.MINUTE, -plageDeRecherche + 1);
        return DateUtils.format(dateDebut.getTime());
    }


    // Table d’adressage des services voir le sous titre 10.2. Table d’adressage des services
    //	du Guide d’implémentation de l’API DSN (v4-4)
//    public static final String baseUriAuthentification = "http://localhost";
//    public static final String baseUriDepotNetE = "http://localhost";
//    public static final String baseUriDepotMsa = "http://localhost";
//    public static final String baseUriConsultationNetE = "http://localhost";
//    public static final String baseUriConsultationMsa = "http://localhost";
//    public static final String baseUriTelechargementNetE = "http://localhost";
//    public static final String baseUriTelechargementMsa = "http://localhost";
}
