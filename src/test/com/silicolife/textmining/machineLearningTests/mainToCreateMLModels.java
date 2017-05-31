package test.com.silicolife.textmining.machineLearningTests;

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

import main.com.silicolife.textmining.machineLearningMains.LoadBiocIntoAnoteTest;

public class mainToCreateMLModels {

	private String corpusDir="src/test/resources/chemdner/trainFile";
	private String modelClassType= "MULTIPLE";
	private String modelDir="tests/ourModel/models/"+ modelClassType+".gz";
	private String sentencesFile = corpusDir +"/text_1000.txt";
	private String corpusSentencesFile="";
	private String annotationsFile = corpusDir +"/train_1000.tsv";
	private String nejiDir="generalMain/NejiCode";
	private String nejiSentencesFile=nejiDir + "/sentences/BC2_text_1000.txt";
	private String nejiAnnotationsDir=nejiDir+"/annotations";
	private String goldAnnotationsFile="src/test/resources/chemdner/train/evaluate200.tsv";



	public void createOURModel (String modelClassType, String modelDir) throws BioTMLException{
		LoadBiocIntoAnoteTest classTest=new LoadBiocIntoAnoteTest();

		System.out.println("Loading the BioTMLCorpus...");

		BioTMLCorpusReaderImpl reader = new BioTMLCorpusReaderImpl();
		IBioTMLCorpus corpus = reader.readBioTMLCorpusFromBioCFiles(sentencesFile, annotationsFile, "nlp4j");

		@SuppressWarnings("static-access")
		IBioTMLModel svm = new MalletClassifierModel(classTest.loadfeatures(), classTest.defaultConfiguration(modelClassType, BioTMLConstants.ner.toString()));

		svm.train(corpus);
		classTest.createDirectories (modelDir);
		IBioTMLModelWriter writer = new BioTMLModelWriterImpl(modelDir);
		writer.writeGZModelFile(svm); 
		System.out.println("Model Creation finished!");		
	}


	public List<IBioTMLEntity> testWithOurModel(String modelFilePath, String corpusFilePath) throws BioTMLException{
		//		String corpusDir="src/test/resources/chemdner/trainFile";
		//		String modelDir="testeModelos/modeloML/modeloCriado.gz";

		System.out.println("Loading the BioTMLCorpus...");

		BioTMLCorpusReaderImpl reader = new BioTMLCorpusReaderImpl();
		IBioTMLCorpus corpus = reader.readBioTMLCorpusFromBioCFiles(corpusFilePath, "nlp4j");


		IBioTMLModelReader modelReader = new BioTMLModelReaderImpl();
		IBioTMLModel svm = modelReader.loadModelFromGZFile(modelFilePath);
		IBioTMLAnnotator annotator = new BioTMLMalletAnnotatorImpl(corpus);
		IBioTMLCorpus annotatedCorpus = annotator.generateAnnotatedBioTMCorpus(svm,8);
		List<IBioTMLEntity> annotationsTest = annotatedCorpus.getEntities();
		//		createAnotatedModelFile(annotatedCorpus, "testeModelos/Corpus/CorpusAnotado.gz");


		//		List<IBioTMLEntity> goldAnnotations = getGoldAnnotations("src/test/resources/chemdner/train/evaluate200.tsv");
		//
		//
		//
		//		evaluateAnnotation(goldAnnotations,annotationsTest);


		//		System.out.println(annotationsTest.get(0).toString());
		//		System.out.println(annotationsTest);
		return annotationsTest;

	}
@Test
	public void createConditionsToRunNeji() throws IOException{
		LoadBiocIntoAnoteTest classTest=new LoadBiocIntoAnoteTest();
//		classTest.convertBioCreativeTExtFilesIntoBC2(sentencesFile, nejiSentencesFile);
		
//		classTest.convertBiocreativeFilesIntoEachGroupFiles(annotationsFile,nejiAnnotationsDir);	
//		classTest.convertFromBioCreativeIntoBC2UsingAFolder(nejiAnnotationsDir, nejiAnnotationsDir+"/bc2Files");
		classTest.convertFromBioCreativeTEXTFILESIntoBC2UsingAFolder("/home/tiagoalves/workspace/development/generalMain/sentencesGrouped", "/home/tiagoalves/workspace/development/generalMain/sentencesGrouped/bc2Sentences");
	}

	public void evaluateNejiAnnotations(String groupType, String nejiAnnotationsDir) throws BioTMLException{
		LoadBiocIntoAnoteTest classTest=new LoadBiocIntoAnoteTest();

		List<IBioTMLEntity> annotations = classTest.getAnnotationsFromBC2AnnotationsFolder(nejiAnnotationsDir, groupType);

		List<IBioTMLEntity> goldAnnotations = classTest.getGoldAnnotations(goldAnnotationsFile);

		System.out.println("Results for:" + groupType.toUpperCase() +"\n");

		classTest.evaluateAnnotation(goldAnnotations,annotations);

	}


	public void evaluateOurModel(String groupType) throws BioTMLException{
		LoadBiocIntoAnoteTest classTest=new LoadBiocIntoAnoteTest();

		List<IBioTMLEntity> annotations = testWithOurModel(modelDir, corpusSentencesFile);

		List<IBioTMLEntity> goldAnnotations = classTest.getGoldAnnotations(goldAnnotationsFile);

		System.out.println("Results for:" + groupType.toUpperCase() +"\n");

		classTest.evaluateAnnotation(goldAnnotations,annotations);

	}

}