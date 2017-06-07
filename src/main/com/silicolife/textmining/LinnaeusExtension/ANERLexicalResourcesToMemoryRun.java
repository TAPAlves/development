package main.com.silicolife.textmining.LinnaeusExtension;

import java.util.GregorianCalendar;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.silicolife.textmining.core.datastructures.documents.CorpusPublicationPaginatorImpl;
import com.silicolife.textmining.core.datastructures.exceptions.process.InvalidConfigurationException;
import com.silicolife.textmining.core.datastructures.report.processes.NERProcessReportImpl;
import com.silicolife.textmining.core.interfaces.core.dataaccess.exception.ANoteException;
import com.silicolife.textmining.core.interfaces.core.document.IAnnotatedDocument;
import com.silicolife.textmining.core.interfaces.core.document.ICorpusPublicationPaginator;
import com.silicolife.textmining.core.interfaces.core.document.corpus.ICorpus;
import com.silicolife.textmining.core.interfaces.core.report.processes.INERProcessReport;
import com.silicolife.textmining.core.interfaces.process.IE.IIEProcess;
import com.silicolife.textmining.core.interfaces.process.IE.INERProcess;
import com.silicolife.textmining.core.interfaces.process.IE.ner.INERConfiguration;
import com.silicolife.textmining.processes.ie.ner.datatstructures.INERPosProccessAddEntities;

public abstract class ANERLexicalResourcesToMemoryRun implements INERProcess {

	final static Logger nerlogger = LoggerFactory.getLogger(ANERLexicalResourcesToMemoryRun.class);
	private INERPosProccessAddEntities nerPosProccessAddEntities;

	public abstract IIEProcess buildProcess(INERConfiguration configuration);
	public abstract void executeNER(INERConfiguration configuration,INERProcessReport report,ICorpusPublicationPaginator publicationsPaginator,INERPosProccessAddEntities nerPosProccessAddEntities) throws ANoteException; 	
	//	public abstract INERConfiguration getProcessConfiguration(IIEProcess ieprocess,ProcessRunStatusConfigurationEnum processStatus) throws ANoteException;

	public INERProcessReport execute(INERConfiguration configuration) throws ANoteException, InvalidConfigurationException
	{
		switch (configuration.getProcessRunStatus()) {
		case createnew :
			return executeCorpusNER(configuration);
		case resume :
			//			return resumeCorpusNER(configuration);
			return null;
		case update :
			//			return updateNER(configuration);
			return null;
		case resumeupdate :
			//			return updateResumeNER(configuration);
			return null;
		default :
			return null;
		}
	}

	public INERProcessReport executeCorpusNER(INERConfiguration configuration) throws ANoteException, InvalidConfigurationException
	{
		validateConfiguration(configuration);
		IIEProcess processToRun = buildProcess(configuration);
		//		nerlogger.info("Created the NER process on DB");
		//		InitConfiguration.getDataAccess().createIEProcess(processToRun);
		//		nerlogger.info("Register NERProcess in Corpus");
		//		InitConfiguration.getDataAccess().registerCorpusProcess(processToRun.getCorpus(), processToRun);
		INERProcessReport report = new NERProcessReportImpl(configuration.getIEProcess().getName() + " report", processToRun);
		//		sINERProcessReport report = new NERProcessReportImpl(configuration.getIEProcess().getName() + " report", null);
		ICorpusPublicationPaginator publicationsPaginator = getPublicationsPaginator(configuration.getCorpus(),10000);
		long startime = GregorianCalendar.getInstance().getTimeInMillis();
		nerPosProccessAddEntities= new NERPosProcessAddEntitiesInMemoryImpl();
		executeNER(configuration,report,publicationsPaginator,nerPosProccessAddEntities);
		long endTime = GregorianCalendar.getInstance().getTimeInMillis();
		report.setTime(endTime-startime);
		return report;
	}

	public List<IAnnotatedDocument> getAllIAnnotatedDocuments () {
		return ((NERPosProcessAddEntitiesInMemoryImpl) nerPosProccessAddEntities).getAnnotatedDocs();
	}

	//	public INERProcessReport resumeCorpusNER(INERConfiguration configuration) throws ANoteException, InvalidConfigurationException
	//	{
	//		validateResumeConfiguration(configuration);
	//		IIEProcess processToResume = configuration.getIEProcess();
	//		nerlogger.info("Resume NER");
	//		INERProcessReport report = new NERProcessReportImpl(configuration.getIEProcess().getName() + " report", processToResume);
	//		ICorpusPublicationPaginator publicationsPaginator = getUnprocessedPublicationsPaginator(configuration.getIEProcess(),500000);
	//		long startime = GregorianCalendar.getInstance().getTimeInMillis();
	//		INERPosProccessAddEntities nerPosProccessAddEntities = new NERPosProcessAddEntitiesImpl();
	//		INERConfiguration processConfiguration = getProcessConfiguration(processToResume,configuration.getProcessRunStatus());
	//		processConfiguration.setIEProcess(processToResume);
	//		executeNER(processConfiguration, report, publicationsPaginator,nerPosProccessAddEntities);
	//		long endTime = GregorianCalendar.getInstance().getTimeInMillis();
	//		report.setTime(endTime-startime);
	//		return report;
	//	}
	//
	//	public INERProcessReport updateNER(INERConfiguration configuration) throws ANoteException, InvalidConfigurationException
	//	{
	//		validateUpdateConfiguration(configuration);
	//		IIEProcess processToUpdate = configuration.getIEProcess();
	//		nerlogger.info("Update NER");
	//		INERProcessReport report = new NERProcessReportImpl(configuration.getIEProcess().getName() + " report", processToUpdate);
	//		int version = processToUpdate.getVersion();
	//		processToUpdate.setVersion(++version);
	//		processToUpdate.setUpdateDate(new Date());
	//		ICorpusPublicationPaginator publicationsPaginator = getPublicationsPaginator(configuration.getCorpus(),10000);
	//		long startime = GregorianCalendar.getInstance().getTimeInMillis();
	//		INERPosProccessAddEntities nerPosProccessAddEntities = new NERPosProcessRemoveExistentEntitiesAddEntitiesImpl();
	//		INERConfiguration processConfiguration = getProcessConfiguration(processToUpdate,configuration.getProcessRunStatus());
	//		processConfiguration.setIEProcess(processToUpdate);
	//		executeNER(processConfiguration, report, publicationsPaginator,nerPosProccessAddEntities);
	//		long endTime = GregorianCalendar.getInstance().getTimeInMillis();
	//		report.setTime(endTime-startime);
	//		return report;
	//	}
	//
	//	/**
	//	 * TO DO
	//	 * 
	//	 */
	//	public INERProcessReport updateResumeNER(INERConfiguration configuration) throws ANoteException, InvalidConfigurationException
	//	{
	//		validateUpdateResumeConfiguration(configuration);
	//		IIEProcess processToUpdateResume = configuration.getIEProcess();
	//		nerlogger.info("Update Resume NER");
	//		INERProcessReport report = new NERProcessReportImpl(configuration.getIEProcess().getName() + " report", processToUpdateResume);
	//		ICorpusPublicationPaginator publicationsPaginator = getOutdatedPublicationsPaginator(processToUpdateResume,500000);
	//		long startime = GregorianCalendar.getInstance().getTimeInMillis();
	//		INERPosProccessAddEntities nerPosProccessAddEntities = new NERPosProcessRemoveExistentEntitiesAddEntitiesImpl();
	//		INERConfiguration processConfiguration = getProcessConfiguration(processToUpdateResume,configuration.getProcessRunStatus());
	//		processConfiguration.setIEProcess(processToUpdateResume);
	//		executeNER(processConfiguration, report, publicationsPaginator,nerPosProccessAddEntities);
	//		long endTime = GregorianCalendar.getInstance().getTimeInMillis();
	//		report.setTime(endTime-startime);
	//		return report;
	//	}

	//	public void validateResumeConfiguration(INERConfiguration configuration) throws InvalidConfigurationException {
	//		//		try {
	//		IIEProcess process = configuration.getIEProcess();
	//		if(process == null){
	//			throw new InvalidConfigurationException("The given process is not present in the database!");
	//		}
	//		//		} catch (ANoteException e) {
	//		//			throw new InvalidConfigurationException("The process cannot be resumed!");
	//		//		}
	//
	//	}

	//	public void validateUpdateConfiguration(INERConfiguration configuration) throws InvalidConfigurationException {
	//		this.validateResumeConfiguration(configuration);	
	//
	//	}

	//	public void validateUpdateResumeConfiguration(INERConfiguration configuration) throws InvalidConfigurationException {
	//		this.validateResumeConfiguration(configuration);	
	//
	//	}

	private ICorpusPublicationPaginator getPublicationsPaginator(ICorpus corpus,Integer pageframe) throws ANoteException {
		return new CorpusPublicationPaginatorImpl(corpus,pageframe);
	}


	//	private IIEProcess getProcessInDatabase(IIEProcess process) throws ANoteException{
	//		return InitConfiguration.getDataAccess().getProcessByID(process.getId());
	//	}

}

