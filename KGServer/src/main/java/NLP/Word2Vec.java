package NLP;

import com.hankcs.hanlp.corpus.io.IOUtil;
import com.hankcs.hanlp.mining.word2vec.DocVectorModel;
import com.hankcs.hanlp.mining.word2vec.Word2VecTrainer;
import com.hankcs.hanlp.mining.word2vec.WordVectorModel;

import java.io.*;
import java.util.ArrayList;
import java.util.Map;


public class Word2Vec {
    private static final String TRAIN_FILE_NAME = "src/kb/kb_service/yuliao.txt";
    private static final String MODEL_FILE_NAME = "src/kb/kb_service/word2vec.txt";

    public static void main(String[] args) throws IOException
    {
        WordVectorModel wordVectorModel = trainOrLoadModel();
        printNearest("感冒", wordVectorModel);
        printNearest("发烧", wordVectorModel);
        printNearest("感冒发烧", wordVectorModel);
        DocVectorModel docVectorModel = new DocVectorModel(wordVectorModel);
        ArrayList<String> documents = new ArrayList<String>();
        try{
            File file = new File("src/kb/kb_service/q_t_0.txt");
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line = null;
            int i =0 ;
            while( (line = br.readLine())!=null){
                documents.add(line);
                docVectorModel.addDocument(i, line);
                i += 1;
            }
        }catch (FileNotFoundException  e){
            e.printStackTrace();
        }
        printNearestDocument("得了感冒怎么办", documents, docVectorModel);
    }

    static void printNearest(String word, WordVectorModel model) {
        System.out.println("\n                                                Word     Cosine\n------------------------------------------------------------------------\n");
        for (Map.Entry<String, Float> entry : model.nearest(word))
        {
            System.out.printf("%50s\t\t%f\n", entry.getKey(), entry.getValue());
        }
    }

    static void printNearestDocument(String document, ArrayList documents, DocVectorModel model) {
        printHeader(document);
        int i = 1;
        for (Map.Entry<Integer, Float> entry : model.nearest(document))
        {
            System.out.printf("%50s\t\t%f\n", documents.get(entry.getKey()), entry.getValue());
            i++;
        }
    }

    private static void printHeader(String query) {
        System.out.printf("\n%50s          Cosine\n------------------------------------------------------------------------\n", query);
    }

    static WordVectorModel trainOrLoadModel() throws IOException {
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
        return loadModel();
    }

    static WordVectorModel loadModel() throws IOException {
        return new WordVectorModel(MODEL_FILE_NAME);
    }

}
