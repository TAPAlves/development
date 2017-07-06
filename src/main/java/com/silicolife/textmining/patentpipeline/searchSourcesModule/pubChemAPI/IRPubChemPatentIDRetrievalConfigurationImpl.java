package main.java.com.silicolife.textmining.patentpipeline.searchSourcesModule.pubChemAPI;

import com.silicolife.textmining.processes.ir.patentpipeline.configuration.IIRPatentPipelineSearchConfiguration;

import main.java.com.silicolife.textmining.patentpipeline.searchSourcesModule.pubChemAPI.pugHelp.PUGRestInputEnum;

public class IRPubChemPatentIDRetrievalConfigurationImpl implements IIRPubChemPatentIDRetrievalConfiguration{

	PUGRestInputEnum inputType;
	IIRPatentPipelineSearchConfiguration pipelineconfiguration;
	
	public IRPubChemPatentIDRetrievalConfigurationImpl(PUGRestInputEnum inputType) {
		this.inputType=inputType;

	}
	
	public IRPubChemPatentIDRetrievalConfigurationImpl(PUGRestInputEnum inputType, IIRPatentPipelineSearchConfiguration pipelineconfiguration ) {
		this.inputType=inputType;
		this.pipelineconfiguration=pipelineconfiguration;

	}

	
	
	
	@Override
	public PUGRestInputEnum getInputType() {
		return inputType;
	}


	//	public String keywords;
	//
	//	public IRPubChemPatentIDRetrievalConfigurationImpl(String keywords) {
	//		this.keywords=keywords;
	//
	//	}
	//
	//	@Override
	//	public String getKeywords() {
	//		return keywords;
	//	}

	@Override
	public IIRPatentPipelineSearchConfiguration getPipelineConfiguration() {
		return pipelineconfiguration;
	}

}
