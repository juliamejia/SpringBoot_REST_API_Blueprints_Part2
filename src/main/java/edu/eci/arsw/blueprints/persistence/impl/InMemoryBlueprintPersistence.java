/*
 * Este es un componente de persistencia de Blueprints en memoria que implementa la interfaz BlueprintsPersistence.
 * Utiliza un mapa para almacenar los Blueprints en memoria.
 */
package edu.eci.arsw.blueprints.persistence.impl;

import edu.eci.arsw.blueprints.model.Blueprint;
import edu.eci.arsw.blueprints.model.Point;
import edu.eci.arsw.blueprints.persistence.BlueprintNotFoundException;
import edu.eci.arsw.blueprints.persistence.BlueprintPersistenceException;
import edu.eci.arsw.blueprints.persistence.BlueprintsPersistence;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Implementación de la interfaz BlueprintsPersistence que almacena Blueprints en memoria.
 */
@Component
@Qualifier("Memory")
public class InMemoryBlueprintPersistence implements BlueprintsPersistence {
    private final int VALUE_PRINTS = 5;
    private final ConcurrentHashMap<Tuple<String,String>,Blueprint> blueprints=new ConcurrentHashMap<>();
    /**
     * Funcion generada para crear dos blueprints que esten con el mismo autor y una cantidad definida por el
     * usuario donde no se puedan repetir (Segundo ciclo for)
     */


    // Constructor que carga datos de prueba al inicializar la instancia
    public InMemoryBlueprintPersistence() {
        Point[] pts = new Point[]{new Point(140, 140), new Point(115, 115)};
        Blueprint bp = new Blueprint("_authorname_", "_bpname_ ", pts);
        blueprints.put(new Tuple<>(bp.getAuthor(), bp.getName()), bp);
        inicializarPrints();
    }

    // Este método inicializa una colección de objetos Blueprint con datos aleatorios.

    private void inicializarPrints() {
        Random random = new Random(); // Creamos una instancia de Random para generar números aleatorios.

        // Crear dos blueprints con el mismo autor.
        for (int i = 0; i < 2; i++) { // Iteramos dos veces para crear dos blueprints.
            Point[] points = new Point[10]; // Creamos un arreglo de 10 puntos.
            for (int j = 0; j < 10; j++) {
                points[j] = new Point(random.nextInt(100), random.nextInt(100)); // Generamos coordenadas aleatorias para los puntos.
            }
            // Creamos un nuevo Blueprint con autor "Cristian Rodriguez", nombre "Blueprint" + i y los puntos generados.
            Blueprint newBp = new Blueprint("Cristian Rodriguez", "Blueprint" + i, points);
            // Usamos un objeto Tuple para crear una clave única (autor y nombre) y luego agregamos el Blueprint al mapa blueprints.
            blueprints.put(new Tuple<>(newBp.getAuthor(), newBp.getName()), newBp);
        }

        // Asignar valores aleatorios para cada autor para evitar que se repitan.
        for (int i = 0; i < VALUE_PRINTS; i++) { // Iteramos VALUE_PRINTS veces para crear más blueprints.
            Point[] points = new Point[10]; // Creamos un arreglo de 10 puntos.
            for (int j = 0; j < 10; j++) {
                points[j] = new Point(random.nextInt(100), random.nextInt(100)); // Generamos coordenadas aleatorias para los puntos.
            }
            // Creamos un nuevo Blueprint con autor "Author" + número aleatorio y nombre "Blueprint" + i.
            Blueprint bp = new Blueprint("Author" + random.nextInt(100) + 10, "Blueprint" + i);
            // Usamos un objeto Tuple para crear una clave única (autor y nombre) y luego agregamos el Blueprint al mapa blueprints.
            blueprints.put(new Tuple<>(bp.getAuthor(), bp.getName()), bp);
        }
    }


    // Método para guardar un Blueprint en memoria
    @Override
    public void saveBlueprint(Blueprint bp) throws BlueprintPersistenceException {
        if (blueprints.containsKey(new Tuple<>(bp.getAuthor(), bp.getName()))) {
            throw new BlueprintPersistenceException("El Blueprint proporcionado ya existe: " + bp);
        } else {
            blueprints.put(new Tuple<>(bp.getAuthor(), bp.getName()), bp);
        }
    }

    // Método para obtener un Blueprint por autor y nombre
    @Override
    public Blueprint getBlueprint(String author, String bprintname) throws BlueprintNotFoundException {
        return blueprints.get(new Tuple<>(author, bprintname));
    }

    // Método para obtener todos los Blueprints almacenados en memoria
    @Override
    public Set<Blueprint> getBluePrints() throws BlueprintPersistenceException, BlueprintNotFoundException {
        Set<Blueprint> prints = new HashSet<>();
        for (Tuple<String, String> tuple : this.blueprints.keySet()) {
            prints.add(blueprints.get(tuple));
        }
        return prints;
    }

    // Método para obtener Blueprints por autor
    @Override
    public Set<Blueprint> getBlueprintsByAuthor(String author) throws BlueprintNotFoundException {
        Set<Blueprint> prints = new HashSet<>();
        for (Tuple<String, String> tuple : this.blueprints.keySet()) {
            if (tuple.o1.equals(author)) {
                prints.add(blueprints.get(tuple));
            }
        }
        return prints;
    }
}
