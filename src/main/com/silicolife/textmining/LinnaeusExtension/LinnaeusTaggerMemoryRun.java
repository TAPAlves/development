package main.com.silicolife.textmining.LinnaeusExtension;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.silicolife.textmining.core.datastructures.annotation.AnnotationPosition;
import com.silicolife.textmining.core.datastructures.annotation.AnnotationPositions;
import com.silicolife.textmining.core.datastructures.annotation.ner.EntityAnnotationImpl;
import com.silicolife.textmining.core.datastructures.exceptions.process.InvalidConfigurationException;
import com.silicolife.textmining.core.datastructures.general.ClassPropertiesManagement;
import com.silicolife.textmining.core.datastructures.process.ProcessOriginImpl;
import com.silicolife.textmining.core.datastructures.process.ner.ElementToNer;
import com.silicolife.textmining.core.datastructures.process.ner.HandRules;
import com.silicolife.textmining.core.datastructures.process.ner.NERCaseSensativeEnum;
import com.silicolife.textmining.core.datastructures.process.ner.ResourcesToNerAnote;
import com.silicolife.textmining.core.datastructures.textprocessing.EntitiesDesnormalization;
import com.silicolife.textmining.core.datastructures.textprocessing.TermSeparator;
import com.silicolife.textmining.core.datastructures.utils.GenerateRandomId;
import com.silicolife.textmining.core.datastructures.utils.Utils;
import com.silicolife.textmining.core.datastructures.utils.conf.GlobalNames;
import com.silicolife.textmining.core.datastructures.utils.conf.GlobalOptions;
import com.silicolife.textmining.core.interfaces.core.annotation.IEntityAnnotation;
import com.silicolife.textmining.core.interfaces.core.dataaccess.exception.ANoteException;
import com.silicolife.textmining.core.interfaces.core.document.ICorpusPublicationPaginator;
import com.silicolife.textmining.core.interfaces.core.document.IDocumentSet;
import com.silicolife.textmining.core.interfaces.core.document.IPublication;
import com.silicolife.textmining.core.interfaces.core.general.classe.IAnoteClass;
import com.silicolife.textmining.core.interfaces.core.report.processes.INERProcessReport;
import com.silicolife.textmining.core.interfaces.process.IProcessOrigin;
import com.silicolife.textmining.core.interfaces.process.IE.IIEProcess;
import com.silicolife.textmining.core.interfaces.process.IE.ner.INERConfiguration;
import com.silicolife.textmining.core.interfaces.resource.IResourceElement;
import com.silicolife.textmining.core.interfaces.resource.lexicalwords.ILexicalWords;
import com.silicolife.textmining.processes.ie.ner.datatstructures.INERPosProccessAddEntities;
import com.silicolife.textmining.processes.ie.ner.linnaeus.LinnaeusTagger;
import com.silicolife.textmining.processes.ie.ner.linnaeus.PublicationIt;
import com.silicolife.textmining.processes.ie.ner.linnaeus.adapt.martin.common.compthreads.IteratorBasedMaster;
import com.silicolife.textmining.processes.ie.ner.linnaeus.adapt.uk.ac.man.documentparser.dataholders.Document;
import com.silicolife.textmining.processes.ie.ner.linnaeus.adapt.uk.ac.man.documentparser.input.DocumentIterator;
import com.silicolife.textmining.processes.ie.ner.linnaeus.adapt.uk.ac.man.entitytagger.Mention;
import com.silicolife.textmining.processes.ie.ner.linnaeus.adapt.uk.ac.man.entitytagger.doc.TaggedDocument;
import com.silicolife.textmining.processes.ie.ner.linnaeus.adapt.uk.ac.man.entitytagger.matching.Matcher;
import com.silicolife.textmining.processes.ie.ner.linnaeus.adapt.uk.ac.man.entitytagger.matching.matchers.ConcurrentMatcher;
import com.silicolife.textmining.processes.ie.ner.linnaeus.adapt.uk.ac.man.entitytagger.matching.matchers.MatchPostProcessor;
import com.silicolife.textmining.processes.ie.ner.linnaeus.adapt.uk.ac.man.entitytagger.matching.matchers.UnionMatcher;
import com.silicolife.textmining.processes.ie.ner.linnaeus.adapt.uk.ac.man.entitytagger.matching.matchers.VariantDictionaryMatcher;
import com.silicolife.textmining.processes.ie.ner.linnaeus.configuration.INERLinnaeusConfiguration;
import com.silicolife.textmining.processes.ie.ner.linnaeus.configuration.LinnauesExecutionData;

public class LinnaeusTaggerMemoryRun extends ANERLexicalResourcesToMemoryRun{

	public static final String linneausTagger = "Linnaeus Tagger";
	public static final String abreviation = "Abbreviation";
	public static final String disambiguation = "Disambiguation";

	final static Logger nerlogger = LoggerFactory.getLogger(LinnaeusTagger.class);

	private boolean stop = false;

	public static final IProcessOrigin linnausOrigin= new ProcessOriginImpl(GenerateRandomId.generateID(),linneausTagger);


	public LinnaeusTaggerMemoryRun() {

	}

	public void executeNER(INERConfiguration configuration,INERProcessReport report,ICorpusPublicationPaginator publicationsPaginator,INERPosProccessAddEntities nerPosProccessAddEntities) throws ANoteException 
	{	
		stop = false;
		INERLinnaeusConfiguration linnauesConfiguration = (INERLinnaeusConfiguration) configuration;
		LinnauesExecutionData linnauesExecutionData = loadExecutionData(linnauesConfiguration);
		Matcher matcher = getMatcher(linnauesConfiguration,linnauesExecutionData.getElements());
		long startime = GregorianCalendar.getInstance().getTimeInMillis();	
		int size = (int) (long) publicationsPaginator.getPublicationsCount();
		long measuretime5 = GregorianCalendar.getInstance().getTimeInMillis();	
		long toprint5 = startime - measuretime5;
		nerlogger.error("M1-M0 :" + toprint5);

		int counter = 0; 
		long measuretime4 = GregorianCalendar.getInstance().getTimeInMillis();
		boolean run = true;
		while(run){
			long measuretime1 = GregorianCalendar.getInstance().getTimeInMillis();
			long toprint4 = measuretime1 - measuretime4;
			nerlogger.error("M4-M1 :" + toprint4);
			IDocumentSet documentSet = publicationsPaginator.nextDocumentSetPage();
			if(documentSet.size() > 0)
			{
				long measuretime2 = GregorianCalendar.getInstance().getTimeInMillis();
				long toprint = measuretime2 - measuretime1;
				nerlogger.error("M2-M1 :" + toprint);
				DocumentIterator documents = getDocumentIterator(configuration, configuration.getIEProcess(), documentSet);
				long measuretime3 = GregorianCalendar.getInstance().getTimeInMillis();
				long toprint2 = measuretime3 - measuretime2;
				nerlogger.error("M3-M2 :" + toprint2);
				counter = executeLinneausForDocumentSet(linnauesConfiguration, configuration.getIEProcess(), nerPosProccessAddEntities, startime,linnauesExecutionData, matcher, report, documents, size, counter,documentSet);
				measuretime4 = GregorianCalendar.getInstance().getTimeInMillis();
				long toprint3 = measuretime4 - measuretime3;
				nerlogger.error("M4-M3 :" + toprint3);
			}
			else
				run = false;
		}
	}

	protected LinnauesExecutionData loadExecutionData(INERLinnaeusConfiguration linnauesConfiguration) throws ANoteException
	{
		nerlogger.info("Getting resource elements to NER");
		ElementToNer elementsToNER = getElementsToNER(linnauesConfiguration);
		HandRules rules = new HandRules(elementsToNER);
		List<IEntityAnnotation> elements = elementsToNER.getTermsByAlphabeticOrder(linnauesConfiguration.getCaseSensitiveEnum());
		Map<Long, Long> resourceMapClass = elementsToNER.getResourceMapClass();
		Map<Long, IResourceElement> resourceIDMapResource = elementsToNER.getMapResourceIDsToResourceElements();
		Map<String, Set<Long>> maplowerCaseToPossibleResourceIDs = elementsToNER.getMaplowerCaseToPossibleResourceIDs();
		Map<Long, String> mapPossibleResourceIDsToTermString = elementsToNER.getMapPossibleResourceIDsToTermString();
		Set<String> stopwords = loadStopWords(linnauesConfiguration);
		nerlogger.info("Finished to get resources to NER");
		return new LinnauesExecutionData(elementsToNER, rules, elements, resourceMapClass, resourceIDMapResource, maplowerCaseToPossibleResourceIDs, mapPossibleResourceIDsToTermString, stopwords);
	}

	//public void resumeNER(INERConfiguration configuration,INERProcessReport report,ICorpusPublicationPaginator publicationsPaginator,INERPosProccessAddEntities nerPosProccessAddEntities) throws ANoteException {
	//	INERLinnaeusConfiguration linnauesConfiguration = (INERLinnaeusConfiguration) convertProcessToConfiguration(configuration.getIEProcess(),ProcessRunStatusConfigurationEnum.resume);
	//	long startime = GregorianCalendar.getInstance().getTimeInMillis();
	//	nerlogger.info("Start to get resources elements on DB");
	//	ElementToNer elementsToNER = getElementsToNER(linnauesConfiguration);
	//	HandRules rules = new HandRules(elementsToNER);
	//	List<IEntityAnnotation> elements = elementsToNER.getTermsByAlphabeticOrder(linnauesConfiguration.getCaseSensitiveEnum());
	//	Map<Long, Long> resourceMapClass = elementsToNER.getResourceMapClass();
	//	Map<Long, IResourceElement> resourceIDMapResource = elementsToNER.getMapResourceIDsToResourceElements();
	//	Map<String, Set<Long>> maplowerCaseToPossibleResourceIDs = elementsToNER.getMaplowerCaseToPossibleResourceIDs();
	//	Map<Long, String> mapPossibleResourceIDsToTermString = elementsToNER.getMapPossibleResourceIDsToTermString();
	//	Set<String> stopwords = loadStopWords(linnauesConfiguration);
	//	Matcher matcher = getMatcher(linnauesConfiguration,elements);
	//	nerlogger.info("Finished to get resources elements on DB");		
	//	int size = (int) (long) publicationsPaginator.getPublicationsCount();
	//	int counter = 0; 
	//	while(publicationsPaginator.hasNextDocumentSetPage()){
	//		IDocumentSet documentSet = publicationsPaginator.nextDocumentSetPage();
	//		DocumentIterator documents = new PublicationIt(configuration.getCorpus(), documentSet, configuration.getIEProcess());
	//		
	//		counter = executeLinneausForDocumentSet(linnauesConfiguration, configuration.getIEProcess(), nerPosProccessAddEntities, startime, elementsToNER, rules,
	//				resourceMapClass, resourceIDMapResource, maplowerCaseToPossibleResourceIDs,
	//				mapPossibleResourceIDsToTermString, stopwords, matcher, report, documents, size, counter);
	//	}
	//	if(stop)
	//	{
	//		report.setcancel();
	//	}
	//}

	private DocumentIterator getDocumentIterator(INERConfiguration configuration, IIEProcess processToRun,
			IDocumentSet documentSet) throws ANoteException {
		DocumentIterator documents = new PublicationIt(configuration.getCorpus(), documentSet, processToRun);
		return documents;
	}

	protected  ElementToNer getElementsToNER(INERLinnaeusConfiguration linnauesConfiguration) throws ANoteException {
		ElementToNer elementsToNER = new ElementToNer(linnauesConfiguration.getResourceToNER(), linnauesConfiguration.isNormalized());
		elementsToNER.processingINfo();
		return elementsToNER;
	}

	private Integer executeLinneausForDocumentSet(INERLinnaeusConfiguration linnauesConfiguration, IIEProcess processToRun,INERPosProccessAddEntities nerPosProccessAddEntities,
			long startime,LinnauesExecutionData linnaeusExecutionData, Matcher matcher, INERProcessReport report,
			DocumentIterator documents, Integer publicationsSize, Integer counter, IDocumentSet documentSet) throws ANoteException {

		ConcurrentMatcher tm = new ConcurrentMatcher(matcher,documents);
		IteratorBasedMaster<TaggedDocument> master = new IteratorBasedMaster<TaggedDocument>(tm,linnauesConfiguration.getNumberOfThreads());
		Thread threadmaster = new Thread(master);
		threadmaster.start();

		while (master.hasNext() && !stop){
			TaggedDocument td = master.next();
			report.incrementDocument();
			if (td != null && !stop)
			{
				String strid = td.getOriginal().getID();
				Long id = Long.valueOf(strid);
				AnnotationPositions positions = new AnnotationPositions();
				AnnotationPositions positionsRules = new AnnotationPositions();

				addMatchesToAnnotationPositions(linnauesConfiguration, linnaeusExecutionData, td,
						positions);
				applyHandRulesToAnnotationPositions(linnaeusExecutionData, td, positionsRules);
				saveAnnotatedDocumentWithAnnotationPositions(linnauesConfiguration, processToRun, report, nerPosProccessAddEntities, td, id,
						positions,positionsRules, documentSet);
			}
			counter++;
			memoryAndProgress(counter,publicationsSize,startime);		
		}
		try {
			threadmaster.join();
		} catch (InterruptedException e) {
			throw new ANoteException(e);
		}
		return counter;
	}



	public static Set<String> loadStopWords(INERLinnaeusConfiguration linnauesConfiguration) throws ANoteException {
		Set<String> stopwords = new HashSet<String>();
		if(linnauesConfiguration.getStopWords()!=null)
		{
			ILexicalWords st = linnauesConfiguration.getStopWords();
			Set<String> stopwordsTmp = st.getLexicalWords().keySet();
			if(linnauesConfiguration.getCaseSensitiveEnum().equals(NERCaseSensativeEnum.INALLWORDS)){
				stopwords = stopwordsTmp;
			}else {
				for(String word:stopwordsTmp){
					if(linnauesConfiguration.getCaseSensitiveEnum().equals(NERCaseSensativeEnum.NONE)){
						stopwords.add(word.toLowerCase());
					}else if(linnauesConfiguration.getCaseSensitiveEnum().equals(NERCaseSensativeEnum.ONLYINSMALLWORDS)
							&& word.length()> linnauesConfiguration.getCaseSensitiveEnum().getSmallWordSize()){
						stopwords.add(word.toLowerCase());
					}else{
						stopwords.add(word);
					}
				}
			}
		}
		return stopwords;
	}

	protected void addMatchesToAnnotationPositions(INERLinnaeusConfiguration linnauesConfiguration,
			LinnauesExecutionData linnauesExecutionData , TaggedDocument td,
			AnnotationPositions positions) throws ANoteException {
		List<Mention> matches = td.getAllMatches();
		for(Mention men:matches){
			String text = men.getText();
			if(!isInStopWords(linnauesExecutionData.getStopwords(), text, linnauesConfiguration)){
				if(linnauesConfiguration.getCaseSensitiveEnum().equals(NERCaseSensativeEnum.INALLWORDS)){
					addAnnotationWithCaseSensitive(linnauesExecutionData.getResourceMapClass(), linnauesExecutionData.getResourceIDMapResource(), positions, men, text);
				}else{
					addAnnotationWithoutCaseSensitive(linnauesExecutionData.getResourceMapClass(), linnauesExecutionData.getResourceIDMapResource(),
							linnauesExecutionData.getMaplowerCaseToPossibleResourceIDs(), linnauesExecutionData.getMapPossibleResourceIDsToTermString(), positions, men,
							text);
				}
			}
		}
	}


	protected void addAnnotationWithCaseSensitive(Map<Long, Long> resourceMapClass,
			Map<Long, IResourceElement> resourceIDMapResource, AnnotationPositions positions, Mention men, String text)
					throws ANoteException {
		long dicEntityID = Long.valueOf(men.getIds()[0]);
		Long classID = resourceMapClass.get(dicEntityID);
		String dictTerm = men.getIds()[1];
		IAnoteClass klass = getIAnoteClass(classID);
		IEntityAnnotation entityAnnotation = new EntityAnnotationImpl(men.getStart(), men.getEnd(), klass , resourceIDMapResource.get(dicEntityID), text,men.isAbbreviation() ,false, new Properties());
		positions.addAnnotationWhitConflitsAndReplaceIfRangeIsMore(new AnnotationPosition(men.getStart(), men.getEnd(), dictTerm, text), entityAnnotation);
	}

	protected IAnoteClass getIAnoteClass(Long classID) throws ANoteException {
		IAnoteClass klass = ClassPropertiesManagement.getClassGivenClassID(classID);
		return klass;
	}

	private void  addAnnotationWithoutCaseSensitive(Map<Long, Long> resourceMapClass,
			Map<Long, IResourceElement> resourceIDMapResource, Map<String, Set<Long>> maplowerCaseToPossibleResourceIDs,
			Map<Long, String> mapPossibleResourceIDsToTermString, AnnotationPositions positions, Mention men,
			String text) throws ANoteException {
		Set<Long> resourceIDs = maplowerCaseToPossibleResourceIDs.get(text.toLowerCase());
		if(resourceIDs == null){
			resourceIDs = maplowerCaseToPossibleResourceIDs.get(men.getIds()[1].toLowerCase());
		}
		if(resourceIDs == null){
			resourceIDs = maplowerCaseToPossibleResourceIDs.get(text);
		}
		if(resourceIDs == null){
			resourceIDs = maplowerCaseToPossibleResourceIDs.get(men.getIds()[1]);
		}
		for(Long resourceID : resourceIDs){
			Long classID = resourceMapClass.get(resourceID);
			String dictTerm = mapPossibleResourceIDsToTermString.get(resourceID);
			IAnoteClass klass = getIAnoteClass(classID);
			IEntityAnnotation entityAnnotation = new EntityAnnotationImpl(men.getStart(), men.getEnd(), klass , resourceIDMapResource.get(resourceID), text, men.isAbbreviation(),false, new Properties());
			positions.addAnnotationWhitConflitsAndReplaceIfRangeIsMore(new AnnotationPosition(men.getStart(), men.getEnd(), dictTerm, text), entityAnnotation);
		}
	}

	private static boolean isInStopWords(Set<String> stopwords, String text, INERLinnaeusConfiguration linnauesConfiguration){
		if(!stopwords.isEmpty()){
			if(linnauesConfiguration.getCaseSensitiveEnum().equals(NERCaseSensativeEnum.INALLWORDS) ||
					(linnauesConfiguration.getCaseSensitiveEnum().equals(NERCaseSensativeEnum.ONLYINSMALLWORDS) 
							&& text.length()<=linnauesConfiguration.getCaseSensitiveEnum().getSmallWordSize())){
				if(stopwords.contains(text)){
					return true;
				}
			}else if(linnauesConfiguration.getCaseSensitiveEnum().equals(NERCaseSensativeEnum.NONE) ||
					linnauesConfiguration.getCaseSensitiveEnum().equals(NERCaseSensativeEnum.ONLYINSMALLWORDS) 
					&& text.length()>linnauesConfiguration.getCaseSensitiveEnum().getSmallWordSize()){
				if(stopwords.contains(text.toLowerCase())){
					return true;
				}
			}
		}
		return false;
	}


	protected void applyHandRulesToAnnotationPositions(LinnauesExecutionData linnaeusExecutionData, TaggedDocument td,
			AnnotationPositions positions) throws ANoteException {
		if(linnaeusExecutionData.getElementsToNER().getRules()!=null && !linnaeusExecutionData.getElementsToNER().getRules().isEmpty())
		{
			if(linnaeusExecutionData.getRules() != null)
				linnaeusExecutionData.getRules().applyRules(td.getOriginal().getRawContent(), positions);	
		}		
	}


	private void saveAnnotatedDocumentWithAnnotationPositions(INERLinnaeusConfiguration linnauesConfiguration,
			IIEProcess processToRun, INERProcessReport report,INERPosProccessAddEntities nerPosProccessAddEntities,TaggedDocument td, Long id,
			AnnotationPositions positions, AnnotationPositions positionsRules, IDocumentSet documentSet) throws ANoteException {
		if(!stop)
		{
			report.incrementEntitiesAnnotated(positions.getAnnotations().size());
			List<IEntityAnnotation> entityAnnotations = positions.getEntitiesFromAnnoattionPositions();
//			List<IPublicationExternalSourceLink> publicationExternalIDSource = new ArrayList<IPublicationExternalSourceLink>();
//			List<IPublicationField> publicationFields = new ArrayList<>();
//			List<IPublicationLabel> publicationLabels = new ArrayList<>();
			IPublication document = documentSet.getDocument(id);
//			IPublication document =  new PublicationImpl(id,
//					"", "", "", "", "",
//					"", "", "", "", "", "",
//					"", false, "", "",
//					publicationExternalIDSource ,
//					publicationFields ,
//					publicationLabels );

			entityAnnotations = correctEntitiesAfterNormalization(linnauesConfiguration, td, entityAnnotations);
			AnnotationPositions annotationsPositionsResult = new AnnotationPositions();
			for(IEntityAnnotation entityAnnotation:entityAnnotations)
			{
				AnnotationPosition position = new AnnotationPosition((int) entityAnnotation.getStartOffset(),(int) entityAnnotation.getEndOffset());
				annotationsPositionsResult.addAnnotationWhitConflitsAndReplaceIfRangeIsMore(position, entityAnnotation);
			}
			// Add Rules Entities
			List<IEntityAnnotation> entityAnnotationsRules = positionsRules.getEntitiesFromAnnoattionPositions();
			for(IEntityAnnotation entityAnnotationsRule:entityAnnotationsRules)
			{
				AnnotationPosition position = new AnnotationPosition((int) entityAnnotationsRule.getStartOffset(),(int) entityAnnotationsRule.getEndOffset());
				annotationsPositionsResult.addAnnotationWhitConflitsAndReplaceIfRangeIsMore(position, entityAnnotationsRule);
			}
			entityAnnotations = annotationsPositionsResult.getEntitiesFromAnnoattionPositions();
			// Add Document Entity Annotations
			nerPosProccessAddEntities.addAnnotatedDocumentEntities(processToRun, document, entityAnnotations);
			
			System.out.println(entityAnnotations);
			
			
		}
	}

	//	private boolean addAnnotatedDocumentEntities (INERPosProccessAddEntities nerPosProccessAddEntities, IIEProcess process,IPublication document,List<IEntityAnnotation> newEntityAnnotations){
	//		annotationService.addCorpusProcessDocumentEntityAnootations(schema.getCorpus().getId(), schema.getId(), document.getId(), entityAnnotations);

	//		Corpus corpus = process.getCorpus();
	//		if (corpus == null)
	//			throw new AnnotationException(ExceptionsCodes.codeNoCorpus, ExceptionsCodes.msgNoCorpus);
	//		Processes processes = process.getprocessManagerDao.getProcessesDao().findById(processId);
	//		if (processes == null)
	//			throw new AnnotationException(ExceptionsCodes.codeNoProcess, ExceptionsCodes.msgNoProcess);
	//		IPublication publications = corpusManagerDao.getPublicationsDao().findById(documentID);
	//		if (publications == null)
	//			throw new AnnotationException(ExceptionsCodes.codeNoPublication, ExceptionsCodes.msgNoPublication);
	//		for(IEntityAnnotation entityAnnotation: newEntityAnnotations)
	//		{
	//			if(entityAnnotation.getClassAnnotation()==null)
	//			{
	//				throw new AnnotationException(ExceptionsCodes.codeNoNullClass, ExceptionsCodes.msgNoNullClass);
	//			}
	//
	//			Annotations annot = AnnotationsWrapper.convertToDeamonStructure(entityAnnotation,corpus,processes,publications);
	//			Classes klass = resourceManagerDao.getClassesDao().findUniqueByAttribute("claName", entityAnnotation.getClassAnnotation().getName());
	//			if(klass==null)
	//			{
	//				resourceManagerDao.getClassesDao().save(annot.getClasses());
	//			}
	//			else
	//			{
	//				annot.setClasses(klass);
	//			}
	//			annotationManagerdao.getAnnotationsDao().save(annot);
	//			Set<AnnotationProperties> annotationPropertiess = annot.getAnnotationPropertieses();
	//			for (AnnotationProperties annotationProperty : annotationPropertiess) {
	//				annotationManagerdao.getAnnotationPropertiesDao().save(annotationProperty);
	//			}
	//		}
	//		
	//		/*
	//		 * Document in corpus is processed
	//		 */
	//		addDocumentInCorpusAsProcessed(corpusId, processId, documentID, processes);
	//		
	//		/*
	//		 * log
	//		 */
	//		AuthUsers user = userLogged.getCurrentUserLogged();
	//		AuthUserLogs log = new AuthUserLogs(user, new Date(), "create", "annotations", null, "Add Annotations");
	//		usersManagerDao.getAuthUserLogsDao().save(log);
	//		
	//		return true;
	//	}





	protected List<IEntityAnnotation> correctEntitiesAfterNormalization(INERLinnaeusConfiguration linnauesConfiguration,
			TaggedDocument td, List<IEntityAnnotation> entityAnnotations) {
		if(linnauesConfiguration.isNormalized()){
			Document linnausDocument = td.getOriginal();
			EntitiesDesnormalization desnormalizer = new EntitiesDesnormalization(linnausDocument.getRawContent(), linnausDocument.getBody(), entityAnnotations);
			entityAnnotations = desnormalizer.getDesnormalizedAnnotations();
		}
		return entityAnnotations;
	}





	private static Properties gereateProperties(INERLinnaeusConfiguration configurations) {
		Properties properties = transformResourcesToOrderMapInProperties(configurations.getResourceToNER());
		if(configurations.isUseAbreviation()){
			properties.put(LinnaeusTagger.abreviation, "true");
		} else {
			properties.put(LinnaeusTagger.abreviation, "false");
		}
		properties.put(LinnaeusTagger.disambiguation, configurations.getDisambiguation().name());
		properties.put(GlobalNames.casesensitive, configurations.getCaseSensitiveEnum().name());
		if(configurations.isNormalized()){
			properties.put(GlobalNames.normalization, "true");
		} else {
			properties.put(GlobalNames.normalization, "false");
		}
		if(configurations.getStopWords()!=null && configurations.getStopWords().getId() > 0)
		{
			properties.put(GlobalNames.nerpreProcessing,GlobalNames.stopWords);
			properties.put(GlobalNames.stopWordsResourceID,String.valueOf(configurations.getStopWords().getId()));
		}
		else
		{
			properties.put(GlobalNames.nerpreProcessing,GlobalNames.nerpreProcessingNo);
		}
		if(configurations.isUsingOtherResourceInfoToImproveRuleAnnotations())
		{
			properties.put(GlobalNames.useOtherResourceInformationInRules,"true");
		}
		if(configurations.getSizeOfSmallWordsToBeNotAnnotated()>0){
			properties.put(GlobalNames.sizeOfNonAnnotatedSmallWords, String.valueOf(configurations.getSizeOfSmallWordsToBeNotAnnotated()));
		}
		properties.put(GlobalNames.numberThreads, configurations.getNumberOfThreads());
		return properties;
	}

	public IIEProcess buildProcess(INERConfiguration configuration) {
		INERLinnaeusConfiguration linnauesConfiguration = (INERLinnaeusConfiguration) configuration;
		String description = LinnaeusTagger.linneausTagger  + " " +Utils.SimpleDataFormat.format(new Date());
		Properties properties = gereateProperties(linnauesConfiguration);
		IIEProcess processToRun = configuration.getIEProcess();
		processToRun.setProperties(properties);
		processToRun.setName(description);
		return processToRun;
	}

	/**
	 * returns a entity recognition Matcher based on the input parameters in ap (provided by the user on the command-line or in a configuration file)
	 * @param elements 
	 * @param ap an ArgParser object containing arguments used to construct the matcher
	 * @param logger A logger to which information messages will be logged. Nothing will be logged if this is null.
	 * @param tag Allows the use of multiple different matchers. For example, if tag == "Genes", properties will need to have the postfix "Genes" to be read. This allows the method to be called multiple times with different tags for different matching options.
	 * @return a matcher that can be used to find and normalize species names in text
	 * @throws DatabaseLoadDriverException 
	 * @throws SQLException 
	 */
	protected  Matcher getMatcher(INERLinnaeusConfiguration linnaeusConfiguration,List<IEntityAnnotation> elements) throws ANoteException{
		List<Matcher> matchers = new ArrayList<Matcher>();
		if(!linnaeusConfiguration.getCaseSensitiveEnum().equals(NERCaseSensativeEnum.ONLYINSMALLWORDS)){
			getMatchersForNoneSmallWordCaseSensitive(linnaeusConfiguration,elements, matchers);
		}else{
			getMatchersForSmallWordCaseSensitive(linnaeusConfiguration,elements, matchers);
		}

		if (matchers.size() == 0){
			return null;
		}
		Matcher matcher = matchers.size() == 1 ? matchers.get(0) : new UnionMatcher(matchers, true);
		matcher = new MatchPostProcessor(matcher, linnaeusConfiguration.getDisambiguation(), linnaeusConfiguration.isUseAbreviation(),null);
		matcher.match("test", new Document("none",null,null,null,null,null,null,null,null,null,null,null,null,null,null));
		return matcher;
	}

	private static void getMatchersForSmallWordCaseSensitive(INERLinnaeusConfiguration linnaeusConfiguration,List<IEntityAnnotation> elements, List<Matcher> matchers) {
		List<String[]> biggerTermsToIdsMapList = new ArrayList<>();
		List<String[]> smallTermsToIdsMapList = new ArrayList<>();
		List<String> biggerTerms = new ArrayList<>();
		List<String> smallTerms = new ArrayList<>();
		for(IEntityAnnotation elem : elements){
			String term = elem.getAnnotationValue();
			String[] termToIdsMapArray = new String[2];
			termToIdsMapArray[0] = String.valueOf(elem.getResourceElement().getId());
			termToIdsMapArray[1] = elem.getAnnotationValue();
			if(linnaeusConfiguration.isNormalized()){
				term = TermSeparator.termSeparator(elem.getAnnotationValue()).trim();
			}
			if(term.length()>linnaeusConfiguration.getCaseSensitiveEnum().getSmallWordSize()){
				biggerTermsToIdsMapList.add(termToIdsMapArray);
				term = term.toLowerCase();
				biggerTerms.add(term);
			}else{
				smallTermsToIdsMapList.add(termToIdsMapArray);
				smallTerms.add(term);
			}
		}
		matchers.add(new VariantDictionaryMatcher(biggerTermsToIdsMapList.toArray(new String[0][2]), biggerTerms.toArray(new String[0]), true));
		matchers.add(new VariantDictionaryMatcher(smallTermsToIdsMapList.toArray(new String[0][2]), smallTerms.toArray(new String[0]), false));
	}

	private static void getMatchersForNoneSmallWordCaseSensitive(INERLinnaeusConfiguration linnaeusConfiguration,List<IEntityAnnotation> elements, List<Matcher> matchers) {
		String[][] termToIdsMapArray = new String[elements.size()][2] ;
		String[] terms = new String[elements.size()];
		int i=0;
		for(IEntityAnnotation elem : elements){			
			termToIdsMapArray[i][0] = String.valueOf(elem.getResourceElement().getId());
			termToIdsMapArray[i][1] = elem.getAnnotationValue();
			terms[i] = elem.getAnnotationValue();
			if(linnaeusConfiguration.isNormalized())
				terms[i] = TermSeparator.termSeparator(elem.getAnnotationValue()).trim();
			if(linnaeusConfiguration.getCaseSensitiveEnum().equals(NERCaseSensativeEnum.NONE))
				terms[i] = terms[i].toLowerCase();
			i++;
		}
		if(linnaeusConfiguration.getCaseSensitiveEnum().equals(NERCaseSensativeEnum.NONE)){
			matchers.add(new VariantDictionaryMatcher(termToIdsMapArray, terms, true));
		}else{
			matchers.add(new VariantDictionaryMatcher(termToIdsMapArray, terms, false));
		}
	}

	protected void memoryAndProgress(int step, int total,long startime) {
		if(step%50==0)
		{
			System.out.println((GlobalOptions.decimalformat.format((double)step/ (double) total * 100)) + " %...");
			nerlogger.info((GlobalOptions.decimalformat.format((double)step/ (double) total * 100)) + " %...");
			//		if(step%1000==0)
			//		{
			//			Runtime.getRuntime().gc();
			//			System.out.println((Runtime.getRuntime().totalMemory()- Runtime.getRuntime().freeMemory())/(1024*1024) + " MB ");
			//		}
		}
	}

	public static TaggedDocument matchDocument(Matcher matcher, Document doc){
		String rawText = doc.toString();

		List<Mention> matches = matcher.match(rawText, doc);

		if (matches == null)
			return new TaggedDocument(doc,null,null,matches,rawText);

		for (Mention m : matches){
			m.setDocid(doc.getID());
		}

		if (doc.isIgnoreCoordinates()){
			for (int i = 0; i < matches.size(); i++){
				Mention m = matches.get(i);
				m.setStart(-1);
				m.setEnd(-1);
			}
		}

		return new TaggedDocument(doc,null,null,matches,rawText);
	}

	@Override
	public void stop() {
		this.stop=true;
	}

	@Override
	public void validateConfiguration(INERConfiguration configuration)throws InvalidConfigurationException {
		if(configuration instanceof INERLinnaeusConfiguration)
		{
			INERLinnaeusConfiguration linnaeusConfiguration = (INERLinnaeusConfiguration) configuration;
			if(linnaeusConfiguration.getCorpus()==null)
			{
				throw new InvalidConfigurationException("Corpus can not be null");
			}
		}
		else
			throw new InvalidConfigurationException("configuration must be INERLexicalResourcesConfiguration isntance");
	}

	private static Properties transformResourcesToOrderMapInProperties(ResourcesToNerAnote resources) {
		Properties prop = new Properties();
		for(int i=0;i<resources.getList().size();i++)
		{
			Set<Long> selected = resources.getList().get(i).getSelectedClassesID();
			long id = resources.getList().get(i).getResource().getId();
			{
				prop.put(String.valueOf(id),ResourceInMemoryImpl.convertClassesToResourceProperties(selected));
			}
		}
		return prop;
	}




	//	public INERConfiguration getProcessConfiguration(IIEProcess ieprocess,ProcessRunStatusConfigurationEnum processStatus) throws ANoteException{
	//		ICorpus corpus = ieprocess.getCorpus();
	//		Map<String, Pattern> patterns = null;
	//		boolean useabreviation = false;
	//		Disambiguation disambiguationEnum = Disambiguation.OFF;
	//		NERCaseSensativeEnum caseSensitiveEnum = NERCaseSensativeEnum.NONE;
	//		boolean normalized = false;
	//		int numThreads = 4;
	//		ILexicalWords stopwords = null;
	//		NERLinnaeusPreProcessingEnum preprocessing = NERLinnaeusPreProcessingEnum.No;
	//		boolean usingOtherResourceInfoToImproveRuleAnnotations = false;
	//		int sizeOfSmallWordsToBeNotAnnotated = 0;
	//
	//		Properties propertiesToConvert = ieprocess.getProperties();
	//		Map<Long, Set<Long>> mapResourceIDToClassesID = new HashMap<>();
	//		for( Object key : propertiesToConvert.keySet()){
	//			String keyString = String.valueOf(key);
	//			Long resourceID = null;
	//			try{
	//				resourceID = Long.valueOf(keyString);
	//			}catch(Exception e){}
	//			if(resourceID != null){
	//				Object classes = propertiesToConvert.get(key);
	//				String classesString = String.valueOf(classes);
	//				String[] classesIdString = classesString.split(",");
	//				Set<Long> klassIDs = new HashSet<>();
	//				for(String klassID : classesIdString){
	//					klassIDs.add(Long.valueOf(klassID));
	//				}
	//				mapResourceIDToClassesID.put(resourceID, klassIDs);
	//			}else{
	//				Object value = propertiesToConvert.get(key);
	//				if(keyString.equals(LinnaeusTagger.abreviation))
	//					useabreviation = Boolean.valueOf(String.valueOf(value));
	//
	//				if(keyString.equals(LinnaeusTagger.disambiguation))
	//					disambiguationEnum = Disambiguation.valueOf(String.valueOf(value));
	//
	//				if(keyString.equals(GlobalNames.casesensitive))
	//					caseSensitiveEnum = NERCaseSensativeEnum.valueOf(String.valueOf(value));
	//
	//				if(keyString.equals(GlobalNames.normalization))
	//					normalized = Boolean.valueOf(String.valueOf(value));
	//
	//				if(keyString.equals(GlobalNames.useOtherResourceInformationInRules))
	//					usingOtherResourceInfoToImproveRuleAnnotations = true;
	//				if(keyString.equals(GlobalNames.numberThreads))
	//					numThreads = Integer.valueOf(String.valueOf(value));
	//				//				if(keyString.equals(GlobalNames.stopWordsResourceID))
	//				//				{
	//				//					stopwords = new LexicalWordsImpl(InitConfiguration.getDataAccess().getResourceByID(Long.valueOf(String.valueOf(value))));
	//				//					preprocessing = NERLinnaeusPreProcessingEnum.StopWords;
	//				//				}
	//				if(keyString.equals(GlobalNames.sizeOfNonAnnotatedSmallWords))
	//					sizeOfSmallWordsToBeNotAnnotated = Integer.valueOf(String.valueOf(value));
	//			}
	//		}
	//
	//		ResourcesToNerAnote resourceToNER = getResourcesToNERForConfiguration(caseSensitiveEnum, usingOtherResourceInfoToImproveRuleAnnotations, sizeOfSmallWordsToBeNotAnnotated, mapResourceIDToClassesID);
	//		return new NERLinnaeusConfigurationImpl(corpus,processStatus, patterns, resourceToNER, useabreviation, disambiguationEnum, caseSensitiveEnum, normalized, numThreads, stopwords, preprocessing, usingOtherResourceInfoToImproveRuleAnnotations,sizeOfSmallWordsToBeNotAnnotated);
	//	}

	//	private ResourcesToNerAnote getResourcesToNERForConfiguration(NERCaseSensativeEnum caseSensitiveEnum,
	//			boolean usingOtherResourceInfoToImproveRuleAnnotations, int sizeOfSmallWordsToBeNotAnnotated, Map<Long, Set<Long>> mapResourceIDToClassesID) throws ANoteException {
	//		ResourcesToNerAnote resourceToNER = new ResourcesToNerAnote(caseSensitiveEnum, usingOtherResourceInfoToImproveRuleAnnotations, sizeOfSmallWordsToBeNotAnnotated);
	//		for(Long resource : mapResourceIDToClassesID.keySet()){
	//
	//			IResource<IResourceElement> resElem = InitConfiguration.getDataAccess().getResourceByID(resource);
	//			Set<Long> selectedClass = mapResourceIDToClassesID.get(resource);
	//			Set<Long> classContent = selectedClass;
	//			resourceToNER.add(resElem, classContent, selectedClass);
	//		}
	//		return resourceToNER;
	//	}





}
