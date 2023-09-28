### Escuela Colombiana de Ingeniería

### Arquitecturas de Software

#### Integrantes: Cristian Rodriguez y Julia Mejia  



#### API REST para la gestión de planos.

En este ejercicio se va a construír el componente BlueprintsRESTAPI, el cual permita gestionar los planos arquitectónicos de una prestigiosa compañia de diseño. La idea de este API es ofrecer un medio estandarizado e 'independiente de la plataforma' para que las herramientas que se desarrollen a futuro para la compañía puedan gestionar los planos de forma centralizada.
El siguiente, es el diagrama de componentes que corresponde a las decisiones arquitectónicas planteadas al inicio del proyecto:

![](img/CompDiag.png)

Donde se definió que:

* El componente BlueprintsRESTAPI debe resolver los servicios de su interfaz a través de un componente de servicios, el cual -a su vez- estará asociado con un componente que provea el esquema de persistencia. Es decir, se quiere un bajo acoplamiento entre el API, la implementación de los servicios, y el esquema de persistencia usado por los mismos.

Del anterior diagrama de componentes (de alto nivel), se desprendió el siguiente diseño detallado, cuando se decidió que el API estará implementado usando el esquema de inyección de dependencias de Spring (el cual requiere aplicar el principio de Inversión de Dependencias), la extensión SpringMVC para definir los servicios REST, y SpringBoot para la configurar la aplicación:


![](img/ClassDiagram.png)

### Parte I

1. Integre al proyecto base suministrado los Beans desarrollados en el ejercicio anterior. Sólo copie las clases, NO los archivos de configuración. Rectifique que se tenga correctamente configurado el esquema de inyección de dependencias con las anotaciones @Service y @Autowired.  
   <img width="161" alt="image" src="https://github.com/juliamejia/SpringBoot_REST_API_Blueprints_Part2/assets/98657146/cd501b72-096f-4dac-a5bd-57c7da4f157a">  

2. Modifique el bean de persistecia 'InMemoryBlueprintPersistence' para que por defecto se inicialice con al menos otros tres planos, y con dos asociados a un mismo autor.
   * En la clase indicada implementamos el siguiente metodo:
     
   ```java 
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
     
   	```
3. Configure su aplicación para que ofrezca el recurso "/blueprints", de manera que cuando se le haga una petición GET, retorne -en formato jSON- el conjunto de todos los planos. Para esto:

	* Modifique la clase BlueprintAPIController teniendo en cuenta el siguiente ejemplo de controlador REST hecho con SpringMVC/SpringBoot:

	```java
	@RestController
	@RequestMapping(value = "/url-raiz-recurso")
	public class XXController {
    
        
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<?> manejadorGetRecursoXX(){
        try {
            //obtener datos que se enviarán a través del API
            return new ResponseEntity<>(data,HttpStatus.ACCEPTED);
        } catch (XXException ex) {
            Logger.getLogger(XXController.class.getName()).log(Level.SEVERE, null, ex);
            return new ResponseEntity<>("Error bla bla bla",HttpStatus.NOT_FOUND);
        }        
	}

	```
	* Haga que en esta misma clase se inyecte el bean de tipo BlueprintServices (al cual, a su vez, se le inyectarán sus dependencias de persisntecia y de filtrado de puntos).
* En la clase indicada añadimos el siguiente metodo
  
  ```java 
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
    ```

4. Verifique el funcionamiento de a aplicación lanzando la aplicación con maven:

	```bash
	$ mvn compile
	$ mvn spring-boot:run
	
	```
	<img width="451" alt="image" src="https://github.com/juliamejia/SpringBoot_REST_API_Blueprints_Part2/assets/98657146/d4dbad29-a458-4c3c-9c68-ebb1dd822093">  
 
	Y luego enviando una petición GET a: http://localhost:8080/blueprints. Rectifique que, como respuesta, se obtenga un objeto jSON con una lista que contenga el detalle de los planos suministados por defecto, y que se haya aplicado el filtrado de puntos correspondiente.  
	 <img width="458" alt="image" src="https://github.com/juliamejia/SpringBoot_REST_API_Blueprints_Part2/assets/98657146/124419de-091b-4bef-8a72-9532927a9c0b">  
	 
5. Modifique el controlador para que ahora, acepte peticiones GET al recurso /blueprints/{author}, el cual retorne usando una representación jSON todos los planos realizados por el autor cuyo nombre sea {author}. Si no existe dicho autor, se debe responder con el código de error HTTP 404. Para esto, revise en [la documentación de Spring](http://docs.spring.io/spring/docs/current/spring-framework-reference/html/mvc.html), sección 22.3.2, el uso de @PathVariable. De nuevo, verifique que al hacer una petición GET -por ejemplo- a recurso http://localhost:8080/blueprints/juan, se obtenga en formato jSON el conjunto de planos asociados al autor 'juan' (ajuste esto a los nombres de autor usados en el punto 2).  
   * En BluePrintAPIController
     
     ```java
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
  	 ```

	* Para: http://localhost:8080/blueprints   
  	<img width="457" alt="image" src="https://github.com/juliamejia/SpringBoot_REST_API_Blueprints_Part2/assets/98657146/28c3b5b0-065f-4966-8436-fa0d13c1a9cf">  
   
	* Para: http://localhost:8080/blueprints/Cristian%Rodriguez   
  	<img width="451" alt="image" src="https://github.com/juliamejia/SpringBoot_REST_API_Blueprints_Part2/assets/98657146/92fb1054-aa41-4302-a88e-167a93db9d92">   

6. Modifique el controlador para que ahora, acepte peticiones GET al recurso /blueprints/{author}/{bpname}, el cual retorne usando una representación jSON sólo UN plano, en este caso el realizado por {author} y cuyo nombre sea {bpname}. De nuevo, si no existe dicho autor, se debe responder con el código de error HTTP 404.
   * En BlueprintAPIController implementamos el siguiente metodo:
     ```java
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
   		```
	* Para: http://localhost:8080/blueprints/Cristian%Rodriguez  
 	 <img width="457" alt="image" src="https://github.com/juliamejia/SpringBoot_REST_API_Blueprints_Part2/assets/98657146/a62c192a-5e4d-488c-b63c-756af77216d5">
   
	* Para: http://localhost:8080/blueprints/Cristian%Rodriguez/BluePrint1  
  	<img width="458" alt="image" src="https://github.com/juliamejia/SpringBoot_REST_API_Blueprints_Part2/assets/98657146/fe7ece3c-bb0f-43b2-a4a5-460c97f96730">  

### Parte II

1.  Agregue el manejo de peticiones POST (creación de nuevos planos), de manera que un cliente http pueda registrar una nueva orden haciendo una petición POST al recurso ‘planos’, y enviando como contenido de la petición todo el detalle de dicho recurso a través de un documento jSON. Para esto, tenga en cuenta el siguiente ejemplo, que considera -por consistencia con el protocolo HTTP- el manejo de códigos de estados HTTP (en caso de éxito o error):

	```	java
	@RequestMapping(method = RequestMethod.POST)	
	public ResponseEntity<?> manejadorPostRecursoXX(@RequestBody TipoXX o){
        try {
            //registrar dato
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (XXException ex) {
            Logger.getLogger(XXController.class.getName()).log(Level.SEVERE, null, ex);
            return new ResponseEntity<>("Error bla bla bla",HttpStatus.FORBIDDEN);            
        }        
 	
	}
	```	

* En la clase BlueprintAPIController
  
  ```java
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
  ```	

2.  Para probar que el recurso ‘planos’ acepta e interpreta
    correctamente las peticiones POST, use el comando curl de Unix. Este
    comando tiene como parámetro el tipo de contenido manejado (en este
    caso jSON), y el ‘cuerpo del mensaje’ que irá con la petición, lo
    cual en este caso debe ser un documento jSON equivalente a la clase
    Cliente (donde en lugar de {ObjetoJSON}, se usará un objeto jSON correspondiente a una nueva orden:

	```	
	$ curl -i -X POST -HContent-Type:application/json -HAccept:application/json http://URL_del_recurso_ordenes -d '{ObjetoJSON}'
	```	

	Con lo anterior, registre un nuevo plano (para 'diseñar' un objeto jSON, puede usar [esta herramienta](http://www.jsoneditoronline.org/)):
	

	Nota: puede basarse en el formato jSON mostrado en el navegador al consultar una orden con el método GET.

* Usando el siguiente comando creamos un nuevo usuario llamado Julia Mejia:  
  
  ```
  curl -i -X POST -HContent-Type:application/json -HAccept:application/json http://localhost:8080/blueprints/addBlueprint -d '{"author":"Julia Mejia","points":[{"x":78,"y":90},{"x":14,"y":65}],"name":"PruebaPost"}'
  ```
   <img width="439" alt="image" src="https://github.com/juliamejia/SpringBoot_REST_API_Blueprints_Part2/assets/98657146/a76bf7ca-27f6-4cc6-b9bc-97903262c165">  

 * Si ya existe el plano  
   <img width="444" alt="image" src="https://github.com/juliamejia/SpringBoot_REST_API_Blueprints_Part2/assets/98657146/78893369-1a6a-4781-bb63-884f35750671">  

3. Teniendo en cuenta el autor y numbre del plano registrado, verifique que el mismo se pueda obtener mediante una petición GET al recurso '/blueprints/{author}/{bpname}' correspondiente.  
   * Para: http://localhost:8080/blueprints/Julia%20Mejia  
     <img width="426" alt="image" src="https://github.com/juliamejia/SpringBoot_REST_API_Blueprints_Part2/assets/98657146/a1843a67-8d36-4ed5-bbc9-123026a078a7">
     
   * Para: http://localhost:8080/blueprints/Julia%20Mejia/PruebaPost  
     <img width="410" alt="image" src="https://github.com/juliamejia/SpringBoot_REST_API_Blueprints_Part2/assets/98657146/ec814623-ea7f-41d9-9f65-f65f57de3a65">  

4. Agregue soporte al verbo PUT para los recursos de la forma '/blueprints/{author}/{bpname}', de manera que sea posible actualizar un plano determinado.
   * En BlueprintAPIController agregamos el siguiente metodo
     ```java
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
		```
     * Por medio del siguiente comando hacemos la peticion de PUT
       ```
       curl -i -X POST -Hcontent-Type:application/json -HAccept:application/json http://localhost:8080/blueprints/Cristian%20Rodriguez/Blueprint1 -d '{"points":[{"x":78,"y":90},{"x":14,"y":65}]}'
       ```
       
       <img width="443" alt="image" src="https://github.com/juliamejia/SpringBoot_REST_API_Blueprints_Part2/assets/98657146/83e7523b-b5f3-4872-9430-53fce063c13e">  


### Parte III

El componente BlueprintsRESTAPI funcionará en un entorno concurrente. Es decir, atederá múltiples peticiones simultáneamente (con el stack de aplicaciones usado, dichas peticiones se atenderán por defecto a través múltiples de hilos). Dado lo anterior, debe hacer una revisión de su API (una vez funcione), e identificar:

* Qué condiciones de carrera se podrían presentar?
* Cuales son las respectivas regiones críticas?

Ajuste el código para suprimir las condiciones de carrera. Tengan en cuenta que simplemente sincronizar el acceso a las operaciones de persistencia/consulta DEGRADARÁ SIGNIFICATIVAMENTE el desempeño de API, por lo cual se deben buscar estrategias alternativas.  
* En InMemoryBlueprintPersistence volvimos el hashmap tipo atómico
  
  ```java
	private final ConcurrentHashMap<Tuple<String,String>,Blueprint> blueprints=new ConcurrentHashMap<>();
	```
  
Escriba su análisis y la solución aplicada en el archivo ANALISIS_CONCURRENCIA.txt  
* ANALISIS EN EL ARCHIVO INDICADO  

#### Criterios de evaluación

1. Diseño.
	* Al controlador REST implementado se le inyectan los servicios implementados en el laboratorio anterior.
	* Todos los recursos asociados a '/blueprint' están en un mismo Bean.
	* Los métodos que atienden las peticiones a recursos REST retornan un código HTTP 202 si se procesaron adecuadamente, y el respectivo código de error HTTP si el recurso solicitado NO existe, o si se generó una excepción en el proceso (dicha excepción NO DEBE SER de tipo 'Exception', sino una concreta)	
2. Funcionalidad.
	* El API REST ofrece los recursos, y soporta sus respectivos verbos, de acuerdo con lo indicado en el enunciado.
3. Análisis de concurrencia.
	* En el código, y en las respuestas del archivo de texto, se tuvo en cuenta:
		* La colección usada en InMemoryBlueprintPersistence no es Thread-safe (se debió cambiar a una con esta condición).
		* El método que agrega un nuevo plano está sujeta a una condición de carrera, pues la consulta y posterior agregación (condicionada a la anterior) no se realizan de forma atómica. Si como solución usa un bloque sincronizado, se evalúa como R. Si como solución se usaron los métodos de agregación condicional atómicos (por ejemplo putIfAbsent()) de la colección 'Thread-Safe' usada, se evalúa como B.
