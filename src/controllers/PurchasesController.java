/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import models.DynamicCombobox;
import models.EmployeesDao;
import static models.EmployeesDao.rol_user;
import models.Products;
import models.ProductsDao;
import models.Purchases;
import models.PurchasesDao;
import views.Print;
import views.SystemView;

/**
 *
 * @author nicoe
 */
public class PurchasesController implements KeyListener, ActionListener,MouseListener {

    private Purchases purchase;
    private PurchasesDao purchaseDao;
    private SystemView views;
    
    private int getIdSuplier = 0;
    private int item = 0;
    DefaultTableModel model = new DefaultTableModel();
    DefaultTableModel temp;

    //Instanciar el modelo productos
    Products product = new Products();
    ProductsDao productDao = new ProductsDao();

    String rol = rol_user;

    public PurchasesController(Purchases purchase, PurchasesDao purchaseDao, SystemView views) {
        this.purchase = purchase;
        this.purchaseDao = purchaseDao;
        this.views = views;
        //Boton de agregar
        this.views.btn_add_product_to_buy.addActionListener(this);
        //Botond e comprar
        this.views.btn_confirm_purchase.addActionListener(this);
        //Boton de eliminar comora
        this.views.btn_remove_purchase.addActionListener(this);
        this.views.txt_purchase_product_code.addKeyListener(this);
        this.views.txt_purchase_price.addKeyListener(this);
        this.views.btn_new_purchase.addActionListener(this);
        this.views.jLabelPurchases.addMouseListener(this);
        this.views.jLabelReports.addMouseListener(this);
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getSource() == views.txt_purchase_product_code) {
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                if (views.txt_purchase_product_code.getText().equals("")) {
                    JOptionPane.showMessageDialog(null, "Ingresa el codigo del producto a comprar");
                } else {
                    int id = Integer.parseInt(views.txt_purchase_product_code.getText());
                    product = productDao.searchCode(id);
                    views.txt_purchase_product_name.setText(product.getName());
                    views.txt_purchase_id.setText("" + product.getId());
                    views.txt_purchase_amount.requestFocus();
                }
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        
            if (e.getSource() == views.txt_purchase_price) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                int quantity;
                double price = Double.parseDouble(views.txt_purchase_price.getText());
                if (views.txt_purchase_amount.getText().equals("")) {
                    quantity = 1;
                    views.txt_purchase_subtotal.setText("" + price * quantity);

                } else {
                    quantity = Integer.parseInt(views.txt_purchase_amount.getText());
                    price = Double.parseDouble(views.txt_purchase_price.getText());
                    views.txt_purchase_subtotal.setText("" + quantity * price);
                }
            }
        }

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == views.btn_add_product_to_buy){
            DynamicCombobox suplier_cmb = (DynamicCombobox) views.cmb_purchase_suplier.getSelectedItem();
            int suplier_id = suplier_cmb.getId();
            if(getIdSuplier == 0){
                getIdSuplier = suplier_id;
                
            }else{
                if(getIdSuplier != suplier_id){
                    JOptionPane.showMessageDialog(null, "No puede realizar una misma compra a diferentes proveedores");
                    
                }else{
                    int amount = Integer.parseInt(views.txt_purchase_amount.getText());
                    String product_name  = views.txt_purchase_product_name.getText();
                    double price =Double.parseDouble(views.txt_purchase_price.getText());
                    int purchase_id = Integer.parseInt(views.txt_purchase_id.getText());
                    String suplier_name = views.cmb_purchase_suplier.getSelectedItem().toString();
                    
                    if(amount>0){
                        temp = (DefaultTableModel) views.purchases_table.getModel();    
                        for (int i = 0; i<views.purchases_table.getRowCount();i++){
                            if(views.purchases_table.getValueAt(i, 1).equals(views.txt_purchase_product_name.getText())){
                                JOptionPane.showMessageDialog(null,"El producto ya esta registrado en la tabla de compras");
                                return;
                            }
                        }
                        ArrayList list = new ArrayList();
                        item = 1;
                        list.add(item);
                        list.add(purchase_id);
                        list.add(product_name);
                        list.add(amount);
                        list.add(price);
                        list.add(amount*price);
                        list.add(suplier_name);
                        
                        Object[] obj = new Object[6];
                        
                        obj[0] = list.get(1);
                        obj[1] = list.get(2);
                        obj[2] = list.get(3);
                        obj[3] = list.get(4);
                        obj[4] = list.get(5);
                        obj[5] = list.get(6);
                        
                        temp.addRow(obj);
                        views.purchases_table.setModel(temp);
                        
                        cleanFields();
                        views.cmb_purchase_suplier.setEditable(false);
                        views.txt_purchase_product_code.requestFocus();
                        calculatePurchase();
                    }
                }
            }
        }else if(e.getSource() == views.btn_confirm_purchase){
            insertPurchase();
        }else if (e.getSource() == views.btn_remove_purchase){
            model = (DefaultTableModel) views.purchases_table.getModel();
            model.removeRow(views.purchases_table.getSelectedRow());
            calculatePurchase();
            views.txt_purchase_product_code.requestFocus();
                    
        }else if (e.getSource() == views.btn_new_purchase){
            cleanTableTemp();
            cleanFields();
        }
        }
    
    
    private void insertPurchase(){
        double total = Double.parseDouble(views.txt_purchase_total_to_pay.getText());
        int employee_id = EmployeesDao.id_user;
        
        if(purchaseDao.registerCPurchaseQuery(getIdSuplier, employee_id, total)){
            int purchase_id = purchaseDao.purchaseId();
            for (int i = 0; i < views.purchases_table.getRowCount(); i++) {
                int product_id = Integer.parseInt(views.purchases_table.getValueAt(i, 0).toString());
                int purchase_amount = Integer.parseInt(views.purchases_table.getValueAt(i, 2).toString());
                double purchase_price = Double.parseDouble(views.purchases_table.getValueAt(i, 3).toString());
                double pruchase_subtotal = purchase_price * purchase_amount;
                
                //Registrar detalles de la compra
                purchaseDao.registerPurchaseDetailQuery(purchase_id, purchase_price, purchase_amount, pruchase_subtotal, product_id);
                
                
                //Traer la cantidad de productos
                product = productDao.searchId(product_id);
                int amount = product.getProduct_quantity() + purchase_amount;
                productDao.UpdateStockQuery(amount, product_id);
                
                
            }
            cleanTableTemp();
            JOptionPane.showMessageDialog(null, "Compra generada con éxito");
            getIdSuplier = 0;
            cleanFields();
            
            Print print = new Print(purchase_id);
            print.setVisible(true);
        }
    }
    
    public void listAllPurchases(){
        if(rol.equals("Administrador") || rol.equals("Auxiliar")){
            List<Purchases> list = purchaseDao.listAllPurchasesQuery();
            model = (DefaultTableModel) views.table_all_purchases.getModel();
            Object[] row = new Object[4];
            
            for (int i = 0; i < list.size(); i++) {
                row[0] = list.get(i).getId();
                row[1] = list.get(i).getSuplier_name_product();
                row[2] = list.get(i).getTotal();
                row[3] = list.get(i).getCreated();
                model.addRow(row);
            }
            views.table_all_purchases.setModel(model);
        }
    }
    public void cleanTable() {
        for (int i = 0; i < model.getRowCount(); i++) {
            model.removeRow(i);
            i = i - 1;
        }
    }
    public void cleanFields(){
        views.txt_purchase_product_name.setText("");
        views.txt_purchase_price.setText("");
        views.txt_purchase_amount.setText("");
        views.txt_purchase_product_code.setText("");
        views.txt_purchase_subtotal.setText("");
        views.txt_purchase_id.setText("");
        views.txt_purchase_total_to_pay.setText("");
        
    }
    
    public void calculatePurchase(){
        double total = 0.0;
        int numRow = views.purchases_table.getRowCount();
        
        
        for(int i = 0; i<numRow;i++){
            total = total + Double.parseDouble(String.valueOf(views.purchases_table.getValueAt(i, 4)));
            
        }
        views.txt_purchase_total_to_pay.setText(""+total);
    }
    
    public void cleanTableTemp(){
        for (int i = 0; i < temp.getRowCount(); i++) {
            temp.removeRow(i);
            i = i - 1;
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if(e.getSource() == views.jLabelPurchases){
            if(rol.equals("Administrador")){
                views.jTabbedPane1.setSelectedIndex(1);
                cleanTable();
            }else{
                views.jTabbedPane1.setEnabledAt(1, false);
                views.jLabelPurchases.setEnabled(false);
                JOptionPane.showMessageDialog(null, "No tiene privilegios de administrador para acceder a esta vista");
            }
        }else if(e.getSource() == views.jLabelReports   ){
            views.jTabbedPane1.setSelectedIndex(6);
            cleanTable();
            listAllPurchases();
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

    
}
