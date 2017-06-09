package com.silicolife.textmining.dictionaryLoader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.silicolife.textmining.core.datastructures.dataaccess.database.schema.TableResourcesElements;
import com.silicolife.textmining.core.datastructures.general.AnoteClass;
import com.silicolife.textmining.core.datastructures.general.ExternalIDImpl;
import com.silicolife.textmining.core.datastructures.general.SourceImpl;
import com.silicolife.textmining.core.datastructures.resources.ResourceElementImpl;
import com.silicolife.textmining.core.datastructures.utils.generic.CSVFileConfigurations;
import com.silicolife.textmining.core.interfaces.core.dataaccess.exception.ANoteException;
import com.silicolife.textmining.core.interfaces.core.general.IExternalID;
import com.silicolife.textmining.core.interfaces.core.general.classe.IAnoteClass;
import com.silicolife.textmining.core.interfaces.resource.IResourceElement;

public class JoChemDictionaryTSVLoader{

	private BufferedReader br;

	private Map<String, IResourceElement> alreadyAddedElemnt;

	public JoChemDictionaryTSVLoader() {
		this.alreadyAddedElemnt = new HashMap<>();
	}

	public List<IResourceElement> loadTermFromGenericCVSFile(File file,CSVFileConfigurations csvfileconfigurations) throws ANoteException,
	IOException {
		//		long startTime = GregorianCalendar.getInstance().getTimeInMillis();
		List<IResourceElement> listResources = importCVSFile(file,csvfileconfigurations);
		//		long endTime = GregorianCalendar.getInstance().getTimeInMillis();
		//		if(cancel)
		//			report.setcancel();
		//		report.setTime(endTime-startTime);
		return listResources;

	}

	public List<IResourceElement> importCVSFile(File file,CSVFileConfigurations csvfileconfigurations) throws ANoteException, IOException {	

		if(file==null)
		{
			throw new IOException("File is null");
		}
		else if(!file.exists())
		{
			throw new IOException("File not exists");
		}
		else
		{
			String line;
			//			int step = 0;
			//			int total = FileHandling.getFileLines(file);
			String term;
			FileReader fr = new FileReader(file);
			br = new BufferedReader(fr);
			while((line = br.readLine())!=null)
			{
				String[] lin = line.split(csvfileconfigurations.getGeneralDelimiter().getValue());
				term = getTerm(lin,csvfileconfigurations);
				if(term != null && term.length()>=TableResourcesElements.mimimumElementSize && term.length()<TableResourcesElements.elementSize)
				{
					String klass = getClass(lin,csvfileconfigurations);
					if(klass != null && klass.length()>=TableResourcesElements.mimimumClasseElementSize && klass.length()<TableResourcesElements.classeSize)
					{
						Set<String> synList = getSynonyms(lin,csvfileconfigurations);
						List<IExternalID> extLis = getExternalIds(lin,csvfileconfigurations);
						getElementToBeAdded(term, klass, synList, extLis, 0);

					}
				}
				//				step++;
			}
		}

		return new ArrayList<>(alreadyAddedElemnt.values());
	}


	public void getElementToBeAdded(String term, String klassStr,Set<String> synonyms, List<IExternalID> externalIDs,Integer prioretyOrder) {

		term = term.trim();
		if(term.isEmpty()|| term.length()<TableResourcesElements.mimimumElementSize ||term.length()>=TableResourcesElements.elementSize)
		{
			term = getSynonym(synonyms);
		}
		if(!term.isEmpty() && term.length() >= TableResourcesElements.mimimumElementSize && term.length()< TableResourcesElements.elementSize)
		{
			IAnoteClass klass = null;
			if(klassStr!=null && !klassStr.isEmpty())
				klass = new AnoteClass(klassStr);
			int priorety = 0;
			if(prioretyOrder!=null && prioretyOrder > -1)
				priorety = prioretyOrder;
			Set<String> synonymsList = new HashSet<>();
			for(String synonym:synonyms){
				synonymsList.add(synonym.trim());
			}
			Set<String> extendalIDsalreadyAddedfortems = new HashSet<>();
			List<IExternalID> externalIDLIst  = new ArrayList<>();
			for(IExternalID externalID:externalIDs)
			{
				String internalID = externalID.getExternalID();
				String source = externalID.getSource().getSource();
				String merge = internalID+source;
				merge = merge.toLowerCase();
				if(!extendalIDsalreadyAddedfortems.contains(merge))
				{
					extendalIDsalreadyAddedfortems.add(merge);
					externalIDLIst.add(externalID);
				}
			}
			addSynonymsToAlreadyAdded(term, synonymsList, externalIDLIst,klass,priorety);
		}
	}


	private void addSynonymsToAlreadyAdded(String term, Set<String> synonymsList, 
			List<IExternalID> externalIDLIst, IAnoteClass klass, int priorety) {
		if (!alreadyAddedElemnt.containsKey(term)){
			IResourceElement elem =new ResourceElementImpl(term,klass,externalIDLIst,new ArrayList<>(synonymsList),priorety,true);
			alreadyAddedElemnt.put(term, elem);
		}
		else{
			IResourceElement savedElem = alreadyAddedElemnt.get(term);
			if (!(savedElem.getSynonyms()==null || savedElem.getSynonyms().isEmpty())){
				synonymsList.addAll(savedElem.getSynonyms());
			}
			if (!(savedElem.getExternalIDsInMemory().isEmpty()||savedElem.getExternalIDsInMemory()==null)){
				externalIDLIst.addAll(savedElem.getExternalIDsInMemory());
			}
			savedElem.setSynonyms(new ArrayList<>(synonymsList));
			savedElem.setExternalIDsInMemory(externalIDLIst);
			alreadyAddedElemnt.put(term, savedElem);
		}
	}

	private String getSynonym(Set<String> termSynomns) {
		if(termSynomns==null || termSynomns.size()<1)
		{
			return new String();
		}
		else
		{
			for(String syn : termSynomns)
			{
				if(syn.trim().length() >= TableResourcesElements.mimimumElementSize)
				{
					termSynomns.remove(syn);
					return syn.trim();
				}
			}
		}
		return new String();
	}

	private List<IExternalID> getExternalIds(String[] lin,CSVFileConfigurations csvfileconfigurations) {
		if(csvfileconfigurations.getColumsDelemiterDefaultValue().getColumnNameColumnParameters().get("ExternalID")==null)
		{
			return new ArrayList<IExternalID>();
		}
		String value = lin[csvfileconfigurations.getColumsDelemiterDefaultValue().getColumnNameColumnParameters().get("ExternalID").getColumnNumber()];
		if(value.equals(csvfileconfigurations.getColumsDelemiterDefaultValue().getColumnNameColumnParameters().get("ExternalID").getDefaultValue().getValue()))
		{
			return new ArrayList<IExternalID>();
		}
		String[] extIDs = value.split(csvfileconfigurations.getColumsDelemiterDefaultValue().getColumnNameColumnParameters().get("ExternalID").getDelimiter().getValue());
		List<IExternalID> listExtID = new ArrayList<IExternalID>();
		for(String extID:extIDs)
		{
			String[] ex = extID.split(csvfileconfigurations.getColumsDelemiterDefaultValue().getColumnNameColumnParameters().get("ExternalID").getSubDelimiter().getValue());
			if(ex.length>1)
			{
				String id = ex[0].replace(csvfileconfigurations.getTextDelimiter().getValue(),"");
				String source = ex[1].replace(csvfileconfigurations.getTextDelimiter().getValue(),"");
				listExtID.add(new ExternalIDImpl(id,  new SourceImpl(source)));
			}
		}
		return listExtID;
	}

	private String getClass(String[] lin,CSVFileConfigurations csvfileconfigurations) {
		String value = lin[csvfileconfigurations.getColumsDelemiterDefaultValue().getColumnNameColumnParameters().get("Class").getColumnNumber()];
		value = value.replace(csvfileconfigurations.getTextDelimiter().getValue(),"");
		return value;
	}

	private Set<String> getSynonyms(String[] lin,CSVFileConfigurations csvfileconfigurations) {
		if(csvfileconfigurations.getColumsDelemiterDefaultValue().getColumnNameColumnParameters().get("Synonyms")==null)
		{
			return new HashSet<String>();
		}
		String value = lin[csvfileconfigurations.getColumsDelemiterDefaultValue().getColumnNameColumnParameters().get("Synonyms").getColumnNumber()];
		if(value.equals(csvfileconfigurations.getColumsDelemiterDefaultValue().getColumnNameColumnParameters().get("Synonyms").getDefaultValue().getValue()))
		{
			return new HashSet<String>();
		}
		else if(value.equals(""))
		{
			return new HashSet<String>();
		}
		String[] syns = value.split(csvfileconfigurations.getColumsDelemiterDefaultValue().getColumnNameColumnParameters().get("Synonyms").getDelimiter().getValue());
		HashSet<String> synList = new HashSet<String>();
		String synElem;
		for(int i=0;i<syns.length;i++)
		{
			synElem = syns[i];
			synElem = synElem.replace(csvfileconfigurations.getTextDelimiter().getValue(),"");
			synList.add(synElem);
		}
		return synList;
	}

	protected String getTerm(String[] lin,CSVFileConfigurations csvfileconfigurations) {
		String value = lin[csvfileconfigurations.getColumsDelemiterDefaultValue().getColumnNameColumnParameters().get("Term").getColumnNumber()];
		if(value!=null)
			value = value.replace(csvfileconfigurations.getTextDelimiter().getValue(),"");
		return value;
	}
}
