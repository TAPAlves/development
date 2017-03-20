package test.com.silicolife.textmining.patentpipeline.bulkData;

import java.io.File;
import java.io.IOException;
import java.util.Set;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.Test;
import org.xml.sax.SAXException;

import com.silicolife.textmining.core.interfaces.core.document.IPublication;

import main.com.silicolife.textmining.patentpipeline.bulkData.BDSS.BDSSUtils;

public class mainTest {

	@Test
	public void test() throws IOException, SAXException, ParserConfigurationException{
		Set<String> fileNames = BDSSUtils.getFileNames("2017");
		for (String filename:fileNames){
			String filePath = BDSSUtils.getBulkPatentFile("2017",filename);
			String patentsFolder = filePath.replace(".zip", "");
			BDSSUtils.unzipPatentFullTextFile(filePath, patentsFolder);
			Set<IPublication> pubs = BDSSUtils.parseXMLfile(new File(patentsFolder).listFiles()[0].toString());
			for(IPublication pub:pubs){
				System.out.println(pub);	
			}
		}
	}

}
