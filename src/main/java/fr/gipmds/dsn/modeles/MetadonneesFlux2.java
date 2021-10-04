package fr.gipmds.dsn.modeles;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.xml.bind.annotation.XmlElement;
import java.util.ArrayList;
import java.util.List;

public class MetadonneesFlux2 {

    public String id;
    @JsonProperty(value = "Retour")
    @XmlElement(name = "retour")
    public List<MetadonneesRetour> retours = new ArrayList<MetadonneesRetour>();
}