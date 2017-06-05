package test.com.silicolife.textmining.machineLearningTests;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import org.apache.commons.io.FileUtils;
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
import pt.ua.tm.neji.core.module.Reader;
import pt.ua.tm.neji.core.module.Writer;
import pt.ua.tm.neji.core.parser.Parser;
import pt.ua.tm.neji.core.parser.ParserLanguage;
import pt.ua.tm.neji.core.parser.ParserLevel;
import pt.ua.tm.neji.core.pipeline.Pipeline;
import pt.ua.tm.neji.exception.NejiException;
import pt.ua.tm.neji.ml.MLHybrid;
import pt.ua.tm.neji.ml.MLModel;
import pt.ua.tm.neji.nlp.NLP;
import pt.ua.tm.neji.parser.GDepParser;
import pt.ua.tm.neji.pipeline.DefaultPipeline;
import pt.ua.tm.neji.reader.RawReader;
import pt.ua.tm.neji.sentencesplitter.LingpipeSentenceSplitter;
import pt.ua.tm.neji.train.config.ModelConfig;
import pt.ua.tm.neji.train.model.CRFModel;
import pt.ua.tm.neji.train.nlp.TrainNLP;
import pt.ua.tm.neji.train.pipeline.TrainPipelinePhase1;
import pt.ua.tm.neji.train.pipeline.TrainPipelinePhase2;
import pt.ua.tm.neji.train.reader.BC2Reader;
import pt.ua.tm.neji.train.trainer.DefaultTrainer;
import pt.ua.tm.neji.writer.BC2Writer;

public class TestsOnBioTMAndNejiModels {

//	private String corpusDir= "src/test/resources/chemdner/devFiles";//"src/test/resources/chemdner/trainFile";
	private String modelClassType= "MULTIPLE";
//	private String modelDir="tests/ourModel/"+ modelClassType+".gz";
//	private String sentencesFile = corpusDir +"/textFiles_1500.txt";//text_1000.txt
//	private String annotationsFile = corpusDir +"annotations_1500.tsv";//"/train_1000.tsv";
	
	
//	private String corpusDir="src/test/resources/chemdner/trainFile";
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
	
	

	@Test
	public void createOurModel() throws BioTMLException{

		System.out.println("Loading the BioTMLCorpus...");

		BioTMLCorpusReaderImpl reader = new BioTMLCorpusReaderImpl();
		IBioTMLCorpus corpus = reader.readBioTMLCorpusFromBioCFiles(sentencesFile, annotationsFile, "nlp4j");

		IBioTMLModel svm = new MalletClassifierModel(MLBioTMLModelVSNejiUtils.loadfeatures(), MLBioTMLModelVSNejiUtils.defaultSVMConfiguration(modelClassType, BioTMLConstants.ner.toString()));

		svm.train(corpus);
		new File(modelDir).mkdirs();
		IBioTMLModelWriter writer = new BioTMLModelWriterImpl(modelDir);
		writer.writeGZModelFile(svm); 
		System.out.println("Model Creation finished!");		
	}

	//	@Test
	public void evaluateUsingAGZModel() throws BioTMLException{
		String corpusDir="src/test/resources/chemdner/trainFile";
		String modelDir="tests/ourModel/FAMILY.gz";

		System.out.println("Loading the BioTMLCorpus...");

		BioTMLCorpusReaderImpl reader = new BioTMLCorpusReaderImpl();
		IBioTMLCorpus corpus = reader.readBioTMLCorpusFromBioCFiles(corpusDir +"/text.txt", "nlp4j");
		//		IBioTMLCorpus corpus = reader.readBioTMLCorpusFromBioCFiles("/home/tiagoalves/workspace/development/src/test/resources/chemdner/dev/chemdner_patents_development_text.txt", "nlp4j");

		IBioTMLModelReader modelReader = new BioTMLModelReaderImpl();
		IBioTMLModel svm = modelReader.loadModelFromGZFile(modelDir);
		IBioTMLAnnotator annotator = new BioTMLMalletAnnotatorImpl(corpus);
		IBioTMLCorpus annotatedCorpus = annotator.generateAnnotatedBioTMCorpus(svm,8);
		List<IBioTMLEntity> annotationsTest = annotatedCorpus.getEntities();


		List<IBioTMLEntity> goldAnnotations = MLBioTMLModelVSNejiUtils.loadGoldAnnotationsFromTSVFile("src/test/resources/chemdner/train/evaluate200.tsv");


		MLBioTMLModelVSNejiUtils.evaluateAnnotation(goldAnnotations,annotationsTest);
	}

	//		@Test
	public void evaluateUsingNejiOutput() throws BioTMLException{
		List<IBioTMLEntity> goldAnnotations = MLBioTMLModelVSNejiUtils.loadGoldAnnotationsFromTSVFile("src/test/resources/chemdner/trainFile/train_1000.tsv");
		List<IBioTMLEntity> annotations = MLBioTMLModelVSNejiUtils.loadAnnotationsFromGroupedBC2File("/home/tiagoalves/workspace/nejiCode/tests/nejiOutput/groupBC2FileOf1000patentsBC2.bc2");
		MLBioTMLModelVSNejiUtils.evaluateAnnotation(goldAnnotations,annotations);
	}

	//@Test
	public void testConversionToBC2() throws IOException{
		MLBioTMLModelVSNejiUtils.convertBioCTextFilesIntoBC2TextFormat(sentencesFile, corpusDir + "/1000patentsBC2.txt");
	}

	//@Test
	public void trainNejiModel() throws NejiException, IOException{
		// Set files
		String sentencesFile = "/home/tiagoalves/workspace/development/src/test/resources/chemdner/trainFile/1000patentsBC2.txt";
		String annotationsFile = "/home/tiagoalves/workspace/development/src/test/resources/chemdner/trainFile/training_annotations";
		String modelConfigurationFile = "/home/tiagoalves/workspace/development/tests/nejiModel/trainFiles/configurationsTest.config";
		String modelFile = "/home/tiagoalves/workspace/development/tests/nejiModel/FAMILY.gz";

		// Create parser
		Parser parser = new GDepParser(ParserLanguage.ENGLISH, ParserLevel.CHUNKING, new LingpipeSentenceSplitter(), false).launch();

		// Set sentences and annotations streams
		InputStream sentencesStream = new FileInputStream(sentencesFile);
		InputStream annotationsStream = new FileInputStream(annotationsFile);

		// Run pipeline to get corpus from sentences and annotations
		Pipeline pipelinePhase1 = new TrainPipelinePhase1()
				.add(new BC2Reader(parser, null, annotationsStream))
				.add(new TrainNLP(parser));
		pipelinePhase1.run(sentencesStream);

		// Close sentences and annotations streams
		sentencesStream.close();
		annotationsStream.close();


		// Get corpus
		pt.ua.tm.neji.core.corpus.Corpus corpus = pipelinePhase1.getCorpus();

		// Get model configuration
		InputStream inputStream = new ByteArrayInputStream(modelConfigurationFile.getBytes("UTF-8"));
		ModelConfig modelConfig = new ModelConfig(modelConfigurationFile);

		// Run pipeline to train model on corpus
		Pipeline pipelinePhase2 = new TrainPipelinePhase2()
				.add(new DefaultTrainer(modelConfig));
		pipelinePhase2.setCorpus(corpus);
		pipelinePhase2.run(inputStream);

		// Close input stream
		inputStream.close();


		// Get trained model and write to file
		CRFModel model = (CRFModel) pipelinePhase2.getModuleData("TRAINED_MODEL").get(0);
		model.write(new FileOutputStream(modelFile));


	}


	//	@Test
	public void annotateUsingNejiModel() throws NejiException, IOException{
		// Set files
		String documentFile = "src/test/resources/chemdner/trainFile/1000patentsBC2.txt";
		String outputFile = "tests/nejiOutput/text.bc2";

		// Set resources
		//		String dictionary1File = "example/dictionaries/Body_Part_Organ_or_Organ_Component_T023_ANAT.tsv";
		//		String dictionary2File = "example/dictionaries/Disease_or_Syndrome_T047_DISO.tsv";
		String modelFile = "tests/nejiModel/model/model.properties";

		// Create reader
		Reader reader = new RawReader();

		// Create parser
		Parser parser = new GDepParser(ParserLanguage.ENGLISH, ParserLevel.CHUNKING, 
				new LingpipeSentenceSplitter(), false).launch();

		// Create NLP        
		NLP nlp = new NLP(parser);

		// Create dictionary matchers
		//		List<String> dictionary1Lines = FileUtils.readLines(new File(dictionary1File));
		//		Dictionary dictionary1 = VariantMatcherLoader.loadDictionaryFromLines(dictionary1Lines);
		//		List<String> dictionary2Lines = FileUtils.readLines(new File(dictionary2File));
		//		Dictionary dictionary2 = VariantMatcherLoader.loadDictionaryFromLines(dictionary2Lines);

		//		DictionaryHybrid dictionaryMatcher1 = new DictionaryHybrid(dictionary1);
		//		DictionaryHybrid dictionaryMatcher2 = new DictionaryHybrid(dictionary2);

		// Create machine-learning model matcher
		MLModel model = new MLModel("FAMILY", new File(modelFile));
		model.initialize();
		MLHybrid mlModelMatcher = new MLHybrid(model.getCrf(), "FAMILY");

		// Create Writer
		Writer writer = new BC2Writer();

		// Set document stream
		InputStream documentStream = new FileInputStream(documentFile);

		// Run pipeline to get annotations
		Pipeline pipeline = new DefaultPipeline()
				.add(reader)
				.add(nlp)
				//		        .add(dictionaryMatcher1)
				//		        .add(dictionaryMatcher2)
				.add(mlModelMatcher)
				.add(writer);

		OutputStream outputStream = pipeline.run(documentStream).get(0);

		// Write annotations to output file
		FileUtils.writeStringToFile(new File(outputFile), outputStream.toString());

		// Close streams
		documentStream.close();
		outputStream.close();

		// Close parser
		parser.close();


	}
	

	private IBioTMLCorpus loadCorpus(String documentfile, String annotationfile) throws BioTMLException {
		BioTMLCorpusReaderImpl reader = new BioTMLCorpusReaderImpl();
		if (annotationfile==null || annotationfile.isEmpty()){
			IBioTMLCorpus corpus = reader.readBioTMLCorpusFromBioCFiles(documentfile, "nlp4j");
			return corpus;
		}
		IBioTMLCorpus corpus = reader.readBioTMLCorpusFromBioCFiles(documentfile, annotationfile, "nlp4j");
		return corpus;
	}	
	
//		@Test
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