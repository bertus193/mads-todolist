package controllers;

import java.util.List;
import javax.inject.*;

import play.*;
import play.mvc.*;
import views.html.*;
import static play.libs.Json.*;
import play.data.Form;
import play.data.FormFactory;
import play.db.jpa.*;
import java.util.HashMap;
import java.util.Map;

import services.*;
import models.*;

public class TareasController extends Controller {

    @Inject FormFactory formFactory;

    @Transactional
    public Result listaTareas(Integer id) {
        List<Tarea> tarea = TareasService.listaTareasUsuario(id);
        /*String salida = "";
        for(Tarea index : tarea){
          salida += index.toString();
          salida += " ";
        }

        return ok(salida);*/
        String mensaje = flash("tareas");
        List<Tarea> tareas = TareasService.listaTareasUsuario(id);
        return ok(listaTareas.render(tareas, mensaje));
    }



}
