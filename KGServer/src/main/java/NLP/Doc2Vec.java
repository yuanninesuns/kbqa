package NLP;

import com.hankcs.hanlp.corpus.io.IOUtil;
import com.hankcs.hanlp.mining.word2vec.DocVectorModel;
import com.hankcs.hanlp.mining.word2vec.Word2VecTrainer;
import com.hankcs.hanlp.mining.word2vec.WordVectorModel;

import java.io.*;
import java.util.ArrayList;
import java.util.Map;

public class Doc2Vec {

    private  DocVectorModel docVectorModel = null;
    private  ArrayList<String> documents = null;
    private  static final String MODEL_FILE_NAME = "data/word2vec.txt";
    private  static final String TRAIN_FILE_NAME = "data/yuliao.txt";
    private  static final String SIM_QUESTION_FILE_NAME = "data/simQA/question.txt";
    private  static final String SIM_ANSWER_FILE_NAME = "data/simQA/answer/";

    public Doc2Vec(){
        init();
    }

    private void init(){
        try{
            WordVectorModel wordVectorModel = trainOrLoadModel();
            documents = new ArrayList<>();
            docVectorModel = new DocVectorModel(wordVectorModel);
            File file = new File(SIM_QUESTION_FILE_NAME);
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line = null;
            int index = 0;
            String question = null;
            while( (line = br.readLine())!=null){
                index = Integer.parseInt(line.split("&")[0]);
                question = line.split("&")[1];
                documents.add(question);
                docVectorModel.addDocument(index, question);
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    private WordVectorModel loadModel() throws IOException {
        return new WordVectorModel(MODEL_FILE_NAME);
    }

    private WordVectorModel trainOrLoadModel() throws IOException {
        if (!IOUtil.isFileExisted(MODEL_FILE_NAME))
        {
            if (!IOUtil.isFileExisted(TRAIN_FILE_NAME))
            {
                System.err.println("语料不存在");
                return null;
            }
            Word2VecTrainer trainerBuilder = new Word2VecTrainer();
            return trainerBuilder.train(TRAIN_FILE_NAME, MODEL_FILE_NAME);
        }
        return loadModel();
    }

    public ArrayList<String> getSimQA(String document){
        ArrayList<String> results = new ArrayList<>();
        String result_templ = "%s&%s";
        String answer_file_templ = SIM_ANSWER_FILE_NAME + "%d.txt";
        try{
            File answer_directory = new File(SIM_ANSWER_FILE_NAME);
            if(!answer_directory.isDirectory()){
                System.out.println("答案文件夹不存在");
            }else{
                for (Map.Entry<Integer, Float> entry : docVectorModel.nearest(document)) {
                    String question = documents.get(entry.getKey());
                    int index = entry.getKey();
                    File answer_file = new File(String.format(answer_file_templ,index));
                    StringBuilder answer = new StringBuilder();
                    BufferedReader br = new BufferedReader(new FileReader(answer_file));
                    String line = null;
                    while( (line = br.readLine())!=null){
                        answer.append(line);
                        answer.append("</br>");
                    }
                    results.add(String.format(result_templ,question,answer.toString()));
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return results;
    }
}
