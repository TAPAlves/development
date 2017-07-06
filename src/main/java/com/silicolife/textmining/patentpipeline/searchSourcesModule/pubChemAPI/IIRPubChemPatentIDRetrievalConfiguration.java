package main.java.com.silicolife.textmining.patentpipeline.searchSourcesModule.pubChemAPI;

import com.silicolife.textmining.processes.ir.patentpipeline.configuration.IIRPatentPipelineSearchConfiguration;
import com.silicolife.textmining.processes.ir.patentpipeline.core.searchmodule.IIRPatentIDRetrievalModuleConfiguration;

import main.java.com.silicolife.textmining.patentpipeline.searchSourcesModule.pubChemAPI.pugHelp.PUGRestInputEnum;

public interface IIRPubChemPatentIDRetrievalConfiguration extends IIRPatentIDRetrievalModuleConfiguration {

	//	public String getKeywords();	

	public PUGRestInputEnum getInputType();	

	public IIRPatentPipelineSearchConfiguration getPipelineConfiguration();

}
