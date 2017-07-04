package test.java.com.silicolife.textmining.machineLearningTests;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.junit.Test;

import com.silicolife.textmining.machinelearning.biotml.core.BioTMLConstants;
import com.silicolife.textmining.machinelearning.biotml.core.annotator.BioTMLMalletAnnotatorImpl;
import com.silicolife.textmining.machinelearning.biotml.core.corpora.BioTMLCorpusImpl;
import com.silicolife.textmining.machinelearning.biotml.core.exception.BioTMLException;
import com.silicolife.textmining.machinelearning.biotml.core.interfaces.IBioTMLAnnotator;
import com.silicolife.textmining.machinelearning.biotml.core.interfaces.IBioTMLCorpus;
import com.silicolife.textmining.machinelearning.biotml.core.interfaces.IBioTMLEntity;
import com.silicolife.textmining.machinelearning.biotml.core.interfaces.IBioTMLModel;
import com.silicolife.textmining.machinelearning.biotml.core.interfaces.IBioTMLModelReader;
import com.silicolife.textmining.machinelearning.biotml.core.interfaces.IBioTMLModelWriter;
import com.silicolife.textmining.machinelearning.biotml.core.models.mallet.BioTMLMalletTransducerModelImpl;
import com.silicolife.textmining.machinelearning.biotml.reader.BioTMLCorpusReaderImpl;
import com.silicolife.textmining.machinelearning.biotml.reader.BioTMLModelReaderImpl;
import com.silicolife.textmining.machinelearning.biotml.writer.BioTMLModelWriterImpl;

import main.java.com.silicolife.textmining.machineLearningMains.MLBioTMLModelVSNejiUtils;

public class ExecuteBioTMLTest {

	private IBioTMLCorpus loadCorpus(String documentfile, String annotationfile, String modelClassType) throws BioTMLException {
		BioTMLCorpusReaderImpl reader = new BioTMLCorpusReaderImpl();
		if (annotationfile==null || annotationfile.isEmpty()){
			IBioTMLCorpus corpus = reader.readBioTMLCorpusFromBioCFiles(documentfile,"nlp4j");
			return corpus;
		}
		IBioTMLCorpus corpus = reader.readBioTMLCorpusFromBioCFiles(documentfile, annotationfile, modelClassType, "nlp4j");
		return corpus;
	}

	private IBioTMLModel createCRFModel(IBioTMLCorpus corpus, String dirToSave, String modelClassType) throws BioTMLException{
		IBioTMLModel crf = new BioTMLMalletTransducerModelImpl(MLBioTMLModelVSNejiUtils.loadWordFeaturesForTest(), MLBioTMLModelVSNejiUtils.defaultCRFConfiguration(modelClassType, BioTMLConstants.ner.toString()));
//		IBioTMLModel crf = new BioTMLMalletTransducerModelImpl(MLBioTMLModelVSNejiUtils.loadfeatures(), MLBioTMLModelVSNejiUtils.defaultCRFConfiguration(modelClassType, BioTMLConstants.ner.toString()));
		crf.train(corpus);
		if (!new File(new File(dirToSave).getParent()).exists()){
			new File(new File(dirToSave).getParent()).mkdirs();
		}
		IBioTMLModelWriter writer = new BioTMLModelWriterImpl(dirToSave);
		writer.writeGZModelFile(crf); 
		crf=readModel(dirToSave);
		System.out.println("Model Creation finished!");
		return crf;
	}
//	Precision: 0.6014947284131856
//	Recall: 0.369153902858547
//	F1: 0.4575170033499137

	private IBioTMLModel createSVMModel(IBioTMLCorpus corpus, String dirToSave, String modelClassType) throws BioTMLException{
		IBioTMLModel svm = new BioTMLMalletTransducerModelImpl(MLBioTMLModelVSNejiUtils.loadWordFeaturesForTest(), MLBioTMLModelVSNejiUtils.defaultSVMConfiguration(modelClassType, BioTMLConstants.ner.toString()));
		svm.train(corpus);
		if (!new File(new File(dirToSave).getParent()).exists()){
			new File(new File(dirToSave).getParent()).mkdirs();
		}
		IBioTMLModelWriter writer = new BioTMLModelWriterImpl(dirToSave);
		writer.writeGZModelFile(svm); 
		svm=readModel(dirToSave);
		System.out.println("Model Creation finished!");
		return svm;
	}

	private IBioTMLModel readModel(String modelDir) throws BioTMLException{
		IBioTMLModelReader read = new BioTMLModelReaderImpl();
		IBioTMLModel model = read.loadModelFromGZFile(modelDir);
		return model;
	}

	public void evaluateOurModel(IBioTMLCorpus goldCorpus, IBioTMLModel model, String comparationFilePath) throws BioTMLException, IOException{
		IBioTMLCorpus predictedCorpus = new BioTMLCorpusImpl(goldCorpus.getDocuments(), "predicted");
		IBioTMLAnnotator annotator = new BioTMLMalletAnnotatorImpl(predictedCorpus);
		IBioTMLCorpus annotatedCorpus = annotator.generateAnnotatedBioTMCorpus(model,8);
		List<IBioTMLEntity> predictedAnnots = annotatedCorpus.getEntities();
		System.out.println(predictedAnnots);
		MLBioTMLModelVSNejiUtils.evaluateAnnotation(goldCorpus.getEntities(), predictedAnnots);
		MLBioTMLModelVSNejiUtils.createFileWithAnnotations(goldCorpus.getEntities(), predictedAnnots,comparationFilePath);
	}

	@Test
	public void main() throws BioTMLException, IOException {
				String trainingDocumentsFile = "src/test/resources/chemdner/train/chemdner_patents_train_text.txt";
				String trainingAnnotationsFile = "src/test/resources/chemdner/train/chemdner_cemp_gold_standard_train.tsv";
//				String devDocumentsFile = "src/test/resources/chemdner/dev/chemdner_patents_development_text.txt";
//				String devAnnotationsFile = "src/test/resources/chemdner/dev/chemdner_cemp_gold_standard_development_v03.tsv";

//		String trainingDocumentsFile = "/home/tiagoalves/workspace/development/generalMain/train/trainSentences/FAMILYchemdner_patents_train_text.txt";
//		String trainingAnnotationsFile = "/home/tiagoalves/workspace/development/generalMain/train/trainAnnotations/FAMILYchemdner_cemp_gold_standard_train.tsv";
		String devDocumentsFile ="/home/tiagoalves/workspace/development/generalMain/development/developmentSentences/FAMILYchemdner_patents_development_text.txt";
		String devAnnotationsFile = "/home/tiagoalves/workspace/development/generalMain/development/developmentAnnotations/FAMILYchemdner_cemp_gold_standard_development_v03.tsv";
		
		
		
		String testModelOnTrainSetFile="testModelOnTrainSetFile.txt";
		String testModelOnTestSetFile="testModelOnTestSetFile.txt";
		//				

		String modelClassType= "FAMILY";
		String modelDir = "generalMain/OurModel/"+ modelClassType+"7000_family_CRF.gz";

		System.out.println("Loading the training BioTMLCorpus...");
		IBioTMLCorpus trainingCorpus = loadCorpus(trainingDocumentsFile, trainingAnnotationsFile, modelClassType);
		IBioTMLModel model = createCRFModel(trainingCorpus, modelDir, modelClassType);
//				IBioTMLModel model = createSVMModel(trainingCorpus, modelDir, modelClassType);
//				IBioTMLModel model = readModel(modelDir);
		evaluateOurModel(trainingCorpus, model, testModelOnTrainSetFile);

		System.out.println("Loading the development BioTMLCorpus...");
		IBioTMLCorpus devCorpus = loadCorpus(devDocumentsFile, devAnnotationsFile, modelClassType);
		evaluateOurModel(devCorpus, model, testModelOnTestSetFile);
	}
}
