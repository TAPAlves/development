package test.com.silicolife.textmining.patentpipeline.bulkData;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

import com.silicolife.textmining.core.datastructures.documents.PublicationImpl;
import com.silicolife.textmining.core.datastructures.documents.PublicationSourcesDefaultEnum;
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
		List<String> possibleIDs = OPSUtils.createPatentIDPossibilities(patent);
		for (String patentPossible:possibleIDs){
			if (patentSet.contains(patentPossible)){
				return true;
			}

		}
		return false;
	}


	private boolean setExistsonSet(String patentWeb, Set<String> patentsRest){
		for (String patent:patentsRest){
			List<String> patentPossible = OPSUtils.createPatentIDPossibilities(patent);
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

		Map<String, List<String>> allPossibleSolutions = getAllSetPossibilities(patentIds);
		Set<String> choosedPatents=new HashSet<>();



		for(String patentID:patentMap.keySet()){

			// Get ID from publication
			IPublication pub = patentMap.get(patentID);
			String pubpatentID = PublicationImpl.getPublicationExternalIDForSource(pub,PublicationSourcesDefaultEnum.patent.name());
			List<String> pubPatentIDSALL = OPSUtils.createPatentIDPossibilities(pubpatentID);

			List<IPublicationExternalSourceLink> externalLinksList = pub.getPublicationExternalIDSource();
			Set<String> pubExternalIDs=new HashSet<>();
			for (IPublicationExternalSourceLink externaLink:externalLinksList){
				String externalID = externaLink.getSourceInternalId();
				List<String> possext = OPSUtils.createPatentIDPossibilities(externalID);
				pubExternalIDs.addAll(possext);
			}

			for (String externalID:pubExternalIDs){
				if (!pubPatentIDSALL.contains(externalID) && 
						existsOnCollection(externalID, allPossibleSolutions.values()) && 
						!choosedPatents.contains(getKeyForAValue(allPossibleSolutions,externalID)) && 
						!verifyChoosedPatentsKindCode(getKeyForAValue(allPossibleSolutions,externalID), choosedPatents)){
					toRemoveIDs.add(getKeyForAValue(allPossibleSolutions,externalID));
				}
			}
			choosedPatents.add(pubpatentID);

		}
		for (String toRemoveID: toRemoveIDs){
			patentMap.remove(toRemoveID);
		}
		PrintWriter print = new PrintWriter("teste"+identifier+".txt");
		print.println("patentIDs: "+patentIds.size());
		Set<String> patentWebService = openCSV("resources/fucoxanthin_pubchem_webservice_patents.csv");
		print.println("web service: "+patentWebService.size());
		print.println("to Remove: " + toRemoveIDs.size());

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
		int numWeb = 0;
		for (String patent:patentWebService){
			print.println("patent web: " + numWeb + ": "+ patent);
			numWeb++;
		}
		
		
		

		int i = 0;
		for (String patent: patentMap.keySet()){
			System.out.println("\n"+i+": "+patentMap.get(patent));
			print.println("\n"+i+": "+patentMap.get(patent));
			i++;
		}
		Map<String, List<String>> webSolutions = getAllSetPossibilities(patentWebService);
		Map<String, List<String>> woRemoveIDs = getAllSetPossibilities(toRemoveIDs);
		Map<String, List<String>> patents = getAllSetPossibilities(patentMap.keySet());
		List<String> allPats=new ArrayList<>();
		List<String> allToRemPAts=new ArrayList<>();
		List<String> allWebPats=new ArrayList<>();
		for (String patent:patents.keySet()){
			allPats.addAll(patents.get(patent));
		}
		for (String patent:woRemoveIDs.keySet()){
			allToRemPAts.addAll(woRemoveIDs.get(patent));
		}
		for (String patent:webSolutions.keySet()){
			allWebPats.addAll(webSolutions.get(patent));
		}

		Set<String> toREmo=new HashSet<>();
		Set<String> toREmo1=new HashSet<>();
		for (String patent:patents.keySet()){
			for (String id:patents.get(patent)){
				if(allWebPats.contains(id)){
					toREmo.add(patent);
					for (String id1:webSolutions.keySet()){
						for (String id2:webSolutions.get(id1)){
							if (id2.equalsIgnoreCase(id)){
								//								webSolutions.remove(id1);
								toREmo1.add(id1);
							}

						}
					}

				}
			}
		}

		for (String toRemoveID: toREmo){
			patents.remove(toRemoveID);
		}
		for (String id1:toREmo1){
			webSolutions.remove(id1);
		}
		toREmo.clear();
		toREmo1.clear();


		for (String patent:woRemoveIDs.keySet()){
			for (String id:woRemoveIDs.get(patent)){
				if(allWebPats.contains(id)){
					toREmo.add(patent);
					for (String id1:webSolutions.keySet()){
						for (String id2:webSolutions.get(id1)){
							if (id2.equalsIgnoreCase(id)){
								toREmo1.add(id1);
							}

						}
					}
				}
			}
		}

		for (String toRemoveID: toREmo){
			woRemoveIDs.remove(toRemoveID);
		}


		for (String id1:toREmo1){
			webSolutions.remove(id1);
		}


		i=1;
		if (webSolutions.size()>0){
			for (String patent:webSolutions.keySet()){
				print.println(i + ": " + "resultant web solution: " + webSolutions.get(patent));
				i++;
			}

		}
		i=1;
		if (patents.size()>0){
			for (String patent:patents.keySet()){
				print.println(i + ": " +"resultant patent: " + patents.get(patent));
				i++;
			}
		}

		print.close();

	}


	private boolean verifyChoosedPatentsKindCode (String externalID, Set<String> choosedPatents) {
		List<String> possibleIDs = OPSUtils.createPatentIDPossibilities(externalID);
		Set<String> patentSet= new HashSet<>();
		for (String patent:choosedPatents){
			patentSet.addAll(OPSUtils.createPatentIDPossibilities(patent));
		}
		for (String patentPossible:possibleIDs){
			if (patentSet.contains(patentPossible)){
				return true;
			}
		}
		return false;
	}




	private String getKeyForAValue(Map<String, List<String>> allPossibleSolutions, String patentID){
		String keyString = new String();
		for (String key:allPossibleSolutions.keySet()){
			List<String> valueList = allPossibleSolutions.get(key);
			if (valueList.contains(patentID)){
				keyString=key;
			}
		}
		return keyString;
	}



	private boolean existsOnCollection(String patent, Collection<List<String>> patentLists) {
		List<String> possibleIDs = OPSUtils.createPatentIDPossibilities(patent);
		Set<String> patentSet= new HashSet<>();
		for (List<String> col: patentLists){
			patentSet.addAll(col);
		}
		for (String patentPossible:possibleIDs){
			if (patentSet.contains(patentPossible)){
				return true;
			}

		}
		return false;
	}


	private Map<String,List<String>> getAllSetPossibilities(Set<String> patentIDs){
		Map<String,List<String>> allOptions=new HashMap<>();
		for (String patent:patentIDs){
			List<String> patentPossible = OPSUtils.createPatentIDPossibilities(patent);
			allOptions.put(patent, patentPossible);
		}
		return allOptions;
	}

}


