/*
 * Created by JFormDesigner on Thu Mar 28 11:27:01 CST 2019
 */

package main;

import java.awt.event.*;

import bean.*;
import dao.EventMapper;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;

/**
 * @author Brainrain
 * UpdateEvent class ofer function of updating and deletion
 */
public class UpdateEvent extends JFrame {
    private DefaultTableModel tableModel;
    private Object[][] data;    //exclude key(eventid)
    private Object[] dataColum; //exclude key(eventid)
    private String[] tableColum = new String[]{"attribute","value"};
    private Object[][] tabelData;
    private String searchId;
    HashMap<String,Object> valueMap = new HashMap<String, Object>();

    public UpdateEvent() {
        initComponents();
    }

//    ===== Update Action=====

    public void searchUpdateEvent(String eventid){
        String resource = "config/Configure.xml";
        InputStream inputStream = null;
        try {
            inputStream = Resources.getResourceAsStream(resource);
        } catch (IOException e) {
            e.printStackTrace();
        }
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        //SqlSession cannot be shared, you must create new object when you use it every time
        SqlSession session = sqlSessionFactory.openSession();
        try{
            EventMapper mapper = session.getMapper(EventMapper.class);
            ArrayList<EventAttribute> attributes = mapper.getAttributes();
            TerrorEvent oneEvent = mapper.selectByPrimaryKey(eventid);
            if(oneEvent == null){
                JOptionPane.showMessageDialog(UpdateEvent.this,"Sorry,no results,try search again.","Message",
                        JOptionPane.INFORMATION_MESSAGE);
            }
            else{
                dataColum = new String[attributes.size()-1];
                data = new Object[1][dataColum.length];
                //get database data and column into array,the eventid into HashMap
                for(int i=1;i<attributes.size();i++){
                    dataColum[i-1] = attributes.get(i).getAttribute_name();
                    data[0][i-1] = getEachEventData.getData(oneEvent)[i];
                }
            }
        }finally {
            session.close();
        }
    }

    //transpose table
    public void processSearchValue(){
        tabelData = new Object[dataColum.length][2];
        for(int i=0;i<dataColum.length;i++){
            tabelData[i][0] = dataColum[i];
            tabelData[i][1] = data[0][i];
        }
    }

    private void button1ActionPerformed(ActionEvent e){
        // TODO add your code here
        if(e.getSource() == button1){
            this.searchId = textField1.getText();
            if(searchId.equals("")){
                JOptionPane.showMessageDialog(UpdateEvent.this,"Please input event ID.","Message",
                        JOptionPane.INFORMATION_MESSAGE);
            }
            else {
                searchUpdateEvent(this.searchId);
                processSearchValue();
                tableModel = new DefaultTableModel(tabelData,tableColum);
                table1.setModel(tableModel);
            }
        }
    }

    private void okButtonActionPerformed(ActionEvent e) {
            // TODO add your code here
            //after editing,commit update
            valueMap.clear();
            table1.validate();table1.updateUI();
            valueMap.put("oldId",this.searchId);
            for(int row=0;row<dataColum.length;row++){
                valueMap.put(dataColum[row].toString(),tableModel.getValueAt(row,1));
            }
            UpdateTo obj = null;
            try {
                obj = UpdateTo.class.newInstance();
            } catch (InstantiationException e1) {
                e1.printStackTrace();
            } catch (IllegalAccessException e1) {
                e1.printStackTrace();
            }
            try {
                BeanUtils.populate(obj,valueMap);
            } catch (IllegalAccessException e1) {
                e1.printStackTrace();
            } catch (InvocationTargetException e1) {
                e1.printStackTrace();
            }
            String resource = "config/Configure.xml";
            InputStream inputStream = null;
            try {
                inputStream = Resources.getResourceAsStream(resource);
            } catch (IOException st) {
                st.printStackTrace();
            }
            SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
            //SqlSession cannot be shared, you must create new object when you use it every time
            SqlSession session = sqlSessionFactory.openSession();
            try{
                EventMapper mapper = session.getMapper(EventMapper.class);
                mapper.updateByPrimaryKey(obj);
                System.out.println(obj.getIyear());
                session.commit();
                JOptionPane.showMessageDialog(UpdateEvent.this,"Update successful.","Message",
                        JOptionPane.INFORMATION_MESSAGE);
            }finally {
                session.close();
            }
    }
//    =====End Update Action=====

//    =====Delete Action=====
    private void button2ActionPerformed(ActionEvent e) {
        // TODO add your code here
        int n = JOptionPane.showConfirmDialog(UpdateEvent.this,"Do you want to delete the data? " +
                "Please be carefully!","Confirmation",JOptionPane.YES_NO_OPTION);
        if(n==JOptionPane.YES_OPTION){
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
            try{
                EventMapper mapper = session.getMapper(EventMapper.class);
                mapper.deleteByPrimaryKey(this.searchId);
                session.commit();
                JOptionPane.showMessageDialog(UpdateEvent.this,"Delete successfully. ",
                        "Message",JOptionPane.INFORMATION_MESSAGE);
            }finally {
                session.close();
            }
        }
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        dialogPane = new JPanel();
        scrollPane1 = new JScrollPane();
        table1 = new JTable();
        contentPanel = new JPanel();
        label1 = new JLabel();
        textField1 = new JTextField();
        button1 = new JButton();
        buttonBar = new JPanel();
        okButton = new JButton();
        button2 = new JButton();
        cancelButton = new JButton();

        //======== this ========
        setTitle("Edit");
        Container contentPane = getContentPane();
        contentPane.setLayout(new GridBagLayout());
        ((GridBagLayout)contentPane.getLayout()).columnWidths = new int[] {0, 0};
        ((GridBagLayout)contentPane.getLayout()).rowHeights = new int[] {0, 0, 0};
        ((GridBagLayout)contentPane.getLayout()).columnWeights = new double[] {1.0, 1.0E-4};
        ((GridBagLayout)contentPane.getLayout()).rowWeights = new double[] {1.0, 0.0, 1.0E-4};

        //======== dialogPane ========
        {
            dialogPane.setBorder(new EmptyBorder(12, 12, 12, 12));
            dialogPane.setLayout(new BorderLayout());

            //======== scrollPane1 ========
            {

                //---- table1 ----
                table1.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
                table1.setRowHeight(50);
                table1.setSelectionForeground(Color.black);
                table1.setSelectionBackground(Color.white);
                scrollPane1.setViewportView(table1);
            }
            dialogPane.add(scrollPane1, BorderLayout.CENTER);

            //======== contentPanel ========
            {
                contentPanel.setBorder(new TitledBorder("Please input event ID you want to edit"));
                contentPanel.setLayout(new GridBagLayout());
                ((GridBagLayout)contentPanel.getLayout()).columnWidths = new int[] {0, 219, 0, 0};
                ((GridBagLayout)contentPanel.getLayout()).rowHeights = new int[] {0, 0, 0, 0};
                ((GridBagLayout)contentPanel.getLayout()).columnWeights = new double[] {0.0, 0.0, 0.0, 1.0E-4};
                ((GridBagLayout)contentPanel.getLayout()).rowWeights = new double[] {0.0, 0.0, 0.0, 1.0E-4};

                //---- label1 ----
                label1.setText("eventid");
                contentPanel.add(label1, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 5, 5), 0, 0));
                contentPanel.add(textField1, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 5, 5), 0, 0));

                //---- button1 ----
                button1.setText("Search");
                button1.addActionListener(e -> button1ActionPerformed(e));
                contentPanel.add(button1, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 5, 0), 0, 0));
            }
            dialogPane.add(contentPanel, BorderLayout.PAGE_START);

            //======== buttonBar ========
            {
                buttonBar.setBorder(new EmptyBorder(12, 0, 0, 0));
                buttonBar.setLayout(new GridBagLayout());
                ((GridBagLayout)buttonBar.getLayout()).columnWidths = new int[] {0, 85, 83, 80};
                ((GridBagLayout)buttonBar.getLayout()).columnWeights = new double[] {1.0, 0.0, 0.0, 0.0};

                //---- okButton ----
                okButton.setText("Update");
                okButton.addActionListener(e -> okButtonActionPerformed(e));
                buttonBar.add(okButton, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 0, 5), 0, 0));

                //---- button2 ----
                button2.setText("Delete");
                button2.addActionListener(e -> button2ActionPerformed(e));
                buttonBar.add(button2, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 0, 5), 0, 0));

                //---- cancelButton ----
                cancelButton.setText("Cancel");
                buttonBar.add(cancelButton, new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 0, 0), 0, 0));
            }
            dialogPane.add(buttonBar, BorderLayout.PAGE_END);
        }
        contentPane.add(dialogPane, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets(0, 0, 0, 0), 0, 0));
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private JPanel dialogPane;
    private JScrollPane scrollPane1;
    private JTable table1;
    private JPanel contentPanel;
    private JLabel label1;
    private JTextField textField1;
    private JButton button1;
    private JPanel buttonBar;
    private JButton okButton;
    private JButton button2;
    private JButton cancelButton;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
