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

public class CustomersDao {

    //Instanciar la conexión a la BD
    ConnectionMySQL cn = new ConnectionMySQL();
    Connection conn;
    PreparedStatement pst; //consultas
    ResultSet rs; //obtener datos de las consultas

    //Registar cliente
    public boolean registerCustomerQuery(Customers customer) {
        String query = "INSERT INTO customers(id,full_name,adress,"
                + "telephone,email,created,updated) "
                + " VALUES(?,?,?,?,?,?,?)";
        Timestamp datetime = new Timestamp(new Date().getTime());

        try {
            conn = cn.getConnection();
            pst = conn.prepareStatement(query);
            pst.setInt(1, customer.getId());
            pst.setString(2, customer.getFull_name());
            pst.setString(3, customer.getAdress());
            pst.setString(4, customer.getTelephone());
            pst.setString(5, customer.getEmail());
            pst.setTimestamp(6, datetime);
            pst.setTimestamp(7, datetime);
            pst.execute();
            return true;

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al registrar al cliente" + e);
            return false;
        }
    }

    //Listar clientes
    public List listCustomerQuery(String value) {
        List<Customers> list_customers = new ArrayList();
        String query = "SELECT * FROM customers";
        String query_search_customer = "SELECT * FROM customers  WHERE id LIKE '%" + value + "%'";
        try {
            conn = cn.getConnection();
            if (value.equalsIgnoreCase("")) {
                pst = conn.prepareStatement(query);
                rs = pst.executeQuery();

            } else {
                pst = conn.prepareStatement(query_search_customer);
                rs = pst.executeQuery();
            }

            while (rs.next()) {
                Customers customer = new Customers();
                customer.setId(rs.getInt("id"));
                customer.setFull_name(rs.getString("full_name"));
                customer.setAdress(rs.getString("adress"));
                customer.setTelephone(rs.getString("telephone"));
                customer.setEmail(rs.getString("email"));
                list_customers.add(customer);

            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.toString());

        }
        return list_customers;

    }

    public Customers searchCustomer(int id) {

        String query_search_customer = "SELECT * FROM customers  WHERE id LIKE '%" + String.valueOf(id) + "%'";

        try {
            Customers customer = new Customers();
            conn = cn.getConnection();
            pst = conn.prepareStatement(query_search_customer);
            rs = pst.executeQuery();
            while (rs.next()) {
            
            customer.setId(rs.getInt("id"));
            customer.setFull_name(rs.getString("full_name"));
            customer.setAdress(rs.getString("adress"));
            customer.setTelephone(rs.getString("telephone"));
            customer.setEmail(rs.getString("email"));
            }
            return customer;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.toString());
            return null;
        }
        
    }
        //Modificar cliente
    public boolean updateCustomerQuery(Customers customer) {
        String query = "UPDATE customers SET full_name=?,adress=?,"
                + "telephone=?,email=?,updated=?  WHERE id=?";
        Timestamp datetime = new Timestamp(new Date().getTime());

        try {
            conn = cn.getConnection();
            pst = conn.prepareStatement(query);
            pst.setString(1, customer.getFull_name());
            pst.setString(2, customer.getAdress());
            pst.setString(3, customer.getTelephone());
            pst.setString(4, customer.getEmail());
            pst.setTimestamp(5, datetime);
            pst.setInt(6, customer.getId());
            pst.execute();
            return true;

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al modificar los datos del cliente" + e);
            return false;
        }
    }

    //Eliminar cliente
    public boolean deleteCustomerQuery(int id) {
        String query = "DELETE FROM customers WHERE id = " + id;
        try {
            conn = cn.getConnection();
            pst = conn.prepareStatement(query);
            pst.execute();
            return true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "No puede eliminar un cliente que tenga relación con otra tabla ");
            return false;
        }

    }

}
