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
import models.Supliers;
import models.SupliersDao;
import views.SystemView;

public class SupliersController implements ActionListener, MouseListener, KeyListener {

    private Supliers suplier;
    private SupliersDao suplierDao;
    private SystemView views;
    String rol = rol_user;

    DefaultTableModel model = new DefaultTableModel();

    public SupliersController(Supliers suplier, SupliersDao suplierDao, SystemView views) {
        this.suplier = suplier;
        this.suplierDao = suplierDao;
        this.views = views;
        //Boton de registrar proveedor
        this.views.btn_register_suplier.addActionListener(this);
        //Boton de modificar proveedor
        this.views.btn_update_suplier.addActionListener(this);
        //Boton de eliminar proveedor
        this.views.btn_delete_suplier.addActionListener(this);
        //Boton de cancelar
        this.views.btn_cancel_suplier.addActionListener(this);
        //Colocar label en escucha
        this.views.jLabelSupliers.addMouseListener(this);
        //Buscador
        this.views.txt_search_suplier.addKeyListener(this);
        this.views.supliers_table.addMouseListener(this);
        
        getSuplierName();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        //Registrar proveedor
        if (e.getSource() == views.btn_register_suplier) {
            //Verificar si los campos están vacios
            if (views.txt_suplier_name.getText().equals("")
                    || views.txt_suplier_description.getText().equals("")
                    || views.txt_suplier_adress.getText().equals("")
                    || views.txt_suplier_telephone.getText().equals("")
                    || views.txt_suplier_email.getText().equals("")
                    || views.cmb_suplier_city.getSelectedItem().toString().equals("")) {
                JOptionPane.showMessageDialog(null, "Todos los campos son obligatorios");
            } else {
                //Realizar la inserción
                suplier.setName(views.txt_suplier_name.getText().trim());
                suplier.setDescription(views.txt_suplier_description.getText().trim());
                suplier.setAdress(views.txt_suplier_adress.getText().trim());
                suplier.setTelephone(views.txt_suplier_telephone.getText().trim());
                suplier.setEmail(views.txt_suplier_email.getText().trim());
                suplier.setCity(views.cmb_suplier_city.getSelectedItem().toString());

                if (suplierDao.registerSuplierQuery(suplier)) {
                    cleanTable();
                    cleanFields();
                    listAllSupliers();
                    JOptionPane.showMessageDialog(null, "Proveedor registrado correctamente");
                } else {
                    JOptionPane.showMessageDialog(null, "Ha ocurrido un error al registrar a un proveedor");
                }
            }
            //Modificar proveedores
        } else if (e.getSource() == views.btn_update_suplier) {
            if (views.txt_suplier_id.getText().equals("")) {
                JOptionPane.showMessageDialog(null, "Selecciona una fila de la tabla para continuar");
            } else {
                //Verificar si los campos están vacios
                if (views.txt_suplier_name.getText().equals("")
                        || views.txt_suplier_adress.getText().equals("")
                        || views.txt_suplier_telephone.getText().equals("")
                        || views.txt_suplier_email.getText().equals("")) {
                    JOptionPane.showMessageDialog(null, "Todos los campos son obligatorios");
                } else {
                    suplier.setName(views.txt_suplier_name.getText().trim());
                    suplier.setDescription(views.txt_suplier_description.getText().trim());
                    suplier.setAdress(views.txt_suplier_adress.getText().trim());
                    suplier.setTelephone(views.txt_suplier_telephone.getText().trim());
                    suplier.setEmail(views.txt_suplier_email.getText().trim());
                    suplier.setCity(views.cmb_suplier_city.getSelectedItem().toString());
                    suplier.setId(Integer.parseInt(views.txt_suplier_id.getText()));

                    if (suplierDao.updateSuplierQuery(suplier)) {
                        cleanTable();
                        cleanFields();
                        listAllSupliers();
                        views.btn_register_suplier.setEnabled(true);
                        JOptionPane.showMessageDialog(null, "Datos del proveedor modificados con éxito");

                    } else {
                        JOptionPane.showMessageDialog(null, "Ha ocurrido un error al modificar a un proveedor");
                    }
                }
            }
            //Eliminar proveedores
        } else if (e.getSource() == views.btn_delete_suplier) {
            int row = views.supliers_table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(null, "Seleccione un proveedor a eliminar");
            } else {
                int id = Integer.parseInt(views.supliers_table.getValueAt(row, 0).toString());
                int question = JOptionPane.showConfirmDialog(null, "Seguro desea eliminar a " + views.txt_suplier_name.getText() + "?");

                if (question == 0 && suplierDao.deleteSuplierQuery(id)) {
                    cleanFields();
                    cleanTable();
                    listAllSupliers();
                    views.btn_register_suplier.setEnabled(true);
                    JOptionPane.showMessageDialog(null, "Proveedor eliminado con exito");
                } else {
                    JOptionPane.showMessageDialog(null, "No se eliminó al proveedor");
                }
            }
        } else if (e.getSource() == views.btn_cancel_suplier) {
            cleanFields();
            views.btn_register_suplier.setEnabled(true);
        }
    }

    //Listar
    public void listAllSupliers() {
        if (rol.equals("Administrador")) {
            List<Supliers> list = suplierDao.listSupliersQuery(views.txt_search_suplier.getText());
            model = (DefaultTableModel) views.supliers_table.getModel();
            Object[] row = new Object[7];
            for (int i = 0; i < list.size(); i++) {
                row[0] = list.get(i).getId();
                row[1] = list.get(i).getName();
                row[2] = list.get(i).getDescription();
                row[3] = list.get(i).getAdress();
                row[4] = list.get(i).getTelephone();
                row[5] = list.get(i).getEmail();
                row[6] = list.get(i).getCity();
                model.addRow(row);
            }
            views.supliers_table.setModel(model);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getSource() == views.supliers_table) {
            int row = views.supliers_table.rowAtPoint(e.getPoint());
            views.txt_suplier_id.setText(views.supliers_table.getValueAt(row, 0).toString());
            views.txt_suplier_name.setText(views.supliers_table.getValueAt(row, 1).toString());
            views.txt_suplier_description.setText(views.supliers_table.getValueAt(row, 2).toString());
            views.txt_suplier_adress.setText(views.supliers_table.getValueAt(row, 3).toString());
            views.txt_suplier_telephone.setText(views.supliers_table.getValueAt(row, 4).toString());
            views.txt_suplier_email.setText(views.supliers_table.getValueAt(row, 5).toString());
            views.cmb_suplier_city.setSelectedItem(views.supliers_table.getValueAt(row, 6).toString());

            //Deshabilitar
            views.txt_suplier_id.setEditable(false);
            views.btn_register_suplier.setEnabled(false);
        }else if (e.getSource()== views.jLabelSupliers){
            if(rol.equals("Administrador")){
                views.jTabbedPane1.setSelectedIndex(4);
                cleanTable();
                cleanFields();
                listAllSupliers();
            }else{
                views.jTabbedPane1.setEnabledAt(4,false);
                views.jLabelSupliers.setEnabled(false);
                JOptionPane.showMessageDialog(null,"No tienes permisos de administrador para acceder a esta vista");
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
        if (e.getSource() == views.txt_search_suplier) {
            //Limpiar tabla
            cleanTable();
            //Listar proveedores
            listAllSupliers();
        }
    }

    //Limpiar campos
    public void cleanFields() {
        views.txt_suplier_id.setText("");
        views.txt_suplier_id.setEditable(true);
        views.txt_suplier_name.setText("");
        views.txt_suplier_description.setText("");
        views.txt_suplier_adress.setText("");
        views.txt_suplier_telephone.setText("");
        views.txt_suplier_email.setText("");
        views.cmb_suplier_city.setSelectedIndex(0);
    }

    public void cleanTable() {
        for (int i = 0; i < model.getRowCount(); i++) {
            model.removeRow(i);
            i = i - 1;
        }
    }
        //Metodo para mostrar el nombre del proveedor
        public void getSuplierName(){
        List<Supliers> list = suplierDao.listSupliersQuery(views.txt_search_suplier.getText());
        for (int i = 0;i<list.size();i++){
            int id = list.get(i).getId();
            String name = list.get(i).getName();
           views.cmb_purchase_suplier.addItem(new DynamicCombobox(id, name));
        }
    }
}
