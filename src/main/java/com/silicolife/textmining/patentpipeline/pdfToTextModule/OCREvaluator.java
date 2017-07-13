package com.silicolife.textmining.patentpipeline.pdfToTextModule;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OCREvaluator {
	private String[] ocrDocument;
	private String[] testDocument;
	private int gapPenalty=-2;
	private int matchValue=1;
	private int mismatchValue=-1;
	int [][] matrix;

	/**
	 * Class that applies dynamic programming algorithm in order to 
	 * obtain a score to longest common subsequence (LCS) by using Smith-Waterman algorithm (local alignment)
	 */
	public OCREvaluator(String[] ocrDocument, String[] testDocument) {
		this.ocrDocument=ocrDocument;
		this.testDocument=testDocument;
		matrix=new int[ocrDocument.length+1][testDocument.length+1];
	}

	public void doMatrix (){
		int lines = ocrDocument.length+1;
		int columns=testDocument.length+1;
		for (int i = 1; i < lines; i++){
			int val = matrix[i-1][0]+gapPenalty;
			if (val>0) {
				matrix[i][0]=val;}
			else{
				matrix[i][0]=0;
			}
		}
		for (int j = 1; j < columns; j++){
			int val=matrix[0][j-1]+gapPenalty;
			if (val>0){
				matrix[0][j]=val;	
			}
			else{
				matrix[0][j]=0;
			}

		}

		for (int i = 1; i < lines; i++){
			for (int j = 1; j < columns; j++){
				int max = wayOption(i, j);
				matrix[i][j]=max;
			}
		}

	}


	private int similarityValue(int ocrTokenIndexOnMatrix ,int testTokenIndexOnMatrix ){
		if (ocrDocument[ocrTokenIndexOnMatrix-1].equalsIgnoreCase(testDocument[testTokenIndexOnMatrix-1])){
			return matchValue;
		}
		else{
			return mismatchValue;
		}
	}


	private int wayOption (int i, int j){
		int[] wayOptions = new int[3];
		wayOptions[0]=matrix[i-1][j-1]+similarityValue(i, j);
		wayOptions[1]=matrix[i][j-1]+gapPenalty;
		wayOptions[2]=matrix[i-1][j]+gapPenalty;
		int max = -10000;
		for (int num:wayOptions){
			if (max<num){
				max=num;
			}
		}
		if (max>0) {
			return max;
		}
		else{
			return 0;
		}
	}

	public double calculateTracebackAndSimilarityPercentageRecall(){
		List<Integer> matchAndTotalTransition = calculateMatchAndTotalTransitions();
		System.out.println("match:"+(double)matchAndTotalTransition.get(0));
		System.out.println("total:"+(double)matchAndTotalTransition.get(1));
//		return verifyAlignment(matchAndTotalTransition.get(0),matchAndTotalTransition.get(1));
	return (double) matchAndTotalTransition.get(0)/(double)testDocument.length;
	}

	
	public double calculateTracebackAndSimilarityPercentagePrecision(){
		List<Integer> matchAndTotalTransition = calculateMatchAndTotalTransitions();
		System.out.println("match:"+(double)matchAndTotalTransition.get(0));
		System.out.println("total:"+(double)matchAndTotalTransition.get(1));
		return (double)matchAndTotalTransition.get(0)/(double)matchAndTotalTransition.get(1);
	}

	public List<Integer> calculateMatchAndTotalTransitions(){
		List<Integer> matchAndTotalTransitions = new ArrayList<>();
		List<Integer[]> positions=new ArrayList<>();
		List<Integer> values=new ArrayList<>();
		List<Integer> majorpathwayValues=new ArrayList<>();
		List<Integer[]> majorpathwhayPositions=new ArrayList<>();
		int totalTransitions = 0;
		int matchTransitions=0;
		List<Object> maxValuestoSearch = findMaxValueOnMatrix();//on first it has positions and secondly has the maximum value.

		@SuppressWarnings("unchecked")
		List<Integer[]> firstPosit = (List<Integer[]>) maxValuestoSearch.get(0);
		@SuppressWarnings("unchecked")
		List<Integer> firstValue = (List<Integer>) maxValuestoSearch.get(1);
		for (int pos = 0; pos < firstPosit.size(); pos++) {
			int i=Arrays.asList(firstPosit.get(pos)).get(0);
			int j=Arrays.asList(firstPosit.get(pos)).get(1);
			positions.add(firstPosit.get(pos));//add the first position to list;
			values.add(firstValue.get(pos));//add the first value to the respective list
			int totalTransitionsLocal = 1;//there is already one entry on positions list
			int matchTransitionsLocal=0;
			do{
				Map<Integer[], Integer> coordenatesDictionary = majorValueInTheWay(i, j);
				positions.add((Integer[]) coordenatesDictionary.keySet().toArray()[0]);
				values.add(coordenatesDictionary.get(coordenatesDictionary.keySet().toArray()[0]));

				i=Arrays.asList(positions.get(totalTransitionsLocal)).get(0);
				j=Arrays.asList(positions.get(totalTransitionsLocal)).get(1);

				//verify if there is a match
				if (values.get(totalTransitionsLocal-1)==values.get(totalTransitionsLocal)+matchValue){
					matchTransitionsLocal+=1;
				}
				totalTransitionsLocal+=1;

			} while (matrix[i][j]!=0 && i<ocrDocument.length+1 && j<testDocument.length+1);


			if (values.size()>majorpathwayValues.size()) {
				majorpathwayValues.clear();
				majorpathwhayPositions.clear();
				majorpathwhayPositions.addAll(positions);
				majorpathwayValues.addAll(values);
				totalTransitions=totalTransitionsLocal-1;//delete the last transition which stops the cycle
				matchTransitions=matchTransitionsLocal;

			}
			positions.clear();
			values.clear();

		}
		matchAndTotalTransitions.add(0, matchTransitions);
		matchAndTotalTransitions.add(1, totalTransitions);
		
		System.out.println(matchTransitions +  "/"+ totalTransitions);
		return matchAndTotalTransitions;
	}
	
	
//	private double verifyAlignment(int matchTransitions,int totalTransitions){
//		int testDocumentSize=testDocument.length;//value to test
//		if(testDocumentSize>ocrDocument.length){//if ocr document has less words than the test, its assumed that the testDocument corresponds to OCRdocument
//			testDocumentSize=ocrDocument.length;
//		}
//		if(totalTransitions<testDocumentSize){
//			System.out.println("Local alignment results: "+(double)matchTransitions/(double)totalTransitions);
//		}
//		return (double) matchTransitions/(double)testDocumentSize;		
//	}



	private List<Object> findMaxValueOnMatrix(){
		List<Integer[]> positions=new ArrayList<>();
		List<Integer> values=new ArrayList<>();
		List<Object> positionsAndValues=new ArrayList<>();
		Integer maxValue = 0;
		for (int line = 0; line < ocrDocument.length+1; line++){
			for (int column = 0; column < testDocument.length+1; column++){
				if (maxValue<matrix[line][column]){
					maxValue=matrix[line][column];
					Integer[] pos={line,column};
					positions.clear();
					values.clear();
					positions.add(pos);
					values.add(maxValue);
				}
				else if (maxValue==matrix[line][column]&&maxValue>0) {
					Integer[] pos={line,column};
					positions.add(pos);
					values.add(maxValue);
				}
			}

		}
		positionsAndValues.add(positions);
		positionsAndValues.add(values);

		return positionsAndValues;
	}

	private Map<Integer[], Integer> majorValueInTheWay(int i,int j){
		int value = -10000;
		Map<Integer[],Integer> way = new HashMap<>();
		if (matrix[i-1][j-1]==matrix[i][j]-matchValue){
			value=(int) matrix[i-1][j-1];
			Integer[] positions={i-1,j-1};
			way.clear();
			way.put(positions,value);
		}
		else if (matrix[i-1][j-1]==matrix[i][j]-mismatchValue){
			if (!way.isEmpty()){
				Integer actualValue = way.get(Arrays.asList(way.keySet()).get(0));
				if (actualValue>matrix[i-1][j-1]){
					value=(int) matrix[i-1][j-1];
					Integer[] positions={i-1,j-1};
					way.clear();
					way.put(positions,value);
				}
			}
			else{
				value=(int) matrix[i-1][j-1];
				Integer[] positions={i-1,j-1};
				way.put(positions,value);
			}

		}
		else if (matrix[i-1][j]==matrix[i][j]-gapPenalty){
			if (!way.isEmpty()){
				Integer actualValue = way.get(Arrays.asList(way.keySet()).get(0));
				if (actualValue>matrix[i-1][j]){
					value=(int) matrix[i-1][j];
					Integer[] positions={i-1,j};
					way.clear();
					way.put(positions,value);
				}
			}
			else{
				value=(int) matrix[i-1][j];
				Integer[] positions={i-1,j};
				way.put(positions,value);
			}
		}

		else if (matrix[i][j-1]==matrix[i][j]-gapPenalty){
			if (!way.isEmpty()){
				Integer actualValue = way.get(Arrays.asList(way.keySet()).get(0));
				if (actualValue>matrix[i][j-1]){
					value=(int) matrix[i][j-1];
					Integer[] positions={i,j-1};
					way.clear();
					way.put(positions,value);
				}
			}
			else{
				value=(int) matrix[i][j-1];
				Integer[] positions={i,j-1};
				way.put(positions,value);
			}
		}
		return way;	
	}

	public void printMatrix(){
		for (int[] line:matrix){
			for (int value:line){//column
				System.out.print(value+"\t");
			} System.out.println("\n");		
		}
	}


}

