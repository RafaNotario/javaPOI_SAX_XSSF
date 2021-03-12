
package controllerInsert;

import conexion.ConexionDBOriginal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

public class controller {

    ConexionDBOriginal con2 = new ConexionDBOriginal();

    public void GuardaTurno(String Cadena) {
        Connection cn = con2.conexion();
        PreparedStatement pps = null;
        String SQL = "";
        SQL = "INSERT INTO carga4 VALUES "+Cadena+";";
       // System.out.println("Qwery = "+SQL);
       try {
            pps = cn.prepareStatement(SQL);
            pps.executeUpdate();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error durante la transaccion.");
            Logger.getLogger(controller.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (pps != null) {
                    pps.close();
                }
                if (cn != null) {
                    cn.close();
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage());
            }
        }   //finally catch  
    }//@endGuardaTurno

}
