package NLP;

import TDB.HandleTDB;
import com.github.jsonldjava.utils.Obj;
import com.hankcs.hanlp.corpus.dependency.CoNll.CoNLLSentence;
import com.hankcs.hanlp.corpus.dependency.CoNll.CoNLLWord;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class Show {

    public static ArrayList<String> Showup_S(String lines){
        ArrayList<String> result = new ArrayList<>();
        String subject = lines.split("&")[0];
        String subject_type = lines.split("&")[1];
        String object_type = lines.split("&")[2];
        ArrayList<String> subject_sim = Question2Sparql_2.getSim(subject);
        System.out.println(subject_sim.get(0)+" "+subject_sim.get(1));
        if(subject_sim!=null&&subject_sim.size()!=0&&Question2Sparql_2.get_type(subject_sim.get(1)).equals(subject_type)){
            System.out.println(subject_sim.get(0)+" "+Question2Sparql_2.get_type(subject_sim.get(1)));
            String lines_new = subject_sim.get(0)+"&"+Question2Sparql_2.get_type(subject_sim.get(1))+"&"+object_type;
            result = HandleTDB.ReadTDB(Question2Sparql_2.re_Q_S(lines_new));
        }
        return result;
    }

    public static ArrayList<String> Showup5(List sentence){

        ArrayList<String> ansend=new ArrayList<>();

        String weiyu="的 de";
        int weiyuid=0;


        ArrayList<String> shiti = new ArrayList();
        ArrayList<String> leixing = new ArrayList();
        String temp="";
//        for (Object word : sentence) {
//            System.out.println(word.toString());
//        }



    for (Object word : sentence) {
        ArrayList<String> tmp=new ArrayList<String>();
        String word0=word.toString().split("/")[0];
        String type0=word.toString().split("/")[1];
        if ((type0.equals("nzz") || type0.equals("njb")||type0.equals("nks") || type0.equals("nzl")||type0.equals("nyp") || type0.equals("njc")||type0.equals("n")||type0.equals("vn"))) {
            if(type0.equals("n") ||type0.equals("vn")) {
                tmp = Question2Sparql_2.getSim(word0);
                if(tmp!=null && tmp.size()!=0){
                    temp=tmp.get(0)+" "+tmp.get(1);
                    //System.out.println(temp);
                    shiti.add(temp);
                }
            }
            else{
                temp = word0+" "+type0;
                shiti.add(temp);
            }
        }

        if ((type0.equals("nzhengzhuang") || type0.equals("njibing")||type0.equals("nkeshi") || type0.equals("nzhiliao")||type0.equals("nyaopin") || type0.equals("njiancha")||type0.equals("n")||type0.equals("vn")) ) {
            if(type0.equals("n") ||type0.equals("vn")) {
                tmp = Question2Sparql_2.getSim2(word0);
                if(tmp!=null && tmp.size()!=0){
                    temp=tmp.get(0)+" "+tmp.get(1);
                    //System.out.println(temp);
                    leixing.add(temp);
                }

            }
            else{
                temp = word0+" "+type0;
                leixing.add(temp);
            }

        }
    }

        String ans="";
        String type="";
        String sparql="";
        if(shiti.size()==1 && leixing.size()==1){
            //System.out.println(shiti.get(0).split(" ")[0]);
            String relation = "<http://dsc.nlp-bigdatalab.org:8086/%s>";
            String subject_type = Question2Sparql_2.get_type(shiti.get(0).split(" ")[1]) ;
            String object_type = Question2Sparql_2.get_type(leixing.get(0).split(" ")[1]) ;
            String relation2 = subject_type+"相关"+object_type;
             //System.out.println(relation2);
            if(relation2.equals("检查相关疾病")||relation2.equals("治疗相关疾病")||relation2.equals("疾病相关检查")||relation2.equals("疾病相关治疗")||relation2.equals("疾病相关疾病")||relation2.equals("疾病相关症状")||relation2.equals("疾病相关科室")||relation2.equals("疾病相关药品")||relation2.equals("症状相关疾病")||relation2.equals("科室相关疾病")||relation2.equals("药品相关疾病")){
                //System.out.println("A类问题");
                ans=shiti.get(0)+"|"+weiyu+"|"+leixing.get(0);
                sparql=Question2Sparql_2.re_Q_A(ans);
                ansend= HandleTDB.ReadTDB(sparql);
            }
            else{
                //System.out.println("B类问题");
                ans=shiti.get(0)+"|"+weiyu+"|"+leixing.get(0);
                //System.out.println(ans);
                sparql=Question2Sparql_2.re_Q_B(ans);
                ansend=HandleTDB.ReadTDB(sparql);
            }
            //这里先判断有没有关联，如果有就调用你的方法，如果没有就调用我的方法
            //ans+=type+"&"+store[0]+"|"+weiyu+"|"+store[1]+"&";
            //A&新生儿臂丛神经麻痹 nzz|有 v|症状 nzhengzhuang&;
            //B&新生儿臂丛神经麻痹 nzz|有 v|症状 nzhengzhuang&;
        }
        else if(shiti.size()==1 && leixing.size()==0){
            //System.out.println("不全句E");
            ans+=shiti.get(0);
            sparql=Question2Sparql_2.re_Q_E(ans);
            //E&新生儿臂丛神经麻痹 nzz&;
            ansend=HandleTDB.ReadTDBE(sparql);
        }
        else if(shiti.size()>1 && leixing.size()==0){
            //System.out.println("不全句F");
            ArrayList<String> tmp=new ArrayList<String>();
            for(String a:shiti)
                tmp.add(a);
            sparql=Question2Sparql_2.re_Q_F(tmp);
            //F&新生儿臂丛神经麻痹 nzz&发烧 njb&;
            ansend=HandleTDB.ReadTDBF(sparql);
        }
        else if(shiti.size()>1 && leixing.size()==1){
            //System.out.println("C类问题");
            ArrayList<String> tmp=new ArrayList<String>();
            for(String a:shiti)
                tmp.add(a+"|"+weiyu+"|"+leixing.get(0));
            sparql=Question2Sparql_2.re_Q_C_n(tmp);
            //C&新生儿臂丛神经麻痹 nzz|是 v|疾病 nzhengzhuang&抽血 njc|是 v|疾病 njibing&;
            ansend=HandleTDB.ReadTDB(sparql);
        }
        else if(shiti.size()==1 && leixing.size()>1){
            //D&新生儿臂丛神经麻痹 nzz|是 v|疾病 njibing&疾病 njibing|去 v|科室 nkeshi&;
            //System.out.println("D类问题");
            ArrayList<String> tmp=new ArrayList<String>();
            ans=shiti.get(0)+"|"+weiyu+"|"+leixing.get(0);
            tmp.add(ans);
            for(int i=0;i<leixing.size()-1;i++){
                ans=leixing.get(i)+"|的 de|"+leixing.get(i+1);
                tmp.add(ans);
            }
            sparql=Question2Sparql_2.re_Q_D_n(tmp);
            ansend=HandleTDB.ReadTDB(sparql);
        }
        else{
            System.out.println("无法解析该问题");
            System.out.println("--------------------------------------------");
            sparql="";
        }


        //System.out.println(weiyu);




        //问题类型&技能 n|是 tvshi|什么 ry$孙尚香 n|的 tvshi|什么 ry$
        return ansend;
    }
}

/*        JsonObject json=new JsonParser().parse(answer).getAsJsonObject();
        JsonArray jsons=(JsonArray)((JsonObject) json.get("results")).get("bindings");
        for(int i=0;i<jsons.size();i++){
            end.add(((JsonObject) ((JsonObject) jsons.get(i)).get("e")).get("value").getAsString());
        }*/
