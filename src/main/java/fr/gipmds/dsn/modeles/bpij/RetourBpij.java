package fr.gipmds.dsn.modeles.bpij;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "retours")
@XmlAccessorType(XmlAccessType.FIELD)
public class RetourBpij {
    @XmlElement(name = "declarant")
    public List<Declarant> declarants;
    public Integer pageencours;
    public Integer pagetotal;


}
