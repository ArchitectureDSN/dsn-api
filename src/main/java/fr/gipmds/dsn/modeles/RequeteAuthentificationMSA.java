package fr.gipmds.dsn.modeles;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "identifiants")
@XmlAccessorType(XmlAccessType.NONE)
public class RequeteAuthentificationMSA {

    @XmlElement(name = "identifiant")
    private String identifiant;

    @XmlElement(name = "motdepasse")
    private String motdepasse;

    public String getIdentifiant() {
        return identifiant;
    }

    public void setIdentifiant(String identifiant) {
        this.identifiant = identifiant;
    }

    public String getMotdepasse() {
        return motdepasse;
    }

    public void setMotdepasse(String motdepasse) {
        this.motdepasse = motdepasse;
    }

}