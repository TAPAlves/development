package com.silicolife.textmining.dictionaryLoader.loaderInterfaces;

import java.util.List;

public interface IBioCreativeChemdnerPatentsLoaderConfiguration {
	
	/**
	 * return the file path string for chemdner samples txt.
	 * @return
	 */
	public String getFilePath();
	
	public boolean getEnglishPatentsCountryCodes();
	
	public List<String> getPatentsCountryCodes();
	
	public int getNumberOfPatentsToDownload();
	

}