package test.com.silicolife.textmining.machineLearningTests;

import java.io.File;
import java.util.List;

import org.junit.Test;

import com.silicolife.textmining.machinelearning.biotml.core.BioTMLConstants;
import com.silicolife.textmining.machinelearning.biotml.core.annotator.BioTMLMalletAnnotatorImpl;
import com.silicolife.textmining.machinelearning.biotml.core.corpora.BioTMLCorpusImpl;
import com.silicolife.textmining.machinelearning.biotml.core.evaluation.BioTMLEvaluator;
import com.silicolife.textmining.machinelearning.biotml.core.evaluation.datastrucures.BioTMLEvaluationImpl;
import com.silicolife.textmining.machinelearning.biotml.core.exception.BioTMLException;
import com.silicolife.textmining.machinelearning.biotml.core.interfaces.IBioTMLAnnotator;
import com.silicolife.textmining.machinelearning.biotml.core.interfaces.IBioTMLConfusionMatrix;
import com.silicolife.textmining.machinelearning.biotml.core.interfaces.IBioTMLCorpus;
import com.silicolife.textmining.machinelearning.biotml.core.interfaces.IBioTMLEntity;
import com.silicolife.textmining.machinelearning.biotml.core.interfaces.IBioTMLEvaluation;
import com.silicolife.textmining.machinelearning.biotml.core.interfaces.IBioTMLModel;
import com.silicolife.textmining.machinelearning.biotml.core.interfaces.IBioTMLModelReader;
import com.silicolife.textmining.machinelearning.biotml.core.interfaces.IBioTMLModelWriter;
import com.silicolife.textmining.machinelearning.biotml.core.models.mallet.MalletClassifierModel;
import com.silicolife.textmining.machinelearning.biotml.core.models.mallet.MalletTransducerModel;
import com.silicolife.textmining.machinelearning.biotml.reader.BioTMLCorpusReaderImpl;
import com.silicolife.textmining.machinelearning.biotml.reader.BioTMLModelReaderImpl;
import com.silicolife.textmining.machinelearning.biotml.writer.BioTMLModelWriterImpl;

import main.com.silicolife.textmining.machineLearningMains.MLBioTMLModelVSNejiUtils;

public class ExecuteBioTMLTest {
	
	private IBioTMLCorpus loadCorpus(String documentfile, String annotationfile) throws BioTMLException {
		BioTMLCorpusReaderImpl reader = new BioTMLCorpusReaderImpl();
		if (annotationfile==null || annotationfile.isEmpty()){
			IBioTMLCorpus corpus = reader.readBioTMLCorpusFromBioCFiles(documentfile, "nlp4j");
			return corpus;
		}
		IBioTMLCorpus corpus = reader.readBioTMLCorpusFromBioCFiles(documentfile, annotationfile, "nlp4j");
		return corpus;
	}
	
	private IBioTMLModel createCRFModel(IBioTMLCorpus corpus, String dirToSave, String modelClassType) throws BioTMLException{
		IBioTMLModel crf = new MalletTransducerModel(MLBioTMLModelVSNejiUtils.loadfeatures(), MLBioTMLModelVSNejiUtils.defaultCRFConfiguration(modelClassType, BioTMLConstants.ner.toString()));
		crf.train(corpus);
		if (!new File(new File(dirToSave).getParent()).exists()){
			new File(new File(dirToSave).getParent()).mkdirs();
		}
		IBioTMLModelWriter writer = new BioTMLModelWriterImpl(dirToSave);
		writer.writeGZModelFile(crf); 
		readModel(dirToSave);
		System.out.println("Model Creation finished!");
		return crf;
	}
	
	private IBioTMLModel createSVMModel(IBioTMLCorpus corpus, String dirToSave, String modelClassType) throws BioTMLException{
		@SuppressWarnings("static-access")
		IBioTMLModel svm = new MalletClassifierModel(MLBioTMLModelVSNejiUtils.loadfeatures(), MLBioTMLModelVSNejiUtils.defaultSVMConfiguration(modelClassType, BioTMLConstants.ner.toString()));
		svm.train(corpus);
		if (!new File(new File(dirToSave).getParent()).exists()){
			new File(new File(dirToSave).getParent()).mkdirs();
		}
		IBioTMLModelWriter writer = new BioTMLModelWriterImpl(dirToSave);
		writer.writeGZModelFile(svm); 
		readModel(dirToSave);
		System.out.println("Model Creation finished!");
		return svm;
	}
	
	private IBioTMLModel readModel(String modelDir) throws BioTMLException{
		IBioTMLModelReader read = new BioTMLModelReaderImpl();
		IBioTMLModel model = read.loadModelFromGZFile(modelDir);
		return model;
	}
	
	public void evaluateOurModel(IBioTMLCorpus goldCorpus, IBioTMLModel model) throws BioTMLException{
		IBioTMLCorpus predictedCorpus = new BioTMLCorpusImpl(goldCorpus.getDocuments(), "predicted");
		IBioTMLAnnotator annotator = new BioTMLMalletAnnotatorImpl(predictedCorpus);
		IBioTMLCorpus annotatedCorpus = annotator.generateAnnotatedBioTMCorpus(model,8);
		List<IBioTMLEntity> predictedAnnots = annotatedCorpus.getEntities();
		System.out.println(predictedAnnots);
		evaluateAnnotation(goldCorpus.getEntities(), predictedAnnots);
	}

	
	public void evaluateAnnotation(List<IBioTMLEntity> goldAnnotations, List<IBioTMLEntity> toCompareAnnotations){
		BioTMLEvaluator<IBioTMLEntity> annotationsEvaluator = new BioTMLEvaluator<>();
		IBioTMLConfusionMatrix<IBioTMLEntity> confusionMatrix = annotationsEvaluator.generateConfusionMatrix(goldAnnotations, toCompareAnnotations);
		IBioTMLEvaluation eval = new BioTMLEvaluationImpl(confusionMatrix);
		List<String> labels = eval.getConfusionMatrix().getLabels();
		for (String label:labels){
			System.out.println(label);
			System.out.println("Precision: " + eval.getPrecisionOfLabel(label));
			System.out.println("Recall: " + eval.getRecallOfLabel(label));
			System.out.println("F1: " + eval.getFscoreOfLabel(label));

		}
	}
	
	
	@Test
	public void main() throws BioTMLException {
		String trainingDocumentsFile = "src/test/resources/chemdner/train/chemdner_patents_train_text.txt";
		String trainingAnnotationsFile = "src/test/resources/chemdner/train/chemdner_cemp_gold_standard_train.tsv";
		String devDocumentsFile = "src/test/resources/chemdner/dev/chemdner_patents_development_text.txt";
		String devAnnotationsFile = "src/test/resources/chemdner/dev/chemdner_cemp_gold_standard_development_v03.tsv";
		String modelClassType= "FAMILY";
		String modelDir = "generalMain/OurModel/"+ modelClassType+".gz";
		
		System.out.println("Loading the training BioTMLCorpus...");
		IBioTMLCorpus trainingCorpus = loadCorpus(trainingDocumentsFile, trainingAnnotationsFile);
		IBioTMLModel model = createCRFModel(trainingCorpus, modelDir, modelClassType);
//		IBioTMLModel model = createSVMModel(trainingCorpus, modelDir, modelClassType);
//		IBioTMLModel model = readModel(modelDir);
		evaluateOurModel(trainingCorpus, model);
		
		System.out.println("Loading the development BioTMLCorpus...");
		IBioTMLCorpus devCorpus = loadCorpus(devDocumentsFile, devAnnotationsFile);
		evaluateOurModel(devCorpus, model);
	}

}
