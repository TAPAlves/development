package com.silicolife.textmining.patentpipeline.searchSourcesModule.pubChemAPI;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import com.silicolife.textmining.core.interfaces.core.dataaccess.exception.ANoteException;
import com.silicolife.textmining.patentpipeline.searchSourcesModule.pubChemAPI.pugHelp.PUGRestInputEnum;
import com.silicolife.textmining.patentpipeline.searchSourcesModule.pubChemAPI.pugHelp.PUGRestUtils;
import com.silicolife.textmining.processes.ir.patentpipeline.configuration.IIRPatentPipelineSearchConfiguration;
import com.silicolife.textmining.processes.ir.patentpipeline.core.searchmodule.AIRPatentIDRecoverSource;
import com.silicolife.textmining.processes.ir.patentpipeline.core.searchmodule.IIRPatentIDRetrievalModuleConfiguration;
import com.silicolife.textmining.processes.ir.patentpipeline.core.searchmodule.WrongIRPatentIDRecoverConfigurationException;

public class IRPubChemPatentIDRetrieval extends AIRPatentIDRecoverSource{

	public IRPubChemPatentIDRetrieval(IIRPatentIDRetrievalModuleConfiguration configuration)
			throws WrongIRPatentIDRecoverConfigurationException {
		super(configuration);
	}

	@Override
	public Set<String> retrievalPatentIds(IIRPatentPipelineSearchConfiguration configuration) throws ANoteException {
		PUGRestInputEnum inputType = ((IIRPubChemPatentIDRetrievalConfiguration)getConfiguration()).getInputType();
		Set<String> patentIDs = new HashSet<>();
		if (inputType.name().equalsIgnoreCase("compoundName")){
			Map<String, Set<String>> patentIDsForAllIdentifiers = PUGRestUtils.getPatentIDsUsingCompoundName(configuration.getQuery());
			for (String key:patentIDsForAllIdentifiers.keySet()){
				patentIDs.addAll(patentIDsForAllIdentifiers.get(key));
			}
		}
		else if (inputType.name().equalsIgnoreCase("compoundIdentifier")){
			Map<String, Set<String>> patentIDsForAllIdentifiers = PUGRestUtils.getPatentIDsUsingCID(configuration.getQuery());
			for (String key:patentIDsForAllIdentifiers.keySet()){
				patentIDs.addAll(patentIDsForAllIdentifiers.get(key));
			}
		}
		return patentIDs;
	}

	@Override
	public String getSourceName() {
		return PUGRestUtils.PUGRestSearch;
	}

	@Override
	public int getNumberOfResults() throws ANoteException {
		return -1;
	}

	@Override
	public void validate(IIRPatentIDRetrievalModuleConfiguration configuration)
			throws WrongIRPatentIDRecoverConfigurationException {
		if(configuration instanceof IIRPubChemPatentIDRetrievalConfiguration)
		{
			IIRPubChemPatentIDRetrievalConfiguration configurationPUGRestSearch = (IIRPubChemPatentIDRetrievalConfiguration) configuration;
			if(configurationPUGRestSearch.getInputType()==null || configurationPUGRestSearch.getInputType().name().isEmpty())
			{
				throw new WrongIRPatentIDRecoverConfigurationException("The input type for the PUG Rest system can not be null or empty!");
			}
			if(!(configurationPUGRestSearch.getPipelineConfiguration()==null)){
				if (configurationPUGRestSearch.getPipelineConfiguration().getQuery()==null || configurationPUGRestSearch.getPipelineConfiguration().getQuery().isEmpty()){
					throw new WrongIRPatentIDRecoverConfigurationException("The input keywords can not be empty!");
				}
				if (configurationPUGRestSearch.getInputType().name().equalsIgnoreCase("compoundName")){
					Pattern pat= Pattern.compile("[a-z]+");			
					if (!pat.matcher(configurationPUGRestSearch.getPipelineConfiguration().getQuery().toLowerCase()).find()){
						throw new WrongIRPatentIDRecoverConfigurationException("The compound name is incorrectly defined!");
					}

				}
				if (configurationPUGRestSearch.getInputType().name().equalsIgnoreCase("compoundIdentifier")){
					try{
						String[] intList = configurationPUGRestSearch.getPipelineConfiguration().getQuery().split(",");
						for (String intger:intList){
							Integer.parseInt(intger);
						}

					}catch (NumberFormatException e) {
						throw new WrongIRPatentIDRecoverConfigurationException("The compound identifier must be an non-zero integer!");
					}
				}

				String responseMessage = PUGRestUtils.verifyValideInputResponse(configurationPUGRestSearch.getPipelineConfiguration().getQuery());
				if (!(responseMessage==null || responseMessage.isEmpty())){
					throw new WrongIRPatentIDRecoverConfigurationException("The introduced compound is not valid: " + responseMessage);					
				}
			}
		}
		else
			throw new WrongIRPatentIDRecoverConfigurationException("Configuration is not a IRPubChemPatentIDRetrievalConfiguration");
	}	

}