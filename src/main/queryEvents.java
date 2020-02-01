/*
 * Created by JFormDesigner on Mon Mar 18 19:32:03 CST 2019
 */

package main;

import bean.EventAttribute;
import bean.QueryTo;
import bean.TerrorEvent;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * @author Brainrain
 */
public class queryEvents extends JFrame {
    private ResultSetTableModel tableModel; // Default
    private ResultSetTableModel tableMode2; //When submit Search
    private QueryTo search;
    private String[] country = {"null"};
    private ArrayList<String> region = new ArrayList<String>(){{
        add("North America");
        add("Central America & Caribbean");
        add("South America");
        add("East Asia");
        add("Southeast Asia");
        add("South Asia");
        add("Central Asia");
        add("Western Europe");
        add("Eastern Europe");
        add("Middle East & North Africa");
        add("Sub-Saharan Africa");
        add("Australasia & Oceania");
    }};

    public queryEvents() {
        try {
            //Where the GUI is constructed:
            tableModel = new ResultSetTableModel();
            initComponents();
            comboBox1.addItemListener(i -> comboBox1ItemStateChanged(i));
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void comboBox1ItemStateChanged(ItemEvent i) {
        tableMode2.setSearchQuery(search,getInputValue(i.getItem()),28);
        table1.validate();table1.updateUI();
    }

    //read file to String array
    private String[] getFileContent(String source){
        ArrayList<String> text = new ArrayList<String>();
        try (FileReader reader = new FileReader(source);
             BufferedReader br = new BufferedReader(reader)
        ){
            String line;
            while ((line = br.readLine()) != null) {
                text.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        //ArrayList transfer to Array
        String[] strs = new String[text.size()];
        return text.toArray(strs);
    }

    private void comboBox12ItemStateChanged(ItemEvent e) {
        // TODO add your code here
        if(region.contains(e.getItem())){
            country = getFileContent(
                    "F:\\论文\\2019-2020毕业论文\\projectDM\\src\\config\\input\\country_txt\\R"+
                            (region.indexOf(e.getItem())+1));
        }
        else if(e.getItem().equals("null")){
            country = new String[]{"null"};
        }
        //======================IMPORTANT===========================
        comboBox6.validate();comboBox6.updateUI();
        comboBox6.setModel(new DefaultComboBoxModel<>(country));
    }
    //submit query sql
    private void button1ActionPerformed(ActionEvent e){
        // TODO add your code here
        if(e.getSource() == button1){
            try {
                this.search = new QueryTo();
                this.search.setEventid(textField1.getText());
                if(textField2.getText().equals(""))
                    this.search.setIDX(Integer.valueOf(0));
                else
                    this.search.setIDX(Integer.valueOf(textField2.getText()));
                this.search.setIyear(getInputValue(comboBox3.getSelectedItem()));
                this.search.setImonth(getInputValue(comboBox4.getSelectedItem()));
                this.search.setIday(getInputValue(comboBox5.getSelectedItem()));
                Integer min = getInputValue(spinner2.getValue());
                Integer max = getInputValue(spinner1.getValue());
                if( min > max && min==0 && max==0){
                    this.search.setNkillMin(0);
                    this.search.setNkillMax(0);
                }else{
                    this.search.setNkillMin(min);
                    this.search.setNkillMax(max);
                }
                this.search.setRegion_txt(comboBox12.getSelectedItem().toString());
                this.search.setCountry_txt(comboBox6.getSelectedItem().toString());
                this.search.setTargtype(comboBox10.getSelectedItem().toString());
                this.search.setAttacktype(comboBox9.getSelectedItem().toString());
                this.search.setGname(comboBox11.getSelectedItem().toString());
                tableMode2 = new ResultSetTableModel(search);
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            //return query result
            if(tableMode2.getPagenum() == 0) {
                //dialog
                JOptionPane.showMessageDialog(queryEvents.this,"Sorry,no results,try search again.",
                        "Search Result Message",JOptionPane.INFORMATION_MESSAGE);
                label16.setText("0");
            }
            else{
                comboBox1.setModel(new DefaultComboBoxModel<>(tableMode2.getPages()));
                label16.setText(String.valueOf(tableMode2.getPagenum()));
                table1.setAutoCreateColumnsFromModel(true);
                if(table1.getAutoCreateColumnsFromModel()){
                    table1.setModel(tableMode2);
                }
            }
        }
    }

    //input type and process null fileds
    private Integer getInputValue(Object value){
        if(value.toString().equals( "null")){
            return -1;
        }
        else return Integer.valueOf(value.toString());
    }

    private void createUIComponents() {
        // TODO: add custom component creation code here
    }

    private void button2ActionPerformed(ActionEvent e) {
        // TODO add your code here
        // first page
        if(e.getSource()==button2 && tableMode2!=null){
            tableMode2.setSearchQuery(search,1,28);
            table1.validate();table1.updateUI();
            comboBox1.setSelectedItem(1);
        }
    }

    private void button5ActionPerformed(ActionEvent e) {
        // TODO add your code here
        //  last page
        if(e.getSource()==button5 && tableMode2!=null){
            tableMode2.setSearchQuery(search,tableMode2.getPages().length,28);
            table1.validate();table1.updateUI();
            comboBox1.setSelectedItem(tableMode2.getPages().length);
        }
    }

    private void button3ActionPerformed(ActionEvent e) {
        // TODO add your code here
        //  previous page
        if(e.getSource()==button3 && tableMode2!=null && tableMode2.getPrePage()!=0){
            int pre = tableMode2.getPrePage();
            tableMode2.setSearchQuery(search,pre,28);
            table1.validate();table1.updateUI();
            comboBox1.setSelectedItem(pre);
        }
    }

    private void button4ActionPerformed(ActionEvent e) {
        // TODO add your code here
        //  next page
        if(e.getSource()==button4 && tableMode2!=null && tableMode2.getNextPage()!=0){
            int np = tableMode2.getNextPage();
            tableMode2.setSearchQuery(search,np,28);
            table1.validate();table1.updateUI();
            comboBox1.setSelectedItem(np);
        }
    }

    private void initComponents() throws SQLException, FileNotFoundException {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        scrollPane1 = new JScrollPane();
        panel1 = new JPanel();
        label1 = new JLabel();
        textField1 = new JTextField();
        label2 = new JLabel();
        comboBox3 = new JComboBox<>();
        label7 = new JLabel();
        comboBox4 = new JComboBox<>();
        label8 = new JLabel();
        comboBox5 = new JComboBox<>();
        label9 = new JLabel();
        textField2 = new JTextField();
        label5 = new JLabel();
        comboBox12 = new JComboBox<>();
        label3 = new JLabel();
        comboBox6 = new JComboBox<>();
        label6 = new JLabel();
        comboBox10 = new JComboBox<>();
        label13 = new JLabel();
        panel2 = new JPanel();
        label14 = new JLabel();
        spinner2 = new JSpinner();
        label15 = new JLabel();
        spinner1 = new JSpinner();
        label11 = new JLabel();
        comboBox11 = new JComboBox<>();
        label4 = new JLabel();
        comboBox9 = new JComboBox<>();
        panel3 = new JPanel();
        button1 = new JButton();
        scrollPane2 = new JScrollPane();
        table1 = new JTable();
        panel4 = new JPanel();
        button2 = new JButton();
        button3 = new JButton();
        comboBox1 = new JComboBox();
        button4 = new JButton();
        button5 = new JButton();
        panel5 = new JPanel();
        label12 = new JLabel();
        label16 = new JLabel();

        //======== this ========
        setTitle("Querying From GTD");
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        //======== scrollPane1 ========
        {

            //======== panel1 ========
            {
                panel1.setBorder(new TitledBorder("Search Panel"));
                panel1.setFont(panel1.getFont().deriveFont(panel1.getFont().getSize() + 2f));
                panel1.setLayout(new GridBagLayout());
                ((GridBagLayout)panel1.getLayout()).columnWidths = new int[] {0, 0, 0, 0, 0, 0, 0, 0, 0};
                ((GridBagLayout)panel1.getLayout()).rowHeights = new int[] {0, 0, 0, 0, 0};
                ((GridBagLayout)panel1.getLayout()).columnWeights = new double[] {0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0E-4};
                ((GridBagLayout)panel1.getLayout()).rowWeights = new double[] {0.0, 0.0, 0.0, 0.0, 1.0E-4};

                //---- label1 ----
                label1.setText("Eventid");
                panel1.add(label1, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.EAST, GridBagConstraints.VERTICAL,
                    new Insets(0, 0, 5, 5), 0, 0));
                panel1.add(textField1, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 5, 5), 0, 0));

                //---- label2 ----
                label2.setText("Year");
                panel1.add(label2, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.EAST, GridBagConstraints.VERTICAL,
                    new Insets(0, 0, 5, 5), 0, 0));

                //---- comboBox3 ----
                comboBox3.setModel(new DefaultComboBoxModel<>(new String[] {
                    "null",
                    "1997",
                    "1998",
                    "1999",
                    "2000",
                    "2001",
                    "2002",
                    "2003",
                    "2004",
                    "2005",
                    "2006",
                    "2007",
                    "2008",
                    "2009",
                    "2010",
                    "2011",
                    "2012",
                    "2013",
                    "2014",
                    "2015",
                    "2016",
                    "2017",
                    "2018",
                    "2019"
                }));
                comboBox3.setBackground(Color.white);
                panel1.add(comboBox3, new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 5, 5), 0, 0));

                //---- label7 ----
                label7.setText("Month");
                panel1.add(label7, new GridBagConstraints(4, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.EAST, GridBagConstraints.VERTICAL,
                    new Insets(0, 0, 5, 5), 0, 0));

                //---- comboBox4 ----
                comboBox4.setModel(new DefaultComboBoxModel<>(new String[] {
                    "null",
                    "0",
                    "1",
                    "2",
                    "3",
                    "4",
                    "5",
                    "6",
                    "7",
                    "8",
                    "9",
                    "10",
                    "11",
                    "12"
                }));
                comboBox4.setBackground(Color.white);
                panel1.add(comboBox4, new GridBagConstraints(5, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 5, 5), 0, 0));

                //---- label8 ----
                label8.setText("Day");
                panel1.add(label8, new GridBagConstraints(6, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.EAST, GridBagConstraints.VERTICAL,
                    new Insets(0, 0, 5, 5), 0, 0));

                //---- comboBox5 ----
                comboBox5.setModel(new DefaultComboBoxModel<>(new String[] {
                    "null",
                    "0",
                    "1",
                    "2",
                    "3",
                    "4",
                    "5",
                    "6",
                    "7",
                    "8",
                    "9",
                    "10",
                    "11",
                    "12",
                    "13",
                    "14",
                    "15",
                    "16",
                    "17",
                    "18",
                    "19",
                    "20",
                    "21",
                    "22",
                    "23",
                    "24",
                    "25",
                    "26",
                    "27",
                    "28",
                    "29",
                    "30",
                    "31"
                }));
                comboBox5.setBackground(Color.white);
                panel1.add(comboBox5, new GridBagConstraints(7, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 5, 0), 0, 0));

                //---- label9 ----
                label9.setText("IDX");
                panel1.add(label9, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
                    GridBagConstraints.EAST, GridBagConstraints.VERTICAL,
                    new Insets(0, 0, 5, 5), 0, 0));
                panel1.add(textField2, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 5, 5), 0, 0));

                //---- label5 ----
                label5.setText("Region");
                panel1.add(label5, new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0,
                    GridBagConstraints.EAST, GridBagConstraints.VERTICAL,
                    new Insets(0, 0, 5, 5), 0, 0));

                //---- comboBox12 ----
                comboBox12.setModel(new DefaultComboBoxModel<>(new String[] {
                    "null",
                    "North America",
                    "Central America & Caribbean",
                    "South America",
                    "East Asia",
                    "Southeast Asia",
                    "South Asia",
                    "Central Asia",
                    "Western Europe",
                    "Eastern Europe",
                    "Middle East & North Africa",
                    "Sub-Saharan Africa",
                    "Australasia & Oceania"
                }));
                comboBox12.setBackground(Color.white);
                comboBox12.addItemListener(e -> comboBox12ItemStateChanged(e));
                panel1.add(comboBox12, new GridBagConstraints(3, 1, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 5, 5), 0, 0));

                //---- label3 ----
                label3.setText("Country");
                panel1.add(label3, new GridBagConstraints(4, 1, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 5, 5), 0, 0));

                //---- comboBox6 ----
                comboBox6.setModel(new DefaultComboBoxModel<>(new String[] {
                    "null",
                    "Canada",
                    "Mexico",
                    "United States"
                }));
                comboBox6.setBackground(Color.white);
                panel1.add(comboBox6, new GridBagConstraints(5, 1, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 5, 5), 0, 0));

                //---- label6 ----
                label6.setText("Target type");
                panel1.add(label6, new GridBagConstraints(6, 1, 1, 1, 0.0, 0.0,
                    GridBagConstraints.EAST, GridBagConstraints.VERTICAL,
                    new Insets(0, 0, 5, 5), 0, 0));

                //---- comboBox10 ----
                comboBox10.setBackground(Color.white);
                comboBox10.setModel(new DefaultComboBoxModel<>(new String[] {
                    "null",
                    "Business",
                    "Government (General)",
                    "Police",
                    "Military",
                    "Abortion Related",
                    "Airports & Aircraft",
                    "Government (Diplomatic)",
                    "Educational Institution",
                    "Food Or Water Supply",
                    "Journalists & Media",
                    "Maritime (Includes Ports And Maritime Facilities)",
                    "Ngo",
                    "Other",
                    "Private Citizens & Property",
                    "Religious Figures/Institutions",
                    "Telecommunication",
                    "Terrorists/Non-State Militias",
                    "Tourists",
                    "Transportation (Other Than Aviation)",
                    "Unknown",
                    "Utilities",
                    "Violent Political Parties"
                }));
                panel1.add(comboBox10, new GridBagConstraints(7, 1, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 5, 0), 0, 0));

                //---- label13 ----
                label13.setText("kill numbers");
                panel1.add(label13, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0,
                    GridBagConstraints.EAST, GridBagConstraints.VERTICAL,
                    new Insets(0, 0, 5, 5), 0, 0));

                //======== panel2 ========
                {
                    panel2.setLayout(new GridBagLayout());
                    ((GridBagLayout)panel2.getLayout()).columnWidths = new int[] {0, 0, 0, 0, 0};
                    ((GridBagLayout)panel2.getLayout()).rowHeights = new int[] {0, 0};
                    ((GridBagLayout)panel2.getLayout()).columnWeights = new double[] {1.0, 1.0, 1.0, 1.0, 1.0E-4};
                    ((GridBagLayout)panel2.getLayout()).rowWeights = new double[] {0.0, 1.0E-4};

                    //---- label14 ----
                    label14.setText(">=");
                    panel2.add(label14, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                        GridBagConstraints.EAST, GridBagConstraints.VERTICAL,
                        new Insets(0, 0, 0, 5), 0, 0));

                    //---- spinner2 ----
                    spinner2.setModel(new SpinnerNumberModel(0, 0, null, 1));
                    panel2.add(spinner2, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 0, 0, 5), 0, 0));

                    //---- label15 ----
                    label15.setText("<=");
                    panel2.add(label15, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0,
                        GridBagConstraints.EAST, GridBagConstraints.VERTICAL,
                        new Insets(0, 0, 0, 5), 0, 0));

                    //---- spinner1 ----
                    spinner1.setModel(new SpinnerNumberModel(0, 0, null, 1));
                    panel2.add(spinner1, new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 0, 0, 0), 0, 0));
                }
                panel1.add(panel2, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 5, 5), 0, 0));

                //---- label11 ----
                label11.setText("group name");
                panel1.add(label11, new GridBagConstraints(2, 2, 1, 1, 0.0, 0.0,
                    GridBagConstraints.EAST, GridBagConstraints.VERTICAL,
                    new Insets(0, 0, 5, 5), 0, 0));

                //---- comboBox11 ----
                comboBox11.setBackground(Color.white);
                comboBox11.setModel(new DefaultComboBoxModel<>(new String[] {
                    "null",
                    "Unknown",
                    "14 K Triad",
                    "14 March Coalition",
                    "1920 Revolution Brigades",
                    "23 May Democratic Alliance (Algeria)",
                    "28s",
                    "313 Brigade (Syria)",
                    "A'chik Matgrik Elite Force (AMEF)",
                    "Aba Cheali Group",
                    "Abbala extremists",
                    "Abdul Ghani Kikli Militia",
                    "Abdul Qader Husseini Battalions of the Free Palestine movement",
                    "Abdullah Azzam Brigades",
                    "Abida Tribe",
                    "Abkhazian guerrillas",
                    "Abkhazian Separatists",
                    "Abu Abbas Brigade",
                    "Abu Amarah Battalion",
                    "Abu Bakr Unis Jabr Brigade",
                    "Abu Jaafar al-Mansur Brigades",
                    "Abu Nidal Organization (ANO)",
                    "Abu Obaida bin Jarrah Brigade",
                    "Abu Salim Martyr's Brigade",
                    "Abu Sayyaf Group (ASG)",
                    "Abu Tira (Central Reserve Forces)",
                    "Aceh Singkil Islamic Care Youth Students Association (PPI)",
                    "Achik Matgrik Army (AMA)",
                    "Achik National Cooperative Army (ANCA)",
                    "Achik National Liberation Army (ANLA)",
                    "Achik National Volunteer Council (ANVC)",
                    "Achik National Volunteer Council-B (ANVC-B)",
                    "Achik Songna An'pachakgipa Kotok (ASAK)",
                    "Achik Tiger Force",
                    "Adan Abyan Islamic Army (AAIA)",
                    "Adan-Abyan Province of the Islamic State",
                    "Adivasi Cobra Militants of Assam (ACMA)",
                    "Adivasi National Liberation Army (ANLA)",
                    "Adivasi People's Army (APA)",
                    "Afar Revolutionary Democratic Unity Front",
                    "Afghan Revolutionary Front",
                    "Afghans",
                    "Africa Marine Commando",
                    "Agwelek Forces",
                    "Ahfad al-Sahaba-Aknaf Bayt al-Maqdis",
                    "Ahle Sunnat Wal Jamaat (ASWJ-Pakistan)",
                    "Ahlu-sunah Wal-jamea (Somalia)",
                    "Ahmad Luebaesa Group",
                    "Ahneish Militia",
                    "Ahrar Al-Jalil (Free People of the Galilee)",
                    "Ahrar al-Sham",
                    "Aibed Er-Rahman katibet",
                    "Aisha Umm-al Mouemeneen (Brigades of Aisha)",
                    "Aitarak Militia",
                    "Ajnad al-Sham",
                    "Ajnad Misr",
                    "Akhil Terai Mukti Morcha (ATMM)",
                    "Akhilesh Singh Gang",
                    "Al Bayda Province of the Islamic State",
                    "Al Zawahiri Loyalists",
                    "Al-Ahwaz Arab People's Democratic Front",
                    "Al-Aqsa Martyrs Brigade",
                    "Al-Arifeen",
                    "Al-Ashtar Brigades",
                    "Al-Badr",
                    "Al-Bakazim",
                    "Al-Fajr",
                    "Al-Faruq Militia",
                    "Al-Fatah",
                    "Al-Fateh Al-Jadid",
                    "Al-Fatihin Army (AFA)",
                    "Al-Furqan (Tunisia)",
                    "Al-Furqan Brigades",
                    "Al-Gama'at al-Islamiyya (IG)",
                    "Al-Hamas Mujahideen",
                    "Al-Haramayn Brigades",
                    "Al-Haydariyah Battalion",
                    "Al-Herak Al-Tihami Movement",
                    "Al-Intiqami al-Pakistani",
                    "Al-Islah Party",
                    "Al-Ittihaad al-Islami (AIAI)",
                    "Al-Jihad (Pakistan)",
                    "Al-Jub Tribe",
                    "Al-Khobar",
                    "Al-Ma'unah",
                    "Al-Madani Brigade",
                    "Al-Madina",
                    "Al-Mansoorian",
                    "Al-Mua'qi'oon Biddam Brigade (Those who Sign with Blood)",
                    "Al-Mujahedin Brigades (Palestine)",
                    "Al-Mus'abi Tribe",
                    "Al-Naqshabandiya Army",
                    "Al-Nasir Army (Syria)",
                    "Al-Nasireen Group",
                    "Al-Nasirin (India)",
                    "Al-Nawaz",
                    "Al-Nusrah Front",
                    "Al-Qaida",
                    "Al-Qaida in Iraq",
                    "Al-Qaida in Lebanon",
                    "Al-Qaida in Saudi Arabia",
                    "Al-Qaida in the Arabian Peninsula (AQAP)",
                    "Al-Qaida in the Indian Subcontinent",
                    "Al-Qaida in the Islamic Maghreb (AQIM)",
                    "Al-Qaida in Yemen",
                    "Al-Qaida Kurdish Battalions (AQKB)",
                    "Al-Qaida Network for Southwestern Khulna Division",
                    "Al-Qaida Organization for Jihad in Sweden",
                    "Al-Qaqa Brigade",
                    "Al-Saadawi Militia",
                    "Al-Sawaiq Brigade",
                    "Al-Shabaab",
                    "Al-Shabaab al-Mu'minin",
                    "Al-Sham Legion",
                    "Al-Shuda Brigade",
                    "Al-Sunna wal Jamma",
                    "Al-Toaiman Tribesmen",
                    "Al-Umar Mujahideen",
                    "Al-Ummah",
                    "Al-Yakin Mujahidin",
                    "Al-Zaidi Tribe - Mareb",
                    "Al-Zawiya Syndicates Council",
                    "Al-Zintan Revolutionaries' Military Council",
                    "Albanian extremists",
                    "Albanian National Army (ANA)",
                    "Albanian Separatists",
                    "Alcubar group",
                    "Alde Hemendik Movement",
                    "Aleppo Fatah Operations Room",
                    "Alex Boncayao Brigade (ABB)",
                    "Alexandros Grigoropoulos Anarchist Attack Group",
                    "Algeria Province of the Islamic State",
                    "Algerian Islamic Extremists",
                    "Algerian Moslem Fundamentalists",
                    "All Assam Revolutionary Army (AARA)",
                    "All Burma Students' Democratic Front (ABSDF)",
                    "All Ethiopian Unity Party (AEUP)",
                    "All India Anna Dravida Munetra Kazgan Party",
                    "All Kamatapur Liberation Force",
                    "All Nepal National Free Student Union-Revolutionary",
                    "All Tripura Tiger Force (ATTF)",
                    "Alliance of Patriots for a Free and Sovereign Congo (APCLS)",
                    "Allied Democratic Forces (ADF)",
                    "Allied Democratic Forces of Guinea (RDFG)",
                    "Amal",
                    "Amazigh Islamic Front",
                    "Ambazonia Defense Forces (ADF)",
                    "Amr Bil Maroof Wa Nahi Anil Munkir",
                    "Anarchist Action (CA / United States)",
                    "Anarchist Anti-Capitalist Action Group",
                    "Anarchist Attack Consortium",
                    "Anarchist Cell Acca (C.A.A.)",
                    "Anarchist Collective of Kallithea-Moschato",
                    "Anarchist Commando Nestor Makhno Group",
                    "Anarchist Faction",
                    "Anarchist Liberation Brigade",
                    "Anarchist Revolt Against Exiled Gendarmes",
                    "Anarchist Squad",
                    "Anarchist Struggle",
                    "Anarchists",
                    "Anarchists Attack Team",
                    "Anarkista Jorge Banos Front of the Everyone For The Homeland Movement (MTP)",
                    "Anas al-Dabbashi Brigade",
                    "Anbar Salvation Council",
                    "Andres Castro United Front",
                    "Angola Rebels",
                    "Angry Brigade",
                    "Angry Brigade (Italy)",
                    "Angry Foxes Cell",
                    "Aniban ng Ayaw sa Komunista (ANAK)\u00a0",
                    "Animal Liberation Front (ALF)",
                    "Animal Rights extremists",
                    "Animal Rights Militia",
                    "Anonymous Underground Movement (MCA)",
                    "Ansar Al Sunnah (Palestine)",
                    "Ansar al-Din",
                    "Ansar al-Din Front",
                    "Ansar al-Dine (Mali)",
                    "Ansar al-Furqan",
                    "Ansar al-Imam Musa al-Sadr",
                    "Ansar al-Islam",
                    "Ansar al-Islam (Burkina Faso)",
                    "Ansar al-Islam (Egypt)",
                    "Ansar al-Jihad",
                    "Ansar Al-Khilafa (Philippines)",
                    "Ansar al-Sharia (Libya)",
                    "Ansar al-Sharia (Pakistan)",
                    "Ansar al-Sharia (Tunisia)",
                    "Ansar al-Sharia Operations Room (Syria)",
                    "Ansar al-Sunna",
                    "Ansar al-Sunna (Mozambique)",
                    "Ansar al-Tahwid wal Sunna",
                    "Ansar Bayt al-Maqdis (Ansar Jerusalem)",
                    "Ansar Ghazwat-ul-Hind",
                    "Ansar Sarallah",
                    "Ansar Wa Mohajir (Pakistan)",
                    "Ansaru (Jama'atu Ansarul Muslimina Fi Biladis Sudan)",
                    "Ansaru ash-Sharia (Russia)",
                    "Ansarul Islam (Pakistan)",
                    "Ansarullah Bangla Team",
                    "Antagonistic Nuclei of the New Urban Guerrilla",
                    "Anti-Abortion extremists",
                    "Anti-Apostate Movement Alliance (AGAP)",
                    "Anti-Arab extremists",
                    "Anti-Balaka Militia",
                    "Anti-Capitalist Action",
                    "Anti-Christian extremists",
                    "Anti-Clerical Pro-Sex Toys Group",
                    "Anti-Communist Command (KAK)",
                    "Anti-Democratic Struggle",
                    "Anti-Environmentalists",
                    "Anti-Gentrification Front",
                    "Anti-Government extremists",
                    "Anti-Government Group",
                    "Anti-Government Guerrillas",
                    "Anti-Gun Control extremists",
                    "Anti-Immigrant extremists",
                    "Anti-Imperialist Commando",
                    "Anti-Imperialist Territorial Nuclei (NTA)",
                    "Anti-Independence extremists",
                    "Anti-Israeli extremists",
                    "Anti-Kim Jong-il extremists",
                    "Anti-LGBT extremists",
                    "Anti-Liberal extremists",
                    "Anti-Muslim extremists",
                    "Anti-Nuclear extremists",
                    "Anti-Park extremists",
                    "Anti-Police extremists",
                    "Anti-Racist Guerrilla Nuclei",
                    "Anti-Republican extremists",
                    "Anti-Semitic extremists",
                    "Anti-Sikh extremists",
                    "Anti-State Action",
                    "Anti-State Justice",
                    "Anti-State Proletarian Nuclei",
                    "Anti-Trump extremists",
                    "Anti-United States extremists",
                    "Anti-White extremists",
                    "Anti-Yanukovych extremists",
                    "Anti-Zionist Movement",
                    "Apella",
                    "Aqmur",
                    "Arab Movement of Azawad (MAA)",
                    "Arab People's Group",
                    "Arab Socialist Baath Party of Iraq",
                    "Arab-Israeli extremists",
                    "Arakan Army (AA)",
                    "Arakan Liberation Party (ALP)",
                    "Arakan Rohingya Salvation Army (ARSA)",
                    "Arauco Malleco Coordinating Group (CAM) - Chile",
                    "Arbav Martyrs of Khuzestan",
                    "Ariska Brodraskapet (Aryan Brotherhood)",
                    "Arm na Poblachta' (Army of the Republic)",
                    "Armata Corsa",
                    "Armata di Liberazione Naziunale (ALN)",
                    "Armed Forces for National Salvation - Army of the People (FASN-EP)",
                    "Armed Forces of the Chechen Republic of Ichkeria",
                    "Armed Forces Revolutionary Council (AFRC)",
                    "Armed Group for the Defence of the People",
                    "Armed Islamic Group (GIA)",
                    "Armed People",
                    "Armed Revolutionary Action (ENEDRA)",
                    "Armed Revolutionary Left (IRA)",
                    "Armed Vanguards of a Second Mohammed Army",
                    "Armenian extremists",
                    "Armenian nationalists",
                    "Army of God",
                    "Army of Islam",
                    "Army of State Liberators",
                    "Army of the Republic of Ilirida",
                    "Army of the Tribes",
                    "Arsonists for Social Cohesion",
                    "Aryan Nation",
                    "Asa'ib Ahl al-Haqq",
                    "Asbat al-Ansar",
                    "Association of Mobil Spill Affected Communities (AMSAC)",
                    "Association Totalement Anti-Guerre (ATAG)",
                    "Athens and Thessaloniki Arsonist Nuclei",
                    "Attack Teams for the Dissolution of the Nation (Greece)",
                    "Authenticity and Development Front",
                    "Autonomous Decorators",
                    "Avengers of the Infants",
                    "Awami League",
                    "Awdal Regional Administration Army (ARAA)",
                    "Ayesha bint al-Sadiq Brigade",
                    "Azawad National Liberation Movement (MNLA)",
                    "Baathist extremists",
                    "Bab Tajura Brigade",
                    "Baba Ladla Gang",
                    "Babbar Khalsa International (BKI)",
                    "Baby Liberation Army",
                    "Bachama extremists",
                    "Badr Brigades",
                    "Bahoz",
                    "Bahrain Province of the Islamic State",
                    "Balakhani Group",
                    "Baloch Liberation Army (BLA)",
                    "Baloch Liberation Front (BLF)",
                    "Baloch Liberation Tigers (BLT)",
                    "Baloch Militant Defense Army",
                    "Baloch Mussalah Diffah Tanzim (BMDT)",
                    "Baloch National Liberation Front",
                    "Baloch Nationalists",
                    "Baloch Republican Army (BRA)",
                    "Baloch Republican Guards (BRG)",
                    "Baloch Republican Party",
                    "Baloch Waja Liberation Army (BWLA)",
                    "Baloch Young Tigers (BYT)",
                    "Balochistan Liberation United Front (BLUF)",
                    "Balochistan National Army",
                    "Bandits",
                    "Bangalee Somo Odhikar Andolon",
                    "Bangladesh Nationalist Party (BNP)",
                    "Bangladesh Sarbahara Party",
                    "Bangladesh Tiger Force",
                    "Bangsamoro Islamic Freedom Movement (BIFM)",
                    "Bangsamoro National Liberation Army",
                    "Bani Jaber tribe",
                    "Banner of Islam",
                    "Banyamulenge rebels",
                    "Barak Valley Tiger Force (BVTF)",
                    "Barisan Revolusi Nasional (BRN)",
                    "Barq al-Nasser Brigade",
                    "Barqa Province of the Islamic State",
                    "Base Movement",
                    "Basque extremists",
                    "Basque Fatherland and Freedom (ETA)",
                    "Basque Separatists",
                    "Bedouin Israeli extremists",
                    "Bedouin Movement (Sudan)",
                    "Bedouin tribesmen",
                    "Beja Congress",
                    "Beli Orlovi (White Eagles)",
                    "Bengali Sangram Mukti Bahini",
                    "Bengali Tiger Force (BTF)",
                    "Benghazi Defense Brigades (BDB)",
                    "Benishangul Gumuz People's Liberation Movement",
                    "Berom Militants",
                    "Bersatu",
                    "Besi Merah Putih Militia",
                    "Bharatiya Janata Party",
                    "Bhisan Himali Bag",
                    "Bhittani tribe",
                    "Bhumi Uchched Pratirodh Committee (BUPC)",
                    "Biafra Zionist Movement (BZM)",
                    "Bilal Badr Group",
                    "Bini-Oru",
                    "Birsa Commando Force (BCF)",
                    "Biswabhumi Sena Bishal Nepal",
                    "Black and Red Anarchist and Anti-Authoritarians Initiative (Greece)",
                    "Black Eagles",
                    "Black Hawks (Anti-Wahhabists)",
                    "Black Hebrew Israelites",
                    "Black Star",
                    "Black Widows",
                    "Bodo Liberation Tigers (BLT)",
                    "Bodo People's Front (BPF)",
                    "Bodu Bala Sena",
                    "Boko Haram",
                    "Borana bandits",
                    "Borderless Solidarity Cell (BSC)",
                    "Breton Liberation Front (FLB)",
                    "Brigade of al-Mukhtar al-Thaqafi",
                    "Brigades of Iman Hassan-al-Basri",
                    "Brigades of Imprisoned Sheikh Omar Abdel-Rahman",
                    "Bru Democratic Front of Mizoram (BDFM)",
                    "Bru National Liberation Front (BNLF)",
                    "Buddhist Monks",
                    "Bunda Dia Kongo (BDK)",
                    "Cambodian Freedom Fighters (CFF)",
                    "Cannibal Army",
                    "Caspian Group",
                    "Catholic Reaction Force",
                    "Caucasus Emirate",
                    "Caucasus Province of the Islamic State",
                    "CCCCC",
                    "Cells of Direct Attack - Living Waste Group",
                    "Chakma tribal group",
                    "Chama Pueblo en Rebelion",
                    "Chechen Rebels",
                    "Children of November",
                    "Chin National Army",
                    "Christian extremists",
                    "Christian State-Holy Rus",
                    "Chukakuha (Middle Core Faction)",
                    "Chukakuha Revolutionary Army",
                    "Citizen's Rights Protection Volunteers",
                    "Citizens for Constitutional Freedom",
                    "Civic United Front (CUF)",
                    "Civil Association for Peace in Colombia, Asocipaz",
                    "Civil Defense Force (CDF)",
                    "Civil Protection Units (YPS)",
                    "Civil War Veterans",
                    "Civilian Defense",
                    "Civilian Joint Task Force (JTF)",
                    "Clandestini Corsi",
                    "Coalition for Unity and Democracy (CUD)",
                    "Coalition to Save the Preserves (CSP)",
                    "Colombian Patriotic Resistance",
                    "Colonel Albert Kahasha Militia",
                    "Colonel Karuna Faction",
                    "Comando Aut\u00f3nomo Voltaire Argando\u00f1a",
                    "Comando Malvinas Argentinas",
                    "Combat 18",
                    "Comite d'Action Viticole",
                    "Communist Party of India - Maoist (CPI-Maoist)",
                    "Communist Party of India- Marxist",
                    "Communist Party of India- Marxist-Leninist",
                    "Communist Party of Nepal - Maoist (CPN-Maoist-Chand)",
                    "Communist Party of Nepal (People's War Group)",
                    "Communist Party of Nepal- Maoist (CPN-M)",
                    "Communist Party of Nepal- Unified Marxist-Leninist (CPN-UML)",
                    "Communist Party of Nepal-Maoist (Baidya)",
                    "Communists",
                    "Concerned Militant Leaders (CML)",
                    "Congolese Democratic Coalition",
                    "Congolese Patriotic Resistence-Patriotic Armed Forces (Pareco/FAP)",
                    "Congolese Rally for Democracy (RCD)",
                    "Conqueror Army",
                    "Conscientious Arsonists (CA)",
                    "Conspiracy of Cells of Fire",
                    "Conspiracy of Vengeful Arsonists",
                    "Continuity Irish Republican Army (CIRA)",
                    "Convention of Patriots for Justice and Peace",
                    "Coordination Committee (CORCOM)",
                    "Coordination of Azawad Movements (CMA)",
                    "Core Conspirators for the Extension of Chaos (N\u00facleo de Conspiradores por la Extensi\u00f3n de Kaos)",
                    "Corsican National Liberation Front (FLNC)",
                    "Corsican National Liberation Front- Historic Channel",
                    "Corsican Nationalists",
                    "Corsican Separatists",
                    "Cossack Separatists",
                    "Council for Justice in Azawad",
                    "Council for the Destruction of Order",
                    "Court Reform extremists",
                    "Croat Democratic Union",
                    "Crypteia",
                    "CSS Movement",
                    "Cuban Exiles",
                    "Cyrenaica Self-Defense Force",
                    "Dagestani Shari'ah Jamaat",
                    "Dashmesh Regiment",
                    "David Yau Yau Militia",
                    "Dayak gang",
                    "Death Squad",
                    "Death Squad (Iraq)",
                    "Deccan Mujahideen",
                    "Defenders of the Nation's Sovereignty",
                    "Delta Democratic Militia",
                    "Democratic Front for the Liberation of Palestine (DFLP)",
                    "Democratic Front for the Liberation of Rwanda (FDLR)",
                    "Democratic Front of the Central African People (FDPC)",
                    "Democratic Iraqi Opposition of Germany",
                    "Democratic Karen Buddhist Army (DKBA)",
                    "Democratic Movement for the Liberation of the Eritrean Kunamas (DMLEK)",
                    "Democratic Youth Federation of India (DYFI)",
                    "Deniers of Holidays",
                    "Detonators of Social Uprisings",
                    "Devrimici Halk Kurtulus Cephesi (DHKP/C)",
                    "Didier Ratsiraka's Militia",
                    "Dima Halao Daoga (DHD)",
                    "Dima Hasao National Army",
                    "Diraa al-Shahbaa Rebel Brigade",
                    "Dissident",
                    "Dissident Republican Guard",
                    "Dissident Republicans",
                    "Diyala Salvation Council",
                    "Donetsk People's Republic",
                    "Donskoy Army",
                    "Draa El Mizan Seriat",
                    "Dravidar Viduthalai Kazhagam (DVK)",
                    "Dynamic Youth Forum",
                    "Earth Liberation Front (ELF)",
                    "East Turkistan Liberation Organization",
                    "Eastern Turkistan Islamic Movement (ETIM)",
                    "Economic Freedom Fighters",
                    "Ed'daoua Es'salafia Lilqadha",
                    "Egbema National Front",
                    "Egbesu Youths of the Bayelsa",
                    "Egyptian Tawhid and Jihad",
                    "Egyptians",
                    "Ejercito Revolucionario Guevarista (Guevarist Revolutionary Army)",
                    "El-Feth katibat",
                    "English Defense League (EDL)",
                    "Enraged Revolutionaries",
                    "Environmentalists",
                    "Enyele Militia",
                    "Epanastatiki Anatropi (Revolutionary Overthrow)",
                    "Equilibrio Nacional",
                    "Eritrean Salvation Front (ESF)",
                    "Erotic Anti-authority Cells",
                    "Ethiopians",
                    "Etnocacerista Movement",
                    "Extremists",
                    "Face to Face (Face-a-Face)",
                    "Fajr al-Umma Brigade",
                    "Fanmi Lavalas",
                    "Farmer's Movement of the Philippines (KMP)",
                    "Fatah al-Islam",
                    "Fatah Hawks",
                    "Fatherland",
                    "Fatherland and Liberty Nationalist Front (FNPL - Frente Nacionalista Patria y Libertad FNPL)",
                    "Fatoni Warriors",
                    "February 14th Movement",
                    "February 17 Martyrs Brigade",
                    "Fedayeen Imam Mahdi",
                    "Federation of Students and Scholars of Cote d'Ivoire (FESCI)",
                    "Feminist extremists",
                    "Fetullah Terrorist Organization",
                    "Fezzan Province of the Islamic State",
                    "Fight Against Authority",
                    "Fight Xenophobia",
                    "Fighters of Democratic Latvia",
                    "Fighting For Freedom Coalition (FFFC-Ottawa)",
                    "Fighting Guerrilla Formation",
                    "Fire and Flame for the Police (FFdP)",
                    "First Capital Command (PCC)",
                    "First of October Antifascist Resistance Group (GRAPO)",
                    "Forbidden Blockade (Greece)",
                    "Force Etudiante Critique",
                    "Forces for the Defense of Democracy (FDD)",
                    "Forces for the Unification of the Central African Republic (FIRCA)",
                    "Forest Brothers",
                    "Former Soldiers/Police",
                    "Four Martyrs Brigade",
                    "Free Aceh Movement (GAM)",
                    "Free and Democratic Society of Eastern Kurdistan (KODAR)",
                    "Free Balochistan Army (FBA)",
                    "Free Democratic People's Government of Laos",
                    "Free Libya Martyrs Brigade",
                    "Free Network South (Freies Netz Sued)",
                    "Free Papua Movement (OPM-Organisasi Papua Merdeka)",
                    "Free Syrian Army",
                    "Free Vietnam Movement",
                    "Freedom Eagles of Africa",
                    "Freital Group",
                    "French Armed Islamic Front",
                    "Friendly Company",
                    "Friends of Freedom",
                    "Friends of Loukanikos",
                    "Front for Peace and Reconciliation (FRP)",
                    "Front for the Liberation of Cabinda / Cabinda Armed Forces (FLEC-FAC)",
                    "Front for the Liberation of the Enclave of Cabinda (FLEC)",
                    "Front for the Restoration of Unity and Democracy",
                    "Fuerzas Armadas Revolucionarias del Pueblo (FARP)",
                    "Fuerzas Auton\u00f3micas y Destructivas Le\u00f3n Czolgoscz",
                    "Fulani extremists",
                    "Fundamentalists",
                    "Future movement (Lebanon)",
                    "Gangs of Conscience",
                    "Garo National Liberation Army",
                    "Gathering to Aid the Oppressed",
                    "Gazteriak",
                    "Gbagbo Loyalists",
                    "Georgian extremists",
                    "Georgian Militants",
                    "German Resistance Movement",
                    "Ghniwah Militia",
                    "Gholam Yahya Akbar",
                    "Gilad Shalhevet Brigades",
                    "Global Intifada",
                    "God's Army",
                    "Golden Dawn",
                    "Gono Bahini (GB)",
                    "Good Vision Party (India)",
                    "Gorkha Janmukti Morcha (GJM)",
                    "Gorkha Liberation Army (GLA)",
                    "Government Supporters",
                    "Great Eastern Islamic Raiders Front (IBDA-C)",
                    "Greater Damascus Operations Room",
                    "Grey Wolves",
                    "Group of Carlo Giuliani",
                    "Group of Popular Fighters",
                    "Group of the Fatima Church Attack",
                    "Groups for Dissemination of Revolutionary Theory and Action",
                    "Groups of Martyr Baha Eleyan",
                    "Grozny Jamaat",
                    "Grupo de Combatientes Populares",
                    "Guadalcanal Liberation Army",
                    "Guadalcanal Liberation Front (GLF)",
                    "Guerrillas",
                    "Gugama Youth Federation",
                    "Gulf Cartel",
                    "Gunib Group",
                    "Gunmen",
                    "Habi's Er'roub seriat",
                    "Habr Gedir Clan",
                    "Hadramawt Province of the Islamic State",
                    "Hadramawt Tribes Alliance",
                    "Hafeez Brohi Group",
                    "Hafiz Gul Bahadur Group",
                    "Haftar Militia",
                    "Haika",
                    "Haji Fateh",
                    "Halqa-e-Mehsud",
                    "Hamas (Islamic Resistance Movement)",
                    "Hamza Combat Group against Atheism and Heresy",
                    "Haqqani Network",
                    "Harakat al-Nujaba",
                    "Harakat Ansar Iran (HAI)",
                    "Harakat ul-Mujahidin (HuM)",
                    "Harakat ul-Mujahidin Al-Almi",
                    "Harkatul Jihad-e-Islami",
                    "Hasam Movement",
                    "Hassan Mabkhut Group",
                    "Hay Andalus Defense Operations Room",
                    "Hay'at Tahrir al-Sham",
                    "Hekla Reception Committee-Initiative for More Social Eruptions",
                    "Hells Angels",
                    "Hezbollah",
                    "Hezbollah Palestine",
                    "High Council for the Unity of Azawad (HCUA)",
                    "Hijaz Province of the Islamic State",
                    "Hill Tiger Force (HTF)",
                    "Himali Tigers",
                    "Hindu extremists",
                    "Hindu Illaignar Sena",
                    "Hindu Morcha Nepal",
                    "Hizb al-Tahrir al-Islami (HT)",
                    "Hizb-I-Islami",
                    "Hizballah-Iraq",
                    "Hizbul al Islam (Somalia)",
                    "Hizbul Mujahideen (HM)",
                    "Hmar People's Convention-Democracy (HPC-D)",
                    "Hofstad Network",
                    "Holders of the Black Banners",
                    "Hoodie Wearers",
                    "Houthi extremists (Ansar Allah)",
                    "Human Rights Renewal Movement (Renovacion por los Derechos Humanos)",
                    "Hushaysh Tribal Members",
                    "Hutu extremists",
                    "Hutus Former Soldiers",
                    "Hynniewtrep National Liberation Council (HNLC)",
                    "Iconoclasts",
                    "Ijaw extremists",
                    "Illuminating Paths of Solidarity",
                    "Imam Hussein Brigade",
                    "Imam Shamil Battalion",
                    "Incel extremists",
                    "Incendiary Committee of Solidarity for Detainees",
                    "Indian Mujahideen",
                    "Indigenous People of Biafra (IPOB)",
                    "Indigenous People's Federal Army (IPFA)",
                    "Indigenous People's Front of Tripura (IPFT)",
                    "Individuals Tending Toward Savagery",
                    "Informal Anarchist Federation",
                    "Informal Feminist Commando for Anti-authoritarian Action",
                    "Ingush Rebels",
                    "Initiative de Resistance Internationaliste",
                    "Insurgents",
                    "International Revolutionary Front",
                    "International Solidarity",
                    "Intifada Martyrs",
                    "Iparretarrak (IK)",
                    "Iraq's Jihadist Leagues",
                    "Iraqi extremists",
                    "Iraqi Islamic Vanguards for National Salvation (IIVNS)",
                    "Iraqi Sunni Extremists",
                    "Irish National Liberation Army (INLA)",
                    "Irish Republican Army (IRA)",
                    "Irish Republican Extremists",
                    "Irrintzi",
                    "Isatabu Freedom Movement (IFM)",
                    "Islambouli Brigades of al-Qaida",
                    "Islami Jamiat-e-Talaba (IJT)",
                    "Islamic Army in Iraq (al-Jaish al-Islami fi al-Iraq)",
                    "Islamic Companies",
                    "Islamic Courts Union (ICU)",
                    "Islamic Defenders' Front (FPI)",
                    "Islamic Fateh",
                    "Islamic Front",
                    "Islamic Front (Syria)",
                    "Islamic Jihad Brigades",
                    "Islamic Jihad Group (IJG)",
                    "Islamic Jihad Organization (Yemen)",
                    "Islamic Jihad Union (Uzbekistan)",
                    "Islamic Movement (Nigeria)",
                    "Islamic Movement for the Liberation of Raja",
                    "Islamic Movement of Iraqi Mujahidin",
                    "Islamic Movement of Kashmir",
                    "Islamic Movement of Uzbekistan (IMU)",
                    "Islamic Party (Somalia)",
                    "Islamic Shashantantra Andolon (ISA)",
                    "Islamic State in Bangladesh",
                    "Islamic State in Egypt",
                    "Islamic State in the Greater Sahara (ISGS)",
                    "Islamic State of Iraq (ISI)",
                    "Islamic State of Iraq and the Levant (ISIL)",
                    "Islamic Swords of Justice in the Land of Ribat",
                    "Islamic Tendency",
                    "Islamic Unification Movement",
                    "Islamic Youth Shura Council",
                    "Islamist extremists",
                    "Israeli extremists",
                    "Israeli settlers",
                    "Itsekiri",
                    "Izberbash Gang",
                    "Jabha East Africa",
                    "Jacinto Araujo Internationalist Rebel Insurrectionist Brigade",
                    "Jadid Al-Qaida Bangladesh (JAQB)",
                    "Jai Shri Ram Hindu Bhai Group",
                    "Jaime Bateman Cayon Group (JBC)",
                    "Jaish al-Adl",
                    "Jaish al-Fatah (Syria)",
                    "Jaish al-Islam (Libya)",
                    "Jaish al-Muhajireen wal-Ansar (Muhajireen Army)",
                    "Jaish al-Mujahideen (Syria)",
                    "Jaish al-Mukhtar",
                    "Jaish al-Muslimin (Army of the Muslims)",
                    "Jaish al-Sunnah",
                    "Jaish al-Ta'ifa al-Mansura",
                    "Jaish al-Umar (JaU)",
                    "Jaish Al-Umma (Army of the Nation)",
                    "Jaish as-Saiyouf (Army of Swords)",
                    "Jaish Tahkim al-Din",
                    "Jaish Usama",
                    "Jaish-e-Islam",
                    "Jaish-e-Khorasan (JeK)",
                    "Jaish-e-Mohammad (Iraq)",
                    "Jaish-e-Mohammad (JeM)",
                    "Jaljala Army",
                    "Jama'atul Mujahideen Bangladesh (JMB)",
                    "Jamaah Ansharut Daulah",
                    "Jamaah Ansharut Tauhid (JAT)",
                    "Jamaat Nusrat al-Islam wal Muslimin (JNIM)",
                    "Jamaat Tauhid Wal Jihad (Pakistan)",
                    "Jamaat-E-Islami (Bangladesh)",
                    "Jamaat-E-Islami (India/Pakistan)",
                    "Jamaat-ul-Ahrar",
                    "Jamiat ul-Mujahedin (JuM)",
                    "Jammu and Kashmir Islamic Front",
                    "Janatantrik Terai Madhes Mukti Party- Bhagat Singh (JTMMP)",
                    "Janatantrik Terai Madhesh Mukti Morcha (JTMMM)",
                    "Janatantrik Terai Mukti Morcha (JTMM)",
                    "Janatantrik Terai Mukti Morcha- Bisphot Singh (JTMM-B)",
                    "Janatantrik Terai Mukti Morcha- Goit (JTMM-G)",
                    "Janatantrik Terai Mukti Morcha- Jwala Singh (JTMM-J)",
                    "Janatantrik Terai Mukti Morcha- Prithvi Singh (JTMM-P)",
                    "Janatantrik Terai Mukti Morcha- Rajan Mukti (JTMM-R)",
                    "Janatantrik Terai Mukti Morcha- Ranbir Singh (JTMM-RS)",
                    "Janatantrik Tiger Janashakti Party",
                    "Janjaweed",
                    "Jarrai",
                    "Jaysh al-Islam (Syria)",
                    "Jemaah Islamiya (JI)",
                    "Jenin Martyrs Brigades",
                    "Jerusalem Groups Hebrew (Qvutzot Yerushalayim)",
                    "Jewish Defense League (JDL)",
                    "Jewish Extremists",
                    "Jharkhand Bachao Aandolan",
                    "Jharkhand Janmukti Parishad (JJP)",
                    "Jharkhand Kranti Raksha Dal (Utari Chotanagpur)",
                    "Jharkhand Liberation Tigers (JLT)",
                    "Jharkhand Prastuti Committee (JPC)",
                    "Jharkhand Sangharsh Jan Mukti Morcha",
                    "Jihadi Movement of the Sunna People of Iran",
                    "Jihadi-inspired extremists",
                    "Jihadist Soldiers",
                    "Joint Meeting Parties (JMP)",
                    "Jordanian extremists",
                    "Jordanian Islamic Resistance",
                    "Juarez Cartel (Carrillo-Fuentes / Mexico)",
                    "Jumuiya ya Taasisi za Kiislam (Community of Muslim Organizations)",
                    "Junaid Jihadist Battalion",
                    "Jund al-Aqsa",
                    "Jund al-Islam",
                    "Jund al-Khilafa",
                    "Jund al-Khilafah (Tunisia)",
                    "Jund al-Sahabah Group",
                    "Jund al-Sham for Tawhid and Jihad",
                    "Jund Al-Tawid",
                    "Jund Ansar Allah",
                    "Jundallah (Iran)",
                    "Jundallah (Pakistan)",
                    "Jundul Khilafah (Philippines)",
                    "Just Punishment Brigades",
                    "Justice and Equality Movement (JEM)",
                    "Justicieros de la Frontera",
                    "Kach",
                    "Kachin Independence Army (KIA)",
                    "Kamtapur Liberation Organization (KLO)",
                    "Kamwina Nsapu Militia",
                    "Kanglei Yawol Kanna Lup (KYKL)",
                    "Kangleipak Communist Party (KCP)",
                    "Kani Brigade",
                    "Kara clan",
                    "Karabulak Gang",
                    "Karamojong Warriors",
                    "Karbi Longri National Liberation Front (KLNLF)",
                    "Karbi Longri North Cachar Liberation Front (KLNLF)",
                    "Karbi National Volunteers (KNV)",
                    "Karbi People's Liberation Tigers (KPLT)",
                    "Karbi Tribe",
                    "Karen National Union",
                    "Karenni National Progressive Party",
                    "Kashmir Freedom Force",
                    "Kashmiri extremists",
                    "Kata'ib al-Imam Ali",
                    "Kata'ib al-Khoul",
                    "Kata'ib Hezbollah",
                    "Kawal ng Pilipinas (Soldier of the Philippines)",
                    "Kazakhstan Liberation Army",
                    "Khaled Ibn al-Walid Army",
                    "Khalid ibn Walid Brigade",
                    "Khalistan Liberation Force",
                    "Kharkiv Partisans",
                    "Khasi Students Union",
                    "Khmer Rouge",
                    "Khorasan Chapter of the Islamic State",
                    "Khorasan jihadi group",
                    "Khumbuwan Mukti Morcha",
                    "Kilafah Islamic Movement",
                    "Kirat Janabadi Workers Party",
                    "Kizilyurtovskiy Group",
                    "Knights of Janzour",
                    "Knights Templar (Caballeros Templarios)",
                    "Kokang",
                    "Kosovo Liberation Army (KLA)",
                    "Kouweikhat Group",
                    "Ku Klux Klan",
                    "Kuki Independent Organization (KIO/KIA)",
                    "Kuki Liberation Army (KLA)",
                    "Kuki National Army (KNA)",
                    "Kuki National Front (KNF)",
                    "Kuki National Liberation Front (KNLF)",
                    "Kuki Revolutionary Army (KRA)",
                    "Kuki Tribal Militants",
                    "Kuki Unification Frontal Organization (KUFO)",
                    "Kurdish Democratic Party-Iraq (KDP)",
                    "Kurdish Militants",
                    "Kurdish Separatists",
                    "Kurdistan Free Life Party",
                    "Kurdistan Freedom Hawks (TAK)",
                    "Kurdistan Workers' Party (PKK)",
                    "Lahij Province of the Islamic State",
                    "Lama Group",
                    "Lashkar-e-Balochistan",
                    "Lashkar-e-Islam (India)",
                    "Lashkar-e-Islam (Pakistan)",
                    "Lashkar-e-Jhangvi",
                    "Lashkar-e-Omar",
                    "Lashkar-e-Taiba (LeT)",
                    "Laskar Jihad",
                    "Last Generation",
                    "League of Damascus for Special Tasks",
                    "Left-wing extremists",
                    "Left-Wing Guerrillas",
                    "Lehava",
                    "Lekagak Tenggamati",
                    "Lendu extremists",
                    "Les Casseurs",
                    "Liberal Democratic Council for Missing Land",
                    "Liberals of Galilee",
                    "Liberation and Justice Movement (LJM)",
                    "Liberation Army for Presevo, Medvedja and Bujanovac (UCPMB)",
                    "Liberation Front for English-speaking Cameroon",
                    "Liberation of Achik Elite Force (LAEF)",
                    "Liberation Tigers of Tamil Eelam (LTTE)",
                    "Liberation War Veterans Association",
                    "Liberians United for Reconciliation and Democracy (LURD)",
                    "Libya Revolutionaries Operations Room (LROR)",
                    "Libya Shield Force",
                    "Libyan Militia",
                    "Liwa Ahrar al-Sunna",
                    "Liwa al-Haqq",
                    "Liwa al-Islam",
                    "Liwa al-Sham",
                    "Liwa al-Tawhid",
                    "Lone Wolves of Radical, Autonomous, Militant National Socialism",
                    "Lord's Resistance Army (LRA)",
                    "Los Rastrojos (Colombia)",
                    "Loyalist Action Force",
                    "Loyalist Volunteer Forces (LVF)",
                    "Loyalists",
                    "Luhansk People's Republic",
                    "LW",
                    "M23",
                    "Ma'sadat al-Mujahideen",
                    "Mabanese Defence Forces",
                    "Macedonian nationalists",
                    "Macheteros",
                    "Macina Liberation Front (FLM)",
                    "Madhesh Mukti Sangram (MMS)",
                    "Madhesh Rastra Janatantrik Revolutionary (RJR) - Nepal",
                    "Madhesi Mukti Tigers (MMT)",
                    "Madhesi People's Rights Forum (MPRF)",
                    "Madhesi Virus Killers",
                    "Magahat Militia",
                    "Mahan Madhesh Janakantri Party (MMJP)- Nepal",
                    "Mahaz Fedai Tahrik Islami Afghanistan",
                    "Mahaz-e-Inquilab",
                    "Mahdaviyat",
                    "Mahdi Army",
                    "Mahidi",
                    "Mahsud Tribe",
                    "Mai Mai Bakata Katanga Militia",
                    "Mai Mai Charles Militia",
                    "Mai Mai Karakara Militia",
                    "Mai Mai Malaika Militia",
                    "Mai Mai Manu Militia",
                    "Mai Mai Mazembe Militia",
                    "Mai Mai Simba Militia",
                    "Mai Mai Yakutumba Militia",
                    "Majlis-e-Askari",
                    "Makhachkala Gang",
                    "Manipur Naga People's Army (MNPA)",
                    "Manipur Nationalist Revolutionary Party (MNRP)",
                    "Maoist Communist Center (MCC)",
                    "Maoist Communist Party (MKP)",
                    "Maoist Communist Party of Manipur",
                    "Maoists",
                    "Mapuche activists",
                    "Mapuche Ancestral Resistance (RAM)",
                    "Marhan Clan",
                    "Mariano Moreno National Liberation Commando",
                    "Mariscal Lopez Army (EML)",
                    "Martyr al-Nimr Battalion",
                    "Martyr Sami al-Ghul Brigades",
                    "Marwan Al-Qawasimi and Amir Abu-Ayshah Group",
                    "Matan Abdulle",
                    "Mateo Morral Insurrectionist Commandos",
                    "Maute Group",
                    "Mawlawi and Mansour Group",
                    "May 15",
                    "May 98",
                    "Mayi Mayi",
                    "Mazdoor Kisan Sangram Samiti (MKSS)",
                    "Medfaiyah Wal-Sewarigh Brigade",
                    "Merille Militia",
                    "Militant Forces Against Huntingdon",
                    "Militant Minority (Greece)",
                    "Militant Organization of Russian Nationalists",
                    "Militant People's Revolutionary Forces",
                    "Militants",
                    "Military Council of the Tribal Revolutionaries (MCTR)",
                    "Militia Members",
                    "Minorities of Metropolitan Attacks",
                    "Minutemen American Defense",
                    "Misanthropic Division",
                    "Miscreants",
                    "Misrata Brigades",
                    "Misseriya Arab Tribesmen",
                    "Misurata Rebels",
                    "Mitiga Militia",
                    "Mlada Bosna",
                    "Mombasa Republican Council (MRC)",
                    "Mongolian Mukti Morcha",
                    "Moran Tiger Force (MTF)",
                    "Moro Ghuraba",
                    "Moro Islamic Liberation Front (MILF)",
                    "Moro National Liberation Front (MNLF)",
                    "Moroccan extremists",
                    "Mouhajiroune Brigade",
                    "Movement for Democracy and Justice in Chad (MDJT)",
                    "Movement for Democratic Change (MDC)",
                    "Movement for Oneness and Jihad in West Africa (MUJAO)",
                    "Movement for the Actualization of the Sovereign State of Biafra (MASSOB)",
                    "Movement for the Emancipation of the Niger Delta (MEND)",
                    "Movement for the Restoration of the Independence of Southern Cameroons",
                    "Movement of Democratic Forces of Casamance",
                    "Movement of Niger People for Justice (MNJ)",
                    "Mozambique Liberation Front (FRELIMO)",
                    "Mozambique National Resistance Movement (MNR)",
                    "Mujahedeen Army",
                    "Mujahedeen Brigades",
                    "Mujahedeen Corps in Iraq",
                    "Mujahedeen Group",
                    "Mujahedeen Shura Council",
                    "Mujahedeen Shura Council in the Environs of Jerusalem",
                    "Mujahedin Kompak",
                    "Mujahedin-e Khalq (MEK)",
                    "Mujahideen Ansar",
                    "Mujahideen Islam Pattani",
                    "Mujahideen Youth Movement (MYM)",
                    "Mujahidin Ambon",
                    "Mujahidin Indonesia Timur (MIT)",
                    "Mukhtar Army",
                    "Mullah Dadullah Front",
                    "Munadil al-Jumalyi Brigade",
                    "Mungiki Sect",
                    "Murle Tribe",
                    "Musa Sudi Yalahow Militia",
                    "Muslim Brotherhood",
                    "Muslim extremists",
                    "Muslim Fundamentalists",
                    "Muslim Militants",
                    "Muslim Rebels",
                    "Muslim Renewal",
                    "Muslim Separatists",
                    "Muslim United Army (MUA)",
                    "Muslim United Liberation Tigers of Assam (MULTA)",
                    "Muslims",
                    "Muslims Against Global Oppression (MAGO)",
                    "Mustafa al-Hujairi Group",
                    "Mutahida Majlis-e-Amal",
                    "Mutassim Billah Gaddafi Battalion",
                    "Muttahida Qami Movement (MQM)",
                    "Naga extremists",
                    "Naga National Council (NNC)",
                    "Naga People",
                    "Naga People's Council (NPC)",
                    "Najd Province of the Islamic State",
                    "Nalut Group",
                    "National Alliance (Chad)",
                    "National Anti-Corruption Front (FNA)",
                    "National Army for the Liberation of Uganda (NALU)",
                    "National Bolshevik Party (Partiya Natsionalnikh Bolshevikov - PNB)",
                    "National Committee for the Restoration of Democracy and State (CNRDR)",
                    "National Congress for the Defense of the People (CNDP)",
                    "National Council for Defense of Democracy (NCDD)",
                    "National Council for Revival and Democracy (CNRD)",
                    "National Democratic Alliance (NDA)",
                    "National Democratic Alliance Army (NDAA-ESSA)",
                    "National Democratic Alliance of Sudan",
                    "National Democratic Front of Bodoland (NDFB)",
                    "National Democratic Front-Bicol (NDF-Bicol)",
                    "National Freedom Party",
                    "National Front Against Tigers (NFAT)",
                    "National Front for the Revolution in Burundi (FRONABU-Tabara)",
                    "National League for Democracy",
                    "National Liberation Alliance of Sa Kaeo",
                    "National Liberation Army (Ecuador)",
                    "National Liberation Army (Nepal)",
                    "National Liberation Army (NLA) (Macedonia)",
                    "National Liberation Army of Colombia (ELN)",
                    "National Liberation Council of Taniland",
                    "National Liberation Force of Bengalis (Bangali Janamukti Bahini)",
                    "National Liberation Front (Ecuador)",
                    "National Liberation Front (FNL) (Burundi)",
                    "National Liberation Front of Provence (FLNP)",
                    "National Liberation Front of Tripura (NLFT)",
                    "National Movement of Revolutionaries (MNR)",
                    "National Organization of Cypriot Fighters (EOKA)",
                    "National People's Party (Rashtriya Janata Dal - RJD)",
                    "National Redemption Front",
                    "National Revolutionary Front of Manipur (NRFM)",
                    "National Santhali Liberation Army (NSLA)",
                    "National Socialist Council of Nagaland",
                    "National Socialist Council of Nagaland-Isak-Muivah (NSCN-IM)",
                    "National Socialist Council of Nagaland-Khaplang (NSCN-K)",
                    "National Socialist Council of Nagaland-Khole-Kitovi (NSCN-KK)",
                    "National Socialist Council of Nagaland-Reformation (NSCN-R)",
                    "National Socialist Council of Nagaland-Unification (NSCN-U)",
                    "National Socialist Underground",
                    "National Union for the Total Independence of Angola (UNITA)",
                    "National United Front of Democracy Against Dictatorship (UDD)",
                    "National Youth Service of Zimbabwe",
                    "Nationalist Integrationist Front (FNI)",
                    "Nawasi Brigade",
                    "Naxalites",
                    "Nazhdak",
                    "Nduma Defense of Congo (NDC)",
                    "Neo-Fascists",
                    "Neo-Nazi extremists",
                    "Nepal Defense Army",
                    "Nepalbad Party",
                    "Nepali Congress Party (NC)",
                    "New Indigenous People's Army",
                    "New People's Army (NPA)",
                    "New Revolutionary Alternative (NRA)",
                    "New Revolutionary Popular Struggle (NELA)",
                    "Niger Delta Avengers (NDA)",
                    "Niger Delta extremists",
                    "Niger Delta Forest Army (NDFA)",
                    "Niger Delta Freedom Fighters (NDDF)",
                    "Niger Delta Greenland Justice Mandate (NDGJM)",
                    "Niger Delta Justice Defense Group (NDJDG)",
                    "Niger Delta Liberation Force (NDLF)-Nigeria",
                    "Niger Delta People's Volunteer Force (NDPVF)",
                    "Niger Delta Vigilante (NDV)",
                    "Nihilistic Patrol and Neighborhood Arsonists",
                    "Nihilists Faction",
                    "Nikhil Bharat Bangali Udbastu Samannay Samiti (NBBUSS)",
                    "Ninjas",
                    "No Borders Group",
                    "Nobles of Jordan",
                    "Nordic Resistance Movement",
                    "November 17 Revolutionary Organization (N17RO)",
                    "Nuer White Army",
                    "Nur-al-Din al-Zinki Movement",
                    "NVF",
                    "Nyatura Militia",
                    "Odessa Underground",
                    "Odua Peoples' Congress (OPC)",
                    "Ogaden National Liberation Front (ONLF)",
                    "Oglaigh na hEireann",
                    "Okba Ibn Nafaa Brigade",
                    "Omar Bin Khattab Group",
                    "Omar Mukhtar Militia",
                    "Ombatse Cult",
                    "Opponents of disabled people",
                    "Opposition Group",
                    "Orakzai Freedom Movement",
                    "Orange Volunteers (OV)",
                    "Orfei Harsha'am",
                    "Organization for Revolutionary Self Defense",
                    "Organization of Soldiers of the Levant",
                    "Oromo Liberation Front",
                    "Otpor",
                    "Overall Deniers of Joining the Existing",
                    "Pagan Sect of the Mountain",
                    "Pahadi Cheetah",
                    "Pakistani People's Party (PPP)",
                    "Palestinian Extremists",
                    "Palestinian Hezbollah",
                    "Palestinian Islamic Jihad (PIJ)",
                    "Palestinians",
                    "Paraguayan People's Army (EPP)",
                    "Paramilitaries",
                    "Paramilitary members",
                    "Parbatya Chattagram Jana Sanghati Samity (PCJSS) - Bangladesh",
                    "Partido Marxista-Leninista ng Pilipinas (PMLP)",
                    "Partisan Sharpshooters",
                    "Party for the Liberation of the Hutu People (PALIPEHUTU)",
                    "Patria Grande Ejercito del Pueblo",
                    "Patriotic Europeans against the Islamization of the West (PEGIDA)",
                    "Patriotic Ginbot 7 Movement for Unity and Democracy (PGMUD)",
                    "Patriotic Resistance Front in Ituri (FRPI)",
                    "Pattali Makkal Katchi (PMK)",
                    "Pattani United Liberation Organization (PULO)",
                    "Pattani United Liberation Organization-MKP (PULO-MKP)",
                    "Peace at Home Council",
                    "Peasant Self-Defense Group (ACCU)",
                    "Pemuda Pancasila",
                    "Pentagon Kidnap Group",
                    "People Against Gangsterism and Drugs (PAGAD)",
                    "People's Amn Committee",
                    "People's Committee against Police Atrocities (PCPA)",
                    "People's Defense Unit (Turkey)",
                    "People's Democratic Party (PDP)",
                    "People's Democratic Struggle Movement",
                    "People's Liberation Army (India)",
                    "People's Liberation Front of India",
                    "People's Liberation Organization of Tamil Eelam",
                    "People's Protection Units (YPG)",
                    "People's Revolutionary Army (ERP)",
                    "People's Revolutionary Militias (MRP)",
                    "People's Revolutionary Movement (MRP)",
                    "People's Revolutionary Party of Kangleipak (PREPAK)",
                    "People's Revolutionary Party of Kangleipak-Progressive (PREPAK-P)",
                    "People's Sovereignty Party (PDR)",
                    "People's Tamil Organization",
                    "People's United Democratic Movement (PUDEMO)",
                    "People's United Liberation Front (PULF)",
                    "People's War Group (PWG)",
                    "Peoples' United Revolutionary Movement (HBDH)",
                    "Pirates",
                    "Pokot extremists",
                    "Popular Army for the Restoration of Democracy (APRD)",
                    "Popular Army Vanguards- Battalions of Return",
                    "Popular Front for Justice in the Congo",
                    "Popular Front for Recovery (FPR)",
                    "Popular Front for the Liberation of Palestine (PFLP)",
                    "Popular Front for the Liberation of Palestine, Gen Cmd (PFLP-GC)",
                    "Popular Front for the Renaissance of the Central African Republic (FPRC)",
                    "Popular Front of India",
                    "Popular Liberation Army (EPL)",
                    "Popular Mobilization Forces (Iraq)",
                    "Popular Resistance (Laiki Antistasi)",
                    "Popular Resistance Brigade",
                    "Popular Resistance Committees",
                    "Popular Resistance Committees (Yemen)",
                    "Popular Resistance Movement (Egypt)",
                    "Popular Revolutionary Action",
                    "Popular Revolutionary Army (Mexico)",
                    "Popular Will (Greece)",
                    "Porattom",
                    "Powers of the Revolutionary Arc",
                    "Pragatishil Tarai Mukti Morcha",
                    "Praveen Dalam",
                    "Pro Hartal Activists",
                    "Pro-Government extremists",
                    "Pro-Indonesia extremists",
                    "Pro-LGBT Rights extremists",
                    "Pro-Pakistani extremists",
                    "Pro-Russia Militia",
                    "Progressive Socialist Party (PSP)",
                    "Proletarian Assault Group",
                    "Proletarian Nuclei for Communism",
                    "Proletarian Solidarity",
                    "Proletariat Self-defense Groups",
                    "Protectors of Islam Brigade",
                    "Protestant extremists",
                    "Provisional National Government of Vietnam",
                    "Provisional RSPCA",
                    "Punjabi Taliban",
                    "Puratchi Puligal",
                    "Purbo Banglar Communist Party",
                    "Qaddafi loyalists",
                    "Qari Kamran Group",
                    "Quit Kashmir Movement (QKM)",
                    "Rabha National Security Force",
                    "Rabid Brothers of Giuliani",
                    "Rafallah al-Sahati Brigade",
                    "Rahanwein Resistance Army (RRA)",
                    "Raia Mutomboki Militia",
                    "Rajput extremists",
                    "Rally of Democratic Forces (RAFD)",
                    "Ramiro Ledesma Social Centre",
                    "Ramzi Nahra Martyr Organization",
                    "Ranbir Sena",
                    "Random Anarchists",
                    "Rashtriya Swayamsevak Sangh",
                    "Raskamboni Movement",
                    "Rastas",
                    "Rastriya Janashakti Party (RJP)",
                    "Real Irish Republican Army (RIRA)",
                    "Real Ulster Freedom Fighters (UFF) - Northern Ireland",
                    "Rebel Military Unit",
                    "Rebellious Group Lambros Foundas",
                    "Rebels",
                    "Red Brigades Fighting Communist Party (BR-PCC)",
                    "Red Brigades Fighting Communist Union (BR-UCC)",
                    "Red Egbesu Water Lions",
                    "Red Hand Defenders (RHD)",
                    "Red Line",
                    "Red Scorpion",
                    "Red Sea Afar Democratic Organization (RSADO)",
                    "Republican Action Against Drugs (RAAD)",
                    "Republican Forces of Burundi (FOREBU)",
                    "Resistance Cell",
                    "Resistencia Galega",
                    "Resistenza Corsa",
                    "Return, Reclamation, Rehabilitation (3R)",
                    "Revenge Brigade",
                    "Revenge Movement",
                    "Revenge of the Trees",
                    "Revolution's Brigade",
                    "Revolutionaries Army (Jaysh al-Thowwar)",
                    "Revolutionary Action of Liberation",
                    "Revolutionary Armed Forces of Colombia (FARC)",
                    "Revolutionary Armed Forces of Colombia (FARC) dissidents",
                    "Revolutionary Army",
                    "Revolutionary Cells (Argentina)",
                    "Revolutionary Cells Network (SRN)",
                    "Revolutionary Cells-Animal Liberation Brigade",
                    "Revolutionary Communist Centre (RCC)",
                    "Revolutionary Continuity",
                    "Revolutionary Force",
                    "Revolutionary Front",
                    "Revolutionary Headquarters (Turkey)",
                    "Revolutionary Insurgent Armed Forces of Ecuador (FAIRE)",
                    "Revolutionary Leninist Brigades",
                    "Revolutionary Liberation Action (Epanastatiki Apelevtherotiki Drasi) - Greece",
                    "Revolutionary Movement for National Salvation (REMNASA)",
                    "Revolutionary Nuclei",
                    "Revolutionary Perspective",
                    "Revolutionary Proletarian Initiative Nuclei (NIPR)",
                    "Revolutionary Punishment Movement",
                    "Revolutionary Struggle",
                    "Revolutionary United Front (RUF)",
                    "Revolutionary Violence Units",
                    "Revolutionary Workers' Council (Kakurokyo)",
                    "Right Sector",
                    "Right-Wing Death Squad",
                    "Right-wing extremists",
                    "Right-Wing Group",
                    "Right-Wing Paramilitaries",
                    "Riyadus-Salikhin Reconnaissance and Sabotage Battalion of Chechen Martyrs",
                    "Robin Food",
                    "Rodolfo Walsh National Command",
                    "Rohingya extremists",
                    "Rohingya Solidarity Organization",
                    "Rubicon (Rouvikonas)",
                    "Rugovasit Group",
                    "Runda Kumpulan Kecil (RKK)",
                    "Russian separatists",
                    "Russian Unity",
                    "Sabaot Land Defense Force (SLDF)",
                    "Saif-ul-Muslimeen",
                    "Salafi Abu-Bakr al-Siddiq Army",
                    "Salafi Daawa Group",
                    "Salafi Extremists",
                    "Salafia Jihadia",
                    "Salafist Group for Preaching and Fighting (GSPC)",
                    "Samyukta Janatantrik Terai Mukti Morcha (SJTMM)",
                    "Samyukta Jatiya Mukti Morcha (SJMM)",
                    "Samyukta Terai Madhes Mukti Party",
                    "Sanaa Province of the Islamic State",
                    "Sanatan Sanstha",
                    "Sanghiya Limbuwan Committee (SLC)",
                    "Saraya al-Mukhtar",
                    "Saraya Waad Allah",
                    "Sardinian Autonomy Movement",
                    "Sasna Tsrer",
                    "Save Kashmir Movement",
                    "Sayfullakh",
                    "Scottish National Liberation Army",
                    "Secessionists",
                    "Sect of Revolutionaries (Greece)",
                    "Seleka",
                    "Self-Defense Group of Imghad Tuaregs and Allies (GATIA)",
                    "Separatists",
                    "September 11",
                    "Serb Radical Party",
                    "Serbian extremists",
                    "Ses'khona Peoples' Rights Movement",
                    "Shabelle Valley militia",
                    "Shabwah Province of the Islamic State",
                    "Shamil Group",
                    "Shamiya Front",
                    "Shan State Army - North (SSA-N)",
                    "Shan State Army - South (SSA-S)",
                    "Shan United Revolutionary Army",
                    "Sheikh Omar Hadid Brigade",
                    "Shia Muslim extremists",
                    "Shield of Islam Brigade",
                    "Shining Path (SL)",
                    "Shiv Sena",
                    "Shuar extremists",
                    "Shura Council of Ajdabiya Revolutionaries",
                    "Shura Council of Benghazi Revolutionaries",
                    "Shura Council of Mujahideen in Derna",
                    "Shutdown G20: Take Hamburg offline!",
                    "Sikh Extremists",
                    "Simon Bolivar Guerrilla Coordinating Board (CGSB)",
                    "Sinai Province of the Islamic State",
                    "Sindh Liberation Front",
                    "Sindh Revolutionary Army",
                    "Sindhu Desh Liberation Army (SDLA)",
                    "Sindhudesh Revolutionary Army (SRA)",
                    "Sinhale Jathika Balamuluwa",
                    "Sipah-e-Sahaba/Pakistan (SSP)",
                    "Sipah-I-Mohammed",
                    "Sisters in Arms",
                    "SKIF Detachment",
                    "Skinheads",
                    "Soldiers of the Caliphate",
                    "Solidarity with imprisoned members of Action Directe (AD)",
                    "Somali Islamic Front",
                    "South East Antrim Brigade",
                    "South Group (Russia)",
                    "South Londonderry Volunteers (SLV)",
                    "South Ossetian Separatists",
                    "South Sudan Armed Forces (SSAF)",
                    "South Sudan Democratic Army",
                    "South Sudan Liberation Army (SSLA)",
                    "South Sudanese extremists",
                    "Southern Front",
                    "Southern Mobility Movement (Yemen)",
                    "Southern Yemen Separatists",
                    "Sovereign Citizen",
                    "Special Purpose Islamic Regiment (SPIR)",
                    "Squadrons of Terror (Katibat El Ahoual)",
                    "Strikers",
                    "Student Network of Mandalay",
                    "Students For Insurrection",
                    "Students Islamic Movement of India (SIMI)",
                    "Sudan Liberation Army-Minni Minawi (SLA-MM)",
                    "Sudan Liberation Movement",
                    "Sudan People's Liberation Army (SPLA)",
                    "Sudan People's Liberation Movement - North",
                    "Sudan People's Liberation Movement in Opposition (SPLM-IO)",
                    "Sudan Revolutionary Front (SRF)",
                    "Sudanese People's Front",
                    "Sudanese Rebels",
                    "Sudurpaschim Janata Dal",
                    "Sungu Sungu",
                    "Sunni Muslim extremists",
                    "Sunni Resistance Committees in Lebanon (SRCL)",
                    "Sunni Supporters",
                    "Support of Ocalan-The Hawks of Thrace",
                    "Supporters of Abd Rabbuh Mansur Hadi",
                    "Supporters of Ali Abdullah Saleh",
                    "Supporters of Ali Mahdi Mohammed",
                    "Supporters of Colonel Mahmud Khudoyberdiyev",
                    "Supporters of Ernest Wamba dia Wamba",
                    "Supporters of General Lino Oviedo",
                    "Supporters of Johnny Adair",
                    "Supporters of Johnny Paul Koroma",
                    "Supporters of Laurent Nkunda",
                    "Supporters of Muhammad Umar Habib",
                    "Supporters of Muse Sudi Yalahow",
                    "Supporters of Pascal Lissouba",
                    "Supporters of Saddam Hussein",
                    "Supporters of Sultan Abd-al-Bagi",
                    "Supporters of the Islamic State in Jerusalem",
                    "Supporters of the Islamic State in the Land of the Two Holy Mosques",
                    "Supreme Command for Jihad and Liberation",
                    "Supreme Council for Islamic Revolution in Iraq (SCIRI)",
                    "Suqour al-Ahvaz",
                    "Swatantra Nepal Dal",
                    "Swaziland Youth Congress (Swayoco)",
                    "Sword of Islam",
                    "Sympathizers of Al-Qaida Organization",
                    "Syrian Army deserters",
                    "Syrian Democratic Forces (SDF)",
                    "Syrian Mujahideen",
                    "Syrian Social Nationalist Party",
                    "Syrian Turkmen Brigades",
                    "Ta'ang National Liberation Army (TNLA)",
                    "Tabu Tribe",
                    "Tajamo Ansar al-Islam",
                    "Takfir wal-Hijra (Excommunication and Exodus)",
                    "Taliban",
                    "Taliban (Pakistan)",
                    "Tamil Makkal Viduthalai Pulikal (TMVP)",
                    "Tamil Nadu Liberation Army",
                    "Tamils",
                    "Tanzeem al-Islami al-Furqan",
                    "Tanzim",
                    "Tarok Militia",
                    "Tawheed Militia",
                    "Tawheedul Islam",
                    "Tawhid and Jihad",
                    "Tawhid and Jihad (Palestine)",
                    "Tehama Movement",
                    "Tehrik al-Mojahedin",
                    "Tehrik-e-Galba Islam",
                    "Tehrik-e-Hurriyat (TeH)",
                    "Tehrik-e-Khilafat",
                    "Tehrik-e-Nafaz-e-Shariat-e-Mohammadi (TNSM)",
                    "Tehrik-e-Nifaz-e-Aman Balochistan-Jhalawan Brigade (TNAB-Jhalawan Brigade)",
                    "Tehrik-e-Taliban Islami (TTI)",
                    "Tehrik-e-Tuhafaz (Pakistan)",
                    "Tehrik-i-Taliban Pakistan (TTP)",
                    "Tela Mohammed",
                    "Telangana Separatists",
                    "Tepi Youth",
                    "Terai Army",
                    "Terai Cobra",
                    "Terai Communist Party",
                    "Terai Janatantrik Madhes Party",
                    "Terai Janatantrik Party",
                    "Terai Madheshi Mukti Morcha (TMMM)",
                    "Terai Rastriya Mukti Sena (TRMS)",
                    "Terena Indians",
                    "Thadou People's Liberation Army (TPLA)",
                    "Thai Islamic Militants",
                    "Thanthai Periyar Dravidar Kazhagam (TPDK)",
                    "Tharuhat Joint Struggle Committee (TJSC)",
                    "The 78 Unemployed",
                    "The Association for Islamic Mobilisation and Propagation (UAMSHO)",
                    "The Black Sun",
                    "The Defense Command of the French People and the Motherland (CDPPF)",
                    "The Extraditables",
                    "The Front for the Liberation of the Cabinda Enclave - Renewed (FLEC)",
                    "The Husayn Ubayyat Martyrs' Brigades",
                    "The Independent Military Wing of the Syrian Revolution Abroad",
                    "The Inevitables",
                    "The Irish Volunteers",
                    "The Islamic Revolution to Liberate Palestine",
                    "The Jean Marc Rouillan Armed and Heartless Columns",
                    "The Joint Revolutionary Council",
                    "The Justice Department",
                    "The Mujahadeen Room in Latakia Countryside",
                    "The Nation's Army",
                    "The New Irish Republican Army",
                    "The Nocturnals",
                    "The Northern Alliance (or United Islamic Front for Salvation of Afghanistan - UIFSA)",
                    "The Owners of the White Flags",
                    "The Revolt",
                    "The Third Way (Der III. Weg)",
                    "The United Revolutionary Front of Bhutan",
                    "The War That Was Never Declared",
                    "Tibetan separatists",
                    "Tigers",
                    "Tigray Peoples Liberation Front (TPLF)",
                    "Tiv Militia",
                    "Tiwa Liberation Army",
                    "Tolib Ayombekov loyalists",
                    "Torpedy Group",
                    "Tribal Group",
                    "Tribal guerrillas",
                    "Tribal Revolutionary Army (TRA)",
                    "Tribesmen",
                    "Tripoli Province of the Islamic State",
                    "Tripoli Revolutionaries Battalion",
                    "Tripura Group",
                    "Tritiya Prastuti Committee (TPC)",
                    "Tuareg extremists",
                    "Tuareg Guerrillas",
                    "Tupamaro Revolutionary Movement",
                    "Turkestan Islamic Party",
                    "Turkish Communist Party/Marxist (TKP-ML)",
                    "Turkish Hezbollah",
                    "Turkish radicals",
                    "Tutsi extremists",
                    "Twa Militia",
                    "U/I Liberian Gunmen",
                    "U/I Snipers",
                    "U/I Somali Militiamen",
                    "Uighur Liberation Organization",
                    "Uighur Separatists",
                    "Ukrainian Insurgent Army",
                    "Ukrainian nationalists",
                    "Ulster Freedom Fighters (UFF)",
                    "Ulster Volunteer Force (UVF)",
                    "Umar al-Mukhtar Martyr Forces",
                    "Umbane People's Liberation Army (Swaziland)",
                    "Ummah Liberation Army",
                    "Unification Army Sons Brigade",
                    "Unified Communist Party of Nepal (Maoist)",
                    "Union for Peace in Central Africa (UPC)",
                    "Union of Chadian Forces (UFNT)",
                    "Union of Congolese Patriots (UPC)",
                    "Union of Democratic Forces",
                    "Union of Forces for Democracy and Development (UFDD)",
                    "Union of Patriots for the Defense of Innocents (UPDI)",
                    "Union of Peoples and Organizations of the State of Guerrero (UPOEG)",
                    "Union of Revolutionary Communists in Turkey (TIKB)",
                    "Union Parishad",
                    "United Achik Liberation Army (UALA)",
                    "United Achik National Front (UANF)",
                    "United Aryan Empire",
                    "United Baloch Army (UBA)",
                    "United Bengali Liberation Front (UBLF)",
                    "United Democratic Liberation Army (UDLA)",
                    "United Democratic Madhesi Front (UDMF)",
                    "United Democratic Terai Liberation Front (UDTLF)",
                    "United Front for Democracy Against Dictatorship",
                    "United Front for Democratic Change (FUC)",
                    "United Garo Security Force (UGSF)",
                    "United Gorkha People's Organization (UGPO)",
                    "United Janatantrik Terai Mukti Morcha (U-JTMM)",
                    "United Jihad Council",
                    "United Karbi Liberation Army (UKLA)",
                    "United Kuki Liberation Front (UKLF) - India",
                    "United Liberation Front of Assam (ULFA)",
                    "United Liberation Front of Barak Valley (ULFBV) - India",
                    "United National Liberation Front (UNLF)",
                    "United National Liberation Front of WESEA (UNLFW)",
                    "United Party for National Development (UPND)",
                    "United People's Democratic Front (UPDF) - Bangladesh",
                    "United People's Democratic Solidarity (UPDS)",
                    "United Reformation Protest of India",
                    "United Revolutionary Front",
                    "United Self Defense Units of Colombia (AUC)",
                    "United Tribal Liberation Army (UTLA)",
                    "Unrepentant Anarchists",
                    "Unsubordinated Desires",
                    "Urban Guerrilla War",
                    "Urhobo Gbagbako",
                    "Urhobo Revolutionary Army",
                    "Vandalicia Teodoro Suarez",
                    "Vanguard of Red Youth (AKM)",
                    "Vanguards of the Caliphate",
                    "Veterans United for Non-Religious Memorials",
                    "Vetevendosje",
                    "Vigorous Burmese Student Warriors",
                    "Villagers",
                    "Vishwa Hindu Parishad (VHP)",
                    "Volunteers of Innocent People of Nagas (VIPN)",
                    "Wahhabi Movement",
                    "Weichan Auka Mapu",
                    "West Indonesia Mujahideen",
                    "West Side Boys",
                    "White extremists",
                    "White Legion (Ecuador)",
                    "White Legion (Georgia)",
                    "White Rabbit Three Percent Illinois Patriot Freedom Fighters Militia",
                    "White Wolves (UK)",
                    "Wild Freedom",
                    "Wild Individualities",
                    "Wolves of Islam",
                    "World Church of the Creator",
                    "Yakariya Bango Insurgent Group",
                    "Yarmouk Martyrs Brigade",
                    "Yazidi extremists",
                    "Yekbun",
                    "Yemenis",
                    "Yimchunger Liberation Front (YLF)",
                    "Young Communist League",
                    "Young Liberators of Pattani",
                    "Young Officer Union of the New Generation and Reformist Armed Forces of the Philippines (YOU-RAFP)",
                    "Youth Movement for the Total Liberation of Azawad",
                    "Youth of Islamic Awakening",
                    "Youth of the Land of Egypt",
                    "Youths",
                    "Zagros Eagles",
                    "Zapatista National Liberation Army",
                    "Zawiya Martyrs Brigade",
                    "Zehri Youth Force (ZYF)",
                    "Zeliangrong United Front",
                    "Zemun Clan",
                    "Zero Tolerance",
                    "Zetas",
                    "Zimbabwe African Nationalist Union (ZANU)",
                    "Zintani Militia",
                    "Zionist Resistance Fighters",
                    "Zomi Revolutionary Army (ZRA)",
                    "Zuwar al-Imam Rida",
                    "Zviadists",
                    "Zwai Tribe"
                }));
                panel1.add(comboBox11, new GridBagConstraints(3, 2, 3, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 5, 5), 0, 0));

                //---- label4 ----
                label4.setText("Attack type");
                panel1.add(label4, new GridBagConstraints(6, 2, 1, 1, 0.0, 0.0,
                    GridBagConstraints.EAST, GridBagConstraints.VERTICAL,
                    new Insets(0, 0, 5, 5), 0, 0));

                //---- comboBox9 ----
                comboBox9.setBackground(Color.white);
                comboBox9.setModel(new DefaultComboBoxModel<>(new String[] {
                    "null",
                    "Assassination",
                    "Hijacking",
                    "Kidnapping",
                    "Barricade Incident",
                    "Bombing/Explosion",
                    "Armed Assault",
                    "Unarmed Assault",
                    "Facility/Infrastructure Attack",
                    "Unknown"
                }));
                panel1.add(comboBox9, new GridBagConstraints(7, 2, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 5, 0), 0, 0));

                //======== panel3 ========
                {
                    panel3.setLayout(new GridBagLayout());
                    ((GridBagLayout)panel3.getLayout()).columnWidths = new int[] {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
                    ((GridBagLayout)panel3.getLayout()).rowHeights = new int[] {0, 0};
                    ((GridBagLayout)panel3.getLayout()).columnWeights = new double[] {0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0E-4};
                    ((GridBagLayout)panel3.getLayout()).rowWeights = new double[] {0.0, 1.0E-4};

                    //---- button1 ----
                    button1.setText("Serach");
                    button1.addActionListener(e -> button1ActionPerformed(e));
                    panel3.add(button1, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.VERTICAL,
                        new Insets(0, 0, 0, 5), 0, 0));
                }
                panel1.add(panel3, new GridBagConstraints(3, 3, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 0, 5), 0, 0));
            }
            scrollPane1.setViewportView(panel1);
        }
        contentPane.add(scrollPane1, BorderLayout.NORTH);

        //======== scrollPane2 ========
        {

            //---- table1 ----
            table1.setAutoCreateRowSorter(true);
            table1.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
            table1.setModel(tableModel);
            scrollPane2.setViewportView(table1);
        }
        contentPane.add(scrollPane2, BorderLayout.CENTER);

        //======== panel4 ========
        {
            panel4.setLayout(new FlowLayout());

            //---- button2 ----
            button2.setText("First");
            button2.addActionListener(e -> button2ActionPerformed(e));
            panel4.add(button2);

            //---- button3 ----
            button3.setText("Previous");
            button3.addActionListener(e -> button3ActionPerformed(e));
            panel4.add(button3);
            panel4.add(comboBox1);

            //---- button4 ----
            button4.setText("Next");
            button4.addActionListener(e -> button4ActionPerformed(e));
            panel4.add(button4);

            //---- button5 ----
            button5.setText("Last");
            button5.addActionListener(e -> button5ActionPerformed(e));
            panel4.add(button5);

            //======== panel5 ========
            {
                panel5.setLayout(new GridBagLayout());
                ((GridBagLayout)panel5.getLayout()).columnWidths = new int[] {0, 0, 0};
                ((GridBagLayout)panel5.getLayout()).rowHeights = new int[] {0, 0};
                ((GridBagLayout)panel5.getLayout()).columnWeights = new double[] {0.0, 0.0, 1.0E-4};
                ((GridBagLayout)panel5.getLayout()).rowWeights = new double[] {0.0, 1.0E-4};

                //---- label12 ----
                label12.setText("Total Events:");
                panel5.add(label12, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 0, 5), 0, 0));

                //---- label16 ----
                label16.setText("50");
                panel5.add(label16, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 0, 0), 0, 0));
            }
            panel4.add(panel5);
        }
        contentPane.add(panel4, BorderLayout.SOUTH);
        setSize(1170, 420);
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents

        //create ResultSetTableModel and display database table
        //End of creating ResultSetTableModel & display
        // dispose of window when user quits application (this overrides
        // the default of HIDE_ON_CLOSE)
        // ensure database connection is closed when user quits application
        // open the panel at first time, perform a default query
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private JScrollPane scrollPane1;
    private JPanel panel1;
    private JLabel label1;
    private JTextField textField1;
    private JLabel label2;
    private JComboBox<String> comboBox3;
    private JLabel label7;
    private JComboBox<String> comboBox4;
    private JLabel label8;
    private JComboBox<String> comboBox5;
    private JLabel label9;
    private JTextField textField2;
    private JLabel label5;
    private JComboBox<String> comboBox12;
    private JLabel label3;
    private JComboBox<String> comboBox6;
    private JLabel label6;
    private JComboBox<String> comboBox10;
    private JLabel label13;
    private JPanel panel2;
    private JLabel label14;
    private JSpinner spinner2;
    private JLabel label15;
    private JSpinner spinner1;
    private JLabel label11;
    private JComboBox<String> comboBox11;
    private JLabel label4;
    private JComboBox<String> comboBox9;
    private JPanel panel3;
    private JButton button1;
    private JScrollPane scrollPane2;
    private JTable table1;
    private JPanel panel4;
    private JButton button2;
    private JButton button3;
    private JComboBox comboBox1;
    private JButton button4;
    private JButton button5;
    private JPanel panel5;
    private JLabel label12;
    private JLabel label16;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
//     execute application
//    public static void main( String args[] )
//    {
//    }
}
