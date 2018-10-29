package TDB;

import NLP.Question2Sparql_2;
import com.hankcs.hanlp.dictionary.CustomDictionary;
import org.apache.jena.atlas.lib.StrUtils;
import org.apache.jena.query.*;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.tdb.TDBFactory;
import org.apache.jena.tdb.base.file.Location;
import org.apache.jena.update.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class InsertTDB {
    private static final String TDBPath="data/KGQATDB";
    private static final String EntityPath="data/dictionary/custom/entity_dict.txt";

    private static String getLabel(String subject_type){
        String nature2="";
        if(subject_type.equals("疾病"))
            nature2="njb";
        else if(subject_type.equals("治疗"))
            nature2="nzl";
        else if(subject_type.equals("检查"))
            nature2="njc";
        else if(subject_type.equals("科室"))
            nature2="nks";
        else if(subject_type.equals("药品"))
            nature2="nyp";
        else if(subject_type.equals("症状"))
            nature2="nzz";
        else
            nature2="";
        return nature2;
    }
    private static void insert(String subject_name,String subject_type) throws IOException {
        String nature=getLabel(subject_type);
        CustomDictionary.insert(subject_name, nature+" 1000");
        File f = new File(EntityPath);
        if (f.exists()) {
            System.out.print("文件存在");
        } else {
            System.out.print("文件不存在");
            try {
                f.createNewFile();// 不存在则创建
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        BufferedWriter output = new BufferedWriter(new FileWriter(f,true));//true,则追加写入text文本
        output.write(subject_name+" "+nature);
        output.write("\r\n");//换行
        output.flush();
    }

    public  static boolean query_entity(String subject_name) {
        System.out.println("1bbbb");
        String sparql="SELECT ?x WHERE{<http://dsc.nlp-bigdatalab.org:8086/%s> <http://www.w3.org/2000/01/rdf-schema#label> ?x}";
        System.out.println("2bbbb");
        Location location = Location.create(TDBPath);
        System.out.println("3bbbb");
        Dataset dataset = TDBFactory.createDataset(location);
        System.out.println("4bbbb");
        // dataset.begin(ReadWrite.WRITE);
        System.out.println("5bbbb");
        try {
            System.out.println("2bbbb");
            Model model = dataset.getDefaultModel();
            String string = String.format(sparql,subject_name);
            Query query = QueryFactory.create(string);
            QueryExecution queryExecution = QueryExecutionFactory.create(query, model);
            ResultSet results = queryExecution.execSelect();
            if(results.hasNext())
                return true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }
    public  static boolean query_relation(String subject_name,String object_name) {
        System.out.println("1CCCC");
        String sparql="SELECT ?x WHERE{<http://dsc.nlp-bigdatalab.org:8086/%s> ?x <http://dsc.nlp-bigdatalab.org:8086/%s>}";
        Location location = Location.create(TDBPath);
        Dataset dataset = TDBFactory.createDataset(location);
        //dataset.begin(ReadWrite.WRITE);
        try {
            Model model = dataset.getDefaultModel();
            String string = String.format(sparql,subject_name,object_name);
            Query query = QueryFactory.create(string);
            QueryExecution queryExecution = QueryExecutionFactory.create(query, model);
            ResultSet results = queryExecution.execSelect();
            System.out.println("2CCCC");
            if(results.hasNext()){
                //System.out.println(re);
                return true;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        System.out.println("3CCCC");
        return false;
    }
    public static boolean insertentity(String subject_name,String subject_type){
        String sparql_label= "INSERT DATA {<http://dsc.nlp-bigdatalab.org:8086/%s> <http://www.w3.org/2000/01/rdf-schema#label> \"%s\" .}";
        String sparql_type="INSERT DATA {<http://dsc.nlp-bigdatalab.org:8086/%s> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://dsc.nlp-bigdatalab.org:8086/%s> .}";
        Location location =Location.create(TDBPath);
        Dataset dataset=TDBFactory.createDataset(location);
        try{
            GraphStore graphStore = GraphStoreFactory.create(dataset) ;
            if(!query_entity(subject_name)){
                System.out.println("没有subject");
                insert(subject_name,subject_type);

                String sparqlUpdateString = StrUtils.strjoinNL(
                        String.format(sparql_label,subject_name,subject_name)
                ) ;
                UpdateRequest request = UpdateFactory.create(sparqlUpdateString) ;
                UpdateProcessor proc = UpdateExecutionFactory.create(request, graphStore) ;
                proc.execute() ;

                String sparqlUpdateString2 = StrUtils.strjoinNL(
                        String.format(sparql_type,subject_name,subject_type)
                ) ;
                UpdateRequest request2 = UpdateFactory.create(sparqlUpdateString2) ;
                UpdateProcessor proc2 = UpdateExecutionFactory.create(request2, graphStore) ;
                proc2.execute() ;
            }
            return  true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            //dataset.commit() ;
            dataset.end() ;
        }
    }
    public static boolean insertrelation(String subject_name,String subject_type,String object_name,String object_type){
        String sparql_relation="INSERT DATA {<http://dsc.nlp-bigdatalab.org:8086/%s> <http://dsc.nlp-bigdatalab.org:8086/%s> <http://dsc.nlp-bigdatalab.org:8086/%s> .}";
        String sparql_label= "INSERT DATA {<http://dsc.nlp-bigdatalab.org:8086/%s> <http://www.w3.org/2000/01/rdf-schema#label> \"%s\" .}";
        String sparql_type="INSERT DATA {<http://dsc.nlp-bigdatalab.org:8086/%s> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://dsc.nlp-bigdatalab.org:8086/%s> .}";
        String relation_temp="%s相关%s";
        Location location =Location.create(TDBPath);
        Dataset dataset=TDBFactory.createDataset(location);
        //dataset.begin(ReadWrite.WRITE) ;

        System.out.println("2aaaa");

        try{
            GraphStore graphStore = GraphStoreFactory.create(dataset) ;

            System.out.println("3aaaa");
            if(!query_entity(subject_name)){
                System.out.println("没有subject");
                insert(subject_name,subject_type);

                String sparqlUpdateString = StrUtils.strjoinNL(
                        String.format(sparql_label,subject_name,subject_name)
                ) ;
                UpdateRequest request = UpdateFactory.create(sparqlUpdateString) ;
                UpdateProcessor proc = UpdateExecutionFactory.create(request, graphStore) ;
                proc.execute() ;

                String sparqlUpdateString2 = StrUtils.strjoinNL(
                        String.format(sparql_type,subject_name,subject_type)
                ) ;
                UpdateRequest request2 = UpdateFactory.create(sparqlUpdateString2) ;
                UpdateProcessor proc2 = UpdateExecutionFactory.create(request2, graphStore) ;
                proc2.execute() ;
            }
            if(!query_entity(object_name)){
                System.out.println("没有object");
                insert(object_name,object_type);
                String sparqlUpdateString = StrUtils.strjoinNL(
                        String.format(sparql_label,object_name,object_name)
                ) ;
                //System.out.println(sparqlUpdateString);
                //System.out.println(sparqlUpdateString);
                //.out.println(sparql_type);
                UpdateRequest request = UpdateFactory.create(sparqlUpdateString) ;
                UpdateProcessor proc = UpdateExecutionFactory.create(request, graphStore) ;
                proc.execute() ;
                String sparqlUpdateString2 = StrUtils.strjoinNL(
                        String.format(sparql_type,object_name,object_type)
                ) ;

                //.out.println(sparql_type);
                UpdateRequest request2 = UpdateFactory.create(sparqlUpdateString2) ;
                UpdateProcessor proc2 = UpdateExecutionFactory.create(request2, graphStore) ;
                proc2.execute() ;
            }
            if(!query_relation(subject_name,object_name)){
                System.out.println("没有relation");
                String relation=String.format(relation_temp,subject_type,object_type);
                String sparqlUpdateString = StrUtils.strjoinNL(
                        String.format(sparql_relation,subject_name,relation,object_name)
                ) ;
                UpdateRequest request = UpdateFactory.create(sparqlUpdateString) ;
                UpdateProcessor proc = UpdateExecutionFactory.create(request, graphStore) ;
                proc.execute() ;
//                dataset.commit() ;
//                dataset.end() ;
            }
            System.out.println("5aaaa");
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            //dataset.commit() ;
            dataset.end() ;
        }
    }

    public static void read(){
        Location location =Location.create(TDBPath);
        Dataset dataset=TDBFactory.createDataset(location);
        dataset.begin(ReadWrite.WRITE) ;

        Model model=dataset.getDefaultModel();
        String string= "SELECT ?x WHERE{<http://dsc.nlp-bigdatalab.org:8086/氧化锌软11cc膏> <http://dsc.nlp-bigdatalab.org:8086/药品相关疾病> ?x}";
        //String string= "SELECT * WHERE{ ?p ?n ?x}limit 10";
        Query query = QueryFactory.create(string);

        QueryExecution queryExecution = QueryExecutionFactory.create(query,model);
        ResultSet results = queryExecution.execSelect();
        while(results.hasNext())
        {
            QuerySolution querySolution=results.nextSolution();
            System.out.println(querySolution.get("?x"));
        }
        model.close();
        dataset.close();

    }

    public static void main(String[] args){
//        String subject="<http://dsc.nlp-bigdatalab.org:8086/氧化锌软膏>";
//        String predicate="<http://dsc.nlp-bigdatalab.org:8086/药品相关疾病>";
//        String object="<http://dsc.nlp-bigdatalab.org:8086/MMMMMMMMMM>";
//        String object2="<http://dsc.nlp-bigdatalab.org:8086/aaaaaaaaaaaa>";
        insertentity("罗伟志型傻逼正","药品");
        //insertrelation("2555氧化锌软11cc膏","药品","2ooiii11ccoooo","疾病");
        //read();
    }

}
