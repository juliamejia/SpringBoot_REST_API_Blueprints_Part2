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

@Component
@Qualifier("Redundancy")
public class FilterRedundancy implements FilterType {
    // Método para revisar y eliminar puntos redundantes en un plano
    public void review(Blueprint bp, Point point){
        List<Point> points = new ArrayList<Point>(bp.getPoints());
        for(int i = 0; i <= points.size() - 1; i++){
            if(point.equals(points.get(i))){
                points.remove(i); // Si el punto es igual a otro, lo elimina para eliminar la redundancia
            }
        }
        points.add(point); // Agrega el punto al plano sin redundancia
        bp.setPoints(points); // Establece la lista de puntos sin redundancia en el plano
    }

    @Override
    public void filterBlueprint(Blueprint bp) throws BlueprintNotFoundException {
        for(Point point : bp.getPoints()){
            review(bp, point); // Aplica la revisión de redundancia a cada punto del plano
        }
    }

    @Override
    public void filterBlueprints(Set<Blueprint> blueprints) throws BlueprintPersistenceException, BlueprintNotFoundException {
        for(Blueprint print : blueprints){
            filterBlueprint(print); // Aplica la revisión de redundancia a cada plano en el conjunto
        }
    }

    @Override
    public void filterPrintsByAuthor(String author, Set<Blueprint> blueprints) throws BlueprintNotFoundException {
        for(Blueprint print : blueprints){
            if(print.getAuthor().equals(author)){
                filterBlueprint(print); // Aplica la revisión de redundancia solo a los planos del autor especificado
            }
        }
    }
}
