package test.com.silicolife.textmining.machineLearningTests;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
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

public class ComparationsBetweenOURandNEJI {

	private String corpusDir="src/test/resources/chemdner/trainFile";
	private String modelClassType= "FAMILY";
	private String modelDir="tests/ourModel/"+ modelClassType+".gz";
	private String sentencesFile = corpusDir +"/text.txt";
	private String annotationsFile = corpusDir +"/annotations.tsv";

	//	@Test
	public void createOurModel() throws BioTMLException{
		LoadBiocIntoAnoteTest classTest=new LoadBiocIntoAnoteTest();



		System.out.println("Loading the BioTMLCorpus...");

		BioTMLCorpusReaderImpl reader = new BioTMLCorpusReaderImpl();
		IBioTMLCorpus corpus = reader.readBioTMLCorpusFromBioCFiles(sentencesFile, annotationsFile, "nlp4j");

		@SuppressWarnings("static-access")
		IBioTMLModel svm = new MalletClassifierModel(classTest.loadfeatures(), classTest.defaultConfiguration("FAMILY", BioTMLConstants.ner.toString()));

		svm.train(corpus);
		createDirectories (modelDir);
		IBioTMLModelWriter writer = new BioTMLModelWriterImpl(modelDir);
		writer.writeGZModelFile(svm); 
		System.out.println("Model Creation finished!");		
	}


	private void createDirectories (String filename){
		if (!new File(new File(filename).getParent()).exists()){
			new File(new File(filename).getParent()).mkdir();
		}
	}



	//	@Test
	public void createNejiModel() throws IOException, InterruptedException{
		String[] args = new String[] {"bash", "-c", "./workspace/nejiCode/nejiTrain.sh "
				+ "-a ./workspace/development/"+annotationsFile
				+ "-c ./workspace/development/"+sentencesFile
				//				+ "-d completeTest/dictionaries "
				+ "-f ./workspace/development/"+"tests/nejiModel/trainFiles/configurationsTest.config"
				+ "-if BC2 "
				+ "-m "+modelClassType
				+ "-o tests/nejiModel/"
				//				+ "-s completeTest/a "
				+ "-t 8"};
		Process proc = new ProcessBuilder(args).start();
		//		Process proc = Runtime.getRuntime().exec(args);

		// Read the output

		BufferedReader reader =  new BufferedReader(new InputStreamReader(proc.getInputStream()));

		String line = "";
		while((line = reader.readLine()) != null) {
			System.out.print(line + "\n");
		}

		proc.waitFor();   

	}
	//	 ./workspace/nejiCode/nejiTrain.sh -a ./workspace/development/src/test/resources/chemdner/trainFile/annotations.tsv -c ./workspace/development/src/test/resources/chemdner/trainFile/text.txt -f ./workspace/development/tests/nejiModel/trainFiles/configurationsTest.config -if BC2 -m FAMILY -o ./workspace/development/tests/nejiModel -t 8


//	@Test
	public void evaluateUsingAGZModel() throws BioTMLException{
		LoadBiocIntoAnoteTest classTest=new LoadBiocIntoAnoteTest();
		String corpusDir="src/test/resources/chemdner/trainFile";
		String modelDir="tests/nejiModel/FAMILY.gz";

		System.out.println("Loading the BioTMLCorpus...");

		BioTMLCorpusReaderImpl reader = new BioTMLCorpusReaderImpl();
		IBioTMLCorpus corpus = reader.readBioTMLCorpusFromBioCFiles(corpusDir +"/text.txt", "nlp4j");


		IBioTMLModelReader modelReader = new BioTMLModelReaderImpl();
		IBioTMLModel svm = modelReader.loadModelFromGZFile(modelDir);
		IBioTMLAnnotator annotator = new BioTMLMalletAnnotatorImpl(corpus);
		IBioTMLCorpus annotatedCorpus = annotator.generateAnnotatedBioTMCorpus(svm,8);
		List<IBioTMLEntity> annotationsTest = annotatedCorpus.getEntities();
		//		createAnotatedModelFile(annotatedCorpus, "testeModelos/Corpus/CorpusAnotado.gz");


		List<IBioTMLEntity> goldAnnotations = classTest.getGoldAnnotations("src/test/resources/chemdner/train/evaluate200.tsv");



		classTest.evaluateAnnotation(goldAnnotations,annotationsTest);


		//		System.out.println(annotationsTest.get(0).toString());
		//		System.out.println(annotationsTest);

	}

	@Test
	public void evaluateUsingNejiOutput() throws BioTMLException{
		LoadBiocIntoAnoteTest classTest=new LoadBiocIntoAnoteTest();
		List<IBioTMLEntity> goldAnnotations = classTest.getGoldAnnotations("src/test/resources/chemdner/train/evaluate200.tsv");
		List<IBioTMLEntity> annotations = classTest.getAnnotationsFromBC2File("/home/tiagoalves/workspace/development/src/test/resources/chemdner/trainFile/annotations_bc2");
		classTest.evaluateAnnotation(goldAnnotations,annotations);
	}
	
	
}





