package test.java.com.silicolife.textmining.patentpipeline.bulkData;

import java.util.Properties;

import org.junit.Test;

import com.silicolife.textmining.core.datastructures.exceptions.process.InvalidConfigurationException;
import com.silicolife.textmining.core.datastructures.init.exception.InvalidDatabaseAccess;
import com.silicolife.textmining.core.interfaces.core.configuration.IProxy;
import com.silicolife.textmining.core.interfaces.core.dataaccess.exception.ANoteException;
import com.silicolife.textmining.core.interfaces.core.report.processes.ir.IIRSearchProcessReport;
import com.silicolife.textmining.core.interfaces.process.IR.exception.InternetConnectionProblemException;
import com.silicolife.textmining.machinelearning.DatabaseConnectionInit;
import com.silicolife.textmining.processes.ir.patentpipeline.PatentPiplineSearch;
import com.silicolife.textmining.processes.ir.patentpipeline.components.metainfomodules.ops.IROPSPatentMetaInformationRetrievalConfigurationImpl;
import com.silicolife.textmining.processes.ir.patentpipeline.components.metainfomodules.ops.OPSPatentMetaInformationRetrieval;
import com.silicolife.textmining.processes.ir.patentpipeline.configuration.IIRPatentPipelineConfiguration;
import com.silicolife.textmining.processes.ir.patentpipeline.configuration.IIRPatentPipelineSearchConfiguration;
import com.silicolife.textmining.processes.ir.patentpipeline.configuration.IIRPatentPipelineSearchStepsConfiguration;
import com.silicolife.textmining.processes.ir.patentpipeline.configuration.IRPatentPipelineSearchConfigurationImpl;
import com.silicolife.textmining.processes.ir.patentpipeline.configuration.IRPatentPipelineSearchStepsConfigurationImpl;
import com.silicolife.textmining.processes.ir.patentpipeline.core.metainfomodule.IIRPatentMetaInformationRetrievalConfiguration;
import com.silicolife.textmining.processes.ir.patentpipeline.core.metainfomodule.IIRPatentMetainformationRetrievalSource;
import com.silicolife.textmining.processes.ir.patentpipeline.core.metainfomodule.WrongIRPatentMetaInformationRetrievalConfigurationException;
import com.silicolife.textmining.processes.ir.patentpipeline.core.searchmodule.IRPatentSearchConfigurationImpl;
import com.silicolife.textmining.processes.ir.patentpipeline.core.searchmodule.WrongIRPatentIDRecoverConfigurationException;

import main.java.com.silicolife.textmining.patentpipeline.pubChemAPI.IIRPubChemPatentIDRetrievalConfiguration;
import main.java.com.silicolife.textmining.patentpipeline.pubChemAPI.IRPubChemPatentIDRetrieval;
import main.java.com.silicolife.textmining.patentpipeline.pubChemAPI.IRPubChemPatentIDRetrievalConfigurationImpl;
import main.java.com.silicolife.textmining.patentpipeline.pubChemAPI.pugHelp.PUGRestInputEnum;

public class TestPUGRestInterlinkedWithAnote {


	@Test
	public void test1() throws WrongIRPatentMetaInformationRetrievalConfigurationException, WrongIRPatentIDRecoverConfigurationException, ANoteException, InvalidConfigurationException, InternetConnectionProblemException, InvalidDatabaseAccess{
		DatabaseConnectionInit.init("localhost", "3306", "anote2db", "root", "admin");



		String query="fucoxanthin";
		String queryName="Teste "+ query;
		String accessTokenOPS = "LLCAsGwQHRQAi9sKU3L83tMcKszoVnhi:q9sxdjCvGbLDsWrc";
		//		String accessTokenBing = "EncryptedAAAAABAAn/qUu/AmQ6dvCfzwtYg/jCAACnUS5ZfHWgOfg2qgoRSDjh3xjFsedr1vWXZWEB0++fwwADMo7OfVl9s//0M8zDE+dRnH85PbNJhggvhXM4KI81+c2wYgC/Je+6hddFOnfa+cyQ==";
		//		String accessTokenGoogle = "AIzaSyD60IrjYQBKEnotVTYWpcWTBVffY6l0XOU";
		//		String customSearchIDGoogle = "017027245643975829422:oimxfoxonzc";
		//		String usernameWIPO = "silicolife";
		//		String pwdWIPO = "zTi8iF0qh";

		IProxy proxy = null;
		Properties prop=null;



		IIRPatentPipelineSearchConfiguration patentPipelineSearchConfiguration = new IRPatentPipelineSearchConfigurationImpl(query);

 		PUGRestInputEnum inputType = PUGRestInputEnum.compoundName;
		IIRPubChemPatentIDRetrievalConfiguration configuration = new IRPubChemPatentIDRetrievalConfigurationImpl(inputType,patentPipelineSearchConfiguration);
		IRPubChemPatentIDRetrieval pugRestSearch = new IRPubChemPatentIDRetrieval(configuration);
		//		Set<String> patentIds = pugRestSearch.retrievalPatentIds(patentPipelineSearchConfiguration);

		//Step 1 - Retrived Patents Ids
		//		IIRPatentIDRetrievalModuleConfiguration configurationEPO = new IRPatentIDRetrievalEPOSearchConfigurationImpl(accessTokenOPS);
		//		IIRPatentIDRetrievalSource patentIDrecoverSourceEPO = new EPOSearchPatentIDRecoverSource(configurationEPO);

		//		IIRPatentIDRetrievalModuleConfiguration configurationBing = new IRPatentIDRetrievalBingSearchConfigurationImpl(accessTokenBing);
		//		IIRPatentIDRetrievalSource patentIDrecoverSourceBing = new BingSearchPatentIDRecoverSource(configurationBing);
		//
		//		IIRPatentIDRetrievalModuleConfiguration configurationGoogle = new IRPatentIDRetrievalGoogleSearchConfigurationImpl(accessTokenGoogle, customSearchIDGoogle);
		//		IIRPatentIDRetrievalSource patentIDrecoverSourceGoogle = new GoogleSearchPatentIDRecoverSource(configurationGoogle);

		//Step 2 - Retrived Meta Information	

		IIRPatentMetaInformationRetrievalConfiguration configurationOPS=new IROPSPatentMetaInformationRetrievalConfigurationImpl(proxy, accessTokenOPS);
		IIRPatentMetainformationRetrievalSource opsMetaInformationretrieval = new OPSPatentMetaInformationRetrieval(configurationOPS);

		//		IIRPatentMetaInformationRetrievalConfiguration configurationWIPO = new IRWIPOPatentMetaInformationRetrievalConfigurationImpl(usernameWIPO, pwdWIPO, proxy );
		//		IIRPatentMetainformationRetrievalSource wipoMetaInformationRetrieval = new WIPOPatentMetaInformationRetrieval(configurationWIPO);

		IIRPatentPipelineSearchStepsConfiguration configurationStepsPipeline=new IRPatentPipelineSearchStepsConfigurationImpl();
		//		configurationPipeline.addIRPatentIDRecoverSource(patentIDrecoverSourceEPO);
		//		configurationPipeline.addIRPatentIDRecoverSource(patentIDrecoverSourceBing);
		//		configurationPipeline.addIRPatentIDRecoverSource(patentIDrecoverSourceGoogle);
		//		configurationPipeline.addIRPatentRetrievalMetaInformation(wipoMetaInformationRetrieval);
		configurationStepsPipeline.addIRPatentIDRecoverSource(pugRestSearch);
		configurationStepsPipeline.addIRPatentRetrievalMetaInformation(opsMetaInformationretrieval);

		IIRPatentPipelineConfiguration configurationAllPipeline = new IRPatentSearchConfigurationImpl(patentPipelineSearchConfiguration,queryName,prop,configurationStepsPipeline);
		PatentPiplineSearch runnerIQueryMaker = new PatentPiplineSearch();
		IIRSearchProcessReport report=runnerIQueryMaker.search(configurationAllPipeline);


		System.out.println(report.getNumberOfDocuments());
		System.out.println(report.getTime());
	}



}
