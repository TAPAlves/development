package com.silicolife.textmining.dictionaryLoader;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.silicolife.textmining.core.datastructures.init.exception.InvalidDatabaseAccess;
import com.silicolife.textmining.core.datastructures.utils.generic.CSVFileConfigurations;
import com.silicolife.textmining.core.datastructures.utils.generic.ColumnDelemiterDefaultValue;
import com.silicolife.textmining.core.datastructures.utils.generic.ColumnParameters;
import com.silicolife.textmining.core.interfaces.core.dataaccess.exception.ANoteException;
import com.silicolife.textmining.core.interfaces.process.IE.io.export.DefaultDelimiterValue;
import com.silicolife.textmining.core.interfaces.process.IE.io.export.Delimiter;
import com.silicolife.textmining.core.interfaces.process.IE.io.export.TextDelimiter;
import com.silicolife.textmining.core.interfaces.resource.IResourceElement;

public class testJoChemDictLoader {

	@Test
	public void test() throws InvalidDatabaseAccess, ANoteException, IOException {
		File file = new File("resources/JoChem.tsv");
		//		IResource<IResourceElement> resource = CreateDictionaryTest.createDictionary("BioDB Dictionary");
		JoChemDictionaryTSVLoader importer = new JoChemDictionaryTSVLoader();
		List<IResourceElement> list = importer.loadTermFromGenericCVSFile(file, getCSVConfigurations());
		System.out.println(list.size());
		for (IResourceElement a: list){
			System.out.println(a);
		}
	}

	private static CSVFileConfigurations getCSVConfigurations() {
		Delimiter vbar = Delimiter.VERTICAL_BAR;
		Map<String, ColumnParameters> columnNameColumnParameters = new HashMap<String, ColumnParameters>();
		ColumnParameters termColum = new ColumnParameters(0, vbar, DefaultDelimiterValue.NONE);
		columnNameColumnParameters.put("Term", termColum );

		//		Delimiter vbar = Delimiter.VERTICAL_BAR;
		ColumnParameters synonyms = new ColumnParameters(1,vbar, DefaultDelimiterValue.NONE, vbar);
		columnNameColumnParameters.put("Synonyms", synonyms );
//		underscore=new Delimiter();
		Delimiter.USER.setUserDelimiter("_");
		ColumnParameters externalids = new ColumnParameters(2, vbar, DefaultDelimiterValue.NONE,Delimiter.USER);
		columnNameColumnParameters.put("ExternalID", externalids );
		ColumnParameters klass = new ColumnParameters(3, vbar, DefaultDelimiterValue.NONE);
		columnNameColumnParameters.put("Class", klass );

		Delimiter generalDelimiter = Delimiter.TAB;
		TextDelimiter textDelimiters = TextDelimiter.NONE;
		DefaultDelimiterValue defaultValue = DefaultDelimiterValue.NONE;
		ColumnDelemiterDefaultValue columsDelemiterDefaultValue = new ColumnDelemiterDefaultValue(columnNameColumnParameters);
		boolean hasHeaders = true;
		CSVFileConfigurations csvFileConfig = new CSVFileConfigurations(generalDelimiter,textDelimiters,defaultValue,columsDelemiterDefaultValue,hasHeaders);
		return csvFileConfig;
	}

	public static List<IResourceElement> getJoChemDictionary() throws ANoteException, IOException{
		File file = new File("resources/JoChem.tsv");
		//		IResource<IResourceElement> resource = CreateDictionaryTest.createDictionary("BioDB Dictionary");
		JoChemDictionaryTSVLoader importer = new JoChemDictionaryTSVLoader();
		List<IResourceElement> list = importer.loadTermFromGenericCVSFile(file, getCSVConfigurations());
		return list;
		
	}
	



}
