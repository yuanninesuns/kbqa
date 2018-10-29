package TDB;

import org.apache.jena.query.*;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdfconnection.RDFConnectionFuseki;
import org.apache.jena.rdfconnection.RDFConnectionRemoteBuilder;
import org.apache.jena.tdb.TDBFactory;
import org.apache.jena.tdb.base.file.Location;
import org.apache.jena.util.FileManager;

import java.util.ArrayList;

public class HandleTDB {
    private static final String TDBPath="data/KGQATDB";
    private static final String DATAPath="data/data.ttl";
    private static final String FusekiPath="http://localhost:3030/kg_demo_medicine/query";

    public static void CreateTDB() {
        Dataset ds = TDBFactory.createDataset(TDBPath);//建立了一个test的TDB，如果存储test的TDB，则表示使用这个TDB
        Model model = ds.getDefaultModel();//这里使用TDB的默认Model
        FileManager.get().readModel(model, DATAPath);//读取RDF文件到指定的model里面
        /*
         * 这里要详细说一下如何读取RDF到Model里面的方法了，其实model就有
         * read方法可以对RDF进行读取，但是上面用FileManager会比较好一点，它会自动
         * 处理许多问题
         */
        model.commit();//这里类似于数据库的commint，意思是把现在的操作立刻提交
        model.close();//结束使用的时候，一定要对Model和Dataset进行关闭
        ds.close();
    }
    public static ArrayList<String> ReadTDB(String string){
        ArrayList<String> ans=new ArrayList<>();
        int flag=0;
        Location location =Location.create(TDBPath);
        Dataset dataset=TDBFactory.createDataset(location);
        dataset.begin(ReadWrite.WRITE) ;

        Model model=dataset.getDefaultModel();
        //String string= "SELECT ?x WHERE{<http://dsc.nlp-bigdatalab.org:8086/氧化锌软膏> <http://dsc.nlp-bigdatalab.org:8086/药品相关疾病> ?x}";
        //String string= "SELECT * WHERE{ ?p ?n ?x}limit 10";
        Query query = QueryFactory.create(string);

        QueryExecution queryExecution = QueryExecutionFactory.create(query,model);
        ResultSet results = queryExecution.execSelect();
        while(results.hasNext())
        {
            flag=1;
            QuerySolution querySolution=results.nextSolution();
            ans.add(querySolution.get("?x").toString());
            System.out.println(querySolution.get("?x").toString());
        }
        if(flag==0)
            System.out.println("无答案！");

//        model.commit();
        model.close();
//        dataset.commit();
       dataset.close();


        System.out.println("aaaaaaaaa");
        System.out.println(ans.size());
        return ans;

    }
    public static ArrayList<String> ReadTDBE(String string){
        ArrayList<String> ans=new ArrayList<>();
        int flag=0;

        Location location =Location.create(TDBPath);
        Dataset dataset=TDBFactory.createDataset(location);
        dataset.begin(ReadWrite.WRITE) ;

        Model model=dataset.getDefaultModel();
        //String string= "SELECT ?x WHERE{<http://dsc.nlp-bigdatalab.org:8086/氧化锌软膏> <http://dsc.nlp-bigdatalab.org:8086/药品相关疾病> ?x}";
        //String string= "SELECT * WHERE{ ?p ?n ?x}limit 10";
        Query query = QueryFactory.create(string);

        QueryExecution queryExecution = QueryExecutionFactory.create(query,model);
        ResultSet results = queryExecution.execSelect();
        while(results.hasNext())
        {
            flag=1;
            QuerySolution querySolution=results.nextSolution();
            String relation = querySolution.get("?p").toString();

            if(!relation.contains("#")){
                ans.add(relation.split("/")[3].substring(2)+"     "+querySolution.get("?x").toString().split("/")[3]);
                System.out.println(relation.split("/")[3].substring(2)+"     "+querySolution.get("?x").toString().split("/")[3]);
            }
        }
        if(flag==0)
            System.out.println("无答案！");

//        model.commit();
        model.close();
//        dataset.commit();
        dataset.close();


        System.out.println("aaaaaaaaa");
        System.out.println(ans.size());
        return ans;

    }
    public static ArrayList<String> ReadTDBF(String string){
        ArrayList<String> ans=new ArrayList<>();
        int flag=0;
        Location location =Location.create(TDBPath);
        Dataset dataset=TDBFactory.createDataset(location);
        dataset.begin(ReadWrite.WRITE) ;

        Model model=dataset.getDefaultModel();
        //String string= "SELECT ?x WHERE{<http://dsc.nlp-bigdatalab.org:8086/氧化锌软膏> <http://dsc.nlp-bigdatalab.org:8086/药品相关疾病> ?x}";
        //String string= "SELECT * WHERE{ ?p ?n ?x}limit 10";
        Query query = QueryFactory.create(string);

        QueryExecution queryExecution = QueryExecutionFactory.create(query,model);
        ResultSet results = queryExecution.execSelect();
        while(results.hasNext())
        {
            flag=1;
            QuerySolution querySolution=results.nextSolution();
            String subject = querySolution.get("?s").toString().split("/")[3];
            String relation = querySolution.get("?p").toString();
            if(!relation.contains("#")){
                ans.add(subject+"     "+relation.split("/")[3].substring(2)+"     "+querySolution.get("?x").toString().split("/")[3]);
                System.out.println(subject+"     "+relation.split("/")[3].substring(2)+"     "+querySolution.get("?x").toString().split("/")[3]);
            }else if(relation.split("#")[1].equals("type")){
                ans.add(subject+"     "+"类型     "+querySolution.get("?x").toString().split("/")[3]);
                System.out.println(subject+"     "+"类型     "+querySolution.get("?x").toString().split("/")[3]);
            }else if(relation.split("#")[1].equals("label")){
                ans.add(subject+"     "+"名称     "+querySolution.get("?x").toString());
                System.out.println(subject+"     "+"名称     "+querySolution.get("?x"));
            }
        }
        if(flag==0)
            System.out.println("无答案！");

//        model.commit();
        model.close();
//        dataset.commit();
        dataset.close();


        System.out.println("aaaaaaaaa");
        System.out.println(ans.size());
        return ans;

    }
    public static void ReadTDBFuseki(String string){
        RDFConnectionRemoteBuilder builder = RDFConnectionFuseki.create()
                .destination(FusekiPath);
        Query query = QueryFactory.create(string);
        try ( RDFConnectionFuseki conn = (RDFConnectionFuseki)builder.build() ) {
            conn.queryResultSet(query, ResultSetFormatter::out);
        }
    }
    public static void main(String []args){
        CreateTDB();

    }

}

