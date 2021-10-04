package fr.gipmds.dsn.test.resources;

public class TestData {

    public static final String aee = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>"
            + "<gipmds:rapport type=\"AEE\" profil=\"DSN\" version=\"v01r04\" xmlns:gipmds=\"http://www.gip-mds.fr/\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">"
            + "<envoi>"
            + "<envoi_identification>"
            + "<declarant><siret>99999106200019</siret><nom>TEST</nom><prenom>MICHEL</prenom></declarant>"
            + "<idflux>UR5WJEBAhi54RcZN8Lur71F</idflux>"
            + "<identifiant>MtoM</identifiant>"
            + "<point_de_depot>01</point_de_depot>"
            + "<essai_reel>02</essai_reel>"
            + "<emetteur_siret>99999106200019</emetteur_siret>"
            + "<date_reception>2014-01-15</date_reception>"
            + "<heure_reception>14:47:42</heure_reception>"
            + "<date_enregistrement>2014-01-15</date_enregistrement>"
            + "<heure_enregistrement>14:47:42</heure_enregistrement>"
            + "</envoi_identification>"
            + "<envoi_bilan><envoi_etat>OK</envoi_etat></envoi_bilan>"
            + "</envoi>" + "</gipmds:rapport>";

    public static final String are = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>"
            + "<gipmds:rapport type=\"ARE\" profil=\"DSN\" version=\"v01r04\" xmlns:gipmds=\"http://www.gip-mds.fr/\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">"
            + "<envoi>"
            + "<envoi_identification>"
            + "<declarant><siret>31784106200019</siret><nom>TEST</nom><prenom>MICHEL</prenom></declarant>"
            + "<idflux>UR5WJEBA5T74R4jNmCgvm1F</idflux>"
            + "<identifiant>MtoM</identifiant>"
            + "<point_de_depot>01</point_de_depot>"
            + "<essai_reel/>"
            + "<emetteur_siret>31784106200019</emetteur_siret>"
            + "<date_reception>2013-10-16</date_reception>"
            + "<heure_reception>10:42:34</heure_reception>"
            + "</envoi_identification>"
            + "<envoi_bilan><envoi_etat>KO</envoi_etat></envoi_bilan>"
            + "<envoi_anomalie>"
            + "<description>"
            + "<code>B1-105-15</code>"
            + "<message>Le format du fichier depot_mtom n'est pas reconnu.</message>"
            + "</description>"
            + "</envoi_anomalie>"
            + "</envoi>"
            + "</gipmds:rapport>";

    public static String retour;

    static {
        for (int i = 0; i < 2000; i++)
            retour += TestData.aee;
    }

    public static final Concentrateur concentrateurInscrit = new Concentrateur(
            "01234567890123", "CTR2000");

    public static final Concentrateur concentrateurNonInscrit = new Concentrateur(
            "01234567890123", "CTR1999");

    public static final Declarant declarantInscrit = new Declarant(
            "98765432109876", "declarant", "declarant");

    public static final Declarant declarantNonInscrit = new Declarant(
            "98765432109876", "Freeman", "Gordon");

    public static final String reponseRecherche = "<retours><flux><id>abcdefghij</id>"
            + "<retour><publication>20130622012000</publication><production>20130622012000</production><nature>10</nature><statut>OK</statut><id>0123456789</id></retour>"
            + "<retour><publication>20130622012000</publication><production>20130622012000</production><nature>11</nature><statut>OK</statut><id>0123456789</id></retour>"
            + "<retour><publication>20130622012000</publication><production>20130622012000</production><nature>20</nature><statut>OK</statut></retour>"
            + "<retour><publication>20130622012000</publication><production>20130622012000</production><nature>21</nature><statut>KO</statut><id>0123456789</id></retour>"
            + "</flux></retours>";

    public static final String reponseRechercheVide = "<retours></retours>";

    public static final String reponseRechercheObjets = "<objets><declarant><siret>12345678901234</siret><nom>FREEMAN</nom><prenom>Gordon</prenom>"
            + "<objet><id>abcdef</id><siren>123456789</siren><nic>12345</nic><nature>FPOC</nature><publication>20130801123242</publication><production>20130801123242</production><url>https://host/telecharger-objet/1.0/abcdef/xml</url><producteur>123456</producteur></objet>"
            + "<objet><id>ghijkl</id><siren>123456789</siren><nic>12345</nic><nature>FPOC</nature><publication>20130801123242</publication>"
            + "<production>20130801123242</production><url>https://host/telecharger-objet/1.0/ghijkl/xml</url><producteur>234567</producteur><porteur>876543</porteur></objet>"
            + "<objet><id>mnopqr</id><siren>123456789</siren><ensemble>ABC</ensemble><nature>FPOC</nature><publication>20130801123242</publication><production>20130801123242</production><url>https://host/telecharger-objet/1.0/mnopqr/xml</url><producteur>345678</producteur></objet>"
            + "</declarant></objets>";


    public static String reponseRechercheListerDepots = "<depots>" +
            " <declarant> <siret>12345678901234</siret> <nom>Test</nom> <prenom>abcde</prenom>" +
            " <depot> <idflux>0123456789</idflux> <date>20130801123242</date> <statut>OK</statut></depot>" +
            " <depot> <idflux>0123456780</idflux> <date>20130801123243</date> <statut>KO</statut></depot>" +
            "</declarant>" +
            "</depots>";

    public static String reponseRechercheListerBPIJ = "<retours>  <declarant>    <siret>12345688912340</siret>" +
            "<declare> <siret>12345688912345</siret> <statut>OK</statut> <bpij>   <publication>20130801</publication>" +
            "   <url>https://.../telecharger-bpij/1.0/F1spdeAilkR4q3UUlQA7haa</url> </bpij> <bpij> " +
            "<publication>20130801</publication>" +
            "   <url>https://.../telecharger-bpij/1.0/F1spdeAilkR4q3UUlQA7hab</url>" +
            " </bpij>    </declare>    <declare> <siret>12345688912346</siret> <statut>OK</statut>" +
            " <message>Pas de BPIJ sur la période</message> </declare>    <declare> <siret>12345688912346</siret>" +
            " <statut>KO</statut> <message>Lien declarant déclaré non validé</message>    </declare>" +
            "  </declarant>  <pageencours>1</pageencours>  <pagetotal>15</pagetotal></retours>";

    public static String reponseRechercheListerBPIJVide = "<retours><declarant><siret>12345688912340</siret><declare>" +
            "<siret>12345688912345</siret><statut>OK</statut><message>Pas de BPIJ sur la période</message></declare>" +
            "<declare><siret>12345688912346</siret><statut>OK</statut><message>Pas de BPIJ sur la période</message>" +
            "</declare></declarant></retours>";
}
