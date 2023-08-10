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
import models.Customers;
import models.CustomersDao;
import models.EmployeesDao;
import static models.EmployeesDao.rol_user;
import models.Products;
import models.ProductsDao;
import models.Sales;
import models.SalesDao;
import views.SystemView;

public class SalesController implements ActionListener, MouseListener, KeyListener {

    private Sales sale;
    private SalesDao saleDao;
    private SystemView views;
    Products product = new Products();
    ProductsDao productDao = new ProductsDao();

    private int item = 0;
    String rol = rol_user;

    DefaultTableModel model = new DefaultTableModel();
    DefaultTableModel temp = new DefaultTableModel();

    public SalesController(Sales sale, SalesDao saleDao, SystemView views) {
        this.sale = sale;
        this.saleDao = saleDao;
        this.views = views;
        views.btn_confirm_sale.addActionListener(this);
        views.btn_new_sale.addActionListener(this);
        views.btn_remove_sale.addActionListener(this);
        views.btn_add_product_sale.addActionListener(this);
        views.jLabelSales.addMouseListener(this);
        views.jLabelReports.addMouseListener(this);
        views.txt_sale_product_code.addKeyListener(this);
        views.txt_sale_customer_id.addKeyListener(this);
        views.txt_sale_quantity.addKeyListener(this);
    }

    private void insertSale() {

        int customer_id = Integer.parseInt(views.txt_sale_customer_id.getText());
        int employee_id = EmployeesDao.id_user;
        double total = Double.parseDouble(views.txt_sale_total_to_pay.getText());

        if (saleDao.registerSaleQuery(customer_id, employee_id, total)) {

            Products product = new Products();
            ProductsDao productDao = new ProductsDao();
            int sale_id = saleDao.saleId();

            // registerPurchaseDetailQuery();
            for (int i = 0; i < views.table_all_sales.getRowCount(); i++) {
                int product_id = Integer.parseInt(views.table_all_sales.getValueAt(i,   0).toString());
                int sale_quantity = Integer.parseInt(views.table_all_sales.getValueAt(i, 2).toString());
                double sale_price = Double.parseDouble(views.table_all_sales.getValueAt(i, 3).toString());
                double sale_subtotal = sale_quantity * sale_price;

                saleDao.registerSaleDetailQuery(product_id, sale_id, sale_quantity, sale_price, sale_subtotal);

                //Traer la cantidad de productos
                product = productDao.searchId(product_id);
                //Obtener cantidad actual y restar la cantidad comprada
                int amount = product.getProduct_quantity() - sale_quantity;
                productDao.UpdateStockQuery(amount, product_id);
            }
            JOptionPane.showMessageDialog(null, "Venta generada");
            cleanTableTemp();
            cleanAllFieldsSales();
        }
    }

    public void listAllSales() {
        if (rol.equals("Administrador")) {
            List<Sales> list = saleDao.listAllSalesQuery();
            model = (DefaultTableModel) views.table_all_sales.getModel();

            //Recorrer la lista
            Object[] row = new Object[5];

            for (int i = 0; i < list.size(); i++) {
                row[0] = list.get(i).getId();
                row[1] = list.get(i).getCustomer_name();
                row[2] = list.get(i).getEmployee_name();
                row[3] = list.get(i).getTotal_to_pay();
                row[4] = list.get(i).getSale_date();
                model.addRow(row);
            }
            views.table_all_sales.setModel(model);
        }
    }

    //Calcular total a pagar tabla de ventas
    private void calculateSales() {
        double total = 0.00;
        int numRow = views.table_all_sales.getRowCount();
        for (int i = 0; i < numRow; i++) {
            if(views.table_all_sales.getValueAt(i, 4)instanceof Double )
            total = total + Double.parseDouble(String.valueOf(views.table_all_sales.getValueAt(i, 4)));
        }
        views.txt_sale_total_to_pay.setText("" + total);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == views.btn_confirm_sale) {
            System.out.println("venta confirmada");
            insertSale();

        } else if (e.getSource() == views.btn_new_sale) {
            cleanAllFieldsSales();
            cleanTableTemp();

        } else if (e.getSource() == views.btn_remove_sale) {
            model = (DefaultTableModel) views.table_all_sales.getModel();
            model.removeRow(views.table_all_sales.getSelectedRow());
            calculateSales();
            views.txt_sale_product_code.requestFocus();

        } else if (!"".equals(e.getSource() == views.btn_add_product_sale)) {

            //Agregar productos a la tabla de ventas temporalmente
            int amount = Integer.parseInt(views.txt_sale_quantity.getText());
            String product_name = views.txt_sale_product_name.getText();
            double price = Double.parseDouble(views.txt_sale_price.getText());
            int sale_id = Integer.parseInt(views.txt_sale_product_id.getText());
            double subtotal = amount * price;
            int stock = Integer.parseInt(views.txt_sale_stock.getText());
            String full_name = views.txt_sale_customer_name.getText();

            if (stock >= amount) {
                item = item + 1;
                temp = (DefaultTableModel) views.table_all_sales.getModel();

                for (int i = 0; i < views.table_all_sales.getRowCount(); i++) {
                    if (views.table_all_sales.getValueAt(i, 1).equals(views.txt_sale_product_name.getText())) {
                        JOptionPane.showMessageDialog(null, "El producto ya esta registrado en la tabla de ventas");
                        return;
                    }
                }

                ArrayList list = new ArrayList();
                list.add(item);
                list.add(sale_id);
                list.add(product_name);
                list.add(amount);
                list.add(price);
                list.add(subtotal);
                list.add(full_name);

                Object[] obj = new Object[6];
                obj[0] = list.get(1);
                obj[1] = list.get(2);
                obj[2] = list.get(3);
                obj[3] = list.get(4);
                obj[4] = list.get(5);
                obj[5] = list.get(6);

                temp.addRow(obj);
                views.table_all_sales.setModel(temp);
                calculateSales();
                cleanFieldsSales();
               
                views.txt_sale_product_code.requestFocus();

            } else {
                JOptionPane.showMessageDialog(null, "Stock no disponible");
            }

        } else {
            JOptionPane.showMessageDialog(null, "Ingrese cantidad");
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getSource() == views.jLabelSales) {
            views.jTabbedPane1.setSelectedIndex(7);
        } else if (e.getSource() == views.jLabelReports) {
            if (rol.equals("Administrador")) {
                views.jTabbedPane1.setSelectedIndex(6);
                listAllSales();
            } else {
                views.jTabbedPane1.setEnabledAt(7, false);
                views.jLabelReports.setEnabled(false);
                JOptionPane.showMessageDialog(null, "No tiene privilegios de administrador para acceder a esta vista");
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

        if (e.getSource() == views.txt_sale_product_code) {

            if (e.getKeyCode() == KeyEvent.VK_ENTER) {

                if (!"".equals(views.txt_sale_product_code.getText())) {

                    int code = Integer.parseInt(views.txt_sale_product_code.getText());
                    product = productDao.searchProduct(code);

                    if (product != null) {
                        views.txt_sale_product_name.setText(product.getName());
                        views.txt_sale_product_id.setText("" + product.getId());
                        views.txt_sale_stock.setText("" + product.getProduct_quantity());
                        System.out.println(product);
                        views.txt_sale_price.setText("" + product.getUnit_price());
                        views.txt_sale_quantity.requestFocus();

                    } else {
                        JOptionPane.showMessageDialog(null, "No existe ningún producto con ese código");
                        cleanFieldsSales();
                        views.txt_sale_product_code.requestFocus();
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Ingrese el código del producto a vender");
                }
            }
        } else if (e.getSource() == views.txt_sale_customer_id) {

            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                CustomersDao customerDao = new CustomersDao();

                if (!"".equals(views.txt_sale_customer_id.getText())) {
                    int customer_id = Integer.parseInt(views.txt_sale_customer_id.getText());
                    Customers customer = customerDao.searchCustomer(customer_id);

                    if (customer != null) {
                        views.txt_sale_customer_name.setText("" + customer.getFull_name());
                    } else {
                        views.txt_sale_customer_id.setText("");
                        JOptionPane.showMessageDialog(null, "El cliente no existe");
                    }
                }
            }
        }

    }

    @Override
    public void keyReleased(KeyEvent e) {

        if (e.getSource() == views.txt_sale_quantity) {

            int quantity;
            double price = Double.parseDouble(views.txt_sale_price.getText());

            if (views.txt_sale_quantity.getText().equals("")) {
                quantity = 1;
                views.txt_sale_price.setText("" + price);
            } else {
                quantity = Integer.parseInt(views.txt_sale_quantity.getText());
                price = Double.parseDouble(views.txt_sale_price.getText());
                views.txt_sale_subtotal.setText("" + quantity * price);
            }
        }

    }

    //Limpiar tabla
    public void cleanTable() {
        for (int i = 0; i < model.getRowCount(); i++) {
            model.removeRow(i);
            i = i - 1;
        }
    }

    //Limpiar tabla temporal
    public void cleanTableTemp() {
        for (int i = 0; i < temp.getRowCount(); i++) {
            temp.removeRow(i);
            i = i - 1;
        }
    }

    // Limpiar algunos campos
    public void cleanFieldsSales() {
        views.txt_sale_product_code.setText("");
        views.txt_sale_product_name.setText("");
        views.txt_sale_quantity.setText("");
        views.txt_sale_product_id.setText("");
        views.txt_sale_price.setText("");
        views.txt_sale_subtotal.setText("");
        views.txt_sale_stock.setText("");
    }

    //Limpiar todos los campos
    public void cleanAllFieldsSales() {
        views.txt_sale_product_code.setText("");
        views.txt_sale_product_name.setText("");
        views.txt_sale_quantity.setText("");
        views.txt_sale_product_id.setText("");
        views.txt_sale_price.setText("");
        views.txt_sale_subtotal.setText("");
        views.txt_sale_customer_id.setText("");
        views.txt_sale_customer_name.setText("");
        views.txt_sale_total_to_pay.setText("");
        views.txt_sale_stock.setText("");
    }

}
