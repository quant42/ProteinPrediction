/*
 * Generate data set from given arff file and sequence files
 * License: GPL
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
import weka.core.converters.ArffSaver;

/**
 * Generate data set from given arff file and sequence files
 * for training a predictor of TML and TMH
 * @author Shen Wei
 */
public class DatasetGenerator {
    /**
     * input arff file
     */
    private File input;
    
    /**
     * out put arff.gz file
     */
    private File output;
    
    /**
     * class labels for new class attribute
     */
    private static FastVector classLabels = null;
    
    /**
     * new class attribute
     */
    private static Attribute classAttr = null;
    
    /**
     * database for fasta sequences with structural info
     */
    private HashMap<String, StructuralFastaSeq> structuralFastaDB;
    
    /**
     * class label for non-TMH/TML residues
     */
    //public static final String CLASS_LABEL_UNKNOWN = "X";
    
    /**
     * 
     * @param in
     * @param out
     * @param seqdb
     * @throws IOException 
     */
    public DatasetGenerator(File in, File out, File seqdb) 
            throws IOException 
    {
        this.input = in;
        this.output = out;
        this.structuralFastaDB = StructuralFastaLoader.loadFromFile(seqdb);
    }
    
    /**
     * generate converted arff file and write into output file
     * @throws IOException 
     */
    public void generateDataset() throws IOException {
        
        Pattern pattern = Pattern.compile("^(.*)_(\\d+)$");
       
        Instances dataset = new Instances(
                new BufferedReader(
                new FileReader(input)));
        
        int oldClassIdx = dataset.numAttributes() - 1;
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
            if (label == null) {
                inst.setClassMissing();
            } else {
                inst.setClassValue(label);
            }
            if (i != 0 && i % 1000 == 0)
                System.err.println("Processed " + i + " lines");
        }
        
        //remove unwanted attributes: class and ID_POS
        dataset.deleteAttributeAt(oldClassIdx);
        dataset.deleteAttributeAt(0);
        
        //remove instances whose class label is missing
        dataset.deleteWithMissingClass();
        
        //save changed data set
        ArffSaver saver = new ArffSaver();
        saver.setCompressOutput(true);
        saver.setFile(output);
        saver.setStructure(dataset);
        saver.writeBatch();
    }
    
    /**
     * for test purpose 
     * @param args input_path output_path db_path
     */
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

    /**
     * get a vector of class labels
     * @return 
     */
    private static FastVector getClassLabels() {
        if (classLabels == null) {
            classLabels = new FastVector();
            classLabels.addElement("L");
            classLabels.addElement("H");
        }
        return classLabels;
    }

    /**
     * get class label of a residue in the given protein
     * @param protein Uniprot name of the protein
     * @param pos index of the residue, starts from 0
     * @return class label to the residue, or null if class label in unknown
     */
    private String getResidueClassLabel(String protein, int pos) {
        StructuralFastaSeq sseq = this.structuralFastaDB.get(protein);
        String label = ""+sseq.getResidueStructure(pos);
        if (getClassLabels().contains(label))
            return label;
        return null;
    }

    /**
     * get class Attribute of the new dataset
     * @return 
     */
    private Attribute getClassAttribute() {
        if (classAttr == null) {
            classAttr = new Attribute("CLASS", getClassLabels());
        }
        return classAttr;
    }
}
