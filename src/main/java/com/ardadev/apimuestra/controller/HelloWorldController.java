package com.ardadev.apimuestra.controller;

import com.ardadev.apimuestra.dto.User;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController // Para Qque Spring reconozca los Controllers necesitamos las anotaciones @Controller y @RestController, @Controler nos devuelve una request completa, encambio @RestController se enfoca en el body de la cabecera generalmente porque utilizamos JSON y XML para una API
@RequestMapping("/hello") // La ruta en la cual interactuara el Controller
public class HelloWorldController {

    //@Autowired
    //private UserRepository userRepository; // Aqui establecemos una inyeccion de dependencias

    @GetMapping // Configuramos que el metodo actuara como un método GET
    public String greettings() {
        return "Hello World Daniel!";
    }
    /*
    @PostMapping // Configuramos que el metodo actuara como un método POST, podriamos usar @Transactional pero como usamos repository ya no es necesario
    public void postExample(@RequestBody String json) { // Para recibir un datos de json configuramos la notacion @RequestBody
        System.out.println("Estan enviando datos y los estamos recibiendo.");
        System.out.println(json);
    }*/

    // Como apunte solo puede haber un metodo post o get por controlador con las mismas caracteristicas*
    // Aqui enviamos un objeto como parametro en vez de un texto plano, para que deserializar el json a un objeto
    // Para el ejemplo enviamos el siguiente JSON {"nombre":"Daniel","email":"danqk@outlook.com"} por Postman como JSON
    @PostMapping
    public void postDTOExample(@RequestBody @Valid User user) { // RequestBody hace referencia a que el tipo de datos que recibira sera parte del cuerpo del envio, y Valid es el uso de Validation para activar las notaciones de validacion en el objeto
        System.out.println(user);
        System.out.println("NOMBRE: " + user.nombre());
        System.out.println("EMAIL: " + user.email());
        //userRepository.save(new UserEntity(user)); Hacemos uso de la interfaz Repository de UserEntity para guardar la informacion en la base de datos
    }

    /* Para actualizar datos de una base de datos identificar por ID cual objeto eliminaremos para eso los records deberian tambien capturar el ID de la entidad
    luego visivilizar el objeto por ID para luego estar seguros de cual objeto eliminaremos, despues pasamos el JSON con todas las modificaciones necesarias
    @PutMapping // Aparte de Put existe Patch, la diferencia Put actializa todos loa atributos de la entidad, Patch hace cambios parciales
    @Transactional // Con transactional podemos hacer commit a todas las referencias de las entidades con jpa automáticamente
    En caso ocurriese un error despues de ejecutar una modificacion queremos modificar otra seguida de esa, hara un rollback y no se ejecutara commit si es que hacemos otro tipo de modificaciones que jmpliquen cambiar la entidad
    public void actualizarEntidad(@RequestBody @Valid RecordEntidad recordEntidad) {
        Entidad entidad = userRepositoy.getReferenceById(recordEntidad.getId());
        entidad.actualizarDatosConUnRecord(recordEntidad);
    }
     */

    /* Esto hace un borrado total
    @DeleteMapping("/{id}") // Indicamos que en la url localhost:8080/hello/{id} se ejecutara el borrado id es una variable que sera utilizado en el metodo
    @Transactional // Transactional para efectaur cambios en la base de datos
    public void borrarEntidad(@PathVariable Long id) { // Indicamos que la variable viene del path, la url
        Entidad entidad = userRepositoy.getReferenceById(id);
        userRepository.delete(entidad);
    }
     */

    /* Esto hace un borrado lógico con ayuda de un listado filtrado
    @DeleteMapping("/{id}") // Indicamos que en la url localhost:8080/hello/{id} se ejecutara el borrado id es una variable que sera utilizado en el metodo
    @Transactional // Transactional para efectaur cambios en la base de datos
    public void borrarEntidad(@PathVariable Long id) { // Indicamos que la variable viene del path, la url
        Entidad entidad = userRepositoy.getReferenceById(id);
        entidad.desactivarAtributo(); // Es un metodo de la entidad, que modifica a false un atributo Booleano, que en la base de datos figura como tinyint, esto sirve como una flag, una bandera identificadora
        // Al commitear con transactional, hace el efecto de actualizar la entidad
    }
     */
        /* Para terminar el borrado lógico nos ayudamos de un listado con filtro personalizado
    @GetMapping
    public Page<RecordListUser> listadoConFiltro(@PageableDefault(size=3) Pageable paginacion) {
        //return userRepository.findAll(paginacion).map(recordListaUser::new);
        return userRepository.findByActivoTrue(paginacion).map(recordListaUser::new); // findByActivoTrue es una nomenclatura que utiliza Spring para automatizar la personalizacion de metodos de busqueda de las bases de datos, lo hace automaticamente
    }
     */
    /*
    La interface quedaria asi:
    public interface UserRepository extends JpaRepository<User,Long> {
        Page<User> findByActivoTrue(Pageable paginacion); // El filtro se automatiza no hay que programar nada, siempre y cuando tengamos el atributo Activo, y que sea booleano True/False
    }
     */
}
