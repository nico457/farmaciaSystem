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
import models.DynamicCombobox;
import static models.EmployeesDao.rol_user;
import models.Products;
import models.ProductsDao;
import views.SystemView;

public class ProductsController implements ActionListener, MouseListener, KeyListener {

    private Products product;
    private ProductsDao productDao;
    private SystemView views;
    String rol = rol_user;

    DefaultTableModel model = new DefaultTableModel();

    public ProductsController(Products product, ProductsDao productDao, SystemView views) {
        this.product = product;
        this.productDao = productDao;
        this.views = views;
        //Boton de registrar producto
        this.views.btn_register_product.addActionListener(this);
        this.views.products_table.addMouseListener(this);
        this.views.txt_search_product.addKeyListener(this);
        this.views.btn_update_product.addActionListener(this);
        this.views.btn_cancel_product.addActionListener(this);
        this.views.btn_delete_product.addActionListener(this);
        this.views.jLabelProducts.addMouseListener(this);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == views.btn_register_product) {
            //Verificar si los campos están vacios
            if (views.txt_product_code.getText().equals("")
                    || views.txt_product_name.getText().equals("")
                    || views.txt_product_description.getText().equals("")
                    || views.txt_product_unit_price.getText().equals("")
                    || views.cmb_product_category.getSelectedItem().toString().equals("")) {
                JOptionPane.showMessageDialog(null, "Todos los campos son obligatorios");
            } else {
                //Realizar la inserción
                product.setCode(Integer.parseInt(views.txt_product_code.getText()));
                product.setName(views.txt_product_name.getText().trim());
                product.setDescription(views.txt_product_description.getText().trim());
                product.setUnit_price(Double.parseDouble(views.txt_product_unit_price.getText()));
                DynamicCombobox category_id = (DynamicCombobox) views.cmb_product_category.getSelectedItem();
                product.setCategory_id(category_id.getId());

                //Registrar producto
                if (productDao.registerProductQuery(product)) {
                    cleanTable();
                    cleanFields();
                    listAllProducts();
                    JOptionPane.showMessageDialog(null, "Producto registrado correctamente");
                } else {
                    JOptionPane.showMessageDialog(null, "Ha ocurrido un error al registrar al producto");
                }
            }
            //Modificar producto
        } else if (e.getSource() == views.btn_update_product) {
            if (views.txt_product_id.getText().equals("")) {
                JOptionPane.showMessageDialog(null, "Selecciona una fila de la tabla para continuar");
            } else {
                //Verificar si los campos están vacios
                if (views.txt_product_code.getText().equals("")
                        || views.txt_product_name.getText().equals("")
                        || views.txt_product_description.getText().equals("")
                        || views.txt_product_unit_price.getText().equals("")
                        || views.cmb_product_category.getSelectedItem().toString().equals("")) {
                    JOptionPane.showMessageDialog(null, "Todos los campos son obligatorios");
                } else {
                    //Realizar la inserción
                    product.setCode(Integer.parseInt(views.txt_product_code.getText()));
                    product.setName(views.txt_product_name.getText().trim());
                    product.setDescription(views.txt_product_description.getText().trim());
                    product.setUnit_price(Double.parseDouble(views.txt_product_unit_price.getText()));
                    DynamicCombobox category_id = (DynamicCombobox) views.cmb_product_category.getSelectedItem();
                    product.setCategory_id(category_id.getId());
                    product.setId(Integer.parseInt(views.txt_product_id.getText()));

                    if (productDao.updateProductQuery(product)) {
                        cleanTable();
                        cleanFields();
                        listAllProducts();
                        views.btn_register_product.setEnabled(true);
                        JOptionPane.showMessageDialog(null, "Datos del producto modificados con éxito");

                    } else {
                        JOptionPane.showMessageDialog(null, "Ha ocurrido un error al modificar a un producto");
                    }
                }
            }
        //Eliminar productos
        }else if(e.getSource() == views.btn_delete_product){
            int row = views.products_table.getSelectedRow();
            if (row == -1){
                JOptionPane.showMessageDialog(null, "Seleccione un producto  a eliminar");
             }else{
                int id = Integer.parseInt(views.products_table.getValueAt(row,0).toString());
                int question = JOptionPane.showConfirmDialog(null,"Seguro desea eliminar  " + views.txt_product_name.getText()+"?");
                
                if (question == 0 && productDao.deleteProductQuery(id)){
                    cleanFields();
                    cleanTable();
                    listAllProducts();
                    views.btn_register_product.setEnabled(true);
                    JOptionPane.showMessageDialog(null,"Producto eliminado con exito");
                }else{
                    JOptionPane.showMessageDialog(null,"No se eliminó al producto");
                }
             }
        //Cancelar
        }else if(e.getSource() == views.btn_cancel_product){
            cleanFields();
            views.btn_register_product.setEnabled(true);
            views.txt_product_id.setEnabled(true);
    }
    }
    //Listar
    public void listAllProducts() {
        if (rol.equals("Administrador") || rol.equals("Auxiliar")) {
            List<Products> list = productDao.listProductsQuery(views.txt_search_product.getText());
            model = (DefaultTableModel) views.products_table.getModel();
            Object[] row = new Object[7];
            for (int i = 0; i < list.size(); i++) {
                row[0] = list.get(i).getId();
                row[1] = list.get(i).getCode();
                row[2] = list.get(i).getName();
                row[3] = list.get(i).getDescription();
                row[4] = list.get(i).getUnit_price();
                row[5] = list.get(i).getProduct_quantity();
                row[6] = list.get(i).getCategory_name();
                model.addRow(row);
            }
            views.products_table.setModel(model);

            if (rol.equals("Auxiliar")) {
                views.btn_register_product.setEnabled(false);
                views.btn_update_product.setEnabled(false);
                views.btn_delete_product.setEnabled(false);
                views.btn_cancel_product.setEnabled(false);

                views.txt_product_code.setEnabled(false);
                views.txt_product_description.setEnabled(false);
                views.txt_product_id.setEditable(false);
                views.txt_product_name.setEditable(false);
                views.txt_product_unit_price.setEditable(false);
            }
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getSource() == views.products_table) {
            int row = views.products_table.rowAtPoint(e.getPoint());
            views.txt_product_id.setText(views.products_table.getValueAt(row, 0).toString());
            product = productDao.searchProduct(Integer.parseInt(views.txt_product_id.getText()));
            views.txt_product_code.setText("" + product.getCode());
            views.txt_product_name.setText(product.getName());
            views.txt_product_description.setText(product.getDescription());
            views.txt_product_unit_price.setText("" + product.getUnit_price());
            views.cmb_product_category.setSelectedItem(new DynamicCombobox(product.getCategory_id(), product.getCategory_name()));
            //Deshabilitar
            views.btn_register_product.setEnabled(false);

            //Deshabilitar
            views.btn_register_suplier.setEnabled(false);
        }else if (e.getSource()== views.jLabelProducts){
            if(rol.equals("Administrador")){
                views.jTabbedPane1.setSelectedIndex(0);
                cleanTable();
                cleanFields();
                listAllProducts();
            }
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
        if (e.getSource() == views.txt_search_product) {
            //Limpiar tabla
            cleanTable();
            //Listar productos
            listAllProducts();
        }
    }

    public void cleanTable() {
        for (int i = 0; i < model.getRowCount(); i++) {
            model.removeRow(i);
            i = i - 1;
        }
    }
    public void cleanFields(){
        views.txt_product_id.setText("");
        views.txt_product_code.setText("");
        views.txt_product_description.setText("");
        views.txt_product_name.setText("");
        views.txt_product_unit_price.setText("");
        
        
        
    }
}
