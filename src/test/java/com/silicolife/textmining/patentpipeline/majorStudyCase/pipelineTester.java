package test.java.com.silicolife.textmining.patentpipeline.majorStudyCase;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.junit.Test;

import com.silicolife.textmining.core.datastructures.documents.PDFtoText;
import com.silicolife.textmining.core.datastructures.exceptions.process.InvalidConfigurationException;
import com.silicolife.textmining.core.datastructures.init.exception.InvalidDatabaseAccess;
import com.silicolife.textmining.core.datastructures.textprocessing.NormalizationForm;
import com.silicolife.textmining.core.datastructures.textprocessing.TermSeparator;
import com.silicolife.textmining.core.init.DatabaseConnectionInit;
import com.silicolife.textmining.core.interfaces.core.configuration.IProxy;
import com.silicolife.textmining.core.interfaces.core.dataaccess.database.DataBaseTypeEnum;
import com.silicolife.textmining.core.interfaces.core.dataaccess.exception.ANoteException;
import com.silicolife.textmining.core.interfaces.core.document.IPublication;
import com.silicolife.textmining.core.interfaces.process.IR.exception.InternetConnectionProblemException;
import com.silicolife.textmining.processes.ir.patentpipeline.PatentPipelineException;
import com.silicolife.textmining.processes.ir.patentpipeline.PatentPiplineSearch;
import com.silicolife.textmining.processes.ir.patentpipeline.components.metainfomodules.ops.IROPSPatentMetaInformationRetrievalConfigurationImpl;
import com.silicolife.textmining.processes.ir.patentpipeline.components.metainfomodules.ops.OPSPatentMetaInformationRetrieval;
import com.silicolife.textmining.processes.ir.patentpipeline.components.metainfomodules.patentrepository.IRPatentRepositoryPatentMetaInformationRetrievalConfigurationImpl;
import com.silicolife.textmining.processes.ir.patentpipeline.components.metainfomodules.patentrepository.PatentRepositoryPatentMetaInformationRetrieval;
import com.silicolife.textmining.processes.ir.patentpipeline.components.retrievalmodules.ops.IROPSPatentRetrievalConfigurationImpl;
import com.silicolife.textmining.processes.ir.patentpipeline.components.retrievalmodules.ops.OPSPatentRetrieval;
import com.silicolife.textmining.processes.ir.patentpipeline.configuration.IIRPatentPipelineConfiguration;
import com.silicolife.textmining.processes.ir.patentpipeline.configuration.IIRPatentPipelineSearchConfiguration;
import com.silicolife.textmining.processes.ir.patentpipeline.configuration.IIRPatentPipelineSearchStepsConfiguration;
import com.silicolife.textmining.processes.ir.patentpipeline.configuration.IRPatentPipelineSearchConfigurationImpl;
import com.silicolife.textmining.processes.ir.patentpipeline.configuration.IRPatentPipelineSearchStepsConfigurationImpl;
import com.silicolife.textmining.processes.ir.patentpipeline.core.PatentPipeline;
import com.silicolife.textmining.processes.ir.patentpipeline.core.metainfomodule.IIRPatentMetaInformationRetrievalConfiguration;
import com.silicolife.textmining.processes.ir.patentpipeline.core.metainfomodule.IIRPatentMetainformationRetrievalSource;
import com.silicolife.textmining.processes.ir.patentpipeline.core.metainfomodule.WrongIRPatentMetaInformationRetrievalConfigurationException;
import com.silicolife.textmining.processes.ir.patentpipeline.core.retrievalmodule.IIRPatentRetrieval;
import com.silicolife.textmining.processes.ir.patentpipeline.core.retrievalmodule.IIRPatentRetrievalConfiguration;
import com.silicolife.textmining.processes.ir.patentpipeline.core.retrievalmodule.IIRPatentRetrievalReport;
import com.silicolife.textmining.processes.ir.patentpipeline.core.retrievalmodule.WrongIRPatentRetrievalConfigurationException;
import com.silicolife.textmining.processes.ir.patentpipeline.core.searchmodule.IRPatentSearchConfigurationImpl;
import com.silicolife.textmining.processes.ir.patentpipeline.core.searchmodule.WrongIRPatentIDRecoverConfigurationException;

import main.java.com.silicolife.textmining.dictionaryLoader.loaderDatastructures.BioCreativeChemdnerPatentsLoaderConfigurationImpl;
import main.java.com.silicolife.textmining.dictionaryLoader.loaderInterfaces.IBioCreativeChemdnerPatentsLoaderConfiguration;
import main.java.com.silicolife.textmining.dictionaryLoader.loaderInterfaces.WrongBioCreativeChemdnerPatentsLoaderConfigurationException;
import main.java.com.silicolife.textmining.patentpipeline.loaders.BioCreativeChemdnerPatentsLoader;
import main.java.com.silicolife.textmining.patentpipeline.pdfToTextModule.OCREvaluator;

public class pipelineTester {

	@Test

	public void test1() throws WrongIRPatentMetaInformationRetrievalConfigurationException, WrongIRPatentIDRecoverConfigurationException, WrongBioCreativeChemdnerPatentsLoaderConfigurationException, IOException, WrongIRPatentRetrievalConfigurationException, PatentPipelineException, ANoteException, InvalidDatabaseAccess, InvalidConfigurationException, InternetConnectionProblemException
	{

		// ################################ CONFIGURATIONS SECTION ################################
		
		
		DatabaseConnectionInit.init(DataBaseTypeEnum.MYSQL, "localhost", "3306", "PatPipeTests", "root", "admin");

//		String accessTokenOPS = "LLCAsGwQHRQAi9sKU3L83tMcKszoVnhi:q9sxdjCvGbLDsWrc";
//		String accessTokenOPS="y1MBmtZjwRm6ia1eNdjeAJh7NxAkyhUG:I81FKAgUAY4GLivF";
//		String 	accessTokenOPS= "n6gwaIONHZKUf63Dv3zgRXrtgkhJjA4m:6zB0g9o4k3fgl3vq";
		String accessTokenOPS= "pmRQIWtNkfJbxNt2wg7GnGL0kP7aB18Y:btjR8acGUMh3aMVf";

		String usernamePatentRepository="guest";
		String passwordPatentRepository="r3p03i7oriUP@tantes!";
		String serverURLPatentRepository="http://mendel.di.uminho.pt:8080/patentrepository";	

		IProxy proxy = null;
		Properties prop=null;

//		String usernameWIPO = "silicolife";
//		String pwdWIPO = "zTi8iF0qh";

		String queryName="MajorStudyCasePatentPipelineTest";
		String query="Patent Pipeline Test Using Biocreative V CHEMDNER task training set";
		String bioCreativeFilePath="src/test/resources/chemdner/train/chemdner_patents_train_text.txt";
		String outputDir="Resultados_StudyCase";
		String txtDirName="MajorStudyCase";
		int numberOfPatentsToDownload=7000;
		List<String> countryCodes=new ArrayList<>(); //empty to include all predefined
		boolean englishPatents = false;




		//		 ################################ 1ST STEP - LOAD BIOCREATIVE FILES (ID AND ABSTRACT) ################################


		IBioCreativeChemdnerPatentsLoaderConfiguration configurationBioCreativeLoader = new BioCreativeChemdnerPatentsLoaderConfigurationImpl(bioCreativeFilePath,numberOfPatentsToDownload,englishPatents,countryCodes);
		BioCreativeChemdnerPatentsLoader mapClass = new BioCreativeChemdnerPatentsLoader(configurationBioCreativeLoader);
		Map<String, ArrayList<String>> map = mapClass.extractPatentIDs();//set de teste

		Map<String, IPublication> mapPatentIDPublication = PatentPipeline.createSimplePublicationMaps(map.keySet());		
		long starttime1 = System.currentTimeMillis();





		//################################ 2ND STEP - EXTRACT METAINFORMATION (1ST STEP OMITTED) ################################


		IIRPatentPipelineSearchStepsConfiguration configurationPipeline=new IRPatentPipelineSearchStepsConfigurationImpl();

		//		IIRPatentMetaInformationRetrievalConfiguration configurationWIPO = new IRWIPOPatentMetaInformationRetrievalConfigurationImpl(usernameWIPO, pwdWIPO, proxy );
		//		IIRPatentMetainformationRetrievalSource wipoMetaInformationRetrieval = new WIPOPatentMetaInformationRetrieval(configurationWIPO);
		//		configurationPipeline.addIRPatentRetrievalMetaInformation(wipoMetaInformationRetrieval);

		IIRPatentMetaInformationRetrievalConfiguration configurationOPSMetaInfoRetrieval=new IROPSPatentMetaInformationRetrievalConfigurationImpl(proxy, accessTokenOPS);
		IIRPatentMetainformationRetrievalSource opsMetaInformationretrieval = new OPSPatentMetaInformationRetrieval(configurationOPSMetaInfoRetrieval);
		configurationPipeline.addIRPatentRetrievalMetaInformation(opsMetaInformationretrieval);
		
		IIRPatentMetaInformationRetrievalConfiguration configurationPatentRepository=new IRPatentRepositoryPatentMetaInformationRetrievalConfigurationImpl(proxy, serverURLPatentRepository, usernamePatentRepository, passwordPatentRepository);
		IIRPatentMetainformationRetrievalSource patentMetainformationPatentRepositoryRecoverSource= new PatentRepositoryPatentMetaInformationRetrieval(configurationPatentRepository);
		configurationPipeline.addIRPatentRetrievalMetaInformation(patentMetainformationPatentRepositoryRecoverSource);

		IIRPatentPipelineSearchConfiguration searchConf= new IRPatentPipelineSearchConfigurationImpl(query);
		IIRPatentPipelineConfiguration configuration = new IRPatentSearchConfigurationImpl(searchConf,queryName,prop,configurationPipeline);

		PatentPiplineSearch runnerIQueryMaker = new PatentPiplineSearch();
		runnerIQueryMaker.search(configuration);
		//		List<IPublication> publications = runnerIQueryMaker.getPublicationDocuments();



		// ################################ 3RD STEP - EXTRACT PDF FILES ################################

		//		IIRPatentRetrievalConfiguration configurationWIPO = new IRWIPOPatentRetrievalConfigurationImpl(username, pwd, outputDir, proxy );
		//		IIRPatentRetrieval WIPOpatentRetrievalProcess = new WIPOPatentRetrieval(configurationWIPO);
		//		patentPipeline.addPatentIDRetrieval(WIPOpatentRetrievalProcess);

		PatentPipeline patentPipeline = new PatentPipeline();
		IIRPatentRetrievalConfiguration configurationOPSPDFRetrieval = new IROPSPatentRetrievalConfigurationImpl(outputDir, proxy, accessTokenOPS);
		IIRPatentRetrieval OPSpatentRetrievalProcess = new OPSPatentRetrieval(configurationOPSPDFRetrieval);
		patentPipeline.addPatentIDRetrieval(OPSpatentRetrievalProcess);		
		IIRPatentRetrievalReport pdfDownload = patentPipeline.executePatentRetrievalPDFStep(mapPatentIDPublication);//set OCR
		System.out.println("Retrieved PatentIds:\n"+pdfDownload.getRetrievedPatents().toString());
		System.out.println("Not Retrieved PatentIds:\n"+pdfDownload.getNotRetrievedPatents().toString());
		System.out.println("Percentage of Total Retrieved Patents:\n"+((float)pdfDownload.getRetrievedPatents().size()/(float)(pdfDownload.getNotRetrievedPatents().size()+pdfDownload.getRetrievedPatents().size()))*100+"%");
		long endtime1 = System.currentTimeMillis();
		System.out.println("\nTIME:"+(float)((endtime1-starttime1)/1000)+"s\n");



		// ################################ 4TH STEP - PDF TO TEXT CONVERSION PROCESS CREATING CORPUS ################################

		Map<String,String> setTest=new HashMap<>();

		for (String patentID :pdfDownload.getRetrievedPatents()){
			Path docPath = Paths.get(outputDir+"/" + txtDirName);//);	
			if (!Files.exists(docPath))
				Files.createDirectories(docPath);
			String txtName = docPath+ "/"+patentID+".txt";
			File verifyFileExistence = new File(txtName);
			if (!verifyFileExistence.exists()){
				long starttime = System.currentTimeMillis();
				System.out.println("Started new OCR for: "+patentID);
				String file = PDFtoText.convertPDFDocument(outputDir+"/"+patentID+".pdf");
				String ocrTermSep=TermSeparator.termSeparator(file);
				createTXTFile(txtName, NormalizationForm.removeOffsetProblemSituation(ocrTermSep));
				long endtime = System.currentTimeMillis();
				System.out.println("OCR done on patent: "+patentID+ " on "+ (float)((endtime-starttime)/1000)+"s");
			}
			setTest.put(patentID,txtName);
			System.out.println("Patent processed:"+patentID);
		}
		FileWriter arq = new FileWriter(outputDir+"/"+txtDirName+"/"+"TrainPatentsResults.txt");
		PrintWriter gravarArq = new PrintWriter(arq);
		List<Double> setPrecision = new ArrayList<Double>();
		List<Double> setRecall = new ArrayList<Double>();
		for (String patent:setTest.keySet()){
			String SEPARATOR = "[ '<>/(),\n\\.]+";
			String ocrResult = openTXTFile(setTest.get(patent));
			String[] tokensOCR = arrayConstruct(ocrResult, SEPARATOR);
			System.out.println("Num tokens OCR:"+tokensOCR.length);
			//0.9997627520759194 

			String docTermSep=TermSeparator.termSeparator(map.get(patent).get(1));
			//				String[] tokensDoc = arrayConstruct(map.get(patent).get(1), SEPARATOR);
			String[] tokensDoc = arrayConstruct(NormalizationForm.removeOffsetProblemSituation(docTermSep), SEPARATOR);
			System.out.println("Num tokens Doc:"+tokensDoc.length);
			OCREvaluator evaluator=new OCREvaluator(tokensOCR,tokensDoc);
			evaluator.doMatrix();
			double precision = evaluator.calculateTracebackAndSimilarityPercentagePrecision();
			double recall = evaluator.calculateTracebackAndSimilarityPercentageRecall();
			gravarArq.println(patent);
			gravarArq.println("Num tokens Doc:"+tokensDoc.length);
			gravarArq.println("Num tokens OCR:"+tokensOCR.length);
			gravarArq.println("Recall: "+recall);
			System.out.println(recall);
			System.out.println(precision);
			gravarArq.println("Precision: "+precision);
			gravarArq.println("Local alignment:" + evaluator.calculateMatchAndTotalTransitions().get(0)+"/"+evaluator.calculateMatchAndTotalTransitions().get(1));
			gravarArq.println();
			setPrecision.add(precision);
			setRecall.add(recall);
		}
		double totalRecall=0;
		double totalPrecision=0;
		double numberPatentes=0;
		for (Double num:setRecall){
			totalRecall+=num;
			numberPatentes+=1;
		}
		for(Double num:setPrecision){
			totalPrecision+=num;
		}
		double mediaRecall= totalRecall/numberPatentes;
		double mediaPrecision=totalPrecision/numberPatentes;
		gravarArq.println();
		gravarArq.println("RESULTADOS GERAIS:");
		gravarArq.println("Precision: "+ mediaPrecision);
		gravarArq.println("Recall: " + mediaRecall);
		gravarArq.println("F1 measure: " + 2*((mediaPrecision*mediaRecall)/(mediaPrecision+mediaRecall)));
		arq.close();


		//5th step - Do NER to Abstracts and to Full Texts

	}


	private static String[] arrayConstruct(String text,String SEPARATOR){
		String[] tokens = text.split(SEPARATOR);
		return tokens;
	}

	private static void createTXTFile(String outputFile, String OCRResult) throws IOException{
		FileWriter arq = new FileWriter(outputFile);
		PrintWriter gravarArq = new PrintWriter(arq);
		gravarArq.print(OCRResult);
		arq.close();
	}

	private static String openTXTFile(String filePath) throws IOException{
		String document=new String();
		FileInputStream stream = new FileInputStream(filePath);
		InputStreamReader reader = new InputStreamReader(stream);
		BufferedReader br = new BufferedReader(reader);
		String line = br.readLine(); 

		while (line != null) {
			document+=line;
			line = br.readLine(); 
		}
		br.close();
		return document;
	}

}
