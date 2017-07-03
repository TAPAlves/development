package main.java.com.silicolife.textmining.patentpipeline.pubChemAPI;

import main.java.com.silicolife.textmining.patentpipeline.pubChemAPI.pugHelp.PUGRestInputEnum;
import com.silicolife.textmining.processes.ir.patentpipeline.configuration.IIRPatentPipelineSearchConfiguration;
import com.silicolife.textmining.processes.ir.patentpipeline.core.searchmodule.IIRPatentIDRetrievalModuleConfiguration;

public interface IIRPubChemPatentIDRetrievalConfiguration extends IIRPatentIDRetrievalModuleConfiguration {

	//	public String getKeywords();	

	public PUGRestInputEnum getInputType();	

	public IIRPatentPipelineSearchConfiguration getPipelineConfiguration();

}
