/*
 * Generate data set from given arff file and sequence files
 */
package proteinprediction.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import proteinprediction.rawdata.StructuralFastaSeq;
import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ArffLoader;
import weka.core.converters.ArffSaver;

/**
 * Generate data set from given arff file and sequence files
 * @author Shen Wei
 */
public class DatasetGenerator {
    private File input;
    private File output;
    private static FastVector classLabels = null;
    private static Attribute classAttr = null;
    private HashMap<String, StructuralFastaSeq> structuralFastaDB;
    
    public static final String CLASS_LABEL_UNKNOWN = "X";
    
    public DatasetGenerator(File in, File out, File seqdb) 
            throws IOException 
    {
        this.input = in;
        this.output = out;
        this.structuralFastaDB = StructuralFastaLoader.loadFromFile(seqdb);
    }
    
    public void generateDataset() throws IOException {
        
        Pattern pattern = Pattern.compile("^(.*)_(\\d+)$");
        
        ArffLoader loader = new ArffLoader();
        loader.setFile(input);
        
        Instances dataset = new Instances(
                new BufferedReader(
                new FileReader(input)));
        //add new attribute
        dataset.insertAttributeAt(getClassAttribute(), dataset.numAttributes());
        dataset.setClassIndex(dataset.numAttributes() - 1);
        
        for (int i = 0; i < dataset.numInstances(); i++) {
            Instance inst = dataset.instance(i);
            
            String idPos = inst.stringValue(0);
            Matcher matcher = pattern.matcher(idPos);
            
            if (!matcher.matches()) continue;
            String protein = matcher.group(1);
            int pos = Integer.parseInt(matcher.group(2));
            String label = getResidueClassLabel(protein, pos);
            try {
                inst.setClassValue(label);
            } catch (Exception e) {
                e.printStackTrace();
                System.err.println(label);
            }
            if (i != 0 && i % 1000 == 0)
                System.err.println("Processed " + i + " lines");
        }

                
        //save changed data set
        ArffSaver saver = new ArffSaver();
        saver.setCompressOutput(true);
        saver.setFile(output);
        saver.setInstances(dataset);
        saver.writeBatch();
    }
    
    
    public static void main(String[] args) {
        if (args.length == 0) return;
        try {
            DatasetGenerator dg = new DatasetGenerator(
                new File(args[0]),
                new File(args[1]),
                new File(args[2]));
            dg.generateDataset();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    private static FastVector getClassLabels() {
        if (classLabels == null) {
            classLabels = new FastVector();
            classLabels.addElement("L");
            classLabels.addElement("H");
         //   classLabels.addElement(CLASS_LABEL_UNKNOWN);
          classLabels.addElement("U");
            classLabels.addElement("I");
            classLabels.addElement("1");
            classLabels.addElement("2");
            classLabels.addElement(" ");
        }
        return classLabels;
    }

    private String getResidueClassLabel(String protein, int pos) {
        StructuralFastaSeq sseq = this.structuralFastaDB.get(protein);
        String label = ""+sseq.getResidueStructure(pos);
        if (getClassLabels().contains(label))
            return label;
        return CLASS_LABEL_UNKNOWN;
    }

    private Attribute getClassAttribute() {
        if (classAttr == null) {
            classAttr = new Attribute("CLASS", getClassLabels());
        }
        return classAttr;
    }
}
