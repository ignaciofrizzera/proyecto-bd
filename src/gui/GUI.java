package gui;

import logica.Logica;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.Collections;

public class GUI {
    private JButton adminButton;
    private JButton userButton;
    private JPanel mainPanel;
    private JTextArea textSQL;
    private JTable valuesTable;
    private JPanel panelAdmin;
    private JPanel panelUser;
    private JLabel ciudadOrigen;
    private JComboBox comboBox1;
    private JLabel ciudadDestino;
    private JComboBox comboBox2;
    private JCheckBox checkBox1;
    private JTextField dayText;
    private JTextField monthText;
    private JTable table1;
    private JList tablesList;
    private JButton consultarButton;
    private DefaultListModel tablesModel;
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

                logica.recibir_query(query);
            }
        });
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
    public boolean connectEmpleado(int legajo, char[] password) {
        return connectEmpleado(legajo, password);
    }

    public void showAdmin() {
        userButton.setVisible(false);
        adminButton.setVisible(false);
        panelAdmin.setVisible(true);

        Collection<String> tablas = logica.get_tablas();

        for(String tabla: tablas){
            tablesModel.addElement(tabla);
        }
    }

    public void showUser() {
        userButton.setVisible(false);
        adminButton.setVisible(false);
        panelUser.setVisible(true);
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
        tablesModel = new DefaultListModel();
        tablesList = new JList(tablesModel);

    }
}

