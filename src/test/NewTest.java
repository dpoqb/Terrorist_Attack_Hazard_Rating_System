package test;

import weka.core.Instances;
import weka.core.converters.DatabaseLoader;
import weka.experiment.InstanceQuery;

public class NewTest {

    @org.junit.Test
    public void weka() {
        // 使用InstanceQuery类
        try {
            System.out.println("\n\n使用DatabaseLoader类从数据库中批量检索数据");
            DatabaseLoader loader = null;
            loader = new DatabaseLoader();
            loader.setSource("jdbc:mysql://127.0.0.1:3306/gtd_data", "root", "abcd=!@#");
            loader.setQuery("select * from tevent_all_info_copy");
            Instances data1 = loader.getDataSet();
            // 使用最后一个属性作为类别属性
            System.out.println(data1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
