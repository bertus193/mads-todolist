package models;

import play.*;
import play.mvc.*;
import play.db.jpa.*;
import javax.persistence.*;
import java.util.List;
import java.util.Date;

public class UsuarioDAO {
    public static Usuario create (Usuario usuario) {
        usuario.nulificaAtributos();
        JPA.em().persist(usuario);
        // Hacemos un flush y un refresh para asegurarnos de que se realiza
        // la creación en la BD y se devuelve el id inicializado
        JPA.em().flush();
        JPA.em().refresh(usuario);
        Logger.debug(usuario.toString());
        return usuario;
    }

    public static Usuario find(String idUsuario) {
        return JPA.em().find(Usuario.class, idUsuario);
    }

    public static Usuario update(Usuario usuario) {
        return JPA.em().merge(usuario);
    }

    public static void delete(String idUsuario) {
        Usuario usuario = JPA.em().getReference(Usuario.class, idUsuario);
        JPA.em().remove(usuario);
    }

    public static List<Usuario> findAll() {
        TypedQuery<Usuario> query = JPA.em().createQuery(
                  "select u from Usuario u ORDER BY id", Usuario.class);
        return query.getResultList();
    }

    /*public static Usuario loginUser(String user, String password){
      return JPA.em().find(Usuario.class, user);
    }*/

    public static boolean loginUser(String user, String password) {
      TypedQuery<Usuario> query = JPA.em().createQuery(
      "SELECT u FROM Usuario u WHERE u.login = ?1 AND u.password = ?2", Usuario.class);
      try{
          Usuario usuario = query.setParameter(1, user)
                                  .setParameter(2, password)
                                  .getSingleResult();
      }
      catch (NoResultException nre){
            return false;
      }
      return true;
    }


    public static int checkRegisterUser(String user, String password) {

      Usuario usuario = new Usuario(user,password);
      TypedQuery<Usuario> query = JPA.em().createQuery(
      "SELECT u FROM Usuario u WHERE u.login = ?1", Usuario.class);
      try{
          usuario = query.setParameter(1, user).getSingleResult();
      }
      catch (NoResultException nre){
            return 3;
      }

      if(usuario.password == null){
        usuario.password = password;
        UsuarioDAO.update(usuario);
        return 1;
      }
      else
        return 2;
    }
}
