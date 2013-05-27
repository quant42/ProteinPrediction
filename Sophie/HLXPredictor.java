import java.util.Random;

import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Remove;
import weka.classifiers.Evaluation;
import weka.classifiers.trees.J48;

public class HLXPredictor {
	
	// loads instances from arff files
	public static Instances getInstances(String path){
		
		System.out.println("Loading Instances");
		DataSource source=null;
		try{
			source= new DataSource(path);
		}
		catch(Exception e){
			System.out.println("Exception Training DataSource Creation");
			System.out.println(e.getMessage());
			System.exit(1);
		}
		
		Instances inst=null;
		try{
			inst = source.getDataSet();
		}
		catch(Exception e){
			System.out.println("Exception Training Instances Creation");
			System.out.println(e.getMessage());
			System.exit(1);
		}
		return inst;
	}

	//removes attributes that are not considered in the prediction
	public static Instances removeAttributes(Instances inst){
		
		System.out.println("Removing attributes");
		
		String[] options = new String[3];
		options[0]="-V";
		options[1]="-R";
		options[2]="182-401,762-981,1229-1239,1403-1413,1432-1442,1490-1500,1577-1587,1606-1616,1635-1645,1780-1790,1838-1848,1867-1877,2096-2315,2707-2717,last";
		Remove remove = new Remove();
		try{
			remove.setOptions(options);
		}	
		catch(Exception e){
			System.out.println("Execption Set Remove Options");
			System.out.println(e.getMessage());
			System.exit(1);
		}
		
		try{
			remove.setInputFormat(inst);
		}
		catch(Exception e){
			System.out.println("Exception in Setting InputFormat");
			System.out.println(e.getMessage());
			System.exit(1);
		}
		
		try{
			inst = Filter.useFilter(inst, remove);
		}
		catch(Exception e){
			System.out.println("Exception removing attributes");
			System.exit(1);
		}
		
		return inst;
		
	}
	
	// sets attribute that should be predicted
	public static void setClass(Instances inst){
		
		inst.setClassIndex(inst.numAttributes() - 1);
		
	}
	
	//trains a J48 tree
	public static J48 train(Instances instTrain){
		
		System.out.println("Building j48 tree");
		
		String[] options = new String[4];
		options[0] = "-C";
		options[1]="0.01";
		options[2]="-M";
		options[3]="3";
		J48 tree = new J48();
		try{
			tree.setOptions(options);
		}
		catch(Exception e){
			System.out.println("Exception Setting Tree J48 Options");
			System.out.println(e.getMessage());
			System.exit(1);
		}
		
		try{
			tree.buildClassifier(instTrain);
		}
		catch(Exception e){
			System.out.println("Exception building classifier tree");
			System.out.println(e.getMessage());
			System.exit(1);
		}

		return tree;
		
	}
	
	//makes prediction for every instance and stores them in a String[#Instances][2]
	//for every instance [0] contains the IDpos attribute and [1] contains just one letter: L, H or X
	public static String[][] predict(J48 tree, Instances instTest, Instances instIdentifier){

		String[][] res = new String [instTest.numInstances()][2];
		
		for (int i = 0; i < instTest.numInstances(); i++) {
			
			double clsLabel=0;
		 	try{
		 		clsLabel = tree.classifyInstance(instTest.instance(i));
		 	}
		 	catch(Exception e){
		 		System.out.println("Exception in classifying label" + i);
		 		System.out.println(e.getMessage());
		 		System.exit(1);
		 	}
		 	
		 	
		   instIdentifier.instance(i).setClassValue(clsLabel);
		   
		   res[i][0]=instIdentifier.instance(i).stringValue(0);
		   
		   if(clsLabel==1.0){
			   res[i][1]="H";
		   }
		   else if(clsLabel==0.0){
			   res[i][1]="L";
		   }
		   else{
			   res[i][1]="X";
		   }
		}
		
		return res;
		
	}
	
	//method to start the predictor, just enter path to training and to predicition file (tha raw and unedited files)
	public static void runPredictor (String PathTrain, String PathPred){
		
		Instances instTrain=getInstances(PathTrain);
		
		instTrain=removeAttributes(instTrain);
		setClass(instTrain);
		
		J48 tree=train(instTrain);
		
		Instances instTest=getInstances(PathPred);
		Instances instIdentifier=new Instances(instTest);
		instTest=removeAttributes(instTest);
		setClass(instTest);
		setClass(instIdentifier);
		
		String [][] prediction = predict(tree, instTest, instIdentifier);
		
//		for (int i=0; i<prediction.length; i++){
//			System.out.println(prediction[i][0]+ "\t"+prediction[i][1] );
//		}
		
	}
	
	public static void main(String args[]){
		
		//runPredictor("/home/marie-sophie/Uni/ProteinPrediction/Arff/TestSet/Train1n.arff", "/home/marie-sophie/Uni/ProteinPrediction/Arff/TestSet/Train2n.arff");

		
		
	}

}
