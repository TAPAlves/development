package test.com.silicolife.textmining.dictionaryLoader;

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

import main.com.silicolife.textmining.dictionaryLoader.JoChemDictionaryTSVLoader;

public class testJoChemDictLoader {

	@Test
	public void test() throws InvalidDatabaseAccess, ANoteException, IOException {
		File file = new File("C:\\Users\\Tiago\\workspace\\developmentUMinho\\resources\\JoChem.tsv");
		//		IResource<IResourceElement> resource = CreateDictionaryTest.createDictionary("BioDB Dictionary");
		JoChemDictionaryTSVLoader importer = new JoChemDictionaryTSVLoader();
		List<IResourceElement> list = importer.loadTermFromGenericCVSFile(file, getCSVConfigurations());
		System.out.println(list.size());
		for (IResourceElement a: list){
			System.out.println(a);
		}
	}

	private CSVFileConfigurations getCSVConfigurations() {
		Map<String, ColumnParameters> columnNameColumnParameters = new HashMap<String, ColumnParameters>();
		ColumnParameters termColum = new ColumnParameters(0, null, DefaultDelimiterValue.HYPHEN);
		columnNameColumnParameters.put("Term", termColum );
		ColumnParameters klass = new ColumnParameters(1, null, DefaultDelimiterValue.HYPHEN);
		columnNameColumnParameters.put("Class", klass );
		Delimiter vbar = Delimiter.VERTICAL_BAR;
		ColumnParameters synonyms = new ColumnParameters(2,vbar, DefaultDelimiterValue.HYPHEN);
		columnNameColumnParameters.put("Synonym", synonyms );
		ColumnParameters externalids = new ColumnParameters(3, vbar, DefaultDelimiterValue.HYPHEN, Delimiter.COLON );
		columnNameColumnParameters.put("ExternalID", externalids );
		Delimiter generalDelimiter = Delimiter.TAB;
		TextDelimiter textDelimiters = TextDelimiter.QUOTATION_MARK;
		DefaultDelimiterValue defaultValue = DefaultDelimiterValue.HYPHEN;
		ColumnDelemiterDefaultValue columsDelemiterDefaultValue = new ColumnDelemiterDefaultValue(columnNameColumnParameters);
		boolean hasHeaders = true;
		CSVFileConfigurations csvFileConfig = new CSVFileConfigurations(generalDelimiter,textDelimiters,defaultValue,columsDelemiterDefaultValue,hasHeaders);
		return csvFileConfig;
	}




}
