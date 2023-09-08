
package com.portfolio.jvb.Controller;

import com.portfolio.jvb.Dto.dtoProyecto;
import org.apache.commons.lang3.StringUtils;
import com.portfolio.jvb.Entity.Proyecto;
import com.portfolio.jvb.Security.Controller.Mensaje;
import com.portfolio.jvb.Service.SProyecto;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/proyecto")
@CrossOrigin(origins = {"https://frontend-44d67.web.app/","http://localhost:4200"})
public class CProyecto {
    @Autowired
    SProyecto sProyecto;
    @GetMapping("/lista")
    public ResponseEntity<List<Proyecto>>list(){
        List<Proyecto> list =sProyecto.list();
        return new ResponseEntity(list, HttpStatus.OK);
    }
    @GetMapping("/detail/{id}")
    public ResponseEntity<Proyecto>getByID(@PathVariable("id")int id){
    if(!sProyecto.existsById(id)){
    return new ResponseEntity(new Mensaje("no existe el id"),HttpStatus.BAD_REQUEST);}
    Proyecto proyecto = sProyecto.getOne(id).get();
    return new ResponseEntity(proyecto,HttpStatus.OK);
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable("id")int id){
        if(!sProyecto.existsById(id)){
            return new ResponseEntity(new Mensaje("No existe el Id"),HttpStatus.NOT_FOUND);
        }
            sProyecto.delete(id);
            return new ResponseEntity(new Mensaje("Educacion eliminada"),HttpStatus.OK);
    }
    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody dtoProyecto dtoproyecto){
        if(StringUtils.isBlank(dtoproyecto.getNombre())){
            return new ResponseEntity(new Mensaje("el nombre es obligatorio"),HttpStatus.BAD_REQUEST);
        }
        if(sProyecto.existsByNombre(dtoproyecto.getNombre())){
            return new ResponseEntity(new Mensaje("Ese nombre ya existe"),HttpStatus.BAD_REQUEST);
        }
        Proyecto proyecto = new Proyecto(dtoproyecto.getNombre(), dtoproyecto.getDescripcion());
        sProyecto.save(proyecto);
        return new ResponseEntity(new Mensaje("Educacion creada"),HttpStatus.OK);
    }
    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable("id")int id, @RequestBody dtoProyecto dtoproyecto){
        if(!sProyecto.existsById(id)){
            return new ResponseEntity(new Mensaje("no existe el id"),HttpStatus.NOT_FOUND);
        }
        if(sProyecto.existsByNombre(dtoproyecto.getNombre())&& sProyecto.getByNombre(dtoproyecto.getNombre()).get().getId()!=id){
            return new ResponseEntity(new Mensaje("ese nombre ya existe"),HttpStatus.BAD_REQUEST);
        }
        if(StringUtils.isBlank(dtoproyecto.getNombre())){
            return new ResponseEntity(new Mensaje("el campo no puede estar vacio"),HttpStatus.BAD_REQUEST);
        }
        Proyecto proyecto = sProyecto.getOne(id).get();
        
        proyecto.setNombre(dtoproyecto.getNombre());
        proyecto.setDescripcion(dtoproyecto.getDescripcion());
        sProyecto.save(proyecto);
        return new ResponseEntity(new Mensaje("Educacion actualizada"),HttpStatus.OK);
    }
}

