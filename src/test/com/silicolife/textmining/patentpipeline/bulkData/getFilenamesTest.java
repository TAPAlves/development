package test.com.silicolife.textmining.patentpipeline.bulkData;

import java.io.IOException;

import org.junit.Test;

import main.com.silicolife.textmining.patentpipeline.bulkData.BDSS.BDSSUtils;

public class getFilenamesTest {
	
	
	@Test
	public void getFilenames() throws IOException{
//		BDSSUtils.getFileNames("2016");
//		BDSSUtils.getBulkPatentFile("2017", (String) BDSSUtils.getFileNames("2017").toArray()[0]);
//		BDSSUtils.getBulkPatentFile("2017","ipg170103.zip");
//		BDSSUtils.getBulkPatentFile("2017","ipg170110.zip");
//System.out.println(BDSSUtils.getFileLenght("https://bulkdata.uspto.gov/data2/patent/grant/redbook/fulltext/2017/ipg170103.zip"));	
		BDSSUtils.unzipPatentFullTextFile("tempZipFiles/ipg170103.zip", "tempZipFiles/ipg170103" );
	}

}
