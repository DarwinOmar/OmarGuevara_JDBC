package dao;

import entidades.Persona;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class daoPersona extends dao.DAOPostgres {

    /**
     * Esta es la forma tradicional usando Statement, para hacer un registro en
     * una base de dato
     *
     * @param p
     * @throws Exception
     */
    public void registrarPersonaStatement(Persona p) throws Exception {
        Connection conexion = null;
//        String cadena = "INSERT INTO persona(nombres, dni, direccion, edad, estado)\n"
//                + "    VALUES ('DARWIN OMAR GUEVARA DIAZ', '12345678', 'AV. PERU', 25, TRUE);";
        String cadena = "INSERT INTO persona(nombres, dni, direccion, edad, estado)"
                + " VALUES ('" + p.getNombres() + "' , '" + p.getDni() + "', '" + p.getDireccion() + "', " + p.getEdad() + ", " + p.isEstado() + ");";

        try {
            Class.forName("org.postgresql.Driver");
            conexion = DriverManager.getConnection("jdbc:postgresql://localhost/jdbc", "postgres", "omarguevara");
            Statement st = conexion.createStatement();
            /*
            executeUpdate para instrucciones update delete insert o instruccions DDL
            executeQuery para instrucciones tipo select que se almancenan en un resulset 
             */
            st.executeUpdate(cadena);
            System.out.println("Registro correcto con statement");
        } catch (ClassNotFoundException | SQLException e) {
            throw e;
        } finally {
            if (conexion != null) {
                conexion.close();
            }
        }
    }

    /**
     * Este listar es utilizando Statement
     *
     * @throws Exception
     */
    public void listarPersonasStatement() throws Exception {
        Connection conexion = null;
        String cadena = "SELECT * from persona";
        ResultSet rs;
        try {
            Class.forName("org.postgresql.Driver");
            conexion = DriverManager.getConnection("jdbc:postgresql://localhost/jdbc", "postgres", "omarguevara");
            Statement st = conexion.createStatement();
            rs = st.executeQuery(cadena);
            System.out.println("LISTAR UTILIZANDO STATEMENT");

            while (rs.next()) {
                System.out.println("--------------DASTOS-------------------");
                System.out.println("---------------------------------------");
                System.out.println("Codigo: " + rs.getString(1));
                System.out.println("Nombre: " + rs.getString(2));
                System.out.println("Dni: " + rs.getString(3));
                System.out.println("Direccion: " + rs.getString(4));
                System.out.println("Edad: " + rs.getInt(5));
                System.out.println("Estado: " + rs.getString(6));
                System.out.println("--------------------------------------- \n");

            }
        } catch (ClassNotFoundException | SQLException e) {
            throw e;
        } finally {
            if (conexion != null) {
                conexion.close();
            }
        }
    }

    /**
     * Este forma de registrar es usado PreparedStatement que es mas eficiente y
     * evita estar concatenando las cadenas
     *
     * @param p
     * @throws Exception
     */
    public void registrarPersonaPreparedStatement(Persona p) throws Exception {
        Connection conexion = null;
        PreparedStatement pst = null;
        String cadena = "INSERT INTO persona(nombres, dni, direccion, edad, estado) VALUES (?, ?, ?, ?, ?)";
//        ResultSet rs;
        try {
            Class.forName("org.postgresql.Driver");
            conexion = DriverManager.getConnection("jdbc:postgresql://localhost/jdbc", "postgres", "omarguevara");
            pst = conexion.prepareStatement(cadena);
            pst.setString(1, p.getNombres());
            pst.setString(2, p.getDni());
            pst.setString(3, p.getDireccion());
            pst.setInt(4, p.getEdad());
            pst.setBoolean(5, p.isEstado());
            pst.executeUpdate();
            System.out.println("Registro Correcto con PreparedStatement");
        } catch (ClassNotFoundException | SQLException e) {
            throw e;
        } finally {
            if (conexion != null) {
                conexion.close();
            }
            if (pst != null) {
                pst.close();
            }
        }
    }

    /**
     * Este listar es usando PreparedStatement
     *
     * @throws Exception
     */
    public void listarPersonasPreparedStatement() throws Exception {
        Connection conexion = null;
        String cadena = "SELECT * from persona where id_persona >=?";
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            Class.forName("org.postgresql.Driver");
            conexion = DriverManager.getConnection("jdbc:postgresql://localhost/jdbc", "postgres", "omarguevara");
            pst = conexion.prepareStatement(cadena);
            pst.setInt(1, 4); // en el caso que necesite parametros
//            Statement st = conexion.createStatement();
            rs = pst.executeQuery();
            while (rs.next()) {
                System.out.println("--------------DASTOS-------------------");
                System.out.println("---------------------------------------");
                System.out.println("Codigo      : " + rs.getString(1));
                System.out.println("Nombre      : " + rs.getString(2));
                System.out.println("Dni         : " + rs.getString(3));
                System.out.println("Direccion   : " + rs.getString(4));
                System.out.println("Edad        : " + rs.getInt(5));
                System.out.println("Estado      : " + rs.getBoolean(6));
                System.out.println("--------------------------------------- \n");
            }
        } catch (ClassNotFoundException | SQLException e) {
            throw e;
        } finally {
            if (conexion != null) {
                conexion.close();
            }
            if (pst != null) {
                pst.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
    }

    /**
     * Ete registra es utilizando CallableStatement para acceder al
     * procedimientos almacenados de la bd
     *
     * @param p
     * @throws Exception
     */
    public void registrarPersonaCallableStatement(Persona p) throws Exception {
        Connection conexion = null;
        CallableStatement cs = null;

        try {
            Class.forName("org.postgresql.Driver");
            conexion = DriverManager.getConnection("jdbc:postgresql://localhost/jdbc", "postgres", "omarguevara");
            cs = conexion.prepareCall("{ call fu_reg_persona(?, ?, ?, ?, ?) }");
            cs.setString(1, p.getNombres());
            cs.setString(2, p.getDni());
            cs.setString(3, p.getDireccion());
            cs.setInt(4, p.getEdad());
            cs.setBoolean(5, p.isEstado());
            cs.execute();
            System.out.println("Registro Correcto con CallableStatement");
        } catch (ClassNotFoundException | SQLException e) {
            throw e;
        } finally {
            if (conexion != null) {
                conexion.close();
            }
            if (cs != null) {
                cs.close();
            }
        }
    }

    /**
     * Este listar es utilizando CallableStatement
     *
     * @throws Exception
     */
    public void listarPersonasCallableStatement() throws Exception {
        Connection conexion = null;
        CallableStatement cs = null;
        ResultSet rs = null;
        int p = 12;
        try {
            Class.forName("org.postgresql.Driver");
            conexion = DriverManager.getConnection("jdbc:postgresql://localhost/jdbc", "postgres", "omarguevara");
            conexion.setAutoCommit(false);
            cs = conexion.prepareCall("{ ? = call fu_lista_personas(?)} ");
            cs.registerOutParameter(1, Types.OTHER); // se registra el parametro de salida
            cs.setObject(2, (Object) p);
            cs.execute();
            rs = (ResultSet) cs.getObject(1); // me retorna la lista o cursor de la bd y se pone el parametro uno por que es el primer parametro 

            while (rs.next()) {
                System.out.println("--------------DASTOS-------------------");
                System.out.println("---------------------------------------");
                System.out.println("Codigo      : " + rs.getInt(1));
                System.out.println("Nombre      : " + rs.getString(2));
                System.out.println("Dni         : " + rs.getString(3));
                System.out.println("Direccion   : " + rs.getString(4));
                System.out.println("Edad        : " + rs.getInt(5));
                System.out.println("Estado      : " + rs.getBoolean(6));
                System.out.println("--------------------------------------- \n");
            }
            conexion.commit();
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println(e.getMessage() + e.getLocalizedMessage());
            conexion.rollback();
            throw e;
        } finally {
            if (conexion != null) {
                conexion.close();
            }
            if (cs != null) {
                cs.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
    }

    
    /**
     * Este metodo utilizo todo lo que hemos visto pero de manera mejorada,
     * utilizando las clasea parametro y daoPostgres, donde simplifico y mejoro
     * todo mi codigo
     *
     * @param persona
     * @throws Exception
     */
    public void registarPersonaFinal(Persona persona) throws Exception {
        List<Parametro> pars = new ArrayList<>();
        try {
            pars.add(new Parametro("", persona.getNombres()));
            pars.add(new Parametro("", persona.getDni()));
            pars.add(new Parametro("", persona.getDireccion()));
            pars.add(new Parametro("", persona.getEdad()));
            pars.add(new Parametro("", persona.isEstado()));
            this.Conectar(true);
            this.EjecutarProcedimiento("{ call fu_reg_persona(?, ?, ?, ?, ?) }", pars);
            this.Cerrar(true);
            System.out.println("Persona registrada utilizando las clases parametro y daoPostgres");
        } catch (Exception e) {
            this.Cerrar(false);
            throw e;
        } finally {
            pars.clear();
        }
    }

    /**
     * En este metodo utilizo en mis proyectos, donde todo mi codigo esta
     * optimizado, puedo hacer que me retorne una List, o lo puedo imprimir con
     * el los ejemplos anteriores
     *
     * @param estado
     * @return
     * @throws Exception
     */
    public List<Persona> listaPersonasFinal() throws Exception {
        List<Persona> listaCategorias;
        List<Parametro> pars = new ArrayList();
        Persona c;
        pars.add(new Parametro("", false, Types.OTHER));
        pars.add(new Parametro("", 3));
        ResultSet rs = null;
        try {
            this.Conectar(true);
            rs = this.EjecutarProcedimientoDatos("{ ? = call fu_lista_personas(?) }", pars); // lista las personas que tiene el codigo >= al parametro que se le ingrese
            listaCategorias = new ArrayList();
            while (rs.next() == true) {
                c = new Persona();
                c.setCodigo(rs.getInt(1));
                c.setNombres(rs.getString(2));
                c.setDni(rs.getString(3));
                c.setDireccion(rs.getString(4));
                c.setEdad(rs.getInt(5));
                c.setEstado(rs.getBoolean(6));
                listaCategorias.add(c);

                System.out.println("--------------DASTOS-------------------");
                System.out.println("---------------------------------------");
                System.out.println("Codigo      : " + rs.getString(1));
                System.out.println("Nombre      : " + rs.getString(2));
                System.out.println("Dni         : " + rs.getString(3));
                System.out.println("Direccion   : " + rs.getString(4));
                System.out.println("Edad        : " + rs.getInt(5));
                System.out.println("Estado      : " + rs.getString(6));
                System.out.println("--------------------------------------- \n");
            }
            this.Cerrar(true);
        } catch (Exception e) {
            this.Cerrar(false);
            throw e;
        } finally {
            pars.clear();
        }
        return listaCategorias;
    }

    /**
     * En este metodo utilizo PreparedStatement, pero adicionalmente hago que me
     * retorne el codigo autogenerado por la base de datos
     *
     * @param p
     * @throws Exception
     */
    public void registrarPersonaModoCuatro(Persona p) throws Exception {
        Connection conexion = null;
        PreparedStatement pst = null;
        String cadena = "INSERT INTO persona(nombres, dni, direccion, edad, estado) VALUES (?, ?, ?, ?, ?)";
        ResultSet rs;
        try {
            Class.forName("org.postgresql.Driver");
            conexion = DriverManager.getConnection("jdbc:postgresql://localhost/jdbc", "postgres", "omarguevara");
            pst = conexion.prepareStatement(cadena, PreparedStatement.RETURN_GENERATED_KEYS);
            pst.setString(1, p.getNombres());
            pst.setString(2, p.getDni());
            pst.setString(3, p.getDireccion());
            pst.setInt(4, p.getEdad());
            pst.setBoolean(5, p.isEstado());
            pst.executeUpdate();

            rs = pst.getGeneratedKeys();

            System.out.println("Registro Correcto con PreparedStatement");

            while (rs.next()) {
                int codigoPersona = rs.getInt(1);
                System.out.println("Codigo de registro es: " + codigoPersona);
            }

        } catch (ClassNotFoundException | SQLException e) {
            throw e;
        } finally {
            if (conexion != null) {
                conexion.close();
            }
            if (pst != null) {
                pst.close();
            }
        }
    }

}
