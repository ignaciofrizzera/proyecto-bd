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
    private final String MSG_ERROR_CONECTAR = "Error al conectar con la base datos";
    private final String MSG_ERROR_ELEGIR_RESERVA = "Error, debe elegir el/los vuelo/s y clase/s para reservar";

    private static Logica logica;

    private JButton adminButton;
    private JButton userButton;
    private JPanel mainPanel;
    private JTextArea textSQL;
    private JTable valuesTable;
    private JPanel panelAdmin;
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
    private JTable tableVueloElegidoIda;
    private JTable tableVueloElegidoVuelta;
    private JButton reservarButton;
    private DefaultListModel<String> tablesListModel;
    private DefaultListModel<String> atributeListModel;
    private DefaultTableModel tableViajesIdaModel;
    private DefaultTableModel valuesTableModel;
    private DefaultTableModel tableViajesVueltaModel;
    private DefaultTableModel tableVueloElegidoIdaModel;
    private DefaultTableModel tableVueloElegidoVueltaModel;

    public GUI() {
        GUI myGUI = this;
        logica = new Logica();

        adminButton.addActionListener(actionEvent -> {
            DialogAdmin dialog = new DialogAdmin(myGUI);
            dialog.pack();
            dialog.setVisible(true);

        });
        userButton.addActionListener(actionEvent -> {
            DialogUser dialog = new DialogUser(myGUI);
            dialog.pack();
            dialog.setVisible(true);
        });


        consultarButton.addActionListener(actionEvent -> {
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

        });

        tablesList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                String selectedTable = tablesList.getSelectedValue();

                atributeListModel.removeAllElements();
                try {
                    for (String s : logica.get_atributos(selectedTable)) {
                        atributeListModel.addElement(s);
                    }
                } catch (SQLException e) {
                    showMsg(MSG_ERROR_CONECTAR);
                }

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
        buscarVuelosButton.addActionListener(actionEvent -> {
            buscarVuelos();

            tableVueloElegidoIdaModel.setColumnCount(0);
            tableVueloElegidoIdaModel.setRowCount(0);

            tableVueloElegidoVueltaModel.setColumnCount(0);
            tableVueloElegidoVueltaModel.setRowCount(0);

            if (!idaVueltaCheckBox.isSelected()) {
                tableViajesVueltaModel.setColumnCount(0);
                tableViajesVueltaModel.setRowCount(0);
            }

        });

        tableViajesVuelta.addMouseListener(new ElegirVueloListener(tableViajesVuelta, tableViajesVueltaModel, tableVueloElegidoVueltaModel));
        tableViajesIda.addMouseListener(new ElegirVueloListener(tableViajesIda, tableViajesIdaModel, tableVueloElegidoIdaModel));

        reservarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (idaVueltaCheckBox.isSelected()) {
                    reservarVuelta();
                } else {
                    reservarIda();
                }
            }

            private void reservarIda() {
                if (tableViajesIda.getSelectedRow() == -1 || tableVueloElegidoIda.getSelectedRow() == -1) {
                    showMsg(MSG_ERROR_ELEGIR_RESERVA);
                } else {
                    String stringVuelo = (String) tableViajesIdaModel.getValueAt(tableViajesIda.getSelectedRow(), 0);

                    String datesString = (String) tableViajesIdaModel.getValueAt(tableViajesIda.getSelectedRow(), tableViajesIda.getColumnCount() - 1);
                    Date fecha = Fechas.convertirStringADate(datesString);

                    String clase = (String) tableVueloElegidoIdaModel.getValueAt(tableVueloElegidoIda.getSelectedRow(), 0);

                    DialogReservarIda dialog = new DialogReservarIda(fecha, clase, stringVuelo, logica);
                    dialog.pack();
                    dialog.setVisible(true);
                }

            }

            private void reservarVuelta() {
                if (tableViajesIda.getSelectedRow() == -1 || tableVueloElegidoIda.getSelectedRow() == -1 ||
                        tableViajesVuelta.getSelectedRow() == -1 || tableVueloElegidoVuelta.getSelectedRow() == -1) {
                    showMsg(MSG_ERROR_ELEGIR_RESERVA);
                } else {
                    String vueloIda = (String) tableViajesIdaModel.getValueAt(tableViajesIda.getSelectedRow(), 0);
                    String dateIdaString = (String) tableViajesIdaModel.getValueAt(tableViajesIda.getSelectedRow(), tableViajesIda.getColumnCount() - 1);
                    Date fechaIda = Fechas.convertirStringADate(dateIdaString);
                    String claseIda = (String) tableVueloElegidoIdaModel.getValueAt(tableVueloElegidoIda.getSelectedRow(), 0);

                    String vueloVuelta = (String) tableViajesVueltaModel.getValueAt(tableViajesVuelta.getSelectedRow(), 0);
                    String dateStringVuelta = (String) tableViajesVueltaModel.getValueAt(tableViajesVuelta.getSelectedRow(), tableViajesVuelta.getColumnCount() - 1);
                    Date fechaVuelta = Fechas.convertirStringADate(dateStringVuelta);
                    String claseVuelta = (String) tableVueloElegidoVueltaModel.getValueAt(tableVueloElegidoVuelta.getSelectedRow(), 0);

                    DialogReservarIdaVuelta dialog = new DialogReservarIdaVuelta(fechaIda, claseIda, vueloIda, fechaVuelta, claseVuelta, vueloVuelta, logica);
                    dialog.pack();
                    dialog.setVisible(true);

                }
            }
        });
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
            showMsg("Fecha de ida inválida.");
            return;
        }
        try {
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
                    showMsg("Fecha de vuelta inválida.");
                    return;
                }

                fechaVuelta = Fechas.convertirStringADate(stringFechaVuelta);
                Collection<Collection<String>> vuelosDisponiblesVuelta = logica.buscar_vuelos(ciudadDestino, ciudadOrigen, fechaVuelta);
                tableViajesVuelta.setVisible(true);
                updateTable(tableViajesVueltaModel, vuelosDisponiblesVuelta);

            }
        } catch (SQLException e) {
            showMsg(MSG_ERROR_CONECTAR);
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

        if (result == null) {
            showMsg("Error result es null o model es null");
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

        try {
            Collection<String> tablas = logica.get_tablas();

            for (String tabla : tablas) {
                tablesListModel.addElement(tabla);
            }
        } catch (SQLException e) {
            showMsg(MSG_ERROR_CONECTAR);
        }
    }

    public void showUser() {
        userButton.setVisible(false);
        adminButton.setVisible(false);
        panelUser.setVisible(true);

        Collection<String> ciudadesOrigen, ciudadesDestino;
        try {
            ciudadesOrigen = logica.ciudades_origen();
            ciudadesDestino = logica.ciudades_destino();

            for (String ciudad : ciudadesOrigen) {
                origenComboBox.addItem(ciudad);
            }

            for (String ciudad : ciudadesDestino) {
                destinoComboBox.addItem(ciudad);
            }
        } catch (SQLException e) {
            showMsg(MSG_ERROR_CONECTAR);
        }

    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                GUI g = new GUI();
                JFrame frame = new JFrame();
                frame.setSize(1920, 1080);
                frame.setVisible(true);
                frame.setContentPane(g.mainPanel);
                frame.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosing(WindowEvent e) {
                        try {
                            logica.shutdown();
                        } catch (SQLException sqlE) {
                        } finally {
                            System.exit(0);

                        }
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }


    private void createUIComponents() {
        tablesListModel = new DefaultListModel<>();
        tablesList = new JList<>(tablesListModel);

        atributeListModel = new DefaultListModel<>();
        atributeList = new JList<>(atributeListModel);

        valuesTableModel = new DefaultTableModel();
        valuesTable = new JTable(valuesTableModel);

        tableViajesIdaModel = new DefaultTableModel();
        tableViajesIda = new JTable(tableViajesIdaModel);

        tableViajesVueltaModel = new DefaultTableModel();
        tableViajesVuelta = new JTable(tableViajesVueltaModel);

        tableVueloElegidoIdaModel = new DefaultTableModel();
        tableVueloElegidoIda = new JTable(tableVueloElegidoIdaModel);

        tableVueloElegidoVueltaModel = new DefaultTableModel();
        tableVueloElegidoVuelta = new JTable(tableVueloElegidoVueltaModel);
    }

    private class ElegirVueloListener extends MouseAdapter {
        private final DefaultTableModel myModel;
        private final DefaultTableModel targetModel;
        private final JTable myTable;

        public ElegirVueloListener(JTable myTable, DefaultTableModel myModel, DefaultTableModel targetModel) {
            this.myModel = myModel;
            this.myTable = myTable;
            this.targetModel = targetModel;
        }

        public void mouseClicked(MouseEvent e) {
            String vuelo = (String) myModel.getValueAt(myTable.getSelectedRow(), 0);

            String datesString = (String) myModel.getValueAt(myTable.getSelectedRow(), myTable.getColumnCount() - 1);
            Date fecha = Fechas.convertirStringADate(datesString);
            try {
                Collection<Collection<String>> res = logica.info_vuelo(vuelo, fecha);
                updateTable(targetModel, res);
            } catch (SQLException ex) {
                showMsg(MSG_ERROR_CONECTAR);
            }

        }
    }


}