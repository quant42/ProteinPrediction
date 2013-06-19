
NAME

ProteinPrediction.jar - Predict whether a residue belongs to transmembrane
helices or transmembrane loops.

PLEASE USE java 7 to run the program!

SYNOPSIS

Preprocessing data sets:
	java -jar ProteinPrediction.jar prepareData <tmps.arff> <imp_strucure.fasta> <comma_separated_weights> [features]

Train predictor:
	java -jar ProteinPrediction.jar train <training_set.arff> [features]

Perform prediction:
	java -jar ProteinPrediction.jar predict <input.arff> <result_output.arff> [output.fasta] [include scores in fasta=true/false] [dictionary.fasta]

Perform validation:
	java -jar ProteinPrediction.jar validate <validation_set.arff> [statistics]

Show help information:
	java -jar ProteinPrediction.jar -h|--help|-help|-?|help
					

DESCRIPTION
	This program predicts whether a residue belongs to transmembrane-helices(TMH) or
	transmembrane-loops(TML). It has 4 different run modes: prepareData, train,
	validate and predict. They cover all steps of the machine learning progress.

Run modes:

	prepareData - This run mode reads *.arff file and a FASTA file which
	contains structure information. Then it will generate datasets for further
	steps of machine learning. Following are the main steps:
	
	1, Read input data, class labels "H" (TMH) and "L"(TML) are assigned to each
	instances.
	2, Remove all non-transmembrane instances.
	3, Remove string attributes and old class attribute. (ID_POS, class)
	4, Select attributes by gain ratio. Default: top-200 attributes.
	5, Split dataset into subsets by given weights.
	6, Balance classes by oversampling.

	train - This run mode read training set and train models for the predictor.
	Main steps:

	1, Select features if user wants to.
	2, Train each low-level predictors.
	3, Use low-level predictors to generate training set for high-level
	predictor.
	4, Store trained models.

	validate - This run mode reads test set and evaluate performance of the
	predictor.

	predict - This run mode performs prediction over set of instances using
	trained models. Classification results are appended to the input data. New
	class attribute names "TMH_TML". There's the posibility to output the neuronal
	network confidence in the fasta file. The confidence character is calculated
	with (40 + round(conv * 80))).

Data folders:
	Files used by this program are organized in a series of folders. After
	running the program, following folder structures will be generated in
	current working directory:

	data/
	|
	|---datasets/	stores all preprecossed data sets (e.g. training set)
	|
	|---models/		stores all trained models for predictors
	|
	|---results/	stores all predicted result and statistics

	By new preprocessing and training process stored data sets and models will
	be overwritten. 

EXAMPLES
	Prepare data sets for training, testing, validation. Suppose size of each
	set is 64%, 20% and 16%. And top 150 features are selected.:

	java -jar ProteinPrediction.jar prepareData tmps.arff imp_structure.fasta \ 
		0.64,0.2,0.16 150

	Train model with first subset:
	java -jar ProteinPrediction.jar train data/datasets/subset_1.arff

	Perform prediction and write results into arff and FASTA files:
	java -jar ProteinPrediction.jar predict tmps.arff output.arff output.fasta

	Perform validation and write validation result into text file:
	java -jar ProteinPrediction.jar validate data/dataset/subset_2.arff \
		statistics.txt
