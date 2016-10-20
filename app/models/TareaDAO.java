package models;

import play.*;
import play.mvc.*;
import play.db.jpa.*;
import javax.persistence.*;
import java.util.List;
import java.util.Date;

public class TareaDAO {
    public static Tarea create (Tarea tarea) {
      try{
        tarea.nulificaAtributos();
        JPA.em().persist(tarea);
        // Hacemos un flush y un refresh para asegurarnos de que se realiza
        // la creación en la BD y se devuelve el id inicializado
        JPA.em().flush();
        JPA.em().refresh(tarea);
        Logger.debug(tarea.toString());
        return tarea;
      }
      catch(PersistenceException ex){
        return null;
      }
    }

    public static Tarea find(Integer idTarea) {
        return JPA.em().find(Tarea.class, idTarea);
    }

    public static void delete(Integer idTarea) {
        Tarea tarea = JPA.em().getReference(Tarea.class, idTarea);
        JPA.em().remove(tarea);
    }

    public static Tarea update(Tarea tarea) {
        return JPA.em().merge(tarea);
    }
}
