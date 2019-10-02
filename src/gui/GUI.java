package gui;

import logica.Logica;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

public class GUI {
    private JButton adminButton;
    private JButton userButton;
    private JPanel mainPanel;
    private JTextArea textSQL;
    private JTable valuesTable;
    private javax.swing.JPanel panelAdmin;
    private JPanel panelUser;
    private JLabel ciudadOrigen;
    private JComboBox origenComboBox;
    private JLabel ciudadDestino;
    private JComboBox destinoComboBox;
    private JCheckBox idaVueltaCheckBox;
    private JTextField diaIdaText;
    private JTextField mesIdaText;
    private JTable tablaViajesDisponibles;
    private JList<String> tablesList;
    private JButton consultarButton;
    private JList<String> atributeList;
    private JPanel panelFechaIda;
    private JPanel panelFechaVuelta;
    private JTextField añoIdaText;
    private JTextField diaVueltaText;
    private JTextField mesVueltaText;
    private JTextField añoVueltaText;
    private JPanel panelCiudades;
    private DefaultListModel<String> tablesListModel;
    private DefaultListModel<String> atributeListModel;
    private DefaultTableModel valuesTableModel;
    private Logica logica;

    public GUI() {
        GUI myGUI = this;
        logica = new Logica();

        adminButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                DialogAdmin dialog = new DialogAdmin(myGUI);
                dialog.pack();
                dialog.setVisible(true);

            }
        });
        userButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                DialogUser dialog = new DialogUser(myGUI);
                dialog.pack();
                dialog.setVisible(true);
            }
        });


        consultarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String query = textSQL.getText();
                try {
                    Collection<Collection<String>> resultado = logica.recibir_query(query);
                    updateTable(resultado);
                } catch (SQLException e) {
                    showSQLError(e.getMessage());
                }

            }
        });

        tablesList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                String selectedTable = tablesList.getSelectedValue();

                atributeListModel.removeAllElements();
                atributeListModel.addAll(logica.get_atributos(selectedTable));

            }
        });
        idaVueltaCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (idaVueltaCheckBox.isSelected()) {
                    cambiarEstado(true);
                } else {
                    cambiarEstado(false);
                }
            }

            private void cambiarEstado(boolean cambio) {
                for (Component c : panelFechaVuelta.getComponents()) {
                    c.setEnabled(cambio);
                }
            }
        });
    }
    private void showSQLError(String msg){
        DialogError dialog = new DialogError(msg);
        dialog.pack();
        dialog.setVisible(true);

    }

    private void updateTable(Collection<Collection<String>> result){
        valuesTableModel.setColumnCount(0);
        valuesTableModel.setRowCount(0);

        Iterator<Collection<String>> iterator = result.iterator();
        Collection<String> columns = iterator.next();

        for (String column:columns){
            valuesTableModel.addColumn(column);
        }

        while (iterator.hasNext()){
            Collection<String> rowAux = iterator.next();
            Object[] row = rowAux.toArray();
            valuesTableModel.addRow(row);
        }
    }

    /**
     * Conecta con la base de datos como administrador con la contraseña especificados.
     *
     * @param password Contraseña.
     * @return true si pudo conectar, false en caso contrario.
     */

    public boolean connectAdmin(char[] password) {
        return logica.conectar_admin(password);
    }

    /**
     * Conecta con la base de datos como empleado, con el legajo y contraseña especificados.
     *
     * @param legajo   Legajo del empleado.
     * @param password Password del empleado.
     * @return true si pudo conectar, false en caso contrario.
     */
    public boolean connectEmpleado(String legajo, char[] password) {
        return logica.conectar_empleado(legajo, password);
    }

    public void showAdmin() {
        userButton.setVisible(false);
        adminButton.setVisible(false);
        panelAdmin.setVisible(true);

        Collection<String> tablas = logica.get_tablas();

        for (String tabla : tablas) {
            tablesListModel.addElement(tabla);
        }
    }

    public void showUser() {
        userButton.setVisible(false);
        adminButton.setVisible(false);
        panelUser.setVisible(true);

        Collection<Collection<String>> c1 = new LinkedList<>();
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    GUI g = new GUI();
                    JFrame frame = new JFrame();
                    frame.setSize(1280, 720);
                    frame.setVisible(true);
                    frame.setContentPane(g.mainPanel);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


    private void createUIComponents() {
        tablesListModel = new DefaultListModel<String>();
        tablesList = new JList<String>(tablesListModel);

        atributeListModel = new DefaultListModel<>();
        atributeList = new JList<String>(atributeListModel);

        valuesTableModel = new DefaultTableModel();
        valuesTable = new JTable(valuesTableModel);
        valuesTableModel.setRowCount(0);
        valuesTableModel.setColumnCount(0);
    }
}

