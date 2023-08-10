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
import models.Categories;
import models.CategoriesDao;
import models.DynamicCombobox;
import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;
import static models.EmployeesDao.rol_user;
import views.SystemView;

public class CategoriesController implements ActionListener, MouseListener, KeyListener {

    private Categories category;
    private CategoriesDao categoryDao;
    private SystemView views;
    String rol = rol_user;

    DefaultTableModel model = new DefaultTableModel();

    public CategoriesController(Categories category, CategoriesDao categoryDao, SystemView views) {
        this.category = category;
        this.categoryDao = categoryDao;
        this.views = views;

        //Boton de registrar categoría
        this.views.btn_register_category.addActionListener(this);
        //Boton de modificar categoría
        this.views.btn_update_category.addActionListener(this);
        //Boton de eliminar categoría
        this.views.btn_delete_category.addActionListener(this);
        //Boton de cancelar categoría
        this.views.btn_cancel_category.addActionListener(this);
        //Colocar label en escucha
        this.views.jLabelCategories.addMouseListener(this);
        //Tabla de empleados
        this.views.categories_table.addMouseListener(this);
        this.views.txt_search_category.addKeyListener(this);
        
        getCategoryName();
        AutoCompleteDecorator.decorate(views.cmb_product_category);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        //Registrar proveedor
        if (e.getSource() == views.btn_register_category) {
            //Verificar si los campos están vacios
            if (views.txt_category_name.getText().equals("")) {
                JOptionPane.showMessageDialog(null, "Todos los campos son obligatorios");
            } else {
                //Realizar la inserción
                category.setName(views.txt_category_name.getText().trim());

                if (categoryDao.registerCategoryQuery(category)) {
                    cleanTable();
                    cleanFields();
                    listAllCategories();
                    JOptionPane.showMessageDialog(null, "Categoría registrado correctamente");
                } else {
                    JOptionPane.showMessageDialog(null, "Ha ocurrido un error al registrar a una categoría");
                }
            }
        } else if (e.getSource() == views.btn_update_category) {
            if (views.txt_category_id.getText().equals("")) {
                JOptionPane.showMessageDialog(null, "Selecciona una fila de la tabla para continuar");
            } else {
                //Verificar si los campos están vacios
                if (views.txt_category_id.getText().equals("")
                        || views.txt_category_name.getText().equals("")) {
                    JOptionPane.showMessageDialog(null, "Todos los campos son obligatorios");
                } else {
                    category.setId(Integer.parseInt(views.txt_category_id.getText()));
                    category.setName(views.txt_category_name.getText().trim());

                    if (categoryDao.updateCategoryQuery(category)) {
                        cleanTable();
                        cleanFields();
                        listAllCategories();
                        views.btn_register_category.setEnabled(true);
                        JOptionPane.showMessageDialog(null, "Datos de la categoría modificados con éxito");

                    } else {
                        JOptionPane.showMessageDialog(null, "Ha ocurrido un error al modificar a una categoría");
                    }
                }
            }
            //Eliminar
        } else if (e.getSource() == views.btn_delete_category) {
            int row = views.categories_table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(null, "Seleccione una categoría a eliminar");
            } else {
                int id = Integer.parseInt(views.categories_table.getValueAt(row, 0).toString());
                int question = JOptionPane.showConfirmDialog(null, "Seguro desea eliminar a la categoría " + views.txt_category_name.getText() + "?");

                if (question == 0 && categoryDao.deleteCategoryQuery(id)) {
                    cleanFields();
                    cleanTable();
                    listAllCategories();
                    views.btn_register_category.setEnabled(true);
                    JOptionPane.showMessageDialog(null, "Categoría eliminada con exito");
                } else {
                    JOptionPane.showMessageDialog(null, "No se eliminó a la categoría");
                }
            }
            //Cancelar
        } else if (e.getSource() == views.btn_cancel_category) {
            cleanFields();
            views.btn_register_category.setEnabled(true);
        }
    }

    //Listar
    public void listAllCategories() {
        if (rol.equals("Administrador")) {
            List<Categories> list = categoryDao.listCategoriesQuery(views.txt_search_category.getText());
            model = (DefaultTableModel) views.categories_table.getModel();
            Object[] row = new Object[2];
            for (int i = 0; i < list.size(); i++) {
                row[0] = list.get(i).getId();
                row[1] = list.get(i).getName();
                model.addRow(row);
            }
            views.categories_table.setModel(model);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getSource() == views.categories_table) {
            int row = views.categories_table.rowAtPoint(e.getPoint());
            views.txt_category_id.setText(views.categories_table.getValueAt(row, 0).toString());
            views.txt_category_name.setText(views.categories_table.getValueAt(row, 1).toString());
            views.btn_register_category.setEnabled(false);
        } else if (e.getSource() == views.jLabelCategories) {
            if (rol.equals("Administrador")) {
                views.jTabbedPane1.setSelectedIndex(5);
                cleanTable();
                cleanFields();
                listAllCategories();
            } else {
                views.jTabbedPane1.setEnabledAt(5, false);
                views.jLabelCategories.setEnabled(false);
                JOptionPane.showMessageDialog(null, "No tienes permisos de administrador para acceder a esta vista");
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
        if (e.getSource() == views.txt_search_category) {
            //Limpiar tabla
            cleanTable();
            //Listar proveedores
            listAllCategories();
        }
    }

    public void cleanTable() {
        for (int i = 0; i < model.getRowCount(); i++) {
            model.removeRow(i);
            i = i - 1;
        }
    }

    //Limpiar campos
    public void cleanFields() {
        views.txt_category_id.setText("");
        views.txt_category_name.setText("");
    }
    
    //Metodo para mostrar el nombre de las categorias
    public void getCategoryName(){
        List<Categories> list = categoryDao.listCategoriesQuery(views.txt_search_category.getText());
        for (int i = 0;i<list.size();i++){
            int id = list.get(i).getId();
            String name = list.get(i).getName();
            views.cmb_product_category.addItem(new DynamicCombobox(id,name));
        }
    }
}
