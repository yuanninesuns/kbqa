package NLP;

import com.hankcs.hanlp.corpus.io.IOUtil;
import com.hankcs.hanlp.mining.word2vec.Word2VecTrainer;
import com.hankcs.hanlp.mining.word2vec.WordVectorModel;

import java.io.*;
import java.util.ArrayList;

public class Question2Sparql_2 {

    private static String entity = "<http://dsc.nlp-bigdatalab.org:8086/%s>";
    private static String relation = "<http://dsc.nlp-bigdatalab.org:8086/%s>";
    private static final String label = "<http://www.w3.org/2000/01/rdf-schema#label>";
    private static final String type = "<http://www.w3.org/1999/02/22-rdf-syntax-ns#type>";


    public static String re_Q_A(String question){
//        System.out.println(question);
//        System.out.println("--------------------------------------------");
        String sparql_A = "SELECT DISTINCT ?x WHERE {\n %s %s ?o. \n"+
                " ?o "+label+" ?x .\n}";
        String[] strs = question.split("\\|");
        String subject = strs[0].split(" ")[0];
        String subject_type = get_type(strs[0].split(" ")[1]) ;
        String object_type = get_type(strs[2].split(" ")[1]);
        String entity1 = String.format(entity,subject);
        String relation1 = String.format(relation,subject_type+"相关"+object_type);
        return String.format(sparql_A,entity1,relation1);
    }


    public static String re_Q_C_n(ArrayList<String> questions){
//        for(String a:questions)
//            System.out.println(a);
//        System.out.println("--------------------------------------------");

        StringBuilder query = new StringBuilder();
        String triple_n_templ = " %s %s ?s . \n";
        String triple_last_templ = " ?s %s ?x .\n}";
        query.append("SELECT DISTINCT ?x WHERE {\n");
        for(String question: questions){
            String[] strs = question.split("\\|");
            String entity1 = String.format(entity,strs[0].split(" ")[0]);
            String relation1 = String.format(relation,get_type(strs[0].split(" ")[1])+"相关"+get_type(strs[2].split(" ")[1]));
            query.append(String.format(triple_n_templ, entity1, relation1));
        }
        query.append(String.format(triple_last_templ,label));
        return query.toString();
    }



    public static String re_Q_D_n(ArrayList<String> questions){
//        for(String a:questions)
//            System.out.println(a);
//        System.out.println("--------------------------------------------");

        StringBuilder query = new StringBuilder();
        String triple_0_templ = " %s %s ?s%d . \n" ;
        String triple_n_templ = " ?s%d %s ?s%d . \n";
        String triple_last_templ = " ?s%d %s ?x. \n}";
        query.append("SELECT DISTINCT ?x WHERE {\n ");

        // 添加第一个
        String[] strs1 = questions.get(0).split("\\|");
        String subject1 = String.format(entity,strs1[0].split(" ")[0]);
        String subject1_type = get_type(strs1[0].split(" ")[1]);
        String object_type1 = get_type(strs1[2].split(" ")[1]);
        String relation1 = String.format(relation,subject1_type+"相关"+object_type1);
        query.append(String.format(triple_0_templ, subject1, relation1, 0));

        // 添加中间n个
        for(int i=1;i<questions.size();i++){
            String[] strsn = questions.get(i).split("\\|");
            String subjectn_type = get_type(strsn[0].split(" ")[1]);

            String object_typen = get_type(strsn[2].split(" ")[1]);

            String relationn = String.format(relation,subjectn_type+"相关"+object_typen);
            query.append(String.format(triple_n_templ, i-1, relationn, i));
        }

        // 添加末尾label
        query.append(String.format(triple_last_templ, questions.size()-1, label));
        return query.toString();
    }

    public static String re_Q_E(String question){
//        System.out.println(question);
//        System.out.println("--------------------------------------------");
        String sparql_E = "SELECT DISTINCT ?s ?p ?x WHERE {\n ?s %s %s . \n"+
                " ?s %s %s . \n"+
                " ?s ?p ?x . \n}";
        String type_templ = "<http://dsc.nlp-bigdatalab.org:8086/%s>";
        String label_templ = "\"%s\"@ZH";
        String subject = String.format(label_templ,question.split(" ")[0]);
        String subject_type = String.format(type_templ,get_type(question.split(" ")[1]));
        return String.format(sparql_E,label, subject,type,subject_type);
    }

    public static String re_Q_F(ArrayList<String> questions){ //?s %s %s .

//        for(String a:questions)
//            System.out.println(a);
        System.out.println("--------------------------------------------");
        String sparql_F = "SELECT DISTINCT ?s ?p ?x WHERE {\n%s\n}";
        String type_templ = "<http://dsc.nlp-bigdatalab.org:8086/%s>";
        String label_templ = "\"%s\"@ZH";
        String triple_templ = " {\n  ?s %s %s . \n  ?s %s %s . \n  ?s ?p ?x .\n }";
        // {%s} UNION {%s}
        StringBuilder query = new StringBuilder();
        boolean flag = false;
        for(String question: questions){
            if(flag) query.append("\n UNION\n");
            String subject = String.format(label_templ,question.split(" ")[0]);
            String subject_type = String.format(type_templ,get_type(question.split(" ")[1]));
            query.append(String.format(triple_templ, label, subject, type, subject_type));
            flag = true;
        }
        return String.format(sparql_F, query);
    }

    public static String re_Q_B(String question){

//        System.out.println(question);
//        System.out.println("--------------------------------------------");
        String[] strs = question.split("\\|");
        String subject_name = strs[0].split(" ")[0];
        String object_type = get_type(strs[2].split(" ")[1]);
        //String subject_name, String object_type
        String subject1 ="<http://dsc.nlp-bigdatalab.org:8086/"+subject_name+">";
        String sparql ="SELECT DISTINCT ?x WHERE { ?a ?b "+subject1+" . ?a ?c ?d . ?d <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://dsc.nlp-bigdatalab.org:8086/"+object_type+"> . ?d <http://www.w3.org/2000/01/rdf-schema#label> ?x . }";

        return sparql;
    }

    public static String re_Q_S(String question ){
        String sparql_S = "SELECT DISTINCT ?x WHERE {\n %s %s ?o. \n"+
                " ?o "+label+" ?x .\n}";
        String[] strs = question.split("&");
        String entity1 = String.format(entity,strs[0]);
        String relation1 = String.format(relation,strs[1]+"相关"+strs[2]);
        return String.format(sparql_S,entity1,relation1);
    }


    public static String get_type(String type){
        String type_ch = null;
        if(type.equals("njb")||type.equals("njibing")){
            type_ch = "疾病";
        }else if(type.equals("nzz")||type.equals("nzhengzhuang")){
            type_ch = "症状";
        }else if(type.equals("njc")||type.equals("njiancha")){
            type_ch = "检查";
        }else if(type.equals("nzl")||type.equals("nzhiliao")){
            type_ch = "治疗";
        }else if(type.equals("nyp")||type.equals("nyaopin")){
            type_ch = "药品";
        }else if(type.equals("nks")||type.equals("nkeshi")){
            type_ch = "科室";
        }else{
            type_ch = null;
        }
        return type_ch;
    }

    private static int min(int... is) {
        int min = Integer.MAX_VALUE;
        for (int i : is) {
            if (min > i) {
                min = i;
            }
        }
        return min;
    }

    // 计算编辑距离
    private static float levenshtein(String str1,String str2) {
        //计算两个字符串的长度。
        int len1 = str1.length();
        int len2 = str2.length();
        //建立上面说的数组，比字符长度大一个空间
        int[][] dif = new int[len1 + 1][len2 + 1];
        //赋初值，步骤B。
        for (int a = 0; a <= len1; a++) {
            dif[a][0] = a;
        }
        for (int a = 0; a <= len2; a++) {
            dif[0][a] = a;
        }
        //计算两个字符是否一样，计算左上的值
        int temp;
        for (int i = 1; i <= len1; i++) {
            for (int j = 1; j <= len2; j++) {
                if (str1.charAt(i - 1) == str2.charAt(j - 1)) {
                    temp = 0;
                } else {
                    temp = 1;
                }
                //取三个值中最小的
                dif[i][j] = min(dif[i - 1][j - 1] + temp, dif[i][j - 1] + 1,
                        dif[i - 1][j] + 1);
            }
        }
        float similarity =1 - (float) dif[len1][len2] / Math.max(str1.length(), str2.length());
        return similarity;
    }

    static WordVectorModel trainOrLoadModel() throws IOException {
        String MODEL_FILE_NAME = "data/word2vec.txt";
        String TRAIN_FILE_NAME = "data/yuliao.txt";
        if (!IOUtil.isFileExisted(MODEL_FILE_NAME))
        {
            if (!IOUtil.isFileExisted(TRAIN_FILE_NAME))
            {
                System.err.println("语料不存在");
                System.exit(1);
            }
            Word2VecTrainer trainerBuilder = new Word2VecTrainer();
            return trainerBuilder.train(TRAIN_FILE_NAME, MODEL_FILE_NAME);
        }
        return loadModel(MODEL_FILE_NAME);
    }

    static WordVectorModel loadModel(String MODEL_FILE_NAME) throws IOException {
        return new WordVectorModel(MODEL_FILE_NAME);
    }

    // 对实体进行匹配


    public static ArrayList<String> getSim(String keywords){
        try{
            WordVectorModel wordVectorModel = trainOrLoadModel();
            ArrayList<String> result = new ArrayList<String>( );
            File file = new File("data/dictionary/custom/entity_dict.txt");
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line = null;
            float maxDistance_w = 0;
            float maxDistance_l = 0;
            String str_type_l = null;
            String str_entity_l = null;
            String str_type_w = null;
            String str_entity_w = null;
            while((line = br.readLine())!=null){
                String entity = line.split(" ")[0];
                String type = line.split(" ")[1];
                float distance_l = levenshtein(keywords, entity);
                if(distance_l>maxDistance_l){
                    maxDistance_l = distance_l;
                    str_type_l = type;
                    str_entity_l = entity;
                }

                float distance_w = wordVectorModel.similarity(keywords, entity);
                if(distance_w>maxDistance_w){
                    maxDistance_w = distance_w;
                    str_type_w = type;
                    str_entity_w = entity;
                }
            }

            if(maxDistance_w>50){
                result.add(str_entity_w);
                result.add(str_type_w);
                return result;
            }else if(maxDistance_l >0.4){
                result.add(str_entity_l);
                result.add(str_type_l);
                return result;
            }
        } catch (FileNotFoundException e){
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }

    public static ArrayList<String> getSim2(String keywords){
        //System.out.println(keywords);
        try{
            WordVectorModel wordVectorModel = trainOrLoadModel();
            ArrayList<String> result = new ArrayList<String>( );
            File file = new File("data/dictionary/custom/entity_dict2.txt");
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line = null;
            float maxDistance_w = 0;
            float maxDistance_l = 0;
            String str_type_l = null;
            String str_entity_l = null;
            String str_type_w = null;
            String str_entity_w = null;
            while((line = br.readLine())!=null){
                String entity = line.split(" ")[0];
                String type = line.split(" ")[1];
                float distance_l = levenshtein(keywords, entity);
                if(distance_l>maxDistance_l){

                    maxDistance_l = distance_l;
                    str_type_l = type;
                    str_entity_l = entity;

                   // System.out.println(str_entity_l+" "+maxDistance_l);
                }

                float distance_w = wordVectorModel.similarity(keywords, entity);
                if(distance_w>maxDistance_w){
                    maxDistance_w = distance_w;
                    str_type_w = type;
                    str_entity_w = entity;
                }
            }

            if(maxDistance_w>50){
                result.add(str_entity_w);
                result.add(str_type_w);
                return result;
            }else if(maxDistance_l >0.4){
                result.add(str_entity_l);
                result.add(str_type_l);
                return result;
            }
        } catch (FileNotFoundException e){
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }



    public static void main(String[] args){
        ArrayList<String> a=Question2Sparql_2.getSim("矮矮白癜风病");
        for(String temp:a)
            System.out.println(temp);
        //解析A类问题
//        String question = "新生儿臂丛神经麻痹 nzz|有 v|疾病 njibing";
//        System.out.println(Question2Sparql_2.re_Q_A(question));

        //解析E类问题
//        String question = "新生儿臂丛神经麻痹 nzz";
//        System.out.println(Question2Sparql_2.re_Q_E(question));

        //解析C类问题
//        ArrayList<String> questions = new ArrayList<>();
//        questions.add("新生儿臂丛神经麻痹 nzz|是 v|疾病 njibing");
//        questions.add("头疼 nzz|是 v|疾病 njibing");
//        questions.add("抽血 njc|是 v|疾病 njibing");
//        System.out.println(Question2Sparql_2.re_Q_C_n(questions));

        //解析D类问题
//        ArrayList<String> questions = new ArrayList<>();
//        questions.add("新生儿臂丛神经麻痹 nzz|是 v|疾病 njibing");
//        questions.add("疾病 njibing|去 v|科室 nkeshi");
//        questions.add("科室 nkeshi|有 v|疾病 njibing");
//        System.out.println(Question2Sparql_2.re_Q_D_n(questions));

        //解析F类问题
//        ArrayList<String> questions = new ArrayList<>();
//        questions.add("新生儿臂丛神经麻痹 nzz");
//        questions.add("发烧 njb");
//        questions.add("抽血 njc");
//        System.out.println(Question2Sparql_2.re_Q_F(questions));
    }
}
