package fr.gipmds.dsn.test.resources;

import fr.gipmds.dsn.utils.Base64Utils;

public class Concentrateur {
    public String siret;
    public String nom;

    public Concentrateur(String siret, String nom) {
        this.siret = siret;
        this.nom = nom;
    }

    public String getFauxJeton() {
        String jeton = "<xmlresult><status>0</status><error_msg>Succes</error_msg>"
                + "<format>3</format><creation_time>22/06/2013 01:20:00(1)</creation_time><siret>"
                + siret
                + "</siret><ttl>7200</ttl><first_name>concentrateur</first_name><last_name>"
                + nom + "</last_name></xmlresult>";

        jeton = Base64Utils.encode(jeton);

        return jeton;
    }
}