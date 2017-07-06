package test.java.com.silicolife.textmining.patentpipeline.majorStudyCase;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.MalformedURLException;
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
import com.silicolife.textmining.core.datastructures.textprocessing.NormalizationForm;
import com.silicolife.textmining.core.datastructures.textprocessing.TermSeparator;
import com.silicolife.textmining.core.interfaces.core.configuration.IProxy;
import com.silicolife.textmining.core.interfaces.core.dataaccess.exception.ANoteException;
import com.silicolife.textmining.core.interfaces.core.document.IPublication;
import com.silicolife.textmining.processes.ir.patentpipeline.PatentPipelineException;
import com.silicolife.textmining.processes.ir.patentpipeline.components.metainfomodules.ops.IROPSPatentMetaInformationRetrievalConfigurationImpl;
import com.silicolife.textmining.processes.ir.patentpipeline.components.metainfomodules.ops.OPSPatentMetaInformationRetrieval;
import com.silicolife.textmining.processes.ir.patentpipeline.components.metainfomodules.wipo.IRWIPOPatentMetaInformationRetrievalConfigurationImpl;
import com.silicolife.textmining.processes.ir.patentpipeline.components.metainfomodules.wipo.WIPOPatentMetaInformationRetrieval;
import com.silicolife.textmining.processes.ir.patentpipeline.components.retrievalmodules.ops.IROPSPatentRetrievalConfigurationImpl;
import com.silicolife.textmining.processes.ir.patentpipeline.components.retrievalmodules.ops.OPSPatentRetrieval;
import com.silicolife.textmining.processes.ir.patentpipeline.components.searchmodule.bing.BingSearchPatentIDRecoverSource;
import com.silicolife.textmining.processes.ir.patentpipeline.components.searchmodule.bing.IRPatentIDRetrievalBingSearchConfigurationImpl;
import com.silicolife.textmining.processes.ir.patentpipeline.components.searchmodule.epo.EPOSearchPatentIDRecoverSource;
import com.silicolife.textmining.processes.ir.patentpipeline.components.searchmodule.epo.IRPatentIDRetrievalEPOSearchConfigurationImpl;
import com.silicolife.textmining.processes.ir.patentpipeline.components.searchmodule.googlesearch.GoogleSearchPatentIDRecoverSource;
import com.silicolife.textmining.processes.ir.patentpipeline.components.searchmodule.googlesearch.IRPatentIDRetrievalGoogleSearchConfigurationImpl;
import com.silicolife.textmining.processes.ir.patentpipeline.configuration.IIRPatentPipelineSearchConfiguration;
import com.silicolife.textmining.processes.ir.patentpipeline.configuration.IIRPatentPipelineSearchStepsConfiguration;
import com.silicolife.textmining.processes.ir.patentpipeline.configuration.IRPatentPipelineSearchConfigurationImpl;
import com.silicolife.textmining.processes.ir.patentpipeline.configuration.IRPatentPipelineSearchStepsConfigurationImpl;
import com.silicolife.textmining.processes.ir.patentpipeline.core.PatentPipeline;
import com.silicolife.textmining.processes.ir.patentpipeline.core.metainfomodule.IIRPatentMetaInformationRetrievalConfiguration;
import com.silicolife.textmining.processes.ir.patentpipeline.core.metainfomodule.IIRPatentMetainformationRetrievalSource;
import com.silicolife.textmining.processes.ir.patentpipeline.core.metainfomodule.WrongIRPatentMetaInformationRetrievalConfigurationException;
import com.silicolife.textmining.processes.ir.patentpipeline.core.ocrmodule.WrongOCRPipelineConfigurationException;
import com.silicolife.textmining.processes.ir.patentpipeline.core.retrievalmodule.IIRPatentRetrieval;
import com.silicolife.textmining.processes.ir.patentpipeline.core.retrievalmodule.IIRPatentRetrievalConfiguration;
import com.silicolife.textmining.processes.ir.patentpipeline.core.retrievalmodule.IIRPatentRetrievalReport;
import com.silicolife.textmining.processes.ir.patentpipeline.core.retrievalmodule.WrongIRPatentRetrievalConfigurationException;
import com.silicolife.textmining.processes.ir.patentpipeline.core.searchmodule.IIRPatentIDRetrievalModuleConfiguration;
import com.silicolife.textmining.processes.ir.patentpipeline.core.searchmodule.IIRPatentIDRetrievalSource;
import com.silicolife.textmining.processes.ir.patentpipeline.core.searchmodule.WrongIRPatentIDRecoverConfigurationException;

import main.java.com.silicolife.textmining.dictionaryLoader.loaderDatastructures.BioCreativeChemdnerPatentsLoaderConfigurationImpl;
import main.java.com.silicolife.textmining.dictionaryLoader.loaderInterfaces.IBioCreativeChemdnerPatentsLoaderConfiguration;
import main.java.com.silicolife.textmining.dictionaryLoader.loaderInterfaces.WrongBioCreativeChemdnerPatentsLoaderConfigurationException;
import main.java.com.silicolife.textmining.patentpipeline.loaders.BioCreativeChemdnerPatentsLoader;
import main.java.com.silicolife.textmining.patentpipeline.pdfToTextModule.OCREvaluator;
import net.sourceforge.tess4j.TesseractException;

public class pipelineTester {

	@Test

	public static void test1() throws WrongIRPatentMetaInformationRetrievalConfigurationException, WrongIRPatentIDRecoverConfigurationException, MalformedURLException
	{

		//1st step - load Biocreative Files (ID and abstract)

		//2nd step - Extract metainformation

		//3rd step - Extract PDF files

		//4th step - Use this files to get corpus with OCR

		//5th step - Do NER to Abstracts and to Full Texts





























		String query = "";

		IIRPatentPipelineSearchConfiguration patentPipelineSearchConfiguration = new IRPatentPipelineSearchConfigurationImpl(query);
		IIRPatentPipelineSearchStepsConfiguration configurationPipeline=new IRPatentPipelineSearchStepsConfigurationImpl();

		//Step 1 - Retrived Patents Ids
		String accessTokenBing = "Ju0WWwspaT9tVXY+JhWeftB2Om9yQeCCz2cRpA+fGCE";
		IIRPatentIDRetrievalModuleConfiguration configurationBing = new IRPatentIDRetrievalBingSearchConfigurationImpl(accessTokenBing);
		IIRPatentIDRetrievalSource patentIDrecoverSourceBing = new BingSearchPatentIDRecoverSource(configurationBing);
		configurationPipeline.addIRPatentIDRecoverSource(patentIDrecoverSourceBing);


		String accessTokenGoogle ="AIzaSyD60IrjYQBKEnotVTYWpcWTBVffY6l0XOU";
		String customSearchIDGoogle = "017027245643975829422:oimxfoxonzc";
		IIRPatentIDRetrievalModuleConfiguration configurationGoogle = new IRPatentIDRetrievalGoogleSearchConfigurationImpl(accessTokenGoogle, customSearchIDGoogle);
		IIRPatentIDRetrievalSource patentIDrecoverSourceGoogle = new GoogleSearchPatentIDRecoverSource(configurationGoogle);
		configurationPipeline.addIRPatentIDRecoverSource(patentIDrecoverSourceGoogle);


		String accessTokenOPS ="LLCAsGwQHRQAi9sKU3L83tMcKszoVnhi:q9sxdjCvGbLDsWrc";
		IIRPatentIDRetrievalModuleConfiguration configurationEPO = new IRPatentIDRetrievalEPOSearchConfigurationImpl(accessTokenOPS);
		IIRPatentIDRetrievalSource patentIDrecoverSourceEPO = new EPOSearchPatentIDRecoverSource(configurationEPO);
		configurationPipeline.addIRPatentIDRecoverSource(patentIDrecoverSourceEPO);



		//Step 2 - Retrived Meta Information
		IProxy proxy = null;
		Properties prop=null;

		String usernameWIPO = "silicolife";
		String pwdWIPO = "zTi8iF0qh";

		IIRPatentMetaInformationRetrievalConfiguration configurationWIPO = new IRWIPOPatentMetaInformationRetrievalConfigurationImpl(usernameWIPO, pwdWIPO, proxy );
		IIRPatentMetainformationRetrievalSource wipoMetaInformationRetrieval = new WIPOPatentMetaInformationRetrieval(configurationWIPO);
		configurationPipeline.addIRPatentRetrievalMetaInformation(wipoMetaInformationRetrieval);


		String accessTokenOPSMetaRetrieval = "LLCAsGwQHRQAi9sKU3L83tMcKszoVnhi:q9sxdjCvGbLDsWrc";
		IIRPatentMetaInformationRetrievalConfiguration configurationOPS=new IROPSPatentMetaInformationRetrievalConfigurationImpl(proxy, accessTokenOPSMetaRetrieval);
		IIRPatentMetainformationRetrievalSource opsMetaInformationretrieval = new OPSPatentMetaInformationRetrieval(configurationOPS);
		configurationPipeline.addIRPatentRetrievalMetaInformation(opsMetaInformationretrieval);

	}


	public static void main(String[] args) throws WrongIRPatentRetrievalConfigurationException, PatentPipelineException, ANoteException, WrongBioCreativeChemdnerPatentsLoaderConfigurationException, IOException, WrongOCRPipelineConfigurationException, TesseractException {
		String accessTokenOPS = "LLCAsGwQHRQAi9sKU3L83tMcKszoVnhi:q9sxdjCvGbLDsWrc";
		String username = "silicolife";
		String pwd = "zTi8iF0qh";
		//		String outputDir = "DownloadAndOCRTestBetterOCR";
		//		String outputDir= "DownloadAndOCRTestHugo/DownloadAndOCRTest";

		String outputDir="Resultados";
		String txtDirName="1approach";
		IProxy proxy = null;
		String path="src/test/resources/data/ALL_patent_abstracts_biocreative.txt";
		int numberOfPatentsToDownload=1000;
		List<String> countryCodes=new ArrayList<>();
		boolean englishPatents = true;
		countryCodes.add("AU");
		countryCodes.add("BZ");
		countryCodes.add("CA");
		countryCodes.add("CB");
		countryCodes.add("IE");
		countryCodes.add("JM");
		countryCodes.add("NZ");
		countryCodes.add("PH");
		countryCodes.add("ZA");
		countryCodes.add("TT");
		countryCodes.add("GB");
		countryCodes.add("US");
		//		countryCodes.add("WO");//some patents are in chinese

		PatentPipeline patentPipeline = new PatentPipeline();

		//		IIRPatentRetrievalConfiguration configurationWIPO = new IRWIPOPatentRetrievalConfigurationImpl(username, pwd, outputDir, proxy );
		//		IIRPatentRetrieval WIPOpatentRetrievalProcess = new WIPOPatentRetrieval(configurationWIPO);
		//		patentPipeline.addPatentIDRetrieval(WIPOpatentRetrievalProcess);

		IIRPatentRetrievalConfiguration configurationOPS = new IROPSPatentRetrievalConfigurationImpl(outputDir, proxy, accessTokenOPS);
		IIRPatentRetrieval OPSpatentRetrievalProcess = new OPSPatentRetrieval(configurationOPS);
		patentPipeline.addPatentIDRetrieval(OPSpatentRetrievalProcess);

		IBioCreativeChemdnerPatentsLoaderConfiguration configurationBioCreativeLoader = new BioCreativeChemdnerPatentsLoaderConfigurationImpl(path,numberOfPatentsToDownload,englishPatents,countryCodes);
		BioCreativeChemdnerPatentsLoader mapClass = new BioCreativeChemdnerPatentsLoader(configurationBioCreativeLoader);
		Map<String, ArrayList<String>> map = mapClass.extractPatentIDs();//set de teste

		Map<String, IPublication> mapPatentIDPublication = patentPipeline.createSimplePublicationMaps(map.keySet());		
		long starttime1 = System.currentTimeMillis();
		IIRPatentRetrievalReport pdfDownload = patentPipeline.executePatentRetrievalPDFStep(mapPatentIDPublication);//set OCR
		System.out.println("Retrieved PatentIds:\n"+pdfDownload.getRetrievedPatents().toString());
		System.out.println("Not Retrieved PatentIds:\n"+pdfDownload.getNotRetrievedPatents().toString());
		System.out.println("Percentage of Total Retrieved Patents:\n"+((float)pdfDownload.getRetrievedPatents().size()/(float)(pdfDownload.getNotRetrievedPatents().size()+pdfDownload.getRetrievedPatents().size()))*100+"%");
		long endtime1 = System.currentTimeMillis();
		System.out.println("\nTIME:"+(float)((endtime1-starttime1)/1000)+"s\n");

		Map<String,String> setTest=new HashMap<>();

		for (String patentID :pdfDownload.getRetrievedPatents()){
			Path docPath = Paths.get(outputDir+"\\" + txtDirName+ "\\");//);	
			if (!Files.exists(docPath))
				Files.createDirectories(docPath);
			String txtName = docPath+ "\\"+patentID+".txt";
			File verifyFileExistence = new File(txtName);
			if (!verifyFileExistence.exists()){
				long starttime = System.currentTimeMillis();
				System.out.println("Started new OCR for: "+patentID);
				String file = PDFtoText.convertPDFDocument(outputDir+"\\"+patentID+".pdf");
				String ocrTermSep=TermSeparator.termSeparator(file);
				createTXTFile(txtName, NormalizationForm.removeOffsetProblemSituation(ocrTermSep));
				long endtime = System.currentTimeMillis();
				System.out.println("OCR done on patent: "+patentID+ " on "+ (float)((endtime-starttime)/1000)+"s");
			}
			setTest.put(patentID,txtName);
			System.out.println("Patent processed:"+patentID);
		}
		FileWriter arq = new FileWriter(outputDir+"\\"+txtDirName+"\\"+"1000PatentsResults.txt");
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

