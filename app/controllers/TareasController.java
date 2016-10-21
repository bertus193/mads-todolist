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
    public Result listaTareas(Integer idUsuario) {
        List<Tarea> tarea = TareasService.listaTareasUsuario(idUsuario);
        String mensaje = flash("tareas");
        List<Tarea> tareas = TareasService.listaTareasUsuario(idUsuario);
        return ok(listaTareas.render(tareas, mensaje, idUsuario));
    }

    @Transactional
    public Result borraTarea(Integer id) {
      Logger.debug("Tarea a borrar: " + id);
        String salida;
        if(TareasService.deleteTarea(id)){
          salida = "si";
        }
        else{
          salida = "no";
        }
        return ok(salida);
    }

    @Transactional
    public Result grabaTareaModificado(Integer idUsuario) {
      Form<Tarea> tareaForm = formFactory.form(Tarea.class).bindFromRequest();
      if (tareaForm.hasErrors()) {
          return redirect(controllers.routes.TareasController.listaTareas(idUsuario));
      }
      Tarea tarea = tareaForm.get();
      Logger.debug("Tarea a modificar: " + tarea.toString());
      
      tarea = TareasService.modificaTarea(tarea, idUsuario);
      flash("tarea", "La tarea se ha modificado correctamente");
      return redirect(controllers.routes.TareasController.listaTareas(idUsuario));
    }
    @Transactional
    public Result editaTarea(Integer idUsuario, Integer id) {
      Form<Tarea> tareaForm = formFactory.form(Tarea.class);
      Tarea tarea = TareasService.findTarea(id);
      tareaForm = tareaForm.fill(tarea);

      return ok(formModificacionTarea.render(tareaForm, "Edita tarea "+id,idUsuario));
    }

    // Devuelve un formulario para crear un nuevo usuario
    public Result formularioNuevoTarea(Integer idUsuario) {
        String mensaje = flash("Nueva tarea");
        return ok(formCreacionTarea.render(formFactory.form(Tarea.class), mensaje,idUsuario));
    }

    @Transactional
    // Añade un nuevo usuario en la BD y devuelve código HTTP
    // de redirección a la página de listado de usuarios
    public Result grabaNuevoTarea(Integer idUsuario) {

        Form<Tarea> tareaForm = formFactory.form(Tarea.class).bindFromRequest();
        if (tareaForm.hasErrors()) {
            return badRequest(formCreacionTarea.render(tareaForm, "Hay errores en el formulario",idUsuario));
        }

        Tarea tarea = tareaForm.get();
        Logger.debug("Usuario a grabar: " + tarea.toString());
        tarea = TareasService.grabaTarea(tarea,idUsuario);
        if(tarea == null){
          flash("Tarea", "Ya existe una Tarea con dicho nombre");
          return redirect(controllers.routes.TareasController.formularioNuevoTarea(idUsuario));
        }
        else{
          flash("Tarea", "La Tarea se ha grabado correctamente");
          return redirect(controllers.routes.TareasController.listaTareas(idUsuario));
        }
    }


}
