import org.dmg.pmml.FieldName;
import org.dmg.pmml.PMML;
import org.jpmml.evaluator.*;
import org.jpmml.model.PMMLUtil;
import org.springframework.util.MethodInvoker;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class PmmlInvoker {

    private ModelEvaluator modelEvaluator;

    /**
     * 通过文件读取模型
     */
    public PmmlInvoker(String pmmlFile) {
        PMML pmml = null;
        InputStream is = null;
        try {
            if (pmmlFile != null) {
                is = MethodInvoker.class.getClassLoader().getResourceAsStream(pmmlFile);
                pmml = PMMLUtil.unmarshal(is);
            }
            try {
                is.close();
            } catch (IOException localIOException) {
            }
            this.modelEvaluator = ModelEvaluatorFactory.newInstance().newModelEvaluator(pmml);
        } catch (SAXException e) {
            pmml = null;
        } catch (JAXBException e) {
            pmml = null;
        } finally {
            try {
                is.close();
            } catch (IOException localIOException3) {
                // throw ...
            }
        }
        this.modelEvaluator.verify();
        System.out.println("模型读取成功");
    }

    /**
     * 通过输入流读取模型
     */
    public PmmlInvoker(InputStream is) {
        PMML pmml = null;
        try {
            pmml = PMMLUtil.unmarshal(is);
            try {
                is.close();
            } catch (IOException localIOException) {
            }
            this.modelEvaluator = ModelEvaluatorFactory.newInstance().newModelEvaluator(pmml);
        } catch (SAXException e) {
            pmml = null;
        } catch (JAXBException e) {
            pmml = null;
        } finally {
            try {
                is.close();
            } catch (IOException localIOException3) {
                // throw ...
            }
        }
        this.modelEvaluator.verify();
        System.out.println("模型读取成功");
    }

    /**
     * 执行pmml
     */
    public Map<FieldName, ?> invoke(Map<FieldName, Object> paramsMap) {
        // 获得pmml文件的fields
        List<ModelField> inputFields = this.modelEvaluator.getInputFields();
        List<ModelField> outputFields = this.modelEvaluator.getOutputFields();
        List<ModelField> targetFields = this.modelEvaluator.getTargetFields();
        // 输出pmml文件的fields信息
        this.printFields(inputFields, "input");
        this.printFields(outputFields, "output");
        this.printFields(targetFields, "target");

        // 输出pmml执行的结果
        Set<FieldName> keySet = this.modelEvaluator.evaluate(paramsMap).keySet();  //获取结果的keySet
        for (FieldName fn : keySet) {
            System.out.println(fn + " : " + this.modelEvaluator.evaluate(paramsMap).get(fn).toString());
        }

        // 返回pmml执行的结果
        return this.modelEvaluator.evaluate(paramsMap);
    }

    /**
     * 输出fields
     */
    private void printFields(List<ModelField> fields, String fieldType) {
        fields.forEach( item -> {
            System.out.println(fieldType + "-start---------------");
            if (item.getDataType() == null) {
                System.out.println(">>>>>String<<<<<");
            } else {
                System.out.println(item.getDataType().value());
            }
            System.out.println(item.getName().getValue());
            System.out.println(fieldType + "-start---------------");
        });
    }

}
