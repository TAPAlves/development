package test.java.com.silicolife.textmining.linneausTaggerToMemoryRun;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.junit.Test;

import com.silicolife.textmining.core.datastructures.exceptions.process.InvalidConfigurationException;
import com.silicolife.textmining.core.datastructures.init.InitConfiguration;
import com.silicolife.textmining.core.datastructures.init.exception.InvalidDatabaseAccess;
import com.silicolife.textmining.core.datastructures.process.ProcessRunStatusConfigurationEnum;
import com.silicolife.textmining.core.datastructures.process.ner.NERCaseSensativeEnum;
import com.silicolife.textmining.core.datastructures.process.ner.ResourcesToNerAnote;
import com.silicolife.textmining.core.interfaces.core.dataaccess.exception.ANoteException;
import com.silicolife.textmining.core.interfaces.core.document.IAnnotatedDocument;
import com.silicolife.textmining.core.interfaces.core.document.corpus.ICorpus;
import com.silicolife.textmining.core.interfaces.core.report.processes.INERProcessReport;
import com.silicolife.textmining.core.interfaces.process.IR.exception.InternetConnectionProblemException;
import com.silicolife.textmining.core.interfaces.resource.IResource;
import com.silicolife.textmining.core.interfaces.resource.IResourceElement;
import com.silicolife.textmining.core.interfaces.resource.lexicalwords.ILexicalWords;
import com.silicolife.textmining.machinelearning.DatabaseConnectionInit;
import com.silicolife.textmining.processes.ie.ner.linnaeus.adapt.uk.ac.man.entitytagger.matching.Matcher.Disambiguation;
import com.silicolife.textmining.processes.ie.ner.linnaeus.configuration.INERLinnaeusConfiguration;
import com.silicolife.textmining.processes.ie.ner.linnaeus.configuration.NERLinnaeusConfigurationImpl;
import com.silicolife.textmining.processes.ie.ner.linnaeus.configuration.NERLinnaeusPreProcessingEnum;

import main.java.com.silicolife.textmining.LinnaeusExtension.LinnaeusTaggerMemoryRun;
import main.java.com.silicolife.textmining.LinnaeusExtension.ResourceInMemoryImpl;
import test.java.com.silicolife.textmining.dictionaryLoader.testJoChemDictLoader;

public class LinneausTaggerToMemoryRunTest {

	@Test
	public void test() throws InvalidDatabaseAccess, ANoteException, InternetConnectionProblemException, IOException, InvalidConfigurationException {
		DatabaseConnectionInit.init("localhost","3306","createdatest","root","admin");
		ICorpus corpus = InitConfiguration.getDataAccess().getCorpusByID(8861883496831132819L);
		//		ICorpus corpus = CreateCorpusFromPublicationManagerTest.createCorpus().getCorpus();
		IResource<IResourceElement> dictionary = createDictionary();
		INERProcessReport report = executeLinnaeus(corpus, dictionary);
		assertTrue(report.isFinishing());
	}

	public static INERProcessReport executeLinnaeus(ICorpus corpus,
			IResource<IResourceElement> dictionary) throws ANoteException, InvalidConfigurationException {
		boolean useabreviation = true;
		boolean normalized = true;
		NERCaseSensativeEnum caseSensitive = NERCaseSensativeEnum.INALLWORDS;
		ILexicalWords stopwords = null;
		NERLinnaeusPreProcessingEnum preprocessing = NERLinnaeusPreProcessingEnum.No;
		Disambiguation disambiguation = Disambiguation.OFF;
		ResourcesToNerAnote resourceToNER = new ResourcesToNerAnote();
		resourceToNER.addUsingAnoteClasses(dictionary, dictionary.getResourceClassContent(), dictionary.getResourceClassContent());
		Map<String, Pattern> patterns = new HashMap<String, Pattern>();
		int numThreads = 4;
		boolean usingOtherResourceInfoToImproveRuleAnnotations = false;
		int sizeOfSmallWordsToBeNotAnnotated = 0;
		INERLinnaeusConfiguration configurations = new NERLinnaeusConfigurationImpl(corpus,ProcessRunStatusConfigurationEnum.createnew, patterns , resourceToNER, useabreviation , disambiguation , caseSensitive , normalized , numThreads , stopwords , preprocessing , usingOtherResourceInfoToImproveRuleAnnotations, sizeOfSmallWordsToBeNotAnnotated );
		LinnaeusTaggerMemoryRun linnaues = new LinnaeusTaggerMemoryRun( );
		System.out.println("Execute Linnaeus");
		INERProcessReport report = linnaues.executeCorpusNER(configurations);
		List<IAnnotatedDocument> anDocs = linnaues.getAllIAnnotatedDocuments();
		for (IAnnotatedDocument doc:anDocs){
			System.out.println(doc.getId() + ": " + doc.getAuthors());

		}
		return report;
	}

	public static IResource<IResourceElement> createDictionary() throws ANoteException, IOException{
		System.out.println("Create Dictionary");

		List<IResourceElement> resource = testJoChemDictLoader.getJoChemDictionary();
		IResource<IResourceElement> reso=new ResourceInMemoryImpl();
		reso.addResourceElements(resource);
		//		IDictionary dictionary = new DictionaryImpl();
		//		dictionary.addResourceElements(resource);
		//		BioMetaEcoCycFlatFileLoader loader = new BioMetaEcoCycFlatFileLoader();
		//		String byocycFolder = "src/test/resources/BioCyc/small";
		//		File file = new File(byocycFolder);
		//		if(loader.checkFile(file))
		//		{
		//			Properties properties = new Properties();
		//			String loaderUID = "";
		//			boolean loadExtendalIDds = true;
		//			IDictionaryLoaderConfiguration configuration = new DictionaryLoaderConfigurationImpl(loaderUID , dictionary, file, properties , loadExtendalIDds );
		//			loader.loadTerms(configuration );
		//		}
		//		return dictionary;
		return reso;
	}



}

