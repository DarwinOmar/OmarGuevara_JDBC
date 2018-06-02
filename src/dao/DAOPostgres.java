package dao;


import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class DAOPostgres {

    private Connection conexion;
    private boolean transaccionIniciada;
    private final String driver = "org.postgresql.Driver";
    
    private final String usuarioPostgres = "postgres";
    private final String url = "jdbc:postgresql://localhost/jdbc";
    private final String clave = "omarguevara";
    

//    

    /**
     * Este metodo se utiliza para generar los reportes
     *
     * @return
     * @throws java.sql.SQLException
     * @throws java.lang.ClassNotFoundException
     *
     */
    protected Connection conexionReporte() throws SQLException, ClassNotFoundException {
        Class.forName(driver);
        this.conexion = DriverManager.getConnection(url, usuarioPostgres, clave);
        return conexion;
    }

    protected Connection getConexion() {
        return conexion;
    }

    protected void Conectar(boolean wTransaccion) throws Exception {
        Class.forName(driver);
        this.conexion = DriverManager.getConnection(url, usuarioPostgres, clave);

        if (wTransaccion == true) {
            this.conexion.setAutoCommit(false);
            this.transaccionIniciada = true;
        } else {
            this.conexion.setAutoCommit(true);
            this.transaccionIniciada = false;
        }
    }

    protected void Cerrar(boolean wEstado) throws Exception {
        if (this.conexion != null) {
            if (this.transaccionIniciada == true) {
                try {
                    if (wEstado == true) {
                        this.conexion.commit();
                    } else {
                        this.conexion.rollback();
                    }
                } catch (Exception e) {
                    throw e;
                }
            }
            try {
                this.conexion.close();
            } catch (Exception e) {
            }
        }
        this.conexion = null;
    }

    protected void EjecutarOrden(String wSQL) throws Exception {
        Statement st;
        if (this.conexion != null) {
            st = this.conexion.createStatement();
            st.executeUpdate(wSQL);
        }
    }

    protected ResultSet EjecutarOrdenDatos(String wSQL) throws Exception {
        Statement st;
        ResultSet rs = null;

        if (this.conexion != null) {
            st = this.conexion.createStatement();
            rs = st.executeQuery(wSQL); //select
        }
        return rs;
    }

    protected Object EjecutarProcedimiento(String wProcedimiento, List<Parametro> wParametros) throws Exception {
        CallableStatement cs;
        Object valor = null;
        int parSalida = -1;
        int i = 1;
        try {
            cs = this.getConexion().prepareCall(wProcedimiento);
            if (wParametros != null) {
                for (Parametro par : wParametros) {
                    if (par.isEntrada() == true) {
                        cs.setObject(i, par.getValor());
//            cs.setObject(par.getNombre(), par.getValor());
                    } else {
                        parSalida = i;
                        cs.registerOutParameter(i, par.getTipo());
                    }
                    i += 1;
                }
            }
            cs.executeUpdate();
            if (parSalida > 0) {
                valor = cs.getObject(parSalida);
                //valor = cs.getObject("xxx");
            }
        } catch (Exception e) {
            throw e;
        } finally {
            cs = null;
        }
        return valor;
    }

    protected ResultSet EjecutarProcedimientoDatos(String wProcedimiento, List<Parametro> wParametros) throws Exception {
        CallableStatement cs;
        ResultSet rs = null;
        int i = 1;

        try {
            cs = this.getConexion().prepareCall(wProcedimiento);
            if (wParametros != null) {
                for (Parametro par : wParametros) {
                    if (par.isEntrada() == true) {
                        cs.setObject(i, par.getValor());
                    } else {
                        cs.registerOutParameter(i, par.getTipo());
                    }
                    i += 1;
                }
            }
            cs.execute();
            //rs = cs.executeQuery();
            rs = (ResultSet) cs.getObject(1);
        } catch (Exception e) {
            throw e;
        } finally {
            cs = null;
        }

        return rs;
    }
//    private final String url = "jdbc:postgresql://10.0.5.100:5432/FSLG_ALMACEN";
    //    private final String clave = "fslgalmacen";
}
