package main.java.com.silicolife.textmining.patentpipeline.searchSourcesModule.bulkData.bdss;

import java.util.ArrayList;
import java.util.List;

public class IIRBDSSConfigurationImpl implements IIRBDSSConfiguration{

	List<String> yearsRange;

	public IIRBDSSConfigurationImpl(String yearsRange) {
		this.yearsRange=transformYearsRange(yearsRange);
		

	}

	@Override
	public List<String> getYearsRange() {
		return yearsRange;
	}

	@Override
	public void setYearsRange(String yearsRange) {
		this.yearsRange=transformYearsRange(yearsRange);

	}

	private List<String> transformYearsRange(String yearsRange){
		List<String> years=new ArrayList<>();
		String[] range = yearsRange.split("-");

		if (range.length>1){
			Integer init = Integer.parseInt(range[0]);
			Integer end=Integer.parseInt(range[1]);
			while (init<=end){
				years.add(init.toString());
				init++;
			}
			
		}
		else{
			years.add(range[0]);
		}
		return years;
	}
	

}
