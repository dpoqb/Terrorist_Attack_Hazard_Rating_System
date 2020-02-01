/*
 * Created by JFormDesigner on Mon Mar 18 16:58:22 CST 2019
 */

package main;

import main.model.Explorer;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import javax.swing.*;
import javax.swing.border.*;

/**
 * @author Brainrain
 */
public class GtdGUIChooser extends JFrame {
    public GtdGUIChooser() {
        initComponents();
    }

    private void button1ActionPerformed(ActionEvent e) {
        // TODO add your code here
        if(e.getSource() == button1){
            queryEvents frame = new queryEvents();
            // 获取屏幕尺寸
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            Dimension frameSize = frame.getSize();// 获取主界面的窗体尺寸
            // 令主界面窗体居中
            if (frameSize.height > screenSize.height)
                frameSize.height = screenSize.height;
            if (frameSize.width > screenSize.width)
                frameSize.width = screenSize.width;
            frame.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
            //frame.setResizable(false);
            frame.setVisible(true);// 令主界面显示
        }
    }

    private void button6ActionPerformed(ActionEvent e) {
        // TODO add your code here
        if(e.getSource() == button6){
            addEvents frame = new addEvents();
            // 获取屏幕尺寸
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            Dimension frameSize = frame.getSize();// 获取主界面的窗体尺寸
            // 令主界面窗体居中
            if (frameSize.height > screenSize.height)
                frameSize.height = screenSize.height;
            if (frameSize.width > screenSize.width)
                frameSize.width = screenSize.width;
            frame.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
            //frame.setResizable(false);
            frame.setVisible(true);// 令主界面显示
        }
    }

    private void button2ActionPerformed(ActionEvent e) {
        // TODO add your code here
        if(e.getSource() == button2){
            UpdateEvent frame = new UpdateEvent();
            // 获取屏幕尺寸
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            Dimension frameSize = frame.getSize();// 获取主界面的窗体尺寸
            // 令主界面窗体居中
            if (frameSize.height > screenSize.height)
                frameSize.height = screenSize.height;
            if (frameSize.width > screenSize.width)
                frameSize.width = screenSize.width;
            frame.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
            //frame.setResizable(false);
            frame.setVisible(true);// 令主界面显示
        }
    }

    private void button4ActionPerformed(ActionEvent e) {
        // TODO add your code here
        if(e.getSource() == button4){
            UpdateEvent frame = new UpdateEvent();
            // 获取屏幕尺寸
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            Dimension frameSize = frame.getSize();// 获取主界面的窗体尺寸
            // 令主界面窗体居中
            if (frameSize.height > screenSize.height)
                frameSize.height = screenSize.height;
            if (frameSize.width > screenSize.width)
                frameSize.width = screenSize.width;
            frame.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
            //frame.setResizable(false);
            frame.setVisible(true);// 令主界面显示
        }
    }

    private void button3ActionPerformed(ActionEvent e) {
        // TODO add your code here
        if(e.getSource() == button3){
            Explorer frame = new Explorer();
            // 获取屏幕尺寸
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            Dimension frameSize = frame.getSize();// 获取主界面的窗体尺寸
            // 令主界面窗体居中
            if (frameSize.height > screenSize.height)
                frameSize.height = screenSize.height;
            if (frameSize.width > screenSize.width)
                frameSize.width = screenSize.width;
            frame.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
            //frame.setResizable(false);
            frame.setVisible(true);// 令主界面显示
        }
    }

    private void thisWindowClosed(WindowEvent e) {
        // TODO add your code here
        System.exit(0);
    }

    private void menuItem3ActionPerformed(ActionEvent e) {
        // TODO add your code here
        final Runtime runtime = Runtime.getRuntime();
        Process process = null;
        final String cmd =
                "rundll32 url.dll FileProtocolHandler " +
                        "file:F:\\论文\\2019-2020毕业论文\\projectDM\\doc\\Codebook_Chinese.doc";
        try {
            process = runtime.exec(cmd);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    private void menuItem4ActionPerformed(ActionEvent e) {
        // TODO add your code here
        final Runtime runtime = Runtime.getRuntime();
        Process process = null;
        final String cmd =
                "rundll32 url.dll FileProtocolHandler " +
                        "file:F:\\论文\\2019-2020毕业论文\\projectDM\\doc\\Codebook_English.pdf";
        try {
            process = runtime.exec(cmd);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        menuBar1 = new JMenuBar();
        menu4 = new JMenu();
        menu5 = new JMenu();
        menu1 = new JMenu();
        menuItem3 = new JMenuItem();
        menuItem4 = new JMenuItem();
        menu2 = new JMenu();
        panel10 = new JPanel();
        label11 = new JLabel();
        panel1 = new JPanel();
        button1 = new JButton();
        button6 = new JButton();
        button2 = new JButton();
        button4 = new JButton();
        button3 = new JButton();
        panel2 = new JPanel();
        label12 = new JLabel();
        label13 = new JLabel();
        label14 = new JLabel();
        label15 = new JLabel();

        //======== this ========
        setTitle("GTD GUI Chooser");
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                thisWindowClosed(e);
            }
        });
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        //======== menuBar1 ========
        {
            menuBar1.setMargin(new Insets(5, 2, 5, 0));

            //======== menu4 ========
            {
                menu4.setText("Settings");
            }
            menuBar1.add(menu4);

            //======== menu5 ========
            {
                menu5.setText("Help");

                //======== menu1 ========
                {
                    menu1.setText("Codebook");

                    //---- menuItem3 ----
                    menuItem3.setText("Chinese");
                    menuItem3.addActionListener(e -> menuItem3ActionPerformed(e));
                    menu1.add(menuItem3);

                    //---- menuItem4 ----
                    menuItem4.setText("English");
                    menuItem4.addActionListener(e -> menuItem4ActionPerformed(e));
                    menu1.add(menuItem4);
                }
                menu5.add(menu1);

                //======== menu2 ========
                {
                    menu2.setText("About");
                }
                menu5.add(menu2);
            }
            menuBar1.add(menu5);
        }
        setJMenuBar(menuBar1);

        //======== panel10 ========
        {
            panel10.setLayout(new GridBagLayout());
            ((GridBagLayout)panel10.getLayout()).columnWidths = new int[] {70, 111, 0, 131, 60, 0};
            ((GridBagLayout)panel10.getLayout()).rowHeights = new int[] {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
            ((GridBagLayout)panel10.getLayout()).columnWeights = new double[] {1.0, 1.0, 0.0, 1.0, 1.0, 1.0E-4};
            ((GridBagLayout)panel10.getLayout()).rowWeights = new double[] {0.0, 0.0, 1.0, 1.0, 0.0, 1.0, 1.0, 1.0, 0.0, 0.0, 1.0E-4};

            //---- label11 ----
            label11.setIcon(new ImageIcon("F:\\\u8bba\u6587\\2019-2020\u6bd5\u4e1a\u8bba\u6587\\projectDM\\src\\image\\logo.png"));
            panel10.add(label11, new GridBagConstraints(0, 2, 4, 5, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.VERTICAL,
                new Insets(0, 0, 5, 10), 0, 0));

            //======== panel1 ========
            {
                panel1.setBorder(new TitledBorder("Application"));
                panel1.setFont(panel1.getFont().deriveFont(panel1.getFont().getSize() + 1f));
                panel1.setLayout(new GridBagLayout());
                ((GridBagLayout)panel1.getLayout()).columnWidths = new int[] {0, 0};
                ((GridBagLayout)panel1.getLayout()).rowHeights = new int[] {0, 0, 0, 0, 0, 0};
                ((GridBagLayout)panel1.getLayout()).columnWeights = new double[] {1.0, 1.0E-4};
                ((GridBagLayout)panel1.getLayout()).rowWeights = new double[] {1.0, 1.0, 1.0, 1.0, 1.0, 1.0E-4};

                //---- button1 ----
                button1.setText("Query Event");
                button1.setFont(button1.getFont().deriveFont(button1.getFont().getSize() + 6f));
                button1.setFocusable(false);
                button1.addActionListener(e -> button1ActionPerformed(e));
                panel1.add(button1, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 5, 0), 0, 0));

                //---- button6 ----
                button6.setText("Add Event");
                button6.setFont(button6.getFont().deriveFont(button6.getFont().getSize() + 6f));
                button6.setFocusable(false);
                button6.addActionListener(e -> {
			button6ActionPerformed(e);
			button6ActionPerformed(e);
		});
                panel1.add(button6, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 5, 0), 0, 0));

                //---- button2 ----
                button2.setText("Update Event");
                button2.setFont(button2.getFont().deriveFont(button2.getFont().getSize() + 6f));
                button2.setFocusable(false);
                button2.addActionListener(e -> button2ActionPerformed(e));
                panel1.add(button2, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 5, 0), 0, 0));

                //---- button4 ----
                button4.setText("Delete Event");
                button4.setFont(button4.getFont().deriveFont(button4.getFont().getSize() + 6f));
                button4.addActionListener(e -> button4ActionPerformed(e));
                panel1.add(button4, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 5, 0), 0, 0));

                //---- button3 ----
                button3.setFont(button3.getFont().deriveFont(button3.getFont().getSize() + 6f));
                button3.setText("Model");
                button3.setFocusable(false);
                button3.addActionListener(e -> button3ActionPerformed(e));
                panel1.add(button3, new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 0, 0), 0, 0));
            }
            panel10.add(panel1, new GridBagConstraints(4, 1, 1, 7, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(0, 0, 5, 0), 0, 0));

            //======== panel2 ========
            {
                panel2.setLayout(new FlowLayout(FlowLayout.LEFT));

                //---- label12 ----
                label12.setText("Risk Rating System for Terrorist Attacks.");
                label12.setFont(label12.getFont().deriveFont(label12.getFont().getSize() - 1f));
                panel2.add(label12);

                //---- label13 ----
                label13.setText("Version 1.0");
                label13.setFont(label13.getFont().deriveFont(label13.getFont().getSize() - 1f));
                panel2.add(label13);

                //---- label14 ----
                label14.setText("(c) 2018-2019");
                label14.setFont(label14.getFont().deriveFont(label14.getFont().getSize() - 1f));
                panel2.add(label14);

                //---- label15 ----
                label15.setText("Song Wei");
                label15.setFont(label15.getFont().deriveFont(label15.getFont().getSize() - 1f));
                panel2.add(label15);
            }
            panel10.add(panel2, new GridBagConstraints(0, 8, 1, 2, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(0, 0, 0, 10), 0, 0));
        }
        contentPane.add(panel10, BorderLayout.CENTER);
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private JMenuBar menuBar1;
    private JMenu menu4;
    private JMenu menu5;
    private JMenu menu1;
    private JMenuItem menuItem3;
    private JMenuItem menuItem4;
    private JMenu menu2;
    private JPanel panel10;
    private JLabel label11;
    private JPanel panel1;
    private JButton button1;
    private JButton button6;
    private JButton button2;
    private JButton button4;
    private JButton button3;
    private JPanel panel2;
    private JLabel label12;
    private JLabel label13;
    private JLabel label14;
    private JLabel label15;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
