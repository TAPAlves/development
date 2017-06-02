package test.com.silicolife.textmining.machineLearningTests;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.junit.Test;

import com.silicolife.textmining.machinelearning.biotml.core.BioTMLConstants;
import com.silicolife.textmining.machinelearning.biotml.core.annotator.BioTMLMalletAnnotatorImpl;
import com.silicolife.textmining.machinelearning.biotml.core.exception.BioTMLException;
import com.silicolife.textmining.machinelearning.biotml.core.interfaces.IBioTMLAnnotator;
import com.silicolife.textmining.machinelearning.biotml.core.interfaces.IBioTMLCorpus;
import com.silicolife.textmining.machinelearning.biotml.core.interfaces.IBioTMLEntity;
import com.silicolife.textmining.machinelearning.biotml.core.interfaces.IBioTMLModel;
import com.silicolife.textmining.machinelearning.biotml.core.interfaces.IBioTMLModelReader;
import com.silicolife.textmining.machinelearning.biotml.core.interfaces.IBioTMLModelWriter;
import com.silicolife.textmining.machinelearning.biotml.core.models.mallet.MalletClassifierModel;
import com.silicolife.textmining.machinelearning.biotml.reader.BioTMLCorpusReaderImpl;
import com.silicolife.textmining.machinelearning.biotml.reader.BioTMLModelReaderImpl;
import com.silicolife.textmining.machinelearning.biotml.writer.BioTMLModelWriterImpl;

import main.com.silicolife.textmining.machineLearningMains.MLBioTMLModelVSNejiUtils;

public class mainToCreateMLModels {

	//	private String corpusDir="src/test/resources/chemdner/trainFile";
	private String modelClassType= "FAMILY";
	//	private String modelDir="tests/ourModel/models/"+ modelClassType+".gz";
	//	private String sentencesFile = corpusDir +"/text_1000.txt";
	private String corpusSentencesFile="/home/tiagoalves/workspace/development/generalMain/development/developmentSentences/"+modelClassType+"textFiles_1500.txt";
	//	private String annotationsFile = corpusDir +"/train_1000.tsv";
	private String nejiDir="generalMain/NejiCode";
	//	private String nejiSentencesFile=nejiDir + "/sentences/BC2_text_1000.txt";
	//	private String nejiAnnotationsDir=nejiDir+"/annotations";
	private String goldAnnotationsFile="/home/tiagoalves/workspace/development/generalMain/development/developmentAnnotations/"+modelClassType+"annotations_1500.tsv";



	//Test with the development set
	private String corpusDir= "/home/tiagoalves/workspace/development/generalMain/train";//"src/test/resources/chemdner/trainFile";
	//	private String modelClassType= "MULTIPLE";
	private String modelDir="generalMain/OurModel/"+ modelClassType+".gz";
	private String sentencesFile = corpusDir +"/trainSentences/"+modelClassType+"text_1000.txt";//text_1000.txt
	private String annotationsFile = corpusDir +"/trainAnnotations/"+modelClassType+"annotations_1500.tsv";//"/train_1000.tsv";
	private String nejiSentencesFile=nejiDir + "/sentences/BC2_text_1000.txt";
	private String nejiAnnotationsDir="generalMain/"+"train/trainAnnotations";

	

	private IBioTMLCorpus loadCorpus(String documentfile, String annotationfile) throws BioTMLException {
		BioTMLCorpusReaderImpl reader = new BioTMLCorpusReaderImpl();
		if (annotationfile==null || annotationfile.isEmpty()){
			IBioTMLCorpus corpus = reader.readBioTMLCorpusFromBioCFiles(documentfile, "nlp4j");
			return corpus;
		}
		IBioTMLCorpus corpus = reader.readBioTMLCorpusFromBioCFiles(documentfile, annotationfile, "nlp4j");
		return corpus;
	}	
	
	//	@Test
	public void createOURModel() throws BioTMLException{
		System.out.println("Loading the BioTMLCorpus...");

		BioTMLCorpusReaderImpl reader = new BioTMLCorpusReaderImpl();
		IBioTMLCorpus corpus = loadCorpus(sentencesFile, annotationsFile);

		IBioTMLModel svm = new MalletClassifierModel(MLBioTMLModelVSNejiUtils.loadfeatures(), MLBioTMLModelVSNejiUtils.defaultSVMConfiguration(modelClassType, BioTMLConstants.ner.toString()));

		svm.train(corpus);
		new File(modelDir).mkdirs();
		IBioTMLModelWriter writer = new BioTMLModelWriterImpl(modelDir);
		writer.writeGZModelFile(svm); 
		System.out.println("Model Creation finished!");		
	}


	public List<IBioTMLEntity> testWithOurModel(String modelFilePath, String corpusFilePath) throws BioTMLException{
		//		String corpusDir="src/test/resources/chemdner/trainFile";
		//		String modelDir="testeModelos/modeloML/modeloCriado.gz";

		System.out.println("Loading the BioTMLCorpus...");
	
		IBioTMLCorpus corpus = loadCorpus(corpusFilePath, null);

		IBioTMLModelReader modelReader = new BioTMLModelReaderImpl();
		IBioTMLModel svm = modelReader.loadModelFromGZFile(modelFilePath);
		IBioTMLAnnotator annotator = new BioTMLMalletAnnotatorImpl(corpus);
		IBioTMLCorpus annotatedCorpus = annotator.generateAnnotatedBioTMCorpus(svm,8);
		List<IBioTMLEntity> annotationsTest = annotatedCorpus.getEntities();
		//		createAnotatedModelFile(annotatedCorpus, "testeModelos/Corpus/CorpusAnotado.gz");


		//		List<IBioTMLEntity> goldAnnotations = getGoldAnnotations("src/test/resources/chemdner/train/evaluate200.tsv");

		//		evaluateAnnotation(goldAnnotations,annotationsTest);


		//		System.out.println(annotationsTest.get(0).toString());
		//		System.out.println(annotationsTest);
		return annotationsTest;

	}
	//@Test
	public void createConditionsToRunNeji() throws IOException{
		//		classTest.convertBioCreativeTExtFilesIntoBC2(sentencesFile, nejiSentencesFile);

		MLBioTMLModelVSNejiUtils.splitBioCAnnotationsFilesByGroups(annotationsFile,nejiAnnotationsDir);	
		MLBioTMLModelVSNejiUtils.convertBioCAnnotationsToBC2FormatUsingAFolder(nejiAnnotationsDir, nejiAnnotationsDir+"/bc2Files");
		//		classTest.convertFromBioCreativeTEXTFILESIntoBC2UsingAFolder("/home/tiagoalves/workspace/development/generalMain/sentencesGrouped", "/home/tiagoalves/workspace/development/generalMain/sentencesGrouped/bc2Sentences");
	}

	public void evaluateNejiAnnotations(String groupType, String nejiAnnotationsDir) throws BioTMLException{
		List<IBioTMLEntity> annotations = MLBioTMLModelVSNejiUtils.loadAnnotationsFromBC2AnnotationsFolder(nejiAnnotationsDir, groupType);

		List<IBioTMLEntity> goldAnnotations = MLBioTMLModelVSNejiUtils.loadGoldAnnotationsFromTSVFile(goldAnnotationsFile);

		System.out.println("Results for:" + groupType.toUpperCase() +"\n");

		MLBioTMLModelVSNejiUtils.evaluateAnnotation(goldAnnotations,annotations);

	}


	public void evaluateOurModel(String groupType) throws BioTMLException{
		List<IBioTMLEntity> annotations = testWithOurModel(modelDir, corpusSentencesFile);

		List<IBioTMLEntity> goldAnnotations = MLBioTMLModelVSNejiUtils.loadGoldAnnotationsFromTSVFile(goldAnnotationsFile);

		System.out.println("Results for:" + groupType.toUpperCase() +"\n");

		MLBioTMLModelVSNejiUtils.evaluateAnnotation(goldAnnotations,annotations);

	}


	@Test
	public void evaluateourmodel() throws BioTMLException{
		//		evaluateOurModel(modelClassType);
		evaluateNejiAnnotations("FAMILY", "/home/tiagoalves/workspace/development/generalMain/output/neji/ABBREVIATIONtextFiles_1500.bc2");
	}


}