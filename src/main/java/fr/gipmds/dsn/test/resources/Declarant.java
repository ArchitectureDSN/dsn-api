package fr.gipmds.dsn.test.resources;

import fr.gipmds.dsn.utils.Base64Utils;

public class Declarant {
    public String siret;
    public String nom;
    public String prenom;
    public String motdepasse;
    public String status;

    public Declarant(String siret, String nom, String prenom, String motdepasse, String status) {
        this.siret = siret;
        this.nom = nom;
        this.prenom = prenom;
        this.motdepasse = motdepasse;
        this.status = status;
    }

    public Declarant(String siret, String nom, String prenom) {
        this.siret = siret;
        this.nom = nom;
        this.prenom = prenom;
    }

    public String getFauxJeton() {
        String jeton = "<xmlresult><status>0</status><error_msg>Succes</error_msg>"
                + "<format>3</format><creation_time>22/06/2013 01:20:00(1)</creation_time>"
                + "<siret>"
                + siret
                + "</siret><ttl>7200</ttl><first_name>"
                + prenom
                + "</first_name>"
                + "<last_name>"
                + nom
                + "</last_name>"
                + "</xmlresult>";

        jeton = Base64Utils.encode(jeton);

        return jeton;
    }

    public String getSNP() {
        return siret + ";" + nom + ";" + prenom;
    }

}