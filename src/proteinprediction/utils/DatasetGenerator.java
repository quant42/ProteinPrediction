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
import proteinprediction.ProgramEntryPoint;
import proteinprediction.ProgramSettings;

/**
 * Generate data set from given arff file and sequence files
 * for training a predictor of TML and TMH
 * @author Shen Wei
 */
public class DatasetGenerator implements ProgramEntryPoint {
    
    /**
     * name of class attribute
     */
    public static final String CLASS_ATTR_NAME = "TMH_TML";
    
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
     * A simple DatasetGenerator that does nothing!
     */
    public DatasetGenerator() {}
    
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
     * for test purpose 
     * @param args input_path output_path db_path
     */
    @Override
    public int run(String[] args) {
        try {
            File inputFile = new File(args[0]);
            File strFasta = new File(args[1]);
            File outDir = new File(args[2]);
            
            if (outDir.exists() && !outDir.isDirectory()) {
                throw new IllegalArgumentException("Output directory is not a directory!");
            } else if (!outDir.exists()) {
                outDir.mkdir();
            }
            
            DatasetGenerator dg = new DatasetGenerator(
                inputFile,
                strFasta);
            Instances dataset = dg.generateDataset();
            
            //select features over balanced full data set
            Instances fullBalanced = DatasetPreprocessor.getBalancedDataset(
                    dataset, true, dataset.numAttributes() - 1);
            String indices = DatasetPreprocessor.featureIndicesStringSelection(
                    fullBalanced, fullBalanced.numAttributes()-1, 300);
            
            //reduce full unbalanced dataset
            dataset = DatasetPreprocessor.selectFeatures(dataset, indices);
            
            //generate subsets
            String weights = "1";
            boolean balance = true;
            
            if (args.length > 4) {
                weights = args[4];
            }
            
            if (args.length > 5) {
                balance = Boolean.parseBoolean(args[5]);
            }
            
            Instances[] subsets = DatasetPreprocessor.splitDataset(
                    dataset, DatasetPreprocessor.getWeights(weights));
            ArffSaver saver = new ArffSaver();
            for (int i = 0; i < subsets.length; i++) {
                Instances subset = subsets[i];
                if (balance) {
                    subset = DatasetPreprocessor.getBalancedDataset(
                            subset, 
                            true, 
                            dataset.numAttributes()-1);
                }
                
                saver.resetOptions();
                saver.setFile(new File(outDir, "dataset_" + (i+1)+".arff"));
                saver.setInstances(subset);
                saver.writeBatch();
            }
            
        } catch(IOException e) {
            return ProgramSettings.PROGRAM_EXIT_IOERROR;
        } catch (Exception e) {
            return ProgramSettings.PROGRAM_EXIT_MALFORMED_ARGS;
        }
        return ProgramSettings.PROGRAM_EXIT_NORMAL;
    }
    
    /**
     * returns the usage of this entry point
     */
    @Override
    public String getUsageAndHelp() {
        StringBuilder sb = new StringBuilder();
        sb.append("Usage: DatasetGenerator <input.arff> ");
        sb.append("<structural_fasta> <output_directory> ");
        sb.append(" [subsets=1] [comma_separated_weights] [balance_classes=true]\n");
        
        sb.append("Example:\n");
        sb.append("DataGenerator tmps.arff imp_structure.fasta output/ \\\n"
                + "              3 0.64,0.2,0.16 true");
        return sb.toString();
    }
    
    /**
     * @return A little description of this program mode
     */
    @Override
    public String getShortDescription() {
        return "read in an arff and a fasta file and outputs an dataset file!";
    }

    /**
     * @return The name of this program mode
     */
    @Override
    public String getCommandLineArgumentName() {
        return "DataGenerator";
    }

    /**
     * get a vector of class labels
     * @return 
     */
    public static FastVector getClassLabels() {
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
        label = label.toUpperCase();
        if (getClassLabels().contains(label))
            return label;
        return null;
    }

    /**
     * get class Attribute of the new dataset
     * @return 
     */
    public static Attribute getClassAttribute() {
        if (classAttr == null) {
            classAttr = new Attribute(CLASS_ATTR_NAME, getClassLabels());
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
        dataset.deleteStringAttributes();
        
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
