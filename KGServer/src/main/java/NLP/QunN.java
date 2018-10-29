package NLP;

import TDB.HandleTDB;
import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.corpus.dependency.CoNll.CoNLLSentence;
import com.hankcs.hanlp.dictionary.CustomDictionary;
import com.hankcs.hanlp.seg.Segment;
import org.apache.jena.base.Sys;
import org.apache.jena.query.*;
import org.apache.jena.tdb.TDBFactory;
import org.apache.jena.tdb.base.file.Location;

import java.util.ArrayList;
import java.util.List;

public class QunN {

    public static ArrayList<String> getResults(String lines){
        String type="";
        String ans="";
        int num=0;
        ArrayList <String> ans2=new ArrayList<>();
        //CustomDictionary.insert("新生儿臂丛神经麻痹", "n 1000");

        Segment segment =HanLP.newSegment().enableCustomDictionary(true);
        List sentence=segment.seg(lines);
        for(Object i:sentence)
            System.out.println(i.toString());

        ans2=Show.Showup5(sentence);

        return ans2;
    }

    public static ArrayList<String> getResults_S(String lines){
        return Show.Showup_S(lines);
    }

    public static void main(String args[]){
         QunN qunN=new QunN();
        //List<String> a=new ArrayList<>();
        //CustomDictionary.reload();
        //qunN.getResults("新生儿臂丛神经麻痹,持续性头晕的症状是什么");
        //qunN.getResults("持续性头晕的疾病吃什么药");//症状相关症状
        //qunN.getResults("持续性头晕小儿维生素A");
//        qunN.getResults("持续性头晕");
//        qunN.getResults("持续性头晕要吃什么药");
        //qunN.getResults("持续性头晕是什么病");//症状相关疾病
        //qunN.getResults("小儿维生素A缺乏病有什么症状");//疾病相关症状
        //qunN.getResults("阳痿，感冒");//症状相关疾病
//        Segment segment =HanLP.newSegment().enableCustomDictionary(true);
        CoNLLSentence sentence = HanLP.parseDependency("小儿维生素A缺乏病有什么症状");
//        System.out.printf(sentence.toString());

    }
}
