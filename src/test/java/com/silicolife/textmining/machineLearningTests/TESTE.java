package com.silicolife.textmining.machineLearningTests;

import java.io.IOException;

import org.junit.Test;

import com.silicolife.textmining.machineLearningMains.MLBioTMLModelVSNejiUtils;

public class TESTE {


//	@Test
	public void splitTrainFiles() throws IOException{

		String trainingDocumentsFile = "src/test/resources/chemdner/train/chemdner_patents_train_text.txt";
		String trainingAnnotationsFile = "src/test/resources/chemdner/train/chemdner_cemp_gold_standard_train.tsv";
		String destinationTextPath = "/home/tiagoalves/workspace/development/generalMain/train/trainSentences/";
		String destinationAnnotPath = "/home/tiagoalves/workspace/development/generalMain/train/trainAnnotations/";
		MLBioTMLModelVSNejiUtils.splitBioCAnnotationsFilesByGroups(trainingAnnotationsFile, destinationAnnotPath);
		String familyAnotationFilePath = "/home/tiagoalves/workspace/development/generalMain/train/trainAnnotations/FAMILYchemdner_cemp_gold_standard_train.tsv";
		MLBioTMLModelVSNejiUtils.splitBioCTextFileIntoAGroupSpecificOneUsingAnnotationsFile(trainingDocumentsFile, familyAnotationFilePath, "FAMILY", destinationTextPath);

	}
	
	
	@Test
	public void splitdevelopmentFiles() throws IOException{
		String trainingDocumentsFile = "src/test/resources/chemdner/dev/chemdner_patents_development_text.txt";
		String trainingAnnotationsFile = "src/test/resources/chemdner/dev/chemdner_cemp_gold_standard_development_v03.tsv";
		String destinationTextPath = "/home/tiagoalves/workspace/development/generalMain/development/developmentSentences";
		String destinationAnnotPath = "/home/tiagoalves/workspace/development/generalMain/development/developmentAnnotations";
		MLBioTMLModelVSNejiUtils.splitBioCAnnotationsFilesByGroups(trainingAnnotationsFile, destinationAnnotPath);
		String familyAnotationFilePath = "/home/tiagoalves/workspace/development/generalMain/development/developmentAnnotations/FAMILYchemdner_cemp_gold_standard_development_v03.tsv";
		MLBioTMLModelVSNejiUtils.splitBioCTextFileIntoAGroupSpecificOneUsingAnnotationsFile(trainingDocumentsFile, familyAnotationFilePath, "FAMILY", destinationTextPath);
		
	}
}

