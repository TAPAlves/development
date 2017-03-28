package test.com.silicolife.textmining.patentpipeline.bulkData;

import java.util.Set;

import org.junit.Test;

import main.com.silicolife.textmining.patentpipeline.PubChemAPI.PUGHelp.PUGRestUtils;

public class PUGRestGetPatentIDsTester {
	
	
	@Test
	public void test1(){
		String identifier="5281239";
		Set<String> set = PUGRestUtils.getPatentIDs(identifier);
		System.out.println(set);
		System.out.println(set.size());
	}
}
