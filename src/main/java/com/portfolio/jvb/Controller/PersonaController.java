
package com.portfolio.jvb.Controller;

import com.portfolio.jvb.Dto.dtoPersona;
import org.apache.commons.lang3.StringUtils;
import com.portfolio.jvb.Entity.Persona;
import com.portfolio.jvb.Security.Controller.Mensaje;
import com.portfolio.jvb.Service.ImpPersonaService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/personas")
@CrossOrigin(origins = {"https://frontend-44d67.web.app/","http://localhost:4200"})
public class PersonaController {
    @Autowired
    ImpPersonaService personaService;
    
    @GetMapping("/lista")
    public ResponseEntity<List<Persona>>list(){
        List<Persona> list = personaService.list();
        return new ResponseEntity(list, HttpStatus.OK);
    }
    @GetMapping("/detail/{id}")
    public ResponseEntity<Persona>getById(@PathVariable("id")int id){
        if(!personaService.existsById(id))
            return new ResponseEntity(new Mensaje("no existe"),HttpStatus.NOT_FOUND);
        Persona persona = personaService.getOne(id).get();
        return new ResponseEntity(persona,HttpStatus.OK);
    }
    
    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody dtoPersona dtopersona){
        if(StringUtils.isBlank(dtopersona.getNombre()))
            return new ResponseEntity(new Mensaje("El nombre es obligatorio"),HttpStatus.BAD_REQUEST);
        if(personaService.existsByNombre(dtopersona.getNombre()))
            return new ResponseEntity(new Mensaje("esa skill existe"),HttpStatus.BAD_REQUEST);
        
        Persona persona = new Persona(dtopersona.getNombre(),dtopersona.getApellido(),dtopersona.getImg(),dtopersona.getDescripcion());
        personaService.save(persona);
        return new ResponseEntity(new Mensaje("Skill agregada"),HttpStatus.OK);
    }
    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable("id")int id,@RequestBody dtoPersona dtopersona){
        if(!personaService.existsById(id))
            return new ResponseEntity(new Mensaje("El ID no existe"),HttpStatus.BAD_REQUEST);
        if(personaService.existsByNombre(dtopersona.getNombre())&& personaService.getByNombre(dtopersona.getNombre()).get().getId()!=id)
            return new ResponseEntity(new Mensaje("Esa skill ya existe"),HttpStatus.BAD_REQUEST);
        if(StringUtils.isBlank(dtopersona.getNombre()))
            return new ResponseEntity(new Mensaje("El nombre es obligatorio"),HttpStatus.BAD_REQUEST);
        
        Persona persona=personaService.getOne(id).get();
        persona.setNombre(dtopersona.getNombre());
        persona.setApellido(dtopersona.getApellido());
        persona.setDescripcion(dtopersona.getDescripcion());
        persona.setImg(dtopersona.getImg());
        personaService.save(persona);
        return new ResponseEntity(new Mensaje("persona actualizada"),HttpStatus.OK);
    }
   /* @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable("id")int id){
        if(!sHys.existsById(id))
            return new ResponseEntity(new Mensaje("El ID no existe"),HttpStatus.BAD_REQUEST);
        
        sHys.delete(id);
        
        return new ResponseEntity(new Mensaje("Skill eliminada"),HttpStatus.OK);
    }*/ 
}
