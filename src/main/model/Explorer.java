/*
 * Created by JFormDesigner on Sun Mar 31 10:27:38 CST 2019
 */

package main.model;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.tree.*;

import bean.EventAttribute;
import dao.EventMapper;
import main.model.clusters.EM.EMcluster;
import main.model.clusters.SimpleKMeans.KMeans;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import weka.clusterers.Clusterer;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.CSVSaver;
import weka.core.converters.ConverterUtils;
import weka.experiment.InstanceQuery;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Normalize;
import weka.filters.unsupervised.attribute.Remove;
import weka.filters.unsupervised.instance.RemoveWithValues;
import weka.gui.GenericObjectEditor;
import weka.gui.explorer.ClustererAssignmentsPlotInstances;
import weka.gui.visualize.PlotData2D;
import weka.gui.visualize.VisualizePanel;

/**
 * @author Brainrain
 */
public class Explorer extends JFrame {
    public Integer total;
    public Integer attr_total;
    private ArrayList<EventAttribute> at;
    //attribute selected table
    private DefaultTableModel tableModel1;
    //a data set from database
    private Instances dataset = null;
    private Instances newDataSet = null;
    //component list
    private static String[] flist;
    //index system
    private ReadIndex index;
    private Instances eventScores;
    //cluster setting
    String[] options = new String[]{"-N","5"};
    public static String[] getFlist() {
        return flist;
    }

    //tableModel1
    private Object data[][];
    private Object colunmNames[];
//    private ArrayList<Integer> removeRow = new ArrayList<>();

    public Explorer(){
        try {
            initComponents();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public SqlSession getSession(){
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
        return session;
    }

    // get total data and attributes
    public void count(){
        total = dataset.numInstances();
        attr_total = dataset.numAttributes();
        label2.setText(this.attr_total.toString());
        label4.setText(this.total.toString());
        label6.setText(this.attr_total.toString()+" (100%) ");
        label8.setText(this.total.toString()+" (100%) ");
    }

    // attributes analysis table
    public void setDataAndColumn(){
        //select attributes table
        SqlSession session = getSession();
        try{
            // set query and execute it
            EventMapper mapper = session.getMapper(EventMapper.class);
            at = mapper.getAttributesAll(); //get event attributes
            colunmNames = new String[]{"Exclude","No.","Name","Missing","Type"};
            data = new Object[at.size()][colunmNames.length];
            flist = new String[at.size()];
            //get database data and column into array,the eventid into HashMap
            for(int i=0;i<at.size();i++){
                data[i][0] = Boolean.FALSE;
                data[i][1] = at.get(i).getA_id();
                data[i][2] = at.get(i).getAttribute_name();
                flist[i]=at.get(i).getAttribute_name();
                data[i][3] = at.get(i).getMissing();
                data[i][4] = at.get(i).getType();
            }
        }
        finally {
            session.close();
        }
    }

    public void setTabel(){
        tableModel1 = new DefaultTableModel(data,colunmNames){
            /**=========IMPORTANT==========
             * return column classs,if not,boolean served as string
             * */
            public Class<?> getColumnClass(int columnIndex) {
                switch (columnIndex){
                    case 0: return Boolean.class;
                    case 1: return Integer.class;
                    case 2: return String.class;
                    default: return String.class;
                }
            }
            public boolean isCellEditable(int row, int col) {
                //Note that the data/cell address is constant,
                //no matter where the cell appears onscreen.
                if (col == 0) {
                    return true;
                } else {
                    return false;
                }
            }
        };
        table1.setModel(tableModel1);
        //set columns width
        TableColumnModel columnModel = table1.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(10);
        //add event to table
        table1.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {

            }
        });
    }

    private void button3ActionPerformed(ActionEvent e) {
        // TODO add your code here
        InstanceQuery query = null;
        String min="0";String max="0";
        String sql = "select * from tevent_all_info";
        if(!radioButton5.isSelected()) {
            min = spinner9.getValue().toString();
            max = spinner11.getValue().toString();
            sql = sql.concat(" limit "+min+","+max);
        }
        try {
            query = new InstanceQuery();
            query.setDatabaseURL("jdbc:mysql://localhost:3306/gtd_data");
            query.setUsername("root");
            query.setPassword("abcd=!@#");
            query.setQuery(sql);
            dataset = query.retrieveInstances();
        } catch (Exception i) {
            i.printStackTrace();
        }
        count();
        setDataAndColumn();
        setTabel();
        list1.setModel(new AbstractListModel<String>() {
            String[] value = Explorer.getFlist();
            @Override
            public int getSize() {
                return value.length;
            }
            @Override
            public String getElementAt(int index) {
                return value[index];
            }
        });    //filling list
    }

    //============data cleaning==============
    /**remove instance and attributes
     * method:
     *  countmissing()
     *  getAttrMissingMoreThan()
     *
     * **/
    public String getRemoveAttrList(Double limit){
        String list = "#";  //默认删除第一个属性idx
        for (int i = 0; i < newDataSet.numAttributes(); i++) {
            if(100*Double.parseDouble(data[i][3].toString())/total >= limit){
                list = list.concat(","+(i+1));     //remove the attribute if its missing rate is more than the limit
            }
            else if(checkBox1.isSelected()&&newDataSet.attribute(i).name().equals("IDX")){
                list = list.concat(","+(i+1));  //remove idx even though checkbox is not selected
            }
            else if (checkBox2.isSelected()&&(newDataSet.attribute(i).name().equals("iyear")||
                    newDataSet.attribute(i).name().equals("imonth")||
                    newDataSet.attribute(i).name().equals("iday")||
                    newDataSet.attribute(i).name().equals("approxdate")||
                    newDataSet.attribute(i).name().equals("resolution"))){
                list = list.concat(","+(i+1));     //remove attribute of date type
                continue;
            }
            else if(checkBox3.isSelected()&&newDataSet.attribute(i).isString()){
                list = list.concat(","+(i+1));     //remove attribute of text type
                continue;
            }
        }
        list = list.substring(2);
        return list;
    }

    public void filterInstance(double limit){
        for(int i=newDataSet.numInstances()-1;i>=0;i--){
            Instance inst = newDataSet.get(i);
            /*remove the instance if its missing rate is more than the limit*/
            if(countmissing(inst) >= limit)
                newDataSet.remove(inst);
        }
    }
    public double countmissing(Instance inst){
        int count_missing = 0;
        for (int j=0;j<inst.numAttributes();j++){
            if(!(inst.attribute(j).isNumeric())){
                // "","?","-9","-99","Unknown" indicate that a value of the attribute is useless,and missing.
                if(inst.stringValue(j).equals("")||
                        inst.stringValue(j).equals("?")||
                        inst.stringValue(j).equals("-9")||
                        inst.stringValue(j).equals("-99")||
                        inst.stringValue(j).equals("Unknown"))
                    count_missing++;
            }
            else if(inst.isMissing(inst.attribute(j)))
                count_missing++;
        }
        return 100*count_missing/inst.numAttributes();
    }
    private void button9ActionPerformed(ActionEvent e) {
        // TODO add your code here
        if(e.getSource()==button9&&dataset==null) {
            JOptionPane.showMessageDialog(Explorer.this,
                    "Cannot find any data set.","Message",JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        this.newDataSet = null;
        newDataSet = new Instances(dataset);       //initialize a new instances with the same header as dataset
        /*filter instances if checkbox is selected
        remove instance: death is missing,property is unknown("property"=-9)* */
        if(checkBox4.isSelected()){
            newDataSet.deleteWithMissing(newDataSet.attribute("nkill"));
        }
        if(checkBox5.isSelected()){
            //class RemoveWithValue Filters instances according to the value of an attribute.
            RemoveWithValues rmvalue = new RemoveWithValues();
            String[] options = new String[4];
            options[0] = "-C";   // Choose attribute to be used for selection
            options[1] = "106"; // Attribute number,"property" I0)
            options[2] = "-L";
            options[3] = "-9";
            try {
                rmvalue.setOptions(options);
                rmvalue.setInputFormat(newDataSet);
                newDataSet = Filter.useFilter(newDataSet,rmvalue);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
        //filter attributes
        Double attrMissingRateLimit = Double.valueOf(spinner1.getValue().toString());
        String rangeList = getRemoveAttrList(attrMissingRateLimit);
        Remove filter = new Remove();
        filter.setAttributeIndices(rangeList);
        try {
            filter.setInputFormat(newDataSet);
            newDataSet = Filter.useFilter(newDataSet,filter);
        } catch (Exception i) {
            i.printStackTrace();
        }
        //update attributes table
//        for(int i=removeRow.size()-1;i>=0;i--) table1.remove(removeRow.get(i));
        //filter instance according to limit
        Double instMissingRateLimit = Double.valueOf(spinner2.getValue().toString());
        try {
            filterInstance(instMissingRateLimit);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        //fill missing value
//        if(checkBox8.isSelected()){
//            ReplaceMissingWithUserConstant fill = new ReplaceMissingWithUserConstant();
//            String[] options = new String[4];
//            options[0]="-N";options[1]="0"; //Specify the replacement constant for nominal attributes·
//            options[2]="-R";options[3]="-1"; //Specify the replacement constant for numeric attributes(default: 0)
//            try {
//                fill.setOptions(options);
//                fill.setInputFormat(newDataSet);
//                newDataSet = Filter.useFilter(newDataSet,fill);
//            } catch (Exception e1) {
//                e1.printStackTrace();
//            }
//        }
        //else { self define }
        String attrout = "";
        for(int i=0;i<newDataSet.numAttributes();i++)
        {
            attrout = attrout.concat(newDataSet.attribute(i).toString()+"\n");
        }
        textArea1.setText(attrout);
        //reset remaining attributes and instances
        label6.setText(newDataSet.numAttributes()+" ("+(100*newDataSet.numAttributes()/attr_total)+"%) ");
        label8.setText(newDataSet.numInstances()+" ("+(100*newDataSet.numInstances()/total)+"%) ");
    }

    private void checkBox8ActionPerformed(ActionEvent e) {
        // TODO add your code here
        if(e.getSource()==checkBox8&&checkBox8.isSelected()){
            textField1.setEditable(false);
        }
        else textField1.setEditable(true);
    }
    //===data cleaning end==============

    //Progress dialog
    public void openProgressDialog(){
        dialog1.setVisible(true);
        int timerDelay = 10;
        new javax.swing.Timer(timerDelay , new ActionListener() {
            private int index = 0;
            private int maxIndex = 100;
            public void actionPerformed(ActionEvent e) {
                if (index < maxIndex) {
                    progressBar1.setValue(index);
                    index++;
                } else {
                    progressBar1.setValue(maxIndex);
                    ((javax.swing.Timer)e.getSource()).stop(); // stop the timer
                }
            }
        }).start();
        progressBar1.setValue(progressBar1.getMinimum());
        addWindowListener(
                new WindowAdapter(){
                    //关闭弹窗后触发事件
                    public void windowClosed(WindowEvent event){
                        dialog1.setVisible(false);
                    }
        }
        );
    }

    private void tree1ValueChanged(TreeSelectionEvent e) {
        // TODO add your code here
        JTree tree = (JTree) e.getSource();
        //利用JTree的getLastSelectedPathComponent()方法取得目前选取的节点.
        DefaultMutableTreeNode selectionNode =
                (DefaultMutableTreeNode)tree.getLastSelectedPathComponent();
//        String nodeName = selectionNode.toString();
        //判断是否为树叶节点，若是则显示文件内容，若不是则不做任何事。
        if (selectionNode.isLeaf()){
            index = new ReadIndex();
            textArea2.setText(index.getStrindex());
        }
    }

    //set weight for attributes,and compute scores for remaining instances
    private void button6ActionPerformed(ActionEvent e) {
        // TODO add your code here
        if(this.index!=null&&newDataSet!=null){
            WeightHandler cal = new WeightHandler(this.index.getUserList());
            eventScores = cal.Socores(newDataSet);
            //标准化
            if(radioButton4.isSelected()){
                Normalize normalize = new Normalize();
                try {
                    normalize.setInputFormat(eventScores);
                    eventScores = Filter.useFilter(eventScores,normalize);
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        }
        else if(newDataSet==null)
            JOptionPane.showMessageDialog(Explorer.this,
                    "Cannot find any data set.","Message",JOptionPane.INFORMATION_MESSAGE);
        else JOptionPane.showMessageDialog(Explorer.this,"Please select an index system.",
                    "Message",JOptionPane.INFORMATION_MESSAGE);
    }
    //进度条完成时
    private void progressBar1StateChanged(ChangeEvent e) {
        // TODO add your code here
        int value = progressBar1.getValue();
        if(progressBar1.getValue()==100){
            label9.setText("Finished.");
        }
    }
    //打开据类型配置窗口
    private void button5ActionPerformed(ActionEvent e) {
        // TODO add your code here
        if(e.getSource()==button5&&comboBox1.getSelectedItem().toString().equals("EM")){
            frame1.setVisible(true);
        }
        else if(e.getSource()==button5&&comboBox1.getSelectedItem().toString().equals("SimpleKMeans")){
            frame2.setVisible(true);
        }
        else
            JOptionPane.showMessageDialog(Explorer.this,"Please select a Clusterer.",
                    "Message",JOptionPane.INFORMATION_MESSAGE);
    }

    //get cluster options
    private void button10ActionPerformed(ActionEvent e) {
        // TODO add your code here
        if(e.getSource()==button10){
            options = new String[10];
            options[0] = "-N";
            options[1] = spinner3.getValue()+"";
            options[2] = "-X";
            options[3] = spinner4.getValue()+"";
            options[4] = "-K";
            options[5] = spinner5.getValue()+"";
            options[6] = "-I";
            options[7] = spinner6.getValue()+"";
            options[8] = "-S";
            options[9] = spinner13.getValue()+"";
        }
        String text = "EM:";
        for(String i:options){
            text = text.concat(i+";");
        }
        textField2.setText(text);
    }

    private void button11ActionPerformed(ActionEvent e) {
        // TODO add your code here
        if(e.getSource()==button11){
            options = new String[6];
            options[0] = "-N";
            options[1] = spinner7.getValue()+"";
            options[2] = "-I";
            options[3] = spinner8.getValue()+"";
            options[4] = "-S";
            options[5] = spinner12.getValue()+"";
        }
        String text = "SimpleKMeans:";
        for(String i:options){
            text = text.concat(i+";");
        }
        textField2.setText(text);
    }
    //start clusterer
    private void button12ActionPerformed(ActionEvent e) {
        // TODO add your code here
        if(eventScores==null){
            JOptionPane.showMessageDialog(Explorer.this,"Cannot find any data set",
                    "Message",JOptionPane.INFORMATION_MESSAGE);
        }
        else if(comboBox1.getSelectedItem().toString().equals("EM")){
            if(radioButton2.isSelected()){
                ////split train set and test set
                try {
                    int trainsize = Math.round(this.eventScores.numInstances()*
                            Integer.valueOf(spinner10.getValue().toString())/100);
                    int testsize = eventScores.numInstances() - trainsize;
                    Instances trainset = new Instances(eventScores,0,trainsize);
                    Instances testset = new Instances(eventScores,trainsize,testsize);
                    EMcluster em = new EMcluster(this.options);
                    em.startEMcluster(trainset,testset);
                    textArea3.setText(em.getPrintEvalution()+em.getPrintTestResult());
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
            else{
                try {
                    EMcluster em = new EMcluster(this.options);
                    em.startEMcluster(this.eventScores);
                    textArea3.setText(em.getPrintEvalution());
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        }
        else if(comboBox1.getSelectedItem().toString().equals("SimpleKMeans")){
            if(radioButton2.isSelected()){
                ////split train set and test set
                int trainsize = Math.round(this.eventScores.numInstances()*
                        Integer.valueOf(spinner10.getValue().toString())/100);
                int testsize = eventScores.numInstances() - trainsize;
                Instances trainset = new Instances(eventScores,0,trainsize);
                Instances testset = new Instances(eventScores,trainsize,testsize);
                try {
                    KMeans km = new KMeans(this.options);
                    km.startKMeans(trainset,testset);
                    textArea3.setText(km.getPrintTrainEvalution()+
                            "\n=== Model and evaluation on test split ===\n"+
                            km.getPrintTestEvalution()+
                            km.getPrintTestResult());
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
            else{
                try {
                    KMeans km = new KMeans(this.options);
                    km.startKMeans(this.eventScores);
                    textArea3.setText(km.getPrintEvalution());
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        }
        if(radioButton3.isSelected()){
            //======IMPORTANT=======
            VisualizePanel temp_vp = new VisualizePanel();
            temp_vp.setShowClassPanel(true);
            PlotData2D points = new PlotData2D(eventScores);
            temp_vp.setInstances(eventScores);
            try {
                temp_vp.addPlot(points);
                if(temp_vp!=null){
                    panel9.add(temp_vp,BorderLayout.CENTER);
                }
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
    }

    private void radioButton2ActionPerformed(ActionEvent e) {
        // TODO add your code here
        if (radioButton2.isSelected())
            spinner10.setEnabled(true);
    }

    private void radioButton1ActionPerformed(ActionEvent e) {
        // TODO add your code here
        if(radioButton1.isSelected())
            spinner10.setEnabled(false);
    }

    private void radioButton5ActionPerformed(ActionEvent e) {
        // TODO add your code here
        if(e.getSource()==radioButton5&&radioButton5.isSelected()){
            spinner9.setEnabled(false);
            spinner11.setEnabled(false);
        }
        else {
            spinner9.setEnabled(true);
            spinner11.setEnabled(true);
        }
    }
// 保存数据集至csv
    private void button13ActionPerformed(ActionEvent e) {
        // TODO add your code here
        CSVSaver saver =new CSVSaver();
        saver.setInstances(eventScores);
        try {
            saver.setFile(new File("F:\\论文\\2019-2020毕业论文\\projectDM\\src\\config\\output\\score.csv"));
            saver.writeBatch();
            JOptionPane.showMessageDialog(Explorer.this,"Saved!",
                    "Message",JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    private void initComponents() throws SQLException {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        tabbedPane1 = new JTabbedPane();
        panel3 = new JPanel();
        scrollPane2 = new JScrollPane();
        panel10 = new JPanel();
        panel13 = new JPanel();
        label29 = new JLabel();
        spinner9 = new JSpinner();
        spinner11 = new JSpinner();
        radioButton5 = new JRadioButton();
        button3 = new JButton();
        panel4 = new JPanel();
        label1 = new JLabel();
        label2 = new JLabel();
        label5 = new JLabel();
        label6 = new JLabel();
        label3 = new JLabel();
        label4 = new JLabel();
        label7 = new JLabel();
        label8 = new JLabel();
        panel6 = new JPanel();
        label18 = new JLabel();
        label17 = new JLabel();
        spinner1 = new JSpinner();
        label21 = new JLabel();
        checkBox1 = new JCheckBox();
        label22 = new JLabel();
        checkBox2 = new JCheckBox();
        label23 = new JLabel();
        checkBox3 = new JCheckBox();
        label10 = new JLabel();
        checkBox6 = new JCheckBox();
        label11 = new JLabel();
        checkBox7 = new JCheckBox();
        separator2 = new JSeparator();
        label12 = new JLabel();
        label13 = new JLabel();
        checkBox8 = new JCheckBox();
        scrollPane3 = new JScrollPane();
        list1 = new JList();
        textField1 = new JTextField();
        button4 = new JButton();
        separator1 = new JSeparator();
        label19 = new JLabel();
        label20 = new JLabel();
        spinner2 = new JSpinner();
        label24 = new JLabel();
        checkBox4 = new JCheckBox();
        label25 = new JLabel();
        checkBox5 = new JCheckBox();
        button9 = new JButton();
        panel5 = new JPanel();
        button1 = new JButton();
        button2 = new JButton();
        scrollPane1 = new JScrollPane();
        table1 = new JTable();
        scrollPane4 = new JScrollPane();
        textArea1 = new JTextArea();
        button8 = new JButton();
        panel11 = new JPanel();
        scrollPane5 = new JScrollPane();
        tree1 = new JTree();
        scrollPane6 = new JScrollPane();
        textArea2 = new JTextArea();
        button7 = new JButton();
        radioButton4 = new JRadioButton();
        button6 = new JButton();
        button13 = new JButton();
        panel8 = new JPanel();
        panel1 = new JPanel();
        comboBox1 = new JComboBox<>();
        textField2 = new JTextField();
        button5 = new JButton();
        panel12 = new JPanel();
        panel14 = new JPanel();
        panel15 = new JPanel();
        radioButton1 = new JRadioButton();
        radioButton2 = new JRadioButton();
        label32 = new JLabel();
        spinner10 = new JSpinner();
        radioButton3 = new JRadioButton();
        button12 = new JButton();
        scrollPane7 = new JScrollPane();
        textArea3 = new JTextArea();
        panel9 = new JPanel();
        dialog1 = new JDialog();
        label9 = new JLabel();
        progressBar1 = new JProgressBar();
        frame1 = new JFrame();
        panel2 = new JPanel();
        label14 = new JLabel();
        spinner3 = new JSpinner();
        label15 = new JLabel();
        spinner4 = new JSpinner();
        label16 = new JLabel();
        spinner5 = new JSpinner();
        label26 = new JLabel();
        spinner6 = new JSpinner();
        label31 = new JLabel();
        spinner13 = new JSpinner();
        button10 = new JButton();
        frame2 = new JFrame();
        panel7 = new JPanel();
        label27 = new JLabel();
        spinner7 = new JSpinner();
        label28 = new JLabel();
        spinner8 = new JSpinner();
        label30 = new JLabel();
        spinner12 = new JSpinner();
        button11 = new JButton();

        //======== this ========
        setTitle("Model");
        setMinimumSize(new Dimension(800, 700));
        Container contentPane = getContentPane();
        contentPane.setLayout(new GridBagLayout());
        ((GridBagLayout)contentPane.getLayout()).columnWidths = new int[] {0, 0};
        ((GridBagLayout)contentPane.getLayout()).rowHeights = new int[] {0, 0, 0, 0};
        ((GridBagLayout)contentPane.getLayout()).columnWeights = new double[] {1.0, 1.0E-4};
        ((GridBagLayout)contentPane.getLayout()).rowWeights = new double[] {0.0, 1.0, 0.0, 1.0E-4};

        //======== tabbedPane1 ========
        {

            //======== panel3 ========
            {
                panel3.setLayout(new GridBagLayout());
                ((GridBagLayout)panel3.getLayout()).columnWidths = new int[] {331, 0, 0};
                ((GridBagLayout)panel3.getLayout()).rowHeights = new int[] {0, 0, 0, 0, 0};
                ((GridBagLayout)panel3.getLayout()).columnWeights = new double[] {1.0, 1.0, 1.0E-4};
                ((GridBagLayout)panel3.getLayout()).rowWeights = new double[] {1.0, 1.0, 1.0, 0.0, 1.0E-4};

                //======== scrollPane2 ========
                {

                    //======== panel10 ========
                    {
                        panel10.setLayout(new GridBagLayout());
                        ((GridBagLayout)panel10.getLayout()).columnWidths = new int[] {0, 0, 0};
                        ((GridBagLayout)panel10.getLayout()).rowHeights = new int[] {0, 0, 0, 0, 0, 0, 0, 0, 0};
                        ((GridBagLayout)panel10.getLayout()).columnWeights = new double[] {1.0, 1.0, 1.0E-4};
                        ((GridBagLayout)panel10.getLayout()).rowWeights = new double[] {0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 1.0, 0.0, 1.0E-4};

                        //======== panel13 ========
                        {
                            panel13.setLayout(new GridBagLayout());
                            ((GridBagLayout)panel13.getLayout()).columnWidths = new int[] {0, 0, 0, 0, 0, 0};
                            ((GridBagLayout)panel13.getLayout()).rowHeights = new int[] {0, 0};
                            ((GridBagLayout)panel13.getLayout()).columnWeights = new double[] {0.0, 1.0, 1.0, 0.0, 1.0, 1.0E-4};
                            ((GridBagLayout)panel13.getLayout()).rowWeights = new double[] {0.0, 1.0E-4};

                            //---- label29 ----
                            label29.setText("Limit");
                            panel13.add(label29, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                                GridBagConstraints.EAST, GridBagConstraints.VERTICAL,
                                new Insets(0, 0, 0, 5), 0, 0));

                            //---- spinner9 ----
                            spinner9.setModel(new SpinnerNumberModel(0, 0, null, 1));
                            spinner9.setEnabled(false);
                            panel13.add(spinner9, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
                                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                                new Insets(0, 0, 0, 5), 0, 0));

                            //---- spinner11 ----
                            spinner11.setModel(new SpinnerNumberModel(8000, 1, null, 1));
                            spinner11.setEnabled(false);
                            panel13.add(spinner11, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0,
                                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                                new Insets(0, 0, 0, 5), 0, 0));

                            //---- radioButton5 ----
                            radioButton5.setText("All");
                            radioButton5.setSelected(true);
                            radioButton5.addActionListener(e -> radioButton5ActionPerformed(e));
                            panel13.add(radioButton5, new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0,
                                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                                new Insets(0, 0, 0, 5), 0, 0));

                            //---- button3 ----
                            button3.setText("Open DB...");
                            button3.setToolTipText("Get all data by defalut.");
                            button3.addActionListener(e -> button3ActionPerformed(e));
                            panel13.add(button3, new GridBagConstraints(4, 0, 1, 1, 0.0, 0.0,
                                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                                new Insets(0, 0, 0, 0), 0, 0));
                        }
                        panel10.add(panel13, new GridBagConstraints(0, 0, 2, 1, 0.0, 0.0,
                            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                            new Insets(5, 0, 5, 0), 0, 0));

                        //======== panel4 ========
                        {
                            panel4.setBorder(new TitledBorder("Instances inforamtion"));
                            panel4.setLayout(new GridBagLayout());
                            ((GridBagLayout)panel4.getLayout()).columnWidths = new int[] {0, 0, 0, 0, 0};
                            ((GridBagLayout)panel4.getLayout()).rowHeights = new int[] {0, 0, 0, 0};
                            ((GridBagLayout)panel4.getLayout()).columnWeights = new double[] {0.0, 1.0, 0.0, 1.0, 1.0E-4};
                            ((GridBagLayout)panel4.getLayout()).rowWeights = new double[] {0.0, 0.0, 0.0, 1.0E-4};

                            //---- label1 ----
                            label1.setText("Attributes:");
                            panel4.add(label1, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                                new Insets(0, 0, 5, 5), 0, 0));

                            //---- label2 ----
                            label2.setText("0");
                            panel4.add(label2, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
                                GridBagConstraints.WEST, GridBagConstraints.VERTICAL,
                                new Insets(0, 0, 5, 5), 0, 0));

                            //---- label5 ----
                            label5.setText("Remaining attributes:");
                            panel4.add(label5, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0,
                                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                                new Insets(0, 0, 5, 5), 0, 0));

                            //---- label6 ----
                            label6.setText("0");
                            panel4.add(label6, new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0,
                                GridBagConstraints.WEST, GridBagConstraints.VERTICAL,
                                new Insets(0, 0, 5, 0), 0, 0));

                            //---- label3 ----
                            label3.setText("Instances:");
                            panel4.add(label3, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
                                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                                new Insets(0, 0, 5, 5), 0, 0));

                            //---- label4 ----
                            label4.setText("0");
                            panel4.add(label4, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0,
                                GridBagConstraints.WEST, GridBagConstraints.VERTICAL,
                                new Insets(0, 0, 5, 5), 0, 0));

                            //---- label7 ----
                            label7.setText("Remaining Instances:");
                            panel4.add(label7, new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0,
                                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                                new Insets(0, 0, 5, 5), 0, 0));

                            //---- label8 ----
                            label8.setText("0");
                            panel4.add(label8, new GridBagConstraints(3, 1, 1, 1, 0.0, 0.0,
                                GridBagConstraints.WEST, GridBagConstraints.VERTICAL,
                                new Insets(0, 0, 5, 0), 0, 0));
                        }
                        panel10.add(panel4, new GridBagConstraints(0, 1, 2, 1, 0.0, 0.0,
                            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                            new Insets(0, 0, 5, 0), 0, 0));

                        //======== panel6 ========
                        {
                            panel6.setBorder(new TitledBorder("Regulation"));
                            panel6.setLayout(new GridBagLayout());
                            ((GridBagLayout)panel6.getLayout()).columnWidths = new int[] {0, 0, 0, 0};
                            ((GridBagLayout)panel6.getLayout()).rowHeights = new int[] {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
                            ((GridBagLayout)panel6.getLayout()).columnWeights = new double[] {1.0, 1.0, 1.0, 1.0E-4};
                            ((GridBagLayout)panel6.getLayout()).rowWeights = new double[] {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0E-4};

                            //---- label18 ----
                            label18.setText("Attributes");
                            panel6.add(label18, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                                new Insets(0, 0, 5, 5), 0, 0));

                            //---- label17 ----
                            label17.setText("Missing rate(%) >=");
                            panel6.add(label17, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
                                GridBagConstraints.EAST, GridBagConstraints.VERTICAL,
                                new Insets(0, 0, 5, 5), 0, 0));
                            panel6.add(spinner1, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0,
                                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                                new Insets(0, 0, 5, 0), 0, 0));

                            //---- label21 ----
                            label21.setText("Remove id");
                            panel6.add(label21, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0,
                                GridBagConstraints.EAST, GridBagConstraints.VERTICAL,
                                new Insets(0, 0, 5, 5), 0, 0));
                            panel6.add(checkBox1, new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0,
                                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                                new Insets(0, 0, 5, 0), 0, 0));

                            //---- label22 ----
                            label22.setText("Remove date");
                            panel6.add(label22, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0,
                                GridBagConstraints.EAST, GridBagConstraints.VERTICAL,
                                new Insets(0, 0, 5, 5), 0, 0));
                            panel6.add(checkBox2, new GridBagConstraints(2, 2, 1, 1, 0.0, 0.0,
                                GridBagConstraints.WEST, GridBagConstraints.VERTICAL,
                                new Insets(0, 0, 5, 0), 0, 0));

                            //---- label23 ----
                            label23.setText("Remove text");
                            panel6.add(label23, new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0,
                                GridBagConstraints.EAST, GridBagConstraints.VERTICAL,
                                new Insets(0, 0, 5, 5), 0, 0));
                            panel6.add(checkBox3, new GridBagConstraints(2, 3, 1, 1, 0.0, 0.0,
                                GridBagConstraints.WEST, GridBagConstraints.VERTICAL,
                                new Insets(0, 0, 5, 0), 0, 0));

                            //---- label10 ----
                            label10.setText("Remove subtype");
                            panel6.add(label10, new GridBagConstraints(1, 4, 1, 1, 0.0, 0.0,
                                GridBagConstraints.EAST, GridBagConstraints.VERTICAL,
                                new Insets(0, 0, 5, 5), 0, 0));
                            panel6.add(checkBox6, new GridBagConstraints(2, 4, 1, 1, 0.0, 0.0,
                                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                                new Insets(0, 0, 5, 0), 0, 0));

                            //---- label11 ----
                            label11.setText("Remove booleantype");
                            panel6.add(label11, new GridBagConstraints(1, 5, 1, 1, 0.0, 0.0,
                                GridBagConstraints.EAST, GridBagConstraints.VERTICAL,
                                new Insets(0, 0, 5, 5), 0, 0));
                            panel6.add(checkBox7, new GridBagConstraints(2, 5, 1, 1, 0.0, 0.0,
                                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                                new Insets(0, 0, 5, 0), 0, 0));
                            panel6.add(separator2, new GridBagConstraints(0, 6, 3, 1, 0.0, 0.0,
                                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                                new Insets(0, 0, 5, 0), 0, 0));

                            //---- label12 ----
                            label12.setText("Filling");
                            panel6.add(label12, new GridBagConstraints(0, 7, 1, 1, 0.0, 0.0,
                                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                                new Insets(0, 0, 5, 5), 0, 0));

                            //---- label13 ----
                            label13.setText("Default");
                            panel6.add(label13, new GridBagConstraints(1, 7, 1, 1, 0.0, 0.0,
                                GridBagConstraints.EAST, GridBagConstraints.VERTICAL,
                                new Insets(0, 0, 5, 5), 0, 0));

                            //---- checkBox8 ----
                            checkBox8.addActionListener(e -> checkBox8ActionPerformed(e));
                            panel6.add(checkBox8, new GridBagConstraints(2, 7, 1, 1, 0.0, 0.0,
                                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                                new Insets(0, 0, 5, 0), 0, 0));

                            //======== scrollPane3 ========
                            {
                                scrollPane3.setViewportView(list1);
                            }
                            panel6.add(scrollPane3, new GridBagConstraints(1, 8, 1, 3, 0.0, 0.0,
                                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                                new Insets(0, 0, 5, 5), 0, 0));
                            panel6.add(textField1, new GridBagConstraints(2, 8, 1, 1, 0.0, 0.0,
                                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                                new Insets(0, 0, 5, 0), 0, 0));

                            //---- button4 ----
                            button4.setText("Save");
                            panel6.add(button4, new GridBagConstraints(2, 9, 1, 1, 0.0, 0.0,
                                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                                new Insets(0, 0, 5, 0), 0, 0));
                            panel6.add(separator1, new GridBagConstraints(0, 11, 3, 1, 0.0, 0.0,
                                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                                new Insets(0, 0, 5, 0), 0, 0));

                            //---- label19 ----
                            label19.setText("Instance");
                            panel6.add(label19, new GridBagConstraints(0, 12, 1, 1, 0.0, 0.0,
                                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                                new Insets(0, 0, 5, 5), 0, 0));

                            //---- label20 ----
                            label20.setText("Missing rate(%) >=");
                            panel6.add(label20, new GridBagConstraints(1, 12, 1, 1, 0.0, 0.0,
                                GridBagConstraints.EAST, GridBagConstraints.VERTICAL,
                                new Insets(0, 0, 5, 5), 0, 0));
                            panel6.add(spinner2, new GridBagConstraints(2, 12, 1, 1, 0.0, 0.0,
                                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                                new Insets(0, 0, 5, 0), 0, 0));

                            //---- label24 ----
                            label24.setText("Remove death unknown");
                            panel6.add(label24, new GridBagConstraints(1, 13, 1, 1, 0.0, 0.0,
                                GridBagConstraints.EAST, GridBagConstraints.VERTICAL,
                                new Insets(0, 0, 5, 5), 0, 0));
                            panel6.add(checkBox4, new GridBagConstraints(2, 13, 1, 1, 0.0, 0.0,
                                GridBagConstraints.WEST, GridBagConstraints.VERTICAL,
                                new Insets(0, 0, 5, 0), 0, 0));

                            //---- label25 ----
                            label25.setText("Remove property unknown");
                            panel6.add(label25, new GridBagConstraints(1, 14, 1, 1, 0.0, 0.0,
                                GridBagConstraints.EAST, GridBagConstraints.VERTICAL,
                                new Insets(0, 0, 5, 5), 0, 0));
                            panel6.add(checkBox5, new GridBagConstraints(2, 14, 1, 1, 0.0, 0.0,
                                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                                new Insets(0, 0, 5, 0), 0, 0));

                            //---- button9 ----
                            button9.setText("Remove");
                            button9.addActionListener(e -> button9ActionPerformed(e));
                            panel6.add(button9, new GridBagConstraints(1, 15, 1, 1, 0.0, 0.0,
                                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                                new Insets(0, 0, 0, 5), 0, 0));
                        }
                        panel10.add(panel6, new GridBagConstraints(0, 3, 2, 5, 0.0, 0.0,
                            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                            new Insets(0, 0, 0, 0), 0, 0));
                    }
                    scrollPane2.setViewportView(panel10);
                }
                panel3.add(scrollPane2, new GridBagConstraints(0, 0, 1, 4, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 0, 5), 0, 0));

                //======== panel5 ========
                {
                    panel5.setBorder(new TitledBorder("Attributes"));
                    panel5.setLayout(new GridBagLayout());
                    ((GridBagLayout)panel5.getLayout()).columnWidths = new int[] {85, 0, 105, 229, 0};
                    ((GridBagLayout)panel5.getLayout()).rowHeights = new int[] {0, 0, 0, 0, 0, 0};
                    ((GridBagLayout)panel5.getLayout()).columnWeights = new double[] {1.0, 1.0, 0.0, 1.0, 1.0E-4};
                    ((GridBagLayout)panel5.getLayout()).rowWeights = new double[] {0.0, 1.0, 1.0, 1.0, 0.0, 1.0E-4};

                    //---- button1 ----
                    button1.setText("All");
                    panel5.add(button1, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 0, 5, 5), 0, 0));

                    //---- button2 ----
                    button2.setText("None");
                    panel5.add(button2, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 0, 5, 5), 0, 0));

                    //======== scrollPane1 ========
                    {

                        //---- table1 ----
                        table1.setSurrendersFocusOnKeystroke(true);
                        scrollPane1.setViewportView(table1);
                    }
                    panel5.add(scrollPane1, new GridBagConstraints(0, 1, 3, 3, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 0, 5, 5), 0, 0));

                    //======== scrollPane4 ========
                    {

                        //---- textArea1 ----
                        textArea1.setEditable(false);
                        scrollPane4.setViewportView(textArea1);
                    }
                    panel5.add(scrollPane4, new GridBagConstraints(3, 1, 1, 3, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 0, 5, 0), 0, 0));

                    //---- button8 ----
                    button8.setText("Remove");
                    panel5.add(button8, new GridBagConstraints(0, 4, 3, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 0, 0, 5), 0, 0));
                }
                panel3.add(panel5, new GridBagConstraints(1, 0, 1, 4, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 0, 0), 0, 0));
            }
            tabbedPane1.addTab("Preprocess", panel3);

            //======== panel11 ========
            {
                panel11.setLayout(new GridBagLayout());
                ((GridBagLayout)panel11.getLayout()).columnWidths = new int[] {0, 0, 0, 0, 0};
                ((GridBagLayout)panel11.getLayout()).rowHeights = new int[] {0, 0, 0, 0, 0, 65, 0, 0};
                ((GridBagLayout)panel11.getLayout()).columnWeights = new double[] {1.0, 1.0, 1.0, 1.0, 1.0E-4};
                ((GridBagLayout)panel11.getLayout()).rowWeights = new double[] {1.0, 1.0, 1.0, 0.0, 0.0, 1.0, 0.0, 1.0E-4};

                //======== scrollPane5 ========
                {
                    scrollPane5.setBorder(new TitledBorder("Select index system"));

                    //---- tree1 ----
                    tree1.setModel(new DefaultTreeModel(
                        new DefaultMutableTreeNode("(root)") {
                            {
                                add(new DefaultMutableTreeNode("index1"));
                                add(new DefaultMutableTreeNode("index2"));
                                add(new DefaultMutableTreeNode("index3"));
                            }
                        }));
                    tree1.addTreeSelectionListener(e -> tree1ValueChanged(e));
                    scrollPane5.setViewportView(tree1);
                }
                panel11.add(scrollPane5, new GridBagConstraints(0, 0, 1, 6, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 20, 5, 5), 0, 0));

                //======== scrollPane6 ========
                {
                    scrollPane6.setBorder(new TitledBorder("View"));

                    //---- textArea2 ----
                    textArea2.setEditable(false);
                    textArea2.setFont(textArea2.getFont().deriveFont(textArea2.getFont().getSize() + 1f));
                    scrollPane6.setViewportView(textArea2);
                }
                panel11.add(scrollPane6, new GridBagConstraints(1, 0, 3, 6, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 5, 20), 0, 0));

                //---- button7 ----
                button7.setText("New");
                panel11.add(button7, new GridBagConstraints(0, 6, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 20, 0, 5), 0, 0));

                //---- radioButton4 ----
                radioButton4.setText("Normalize");
                panel11.add(radioButton4, new GridBagConstraints(1, 6, 1, 1, 0.0, 0.0,
                    GridBagConstraints.EAST, GridBagConstraints.VERTICAL,
                    new Insets(0, 0, 0, 5), 0, 0));

                //---- button6 ----
                button6.setText("Score");
                button6.addActionListener(e -> button6ActionPerformed(e));
                panel11.add(button6, new GridBagConstraints(2, 6, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 0, 25), 0, 0));

                //---- button13 ----
                button13.setText("Save");
                button13.setToolTipText("save data set");
                button13.addActionListener(e -> button13ActionPerformed(e));
                panel11.add(button13, new GridBagConstraints(3, 6, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 0, 20), 0, 0));
            }
            tabbedPane1.addTab("Index System", panel11);

            //======== panel8 ========
            {
                panel8.setLayout(new GridBagLayout());
                ((GridBagLayout)panel8.getLayout()).columnWidths = new int[] {0, 0};
                ((GridBagLayout)panel8.getLayout()).rowHeights = new int[] {0, 0, 0, 0};
                ((GridBagLayout)panel8.getLayout()).columnWeights = new double[] {1.0, 1.0E-4};
                ((GridBagLayout)panel8.getLayout()).rowWeights = new double[] {0.0, 1.0, 0.0, 1.0E-4};

                //======== panel1 ========
                {
                    panel1.setBorder(new TitledBorder("Select clusterer"));
                    panel1.setLayout(new GridBagLayout());
                    ((GridBagLayout)panel1.getLayout()).columnWidths = new int[] {0, 0, 0, 0, 0};
                    ((GridBagLayout)panel1.getLayout()).rowHeights = new int[] {0, 0, 0, 0};
                    ((GridBagLayout)panel1.getLayout()).columnWeights = new double[] {0.0, 1.0, 0.0, 1.0, 1.0E-4};
                    ((GridBagLayout)panel1.getLayout()).rowWeights = new double[] {0.0, 0.0, 0.0, 1.0E-4};

                    //---- comboBox1 ----
                    comboBox1.setModel(new DefaultComboBoxModel<>(new String[] {
                        "(null)",
                        "EM",
                        "SimpleKMeans"
                    }));
                    comboBox1.setBackground(Color.white);
                    panel1.add(comboBox1, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 0, 5, 5), 0, 0));

                    //---- textField2 ----
                    textField2.setEditable(false);
                    textField2.setBackground(Color.white);
                    panel1.add(textField2, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 0, 5, 5), 0, 0));

                    //---- button5 ----
                    button5.setText("setting");
                    button5.addActionListener(e -> button5ActionPerformed(e));
                    panel1.add(button5, new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 0, 5, 5), 0, 0));
                }
                panel8.add(panel1, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 5, 0), 0, 0));

                //======== panel12 ========
                {
                    panel12.setLayout(new GridBagLayout());
                    ((GridBagLayout)panel12.getLayout()).columnWidths = new int[] {0, 0, 0};
                    ((GridBagLayout)panel12.getLayout()).rowHeights = new int[] {0, 0};
                    ((GridBagLayout)panel12.getLayout()).columnWeights = new double[] {0.0, 1.0, 1.0E-4};
                    ((GridBagLayout)panel12.getLayout()).rowWeights = new double[] {1.0, 1.0E-4};

                    //======== panel14 ========
                    {
                        panel14.setLayout(new GridBagLayout());
                        ((GridBagLayout)panel14.getLayout()).columnWidths = new int[] {0, 0, 0, 0};
                        ((GridBagLayout)panel14.getLayout()).rowHeights = new int[] {0, 0, 0, 0};
                        ((GridBagLayout)panel14.getLayout()).columnWeights = new double[] {0.0, 0.0, 0.0, 1.0E-4};
                        ((GridBagLayout)panel14.getLayout()).rowWeights = new double[] {0.0, 0.0, 0.0, 1.0E-4};

                        //======== panel15 ========
                        {
                            panel15.setBorder(new TitledBorder("Cluster mode"));
                            panel15.setLayout(new GridBagLayout());
                            ((GridBagLayout)panel15.getLayout()).columnWidths = new int[] {0, 0, 0, 0};
                            ((GridBagLayout)panel15.getLayout()).rowHeights = new int[] {0, 0, 0, 0};
                            ((GridBagLayout)panel15.getLayout()).columnWeights = new double[] {0.0, 0.0, 0.0, 1.0E-4};
                            ((GridBagLayout)panel15.getLayout()).rowWeights = new double[] {0.0, 0.0, 0.0, 1.0E-4};

                            //---- radioButton1 ----
                            radioButton1.setText("Using training set");
                            radioButton1.setSelected(true);
                            radioButton1.addActionListener(e -> radioButton1ActionPerformed(e));
                            panel15.add(radioButton1, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                                new Insets(0, 0, 5, 5), 0, 0));

                            //---- radioButton2 ----
                            radioButton2.setText("Precentage split");
                            radioButton2.addActionListener(e -> radioButton2ActionPerformed(e));
                            panel15.add(radioButton2, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
                                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                                new Insets(0, 0, 5, 5), 0, 0));

                            //---- label32 ----
                            label32.setText("%");
                            panel15.add(label32, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0,
                                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                                new Insets(0, 0, 5, 5), 0, 0));

                            //---- spinner10 ----
                            spinner10.setModel(new SpinnerNumberModel(66, null, null, 1));
                            spinner10.setEnabled(false);
                            panel15.add(spinner10, new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0,
                                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                                new Insets(0, 0, 5, 0), 0, 0));

                            //---- radioButton3 ----
                            radioButton3.setText("Store clusters for visualization");
                            panel15.add(radioButton3, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0,
                                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                                new Insets(0, 0, 0, 5), 0, 0));
                        }
                        panel14.add(panel15, new GridBagConstraints(0, 0, 3, 1, 0.0, 0.0,
                            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                            new Insets(0, 0, 5, 0), 0, 0));

                        //---- button12 ----
                        button12.setText("Start");
                        button12.addActionListener(e -> button12ActionPerformed(e));
                        panel14.add(button12, new GridBagConstraints(0, 1, 3, 1, 0.0, 0.0,
                            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                            new Insets(0, 0, 5, 0), 0, 0));
                    }
                    panel12.add(panel14, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 0, 0, 5), 0, 0));

                    //======== scrollPane7 ========
                    {

                        //---- textArea3 ----
                        textArea3.setBorder(new TitledBorder("Clusterer Output"));
                        textArea3.setEditable(false);
                        scrollPane7.setViewportView(textArea3);
                    }
                    panel12.add(scrollPane7, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
                        GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                        new Insets(0, 0, 0, 0), 0, 0));
                }
                panel8.add(panel12, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 5, 0), 0, 0));
            }
            tabbedPane1.addTab("Cluster", panel8);

            //======== panel9 ========
            {
                panel9.setLayout(new BorderLayout());
            }
            tabbedPane1.addTab("Visualization", panel9);
        }
        contentPane.add(tabbedPane1, new GridBagConstraints(0, 0, 1, 2, 0.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets(0, 0, 5, 0), 0, 0));
        pack();
        setLocationRelativeTo(getOwner());

        //======== dialog1 ========
        {
            dialog1.setTitle("Progress");
            Container dialog1ContentPane = dialog1.getContentPane();
            dialog1ContentPane.setLayout(new GridBagLayout());
            ((GridBagLayout)dialog1ContentPane.getLayout()).columnWidths = new int[] {0, 0};
            ((GridBagLayout)dialog1ContentPane.getLayout()).rowHeights = new int[] {0, 0, 0, 0, 0};
            ((GridBagLayout)dialog1ContentPane.getLayout()).columnWeights = new double[] {1.0, 1.0E-4};
            ((GridBagLayout)dialog1ContentPane.getLayout()).rowWeights = new double[] {1.0, 0.0, 1.0, 1.0, 1.0E-4};

            //---- label9 ----
            label9.setText("The data are being processed...");
            dialog1ContentPane.add(label9, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.VERTICAL,
                new Insets(0, 0, 5, 0), 0, 0));

            //---- progressBar1 ----
            progressBar1.addChangeListener(e -> progressBar1StateChanged(e));
            dialog1ContentPane.add(progressBar1, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(0, 0, 5, 0), 0, 0));
            dialog1.pack();
            dialog1.setLocationRelativeTo(dialog1.getOwner());
        }

        //======== frame1 ========
        {
            frame1.setTitle("EM");
            frame1.setMinimumSize(new Dimension(50, 50));
            Container frame1ContentPane = frame1.getContentPane();
            frame1ContentPane.setLayout(new GridBagLayout());
            ((GridBagLayout)frame1ContentPane.getLayout()).columnWidths = new int[] {0, 0, 0, 0};
            ((GridBagLayout)frame1ContentPane.getLayout()).rowHeights = new int[] {0, 0, 0, 0, 0, 0};
            ((GridBagLayout)frame1ContentPane.getLayout()).columnWeights = new double[] {1.0, 1.0, 1.0, 1.0E-4};
            ((GridBagLayout)frame1ContentPane.getLayout()).rowWeights = new double[] {0.0, 0.0, 0.0, 0.0, 0.0, 1.0E-4};

            //======== panel2 ========
            {
                panel2.setLayout(new GridBagLayout());
                ((GridBagLayout)panel2.getLayout()).columnWidths = new int[] {0, 0, 0};
                ((GridBagLayout)panel2.getLayout()).rowHeights = new int[] {0, 0, 0, 0, 0, 0, 0, 0};
                ((GridBagLayout)panel2.getLayout()).columnWeights = new double[] {0.0, 1.0, 1.0E-4};
                ((GridBagLayout)panel2.getLayout()).rowWeights = new double[] {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0E-4};

                //---- label14 ----
                label14.setText("numClusters");
                panel2.add(label14, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
                    GridBagConstraints.EAST, GridBagConstraints.VERTICAL,
                    new Insets(0, 0, 5, 5), 0, 0));

                //---- spinner3 ----
                spinner3.setModel(new SpinnerNumberModel(-1, null, null, 1));
                panel2.add(spinner3, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 5, 0), 0, 0));

                //---- label15 ----
                label15.setText("numFolds");
                panel2.add(label15, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0,
                    GridBagConstraints.EAST, GridBagConstraints.VERTICAL,
                    new Insets(0, 0, 5, 5), 0, 0));

                //---- spinner4 ----
                spinner4.setModel(new SpinnerNumberModel(10, null, null, 1));
                panel2.add(spinner4, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 5, 0), 0, 0));

                //---- label16 ----
                label16.setText("numKMeansRuns");
                panel2.add(label16, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0,
                    GridBagConstraints.EAST, GridBagConstraints.VERTICAL,
                    new Insets(0, 0, 5, 5), 0, 0));

                //---- spinner5 ----
                spinner5.setModel(new SpinnerNumberModel(10, null, null, 1));
                panel2.add(spinner5, new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 5, 0), 0, 0));

                //---- label26 ----
                label26.setText("maxInterations");
                panel2.add(label26, new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0,
                    GridBagConstraints.EAST, GridBagConstraints.VERTICAL,
                    new Insets(0, 0, 5, 5), 0, 0));

                //---- spinner6 ----
                spinner6.setModel(new SpinnerNumberModel(100, null, null, 1));
                panel2.add(spinner6, new GridBagConstraints(1, 4, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 5, 0), 0, 0));

                //---- label31 ----
                label31.setText("seed");
                panel2.add(label31, new GridBagConstraints(0, 5, 1, 1, 0.0, 0.0,
                    GridBagConstraints.EAST, GridBagConstraints.VERTICAL,
                    new Insets(0, 0, 5, 5), 0, 0));

                //---- spinner13 ----
                spinner13.setModel(new SpinnerNumberModel(500, null, null, 1));
                panel2.add(spinner13, new GridBagConstraints(1, 5, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 5, 0), 0, 0));
            }
            frame1ContentPane.add(panel2, new GridBagConstraints(0, 1, 3, 2, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(0, 10, 5, 10), 0, 0));

            //---- button10 ----
            button10.setText("OK");
            button10.addActionListener(e -> button10ActionPerformed(e));
            frame1ContentPane.add(button10, new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(0, 0, 5, 5), 0, 0));
            frame1.pack();
            frame1.setLocationRelativeTo(frame1.getOwner());
        }

        //======== frame2 ========
        {
            frame2.setTitle("SimpleKMeans");
            frame2.setMinimumSize(new Dimension(50, 50));
            Container frame2ContentPane = frame2.getContentPane();
            frame2ContentPane.setLayout(new GridBagLayout());
            ((GridBagLayout)frame2ContentPane.getLayout()).columnWidths = new int[] {0, 0, 0, 0};
            ((GridBagLayout)frame2ContentPane.getLayout()).rowHeights = new int[] {0, 0, 0, 0, 0, 0, 0};
            ((GridBagLayout)frame2ContentPane.getLayout()).columnWeights = new double[] {1.0, 1.0, 1.0, 1.0E-4};
            ((GridBagLayout)frame2ContentPane.getLayout()).rowWeights = new double[] {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0E-4};

            //======== panel7 ========
            {
                panel7.setLayout(new GridBagLayout());
                ((GridBagLayout)panel7.getLayout()).columnWidths = new int[] {0, 0, 0};
                ((GridBagLayout)panel7.getLayout()).rowHeights = new int[] {0, 0, 0, 0, 0, 0};
                ((GridBagLayout)panel7.getLayout()).columnWeights = new double[] {0.0, 1.0, 1.0E-4};
                ((GridBagLayout)panel7.getLayout()).rowWeights = new double[] {0.0, 0.0, 0.0, 0.0, 0.0, 1.0E-4};

                //---- label27 ----
                label27.setText("numClusters");
                panel7.add(label27, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
                    GridBagConstraints.EAST, GridBagConstraints.VERTICAL,
                    new Insets(0, 0, 5, 5), 0, 0));

                //---- spinner7 ----
                spinner7.setModel(new SpinnerNumberModel(2, null, null, 1));
                panel7.add(spinner7, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 5, 0), 0, 0));

                //---- label28 ----
                label28.setText("maxinterations");
                panel7.add(label28, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0,
                    GridBagConstraints.EAST, GridBagConstraints.VERTICAL,
                    new Insets(0, 0, 5, 5), 0, 0));

                //---- spinner8 ----
                spinner8.setModel(new SpinnerNumberModel(500, null, null, 1));
                panel7.add(spinner8, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 5, 0), 0, 0));

                //---- label30 ----
                label30.setText("seed");
                panel7.add(label30, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0,
                    GridBagConstraints.EAST, GridBagConstraints.VERTICAL,
                    new Insets(0, 0, 5, 5), 0, 0));

                //---- spinner12 ----
                spinner12.setModel(new SpinnerNumberModel(500, null, null, 1));
                panel7.add(spinner12, new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 5, 0), 0, 0));
            }
            frame2ContentPane.add(panel7, new GridBagConstraints(0, 1, 3, 3, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(0, 10, 5, 10), 0, 0));

            //---- button11 ----
            button11.setText("OK");
            button11.addActionListener(e -> button11ActionPerformed(e));
            frame2ContentPane.add(button11, new GridBagConstraints(1, 4, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(0, 0, 5, 6), 0, 0));
            frame2.pack();
            frame2.setLocationRelativeTo(frame2.getOwner());
        }

        //---- buttonGroup1 ----
        ButtonGroup buttonGroup1 = new ButtonGroup();
        buttonGroup1.add(radioButton1);
        buttonGroup1.add(radioButton2);
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }
    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private JTabbedPane tabbedPane1;
    private JPanel panel3;
    private JScrollPane scrollPane2;
    private JPanel panel10;
    private JPanel panel13;
    private JLabel label29;
    private JSpinner spinner9;
    private JSpinner spinner11;
    private JRadioButton radioButton5;
    private JButton button3;
    private JPanel panel4;
    private JLabel label1;
    private JLabel label2;
    private JLabel label5;
    private JLabel label6;
    private JLabel label3;
    private JLabel label4;
    private JLabel label7;
    private JLabel label8;
    private JPanel panel6;
    private JLabel label18;
    private JLabel label17;
    private JSpinner spinner1;
    private JLabel label21;
    private JCheckBox checkBox1;
    private JLabel label22;
    private JCheckBox checkBox2;
    private JLabel label23;
    private JCheckBox checkBox3;
    private JLabel label10;
    private JCheckBox checkBox6;
    private JLabel label11;
    private JCheckBox checkBox7;
    private JSeparator separator2;
    private JLabel label12;
    private JLabel label13;
    private JCheckBox checkBox8;
    private JScrollPane scrollPane3;
    private JList list1;
    private JTextField textField1;
    private JButton button4;
    private JSeparator separator1;
    private JLabel label19;
    private JLabel label20;
    private JSpinner spinner2;
    private JLabel label24;
    private JCheckBox checkBox4;
    private JLabel label25;
    private JCheckBox checkBox5;
    private JButton button9;
    private JPanel panel5;
    private JButton button1;
    private JButton button2;
    private JScrollPane scrollPane1;
    private JTable table1;
    private JScrollPane scrollPane4;
    private JTextArea textArea1;
    private JButton button8;
    private JPanel panel11;
    private JScrollPane scrollPane5;
    private JTree tree1;
    private JScrollPane scrollPane6;
    private JTextArea textArea2;
    private JButton button7;
    private JRadioButton radioButton4;
    private JButton button6;
    private JButton button13;
    private JPanel panel8;
    private JPanel panel1;
    private JComboBox<String> comboBox1;
    private JTextField textField2;
    private JButton button5;
    private JPanel panel12;
    private JPanel panel14;
    private JPanel panel15;
    private JRadioButton radioButton1;
    private JRadioButton radioButton2;
    private JLabel label32;
    private JSpinner spinner10;
    private JRadioButton radioButton3;
    private JButton button12;
    private JScrollPane scrollPane7;
    private JTextArea textArea3;
    private JPanel panel9;
    private JDialog dialog1;
    private JLabel label9;
    private JProgressBar progressBar1;
    private JFrame frame1;
    private JPanel panel2;
    private JLabel label14;
    private JSpinner spinner3;
    private JLabel label15;
    private JSpinner spinner4;
    private JLabel label16;
    private JSpinner spinner5;
    private JLabel label26;
    private JSpinner spinner6;
    private JLabel label31;
    private JSpinner spinner13;
    private JButton button10;
    private JFrame frame2;
    private JPanel panel7;
    private JLabel label27;
    private JSpinner spinner7;
    private JLabel label28;
    private JSpinner spinner8;
    private JLabel label30;
    private JSpinner spinner12;
    private JButton button11;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
    public static void main(String[] args){
        new Explorer().setVisible(true);
    }
}
