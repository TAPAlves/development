package test.com.silicolife.textmining.patentpipeline.bulkData;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashSet;
import java.util.Set;

import javax.management.InstanceNotFoundException;

import org.junit.Test;

import com.silicolife.textmining.core.interfaces.core.dataaccess.exception.ANoteException;
import com.silicolife.textmining.processes.ir.epopatent.OPSUtils;
import com.silicolife.textmining.processes.ir.patentpipeline.configuration.IIRPatentPipelineSearchConfiguration;
import com.silicolife.textmining.processes.ir.patentpipeline.configuration.IRPatentPipelineSearchConfigurationImpl;
import com.silicolife.textmining.processes.ir.patentpipeline.core.searchmodule.WrongIRPatentIDRecoverConfigurationException;

import main.com.silicolife.textmining.patentpipeline.PubChemAPI.IIRPubChemPatentIDRetrievalConfiguration;
import main.com.silicolife.textmining.patentpipeline.PubChemAPI.IRPubChemPatentIDRetrieval;
import main.com.silicolife.textmining.patentpipeline.PubChemAPI.IRPubChemPatentIDRetrievalConfigurationImpl;
import main.com.silicolife.textmining.patentpipeline.PubChemAPI.PUGHelp.PUGRestInputEnum;
import main.com.silicolife.textmining.patentpipeline.PubChemAPI.PUGHelp.PUGRestUtils;

public class PUGRestGetPatentIDsTester {


	//	@Test
	public void test1(){
		String identifier="5281239";
		Set<String> set = PUGRestUtils.getPatentIDsUsingCID(identifier);
		System.out.println(set);
		System.out.println(set.size());
		for (String a:set){
			System.out.println(a);
		}
	}


	//	@Test
	public void test2() throws UnsupportedEncodingException{
		String identifier="hydrochloric acid";
		Set<String> set = PUGRestUtils.getPatentIDsUsingCompoundName(identifier);
		System.out.println(set);
		System.out.println(set.size());
		for (String a:set){
			System.out.println(a);
		}
	}


	//	@Test
	public void test3() throws UnsupportedEncodingException{
		String identifier="sodium hydroxide";
		Set<String> set = PUGRestUtils.getPatentIDsUsingCompoundName(identifier);
		System.out.println(set);
		System.out.println(set.size());
		for (String a:set){
			System.out.println(a);
		}
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

	@Test
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
}


