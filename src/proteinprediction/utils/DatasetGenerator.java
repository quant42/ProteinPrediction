/*
 * Generate data set from given arff file and sequence files
 * License: GPL
 */
package proteinprediction.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import proteinprediction.io.StructuralFastaLoader;
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
     * 
     * @param in input arff file
     * @param seqdb input structural fasta database
     * @throws IOException 
     */
    public DatasetGenerator(File in, File seqdb) 
            throws IOException 
    {
        this.input = in;
        this.structuralFastaDB = StructuralFastaLoader.loadFromFile(seqdb);
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
        if (sseq == null) return null;
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

    /**
     * generate converted arff file and write into output file
     * @return processed dataset
     * @throws IOException 
     */
    public Instances generateDataset() throws IOException {
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
        }
        
        //remove unwanted attributes: class and ID_POS
        dataset.deleteAttributeAt(oldClassIdx);
        dataset.deleteAttributeAt(0);
        
        //remove instances whose class label is missing
        dataset.deleteWithMissingClass();
        
        return dataset;
    }
    
    /**
     * for test purpose 
     * @param args input_path output_path db_path
     */
    public static void main(String[] args) {
        try {
            DatasetGenerator dg = new DatasetGenerator(
                new File(args[0]),
                new File(args[2]));
            File output = new File(args[1]);
            Instances dataset = dg.generateDataset();
            ArffSaver saver = new ArffSaver();
            saver.setFile(output);
            saver.setInstances(dataset);
            saver.writeBatch();
            
        } catch(IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Usage: DataGenerator <input.arff> <output.arff>"
                    + " <structural_fasta>");
        }
    }
}
