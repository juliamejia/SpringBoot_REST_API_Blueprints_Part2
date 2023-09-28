package edu.eci.arsw.blueprints.filter.types;

import edu.eci.arsw.blueprints.model.Blueprint;
import edu.eci.arsw.blueprints.persistence.BlueprintNotFoundException;
import edu.eci.arsw.blueprints.persistence.BlueprintPersistenceException;

import java.util.Set;

public interface FilterType {

    // Método para filtrar un solo plano Blueprint.
    // Puede arrojar una excepción BlueprintNotFoundException si el plano no se encuentra.
    public void filterBlueprint(Blueprint bp) throws BlueprintNotFoundException;

    // Método para filtrar un conjunto (Set) de planos Blueprints.
    // Puede arrojar excepciones BlueprintPersistenceException o BlueprintNotFoundException.
    public void filterBlueprints(Set<Blueprint> blueprints) throws BlueprintPersistenceException, BlueprintNotFoundException;

    // Método para filtrar planos por autor en un conjunto (Set) dado.
    // Puede arrojar una excepción BlueprintNotFoundException si el autor no se encuentra.
    public void filterPrintsByAuthor(String author, Set<Blueprint> blueprints) throws BlueprintNotFoundException;
}
