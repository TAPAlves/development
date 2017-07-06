package main.java.com.silicolife.textmining.machineLearningMains;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import com.silicolife.textmining.core.datastructures.utils.FileHandling;
import com.silicolife.textmining.core.datastructures.utils.GenerateRandomId;
import com.silicolife.textmining.machinelearning.biotml.core.BioTMLModelLabelType;
import com.silicolife.textmining.machinelearning.biotml.core.corpora.BioTMLDocumentImpl;
import com.silicolife.textmining.machinelearning.biotml.core.corpora.BioTMLEntityImpl;
import com.silicolife.textmining.machinelearning.biotml.core.evaluation.BioTMLEvaluator;
import com.silicolife.textmining.machinelearning.biotml.core.evaluation.datastrucures.BioTMLEvaluationImpl;
import com.silicolife.textmining.machinelearning.biotml.core.evaluation.datastrucures.BioTMLModelEvaluationConfiguratorImpl;
import com.silicolife.textmining.machinelearning.biotml.core.exception.BioTMLException;
import com.silicolife.textmining.machinelearning.biotml.core.features.BioTMLFeatureGeneratorConfiguratorImpl;
import com.silicolife.textmining.machinelearning.biotml.core.features.modules.NLP4JFeatures;
import com.silicolife.textmining.machinelearning.biotml.core.interfaces.IBioTMLConfusionMatrix;
import com.silicolife.textmining.machinelearning.biotml.core.interfaces.IBioTMLDocument;
import com.silicolife.textmining.machinelearning.biotml.core.interfaces.IBioTMLEntity;
import com.silicolife.textmining.machinelearning.biotml.core.interfaces.IBioTMLEvaluation;
import com.silicolife.textmining.machinelearning.biotml.core.interfaces.IBioTMLModelConfigurator;
import com.silicolife.textmining.machinelearning.biotml.core.interfaces.IBioTMLModelEvaluationConfigurator;
import com.silicolife.textmining.machinelearning.biotml.core.mllibraries.BioTMLAlgorithm;
import com.silicolife.textmining.machinelearning.biotml.core.models.BioTMLModelConfiguratorImpl;
import com.silicolife.textmining.machinelearning.biotml.core.nlp.nlp4j.BioTMLNLP4J;

public class MLBioTMLModelVSNejiUtils {

	// --------------------------CONFIGURATIONS SECTION --------------------------//

	public static IBioTMLModelConfigurator defaultSVMConfiguration(String modelClassType, String modelIEType){
		BioTMLModelConfiguratorImpl configuration = new BioTMLModelConfiguratorImpl(modelClassType, modelIEType);
		configuration.setAlgorithmType(BioTMLAlgorithm.malletsvm);
		configuration.setNumThreads(5);
		return configuration;
	}


	public static IBioTMLModelConfigurator defaultCRFConfiguration(String modelClassType, String modelIEType) {
		BioTMLModelConfiguratorImpl configuration = new BioTMLModelConfiguratorImpl(modelClassType, modelIEType);
		configuration.setAlgorithmType(BioTMLAlgorithm.malletcrf);
		configuration.setNumThreads(8);
//		configuration.getTransducerConfiguration().setAllowedTransitionStates(null);
		configuration.getTransducerConfiguration().setForbiddenTransitionStates(null);
		configuration.getTransducerConfiguration().setModelOrder(1);
//		configuration.setModelLabelType(BioTMLModelLabelType.bio);
		return configuration;
	}


	public static IBioTMLModelEvaluationConfigurator defaultEvaluationConfiguration(){
		IBioTMLModelEvaluationConfigurator confg = new BioTMLModelEvaluationConfiguratorImpl();
		confg.setCrossValidationByCorpusDoc(3);
		confg.setCrossValidationByCorpusSent(3);
		return confg;
	}


	// --------------------------EVALUATION --------------------------//


	public static void evaluateAnnotation(List<IBioTMLEntity> goldAnnotations, List<IBioTMLEntity> toCompareAnnotations){
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

	public static void evaluateNejiAnnotations(String groupType, String nejiAnnotationsDir, String goldAnnotationsFile) throws BioTMLException{
		List<IBioTMLEntity> annotations = loadAnnotationsFromBC2AnnotationsFolder(nejiAnnotationsDir, groupType);
		List<IBioTMLEntity> goldAnnotations = loadGoldAnnotationsFromTSVFile(goldAnnotationsFile);
		System.out.println("Results for:" + groupType.toUpperCase() +"\n");
		evaluateAnnotation(goldAnnotations,annotations);

	}


	public static void createFileWithAnnotations(List<IBioTMLEntity> goldAnnotations, List<IBioTMLEntity> toCompareAnnotations, String destinFilePath) throws IOException{
		PrintWriter analisysFile=new PrintWriter(new File(destinFilePath));
		analisysFile.println("Gold Annotations\n");
		for (IBioTMLEntity anotation:goldAnnotations){
			analisysFile.println(anotation);
		}
		analisysFile.println();
		analisysFile.println("To Compare Annotations\n");
		for (IBioTMLEntity anotation:toCompareAnnotations){
			analisysFile.println(anotation);
		}
		analisysFile.close();
	}


	// --------------------------FEATURES GENERATOR --------------------------//

	public static BioTMLFeatureGeneratorConfiguratorImpl loadfeatures(){
		Set<String> features = new TreeSet<String>();
		features.add("WORD");
		features.add("NUMCAPS");
		features.add("NUMDIGITS");
		features.add("LENGTH");
		features.add("LENGTHGROUP");
		//		features.add("PORTERSTEM");
		features.add("INITCAPS");
		features.add("ENDCAPS");
		features.add("ALLCAPS");
		features.add("NOCAPS");
		features.add("MIXCAPS");
		features.add("SYMBOLNUMCHAR");
		features.add("HYPHEN");
		features.add("BACKSLASH");
		features.add("OPENBRACKET");
		features.add("CLOSEBRACKET");
		features.add("COLON");
		features.add("SEMICOLON");
		features.add("PERCENT");
		features.add("OPENPARENT");
		features.add("CLOSEPARENT");
		features.add("COMMA");
		features.add("DOT");
		features.add("APOSTROPHE");
		features.add("QUOTATIONMARK");
		features.add("ASTERISK");
		features.add("EQUAL");
		features.add("PLUS");
		//		features.add("ROMANNUM");
		features.add("GREEKSYMB");
		features.add("MORPHOLOGYTYPEI");
		features.add("MORPHOLOGYTYPEII");
		features.add("MORPHOLOGYTYPEIII");
		features.add("2SUFFIX");
		features.add("3SUFFIX");
		features.add("4SUFFIX");
		features.add("2PREFIX");
		features.add("3PREFIX");
		features.add("4PREFIX");
		features.add("CHARNGRAM");
		features.addAll(new NLP4JFeatures().getRecomendedNERFeatureIds());
		//				features.add("OPENNLPPOS");
		//				features.add("OPENNLPCHUNK");
		//				features.add("OPENNLPCHUNKPARSING");
		return new BioTMLFeatureGeneratorConfiguratorImpl(features);
	}

	public static BioTMLFeatureGeneratorConfiguratorImpl loadWordFeaturesForTest(){
		Set<String> features = new TreeSet<String>();
		features.add("WORD");
		return new BioTMLFeatureGeneratorConfiguratorImpl(features);
	}


	// --------------------------LOADERS SECTION --------------------------//

	public static List<IBioTMLDocument> loadBioTMLDocumentsFromBioCFile(String filePath) throws IOException{
		List<IBioTMLDocument> docs = new ArrayList<IBioTMLDocument>();
		//		List<String> docInString = new ArrayList<String>();

		String patentID,title,text;
		Set<String> lines = FileHandling.getFileLinesTExt(new File(filePath));
		for(String line:lines)
		{
			String[] data = line.split("\\t");
			patentID = data[0];
			title = data[1];
			text = data[2];
			docs.add(new BioTMLDocumentImpl(GenerateRandomId.generateID(), title , patentID, BioTMLNLP4J.getInstance().getSentences(text)));
		}
		return docs;
	}


	public static List<IBioTMLEntity> loadGoldAnnotationsFromTSVFile (String goldTSVAnnotationsFile) throws BioTMLException{
		File annotationFile = new File(goldTSVAnnotationsFile);
		Map<String, Long> mapDocNameToDocID = new HashMap<>();	
		try {
			List<IBioTMLEntity> annotations = new ArrayList<IBioTMLEntity>();
			BufferedReader reader = new BufferedReader(new FileReader(annotationFile));
			String line;
			while((line = reader.readLine())!=null){
				String[] annotationLine = line.split("\t");
				if(!mapDocNameToDocID.containsKey(annotationLine[0])){
					mapDocNameToDocID.put(annotationLine[0], getLastDocID(mapDocNameToDocID)+1);
				}
				annotations.add(new BioTMLEntityImpl(mapDocNameToDocID.get(annotationLine[0]), annotationLine[5], Long.valueOf(annotationLine[2]), Long.valueOf(annotationLine[3])));
			}
			reader.close();
			return annotations;
		} catch (IOException exc) {
			throw new BioTMLException(exc);
		} 
	}


	public static List<IBioTMLEntity> loadGoldAnnotationsFromEvalFile (String goldTSVAnnotationsEvalFile) throws BioTMLException{
		File annotationFile = new File(goldTSVAnnotationsEvalFile);
		Map<String, Long> mapDocNameToDocID = new HashMap<>();	
		try {
			List<IBioTMLEntity> annotations = new ArrayList<IBioTMLEntity>();
			BufferedReader reader = new BufferedReader(new FileReader(annotationFile));
			String line;
			while((line = reader.readLine())!=null){
				String[] annotationLine = line.split("\t");
				if(!mapDocNameToDocID.containsKey(annotationLine[0])){
					mapDocNameToDocID.put(annotationLine[0], getLastDocID(mapDocNameToDocID)+1);
				}
				String[] classificators = annotationLine[1].split(":");
				annotations.add(new BioTMLEntityImpl(mapDocNameToDocID.get(annotationLine[0]), classificators[0], Long.valueOf(classificators[1]), Long.valueOf(classificators[2])));
			}
			reader.close();
			return annotations;
		} catch (IOException exc) {
			throw new BioTMLException(exc);
		}
	}

	public static List<IBioTMLEntity> loadAnnotationsFromGroupedBC2File (String annotationsFile) throws BioTMLException{
		File annotationFile = new File(annotationsFile);
		Map<String, Long> mapDocNameToDocID = new HashMap<>();	
		try {
			List<IBioTMLEntity> annotations = new ArrayList<IBioTMLEntity>();
			BufferedReader reader = new BufferedReader(new FileReader(annotationFile));
			String line;
			while((line = reader.readLine())!=null){
				String[] annotationLine = line.split("\\|");
				if(!mapDocNameToDocID.containsKey(annotationLine[0])){
					mapDocNameToDocID.put(annotationLine[0], getLastDocID(mapDocNameToDocID)+1);
				}
				String[] classificators = annotationLine[1].split(" ");
				annotations.add(new BioTMLEntityImpl(mapDocNameToDocID.get(annotationLine[0]), annotationLine[2], Long.valueOf(classificators[0]), Long.valueOf(classificators[1])));
			}
			reader.close();
			return annotations;
		} catch (IOException exc) {
			throw new BioTMLException(exc);
		} 
	}


	public static List<IBioTMLEntity> loadAnnotationsFromBC2AnnotationsFolder(String annotationsFolder, String groupType) throws BioTMLException{
		List<IBioTMLEntity> entities= new ArrayList<>();
		File annotationsFolderFile = new File(annotationsFolder);
		if (annotationsFolderFile.isDirectory()){
			File[] files = annotationsFolderFile.listFiles();
			for (int i = 0; i < files.length; i++) {
				entities.addAll(loadAnnotationsFromBC2AnnotationsFile(files[i].getPath(), groupType));
			}
		}
		else{
			throw new BioTMLException("The file that you provided must be a folder! Use loadAnnotationsFromBC2AnnotationsFile method instead.");
		}
		return entities;
	}


	public static List<IBioTMLEntity> loadAnnotationsFromBC2AnnotationsFile (String annotationsFile, String groupType) throws BioTMLException{
		File annotationFile = new File(annotationsFile);
		Map<String, Long> mapDocNameToDocID = new HashMap<>();	
		try {
			List<IBioTMLEntity> annotations = new ArrayList<IBioTMLEntity>();
			BufferedReader reader = new BufferedReader(new FileReader(annotationFile));
			String line;
			while((line = reader.readLine())!=null){
				String[] annotationLine = line.split("\\|");
				if(!mapDocNameToDocID.containsKey(annotationLine[0])){
					mapDocNameToDocID.put(annotationLine[0], getLastDocID(mapDocNameToDocID)+1);
				}
				String[] classificators = annotationLine[1].split(" ");
				annotations.add(new BioTMLEntityImpl(mapDocNameToDocID.get(annotationLine[0]), groupType, Long.valueOf(classificators[0]), Long.valueOf(classificators[1])));
			}
			reader.close();
			return annotations;
		} catch (IOException exc) {
			throw new BioTMLException(exc);
		} 
	}


	// --------------------------CONVERTERS SECTION --------------------------//

	public static  void convertBioCAnnotationsFileIntoBC2AnnotationsFormat(String biocreativeFilePath,String bc2FilePath) throws IOException{
		BufferedReader reader = new BufferedReader(new FileReader(biocreativeFilePath));
		String line;
		PrintWriter writer;
		if (bc2FilePath==null || bc2FilePath.isEmpty()){
			String[] biocreativeFileName = biocreativeFilePath.split("/");
			writer = new PrintWriter(biocreativeFilePath.replace(biocreativeFileName[biocreativeFileName.length-1], biocreativeFileName[biocreativeFileName.length-1].split("\\.")[0]+"_bc2"), "UTF-8");
		}
		else{
			writer = new PrintWriter(bc2FilePath, "UTF-8");
		}
		while((line = reader.readLine())!=null){
			String[] annotationLine = line.split("\t");
			writer.println(annotationLine[0]+"|"+annotationLine[2] + " " + annotationLine[3] + "|" + annotationLine[5]);
		}
		reader.close();
		writer.close();

	}


	public static void convertBioCTextFilesIntoBC2TextFormat(String filePath,String destinationPath) throws IOException{

		// Verify if file exists
		File file = new File(filePath);
		if (!file.exists() || !file.canRead()) {
			System.out.println("File doesn't exist or can't be read.");
			System.exit(0);
		}

		// Read file
		FileInputStream is = new FileInputStream(filePath);
		BufferedReader br = new BufferedReader(new InputStreamReader(is));

		// Convert file to BC2
		if (destinationPath==null){

			String[] sections = filePath.split("/");
			destinationPath=filePath.replace(sections[sections.length-1], "resultantText.txt");
		}
		if (!new File(destinationPath).exists()){
			new File(destinationPath).mkdirs();
		}
		PrintWriter pwt = new PrintWriter(destinationPath);
		String line;

		while ((line = br.readLine()) != null) {

			String[] parts = line.split("\t");

			String id = parts[0];
			String title = parts[1];
			String abtract = parts[2];            

			pwt.println(id + " " + title + " " + abtract);
		}
		pwt.close();
		br.close();
		is.close();
	}


	public static void convertBC2FilesToBC2GroupFilesUsingGoldAnnotationsFile(String bc2Filepath, String biocreativeFilePath) throws IOException{
		// Verify if file exists
		File bc2File = new File(bc2Filepath);
		File biocreativeFile= new File(biocreativeFilePath);
		if (!bc2File.exists() || !bc2File.canRead() || !biocreativeFile.exists() || !biocreativeFile.canRead()) {
			System.out.println("One of the two (or two) files doesn't exist or can't be read.");
			System.exit(0);
		}
		// Read file
		FileInputStream is = new FileInputStream(bc2Filepath);
		BufferedReader br = new BufferedReader(new InputStreamReader(is));

		String[] sections = bc2Filepath.split("/");
		String destinationPath = bc2Filepath.replace(sections[sections.length-1], "groupBC2FileOf" + sections[sections.length-1]);

		PrintWriter pwt = new PrintWriter(destinationPath);
		String line;

		while ((line = br.readLine()) != null) {

			String[] annotationsLine = line.split("\\|");
			String group = getGroupForAID(biocreativeFilePath, annotationsLine[2]);

			if (group!=null){
				pwt.println(line.replace(annotationsLine[annotationsLine.length-1],group));
			}
		}
		pwt.close();
		br.close();
		is.close();
	}

	public static void convertBioCAnnotationsToBC2FormatUsingAFolder(String biocreativeFilesDir, String destinationDir) throws IOException{
		File dest = new File(destinationDir);
		if (!dest.exists()){
			dest.mkdirs();
		}
		File[] files = new File (biocreativeFilesDir).listFiles();
		for (File file:files){
			if (!file.isDirectory()){
				convertBioCAnnotationsFileIntoBC2AnnotationsFormat(file.getPath(), destinationDir+"/"+(file.getName().replace(".tsv", "")));			
			}
		}
	}


	public static void convertBioCTextToBC2FormatUsingAFolder(String biocreativeFilesDir, String destinationDir) throws IOException{
		File dest = new File(destinationDir);
		if (!dest.exists()){
			dest.mkdirs();
		}
		File[] files = new File (biocreativeFilesDir).listFiles();
		for (File file:files){
			if (!file.isDirectory()){
				convertBioCTextFilesIntoBC2TextFormat(file.getPath(), destinationDir+"/"+(file.getName().replace(".txt", "")));			
			}
		}
	}

	public static void convertJoChemDictToNejiFormat(String joChemPath, String joChemDestinPath) throws IOException{
		File file = new File(joChemPath);
		if (!file.exists() || !file.canRead()) {
			System.out.println("File doesn't exist or can't be read.");
			System.exit(0);
		}

		FileInputStream is = new FileInputStream(joChemPath);
		BufferedReader br = new BufferedReader(new InputStreamReader(is));

		PrintWriter pwt = new PrintWriter(joChemDestinPath);

		String line;
		while ((line = br.readLine()) != null) {
			String[] parts = line.split("\t");
			String ids = parts[2];
			String[] idParts = ids.split("\\|")[0].split("_");//cas entry
			pwt.println(idParts[0]+":"+idParts[1]+":"+"type:"+"CHEMICAL\t"+parts[0]+"|"+parts[1]);
		}
		br.close();
		is.close();

	}

	// --------------------------SPLITTERS SECTION --------------------------//

	public static List<String> splitBioCAnnotationsFilesByGroups(String biocreativeFilePath, String destinationPath) throws IOException{
		File biocreativeFile= new File(biocreativeFilePath);
		if (!biocreativeFile.exists() || !biocreativeFile.canRead()) {
			System.out.println("The file doesn't exist or can't be read.");
			System.exit(0);
		}
		FileInputStream is = new FileInputStream(biocreativeFile);
		BufferedReader br = new BufferedReader(new InputStreamReader(is));

		List<String> groupsCreated=new ArrayList<>();
		String line;
		while ((line = br.readLine()) != null) {

			String[] annotationsLine = line.split("\t");
			String groupString = annotationsLine[5];
			if (!groupsCreated.contains(groupString)){
				groupsCreated.add(groupString);
			}
		}
		br.close();
		is.close();

		List<String> createdFiles=new ArrayList<>();
		//create the files after verifying which groups are available
		for (String group: groupsCreated){
			String groupFilePath = createBioCAnnotationsFileForASpecificGroup(biocreativeFile, group, destinationPath);
			createdFiles.add(groupFilePath);
		}
		return createdFiles;
	}



	public static void splitBioCTextFileIntoAGroupSpecificOneUsingAnnotationsFile(String bcFilePath, String annotationsFilePath, String group, String destinDir) throws IOException{
		File file = new File(bcFilePath);
		if (!file.exists() || !file.canRead()) {
			System.out.println("File doesn't exist or can't be read.");
			System.exit(0);
		}
		//load ids from annotations file 
		Set<String> ids=new HashSet<>();

		FileInputStream is = new FileInputStream(annotationsFilePath);
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		String line;

		while ((line = br.readLine()) != null) {
			String[] parts = line.split("\t");
			ids.add(parts[0]);
		}
		br.close();
		is.close();

		//filter text file using the previous ids
		is = new FileInputStream(bcFilePath);
		br = new BufferedReader(new InputStreamReader(is));

		PrintWriter pwt = new PrintWriter(destinDir + "/"+group + file.getName());

		line="";

		while ((line = br.readLine()) != null) {

			String[] parts = line.split("\t");

			String id = parts[0];
			if (ids.contains(id)){
				pwt.println(line);
			}
		}

		pwt.close();
		br.close();
		is.close();
	}


	// --------------------------PRIVATE METHODS SECTION --------------------------//

	private static long getLastDocID(Map<String, Long> mapDocNameToDocID){
		List<Integer> docIDs=new ArrayList<>();
		if (mapDocNameToDocID.keySet().size()>0){
			Iterator<Long> iterator = mapDocNameToDocID.values().iterator();
			while (iterator.hasNext()){
				docIDs.add(iterator.next().intValue());
			}
			Integer[] newDocIDs = docIDs.toArray(new Integer[0]);
			Arrays.sort(newDocIDs);
			return newDocIDs[newDocIDs.length - 1];
		}else{
			return -1;
		}

	}
	private static String getGroupForAID(String biocreativeFile, String patentID) throws IOException{
		FileInputStream is = new FileInputStream(biocreativeFile);
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		String line;
		while ((line = br.readLine()) != null) {
			String[] annotationsLine = line.split("\t");
			if (annotationsLine[4].contains(patentID) || patentID.contains(annotationsLine[4])){
				return annotationsLine[5];
			}
		}
		is.close();
		br.close();
		return null;
	}

	private static String createBioCAnnotationsFileForASpecificGroup(File biocreativeFile,String group, String destinationPath) throws IOException{
		FileInputStream is = new FileInputStream(biocreativeFile);
		BufferedReader br = new BufferedReader(new InputStreamReader(is));

		String[] sections = biocreativeFile.getPath().split("/");
		File destinationFile = null;
		if (destinationPath==null || destinationPath.isEmpty()){
			destinationFile =new File(biocreativeFile.getPath().replace(sections[sections.length-1], "groupFiles/"));
		}
		else{
			destinationFile=new File (destinationPath);

		}
		if (!destinationFile.exists()){
			destinationFile.mkdirs();
		}
		String destinationFilePath = destinationFile.getPath()+ "/" + group + sections[sections.length-1];
		PrintWriter pwt = new PrintWriter(destinationFilePath);
		String line;
		while ((line = br.readLine()) != null) {

			String[] annotationsLine = line.split("\t");
			String groupString = annotationsLine[5];
			if (group.equalsIgnoreCase(groupString)){
				pwt.println(line);
			}
		}
		pwt.close();
		br.close();
		is.close();

		return destinationFilePath;
	}

}