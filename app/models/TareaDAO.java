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
        return tarea;
      }
      catch(PersistenceException ex){
        return null;
      }
    }

    public static Tarea find(Integer id) {
        return JPA.em().find(Tarea.class, id);
    }
}
