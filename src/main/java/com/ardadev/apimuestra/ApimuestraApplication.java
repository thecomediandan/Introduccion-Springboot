package com.ardadev.apimuestra;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 1. Descargar y configurar tu proyecto en https://start.spring.io/
 * 2. Tenemos Spring 3 y Java 17 con Maven
 * 3. Tenemos TomCat por defecto y corremos un Jar para desplegar internamente
 * 4. SpringDevTools para un efecto hotreload
 * Para activarlo nos vamos a File>Settings>Build, Execution, Deployment>Compiler y habilitar Build project automatically
 * Y tambien activar File>Settings>Advanced Settings habilitar Allow auto-make to start...
 * 5. Lombok para la simplificación de algunos códigos repetitivos
 * 6. SpringWeb para exponer nuestro metodos de REST
 * -----Persistencia-----
 * Para instalar dependencias y no tener problemas con Spring, podemos encontrarlas dependencias en Spring initlzr
 * 7. Spring Data JPA, ORM para Spring, por defecto necesita de la configuracion de la conexión a una base de datos en la carpeta resources el archivo application.properties debera ser editado para una conexión a una base de datos
 * 8. MySQL Driver, Controlador para MySQL
 * 9. Flyway Migration, para migraciones de bases de datos
 * Probablemente necesitemos hacer una configuracion en application.properties para Flyway
 * spring.flyway.baselineOnMigrate=true
 * en application.yml si lo hay
 * spring:
 *   flyway:
 *     baselineOnMigrate: true
 *
 * Esto porque probablemente usemos una base de datos no vacia y para tener un historial de migraciones necesitaremos crearla cuando se detecte que no hay ningun historial en la base de datos
 *
 * 10. Supongamos que tenemos nuestras entidades mapeadas, el uso de Lombok en esto seria para lo siguiente
 * en las anotaciones de una entidad tendremos:
 * @Getter Para generar automaticamente los getters de la clase o entidad
 * @Setter Para generar automaticamente los setters de la clase o entidad
 * @NoArgsConstructor Para generar automaticamente un constructor sin argumentos en la clase o entidad
 * @AllArgsConstructor Para generar automaticamente un constructor con todos los atributos en los argumentos en la clase o entidad
 * @EqualsAndHashCode(of = "id") Para generar automaticamente el metodo EqualsAndHash y un criterio de compracion, generalmente es el campo que hace referencia a la clave primaria de la base de datos
 * ... por ejemplo
 *
 * 11. En Spring nose utiliza el patron DAO (Data Access Object), porque es muy antiguo y extenso en su lugar utilizaremos interfaces repository
 * DAO contenia metodos que interactua con la base de datos para encontrar u operar con los datos orientada a objetos
 * INTERFACES REPOSITORY utiliza un patron de interfaces que implementan una interface llamada JpaRepository<NombreDeEntidad, TipoDeDatoDeClave> el cual ya implementa acciones parecidas a lo que haciamos con DAO
 * Lo utilizamos en un Controller, creando una variable del tipo de la interfaz creada, podemos utilizar una notacion par que nos instancie un objeto de ese tipo porque se trata de una interfaz, @Autowired no es recomendable para produccion
 * generelamente en un método Post del controller es donde deseamos persistir la información, es en este metodo donde podemos hacer uso de este repository usando por ejemplo el metodo ´save´ de la interface para persistir un objeto del tipo de la entidad
 * tambien en el metodo save podemos enviar un objeto DTO que haga la referencia a la entidad, creando un constructor en la entidad para que reciba el DTO y nos cree una entidad con esos datos y asi persistirlos con el metodo save de la interface
 * 12. Utilizaremos Flyway para crear o migrar una base de datos, flyway-core se encarga de las migraciones y flyway-mysql es el dialecto utilizado
 * Flyway sirve para gestionar tu base de datos atraves de código.
 * Para la configuracion de los archivos de migración necitamos crear los archivos en la ruta resources/db/migration que es la ruta que reconoce Flyway
 * tambien los archivos tienen una nomenclatura para reconocer y guardar los archivos en un base de datos propia de flyway
 * esta nomenclatura es parecida a V1__nombre-de-migracion.sql, son basicamente creaciones modificaciones a la base de datos
 * con lenguaje SQL, al levantar el servidor estas se ejecutan automaticamente solo cuando no se hayan ejecutado anteriormente.
 * 13. Recordar que las rutas resources/static se almacenan archivos .js o .css y resources/templates guardamos archivos .html todos esto para aplicaciones web.
 * 14. Agregamos la dependencia Validation a spring (spring-boot-starter-validation), para validar las entradas de los datos enviados al metodo POST
 * por ejemplo tenemos configurados en el JPA de las entidades los campos de las bases de datos como not null, unique son constraints reglas en la base de datos
 * que si rompen nos muestran errores a nivel de bases de datos, pero queremos que estas validaciones sean enviadas a nivel de objetos, por codigo.
 * para esto usaremos anotaciones en los DTO exactamente en los parámetros de entrada anotaciones como
 * @NotNull, @NotBlanck, @Email, @Pattern(regexp = "\\d{4,6}") por ejemplo, para activar estas notaciones las referencias en parametros de funciones por ejemplo en el post, deberan utilizar la notacion @Valid
 * 15. Si queremos listar los datos de la base de datos podemos usar el metodo findAll de nuestros reporsitorios creados (interfaces)
 * Si queremos mostrar algunos datos de la entidad solo para la lista podemos usar un record para crear una clase en tiempo de compilacion hacienodo referencia a los campos de la entidad deseada para esto
 * haremos los siguiente en el record tendremos un constructor que reciba la entidad e ir clasificando los campos necesarrios y setearlos en el record luego  uitilizaremos el findAll de la siguiente manera
 * entidadRepository.findAll().stream().map(entidadRecord::new).toList(); lo que nos devolvera una List<entidadRecord> todos esto en un metodo con la notacion GetMapping.
 * 16. Si queremos una lista paginada, por ejemplo el numero 15.
 * lo reescribiriamos asi:
 * sera una metodo con @GetMapping
 * en vez de devolver List<entidadRecord> devolvera Page<entidadRecord>
 * tendra un parametro de entrada del tipo 'Pageable' lo llamaremos paginacion
 * el retorno ya no sera entidadRepository.findAll().stream().map(entidadRecord::new).toList(); si nos
 * entidadRepository.findAll(paginacion).map(entidadRecord::new);
 * Para elegir la cantidad mostrada de datos y las paginas utilizaremos query params en la url de nuestra API
 * es la url seguido de ?size=10&page=0, esto indicaria que queremos que nos muestre el paginado de 10 en 10, y que estos nos muestre la primera página
 * tambien existe el query params sort=atributoDeEntidad, para ordenar dependiendo del nombre del campo que queremos el orden del listado, el nombre debe ser el mismo que se puso en la clase entidad no de la base de datos ni el DTO
 * otra forma progrmática es usando la notacion @PageableDefault(size=10,page=0,sort=atributoDeEntidad) en el parametro de la funcion donde definimos la entrada del tipo de dato Pageable en el metodo que tiene la notacion GetMapping
 * 17. Para mostrar informacion por consola de las sentencias SQL que spring ejecuta debemos escribir la siguiente linea
 * spring.jpa.show-sql=true y spring.jpa.properties.hibernate.format_sql=true en el archivo application.properties ubicado en resources.
 */

@SpringBootApplication
public class ApimuestraApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApimuestraApplication.class, args);
	}

}
