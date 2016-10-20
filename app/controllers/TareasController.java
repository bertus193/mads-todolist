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
      tarea = TareasService.modificaTarea(tarea);
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


}
