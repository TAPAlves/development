package main.com.silicolife.textmining.patentpipeline.bulkData.BDSS;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

import com.silicolife.textmining.core.datastructures.utils.conf.GlobalOptions;
import com.silicolife.textmining.utils.http.ResponseHandler;
import com.silicolife.textmining.utils.http.exceptions.ResponseHandlingException;

public class BDSSYearFileDownloadHandler implements ResponseHandler<File> {

	private String pathDoc;
	private long totalFileSize;
	private static final int DEFAULT_BUFFER_SIZE = 1024 * 4;

	public BDSSYearFileDownloadHandler(String pathDoc, long totalFileSize) {
		this.pathDoc=pathDoc;
		this.totalFileSize=totalFileSize;
	}

	@Override
	public File buildResponse(InputStream response, String responseMessage, Map<String, List<String>> headerFields,
			int status) throws ResponseHandlingException {

		File file = new File(pathDoc);

		try {
			FileOutputStream outputStream = new FileOutputStream(file);
			file.createNewFile();
			copyLarge(response, outputStream);
			outputStream.close();

		} catch (IOException e) {
			e.printStackTrace();
			file.delete();
			
		}
		return file;
	}


	private long copyLarge(InputStream input, OutputStream output)
			throws IOException {
		long startTime = GregorianCalendar.getInstance().getTimeInMillis();	
		byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
		long count = 0;
		int n = 0;
		while (-1 != (n = input.read(buffer))) {
			output.write(buffer, 0, n);
			count += n;
			memoryAndProgress( count, totalFileSize, startTime);
		}
		return count;
	}

	protected void memoryAndProgress(long step, long total, long startTime) {
		System.out.println((GlobalOptions.decimalformat.format((double)step/ (double) total * 100)) + " %...");
		//		logger.info((GlobalOptions.decimalformat.format((double)step/ (double) total * 100)) + " %...");
		Runtime.getRuntime().gc();
		System.out.println((Runtime.getRuntime().totalMemory()- Runtime.getRuntime().freeMemory())/(1024*1024) + " MB ");
	}

}
