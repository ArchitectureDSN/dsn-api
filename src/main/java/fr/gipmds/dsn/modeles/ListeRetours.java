package fr.gipmds.dsn.modeles;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "retours")
@XmlAccessorType(XmlAccessType.FIELD)
public class ListeRetours {

    public List<MetadonneesFlux> flux = new ArrayList<MetadonneesFlux>();
}
