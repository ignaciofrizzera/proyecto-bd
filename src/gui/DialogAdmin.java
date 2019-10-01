package gui;

import javax.swing.*;
import java.awt.event.*;

public class DialogAdmin extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JPasswordField passwordField;
    private JLabel passwordLabel;
    private JLabel errorLabel;
    private GUI mainGUI;

    public DialogAdmin(GUI mainGUI) {
        this.mainGUI = mainGUI;
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onOK() {
        boolean conecto = mainGUI.connectAdmin(passwordField.getPassword());

        if (!conecto){
            errorLabel.setVisible(true);
        }else{
            mainGUI.showAdmin();
            dispose();
        }
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }


}
