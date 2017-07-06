package main.java.com.silicolife.textmining.dictionaryLoader.loaderDatastructures;

import java.util.List;

import main.java.com.silicolife.textmining.dictionaryLoader.loaderInterfaces.IBioCreativeChemdnerPatentsLoaderConfiguration;

public class BioCreativeChemdnerPatentsLoaderConfigurationImpl implements IBioCreativeChemdnerPatentsLoaderConfiguration{

	private String filePath;
	private int numberOfPatentsToDownload;
	private boolean englishPatentsCountryCodes;
	private List<String> countryCodes;
	
	public BioCreativeChemdnerPatentsLoaderConfigurationImpl(String filePath, int numberOfPatentsToDownload, boolean englishPatentsCountryCodes, List<String> countryCodes) {
		this.filePath=filePath;
		this.numberOfPatentsToDownload=numberOfPatentsToDownload;
		this.englishPatentsCountryCodes=englishPatentsCountryCodes;
		this.countryCodes=countryCodes;
	}
	@Override
	public String getFilePath() {
		return filePath;
	}
	@Override
	public boolean getEnglishPatentsCountryCodes() {
		return englishPatentsCountryCodes;
	}
	@Override
	public List<String> getPatentsCountryCodes() {
		return countryCodes;
	}
	@Override
	public int getNumberOfPatentsToDownload() {
		return numberOfPatentsToDownload;
	}

}
