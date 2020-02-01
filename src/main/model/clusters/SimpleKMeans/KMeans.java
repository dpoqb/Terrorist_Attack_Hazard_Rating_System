package main.model.clusters.SimpleKMeans;
import weka.clusterers.ClusterEvaluation;
import weka.clusterers.SimpleKMeans;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.Utils;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Remove;

public class KMeans {
    private String[] options;

    private SimpleKMeans clusterer_train;
    private SimpleKMeans clusterer;

    private String printEvalution = "";

    private String printTestResult = "";    //打印预测结果
    private String printTrainEvalution = "";
    private String printTestEvalution = "";

    public SimpleKMeans getClusterer() {
        return clusterer;
    }

    public String getPrintTestResult() {
        return printTestResult;
    }

    public SimpleKMeans getClusterer_train() {
        return clusterer_train;
    }

    public String getPrintEvalution() {
        return printEvalution;
    }

    public String getPrintTrainEvalution() {
        return printTrainEvalution;
    }

    public String getPrintTestEvalution() {
        return printTestEvalution;
    }

    public KMeans(String[] options){
        this.options = options;
    }

    public void startKMeans(Instances data) throws Exception {
        //create temp instances
        Instances tempinst = new Instances(data);
        Remove filter = new Remove();
        filter.setAttributeIndices("1");
        filter.setInputFormat(tempinst);
        tempinst = Filter.useFilter(tempinst,filter);

        clusterer = new SimpleKMeans();
        clusterer.setOptions(options);
        clusterer.buildClusterer(tempinst);
        //输出评估
        printEvalution = printEvalution.concat(clusteringEvaluation(clusterer,tempinst));
    }

    public void startKMeans(Instances train, Instances test) throws Exception {
        //判断训练集和测试集是否兼容
        if(train.equalHeaders(test)){
            Remove filter = new Remove();
            filter.setAttributeIndices("1");
            //训练模型
            Instances temptrain = new Instances(train);
            filter.setInputFormat(temptrain);
            temptrain = Filter.useFilter(temptrain,filter);

            clusterer_train = new SimpleKMeans();
            clusterer_train.setOptions(options);
            clusterer_train.buildClusterer(temptrain);
            //输出训练集评估
            printTrainEvalution = printTestEvalution.concat(clusteringEvaluation(clusterer_train,temptrain));

            //测试
            Instances temptest = new Instances(test);
            filter.setInputFormat(temptest);
            temptest = Filter.useFilter(temptest,filter);
            //输出测试集评估
            printTestEvalution = printTestEvalution.concat(clusteringEvaluation(clusterer_train,temptest));
            //输出预测
            getPredication(clusterer_train,temptest,test);
        }
        else throw new Exception("Train set and test set are incompatible:"+train.equalHeadersMsg(test));
    }

    public void getPredication(SimpleKMeans clusterer, Instances test, Instances primtest) throws Exception {
        /*遍历整个测试集，对每一个实例预测其所在的簇并计算其分布，调用聚类器对象的clusterInstance方法
         * 预测作为输入参数的实例属于哪个簇，调用distributionForInstance方法预测给定实例属于某个簇的隶属度
         * */
        printTestResult = printTestResult.concat("IDx \t-\t No. \t-\t Level  \t-\t distribution"+"\n");
        for (int i = 0; i < test.numInstances(); i++) {
            int cluster = clusterer.clusterInstance(test.instance(i));
            double[] dist = clusterer.distributionForInstance(test.instance(i));
            printTestResult = printTestResult.concat((int)primtest.get(i).value(primtest.attribute(0))+"");
            printTestResult = printTestResult.concat(" \t-\t ");
            printTestResult = printTestResult.concat((i + 1)+"");
            printTestResult = printTestResult.concat(" \t-\t ");
            printTestResult = printTestResult.concat(cluster+"");
            printTestResult = printTestResult.concat(" \t-\t ");
            printTestResult = printTestResult.concat(Utils.arrayToString(dist));
            printTestResult = printTestResult.concat("\n");
        }
    }

    public String clusteringEvaluation(SimpleKMeans cluster, Instances data) throws Exception {
        ClusterEvaluation clusterEval = new ClusterEvaluation();
        clusterEval.setClusterer(cluster);
        clusterEval.evaluateClusterer(data);
        return  (clusterEval.clusterResultsToString()+"\n");
    }
}
