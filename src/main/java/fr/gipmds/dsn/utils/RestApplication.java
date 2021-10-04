package fr.gipmds.dsn.utils;


import fr.gipmds.dsn.services.auth.ServiceAuthentificationMSAImpl;
import fr.gipmds.dsn.services.auth.ServiceAuthentificationNetEImpl;
import fr.gipmds.dsn.services.depot.ServiceDepot2;
import fr.gipmds.dsn.services.depot.ServiceDepotImpl;
import fr.gipmds.dsn.services.depot.recherche.ServiceRechercherDepotsParConcentrateur;
import fr.gipmds.dsn.services.depot.recherche.ServiceRechercherDepotsParConcentrateur2;
import fr.gipmds.dsn.services.recherche.*;
import fr.gipmds.dsn.services.telechargement.ServiceTelechargement;

import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

public class RestApplication extends Application {
    private final Set<Object> singletons = new HashSet<>();

    public RestApplication() {


        singletons.add(new ServiceAuthentificationMSAImpl());
        singletons.add(new ServiceAuthentificationNetEImpl());

        singletons.add(new ServiceDepot2());
        singletons.add(new ServiceDepotImpl());

        singletons.add(new ServiceRechercheObjetsParConcentrateur());
        singletons.add(new ServiceRechercheObjetsParDeclarant());
        singletons.add(new ServiceRechercheParConcentrateur());
        singletons.add(new ServiceRechercheParDeclarant());
        singletons.add(new ServiceRechercheParDeclarant2());
        singletons.add(new ServiceRechercheParFlux());
        singletons.add(new ServiceRechercheParFlux2());

        singletons.add(new ServiceTelechargement());

        singletons.add(new ServiceRechercherDepotsParConcentrateur());
        singletons.add(new ServiceRechercherDepotsParConcentrateur2());

        singletons.add(new ServiceRechercheBPIJ());

        singletons.add(new ExpiresHeaderFilter());
    }

    @Override
    public Set<Object> getSingletons() {
        return singletons;
    }
}