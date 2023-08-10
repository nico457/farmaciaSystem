/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.JOptionPane;


public class SupliersDao {
    //Instanciar la conexión a la BD
    ConnectionMySQL cn = new ConnectionMySQL();
    Connection conn;
    PreparedStatement pst; //consultas
    ResultSet rs; //obtener datos de las consultas
    
    //Variables para enviar datos entre interfaces
    public static int id_user = 0;
    public static String full_name_user = "";
    public static String username_user = "";
    public static String adress_user = "";
    public static String rol_user = "";
    public static String email_user = "";
    public static String telephone_user = "";
    
    
    //Registar proveedor
    public boolean registerSuplierQuery(Supliers suplier){
        String query ="INSERT INTO supliers(name,description,adress,"
                + "telephone,email,city,created,updated) "
                + " VALUES(?,?,?,?,?,?,?,?)";
        Timestamp datetime = new Timestamp(new Date().getTime());
        
        try{
            conn = cn.getConnection();
            pst = conn.prepareStatement(query);
            pst.setString(1,suplier.getName());
            pst.setString(2,suplier.getDescription());
            pst.setString(3,suplier.getAdress());
            pst.setString(4,suplier.getTelephone());
            pst.setString(5,suplier.getEmail());
            pst.setString(6,suplier.getCity());
            pst.setTimestamp(7,datetime);
            pst.setTimestamp(8,datetime);
            pst.execute();
            return true;
            
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null,"Error al registrar al proveedor" + e);
            return false;
        }
    }
    
    //Listar proveedores
    public List listSupliersQuery(String value){
        List<Supliers> list_supliers = new ArrayList();
        String query = "SELECT * FROM supliers";
        String query_search_suplier = "SELECT * FROM supliers  WHERE name LIKE '%" + value + "%'";
        try{
            conn = cn.getConnection();
            if(value.equalsIgnoreCase("")){
                pst = conn.prepareStatement(query);
                rs = pst.executeQuery();
                
            }else{
                pst= conn.prepareStatement(query_search_suplier);
                rs = pst.executeQuery();
            }
            
            while (rs.next()){
                Supliers suplier = new Supliers();
                suplier.setId(rs.getInt("id"));
                suplier.setName(rs.getString("name"));
                suplier.setDescription(rs.getString("description"));
                suplier.setAdress(rs.getString("adress"));
                suplier.setTelephone(rs.getString("telephone"));
                suplier.setEmail(rs.getString("email"));
                suplier.setCity(rs.getString("city"));
                list_supliers.add(suplier);
                
          }
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null,e.toString());
    
        }
        return list_supliers;
        
    }
    
    //Modificar proveedor
    public boolean updateSuplierQuery(Supliers suplier){
        String query ="UPDATE supliers SET name=?,description=?,adress=?,"
                + "telephone=?,email=?,city=?,updated=?  WHERE id=?";
        Timestamp datetime = new Timestamp(new Date().getTime());
        
        try{
            conn = cn.getConnection();
            pst = conn.prepareStatement(query);
            pst.setString(1,suplier.getName());
            pst.setString(2,suplier.getDescription());
            pst.setString(3,suplier.getAdress());
            pst.setString(4,suplier.getTelephone());
            pst.setString(5,suplier.getEmail());
            pst.setString(6,suplier.getCity());
            pst.setTimestamp(7,datetime);
            pst.setInt(8,suplier.getId());
            pst.execute();
            return true;
            
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null,"Error al modificar los datos del proveedor" + e);
            return false;
        }
    }
    
    //Eliminar proveedor
    public boolean deleteSuplierQuery(int id){
        String query = "DELETE FROM supliers WHERE id = " + id;
        try{
            conn = cn.getConnection();
            pst = conn.prepareStatement(query);
            pst.execute();
            return true;
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null,"No puede eliminar un proveedor que tenga relación con otra tabla ");
            return false;
        }
        
    }
}

    
