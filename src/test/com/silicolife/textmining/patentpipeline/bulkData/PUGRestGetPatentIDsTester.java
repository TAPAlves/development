package test.com.silicolife.textmining.patentpipeline.bulkData;

import java.io.UnsupportedEncodingException;
import java.util.Set;

import org.junit.Test;

import main.com.silicolife.textmining.patentpipeline.PubChemAPI.PUGHelp.PUGRestUtils;

public class PUGRestGetPatentIDsTester {


//	@Test
	public void test1(){
		String identifier="5281239";
		Set<String> set = PUGRestUtils.getPatentIDsUsingCID(identifier);
		System.out.println(set);
		System.out.println(set.size());
		for (String a:set){
			System.out.println(a);
		}
	}
	
	
	@Test
	public void test2() throws UnsupportedEncodingException{
		String identifier="hydrochloric acid";
		Set<String> set = PUGRestUtils.getPatentIDsUsingCompoundName(identifier);
		System.out.println(set);
		System.out.println(set.size());
		for (String a:set){
			System.out.println(a);
		}
	}
	
}
