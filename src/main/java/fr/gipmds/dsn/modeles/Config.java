package fr.gipmds.dsn.modeles;

public class Config {
    public static final int plageDeRechercheParDeclarant = 60; // 60 minutes
    public static final int plageDeRechercheParConcentrateur = 10; // 10 minutes
    public static final long RATE_LIMITING_BACKOFF = 2 * 60 * 1000L; // 2 minutes in millis
}
