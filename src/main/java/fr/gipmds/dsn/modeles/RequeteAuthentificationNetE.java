package fr.gipmds.dsn.modeles;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.io.StringWriter;

@XmlRootElement(name = "identifiants")
@XmlAccessorType(XmlAccessType.NONE)
public class RequeteAuthentificationNetE implements Serializable {

    private static final long serialVersionUID = 1L;

    @XmlElement(name = "siret")
    private String siret;

    @XmlElement(name = "nom")
    private String nom;

    @XmlElement(name = "prenom")
    private String prenom;

    @XmlElement(name = "motdepasse")
    private String motdepasse;

    @XmlElement(name = "service")
    private String service;

    public String getSiret() {
        return siret;
    }

    public void setSiret(String siret) {
        this.siret = siret;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getMotdepasse() {
        return motdepasse;
    }

    public void setMotdepasse(String motdepasse) {
        this.motdepasse = motdepasse;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    @Override
    public String toString() {
        JAXBContext context;
        try {
            context = JAXBContext.newInstance(RequeteAuthentificationNetE.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            StringWriter out = new StringWriter();
            marshaller.marshal(this, out);
            return out.toString();
        } catch (JAXBException e) {
            e.printStackTrace();
            return null;
        }
    }
}