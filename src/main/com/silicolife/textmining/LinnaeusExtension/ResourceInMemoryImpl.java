package main.com.silicolife.textmining.LinnaeusExtension;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Observable;
import java.util.Set;

import com.silicolife.textmining.core.datastructures.dataaccess.database.dataaccess.implementation.exceptions.ResourcesExceptions;
import com.silicolife.textmining.core.datastructures.dataaccess.database.dataaccess.implementation.exceptions.general.ExceptionsCodes;
import com.silicolife.textmining.core.datastructures.dataaccess.database.dataaccess.implementation.model.core.entities.Classes;
import com.silicolife.textmining.core.datastructures.dataaccess.database.dataaccess.implementation.model.core.entities.Resources;
import com.silicolife.textmining.core.datastructures.init.InitConfiguration;
import com.silicolife.textmining.core.datastructures.language.LanguageProperties;
import com.silicolife.textmining.core.datastructures.resources.ResourceElementSetImpl;
import com.silicolife.textmining.core.datastructures.resources.content.ResourceClassContentImpl;
import com.silicolife.textmining.core.datastructures.resources.content.ResourceClassesContentImpl;
import com.silicolife.textmining.core.datastructures.resources.content.ResourceContentImpl;
import com.silicolife.textmining.core.datastructures.utils.GenerateRandomId;
import com.silicolife.textmining.core.datastructures.utils.conf.GlobalOptions;
import com.silicolife.textmining.core.interfaces.core.dataaccess.exception.ANoteException;
import com.silicolife.textmining.core.interfaces.core.dataaccess.exception.DaemonException;
import com.silicolife.textmining.core.interfaces.core.dataaccess.layer.resources.IResourceManagerReport;
import com.silicolife.textmining.core.interfaces.core.general.IExternalID;
import com.silicolife.textmining.core.interfaces.core.general.classe.IAnoteClass;
import com.silicolife.textmining.core.interfaces.resource.IResource;
import com.silicolife.textmining.core.interfaces.resource.IResourceElement;
import com.silicolife.textmining.core.interfaces.resource.IResourceElementSet;
import com.silicolife.textmining.core.interfaces.resource.content.IResourceContent;

public class ResourceInMemoryImpl extends Observable implements IResource<IResourceElement>{

	/**
	 * Logger
	 */
	//		static Logger logger = Logger.getLogger(ResourceImpl.class.getName());

	private String name;
	private String info;
	private Long id;
	private boolean active;
	private String type;

	private List<IResourceElement> resources;


	/**
	 * Constructor that start a prepared statement to insert a resource element
	 * 
	 * @param db
	 * @param id
	 * @param name
	 * @param info
	 */
	public ResourceInMemoryImpl(long id,String name,String info,String type,boolean active)
	{
		this.id=id;
		if(name==null){this.name="";}
		else {this.name=name;}
		if(info==null) this.info="";
		else this.info=info;
		this.active = true;
		this.type=type;
	}

	public ResourceInMemoryImpl(String name,String info,String type,boolean active)
	{
		this(GenerateRandomId.generateID(), name, info,type, active);
	}	


	public ResourceInMemoryImpl() {
		super();
	}


	@Override
	public long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public String getName() {
		return name;
	}

	public void setName(String name){
		this.name = name;
	}

	@Override
	public String getInfo() {
		return info;
	}


	public void setInfo(String info) {
		this.info = info;
	}

	@Override
	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}


	public IResourceElementSet<IResourceElement> getResourceElementsByName(String name)
	{
		IResourceElementSet<IResourceElement> elementSet = new ResourceElementSetImpl<IResourceElement>();
		if (resources!=null && !resources.isEmpty()){
			for (IResourceElement resourceElement : resources) {
				if (resourceElement.getTerm().equalsIgnoreCase(name)){
					elementSet.addElementResource(resourceElement);
				}
			}
		}
		return elementSet;
	}


	public IResourceElementSet<IResourceElement> getResourceElementsByClass(IAnoteClass klass) throws ANoteException {
		IResourceElementSet<IResourceElement> elementSet = new ResourceElementSetImpl<IResourceElement>();
		if (resources!=null && !resources.isEmpty()){
			for (IResourceElement resourceElement : resources) {
				if (resourceElement.getTermClass().getName().equalsIgnoreCase(klass.getName())){
					elementSet.addElementResource(resourceElement);
				}
			}
		}
		return elementSet;
	}

	/**
	 * Method that return a List of resource elements of a resource
	 * 
	 * @return
	 */
	public IResourceElementSet<IResourceElement> getResourceElements(){
		IResourceElementSet<IResourceElement> elements = new ResourceElementSetImpl<>();
		elements.addAllElementResource(new HashSet<>(resources));
		return elements;
	}


	public Set<IAnoteClass> getResourceClassContent(){		
		Set<IAnoteClass> classes=new HashSet<>();
		if (resources!=null && !resources.isEmpty()){
			for (IResourceElement resourceElement : resources) {
				classes.add(resourceElement.getTermClass());
			}
		}
		return classes;
	}

	public IResourceElement getResourceElementByID(long termID)
	{

		if (resources!=null && !resources.isEmpty()){
			for (IResourceElement resourceElement : resources) {
				if (resourceElement.getId()==termID){
					return resourceElement;
				}
			}
		}
		return null;
		//		return InitConfiguration.getDataAccess().getResourceElementByID(termID);
	}


	@Override
	public synchronized IResourceContent getResourceContent(){
		return null;
	}

	/**
	 * Method that add a Resource Element
	 * 
	 * @param elem - Resource Element
	 * @return 
	 */
	public IResourceManagerReport addResourceElements(List<IResourceElement> elements) throws ANoteException
	{
		this.resources.addAll(elements);
		return null;
	}

	/**
	 * Update Element
	 * 
	 * @param elem
	 * @return
	 */
	public IResourceManagerReport updateResourceElement(IResourceElement elem){
//		return InitConfiguration.getDataAccess().updateResourceElement(elem);
		return null;
	}

	public void inactivateElement(IResourceElement elem) throws ANoteException
	{
	}

	public void inactiveResourceElementElementsByClassID(long classID) throws ANoteException {
	}

	/**
	 * Method that remove a Resource Element 
	 * 
	 * @param elem
	 * @return
	 */
	public void inactivateResourceElement(IResourceElement elem) throws ANoteException {
	}

	public void removeResourceElementSynonyms(IResourceElement elem) throws ANoteException{
		long elemID = elem.getId();
		IResourceElement memoryElement = getResourceElementByID(elemID);
		memoryElement.setSynonyms(new ArrayList<>());
	}

	public void removeResourceElementSynonym(IResourceElement elem,String synonym) throws ANoteException
	{
//		InitConfiguration.getDataAccess().removeResourceElementSynonym(elem, synonym);
	}

	protected void memoryAndProgress(int step, int total) {
		System.out.println((GlobalOptions.decimalformat.format((double)step/ (double) total * 100)) + " %...");
		Runtime.getRuntime().gc();
		System.out.println((Runtime.getRuntime().totalMemory()- Runtime.getRuntime().freeMemory())/(1024*1024) + " MB ");
	}

	public void notifyViewObservers(){
		setChanged();
		notifyObservers();
	}

	public static String convertClassesToResourceProperties(Set<Long> classes) {
		String classesStr = new String();
		for(Long classID:classes)
		{
			classesStr = classesStr.concat(classID+",");
		}
		if(classesStr.length() > 0)
			classesStr = classesStr.substring(0,classesStr.length()-1);
		return classesStr;
	}

	@Override
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void inactiveElementsByClassID(long classID) throws ANoteException {
		InitConfiguration.getDataAccess().removeResourceClass(this, classID);		
	}

	public void updateElement(IResourceElement element) throws ANoteException
	{
		InitConfiguration.getDataAccess().updateResourceElement(element);
	}

	public int compareTo(IResource<IResourceElement> resource)
	{
		if(this.getId()==resource.getId())
		{
			return 0;
		}
		else if(this.getId()<=resource.getId())
		{
			return -1;
		}
		return 1;
	}

	public boolean checkiftermalreadyexist(String term) throws ANoteException {
		return InitConfiguration.getDataAccess().checkResourceElementExistsInResource(this, term);
	}

	public IResourceManagerReport addExternalID(IResourceElement elem, List<IExternalID> externalIDs) throws ANoteException {
		return InitConfiguration.getDataAccess().addResourceElementExternalIds(this,elem, externalIDs);
	}

	public String toString()
	{
		String info = new String();
		info = getType() + " : " + getName() + " (ID :"+ getId() + " ) ";
		if(!getInfo().equals(""))
		{
			info = info + LanguageProperties.getLanguageStream("pt.uminho.anote2.general.notes")+": "+getInfo();
		}
		if(!isActive())
		{
			info = info + " ("+LanguageProperties.getLanguageStream("pt.uminho.anote2.general.inactive")+") ";
		}
		return info;
	}

	public boolean isFill() throws ANoteException {
		return InitConfiguration.getDataAccess().getResourceContent(this).getTermNumber()!=0;
	}
}


