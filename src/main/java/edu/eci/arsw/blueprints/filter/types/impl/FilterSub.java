package edu.eci.arsw.blueprints.filter.types.impl;

import edu.eci.arsw.blueprints.filter.types.FilterType;
import edu.eci.arsw.blueprints.model.Blueprint;
import edu.eci.arsw.blueprints.model.Point;
import edu.eci.arsw.blueprints.persistence.BlueprintNotFoundException;
import edu.eci.arsw.blueprints.persistence.BlueprintPersistenceException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

// Esta clase es un componente de Spring, marcado con @Component
// Esto significa que Spring lo detectará y gestionará automáticamente.
@Component
// La anotación @Qualifier se utiliza para calificar este componente con un nombre específico.
@Qualifier("Sub")
public class FilterSub implements FilterType {

    @Override
    public void filterBlueprint(Blueprint bp) throws BlueprintNotFoundException {
        // Este método filtra los puntos de un Blueprint dejando solo los puntos en índices impares.
        List<Point> points = new ArrayList<Point>(bp.getPoints()); // Copia la lista de puntos del Blueprint.
        List<Point> pointsFilter = new ArrayList<Point>(); // Crear una nueva lista para almacenar los puntos filtrados.

        int size = points.size();
        for (int i = 0; i < points.size(); i++) {
            if (i % 2 == 1) { // Si el índice es impar, agregar el punto a la lista de puntos filtrados.
                pointsFilter.add(points.get(i));
            }
        }
        bp.setPoints(pointsFilter); // Establece la lista de puntos filtrados en el Blueprint original.
    }

    @Override
    public void filterBlueprints(Set<Blueprint> blueprints) throws BlueprintPersistenceException, BlueprintNotFoundException {
        // Este método aplica el filtro a una colección de Blueprints.
        for (Blueprint print : blueprints) {
            filterBlueprint(print); // Llama al método anterior para filtrar cada Blueprint en la colección.
        }
    }

    @Override
    public void filterPrintsByAuthor(String author, Set<Blueprint> blueprints) throws BlueprintNotFoundException {
        // Este método filtra los Blueprints en función del autor.
        for (Blueprint print : blueprints) {
            if (print.getAuthor().equals(author)) { // Si el autor coincide, aplicar el filtro.
                filterBlueprint(print);
            }
        }
    }
}
