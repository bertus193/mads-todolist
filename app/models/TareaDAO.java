package models;

import play.*;
import play.mvc.*;
import play.db.jpa.*;
import javax.persistence.*;
import java.util.List;
import java.util.Date;

public class TareaDAO {
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
