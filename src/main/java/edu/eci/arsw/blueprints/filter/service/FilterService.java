package edu.eci.arsw.blueprints.filter.service;

import edu.eci.arsw.blueprints.filter.types.FilterType;
import edu.eci.arsw.blueprints.model.Blueprint;
import edu.eci.arsw.blueprints.persistence.BlueprintNotFoundException;
import edu.eci.arsw.blueprints.persistence.BlueprintPersistenceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import java.util.Set;

@Service
public class FilterService {
    @Autowired
    @Qualifier("Redundancy")
    FilterType filter;

    // El servicio de filtrado utiliza una implementación concreta de FilterType (marcada como "Redundancy").

    public void filterBlueprint(Blueprint bp) throws BlueprintNotFoundException {
        // Llama al método de filtrado específico implementado en FilterType para un solo plano (Blueprint).
        filter.filterBlueprint(bp);
    }

    public void filterBlueprints(Set<Blueprint> blueprints) throws BlueprintNotFoundException, BlueprintPersistenceException {
        // Llama al método de filtrado específico implementado en FilterType para un conjunto de planos (Blueprints).
        filter.filterBlueprints(blueprints);
    }
}
