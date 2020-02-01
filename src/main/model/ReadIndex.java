package main.model;

import main.model.bean.Index1;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ReadIndex {
    private String indexSource = "F:\\论文\\2019-2020毕业论文\\projectDM\\src\\main\\model\\index\\index1.csv";
    private List<Index1> userList;
    private String strindex;

    public List<Index1> getUserList() {
        return userList;
    }

    public String getStrindex() {
        return strindex;
    }

    public  ReadIndex(){
        File fileName = new File(indexSource);
        //csv文件 默认第一行是引用字段
        String [] FILE_HEADER = {"一级指标B","编号B","二级指标C","属性编码","说明","编号C","权值","综合得分"};
        strindex = "一级指标B,编号B,二级指标C,属性编码,说明,编号C,权值,综合得分\n";
        CSVParser csvFileParser = null;
        Reader reader = null;
        //创建CSVFormat（header mapping）
        CSVFormat csvFileFormat = CSVFormat.DEFAULT.withHeader(FILE_HEADER);
        try
        {
            //初始化FileReader object
            reader = new InputStreamReader(new FileInputStream(fileName),"gbk");
            //初始化 CSVParser object
            csvFileParser = new CSVParser(reader, csvFileFormat);
            //CSV文件records
            List<CSVRecord> csvRecords = csvFileParser.getRecords();
            // CSV
            userList = new ArrayList<Index1>();
            for (int i = 1; i < csvRecords.size(); i++) {
                CSVRecord record = csvRecords.get(i);
                //创建用户对象填入数据
                String filed1 = record.get("一级指标B");
                String filed2 = record.get("编号B");
                String filed3 = record.get("二级指标C");
                String filed4 = record.get("属性编码");
                String filed5 = record.get("说明");
                String filed6 = record.get("编号C");
                Double filed7 = Double.valueOf(record.get("权值"));
                Double filed8 = Double.valueOf(record.get("综合得分"));
                Index1 user = new Index1(filed1,filed2,filed3,filed4,filed5,filed6,filed7,filed8);
                userList.add(user);
                strindex = strindex.concat(filed1+","+filed2+","+filed3+","+
                        filed4+","+filed5+","+filed6+","+filed7+","+filed8+"\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                reader.close();
                csvFileParser.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
