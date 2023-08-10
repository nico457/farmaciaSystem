package controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import models.Customers;
import models.CustomersDao;
import models.Employees;
import static models.EmployeesDao.id_user;
import views.SystemView;

public class CustomersController implements ActionListener, MouseListener, KeyListener {

    private Customers customer;
    private CustomersDao customerDao;
    private SystemView views;

    DefaultTableModel model = new DefaultTableModel();

    public CustomersController(Customers customer, CustomersDao customerDao, SystemView views) {
        this.customer = customer;
        this.customerDao = customerDao;
        this.views = views;
        //Boton de registrar cliente
        this.views.btn_register_customer.addActionListener(this);
        //Boton de modificar cliente
        this.views.btn_update_customer.addActionListener(this);
        //Boton de eliminar cliente
        this.views.btn_delete_customer.addActionListener(this);
        //Boton de cancelar
        this.views.btn_cancel_customer.addActionListener(this);
        //Colocar label en escucha
        this.views.jLabelCustomers.addMouseListener(this);
        //Buscador
        this.views.txt_search_customer.addKeyListener(this);
        this.views.customers_table.addMouseListener(this);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        //Registrar cliente
        if (e.getSource() == views.btn_register_customer) {
            //Verificar si los campos están vacios
            if (views.txt_customer_id.getText().equals("")
                    || views.txt_customer_full_name.getText().equals("")
                    || views.txt_customer_adress.getText().equals("")
                    || views.txt_customer_telephone.getText().equals("")
                    || views.txt_customer_email.getText().equals("")) {
                JOptionPane.showMessageDialog(null, "Todos los campos son obligatorios");
            } else {
                //Realizar la inserción
                customer.setId(Integer.parseInt(views.txt_customer_id.getText().trim()));
                customer.setFull_name(views.txt_customer_full_name.getText().trim());
                customer.setAdress(views.txt_customer_adress.getText().trim());
                customer.setTelephone(views.txt_customer_telephone.getText().trim());
                customer.setEmail(views.txt_customer_email.getText().trim());

                if (customerDao.registerCustomerQuery(customer)) {
                    cleanTable();
                    cleanFields();
                    listAllCustomers();
                    JOptionPane.showMessageDialog(null, "Cliente registrado correctamente");
                } else {
                    JOptionPane.showMessageDialog(null, "Ha ocurrido un error al registrar a un cliente");
                }
            }
            //Modificar cliente
        } else if (e.getSource() == views.btn_update_customer) {
            if (views.txt_customer_id.getText().equals("")) {
                JOptionPane.showMessageDialog(null, "Selecciona una fila de la tabla para continuar");
            } else {
                //Verificar si los campos están vacios
                if (views.txt_customer_id.getText().equals("")
                        || views.txt_customer_full_name.getText().equals("")
                        || views.txt_customer_adress.getText().equals("")
                        || views.txt_customer_telephone.getText().equals("")
                        || views.txt_customer_email.getText().equals("")) {
                    JOptionPane.showMessageDialog(null, "Todos los campos son obligatorios");
                } else {
                    customer.setId(Integer.parseInt(views.txt_customer_id.getText().trim()));
                    customer.setFull_name(views.txt_customer_full_name.getText().trim());
                    customer.setAdress(views.txt_customer_adress.getText().trim());
                    customer.setTelephone(views.txt_customer_telephone.getText().trim());
                    customer.setEmail(views.txt_customer_email.getText().trim());

                    if (customerDao.updateCustomerQuery(customer)) {
                        cleanTable();
                        cleanFields();
                        listAllCustomers();
                        views.btn_register_customer.setEnabled(true);
                        JOptionPane.showMessageDialog(null, "Datos del cliente modificados con éxito");

                    } else {
                        JOptionPane.showMessageDialog(null, "Ha ocurrido un error al modificar a un cliente");
                    }
                }

            }
            //Eliminar
        } else if (e.getSource() == views.btn_delete_customer) {
            int row = views.customers_table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(null, "Seleccione un cliente a eliminar");
            } else {
                int id = Integer.parseInt(views.customers_table.getValueAt(row, 0).toString());
                int question = JOptionPane.showConfirmDialog(null, "Seguro desea eliminar a " + views.txt_customer_full_name.getText() + "?");

                if (question == 0 && customerDao.deleteCustomerQuery(id)) {
                    cleanFields();
                    cleanTable();
                    listAllCustomers();
                    views.btn_register_customer.setEnabled(true);
                    JOptionPane.showMessageDialog(null, "Cliente eliminado con exito");
                } else {
                    JOptionPane.showMessageDialog(null, "No se eliminó al cliente");
                }
            }
            //Cancelar
        } else if (e.getSource() == views.btn_cancel_customer) {
            cleanFields();
            views.btn_register_customer.setEnabled(true);
        }

    }

    //Listar
    public void listAllCustomers() {
        List<Customers> list = customerDao.listCustomerQuery(views.txt_search_customer.getText());
        model = (DefaultTableModel) views.customers_table.getModel();
        Object[] row = new Object[5];
        for (int i = 0; i < list.size(); i++) {
            row[0] = list.get(i).getId();
            row[1] = list.get(i).getFull_name();
            row[2] = list.get(i).getAdress();
            row[3] = list.get(i).getTelephone();
            row[4] = list.get(i).getEmail();
            model.addRow(row);
        }
        
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getSource() == views.customers_table) {
            int row = views.customers_table.rowAtPoint(e.getPoint());
            views.txt_customer_id.setText(views.customers_table.getValueAt(row, 0).toString());
            views.txt_customer_full_name.setText(views.customers_table.getValueAt(row, 1).toString());
            views.txt_customer_adress.setText(views.customers_table.getValueAt(row, 2).toString());
            views.txt_customer_telephone.setText(views.customers_table.getValueAt(row, 3).toString());
            views.txt_customer_email.setText(views.customers_table.getValueAt(row, 4).toString());

            //Deshabilitar
            views.txt_customer_id.setEditable(false);
            views.btn_register_customer.setEnabled(false);
        } else if (e.getSource() == views.jLabelCustomers) {
            views.jTabbedPane1.setSelectedIndex(2);
            cleanTable();
            cleanFields();
            listAllCustomers();
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getSource() == views.txt_search_customer) {
            //Limpiar tabla
            cleanTable();
            //Listar clientes
            listAllCustomers();
        }
    }

    //Limpiar campos
    public void cleanFields() {
        views.txt_customer_id.setText("");
        views.txt_customer_id.setEditable(true);
        views.txt_customer_full_name.setText("");
        views.txt_customer_adress.setText("");
        views.txt_customer_telephone.setText("");
        views.txt_customer_email.setText("");
    }

    public void cleanTable() {
        for (int i = 0; i < model.getRowCount(); i++) {
            model.removeRow(i);
            i = i - 1;
        }
    }

}
