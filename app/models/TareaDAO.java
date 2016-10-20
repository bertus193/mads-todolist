package models;

import play.*;
import play.mvc.*;
import play.db.jpa.*;
import javax.persistence.*;

public class TareaDAO {
    public static Tarea find(Integer idTarea) {
        return JPA.em().find(Tarea.class, idTarea);
    }

    public static void delete(Integer idTarea) {
        Tarea tarea = JPA.em().getReference(Tarea.class, idTarea);
        JPA.em().remove(tarea);
    }
}
