
package test.java.com.silicolife.textmining.machineLearningTests;


import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.junit.Test;

import com.silicolife.textmining.machinelearning.biotml.core.BioTMLConstants;
import com.silicolife.textmining.machinelearning.biotml.core.BioTMLModelLabelType;
import com.silicolife.textmining.machinelearning.biotml.core.corpora.BioTMLCorpusImpl;
import com.silicolife.textmining.machinelearning.biotml.core.evaluation.BioTMLEvaluator;
import com.silicolife.textmining.machinelearning.biotml.core.evaluation.datastrucures.BioTMLEvaluationImpl;
import com.silicolife.textmining.machinelearning.biotml.core.exception.BioTMLException;
import com.silicolife.textmining.machinelearning.biotml.core.features.BioTMLFeatureGeneratorConfiguratorImpl;
import com.silicolife.textmining.machinelearning.biotml.core.interfaces.IBioTMLConfusionMatrix;
import com.silicolife.textmining.machinelearning.biotml.core.interfaces.IBioTMLCorpus;
import com.silicolife.textmining.machinelearning.biotml.core.interfaces.IBioTMLCorpusReader;
import com.silicolife.textmining.machinelearning.biotml.core.interfaces.IBioTMLDocument;
import com.silicolife.textmining.machinelearning.biotml.core.interfaces.IBioTMLEntity;
import com.silicolife.textmining.machinelearning.biotml.core.interfaces.IBioTMLEvaluation;
import com.silicolife.textmining.machinelearning.biotml.core.interfaces.IBioTMLFeatureGeneratorConfigurator;
import com.silicolife.textmining.machinelearning.biotml.core.interfaces.IBioTMLModel;
import com.silicolife.textmining.machinelearning.biotml.core.interfaces.IBioTMLModelConfigurator;
import com.silicolife.textmining.machinelearning.biotml.core.interfaces.IBioTMLModelReader;
import com.silicolife.textmining.machinelearning.biotml.core.interfaces.IBioTMLModelWriter;
import com.silicolife.textmining.machinelearning.biotml.core.interfaces.IBioTMLToken;
import com.silicolife.textmining.machinelearning.biotml.core.mllibraries.BioTMLAlgorithm;
import com.silicolife.textmining.machinelearning.biotml.core.mllibraries.mallet.features.CorpusWithFeatures2TokenSequence;
import com.silicolife.textmining.machinelearning.biotml.core.mllibraries.mallet.multithread.BioTMLCorpusToNERInstancesThreadCreator;
import com.silicolife.textmining.machinelearning.biotml.core.mllibraries.mallet.multithread.InstanceListExtended;
import com.silicolife.textmining.machinelearning.biotml.core.models.BioTMLModelConfiguratorImpl;
import com.silicolife.textmining.machinelearning.biotml.core.models.mallet.BioTMLMalletClassifierModelImpl;
import com.silicolife.textmining.machinelearning.biotml.core.models.mallet.BioTMLMalletTransducerModelImpl;
import com.silicolife.textmining.machinelearning.biotml.reader.BioTMLCorpusReaderImpl;
import com.silicolife.textmining.machinelearning.biotml.reader.BioTMLModelReaderImpl;
import com.silicolife.textmining.machinelearning.biotml.writer.BioTMLModelWriterImpl;

import cc.mallet.pipe.Pipe;
import cc.mallet.pipe.SerialPipes;
import cc.mallet.pipe.TokenSequence2FeatureVectorSequence;
import cc.mallet.types.FeatureVector;
import cc.mallet.types.FeatureVectorSequence;
import cc.mallet.types.Instance;
import cc.mallet.types.Sequence;

public class BioCreativeTest {

	@Test
	public void test() throws BioTMLException {
		String classType = "FAMILY";
		String modelpath = "src/test/resources/biocreative/training_" + classType +".gz";

		//		String trainingDocs = "src/test/resources/biocreative/small_test.txt";
		//		String trainingAnnots = "src/test/resources/biocreative/small_test.tsv";	
		//		String trainingDocs = "src/test/resources/chemdner/train/chemdner_patents_train_text.txt";
		//		String trainingAnnots = "src/test/resources/chemdner/train/chemdner_cemp_gold_standard_train.tsv";
		String trainingDocs = "/home/tiagoalves/workspace/development/generalMain/train/trainSentences/FAMILYchemdner_patents_train_text.txt";
		String trainingAnnots = "/home/tiagoalves/workspace/development/generalMain/train/trainAnnotations/FAMILYchemdner_cemp_gold_standard_train.tsv";


		System.out.println("Read training corpus...");
		IBioTMLCorpus corpus = loadCorpus(trainingDocs, trainingAnnots);

		Set<String> strings = getEntitiesStrings(corpus, corpus.getEntities());

		System.out.println("Create/Read training model...");
		IBioTMLModel model = createModel(corpus, classType, BioTMLAlgorithm.malletcrf);
		//		writeModel(model, modelpath);
		//		IBioTMLModel model = readModel(modelpath);

		//		IBioTMLModelEvaluationConfigurator configuration = new BioTMLModelEvaluationConfiguratorImpl();
		//		configuration.setCrossValidationByCorpusDoc(10);
		//		IBioTMLMultiEvaluation eval1 = model.evaluate(corpus, configuration);
		//		System.out.println(eval1);


		IBioTMLEvaluation eval = evaluateModel(corpus, model);
		System.out.println(eval.getConfusionMatrix().toString());
		List<IBioTMLEntity> entities = (List<IBioTMLEntity>) eval.getConfusionMatrix().getFalsePositivesOfLabel(classType);
		Set<String> annotstrings = getEntitiesStrings(corpus, entities);

		System.out.println("evaluation scores for " + classType);
		System.out.println("Recall: " + eval.getRecallOfLabel(classType));
		System.out.println("Precison: " + eval.getPrecisionOfLabel(classType));
		System.out.println("F1 score: " + eval.getFscoreOfLabel(classType));
		System.out.println(strings.contains(annotstrings));
	}

	//	@Test
	public void test2() throws BioTMLException, IOException{
		String classType = "FAMILY";
		String trainingDocs = "src/test/resources/biocreative/chemdner_patents_train_text.txt";
		String trainingAnnots = "src/test/resources/biocreative/chemdner_cemp_gold_standard_train.tsv";
		String matrixFile = "src/test/resources/biocreative/chemdner_patents_train_text.train";

		System.out.println("Read corpus...");
		IBioTMLCorpus corpus = loadCorpus(trainingDocs, trainingAnnots);
		Set<String> featuresUIDs = new HashSet<>();
		featuresUIDs.add("WORD");
		IBioTMLFeatureGeneratorConfigurator featureConfiguration = new BioTMLFeatureGeneratorConfiguratorImpl(featuresUIDs);
		BioTMLCorpusToNERInstancesThreadCreator instancesCreator = new BioTMLCorpusToNERInstancesThreadCreator(corpus, classType, BioTMLModelLabelType.bio);
		ExecutorService executor = Executors.newFixedThreadPool(4);
		ArrayList<Pipe> pipe = new ArrayList<Pipe>();
		pipe.add(new CorpusWithFeatures2TokenSequence());
		pipe.add(new TokenSequence2FeatureVectorSequence(true, true));
		InstanceListExtended instances = new InstanceListExtended(new SerialPipes(pipe));
		instancesCreator.insertInstancesIntoExecutor(executor, featureConfiguration, instances);
		executor.shutdown();
		try {
			executor.awaitTermination(Long.MAX_VALUE, TimeUnit.MINUTES);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		FileWriter fw = new FileWriter(matrixFile);
		for(Instance instance : instances){
			List<IBioTMLToken> sentenceTokens = (List<IBioTMLToken>)instance.getName();
			FeatureVectorSequence data = (FeatureVectorSequence)instance.getData();
			Sequence target = (Sequence)instance.getTarget();
			for(int i = 0; i< sentenceTokens.size(); i++){
				StringBuilder sb = new StringBuilder();
				sb.append(sentenceTokens.get(i).getToken());
				FeatureVector features = data.get(i);
				sb.append("\t");
				sb.append(features.toString().trim());
				sb.append("\t");
				sb.append(target.get(i).toString().trim());
				sb.append("\n");
				fw.write(sb.toString());
			}
			fw.write("\n");
		}
		fw.close();
	}


	private IBioTMLCorpus loadCorpus(String docPath, String annotPath) throws BioTMLException{
		IBioTMLCorpusReader reader = new BioTMLCorpusReaderImpl();
		return reader.readBioTMLCorpusFromBioCFiles(docPath, annotPath, "nlp4j");
	}

	private IBioTMLModel createModel(IBioTMLCorpus corpus, String classType, BioTMLAlgorithm modelAlgorithm) throws BioTMLException{
		IBioTMLModelConfigurator modelConfiguration = new BioTMLModelConfiguratorImpl(classType, BioTMLConstants.ner.toString());
		modelConfiguration.getTransducerConfiguration().setForbiddenTransitionStates(null);
		//		modelConfiguration.getTransducerConfiguration().setAllowedTransitionStates(null);

		//		modelConfiguration.setModelOrder(2);
		//		Set<String> featuresUIDs = BioTMLFeaturesManager.getInstance().getNERRecomendedFeatures();
		Set<String> featuresUIDs = new HashSet<>();
		featuresUIDs.add("WORD");
		//		featuresUIDs.add("NLP4JPOS");
		//		featuresUIDs.add("OPENNLPCHUNK");
		//		Set<String> featuresUIDs = getFeatures();
		IBioTMLFeatureGeneratorConfigurator featureConfiguration = new BioTMLFeatureGeneratorConfiguratorImpl(featuresUIDs);
		if(modelAlgorithm.equals(BioTMLAlgorithm.malletsvm)){
			modelConfiguration.setAlgorithmType(BioTMLAlgorithm.malletsvm);
			IBioTMLModel model = new BioTMLMalletClassifierModelImpl(featureConfiguration, modelConfiguration);
			model.train(corpus);
			return model;
		}
		else if(modelAlgorithm.equals(BioTMLAlgorithm.malletcrf)){
			modelConfiguration.setAlgorithmType(BioTMLAlgorithm.malletcrf);
			IBioTMLModel model = new BioTMLMalletTransducerModelImpl(featureConfiguration, modelConfiguration);
			model.train(corpus);
			return model;
		}
		System.err.println("Failed to train model!");
		return null;
	}

	private void writeModel(IBioTMLModel model, String path) throws BioTMLException{
		IBioTMLModelWriter writer = new BioTMLModelWriterImpl(path);
		writer.writeGZModelFile(model);
	}

	private IBioTMLModel readModel(String path) throws BioTMLException{
		IBioTMLModelReader reader = new BioTMLModelReaderImpl();
		return reader.loadModelFromGZFile(path);
	}

	private IBioTMLEvaluation evaluateModel(IBioTMLCorpus goldCorpus, IBioTMLModel model) throws BioTMLException{
		IBioTMLCorpus corpus = new BioTMLCorpusImpl(goldCorpus.getDocuments(), "prediction Corpus");
		corpus = model.predict(corpus);
		BioTMLEvaluator<IBioTMLEntity> evaluator = new BioTMLEvaluator<>();
		IBioTMLConfusionMatrix<IBioTMLEntity> confusionMatrix = evaluator.generateConfusionMatrix(goldCorpus.getEntities(), corpus.getEntities());
		return new BioTMLEvaluationImpl(confusionMatrix);
	}



	private Set<String> getFeatures(){
		Set<String> features = new HashSet<>();
		features.add("STANFORDNLPPOS");
		features.add("NOCAPS");
		features.add("PERCENT");
		features.add("OPENPARENT");
		features.add("NUMCAPS");
		features.add("2SUFFIX");
		features.add("OPENBRACKET");
		features.add("ELEMENTNAMES");
		features.add("SEMICOLON");
		features.add("MIXCAPS");
		features.add("OPENNLPCHUNK");
		features.add("LENGTH");
		features.add("ASTERISK");
		features.add("NLP4JLEMMA");
		features.add("OPENNLPPOS");
		features.add("SYMBOLNUMCHAR");
		features.add("COLON");
		features.add("CLOSEBRACKET");
		features.add("WORD");
		features.add("CLOSEPARENT");
		features.add("PORTERSTEM");
		features.add("CHARNGRAM");
		features.add("INITCAPS");
		features.add("HYPHEN");
		features.add("COMMA");
		features.add("MORPHOLOGYTYPEIII");
		features.add("WINDOWOPENNLPCHUNK");
		features.add("QUOTATIONMARK");
		features.add("EQUAL");
		features.add("WINDOWSTANFORDNLPLEMMA");
		features.add("4SUFFIX");
		features.add("ROMANNUM");
		features.add("PLUS");
		features.add("ENDCAPS");
		features.add("NUMDIGITS");
		features.add("4PREFIX");
		features.add("GREEKSYMB");
		features.add("APOSTROPHE");
		features.add("STANFORDNLPLEMMA");
		features.add("DOT");
		features.add("NLP4JPOS");
		features.add("ALLCAPS");
		features.add("BACKSLASH");
		return features;
	}

	private Set<String> getEntitiesStrings(IBioTMLCorpus corpus, List<IBioTMLEntity> entities) throws BioTMLException{
		Set<String> entitie = new HashSet<>();
		for(IBioTMLEntity entity : entities){
			IBioTMLDocument doc = corpus.getDocumentByID(entity.getDocID());
			entitie.add(doc.toString().substring((int)entity.getStartOffset(), (int)entity.getEndOffset()));
		}
		return entitie;
	}

}
