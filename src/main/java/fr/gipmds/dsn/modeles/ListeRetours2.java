package fr.gipmds.dsn.modeles;

import com.fasterxml.jackson.annotation.JsonRootName;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@JsonRootName(value = "Retours")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "retours")
public class ListeRetours2 {

    public List<MetadonneesFlux2> flux = new ArrayList<MetadonneesFlux2>();
}
