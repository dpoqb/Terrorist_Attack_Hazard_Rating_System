package test;

import bean.EventAttribute;
import bean.ExcelInput;
import dao.EventMapper;
import main.model.bean.Index1;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import main.*;
import prefuse.data.util.Index;
import weka.core.Instances;
import weka.experiment.InstanceQuery;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Remove;

public class Test {

    @org.junit.Before
    public void setUp() throws Exception {
    }

    @org.junit.After
    public void tearDown() throws Exception {
    }

//    @org.junit.Test
//    public void mybatistest() throws IOException {
//        String resource = "config/Configure.xml";
//        InputStream inputStream = Resources.getResourceAsStream(resource);
//        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
//        //SqlSession cannot be shared, you must create new object when you use it every time
//        SqlSession session = sqlSessionFactory.openSession();
//        try {
//            // Mybatis can create a agent object(after the interface tied to XML),although this interface can't create new object
//            EventMapper mapper = session.getMapper(EventMapper.class);
//            Page<Object> page = PageHelper.startPage(1,50);
//            List<EventAttribute> eva = mapper.getAttributes();
//            PageInfo<EventAttribute> pinfo = new PageInfo<>(eva,5);
//            for (EventAttribute e:eva){
//                System.out.println(e);
//            }
//            System.out.println("当前页码："+pinfo.getPageNum());
//            System.out.println("总记录数："+pinfo.getTotal());
//            System.out.println("每页记录数："+pinfo.getPageSize());
//            System.out.println("总页码："+pinfo.getPages());
//        } finally {
//            session.close();
//        }
//    }

    public void excel() {
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
        EventMapper mapper1 = session.getMapper(EventMapper.class);
        ArrayList<EventAttribute> atr = mapper1.getAttributes();
        // throw the first attributes idx
        String[] column = new String[atr.size() - 1];
        for (int i = 0; i < column.length; i++) {
            column[i] = atr.get(i + 1).getAttribute_name();
        }
        XlsxPaese excel = new XlsxPaese();
        List<ExcelInput> hd = null;
        session.close();
        try {
            hd = excel.getBeanList(
                    "F:\\论文\\2019-2020毕业论文\\projectDM\\src\\config\\input\\template\\gtd_template.xls",
                    column, ExcelInput.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            inputStream = null;
            try {
                inputStream = Resources.getResourceAsStream(resource);
            } catch (IOException e) {
                e.printStackTrace();
            }
            sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
            //SqlSession cannot be shared, you must create new object when you use it every time
            session = sqlSessionFactory.openSession();
            EventMapper mapper2 = session.getMapper(EventMapper.class);
            for (int i = 1; i < hd.size(); i++) {
                System.out.println(hd.get(i));
                mapper2.insertSelective(hd);
            }
            session.commit();   //commit insert
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    @org.junit.Test
    public void weka() {
        InstanceQuery query = null;
        Instances data = null;

        // 使用InstanceQuery类
        try {
            query = new InstanceQuery();
        } catch (Exception e) {
            e.printStackTrace();
        }
        query.setDatabaseURL("jdbc:mysql://localhost:3306/gtd_data");
        query.setUsername("root");
        query.setPassword("abcd=!@#");
        query.setQuery("select * from tevent_attributes");
        try {
            data = query.retrieveInstances();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(data);
    }

    @org.junit.Test
    public void test1() {
//        File fileName = new File(
//                "F:\\论文\\2019-2020毕业论文\\projectDM\\src\\main\\model\\index\\index1.csv");
//        //csv文件 默认第一行是引用字段
//        String [] FILE_HEADER = {"一级指标B","编号B","二级指标C","说明","编号C","权值","综合得分"};
//        CSVParser csvFileParser = null;
//        Reader reader = null;
//        //创建CSVFormat（header mapping）
//        CSVFormat csvFileFormat = CSVFormat.DEFAULT.withHeader(FILE_HEADER);
//        try
//        {
//            //初始化FileReader object
//             reader = new InputStreamReader(new FileInputStream(fileName),"gbk");
//            //初始化 CSVParser object
//            csvFileParser = new CSVParser(reader, csvFileFormat);
//            //CSV文件records
//            List<CSVRecord> csvRecords = csvFileParser.getRecords();
//            // CSV
//            List<Index1> userList = new ArrayList<Index1>();
//            for (int i = 1; i < csvRecords.size(); i++) {
//                CSVRecord record = csvRecords.get(i);
//                //创建用户对象填入数据
//                Index1 user = new Index1(record.get("一级指标B"), record.get("编号B"),
//                record.get("二级指标C"), record.get("说明"),record.get("编号C"),
//                        Double.valueOf(record.get("权值")),Double.valueOf(record.get("综合得分")));
//                userList.add(user);
//            }
//            // 遍历打印
//            for (Index1 user : userList) {
//                System.out.println(user.toString());
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                reader.close();
//                csvFileParser.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }
    }
    @org.junit.Test
    public void test2() {
        System.out.println((int)12.0);
    }

}