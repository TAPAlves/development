package main.java.com.silicolife.textmining.patentpipeline.searchSourcesModule.bulkData.bdss;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;

import com.silicolife.textmining.utils.http.ResponseHandler;
import com.silicolife.textmining.utils.http.exceptions.ResponseHandlingException;

public class BDSSYearFileListHandler implements ResponseHandler<Set<String>>{

	private Set<String> filenames;


	public BDSSYearFileListHandler(Set<String> filenames) {
		this.filenames=filenames;
	}


	@Override
	public Set<String> buildResponse(InputStream response, String responseMessage, Map<String, List<String>> headerFields,
			int status) throws ResponseHandlingException {
		try {
			String pattern="href=\"{1}.*zip{1}\"{1}";

			String xmlString = IOUtils.toString(response, "UTF-8").trim();

			Pattern r=Pattern.compile(pattern);
			Matcher m=r.matcher(xmlString);

			while (m.find()){
				String zipFilename = m.group().replace("href=", "").replaceAll("\"", "");
				System.out.println(zipFilename);
				filenames.add(zipFilename);
			}


		} catch (IOException e) {
			return filenames;
		}
		return filenames;


	}

}
