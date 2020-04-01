package FunctionReader.src;

import FunctionReader.src.FileCreator;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class Creation {
    private JTextField fileName;
    private JTextField StartY;
    private JTextField startX;
    private JTextField tol;
    private JTextField mu;
    private JTextField mass;
    private JButton jb;
    private JLabel mass1;
    private JLabel mu1;
    private JLabel tol1;
    private JLabel startX1;
    private JLabel starty1;
    private JLabel vmax1;
    private JTextField vmax;
    private JLabel goalx1;
    private JTextField goalX;
    private JLabel goaly1;
    private JTextField goalY;
    private JLabel height1;
    private JTextField height;
    private JLabel fileName1;
    private JPanel mainPanel;
    private JTextField gravity;
    private JLabel gravity1;

    public Creation() {
        jb.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {

                try {
                    FileCreator f = new FileCreator(fileName.getText(), mass.getText(), mu.getText(), tol.getText(), startX.getText(), StartY.getText(), vmax.getText(), goalX.getText(), goalY.getText(), height.getText(), gravity.getText());
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
    }
    public static void main(String[] args)
    {
        JFrame frame = new JFrame("Fire Golf");
        frame.setContentPane(new Creation().mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

    }


}

