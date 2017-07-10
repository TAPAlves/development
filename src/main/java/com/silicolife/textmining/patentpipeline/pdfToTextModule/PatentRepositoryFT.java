package main.java.com.silicolife.textmining.patentpipeline.pdfToTextModule;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.silicolife.textmining.core.interfaces.core.dataaccess.exception.ANoteException;
import com.silicolife.textmining.processes.ir.patentpipeline.components.metainfomodules.patentrepository.PatentEntity;

public class PatentRepositoryFT {

	//	public class PatentRepositoryPatentMetaInformationRetrieval extends AIRPatentMetaInformationRetrieval{

	private static String url = "patent/metainformation";

	private String patentRepositoryServerBasedUrl;
	private Set<String> patentIDs;
	private String txtFolderPath;

	public PatentRepositoryFT(String patentRepositoryServerBasedUrl, Set<String> patentIDs, String txtFolderPath ){
		this.patentIDs=patentIDs;
		this.txtFolderPath=txtFolderPath;
	}



	public Set<String> retrievePatentsFullTextFromPatentRepository() throws ANoteException, FileNotFoundException {

		Set<String> patentsDownloaded=new HashSet<>();
		for(String patentID:patentIDs)
		{
			PatentEntity patentEntity = searchPatentEntity(patentID);
			if(patentEntity!=null)
			{
				boolean ftExtract=createTXTFileWithFTContent(patentEntity, txtFolderPath, patentID);
				if (ftExtract){
					patentsDownloaded.add(patentID);
				}
			}
		}
		return patentsDownloaded;
	}

	private PatentEntity searchPatentEntity(String patentID)
	{
		try {
			String urlGetPatentInformation = patentRepositoryServerBasedUrl + "/" + url +"/" + patentID;
			InputStream imputstream = new URL(urlGetPatentInformation).openStream();
			ObjectMapper objectMapper = new ObjectMapper();
			PatentEntity result = objectMapper.readValue(imputstream,PatentEntity.class);
			return result;
		} catch (IOException e) {
			//				e.printStackTrace();
		}
		return null;
	}



	private boolean createTXTFileWithFTContent (PatentEntity patentEntity, String txtFolder, String patentID) throws FileNotFoundException{
		if (!(patentEntity.getFullTextContent()==null || patentEntity.getFullTextContent().isEmpty())){
			PrintWriter writer = new PrintWriter(txtFolder + "/" + patentID + ".txt");
			writer.write("ABSTRACT:\n"+patentEntity.getAbstractText() + "\n"+patentEntity.getFullTextContent());
			writer.close();
			return true;
		}
		return false;
	}



}
