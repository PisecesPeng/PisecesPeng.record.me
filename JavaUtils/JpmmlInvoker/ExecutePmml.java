import org.dmg.pmml.FieldName;

import java.util.HashMap;
import java.util.Map;

public class ExecutePmml {
    public static void main(String[] args) throws Exception {
        // get pmml by resources path
        PmmlInvoker pmmlInvoker = new PmmlInvoker("pmml/iris_rf.pmml");
        // get pmml params
        Map<FieldName, Object> params = new HashMap<>();
        params.put(new FieldName("Sepal.Length"), 5.1);
        params.put(new FieldName("Sepal.Width"), 3.5);
        params.put(new FieldName("Petal.Length"), 1.4);
        params.put(new FieldName("Petal.Width"), 0.1);
        params.put(new FieldName("Species"), 0);
        // pmml invoke
        pmmlInvoker.invoke(params);
    }
}
