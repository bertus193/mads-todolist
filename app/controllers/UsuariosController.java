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

public class UsuariosController extends Controller {

    @Inject FormFactory formFactory;

    @Transactional(readOnly = true)
    // Devuelve una página con la lista de usuarios
    public Result listaUsuarios() {
        // Obtenemos el mensaje flash guardado en la petición
        // por el controller grabaUsuario
        String mensaje = flash("grabaUsuario");
        List<Usuario> usuarios = UsuariosService.findAllUsuarios();
        return ok(listaUsuarios.render(usuarios, mensaje));
    }

    // Devuelve un formulario para crear un nuevo usuario
    public Result formularioNuevoUsuario() {
        return ok(formCreacionUsuario.render(formFactory.form(Usuario.class),""));
    }

    @Transactional
    // Añade un nuevo usuario en la BD y devuelve código HTTP
    // de redirección a la página de listado de usuarios
    public Result grabaNuevoUsuario() {

        Form<Usuario> usuarioForm = formFactory.form(Usuario.class).bindFromRequest();
        if (usuarioForm.hasErrors()) {
            return badRequest(formCreacionUsuario.render(usuarioForm, "Hay errores en el formulario"));
        }
        Usuario usuario = usuarioForm.get();
        Logger.debug("Usuario a grabar: " + usuario.toString());
        usuario = UsuariosService.grabaUsuario(usuario);
        flash("grabaUsuario", "El usuario se ha grabado correctamente");
        return redirect(controllers.routes.UsuariosController.listaUsuarios());
    }

    @Transactional
    public Result grabaUsuarioModificado() {
      Form<Usuario> usuarioForm = formFactory.form(Usuario.class).bindFromRequest();
      if (usuarioForm.hasErrors()) {
          return redirect(controllers.routes.UsuariosController.listaUsuarios());
      }
      Usuario usuario = usuarioForm.get();
      Logger.debug("Usuario a modificar: " + usuario.toString());
      usuario = UsuariosService.modificaUsuario(usuario);
      flash("modificaUsuario", "El usuario se ha modificado correctamente");
      return redirect(controllers.routes.UsuariosController.listaUsuarios());
    }

    @Transactional
    public Result detalleUsuario(String id) {
        Usuario usuario = UsuariosService.findUsuario(id);

        return ok(usuario.toString());
    }

    @Transactional
    public Result editaUsuario(String id) {
        Form<Usuario> usuarioForm = formFactory.form(Usuario.class);
        Usuario usuario = UsuariosService.findUsuario(id);
        usuarioForm = usuarioForm.fill(usuario);

        return ok(formModificacionUsuario.render(usuarioForm, "Edita usuario "+id));
    }

    @Transactional
    public Result borraUsuario(String id) {
      Logger.debug("Usuario a borrar: " + id);
        String salida;
        if(UsuariosService.deleteUsuario(id)){
          salida = "si";
        }
        else{
          salida = "no";
        }
        return ok(salida);
    }

}
