package dao;

import play.db.Database;
import play.db.Databases;
import play.db.jpa.*;
import org.junit.*;
import org.dbunit.*;
import org.dbunit.dataset.*;
import org.dbunit.dataset.xml.*;
import java.io.FileInputStream;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import models.*;

public class UsuarioDaoDbUnitTest {

    static Database db;
    static JPAApi jpa;
    JndiDatabaseTester databaseTester;

    @BeforeClass
    static public void initDatabase() {
        db = Databases.inMemoryWith("jndiName", "DefaultDS");
        // Necesario para inicializar el nombre JNDI de la BD
        db.getConnection();
        // Se activa la compatibilidad MySQL en la BD H2
        db.withConnection(connection -> {
            connection.createStatement().execute("SET MODE MySQL;");
        });
        jpa = JPA.createFor("memoryPersistenceUnit");
    }

    @Before
    public void initData() throws Exception {
        databaseTester = new JndiDatabaseTester("DefaultDS");
        IDataSet initialDataSet = new FlatXmlDataSetBuilder().build(new
        FileInputStream("test/resources/usuarios_dataset.xml"));
        databaseTester.setDataSet(initialDataSet);
        databaseTester.onSetup();
    }

    @After
    public void clearData() throws Exception {
        databaseTester.onTearDown();
    }

    @AfterClass
    static public void shutdownDatabase() {
        jpa.shutdown();
        db.shutdown();
    }

    @Test
    public void findUsuarioPorLogin() {
        jpa.withTransaction(() -> {
            Usuario usuario = UsuarioDAO.findUsuarioPorLogin("juan");
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyy");
            try {
                Date diezDiciembre93 = sdf.parse("10-12-1993");
                assertTrue(usuario.login.equals("juan") &&
                           usuario.fechaNacimiento.compareTo(diezDiciembre93) == 0);
            } catch (java.text.ParseException ex) {
            }
        });
    }

    @Test
    public void actualizaUsuario() {
        jpa.withTransaction(() -> {
            Usuario usuario = UsuarioDAO.find(2);
            usuario.apellidos = "Anabel Pérez";
            UsuarioDAO.update(usuario);
        });

        jpa.withTransaction(() -> {
            Usuario usuario = UsuarioDAO.find(2);
            assertThat(usuario.apellidos, equalTo("Anabel Pérez"));
        });
    }

    @Test
    public void testCheckRegisterNoPassword() {
        jpa.withTransaction(() -> {
            int salida = UsuarioDAO.checkRegisterUser("juan", "1234");
            assertThat(salida, equalTo(1));
        });
        //Ya se ha registrado
        jpa.withTransaction(() -> {
            int salida = UsuarioDAO.checkRegisterUser("juan", "1234");
            assertThat(salida, equalTo(2));
        });
    }

    @Test
    public void testFindAllUsers() {
        jpa.withTransaction(() -> {
            List<Usuario> usuarios = UsuarioDAO.findAll();
            Usuario user = usuarios.get(0);
            assertThat(user.eMail, equalTo("juan.gutierrez@gmail.com"));
            Usuario user = usuarios.get(1);
            assertThat(user.eMail, equalTo("anabel.perez@gmail.com"));
        });
    }

    @Test
    public void testBorrarFindAll() {
        jpa.withTransaction(() -> {
            UsuarioDAO.delete(2);
            List<Usuario> usuarios = UsuarioDAO.findAll();
            assertThat(usuarios.size(), equalTo(1));

            Usuario user = new Usuario("hola", "1234");
            UsuarioDAO.create(user);
            usuarios = UsuarioDAO.findAll();
            assertThat(usuarios.size(), equalTo(2));
        });
    }

}
