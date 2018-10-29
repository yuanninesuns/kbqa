package org.horizon.controller.emp;

import NLP.QunN;
import NLP.Doc2Vec;
import TDB.InsertTDB;
import org.apache.jena.base.Sys;
import org.horizon.KGServer;
import org.horizon.bean.Employee;
import org.horizon.bean.Position;
import org.horizon.bean.RespBean;
import org.horizon.common.EmailRunnable;
import org.horizon.common.poi.PoiUtils;
import org.horizon.service.DepartmentService;
import org.horizon.service.EmpService;
import org.horizon.service.JobLevelService;
import org.horizon.service.PositionService;
import org.neo4j.driver.v1.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutorService;

/**
 * Created by sang on 2018/1/12.
 */
@RestController
@RequestMapping("/employee/basic")
public class EmpBasicController {
    @Autowired
    EmpService empService;
    @Autowired
    DepartmentService departmentService;
    @Autowired
    PositionService positionService;
    @Autowired
    JobLevelService jobLevelService;
    @Autowired
    ExecutorService executorService;

    //基础查询  ——需要整合
    @RequestMapping(value = "/basicdata", method = RequestMethod.POST)
    public static ArrayList<String> getAllNations(String str) {
        ArrayList<String> arr = new ArrayList<String>();
        System.out.println("aaaaavavvddd");
        arr= QunN.getResults(str);
        System.out.println("aaaaaaddd");
        return arr;
    }

    @RequestMapping(value = "/maxWorkID", method = RequestMethod.POST)
    //高级查询 ——需要整合
    public ArrayList<String> maxWorkID(String s1,String s2,String s3) {
        System.out.println(s1+" "+s2+" "+s3);
        String lines = s2+"&"+s1+"&"+s3;
        System.out.println(lines);
        ArrayList<String> arr = QunN.getResults_S(lines);
        for(String strs: arr){
            System.out.println(strs);
        }
        return arr;
    }

    //点击关键字出现球球 ——需要整合
    @RequestMapping(value = "/emp", method = RequestMethod.POST)
    public static ArrayList<String> addEmp(String key) {
        System.out.println(key);
//        key = key.split("@")[0];
        Driver driver = GraphDatabase.driver("bolt://localhost:7687", AuthTokens.basic("neo4j", "password"));
        ArrayList<String> arr = new ArrayList<>();
        arr.add(key);
        try (Session session = driver.session()) {
            StatementResult result = session.run("match(n{name:'"+key+"'})--(m:YiItem)" +
                    "return m.name as name,m.type as type,n.name,n.type limit 10");
            System.out.println("80 "+result);
            while (result.hasNext() )
            {
                Record record = result.next();
                arr.add(record.get("name").asString());
                System.out.println(record);
            }
        }
        driver.close();
        return arr;
    }

    //用户添加节点数据
    @RequestMapping(value = "/exportEmp", method = RequestMethod.POST)
    public Boolean exportEmp(String kind,String node) {
        System.out.println(kind+" "+node);
        String fileName="src/main/java/org/horizon/controller/emp/newnode.txt";
        try {
            //使用这个构造函数时，如果存在kuka.txt文件，
            //则直接往kuka.txt中追加字符串
            System.out.println("145");
            FileWriter writer=new FileWriter(fileName,true);
            writer.write(kind + " " + node + "\n");
            writer.close();
        } catch (IOException e) {
            System.out.println("151");
            e.printStackTrace();
        }
        return true;
    }

    //用户添加关系数据
    @RequestMapping(value = "/exportEmp2", method = RequestMethod.POST)
    public Boolean exportEmp2(String kind,String node,String kind2,String node2) {
        System.out.println(kind + " " + node + kind2 + " " + node2);
        String fileName="src/main/java/org/horizon/controller/emp/newrelation.txt";
        try {
            FileWriter writer=new FileWriter(fileName,true);
            writer.write(kind + " " + node + " " +kind2 + " " + node2 +"\n");
            writer.close();
        } catch (IOException e) {
            System.out.println("添加关系数据失败");
            e.printStackTrace();
        }
        return true;
    }

    //加载新节点数据
    @RequestMapping(value = "/importEmp", method = RequestMethod.POST)
    public ArrayList<String> importEmp() {
        File file=new File("src/main/java/org/horizon/controller/emp/newnode.txt");
        BufferedReader reader=null;
        String temp=null;
        ArrayList<String> arr = new ArrayList<>();
        try{
            reader=new BufferedReader(new FileReader(file));
            while((temp=reader.readLine())!=null){
                System.out.println(temp);
                arr.add(temp);
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
        finally{
            if(reader!=null){
                try{
                    reader.close();
                }
                catch(Exception e){
                    e.printStackTrace();
                }
            }
        }
        return arr;
    }

    //加载新关系数据
    @RequestMapping(value = "/importEmp2", method = RequestMethod.POST)
    public ArrayList<String> importEmp2() {
        File file=new File("src/main/java/org/horizon/controller/emp/newrelation.txt");
        BufferedReader reader=null;
        String temp=null;
        ArrayList<String> arr = new ArrayList<>();
        try{
            reader=new BufferedReader(new FileReader(file));
            while((temp=reader.readLine())!=null){
                System.out.println(temp);
                arr.add(temp);
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
        finally{
            if(reader!=null){
                try{
                    reader.close();
                }
                catch(Exception e){
                    e.printStackTrace();
                }
            }
        }
        return arr;
    }

    //管理审核新节点 ——需要整合
    @RequestMapping(value = "/addNewNode", method = RequestMethod.POST)
    public boolean addNewNode(String node,String value){
        //if(node.equals("yao"))

        if(InsertTDB.insertentity(value,node)){
            Driver driver = GraphDatabase.driver("bolt://localhost:7687", AuthTokens.basic("neo4j", "password"));
            try (Session session = driver.session()) {
                StatementResult result = session.run("CREATE (YiItem {name:"+value+",type:"+node+"})");
            }
            driver.close();
            System.out.println("添加新节点"+node+" "+value);
            return true;
        }
        else
            return false;

    }

    //管理审核新关系 ——需要整合
    @RequestMapping(value = "/addNewRelation",method = RequestMethod.POST)
    public boolean addNewRelation(String node,String value,String node2,String value2){
        if(InsertTDB.insertrelation(value,node,value2,node2)){
            String relation = node+"相关"+node2;
            Driver driver = GraphDatabase.driver("bolt://localhost:7687", AuthTokens.basic("neo4j", "password"));
            try (Session session = driver.session()) {
                StatementResult result = session.run("MATCH (a:YiItem),(b:YiItem)" +
                        "WHERE a.name = '+value+' AND b.name = '+value2+'" +
                        "CREATE (a)-[:YiRELATION { type: '+relation+' }]->(b)");
            }
            driver.close();
            System.out.println("添加新关系"+node+" "+value+" "+node2+" "+value2);
            return true;
        }
        return false;

    }

    //更新节点文件
    @RequestMapping(value = "/updateFile",method = RequestMethod.POST)
    public void updateFile(String arr){
        System.out.println("更新文件 " + arr );
        String[] lines = arr.split("@");
        String fileName="src/main/java/org/horizon/controller/emp/newnode.txt";
        try
        {
            //使用这个构造函数时，如果存在kuka.txt文件，
            //则先把这个文件给删除掉，然后创建新的kuka.txt
            FileWriter writer=new FileWriter(fileName);
            for(String line :lines){
                writer.write(line+"\n");
            }
            writer.close();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    //更新关系文件
    @RequestMapping(value = "/updateRFile",method = RequestMethod.POST)
    public void updateRFile(String arr){
        System.out.println("更新关系文件 " + arr );
        String[] lines = arr.split("@");
        String fileName="src/main/java/org/horizon/controller/emp/newrelation.txt";
        try
        {
            //使用这个构造函数时，如果存在kuka.txt文件，
            //则先把这个文件给删除掉，然后创建新的kuka.txt
            FileWriter writer=new FileWriter(fileName);
            System.out.println("啦啦啦啦");
            for(String line :lines){
                writer.write(line+"\n");
            }
            System.out.println("璐璐璐璐");
            writer.close();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    //相似问题
    @RequestMapping(value = "/getSimilarity",method = RequestMethod.POST)
    public ArrayList<String> getSimilarity(String str){
        Doc2Vec doc2Vec = new Doc2Vec();
        ArrayList<String> arr = doc2Vec.getSimQA(str);
        return  arr;
    }

    public static void main(String args[]){
        ArrayList<String> arr = addEmp("小儿维生素A缺乏病有什么症状");
        for(String str:arr){
            System.out.println("249 "+str);
        }
    }
}
