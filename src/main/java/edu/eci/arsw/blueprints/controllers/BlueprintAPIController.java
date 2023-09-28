package edu.eci.arsw.blueprints.controllers;

import java.util.*;
import edu.eci.arsw.blueprints.model.Blueprint;
import edu.eci.arsw.blueprints.model.Point;
import edu.eci.arsw.blueprints.persistence.BlueprintNotFoundException;
import edu.eci.arsw.blueprints.persistence.BlueprintPersistenceException;
import edu.eci.arsw.blueprints.persistence.impl.InMemoryBlueprintPersistence;
import edu.eci.arsw.blueprints.persistence.impl.Tuple;
import edu.eci.arsw.blueprints.services.BlueprintsServices;
import org.jboss.logging.Logger;
import org.jboss.logging.Logger.Level;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import org.json.JSONObject;

/**
 * Esta clase define el controlador de la API para administrar blueprints.
 * El controlador maneja peticiones GET y POST relacionadas con blueprints.
 */

@Service
@RestController
public class BlueprintAPIController {

    @Autowired
    BlueprintsServices service;

    // Maneja peticiones GET para obtener todos los blueprints.
    @RequestMapping(value = "/blueprints", method = RequestMethod.GET)
    public ResponseEntity<?> manejadorGetBluePrints() {
        ResponseEntity<?> mensaje = null;
        Set<Blueprint> bps = null;

        try {
            bps = service.getAllBlueprints();
            service.applyFilter(bps);
            mensaje = new ResponseEntity<>(bps, HttpStatus.ACCEPTED);
        } catch (BlueprintNotFoundException e) {
            mensaje = new ResponseEntity<>("No se encontró el autor", HttpStatus.NOT_FOUND);
        } catch (BlueprintPersistenceException e) {
            mensaje = new ResponseEntity<>("Algo salió mal", HttpStatus.BAD_REQUEST);
        }
        return mensaje;
    }

    // Maneja peticiones GET para obtener un blueprint específico por autor y nombre.
    @RequestMapping(value = "/blueprints/{author}/{bpname}")
    public ResponseEntity<?> manejadorGetBluePrint(@PathVariable String author, @PathVariable String bpname) {
        Blueprint bp = null;
        ResponseEntity<?> mensaje;

        try {
            bp = service.getBlueprint(author, bpname);
            mensaje = new ResponseEntity<>(bp, HttpStatus.ACCEPTED);
        } catch (BlueprintNotFoundException e) {
            mensaje = new ResponseEntity<>("No existe autor o plano con ese nombre.", HttpStatus.NOT_FOUND);
        }
        return mensaje;
    }

    // Maneja peticiones GET para obtener todos los blueprints de un autor específico.
    @RequestMapping(value = "/blueprints/{author}", method = RequestMethod.GET)
    public ResponseEntity<?> manejadorGetBluePrintsByAuthor(@PathVariable String author) {
        ResponseEntity<?> mensaje;
        Set<Blueprint> bps = null;

        try {
            bps = service.getBlueprintsByAuthor(author);
            mensaje = new ResponseEntity<>(bps, HttpStatus.ACCEPTED);
        } catch (BlueprintNotFoundException e) {
            mensaje = new ResponseEntity<>("No se encontró el autor", HttpStatus.NOT_FOUND);
        } catch (BlueprintPersistenceException e) {
            mensaje = new ResponseEntity<>("Algo salió mal", HttpStatus.BAD_REQUEST);
        }
        return mensaje;
    }

    // Maneja peticiones POST para agregar un nuevo blueprint.
    @RequestMapping(value = "/blueprints/addBlueprint", method = RequestMethod.POST, consumes = "application/json")
    public ResponseEntity<?> manejadorPostRecursoBluePrint(@RequestBody Blueprint bp) {
        ResponseEntity<?> mensaje;

        try {
            // Registrar el nuevo blueprint.
            service.addNewBlueprint(bp);
            mensaje = new ResponseEntity<>(HttpStatus.CREATED);
        } catch (BlueprintPersistenceException e) {
            Logger.getLogger(BlueprintAPIController.class.getName()).log(Level.FATAL, null, e);
            mensaje = new ResponseEntity<>("El nombre del plano ya existe", HttpStatus.NOT_ACCEPTABLE);
        }
        return mensaje;
    }

    // Maneja peticiones PUT para actualizar un blueprint existente con nuevos puntos.
    @PutMapping(value = "/blueprints/{author}/{bpname}")
    public ResponseEntity<?> manejadorPutRecursoBluePrint(@PathVariable String author, @PathVariable String bpname, @RequestBody List<Point> points) {
        ResponseEntity<?> mensaje;

        try {
            // Actualizar el blueprint con los nuevos puntos.
            service.updateBluePrint(author, bpname, points);
            Blueprint bp = service.getBlueprint(author, bpname);
            mensaje = new ResponseEntity<>(bp, HttpStatus.ACCEPTED);
        } catch (BlueprintNotFoundException e) {
            mensaje = new ResponseEntity<>("No existe el plano con el nombre dado", HttpStatus.NOT_FOUND);
        }
        return mensaje;
    }
}
