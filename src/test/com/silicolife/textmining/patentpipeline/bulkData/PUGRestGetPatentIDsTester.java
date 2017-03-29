package test.com.silicolife.textmining.patentpipeline.bulkData;

import java.io.UnsupportedEncodingException;
import java.util.Set;

import org.junit.Test;

import com.silicolife.textmining.core.interfaces.core.dataaccess.exception.ANoteException;
import com.silicolife.textmining.processes.ir.patentpipeline.configuration.IIRPatentPipelineSearchConfiguration;
import com.silicolife.textmining.processes.ir.patentpipeline.configuration.IRPatentPipelineSearchConfigurationImpl;
import com.silicolife.textmining.processes.ir.patentpipeline.core.searchmodule.WrongIRPatentIDRecoverConfigurationException;

import main.com.silicolife.textmining.patentpipeline.PubChemAPI.IRPubChemPatentIDRetrieval;
import main.com.silicolife.textmining.patentpipeline.PubChemAPI.PUGHelp.IIRPubChemPatentIDRetrievalConfiguration;
import main.com.silicolife.textmining.patentpipeline.PubChemAPI.PUGHelp.IRPubChemPatentIDRetrievalConfigurationImpl;
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
		String identifier="fucoxanthin";
		PUGRestInputEnum inputType = PUGRestInputEnum.compoundName;
		IIRPatentPipelineSearchConfiguration query = new IRPatentPipelineSearchConfigurationImpl(identifier);
		IIRPubChemPatentIDRetrievalConfiguration configuration = new IRPubChemPatentIDRetrievalConfigurationImpl(inputType,query);
		IRPubChemPatentIDRetrieval pugRestSearch = new IRPubChemPatentIDRetrieval(configuration);
		Set<String> patentIds = pugRestSearch.retrievalPatentIds(query);
		
		System.out.println(patentIds.size());
		for(String patentID :patentIds)
			System.out.println(patentID);
	}
	
	
	@Test
	public void test6() throws WrongIRPatentIDRecoverConfigurationException, ANoteException{
		String identifier="5281239";
		PUGRestInputEnum inputType = PUGRestInputEnum.compoundIdentifier;
		IIRPatentPipelineSearchConfiguration query = new IRPatentPipelineSearchConfigurationImpl(identifier);
		IIRPubChemPatentIDRetrievalConfiguration configuration = new IRPubChemPatentIDRetrievalConfigurationImpl(inputType,query);
		IRPubChemPatentIDRetrieval pugRestSearch = new IRPubChemPatentIDRetrieval(configuration);
		Set<String> patentIds = pugRestSearch.retrievalPatentIds(query);
		
		System.out.println(patentIds.size());
		for(String patentID :patentIds)
			System.out.println(patentID);
	}

}


