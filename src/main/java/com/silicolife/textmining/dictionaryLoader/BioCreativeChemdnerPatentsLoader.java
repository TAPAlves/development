package main.java.com.silicolife.textmining.dictionaryLoader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import main.java.com.silicolife.textmining.dictionaryLoader.loaderInterfaces.IBioCreativeChemdnerPatentsLoaderConfiguration;
import main.java.com.silicolife.textmining.dictionaryLoader.loaderInterfaces.WrongBioCreativeChemdnerPatentsLoaderConfigurationException;

public class BioCreativeChemdnerPatentsLoader {

	private IBioCreativeChemdnerPatentsLoaderConfiguration configuration;

	public BioCreativeChemdnerPatentsLoader(IBioCreativeChemdnerPatentsLoaderConfiguration configuration) throws WrongBioCreativeChemdnerPatentsLoaderConfigurationException{
		this.configuration=configuration;
		validate(configuration);
	}

	public Map<String, ArrayList<String>> openFile() throws IOException{
		Map<String, ArrayList<String>> mapIdsAndAbstracts = new HashMap<>();
		FileInputStream stream = new FileInputStream(configuration.getFilePath());
		InputStreamReader reader = new InputStreamReader(stream);
		BufferedReader br = new BufferedReader(reader);
		String line = br.readLine(); 
		while (line != null) {
			System.out.printf("%s\n", line);
			String[] values= line.split("\t");
			ArrayList<String> titleAndAbstract = new ArrayList<>();
			titleAndAbstract.add(values[1]);//title
			titleAndAbstract.add(values[2]);//abstract
			mapIdsAndAbstracts.put(values[0], titleAndAbstract);//map of patents and respective abstract
			line = br.readLine(); 
		}
		br.close();
		return mapIdsAndAbstracts;
	}

	public List<String> sortMapByLenght(Map<String, ArrayList<String>> map){
		List<String> major=new ArrayList<>();
		for (String patentID:map.keySet()){
			int value=map.get(patentID).get(1).length();
			if (major.size()!=0){
				int actualMajorValue = map.get(major.get(0)).get(1).length();//major value on 1st place
				if(value>actualMajorValue){
					major.add(0, patentID);
				}
				else{
					boolean valueFounded=false;
					for (int i = 1; i < major.size(); i++) {
						int valueOnList = map.get(major.get(i)).get(1).length();
						if(valueOnList<value){
							major.add(i, patentID);
							valueFounded=true;
							break;
						}
					}
					if(!valueFounded){
						major.add(patentID);	
					}	
				}
			}
			else{
				major.add(patentID);

			}
		}
		return major;	
	}

	public List<String> sortMapByLenghtOnlyEnglish(Map<String, ArrayList<String>> map,List<String> countryCodes){
		List<String> major=new ArrayList<>();
		for (String patentID:map.keySet()){
			if(verifyEnglishCountryCodes(patentID,countryCodes)){
				int value=map.get(patentID).get(1).length();
				if (major.size()!=0){
					int actualMajorValue = map.get(major.get(0)).get(1).length();//major value on 1st place
					if(value>actualMajorValue){
						major.add(0, patentID);
					}
					else{
						boolean valueFounded=false;
						for (int i = 1; i < major.size(); i++) {
							int valueOnList = map.get(major.get(i)).get(1).length();
							if(valueOnList<value){
								major.add(i, patentID);
								valueFounded=true;
								break;
							}
						}
						if(!valueFounded){
							major.add(patentID);	
						}	
					}
				}
				else{
					major.add(patentID);

				}
			}
		}
		return major;	
	}

	public Map<String, ArrayList<String>> extractPatentIDs() throws IOException{
		Map<String, ArrayList<String>> newMap=new HashMap<>();
		Map<String, ArrayList<String>> allMap = openFile();
		List<String> sortedList=new ArrayList<>();		
		if (configuration.getEnglishPatentsCountryCodes()==true){
			if(configuration.getPatentsCountryCodes()!=null||!configuration.getPatentsCountryCodes().isEmpty()){
				sortedList = sortMapByLenghtOnlyEnglish(allMap,configuration.getPatentsCountryCodes());		
			}
			else{
				sortedList=sortMapByLenghtOnlyEnglish(allMap, null);
			}
		}
		else{
			sortedList=sortMapByLenght(allMap);
		}

		if (sortedList.size()>=configuration.getNumberOfPatentsToDownload()){
			for (int i = 0; i < configuration.getNumberOfPatentsToDownload(); i++) {
				newMap.put(sortedList.get(i), allMap.get(sortedList.get(i)));
			}
		}
		else{
			for (int i = 0; i < sortedList.size(); i++) {
				newMap.put(sortedList.get(i), allMap.get(sortedList.get(i)));
				}
		}
		//		for (int i = 99; i < 300; i++) {
		//			ArrayList<String> keys = new ArrayList<>(allMap.keySet());
		//			newMap.put(keys.get(i),allMap.get(keys.get(i)));
		//		}
		return newMap;
	}

	private void validate(IBioCreativeChemdnerPatentsLoaderConfiguration configuration) throws WrongBioCreativeChemdnerPatentsLoaderConfigurationException {
		if (configuration.getFilePath()==null||configuration.getFilePath().isEmpty()){
			throw new WrongBioCreativeChemdnerPatentsLoaderConfigurationException("The filepath can not be null or empty");
		}

		else{
			if (!configuration.getFilePath().endsWith(".txt")){
				throw new WrongBioCreativeChemdnerPatentsLoaderConfigurationException("The filePath must be a txt file");
			}
			else{
				File file = new File(configuration.getFilePath());
				if(!file.isFile()){
					throw new WrongBioCreativeChemdnerPatentsLoaderConfigurationException("The given filePath is not a file");
				}
				if(!file.exists()){
					throw new WrongBioCreativeChemdnerPatentsLoaderConfigurationException("The file does not exist");
				}
			}

		}
	}


	public static boolean verifyEnglishCountryCodes(String patentID, List<String> countryCodes){
		Set<String> countriesAllowed =new HashSet<>();
		if(countryCodes.isEmpty()||countryCodes==null){
			countriesAllowed.add("AU");
			countriesAllowed.add("BZ");
			countriesAllowed.add("CA");
			countriesAllowed.add("CB");
			countriesAllowed.add("IE");
			countriesAllowed.add("JM");
			countriesAllowed.add("NZ");
			countriesAllowed.add("PH");
			countriesAllowed.add("ZA");
			countriesAllowed.add("TT");
			countriesAllowed.add("GB");
			countriesAllowed.add("US");
			countriesAllowed.add("EP");
			countriesAllowed.add("WO");}
		else{
			countriesAllowed.addAll(countryCodes);
		}
		for (String cc:countriesAllowed){
			if (patentID.subSequence(0, 2).equals(cc)){
				return true;
			}
		}
		return false;
	}




}

