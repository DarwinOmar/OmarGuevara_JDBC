package omarguevara_jdbc;

import dao.daoPersona;
import entidades.Persona;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author DarwinOmarUsuario
 */
public class OmarGuevara_JDBC {

    public static void main(String[] args) {
        // TODO code application logic here
        daoPersona daoP = new daoPersona();
        Persona p = new Persona("DARWIN PEREZ ZABALETA", "55512312", "AV. BOLIVIA 123", 20, true);

        try {

            daoP.registrarPersonaStatement(p);
            daoP.registrarPersonaPreparedStatement(p);
            daoP.registrarPersonaCallableStatement(p);
            daoP.registarPersonaFinal(p);
            daoP.listarPersonasStatement();
            daoP.listarPersonasPreparedStatement();
            daoP.listarPersonasCallableStatement();
            daoP.listaPersonasFinal();

        } catch (Exception ex) {
            System.out.println("Error " + ex.getMessage());
        }
    }

}
