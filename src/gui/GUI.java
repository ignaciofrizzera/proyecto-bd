package gui;

import fechas.Fechas;
import logica.Logica;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

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
    private JTable tableViajesIda;
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
    private JButton buscarVuelosButton;
    private JTable tableViajesVuelta;
    private JTable tableVueloElegido;
    private DefaultListModel<String> tablesListModel;
    private DefaultListModel<String> atributeListModel;
    private DefaultTableModel tableViajesIdaModel;
    private DefaultTableModel valuesTableModel;
    private DefaultTableModel tableViajesVueltaModel;
    private DefaultTableModel tableVueloElegidoModel;

    private static Logica logica;

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
                    Collection<Collection<String>> resultado = logica.recibir_statement(query);

                    if (resultado != null) {
                        updateTable(valuesTableModel, resultado);
                    } else {
                        showMsg("Se actualizó la base de datos correctamente");
                    }
                } catch (SQLException e) {
                    showMsg(e.getMessage());
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
        buscarVuelosButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                buscarVuelos();
            }
        });


        tableViajesVuelta.addMouseListener(new ElegirVueloListener(tableViajesVuelta, tableViajesVueltaModel));
        tableViajesIda.addMouseListener(new ElegirVueloListener(tableViajesIda, tableViajesIdaModel));

    }

    private void buscarVuelos() {
        String ciudadOrigen,
                ciudadDestino,
                diaIda,
                mesIda,
                añoIda,
                diaVuelta,
                mesVuelta,
                añoVuelta,
                stringFechaIda,
                stringFechaVuelta;

        Date fechaIda, fechaVuelta;

        ciudadOrigen = (String) origenComboBox.getSelectedItem();
        ciudadDestino = (String) destinoComboBox.getSelectedItem();
        diaIda = diaIdaText.getText();
        mesIda = mesIdaText.getText();
        añoIda = añoIdaText.getText();

        stringFechaIda = diaIda + "/" + mesIda + "/" + añoIda;

        if (!Fechas.validar(stringFechaIda)) {
            showMsg("Fecha de ida invalida.");
            return;
        }

        fechaIda = Fechas.convertirStringADate(stringFechaIda);
        Collection<Collection<String>> vuelosDisponiblesIda = logica.buscar_vuelos(ciudadOrigen, ciudadDestino, fechaIda);
        tableViajesIda.setVisible(true);
        updateTable(tableViajesIdaModel, vuelosDisponiblesIda);

        if (idaVueltaCheckBox.isSelected()) {
            diaVuelta = diaVueltaText.getText();
            mesVuelta = mesVueltaText.getText();
            añoVuelta = añoVueltaText.getText();

            stringFechaVuelta = diaVuelta + "/" + mesVuelta + "/" + añoVuelta;

            if (!Fechas.validar(stringFechaVuelta)) {
                showMsg("Fecha de vuelta invalida.");
                return;
            }

            fechaVuelta = Fechas.convertirStringADate(stringFechaVuelta);
            Collection<Collection<String>> vuelosDisponiblesVuelta = logica.buscar_vuelos(ciudadDestino, ciudadOrigen, fechaVuelta);
            tableViajesVuelta.setVisible(true);
            updateTable(tableViajesVueltaModel, vuelosDisponiblesVuelta);
        }
    }


    private void showMsg(String msg) {
        DialogMsg dialog = new DialogMsg(msg);
        dialog.pack();
        dialog.setVisible(true);

    }

    private void updateTable(DefaultTableModel model, Collection<Collection<String>> result) {
        model.setColumnCount(0);
        model.setRowCount(0);

        if (model==null || result == null){
            showMsg("error result es null o model es null");
            return;
        }

        Iterator<Collection<String>> iterator = result.iterator();

        if (iterator.hasNext()) {
            Collection<String> columns = iterator.next();

            for (String column : columns) {
                model.addColumn(column);
            }

            while (iterator.hasNext()) {
                Collection<String> rowAux = iterator.next();
                Object[] row = rowAux.toArray();
                model.addRow(row);
            }
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

        Collection<String> ciudadesOrigen, ciudadesDestino;
        ciudadesOrigen = logica.ciudades_origen();
        ciudadesDestino = logica.ciudades_destino();

        for (String ciudad : ciudadesOrigen) {
            origenComboBox.addItem(ciudad);
        }

        for (String ciudad : ciudadesDestino) {
            destinoComboBox.addItem(ciudad);
        }

    }

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    GUI g = new GUI();
                    JFrame frame = new JFrame();
                    frame.setSize(1920, 1080);
                    frame.setVisible(true);
                    frame.setContentPane(g.mainPanel);
                    frame.addWindowListener(new WindowAdapter() {
                        @Override
                        public void windowClosing(WindowEvent e) {
                            logica.shutdown();
                            System.exit(0);
                        }
                    });
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

        tableViajesIdaModel = new DefaultTableModel();
        tableViajesIda = new JTable(tableViajesIdaModel);

        tableViajesVueltaModel = new DefaultTableModel();
        tableViajesVuelta = new JTable(tableViajesVueltaModel);

        tableVueloElegidoModel = new DefaultTableModel();
        tableVueloElegido = new JTable(tableVueloElegidoModel);
    }

    //Este hay que borrarlo
    Collection<Collection<String>> doSo(){return null;}

    private class ElegirVueloListener extends MouseAdapter {
        private DefaultTableModel myModel;
        private JTable myTable;

        public ElegirVueloListener(JTable myTable, DefaultTableModel myModel) {
            this.myModel = myModel;
            this.myTable = myTable;
        }

        public void mouseClicked(MouseEvent e) {
            int vuelo;
            String stringVuelo = (String) myModel.getValueAt(myTable.getSelectedRow(), 0);
            vuelo = Integer.parseInt(stringVuelo);

            

            Collection<Collection<String>> res = logica.info_vuelo();
            updateTable(tableVueloElegidoModel, res);

        }
    }


}