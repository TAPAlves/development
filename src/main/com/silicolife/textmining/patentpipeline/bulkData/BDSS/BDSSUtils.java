package main.com.silicolife.textmining.patentpipeline.bulkData.BDSS;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.io.IOUtils;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import com.silicolife.textmining.utils.http.HTTPClient;
import com.silicolife.textmining.utils.http.exceptions.ClientErrorException;
import com.silicolife.textmining.utils.http.exceptions.ConnectionException;
import com.silicolife.textmining.utils.http.exceptions.RedirectionException;
import com.silicolife.textmining.utils.http.exceptions.ResponseHandlingException;
import com.silicolife.textmining.utils.http.exceptions.ServerErrorException;



public class BDSSUtils {

	private static String generalURL="https://bulkdata.uspto.gov/data2/patent/grant/redbook/fulltext/";

	private static String outputDir="tempZipFiles/";


	/**
	 * Size of the buffer to read/write data
	 */
	private static final int BUFFER_SIZE = 4096;

	private static HTTPClient client = new HTTPClient();



	public static Set<String> getFileNames (String year){

		Map<String, String> headers = new HashMap<String, String>();

		String urlPatentFullTextFilesList= generalURL + year;

		Set<String> filenames = new HashSet<>();

		try {
			client.get(urlPatentFullTextFilesList, headers, new BDSSYearFileListHandler(filenames));
		} catch (RedirectionException | ClientErrorException | ServerErrorException | ConnectionException
				| ResponseHandlingException e) {
		}
		return filenames;

	}



	public static void getBulkPatentFile(String year, String filename) throws IOException{
		Path docPath = Paths.get(outputDir);	
		if (!Files.exists(docPath)){
			Files.createDirectories(docPath);
		}

		Map<String, String> headers = new HashMap<String, String>();

		String urlPatentFullTextFile= generalURL + year + "/" + filename;
		long totalFileSize = getFileLenght(urlPatentFullTextFile);

		try {
			client.get(urlPatentFullTextFile, headers, new BDSSYearFileDownloadHandler(docPath.toString()+ "/" +filename, totalFileSize));
		} catch (RedirectionException | ClientErrorException | ServerErrorException | ConnectionException
				| ResponseHandlingException e) {
		}




	}


	public static long getFileLenght(String urlConnection){
		long totalFileSize = 0;
		try {
			URL url = new URL(urlConnection);;
			URLConnection conn = url.openConnection();
			conn.connect();
			totalFileSize = conn.getContentLengthLong();
		} catch (Exception e) {

		}
		return totalFileSize;
	}



	public static void getPatentIDs (){

	}


	public static void getMapWithMetainformationFromAllPatents(){

	}


	public static Document createJDOMDocument(InputStream response) throws ParserConfigurationException, SAXException, IOException {
		String stream = IOUtils.toString(response, "UTF-8");
		stream = stream.replaceAll("\n", "");
		stream = stream.replaceAll("\\s{2,}", "");
		InputStream imputstream = new ByteArrayInputStream(stream.getBytes("UTF-8"));
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder parser = factory.newDocumentBuilder();
		Document doc = parser.parse(imputstream);
		return doc;

	}


	public static void unzipPatentFullTextFile(String zipFilePath, String destDirectory) throws IOException {
		File destDir = new File(destDirectory);
		if (!destDir.exists()) {
			destDir.mkdir();
		}
		ZipInputStream zipIn = new ZipInputStream(new FileInputStream(zipFilePath));

		ZipEntry entry = zipIn.getNextEntry();

		// iterates over entries in the zip file
		while (entry != null) {
			String filePath = destDirectory + File.separator + entry.getName();
			if (!entry.isDirectory()) {
				// if the entry is a file, extracts it
				extractFile(zipIn, filePath);
			} else {
				// if the entry is a directory, make the directory
				File dir = new File(filePath);
				dir.mkdir();
			}
			zipIn.closeEntry();
			entry = zipIn.getNextEntry();
		}
		zipIn.close();
	}


	private static void extractFile(ZipInputStream zipIn, String filePath) throws IOException {
		BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath));
		byte[] bytesIn = new byte[BUFFER_SIZE];
		int read = 0;
		while ((read = zipIn.read(bytesIn)) != -1) {
			bos.write(bytesIn, 0, read);
		}
		bos.close();
	}


	
	
	public static void parseXMLfile(String xmlPath) throws FileNotFoundException{
		
		InputStream rawData = new FileInputStream(new File(xmlPath));
		
	}







}
