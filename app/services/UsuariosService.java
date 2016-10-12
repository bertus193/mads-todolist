package services;

import play.*;
import play.mvc.*;
import play.db.jpa.*;
import java.util.List;
import java.util.Date;
import java.util.ArrayList;

import models.*;

public class UsuariosService {
    public static Usuario grabaUsuario(Usuario usuario) {
        return UsuarioDAO.create(usuario);
    }

    public static Usuario modificaUsuario(Usuario usuario) {
      return UsuarioDAO.update(usuario);
    }

    public static Usuario findUsuario(Integer id) {
        return  UsuarioDAO.find(id);
    }

    public static boolean deleteUsuario(Integer id) {
        UsuarioDAO.delete(id);
        Usuario user = UsuariosService.findUsuario(id);
        if(user != null){
          return true;
        }
        else{
          return false;
        }
    }

    public static List<Usuario> findAllUsuarios() {
        List<Usuario> lista = UsuarioDAO.findAll();
        Logger.debug("Numero de usuarios: " + lista.size());
        return lista;
    }

    public static boolean loginUsuario(String login, String password){
      Boolean usuario = UsuarioDAO.loginUser(login, password);
      if(usuario == true){
        return true;
      }
      else{
        return false;
      }

    }

    public static int registroUsuario(String login, String password, String password2){
      if(!password.equals(password2)){
        return 4;
      }
      int resultado = UsuarioDAO.checkRegisterUser(login, password);
      if(resultado != 3 && resultado != 2){
        return 1;
      }
      return resultado;
    }
}
