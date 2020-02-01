/*
 * Created by JFormDesigner on Tue Mar 26 12:37:12 CST 2019
 */

package main;

import bean.EventAttribute;
import bean.ExcelInput;
import dao.EventMapper;
import jdk.nashorn.internal.scripts.JO;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.freehep.util.Assert;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.border.*;

/**
 * @author Brainrain
 */
public class addEvents extends JFrame {
    public addEvents() {
        initComponents();
    }

    private void button1ActionPerformed(ActionEvent e) {
        // TODO add your code here
        if (e.getSource() == button1) {
            JFileChooser dataFile = new JFileChooser();
            dataFile.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
            dataFile.showDialog(new JLabel(), "Select");
            File file = dataFile.getSelectedFile();
            if(file!=null) textField1.setText(file.getPath());
        }
    }

    private void button2ActionPerformed(ActionEvent e) {
        // TODO add your code here
        // choose file
        final Runtime runtime = Runtime.getRuntime();
        Process process = null;
        final String cmd =
                "rundll32 url.dll FileProtocolHandler " +
                        "file:F:\\论文\\2019-2020毕业论文\\projectDM\\src\\config\\input\\template\\gtd_template.xls";
        try {
            process = runtime.exec(cmd);
            } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    private void okButtonActionPerformed(ActionEvent e) {
        // TODO add your code here
       // add new data
       if(e.getSource()==okButton && !(textField1.getText().isEmpty())){
           String resource = "config/Configure.xml";
           InputStream inputStream = null;
           try {
               inputStream = Resources.getResourceAsStream(resource);
           } catch (IOException i) {
               i.printStackTrace();
           }
           SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
           //SqlSession cannot be shared, you must create new object when you use it every time
           SqlSession session = sqlSessionFactory.openSession();
           EventMapper mapper1 = session.getMapper(EventMapper.class);
           ArrayList<EventAttribute> atr = mapper1.getAttributes();
           // throw the first attributes idx
           String[] column = new String[atr.size()-1];
           for(int i=0;i<column.length;i++){
               column[i] = atr.get(i+1).getAttribute_name();
           }
           XlsxPaese excel = new XlsxPaese();
           List<ExcelInput> hd = null;
           session.close();
           try {
               hd = excel.getBeanList(textField1.getText(),column, ExcelInput.class);
           } catch (Exception i) {
               i.printStackTrace();
           }
           try {
               inputStream = null;
               try {
                   inputStream = Resources.getResourceAsStream(resource);
               } catch (IOException i) {
                   i.printStackTrace();
               }
               sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
               //SqlSession cannot be shared, you must create new object when you use it every time
               session = sqlSessionFactory.openSession();
               EventMapper mapper2 =  session.getMapper(EventMapper.class);
               //批量插入
               int reslut = mapper2.insertSelective(hd);
               JOptionPane.showMessageDialog(
                       addEvents.this,
                       "Add successfully "+hd.size()+" records",
                       "Message", JOptionPane.INFORMATION_MESSAGE);
           }catch (Exception i) {
               JOptionPane.showMessageDialog(addEvents.this,"Insert Exception!",
                       "Error", JOptionPane.INFORMATION_MESSAGE);
               i.printStackTrace();
           }finally {
               session.commit();   //commit insert
               session.close();
           }
       }
       else
           JOptionPane.showMessageDialog(addEvents.this,"Please choose a data source.",
                   "Message", JOptionPane.INFORMATION_MESSAGE);
    }


    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        dialogPane = new JPanel();
        contentPanel = new JPanel();
        label1 = new JLabel();
        textField1 = new JTextField();
        button1 = new JButton();
        button2 = new JButton();
        buttonBar = new JPanel();
        okButton = new JButton();
        cancelButton = new JButton();

        //======== this ========
        setMinimumSize(new Dimension(800, 60));
        setTitle("Choose data source and add events");
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        //======== dialogPane ========
        {
            dialogPane.setBorder(new EmptyBorder(12, 12, 12, 12));
            dialogPane.setLayout(new BorderLayout());

            //======== contentPanel ========
            {
                contentPanel.setLayout(new GridBagLayout());
                ((GridBagLayout)contentPanel.getLayout()).columnWidths = new int[] {0, 0, 0, 0, 0, 0, 0};
                ((GridBagLayout)contentPanel.getLayout()).rowHeights = new int[] {0, 0, 0, 0};
                ((GridBagLayout)contentPanel.getLayout()).columnWeights = new double[] {1.0, 0.0, 1.0, 0.0, 0.0, 1.0, 1.0E-4};
                ((GridBagLayout)contentPanel.getLayout()).rowWeights = new double[] {0.0, 0.0, 0.0, 1.0E-4};

                //---- label1 ----
                label1.setText("Data Source");
                contentPanel.add(label1, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 5, 5), 0, 0));
                contentPanel.add(textField1, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 5, 5), 0, 0));

                //---- button1 ----
                button1.setText("Choose Data Source");
                button1.addActionListener(e -> button1ActionPerformed(e));
                contentPanel.add(button1, new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 5, 5), 0, 0));

                //---- button2 ----
                button2.setText("Template");
                button2.addActionListener(e -> button2ActionPerformed(e));
                contentPanel.add(button2, new GridBagConstraints(4, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 5, 5), 0, 0));
            }
            dialogPane.add(contentPanel, BorderLayout.CENTER);

            //======== buttonBar ========
            {
                buttonBar.setBorder(new EmptyBorder(12, 0, 0, 0));
                buttonBar.setLayout(new GridBagLayout());
                ((GridBagLayout)buttonBar.getLayout()).columnWidths = new int[] {0, 85, 80};
                ((GridBagLayout)buttonBar.getLayout()).columnWeights = new double[] {1.0, 0.0, 0.0};

                //---- okButton ----
                okButton.setText("OK");
                okButton.addActionListener(e -> okButtonActionPerformed(e));
                buttonBar.add(okButton, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 0, 5), 0, 0));

                //---- cancelButton ----
                cancelButton.setText("Cancel");
                buttonBar.add(cancelButton, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 0, 0), 0, 0));
            }
            dialogPane.add(buttonBar, BorderLayout.SOUTH);
        }
        contentPane.add(dialogPane, BorderLayout.CENTER);
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private JPanel dialogPane;
    private JPanel contentPanel;
    private JLabel label1;
    private JTextField textField1;
    private JButton button1;
    private JButton button2;
    private JPanel buttonBar;
    private JButton okButton;
    private JButton cancelButton;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
