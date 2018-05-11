package TextStructure;

import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.util.StringUtils;

import java.util.Properties;

public class CorenlpLoader {

    private static CorenlpLoader instance;
    private  static StanfordCoreNLP pipeline;

    private CorenlpLoader() {
        Properties frenchProperties = StringUtils.argsToProperties("-props", "StanfordCoreNLP-french.properties");
        pipeline = new StanfordCoreNLP(frenchProperties);
    }

    public static CorenlpLoader getInstance() {
        if(instance != null) {
            return instance;
        }
        instance = new CorenlpLoader();
        return instance;
    }

    public static StanfordCoreNLP getPipeline() {
        return pipeline;
    }
}
