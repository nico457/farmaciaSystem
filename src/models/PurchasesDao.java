
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


public class PurchasesDao {
    //Instanciar la conexión a la BD
    ConnectionMySQL cn = new ConnectionMySQL();
    Connection conn;
    PreparedStatement pst; //consultas
    ResultSet rs; //obtener datos de las consultas
    
    //Registar compra
    public boolean registerCPurchaseQuery(int suplier_id, int employee_id, double total){
        String query ="INSERT INTO purchases(suplier_id,employee_id,total,created)"
                + " VALUES(?,?,?,?)";
        Timestamp datetime = new Timestamp(new Date().getTime());
        
        try{
            conn = cn.getConnection();
            pst = conn.prepareStatement(query);
            pst.setInt(1,suplier_id);
            pst.setInt(2,employee_id);
            pst.setDouble(3,total);
            pst.setTimestamp(4,datetime);
            pst.execute();
            return true;
            
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null,"Error al registrar la compra" + e);
            return false;
        }
    }
    
    //Registrar detalle de compra
    public boolean registerPurchaseDetailQuery(int purchase_id, double purchase_price, int purchase_amount,
                                                double purchase_subtotal, int product_id){
    String query = "INSERT INTO purchase_details(purchase_id, purchase_price, purchase_amount,"
            + "purchase_subtotal, product_id) VALUES(?,?,?,?,?)";
    Timestamp datetime = new Timestamp(new Date().getTime());
        
        try{
            conn = cn.getConnection();
            pst = conn.prepareStatement(query);
            pst.setInt(1,purchase_id);
            pst.setDouble(2,purchase_price);
            pst.setInt(3,purchase_amount);
            pst.setDouble(4,purchase_subtotal);
            pst.setInt(5,product_id);
            pst.execute();
            return true;
            
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null,"Error al registrar los detalles de la compra" + e);
            return false;
        }
    
}
    
    //Obtener id de la compra
    public int purchaseId(){
        int id = 0;
        String query = "SELECT MAX(id) AS id FROM purchases";
        try{
            conn = cn.getConnection();
            pst = conn.prepareStatement(query);
            rs = pst.executeQuery();
            if(rs.next()){
                id = rs.getInt("id");
            }
        }catch(SQLException e){
            System.err.println(e.getMessage());
            
        }
        return id;
    }
    
    //Listar todas las compras realizadas
    public List listAllPurchasesQuery(){
        List<Purchases> list_purchases = new ArrayList();
        String query = "SELECT pu.*, su.name AS suplier_name FROM purchases pu,"
                + " supliers su WHERE pu.suplier_id = su.id ORDER BY pu.id ASC";
        try{
            conn = cn.getConnection();
            pst = conn.prepareStatement(query);
            rs = pst.executeQuery();
    
            while (rs.next()){
                Purchases purchase = new Purchases();
                purchase.setId(rs.getInt("id"));
                purchase.setSuplier_name_product(rs.getString("suplier_name"));
                purchase.setTotal(rs.getDouble("total"));
                purchase.setCreated(rs.getString("created"));
                list_purchases.add(purchase);
                
          }
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null,e.toString());
    
        }
        return list_purchases;
        
    }
    
    //Listar compras para imprimir factura 
    public List listPurchaseDetailQuery(int id){
        List<Purchases> list_purchases = new ArrayList();
        String query = """
                       SELECT pu.created, pude.purchase_price , pude.purchase_amount, pude.purchase_subtotal, su.name AS suplier_name ,
                       pro.name AS product_name, em. full_name FROM purchases pu INNER JOIN purchase_details pude ON pu.id = pude.purchase_id
                       INNER JOIN products pro ON pude.product_id = pro.id INNER JOIN supliers su ON pu.suplier_id = su.id
                       INNER JOIN employees em ON pu.employee_id = em.id WHERE pu.id = ?""";
        try{
            conn = cn.getConnection();
            pst = conn.prepareStatement(query);
            pst.setInt(1,id);
            rs = pst.executeQuery();
    
            while (rs.next()){
                Purchases purchase = new Purchases();
                purchase.setProduct_name(rs.getString("product_name"));
                purchase.setPurchase_amount(rs.getInt("purchase_amount"));
                purchase.setPurchase_price(rs.getDouble("purchase_price"));
                purchase.setPurchase_subtotal(rs.getInt("purchase_subtotal"));
                purchase.setSuplier_name_product(rs.getString("suplier_name"));
                purchase.setCreated(rs.getString("created"));
                purchase.setPurcharser(rs.getString("full_name"));
                list_purchases.add(purchase);
                
          }
        }catch(SQLException e){
            JOptionPane.showMessageDialog(null,e.toString());
    
        }
        return list_purchases;
        
    }
    
}
