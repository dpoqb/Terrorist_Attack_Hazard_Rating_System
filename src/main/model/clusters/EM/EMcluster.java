package main.model.clusters.EM;

import weka.clusterers.ClusterEvaluation;
import weka.clusterers.EM;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.Utils;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.ClusterMembership;
import weka.filters.unsupervised.attribute.Remove;

public class EMcluster {
    private String[] options;

    private EM clusterer = new EM();
    private String printTestResult;
    private String printEvalution;

    public EM getClusterer() {
        return clusterer;
    }

    public EMcluster(String[] options) throws Exception {
        this.options = options;
    }

    public String getPrintTestResult() {
        return printTestResult;
    }

    public String getPrintEvalution() {
        return printEvalution;
    }

    public void startEMcluster(Instances data) throws Exception {
        //create temp instances
        Instances tempinst = new Instances(data);
        Remove filter = new Remove();
        filter.setAttributeIndices("1");
        filter.setInputFormat(tempinst);
        tempinst = Filter.useFilter(tempinst,filter);

        clusterer = new EM();
        clusterer.setOptions(options);
        clusterer.buildClusterer(tempinst);
        //输出评估
        clusteringEvaluation(clusterer,tempinst);
    }

    public void startEMcluster(Instances train, Instances test) throws Exception {
        //判断训练集和测试集是否兼容
        if(train.equalHeaders(test)){
            Remove filter = new Remove();
            filter.setAttributeIndices("1");

            Instances temptrain = new Instances(train);
            filter.setInputFormat(temptrain);
            temptrain = Filter.useFilter(temptrain,filter);

            clusterer = new EM();
            clusterer.setOptions(options);
            clusterer.buildClusterer(temptrain);
            //输出评估
            clusteringEvaluation(clusterer,temptrain);

            Instances temptest = new Instances(test);
            filter.setInputFormat(temptest);
            temptest = Filter.useFilter(temptest,filter);
            //输出预测
            getPredication(clusterer,temptest,test);
        }
        else throw new Exception("Train set and test set are incompatible:"+train.equalHeadersMsg(test));
    }

    public void getPredication(EM clusterer, Instances test, Instances primtest) throws Exception {
        //Sets the ranges of attributes to be ignored. If provided string is null, no attributes will be ignored.
        printTestResult = "";
        /*遍历整个测试集，对每一个实例预测其所在的簇并计算其分布，调用聚类器对象的clusterInstance方法
         * 预测作为输入参数的实例属于哪个簇，调用distributionForInstance方法预测给定实例属于某个簇的隶属度
         * */
        printTestResult = printTestResult.concat("IDx \t-\t No. \t-\t Level  \t-\t distribution"+"\n");
        for (int i = 0; i < test.numInstances(); i++) {
            int cluster = clusterer.clusterInstance(test.instance(i));
            double[] dist = clusterer.distributionForInstance(test.instance(i));
            printTestResult = printTestResult.concat((int)primtest.get(i).value(primtest.attribute(0))+"");
            printTestResult = printTestResult.concat(""+"\t-\t");
            printTestResult = printTestResult.concat((i + 1)+"");
            printTestResult = printTestResult.concat(" \t-\t ");
            printTestResult = printTestResult.concat(cluster+"");
            printTestResult = printTestResult.concat(" \t-\t ");
            printTestResult = printTestResult.concat(Utils.arrayToString(dist));
            printTestResult = printTestResult.concat("\n");
        }
    }

    public void clusteringEvaluation(EM cluster, Instances data) throws Exception {
        ClusterEvaluation clusterEval = new ClusterEvaluation();
        clusterEval.setClusterer(cluster);
        clusterEval.evaluateClusterer(data);
        printTestResult = new String("");
        printEvalution = printTestResult.concat(clusterEval.clusterResultsToString()+"\n");
    }

}
