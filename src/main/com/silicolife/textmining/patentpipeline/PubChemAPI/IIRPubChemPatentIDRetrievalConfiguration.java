package main.com.silicolife.textmining.patentpipeline.PubChemAPI;

import com.silicolife.textmining.processes.ir.patentpipeline.configuration.IIRPatentPipelineSearchConfiguration;
import com.silicolife.textmining.processes.ir.patentpipeline.core.searchmodule.IIRPatentIDRetrievalModuleConfiguration;

import main.com.silicolife.textmining.patentpipeline.PubChemAPI.PUGHelp.PUGRestInputEnum;

public interface IIRPubChemPatentIDRetrievalConfiguration extends IIRPatentIDRetrievalModuleConfiguration {

	//	public String getKeywords();	

	public PUGRestInputEnum getInputType();	

	public IIRPatentPipelineSearchConfiguration getPipelineConfiguration();

}
