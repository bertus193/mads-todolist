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
        String mensaje = flash("usuario");
        List<Usuario> usuarios = UsuariosService.findAllUsuarios();
        return ok(listaUsuarios.render(usuarios, mensaje));
    }

    // Devuelve un formulario para crear un nuevo usuario
    public Result formularioNuevoUsuario() {
        String mensaje = flash("usuario");
        return ok(formCreacionUsuario.render(formFactory.form(Usuario.class), mensaje));
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
        if(usuario == null){
          flash("usuario", "Ya existe un usuario con dicho nombre");
          return redirect(controllers.routes.UsuariosController.formularioNuevoUsuario());
        }
        else{
          flash("usuario", "El usuario se ha grabado correctamente");
          return redirect(controllers.routes.UsuariosController.listaUsuarios());
        }
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
      flash("usuario", "El usuario se ha modificado correctamente");
      return redirect(controllers.routes.UsuariosController.listaUsuarios());
    }

    @Transactional
    public Result detalleUsuario(Integer id) {
        Usuario usuario = UsuariosService.findUsuario(id);

        return ok(usuario.toString());
    }

    @Transactional
    public Result editaUsuario(Integer id) {
        Form<Usuario> usuarioForm = formFactory.form(Usuario.class);
        Usuario usuario = UsuariosService.findUsuario(id);
        usuarioForm = usuarioForm.fill(usuario);

        return ok(formModificacionUsuario.render(usuarioForm, "Edita usuario "+id));
    }

    @Transactional
    public Result borraUsuario(Integer id) {
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

    @Transactional
    public Result login() {
      String mensaje = flash("usuario");
      Form<Usuario> usuarioForm = formFactory.form(Usuario.class);
      return ok(formLogin.render(formFactory.form(Usuario.class), mensaje));
    }

    @Transactional
    public Result checkLogin() {
      Form<Usuario> usuarioForm = formFactory.form(Usuario.class).bindFromRequest();
      if (usuarioForm.hasErrors()) {
          return redirect(controllers.routes.UsuariosController.login());
      }
      Usuario usuario = usuarioForm.get();

      if(UsuariosService.loginUsuario(usuario.login, usuario.password)){
        flash("usuario", "Has iniciado sesion correctamente "+usuario.login+". ");
        return redirect(controllers.routes.UsuariosController.listaUsuarios());
      }
      else{
        flash("usuario", "Los datos no se corresponden o puedes no estar registrado");
        return redirect(controllers.routes.UsuariosController.login());
      }

    }

    @Transactional
    public Result registro(){
      Form<Usuario> usuarioForm = formFactory.form(Usuario.class);
      String mensaje = flash("usuario");

      return ok(formRegistro.render(formFactory.form(Usuario.class),mensaje));
    }

    @Transactional
    public Result checkRegistro(){
      Form<Usuario> usuarioForm = formFactory.form(Usuario.class).bindFromRequest();
      if (usuarioForm.hasErrors()) {
          return redirect(controllers.routes.UsuariosController.registro());
      }
      Usuario usuario = usuarioForm.get();

      int registro = UsuariosService.registroUsuario(usuario.login, usuario.password, usuario.password2);

      if(registro == 1){
        flash("usuario", "Te has registrado correctamente "+usuario.login+". ");
        return redirect(controllers.routes.UsuariosController.listaUsuarios());
      }
      else if(registro == 2){
        flash("usuario", "Dicho usuario ya esta registrado");
        return redirect(controllers.routes.UsuariosController.registro());
      }
      else if(registro == 3){
        flash("usuario", "No existe un usuario con dicho nombre");
        return redirect(controllers.routes.UsuariosController.registro());
      }
      else{
        flash("usuario", "Las contraseñas no coinciden");
        return redirect(controllers.routes.UsuariosController.registro());
      }
    }

}
