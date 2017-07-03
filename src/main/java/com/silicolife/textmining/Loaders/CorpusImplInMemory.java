package main.java.com.silicolife.textmining.Loaders;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Properties;

import com.silicolife.textmining.core.datastructures.documents.DocumentSetImpl;
import com.silicolife.textmining.core.datastructures.documents.corpus.CorpusStatisticsImpl;
import com.silicolife.textmining.core.datastructures.utils.GenerateRandomId;
import com.silicolife.textmining.core.datastructures.utils.conf.GlobalNames;
import com.silicolife.textmining.core.interfaces.core.document.IDocumentSet;
import com.silicolife.textmining.core.interfaces.core.document.IPublication;
import com.silicolife.textmining.core.interfaces.core.document.corpus.CorpusTextType;
import com.silicolife.textmining.core.interfaces.core.document.corpus.ICorpus;
import com.silicolife.textmining.core.interfaces.core.document.corpus.ICorpusStatistics;
import com.silicolife.textmining.core.interfaces.process.IE.IIEProcess;

public class CorpusImplInMemory extends Observable implements ICorpus {

	private long id;
	private String description;
	private String notes;
	private Properties properties;
	protected IDocumentSet documentSet;
	private List<IIEProcess> processesList;

	public CorpusImplInMemory() {
	}

	public CorpusImplInMemory(String description, String notes, Properties properties) {
		this.id = GenerateRandomId.generateID();
		this.description = description;
		this.notes = notes;
		this.properties = properties;
		this.documentSet = null;
		this.processesList=null;
	}

	public CorpusImplInMemory(long id, String description, String notes, Properties properties) {
		this.id = id;
		this.description = description;
		this.notes = notes;
		this.properties = properties;
		this.documentSet = null;
		this.processesList=null;
	}

	@Override
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@Override
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	@Override
	public Properties getProperties() {
		return properties;
	}

	public void setProperties(Properties properties) {
		this.properties = properties;
	}

	public List<IIEProcess> getProcessesList() {
		return processesList;
	}

	public void setProcessesList(List<IIEProcess> processesList) {
		this.processesList = processesList;
	}

	//		@JsonIgnore
	//		@Override
	//		public void addDocument(IPublication doc) throws ANoteException {
	//		}

	//		@JsonIgnore
	//		@Override
	//		public void registerProcess(IIEProcess ieProcess) throws ANoteException {
	//			InitConfiguration.getDataAccess().registerCorpusProcess(this, ieProcess);
	//		}

	@Override
	public synchronized IDocumentSet getArticlesCorpus() {
		//		if(documentSet==null)
		//		{
		//			documentSet = InitConfiguration.getDataAccess().getCorpusPublications(this);
		//		}
		return documentSet;
	}

	@Override
	public synchronized List<IIEProcess> getIEProcesses() {
		//		if(processesList==null)
		//		{
		//			processesList = InitConfiguration.getDataAccess().getCorpusProcesses(this);
		//		}
		return processesList;
	}

	@Override
	public ICorpusStatistics getCorpusStatistics(){
		Integer documents = documentSet.size();
		Integer processes = processesList.size();
		ICorpusStatistics corpusstatistics = new CorpusStatisticsImpl(documents, processes);
		return corpusstatistics;
	}


	@Override
	public CorpusTextType getCorpusTextType() {
		return CorpusTextType.convertStringToCorpusType(getProperties().getProperty(GlobalNames.textType));
	}

	public void freememory() {
		this.processesList=null;
		this.documentSet=null;
	}


	@Override
	public List<IIEProcess> getIEProcessesFilterByType(String type) {
		List<IIEProcess> result = new ArrayList<IIEProcess>();
		for(IIEProcess process:getIEProcesses())
		{
			if(process.getType().getType().equals(type))
			{
				//				Boolean hasPermission = InitConfiguration.getDataAccess().hasPermission(process, Permissions.getWritegrant());
				//				if (hasPermission) {
				result.add(process);
				//				}
			}
		}
		return result;
	}


	public void setArticlesCorpus (List<IPublication> publications){
		for (IPublication document: publications){
			addDocument(document);
		}

	}

	public void addDocument (IPublication pub){
		if (this.documentSet==null){
			documentSet= new DocumentSetImpl();
		}
		this.documentSet.addDocument(pub.getId(), pub);
	}

	public void addIEProcess (IIEProcess ieprocess){
		if (this.processesList==null){
			processesList=new ArrayList<>();
		}
		this.processesList.add(ieprocess);}


}

