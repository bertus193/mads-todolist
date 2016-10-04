# Documentación Práctica 1
======

##Documentación Desarrollador Registro/Inicio de sesión

Login:
  1. Formulario: Añadido formulario con login
  2. UsuariosController: Añadido métodos login() y checkLogin()
  3. UsuariosService: Añadido loginUsuario(String, String):Boolean
  4. UsuarioDAO: Añadido loginUser(String,String):Boolean
  5. routes: Añadido /login y /loginCheck

UsuarioDAO hará una comprobación en la base de datos revisando ambos parámetros, en caso de que sea cierto devolverá true o en caso contrario al no encontrar el usuario en la base de datos se controlará la excepción que ésta crea devolviendo false.

Dicho Booleano pasará a UsuarioService que así mismo volverá a UsuariosController.

Una vez en UsuariosController en caso de true, pasará a una pagina nueva indicando que se ha iniciado sesión correctamente y el nombre del usuario. En caso contrario, se volverá a pedir el login y se mandará un mensaje de error indicando que no se ha iniciado sesión correctamente.

Registro:
  1. Formulario: Añadido formulario con registro
  2. UsuariosController: Añadido métodos registro() y checkRegistro()
  3. UsuariosService: Añadido registroUsuario(String, String, String):Int
  4. UsuarioDAO: Añadido checkRegisterUser(String, String, String):Int
  5. routes: Añadido /registro y /checkRegistro
  6. Usuario: Añadido campo password2 para comprobación de contraseña.


  registroUsuario(String, String, String):Int códigos:
    1. Se has registrado correctamente
    2. Dicho usuario ya está registrado
    3. No existe un usuario con dicho nombre
    4. Las contraseñas no coinciden

  Es necesario repetir la contraseña, por tanto, se añadirá un campo a Usuario puesto que el formulario utiliza este modelo.

  Una vez es enviado el formulario pasa a registroUsuario. Éste, si las contraseñas no coinciden devolverá el mensaje de error 4: Las contraseñas no coinciden. En caso contrario, llamará a checkRegisterUser.

  checkRegisterUser comprobará si dicho login existe, en caso de ser falso devolverá 3. En cambio, si el login existe y su contraseña de dicho usuario es null devolverá 1. Si ésta no es null devolverá 2.

  Dependiendo del mensaje pasará a listaUsuarios (código 1) o volverá a la pantalla de registro en cualquiera de los otros 3 casos.

  
