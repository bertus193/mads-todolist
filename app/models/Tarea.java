package models;

import java.util.Date;
import javax.persistence.*;
import play.data.validation.Constraints;
import play.data.format.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

@Entity
@Table(uniqueConstraints={@UniqueConstraint(columnNames = {"login"})})
public class Tarea {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    public Integer id;
    @Constraints.Required
    public String login;
    public String nombre;
    public String apellidos;
    public String eMail;
    public String descripcion;

    public Tarea() {}

    public Tarea(String descripcion) {
      this.descripcion = descripcion;
    }

    public String toString() {
        return String.format("Tarea id: %s descripcion: %s", id, descripcion);
    }

    public void nulificaAtributos() {
          if (descripcion != null && descripcion.isEmpty()) descripcion = null;
    }
}
