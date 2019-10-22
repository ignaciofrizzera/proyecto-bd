package gui;

import javax.swing.*;
import java.awt.event.*;

public class DialogUser extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JLabel userLabel;
    private JLabel errorLabel;
    private JTextField userTextField;
    private JLabel passwordLabel;
    private JPasswordField passwordField;
    private final GUI mainGUI;

    public DialogUser(GUI mainGUI) {
        this.mainGUI = mainGUI;
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
        String legajo = userTextField.getText();
        boolean conecto = mainGUI.connectEmpleado(legajo, passwordField.getPassword());
        if (!conecto) {
            errorLabel.setVisible(true);
        } else {
            mainGUI.showUser();
            dispose();
        }
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

}
