/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package studentrecordmanagementsystem;

import javax.swing.SwingUtilities;
import ui.MainApplicationFrame;

public class StudentRecordManagementSystem {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                MainApplicationFrame frame = new MainApplicationFrame();
                frame.setVisible(true);
            }
        });
    }
}