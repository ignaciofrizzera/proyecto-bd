package gui;

import logica.Logica;

import javax.swing.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.util.Date;

public class DialogReservarIda extends JDialog {

    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField tipoDocumentoText;
    private JTextField numeroDocumentoText;
    private final Date fecha;
    private final String clase;
    private final String vuelo;
    private final Logica logica;

    public DialogReservarIda(Date fecha, String clase, String vuelo, Logica logica) {
        this.fecha = fecha;
        this.clase = clase;
        this.vuelo = vuelo;
        this.logica = logica;

        setContentPane(contentPane);
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(e -> onOK());

        buttonCancel.addActionListener(e -> onCancel());

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(e -> onCancel(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onOK() {
        String tipoDocumento = tipoDocumentoText.getText();
        String numeroDocumentoString = numeroDocumentoText.getText();
        int numeroDocumento;
        String res=null;

        try {
            numeroDocumento = Integer.parseInt(numeroDocumentoString);
            res = logica.reservar_ida(fecha, clase, vuelo, tipoDocumento, numeroDocumento);
            showMsg(res);
            dispose();
        } catch (NumberFormatException e) {
            String ERROR_DNI = "El documento sólo puede contener números";
            showMsg(ERROR_DNI);
        } catch (SQLException e){
            showMsg(e.getMessage());
        }
    }
    private void showMsg(String msg) {
        DialogMsg dialog = new DialogMsg(msg);
        dialog.pack();
        dialog.setVisible(true);

    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }


}
