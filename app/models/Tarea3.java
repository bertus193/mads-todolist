package models;

import java.util.Date;
import javax.persistence.*;
import play.data.validation.Constraints;
import play.data.format.*;

@Entity
public class Tarea3 {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    public Integer id;
    @Constraints.Required
    public String descripcion;
    @ManyToOne
    @JoinColumn(name="usuarioId")
    public Usuario usuario;

    // Un constructor vacío necesario para JPA
    public Tarea3() {}

    // El constructor principal con los campos obligatorios
    public Tarea3(String descripcion) {
        this.descripcion = descripcion;
    }

}
