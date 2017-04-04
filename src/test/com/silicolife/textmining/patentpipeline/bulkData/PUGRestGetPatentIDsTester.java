package test.com.silicolife.textmining.patentpipeline.bulkData;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

import com.silicolife.textmining.core.datastructures.documents.PublicationImpl;
import com.silicolife.textmining.core.datastructures.documents.PublicationSourcesDefaultEnum;
import com.silicolife.textmining.core.datastructures.documents.query.QueryPublicationRelevanceImpl;
import com.silicolife.textmining.core.interfaces.core.configuration.IProxy;
import com.silicolife.textmining.core.interfaces.core.dataaccess.exception.ANoteException;
import com.silicolife.textmining.core.interfaces.core.document.IPublication;
import com.silicolife.textmining.core.interfaces.core.document.IPublicationExternalSourceLink;
import com.silicolife.textmining.processes.ir.epopatent.OPSUtils;
import com.silicolife.textmining.processes.ir.patentpipeline.PatentPipelineException;
import com.silicolife.textmining.processes.ir.patentpipeline.components.metainfomodules.ops.IROPSPatentMetaInformationRetrievalConfigurationImpl;
import com.silicolife.textmining.processes.ir.patentpipeline.components.metainfomodules.ops.OPSPatentMetaInformationRetrieval;
import com.silicolife.textmining.processes.ir.patentpipeline.configuration.IIRPatentPipelineSearchConfiguration;
import com.silicolife.textmining.processes.ir.patentpipeline.configuration.IRPatentPipelineSearchConfigurationImpl;
import com.silicolife.textmining.processes.ir.patentpipeline.core.PatentPipeline;
import com.silicolife.textmining.processes.ir.patentpipeline.core.metainfomodule.IIRPatentMetaInformationRetrievalConfiguration;
import com.silicolife.textmining.processes.ir.patentpipeline.core.metainfomodule.IIRPatentMetaInformationRetrievalReport;
import com.silicolife.textmining.processes.ir.patentpipeline.core.metainfomodule.IIRPatentMetainformationRetrievalSource;
import com.silicolife.textmining.processes.ir.patentpipeline.core.metainfomodule.WrongIRPatentMetaInformationRetrievalConfigurationException;
import com.silicolife.textmining.processes.ir.patentpipeline.core.searchmodule.WrongIRPatentIDRecoverConfigurationException;

import main.com.silicolife.textmining.patentpipeline.pubChemAPI.IIRPubChemPatentIDRetrievalConfiguration;
import main.com.silicolife.textmining.patentpipeline.pubChemAPI.IRPubChemPatentIDRetrieval;
import main.com.silicolife.textmining.patentpipeline.pubChemAPI.IRPubChemPatentIDRetrievalConfigurationImpl;
import main.com.silicolife.textmining.patentpipeline.pubChemAPI.pugHelp.PUGRestInputEnum;
import main.com.silicolife.textmining.patentpipeline.pubChemAPI.pugHelp.PUGRestUtils;

public class PUGRestGetPatentIDsTester {


	//	@Test
	public void test1(){
		String identifier="5281239";
		Map<String, Set<String>> set = PUGRestUtils.getPatentIDsUsingCID(identifier);
		System.out.println(set);
		System.out.println(set.size());
		//		for (String a:set){
		//			System.out.println(a);
		//		}
	}


	//	@Test
	public void test2() throws UnsupportedEncodingException{
		String identifier="hydrochloric acid";
		Map<String, Set<String>> set = PUGRestUtils.getPatentIDsUsingCompoundName(identifier);
		System.out.println(set);
		System.out.println(set.size());
		//		for (String a:set){
		//			System.out.println(a);
		//		}
	}


	//	@Test
	public void test3() throws UnsupportedEncodingException{
		String identifier="sodium hydroxide";
		Map<String, Set<String>> set = PUGRestUtils.getPatentIDsUsingCompoundName(identifier);
		System.out.println(set);
		System.out.println(set.size());
		//		for (String a:set){
		//			System.out.println(a);
		//		}
	}



	//	@Test
	public void test4() throws WrongIRPatentIDRecoverConfigurationException, ANoteException{
		String identifier="fucoxanthin";
		PUGRestInputEnum inputType = PUGRestInputEnum.compoundName;
		IIRPubChemPatentIDRetrievalConfiguration configuration = new IRPubChemPatentIDRetrievalConfigurationImpl(inputType);
		IRPubChemPatentIDRetrieval pugRestSearch = new IRPubChemPatentIDRetrieval(configuration);
		IIRPatentPipelineSearchConfiguration query = new IRPatentPipelineSearchConfigurationImpl(identifier);
		Set<String> patentIds = pugRestSearch.retrievalPatentIds(query);
		System.out.println(patentIds.size());
		for(String patentID :patentIds)
			System.out.println(patentID);
	}

	//	@Test
	public void test5() throws WrongIRPatentIDRecoverConfigurationException, ANoteException{
		String identifier="Polyhydroxybutyrate";
		PUGRestInputEnum inputType = PUGRestInputEnum.compoundName;
		IIRPatentPipelineSearchConfiguration query = new IRPatentPipelineSearchConfigurationImpl(identifier);
		IIRPubChemPatentIDRetrievalConfiguration configuration = new IRPubChemPatentIDRetrievalConfigurationImpl(inputType,query);
		IRPubChemPatentIDRetrieval pugRestSearch = new IRPubChemPatentIDRetrieval(configuration);
		Set<String> patentIds = pugRestSearch.retrievalPatentIds(query);
		for(String patentID :patentIds)
			System.out.println(patentID);

		System.out.println(patentIds.size());
	}


	//	@Test
	public void test6() throws WrongIRPatentIDRecoverConfigurationException, ANoteException{
		String identifier="5281239,441";
		PUGRestInputEnum inputType = PUGRestInputEnum.compoundIdentifier;
		IIRPatentPipelineSearchConfiguration query = new IRPatentPipelineSearchConfigurationImpl(identifier);
		IIRPubChemPatentIDRetrievalConfiguration configuration = new IRPubChemPatentIDRetrievalConfigurationImpl(inputType,query);
		IRPubChemPatentIDRetrieval pugRestSearch = new IRPubChemPatentIDRetrieval(configuration);
		Set<String> patentIds = pugRestSearch.retrievalPatentIds(query);

		System.out.println(patentIds.size());
		for(String patentID :patentIds)
			System.out.println(patentID);
	}

	//	@Test
	public void test7() throws WrongIRPatentIDRecoverConfigurationException, ANoteException, IOException{
		String identifier="fucoxanthin";
		PUGRestInputEnum inputType = PUGRestInputEnum.compoundName;
		IIRPatentPipelineSearchConfiguration query = new IRPatentPipelineSearchConfigurationImpl(identifier);
		IIRPubChemPatentIDRetrievalConfiguration configuration = new IRPubChemPatentIDRetrievalConfigurationImpl(inputType,query);
		IRPubChemPatentIDRetrieval pugRestSearch = new IRPubChemPatentIDRetrieval(configuration);
		Set<String> patentIds = pugRestSearch.retrievalPatentIds(query);
		//				for(String patentID :patentIds)
		//					System.out.println(patentID);

		System.out.println(patentIds.size());
		Set<String> patentWebService = openCSV("resources/fucoxanthin_pubchem_webservice_patents.csv");
		System.out.println(patentWebService.size());
		int numNotweb = 0;
		int numNotSet = 0;
		for (String patnetSet:patentIds){
			System.out.println("patent from set:" + patnetSet);
		}

		for (String patent: patentIds){
			if (!patentWebService.contains(patent)){
				if (!existsOnSet(patent, patentWebService)){
					System.out.println("patent ID not in web: "+ numNotweb + ": " + patent);
					numNotweb++;
				}
			}
		}
		for (String patent:patentWebService){
			if (!patentIds.contains(patent)){
				if (!setExistsonSet(patent, patentIds)){
					System.out.println("patent web not in set: " + numNotSet + ": "+ patent);
					numNotSet++;
				}
			}
		}


	}


	private boolean existsOnSet(String patent, Set<String> patentSet){
		Set<String> possibleIDs = OPSUtils.createPatentIDPossibilities(patent);
		for (String patentPossible:possibleIDs){
			if (patentSet.contains(patentPossible)){
				return true;
			}

		}
		return false;
	}


	private boolean setExistsonSet(String patentWeb, Set<String> patentsRest){
		for (String patent:patentsRest){
			Set<String> patentPossible = OPSUtils.createPatentIDPossibilities(patent);
			if (patentPossible.contains(patentWeb)){
				return true;
			}
		}
		return false;
	}


	private Set<String> openCSV (String csvPath) throws IOException{
		Set<String> patentIDs=new HashSet<>();
		BufferedReader br = new BufferedReader(new FileReader(csvPath));
		String line;
		int lineNumber = 0;
		while ((line = br.readLine()) != null) {
			if (lineNumber>0){
				String[] patent = line.split(",");
				patentIDs.add(patent[0]);
			}
			lineNumber++;
		}
		br.close();
		return patentIDs;
	}

	//	@Test
	public void test8() throws WrongIRPatentIDRecoverConfigurationException, ANoteException{
		String identifier="SCHEMBL14996002";
		PUGRestInputEnum inputType = PUGRestInputEnum.compoundName;
		IIRPatentPipelineSearchConfiguration query = new IRPatentPipelineSearchConfigurationImpl(identifier);
		IIRPubChemPatentIDRetrievalConfiguration configuration = new IRPubChemPatentIDRetrievalConfigurationImpl(inputType,query);
		IRPubChemPatentIDRetrieval pugRestSearch = new IRPubChemPatentIDRetrieval(configuration);
		Set<String> patentIds = pugRestSearch.retrievalPatentIds(query);
		for(String patentID :patentIds)
			System.out.println(patentID);

		System.out.println(patentIds.size());
	}


	@Test
	public void test9() throws WrongIRPatentIDRecoverConfigurationException, ANoteException, WrongIRPatentMetaInformationRetrievalConfigurationException, PatentPipelineException, IOException{
		String identifier="fucoxanthin";
		PUGRestInputEnum inputType = PUGRestInputEnum.compoundName;
		IIRPatentPipelineSearchConfiguration query = new IRPatentPipelineSearchConfigurationImpl(identifier);
		IIRPubChemPatentIDRetrievalConfiguration configuration = new IRPubChemPatentIDRetrievalConfigurationImpl(inputType,query);
		IRPubChemPatentIDRetrieval pugRestSearch = new IRPubChemPatentIDRetrieval(configuration);
		Set<String> patentIds = pugRestSearch.retrievalPatentIds(query);

		PatentPipeline patentPipeline = new PatentPipeline();

		IProxy proxy = null;
		String accessTokenOPS = "LLCAsGwQHRQAi9sKU3L83tMcKszoVnhi:q9sxdjCvGbLDsWrc";
		IIRPatentMetaInformationRetrievalConfiguration configurationOPS=new IROPSPatentMetaInformationRetrievalConfigurationImpl(proxy, accessTokenOPS);
		IIRPatentMetainformationRetrievalSource opsMetaInformationretrieval = new OPSPatentMetaInformationRetrieval(configurationOPS);

		patentPipeline.addPatentsMetaInformationRetrieval(opsMetaInformationretrieval);


		Map<String, IPublication> patentMap =new HashMap<>();
		IIRPatentMetaInformationRetrievalReport reportMetaInformation = patentPipeline.executePatentRetrievalMetaInformationStep(patentIds);
		patentMap=reportMetaInformation.getMapPatentIDPublication();
		Set<String> toRemoveIDs=new HashSet<>();

		Set<String> allPossibleSolutions = getAllSetPossibilities(patentIds);
		
		
		for(String patentID:patentMap.keySet()){

			// Get ID from publication
			IPublication pub = patentMap.get(patentID);
			String pubpatentID = PublicationImpl.getPublicationExternalIDForSource(pub,PublicationSourcesDefaultEnum.patent.name());
			Set<String> pubPatentIDSALL = OPSUtils.createPatentIDPossibilities(pubpatentID);
			
			List<IPublicationExternalSourceLink> externalLinksList = pub.getPublicationExternalIDSource();
			Set<String> pubExternalIDs=new HashSet<>();
			for (IPublicationExternalSourceLink externaLink:externalLinksList){
				pubExternalIDs.add(externaLink.getSourceInternalId());
			}

			for (String externalID:pubExternalIDs){
				if (!(externalID.equalsIgnoreCase(pubpatentID) || pubPatentIDSALL.contains(externalID)) && existsOnSet(externalID, allPossibleSolutions)){
					//					patentMap.remove(externalID);
					toRemoveIDs.add(patentID);
				}
			}
		}
		for (String toRemoveID: toRemoveIDs){
			patentMap.remove(toRemoveID);
		}
		PrintWriter print = new PrintWriter("teste"+identifier+".txt");
		print.println(patentIds.size());
		Set<String> patentWebService = openCSV("resources/fucoxanthin_pubchem_webservice_patents.csv");
		print.println(patentWebService.size());

		int numNotweb = 0;
		int numNotSet = 0;
		for (String patnetSet:patentIds){
			print.println("patent from set:" + patnetSet);
		}

		for (String patent: patentIds){
			if (!patentWebService.contains(patent)){
				if (!existsOnSet(patent, patentWebService)){
					print.println("patent ID not in web: "+ numNotweb + ": " + patent);
					numNotweb++;
				}
			}
		}
		for (String patent:patentWebService){
			if (!patentIds.contains(patent)){
				if (!setExistsonSet(patent, patentIds)){
					print.println("patent web not in set: " + numNotSet + ": "+ patent);
					numNotSet++;
				}
			}
		}

		int i = 0;
		for (String patent: patentMap.keySet()){
			System.out.println("\n"+i+": "+patentMap.get(patent));
			print.println("\n"+i+": "+patentMap.get(patent));
			i++;

		}
		print.close();

	}

	private Set<String> getAllSetPossibilities(Set<String> patentIDs){
		Set<String> allPatentOptions=new HashSet<>();
		for (String patent:patentIDs){
			Set<String> patentPossible = OPSUtils.createPatentIDPossibilities(patent);
			allPatentOptions.addAll(patentPossible);
		}
		return allPatentOptions;
	}

}


