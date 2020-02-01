package main.model;

import main.model.bean.Index1;
import prefuse.data.util.Index;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;
import weka.filters.unsupervised.attribute.Add;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class WeightHandler {
 private HashMap<String,HashMap<String,Double>> index = new HashMap<String,HashMap<String,Double>>();

    public HashMap<String, HashMap<String, Double>> getIndex() {
        return index;
    }

    public WeightHandler(List<Index1> index){
        for (Index1 i:index){
            if(this.index.containsKey(i.getAttr())){
                this.index.get(i.getAttr()).put(i.getTow(),i.getCompreweight());
            }
            else {
                HashMap<String,Double> map = new HashMap<>();
                this.index.put(i.getAttr(),map);
                map.put(i.getTow(),i.getCompreweight());
            }
        }
    }

    /**event scores*/
    public Instances Socores(Instances data){
        //在内存中创建新的数据集
        Attribute eventid = new Attribute("IDX");//create string attribute
        Attribute ascore = new Attribute("score"); //create numeric attribute
        ArrayList<Attribute> attributes = new ArrayList<Attribute>();
        attributes.add(eventid);
        attributes.add(ascore);
        //param1:relation name,param2:attributes,param3:preserve memory
        Instances score = new Instances("EventScores",attributes,data.numInstances());
        //compute scores
        for(int i=0;i<data.numInstances();i++){
            double instscore = 0;
            Instance event = data.get(i);
            for(int j=0;j<data.numAttributes();j++){
                String attrname = event.attribute(j).name();
                if(event.attribute(j).isNumeric()&&index.containsKey(attrname))
                    instscore = instscore + event.value(j)*index.get(attrname).get(attrname);
                else if(event.attribute(j).isNominal()&&this.index.containsKey(attrname)
                &&data.attribute(j).numValues()>3){
                    if (this.index.get(attrname).containsKey((int)event.value(j)+""))
                        instscore = instscore + index.get(attrname).get((int)event.value(j)+"");
                }
                else if(this.index.containsKey(attrname)&&(int)event.value(j)==1){
                    //attribute include value 1,0,-9,
                    instscore = instscore + index.get(attrname).get(1+"");
                }
            }
            double[] values = new double[score.numAttributes()];
            values[0] = event.value(data.attribute("IDX").index());
            values[1]=instscore;    //numeric型属性填充值
            Instance inst = new DenseInstance(1.0,values);
            score.add(inst);
        }
        return score;
    }

}
