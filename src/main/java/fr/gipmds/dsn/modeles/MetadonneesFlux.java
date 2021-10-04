package fr.gipmds.dsn.modeles;

import javax.xml.bind.annotation.XmlElement;
import java.util.ArrayList;
import java.util.List;

public class MetadonneesFlux {

    public String id;

    @XmlElement(name = "retour")
    public List<MetadonneesRetour> retours = new ArrayList<MetadonneesRetour>();
}