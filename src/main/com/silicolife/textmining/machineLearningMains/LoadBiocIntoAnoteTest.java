package main.com.silicolife.textmining.machineLearningMains;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

import com.silicolife.textmining.core.datastructures.utils.FileHandling;
import com.silicolife.textmining.core.datastructures.utils.GenerateRandomId;
import com.silicolife.textmining.core.interfaces.core.dataaccess.exception.ANoteException;
import com.silicolife.textmining.core.interfaces.core.report.corpora.ICorpusCreateReport;
import com.silicolife.textmining.machinelearning.biotml.core.BioTMLConstants;
import com.silicolife.textmining.machinelearning.biotml.core.annotator.BioTMLMalletAnnotatorImpl;
import com.silicolife.textmining.machinelearning.biotml.core.corpora.BioTMLCorpusImpl;
import com.silicolife.textmining.machinelearning.biotml.core.corpora.BioTMLDocumentImpl;
import com.silicolife.textmining.machinelearning.biotml.core.corpora.BioTMLEntityImpl;
import com.silicolife.textmining.machinelearning.biotml.core.evaluation.BioTMLEvaluator;
import com.silicolife.textmining.machinelearning.biotml.core.evaluation.datastrucures.BioTMLEvaluationImpl;
import com.silicolife.textmining.machinelearning.biotml.core.evaluation.datastrucures.BioTMLModelEvaluationConfiguratorImpl;
import com.silicolife.textmining.machinelearning.biotml.core.exception.BioTMLException;
import com.silicolife.textmining.machinelearning.biotml.core.features.BioTMLFeatureGeneratorConfiguratorImpl;
import com.silicolife.textmining.machinelearning.biotml.core.features.modules.NLP4JFeatures;
import com.silicolife.textmining.machinelearning.biotml.core.interfaces.IBioTMLAnnotator;
import com.silicolife.textmining.machinelearning.biotml.core.interfaces.IBioTMLConfusionMatrix;
import com.silicolife.textmining.machinelearning.biotml.core.interfaces.IBioTMLCorpus;
import com.silicolife.textmining.machinelearning.biotml.core.interfaces.IBioTMLCorpusReader;
import com.silicolife.textmining.machinelearning.biotml.core.interfaces.IBioTMLCorpusWriter;
import com.silicolife.textmining.machinelearning.biotml.core.interfaces.IBioTMLDocument;
import com.silicolife.textmining.machinelearning.biotml.core.interfaces.IBioTMLEntity;
import com.silicolife.textmining.machinelearning.biotml.core.interfaces.IBioTMLEvaluation;
import com.silicolife.textmining.machinelearning.biotml.core.interfaces.IBioTMLModel;
import com.silicolife.textmining.machinelearning.biotml.core.interfaces.IBioTMLModelConfigurator;
import com.silicolife.textmining.machinelearning.biotml.core.interfaces.IBioTMLModelEvaluationConfigurator;
import com.silicolife.textmining.machinelearning.biotml.core.interfaces.IBioTMLModelReader;
import com.silicolife.textmining.machinelearning.biotml.core.interfaces.IBioTMLModelWriter;
import com.silicolife.textmining.machinelearning.biotml.core.interfaces.IBioTMLMultiEvaluation;
import com.silicolife.textmining.machinelearning.biotml.core.interfaces.IBioTMLSentence;
import com.silicolife.textmining.machinelearning.biotml.core.interfaces.IBioTMLToken;
import com.silicolife.textmining.machinelearning.biotml.core.mllibraries.BioTMLAlgorithm;
import com.silicolife.textmining.machinelearning.biotml.core.models.BioTMLModelConfigurator;
import com.silicolife.textmining.machinelearning.biotml.core.models.mallet.MalletClassifierModel;
import com.silicolife.textmining.machinelearning.biotml.core.models.mallet.MalletTransducerModel;
import com.silicolife.textmining.machinelearning.biotml.core.nlp.nlp4j.BioTMLNLP4J;
import com.silicolife.textmining.machinelearning.biotml.reader.BioTMLCorpusReaderImpl;
import com.silicolife.textmining.machinelearning.biotml.reader.BioTMLModelReaderImpl;
import com.silicolife.textmining.machinelearning.biotml.writer.BioTMLCorpusWriterImpl;
import com.silicolife.textmining.machinelearning.biotml.writer.BioTMLModelWriterImpl;

import pt.ua.tm.neji.core.module.Reader;
import pt.ua.tm.neji.core.module.Writer;
import pt.ua.tm.neji.core.parser.Parser;
import pt.ua.tm.neji.core.parser.ParserLanguage;
import pt.ua.tm.neji.core.parser.ParserLevel;
import pt.ua.tm.neji.core.pipeline.Pipeline;
import pt.ua.tm.neji.exception.NejiException;
import pt.ua.tm.neji.ml.MLHybrid;
import pt.ua.tm.neji.ml.MLModel;
import pt.ua.tm.neji.nlp.NLP;
import pt.ua.tm.neji.parser.GDepParser;
import pt.ua.tm.neji.pipeline.DefaultPipeline;
import pt.ua.tm.neji.reader.RawReader;
import pt.ua.tm.neji.sentencesplitter.LingpipeSentenceSplitter;
import pt.ua.tm.neji.train.config.ModelConfig;
import pt.ua.tm.neji.train.model.CRFModel;
import pt.ua.tm.neji.train.nlp.TrainNLP;
import pt.ua.tm.neji.train.pipeline.TrainPipelinePhase1;
import pt.ua.tm.neji.train.pipeline.TrainPipelinePhase2;
import pt.ua.tm.neji.train.reader.BC2Reader;
import pt.ua.tm.neji.train.trainer.DefaultTrainer;
import pt.ua.tm.neji.writer.BC2Writer;
import test.com.silicolife.textmining.LinneausTaggerToMemoryRun.CHEMDNERCorpusToLinneausTaggerInMemory;

public class LoadBiocIntoAnoteTest {

	//	@Test
	public void test1() throws BioTMLException{
		createAModel();
	}


	public static ICorpusCreateReport loadCorpusFromBiocreative() throws IOException, ANoteException{
		CHEMDNERCorpusToLinneausTaggerInMemory testClass = new CHEMDNERCorpusToLinneausTaggerInMemory();
		ICorpusCreateReport report = testClass.createCorpusFromBioCreativeFiles();
		return report;
	}


	public static void createAModel() throws BioTMLException{


		//			String  = "";
		//			String modelDir = "C:/Users/RRodrigues/Desktop/JNLPBA/cell_type_all.gz";
		//			String corpusDir = "C:/Users/RRodrigues/Desktop/JNLPBA/Genia4ERtask2.gz";
		String modelDir="testeModelos/modeloML/modeloCriado.gz";
		String corpusDir="src/test/resources/chemdner/trainFile";


		String modelClassType = "chemical";
		String modelIEType = "NER";
		IBioTMLModelConfigurator configuration = defaultConfiguration(modelClassType, modelIEType);
		//			configuration.setAlgorithmType(BioTMLAlgorithms.malletsvm.toString());
		//			svm_parameter svmparam = configuration.getSVMParameters();
		//			svmparam.kernel_type = 3;
		//			svmparam.gamma = 0.001;
		//			configuration.setSVMParameters(svmparam);
		configuration.setNumThreads(7);

		System.out.println("Loading the BioTMLCorpus...");
		BioTMLCorpusReaderImpl reader = new BioTMLCorpusReaderImpl();
		IBioTMLCorpus corpus = reader.readBioTMLCorpusFromDirFolder(corpusDir,"nlp4j");
		System.out.println("Starting the model...");

		//			IBioTMLModel model =new MalletClassifierModel(corpus, loadFeatures(), configuration, defaultEvaluationConfiguration());
		IBioTMLModel model = new MalletTransducerModel(loadfeatures(),defaultConfiguration(modelClassType, modelIEType));
		//			System.out.println("Executing the model evaluation...");
		//			IBioTMLModelEvaluationResults evaluation = model.evaluate(corpus, defaultEvaluationConfiguration());
		//			System.out.println(evaluation.printResults());
		System.out.println("Executing the model training...");
		model.train(corpus);
		System.out.println("Saving the model...");
		IBioTMLModelWriter writer = new BioTMLModelWriterImpl(modelDir);
		writer.writeGZModelFile(model);
		System.out.println("Model Creation finished!");


	}

	//
	//	private static BioTMLFeatureGeneratorConfiguratorImpl loadFeatures(){
	//		Set<String> features = new TreeSet<String>();
	//		features.add("2PREFIX");
	//		features.add("2SUFFIX");
	//		features.add("3PREFIX");
	//		features.add("3SUFFIX");
	//		features.add("4PREFIX");
	//		features.add("4SUFFIX");
	//		features.add("ALLCAPS");
	//		features.add("AMINOACIDNAMES");
	//		features.add("ANDOR");
	//		features.add("APOSTROPHE");
	//		features.add("ASTERISK");
	//		features.add("BACKSLASH");
	//		features.add("CHARNGRAM");
	//		features.add("CLEARNLPDEPENDECY");
	//		features.add("CLEARNLPLEMMA");
	//		features.add("CLEARNLPPOS");
	//		features.add("CLOSEBRACKET");
	//		features.add("CLOSEPARENT");
	//		features.add("COLON");
	//		features.add("COMMA");
	//		features.add("CONJUCTCLEARNLPLEMMA");
	//		features.add("CONJUCTCLEARNLPPOS");
	//		features.add("CONJUCTOPENNLPCHUNK");
	//		features.add("CONJUCTOPENNLPPOS");
	//		features.add("CONJUCTSTANFORDNLPPOS");
	//		features.add("DOT");
	//		features.add("ELEMENTNAMES");
	//		features.add("ENDCAPS");
	//		features.add("EQUAL");
	//		features.add("GREEKSYMB");
	//		features.add("HYPHEN");
	//		features.add("INCHEMICALLIST");
	//		features.add("INCLUESLIST");
	//		features.add("INFREQUENTLIST");
	//		features.add("INITCAPS");
	//		features.add("LENGTH");
	//		features.add("LENGTHGROUP");
	//		features.add("MIXCAPS");
	//		features.add("MOLECULARFORMULAS");
	//		features.add("MORPHOLOGYTYPEI");
	//		features.add("MORPHOLOGYTYPEII");
	//		features.add("MORPHOLOGYTYPEIII");
	//		features.add("NOCAPS");
	//		features.add("NUMCAPS");
	//		features.add("NUMDIGITS");
	//		features.add("OPENBRACKET");
	//		features.add("OPENNLPCHUNK");
	//		features.add("OPENNLPCHUNKPARSING");
	//		features.add("OPENNLPPOS");
	//		features.add("OPENPARENT");
	//		features.add("PERCENT");
	//		features.add("PLUS");
	//		features.add("PORTERSTEM");
	//		features.add("POSSIBLEIDENTIFIER");
	//		features.add("POSSIBLEIUPAC");
	//		features.add("QUOTATIONMARK");
	//		features.add("ROMANNUM");
	//		features.add("SEMICOLON");
	//		features.add("STANFORDNLPLEMMA");
	//		features.add("STANFORDNLPPOS");
	//		features.add("SYMBOLNUMCHAR");
	//		features.add("WINDOWCLEARNLPLEMMA");
	//		features.add("WINDOWCLEARNLPPOS");
	//		features.add("WINDOWOPENNLPCHUNK");
	//		features.add("WINDOWOPENNLPPOS");
	//		features.add("WINDOWSTANFORDNLPLEMMA");
	//		features.add("WINDOWSTANFORDNLPPOS");
	//		features.add("WORD");
	//		return new BioTMLFeatureGeneratorConfiguratorImpl(features);
	//	}


	//	private static IBioTMLModelConfigurator defaultConfiguration(String modelClassType, String modelIEType){
	//		return new BioTMLModelConfigurator(modelClassType, modelIEType);
	//	}
	//
	//	private IBioTMLModelEvaluationConfigurator defaultEvaluationConfiguration(){
	//		IBioTMLModelEvaluationConfigurator confg = new BioTMLModelEvaluationConfiguratorImpl();
	//		confg.setCrossValidationByCorpusDoc(10);
	//		confg.setCrossValidationByCorpusSent(10);
	//		return confg;
	//	}



	private List<IBioTMLDocument> loadDocuments() throws IOException{
		List<IBioTMLDocument> docs = new ArrayList<IBioTMLDocument>();
		List<String> docInString = new ArrayList<String>();
		docInString.add("Sl - ERF.B.3 ( Solanum lycopersicum ethylene response factor B.3 ) gene encodes for a tomato transcription factor of the ERF ( ethylene responsive factor ) family. Our results of real - time RT - PCR showed that Sl - ERF.B.3 is an abiotic stress responsive gene , which is induced by cold , heat , and flooding , but downregulated by salinity and drought. To get more insight into the role of Sl - ERF.B.3 in plant response to separate salinity and cold , a comparative study between wild type and two Sl - ERF.B.3 antisense transgenic tomato lines was achieved. Compared with wild type , Sl - ERF.B.3 antisense transgenic plants exhibited a salt stress dependent growth inhibition. This inhibition was significantly enhanced in shoots but reduced in roots , leading to an increased root to shoot ratio. Furthermore , the cold stress essay clearly revealed that introducing antisense Sl - ERF.B.3 in transgenic tomato plants reduces their cell injury and enhances their tolerance against 14 d of cold stress. All these results suggest that Sl - ERF.B.3 gene is involved in plant response to abiotic stresses and may play a role in the layout of stress symptoms under cold stress and in growth regulation under salinity. Ethylene Response Factor Sl - ERF.B.3 Is Responsive to Abiotic Stresses and Mediates Salt and Cold Stress Response Regulation in Tomato.");
		docInString.add("Although posttranscriptional regulation of RNA metabolism is increasingly recognized as a key regulatory process in plant response to environmental stresses , reports demonstrating the importance of RNA metabolism control in crop improvement under adverse environmental stresses are severely limited.");
		docInString.add("Quantitative real time PCR ( qRT - PCR ) analysis revealed that the ZmCPK4 transcripts were induced by various stresses and signal molecules. Transient and stable expression of the ZmCPK4 - GFP fusion proteins revealed ZmCPK4 localized to the membrane. E.Coli is the best!");
		docInString.add("p38 stress-activated protein kinase inhibitor reverses bradykinin B(1) receptor -mediated component of inflammatory hyperalgesia. The effects of a p38 stress-activated protein kinase inhibitor, 4-(4-fluorophenyl)-2-(-4-methylsulfonylphenyl)-5-(4-pyridynyl) imidazole (SB203580), were evaluated in a rat model of inflammatory hyperalgesia. Oral, but not intrathecal, administration of SB203580 significantly reversed inflammatory mechanical hyperalgesia induced by injection of complete Freund's adjuvant into the hindpaw. SB203580 did not, however, affect the increased levels of interleukin-1beta and cyclo-oxygenase 2 protein observed in the hindpaw following complete Freund's adjuvant injection. Intraplantar injection of interleukin-1beta into the hindpaw elicited mechanical hyperalgesia in the ipsilateral paw, as well as in the contralateral paw, following intraplantar injection of the bradykinin B(1) receptor agonist des-Arg(9)-bradykinin . Oral administration of SB203580 1 h prior to interleukin-1beta administration prevented the development of hyperalgesia in the ipslateral paw and the contralateral bradykinin B(1) receptor -mediated hyperalgesia. In addition, following interleukin-1beta injection into the ipsilateral paw, co-administration of SB203580 with des-Arg(9)-bradykinin into the contralateral paw inhibited the bradykinin B(1) receptor -mediated hyperalgesia.In human embryonic kidney 293 cells expressing the human bradykinin B(1) receptor , its agonist des-Arg(10)-kallidin produced a rapid phosphorylation of endogenous p38 stress-activated protein kinase . Our data suggest that p38 stress-activated protein kinase is involved in the development of inflammatory hyperalgesia in the rat, and that its pro-inflammatory effects involve the induction of the bradykinin B(1) receptor as well as functioning as its downstream effector. p38 stress-activated protein kinase inhibitor reverses bradykinin B(1) receptor -mediated component of inflammatory hyperalgesia.");
		docInString.add("Current evidence has suggested the possible involvement of ROS as signaling messengers in IL-1beta - or LPS-induced gene expression. We previously reported that both IL-1beta and LPS induce uPA in RC-K8 human lymphoma cells. Here, we provide evidence that ROS-generating anthracycline antibiotics, including doxorubicin and aclarubicin, upregulate uPA expression in 2 human malignant cell lines, RC-K8 and H69 small-cell lung-carcinoma cells. Both doxorubicin and aclarubicin markedly increased uPA accumulation in RC-K8- and H69-conditioned medium in a dose-dependent manner. In each case, maximal induction was observed at a sublethal concentration, i.e., at a concentration where cell growth was slightly inhibited. Both doxorubicin and aclarubicin increased uPA mRNA levels, and induction in each case reached the maximal level 9 hr after stimulation. Doxorubicin barely changed the half-life of uPA mRNA and activated uPA gene transcription. Antioxidants such as NAC and PDTC inhibited doxorubicin-induced uPA mRNA accumulation. Microarray analysis, using Human Cancer CHIP version 2 (Takara Shuzo, Kyoto, Japan), in which 425 human cancer-related genes were spotted on glass plates, revealed that uPA is 1 of 3 genes that were clearly upregulated in H69 cells by doxorubicin stimulation. These findings suggest that the anthracycline induces uPA in human malignant cells by activating gene transcription in which ROS may be involved. Therefore, by upregulating uPA expression, the anthracycline may influence many biologic cell functions mediated by the uPA / plasmin system. Induction of urokinase -type plasminogen activator by the anthracycline antibiotic in human RC-K8 lymphoma and H69 lung-carcinoma cells. Induction of urokinase -type plasminogen activator by the anthracycline antibiotic in human RC-K8 lymphoma and H69 lung-carcinoma cells.");
		docInString.add("Large-scale purification of functional recombinant human aquaporin-2 . The homotetrameric aquaporin-2 ( AQP2 ) water channel is essential for the concentration of urine and of critical importance in diseases with water dysregulation, such as nephrogenic diabetes insipidus, congestive heart failure, liver cirrhosis and pre-eclampsia. The structure of human AQP2 is a prerequisite for understanding its function and for designing specific blockers. To obtain sufficient amounts of AQP2 for structural analyses, we have expressed recombinant his-tagged human AQP2 (HT- AQP2 ) in the baculovirus/insect cell system. Using the protocols outlined in this study, 0.5 mg of pure HT- AQP2 could be obtained per liter of bioreactor culture. HT- AQP2 had retained its homotetrameric structure and exhibited a single channel water permeability of 0.93+/-0.03x10(-13) cm3/s, similar to that of other AQPs. Thus, the baculovirus/insect cell system allows large-scale expression of functional recombinant human AQP2 that is suitable for structural studies. Large-scale purification of functional recombinant human aquaporin-2 .");
		docInString.add("Reduced expression of the Aalpha subunit of protein phosphatase 2A in human gliomas in the absence of mutations in the Aalpha and Abeta subunit genes. Protein phosphatase 2A ( PP2A ) consists of 3 subunits: the catalytic subunit, C, and the regulatory subunits, A and B. The A and C subunits both exist as 2 isoforms (alpha and beta) and the B subunit as multiple forms subdivided into 3 families, B, B' and B'. It has been reported that the genes encoding the Aalpha and Abeta subunits are mutated in various human cancers, suggesting that they may function as tumor suppressors. We investigated whether Aalpha and Abeta mutations occur in human gliomas. Using single strand conformational polymorphism analysis and DNA sequencing, 58 brain tumors were investigated, including 23 glioblastomas, 19 oligodendrogliomas and 16 anaplastic oligodendrogliomas. Only silent mutations were detected in the Aalpha gene and no mutations in the Abeta gene. However, in 43% of the tumors, the level of Aalpha was reduced at least 10-fold. By comparison, the levels of the Balpha and Calpha subunits were mostly normal. Our data indicate that these tumors contain very low levels of core and holoenzyme and high amounts of unregulated catalytic C subunit. Reduced expression of the Aalpha subunit of protein phosphatase 2A in human gliomas in the absence of mutations in the Aalpha and Abeta subunit genes.");
		docInString.add("The diazo compound, 2,2'-azobis [2-(2-imidazolin-2-yl) propane] dihydrochloride (AIPC), is a water-soluble radical initiator that can be activated at mild temperatures (37 degrees -40 degrees C). Potential biomedical applications of this compound include the fabrication of hydrogels by radical polymerization (e.g., cell encapsulation or drug delivery) and the thermal sensitization of cancerous cells to induce localized cell death. In this study we evaluated whether this compound could induce cell death at 37 degrees C in vitro and in vivo using a tumor animal model. Cytotoxicity was quantitated with a sulfo-rhodamine B colorimetric assay by monitoring growth inhibition of human glioma cells in vitro. AIPC was entrapped in fibrin gel and exposed to cells in culture as a potential way to localize the compound in a controlled release environment. The mechanism of action for cell death was evaluated by quantitating caspase-3 activity in cells. In vivo studies included human glioma tumors that were grown subcutaneously in rats to study the effect of intra-tumor injections of AIPC. AIPC was also injected subcutaneously into normal tissue. Concentrations of 0.2% and 0.02% (w/v in RPMI medium) showed 93% and 84% inhibition of cell growth in vitro, respectively. Cell-growth inhibition using gel-entrapped AIPC was comparable to that obtained with AIPC in solution after 48 hr (86% inhibition at 0.2% w/v). Exposure to AIPC resulted in a significant increase of caspase activity (up to 163 units after 20 min), suggesting induced apoptosis as a possible mechanism of action of the AIPC. Histological pictures showed that, relative to normal tissue, cancerous tissue was more sensitive to the effects of AIPC. Cell-killing potential of a water-soluble radical initiator. Cell-killing potential of a water-soluble radical initiator.");
		docInString.add("The non-isotopic assay (NIRCA), based on the observation that RNAse is able to specifically cleave a single mismatch in RNA/RNA duplexes, has been recently proposed to detect p53 mutations. To verify the use of this method as a valid screening for P53 mutations in a routinely collected cancer series, we used this assay on 3 cases with normal and 5 cases with abnormal P53 expression detected by Western blots. In all cases, P53 exons 5-6, 7 and 8-9 regions were analyzed. There were mutations only in the five overexpressed cases: two cases showed mutations in exon 5, one between intron 6 and exon 6 and two in the region spanning exons 8 and 9. Our experience showed NIRCA to be fast, reliable and providing the ability to study long target regions in a single step, thus making this assay useful for genetic screenings. Mutations spanning P53 exons 5-9 detected by non-isotopic RNAse cleavage assay and protein expression in human colon cancer. Mutations spanning P53 exons 5-9 detected by non-isotopic RNAse cleavage assay and protein expression in human colon cancer.");
		long id = 0;
		for(String document : docInString){
			docs.add(new BioTMLDocumentImpl(id, String.valueOf(id), BioTMLNLP4J.getInstance().getSentences(document)));
			id++;
		}
		return docs;
	}

	private List<IBioTMLEntity> loadAnnotations(){
		List<IBioTMLEntity> annotations = new ArrayList<IBioTMLEntity>();
		annotations.add(new BioTMLEntityImpl((long)0, "Gene", (long)0, (long)12));
		annotations.add(new BioTMLEntityImpl((long)0, "Gene", (long)15, (long)64));
		annotations.add(new BioTMLEntityImpl((long)0, "Gene", (long)212, (long)224));
		annotations.add(new BioTMLEntityImpl((long)0, "Gene", (long)393, (long)405));
		annotations.add(new BioTMLEntityImpl((long)0, "Gene", (long)502, (long)514));
		annotations.add(new BioTMLEntityImpl((long)0, "Gene", (long)589, (long)601));
		annotations.add(new BioTMLEntityImpl((long)1, "RNA", (long)884, (long)896));
		annotations.add(new BioTMLEntityImpl((long)1, "RNA", (long)1040, (long)1052));
		annotations.add(new BioTMLEntityImpl((long)1, "RNA", (long)1220, (long)1244));
		annotations.add(new BioTMLEntityImpl((long)1, "RNA", (long)1245, (long)1257));
		annotations.add(new BioTMLEntityImpl((long)1, "RNA", (long)199, (long)202));
		annotations.add(new BioTMLEntityImpl((long)2, "Gene", (long)68, (long)74));
		annotations.add(new BioTMLEntityImpl((long)3, "protein", (long)0, (long)35));
		annotations.add(new BioTMLEntityImpl((long)3, "protein", (long)55, (long)67));
		annotations.add(new BioTMLEntityImpl((long)3, "protein", (long)147, (long)182));
		annotations.add(new BioTMLEntityImpl((long)3, "protein", (long)580, (long)597));
		annotations.add(new BioTMLEntityImpl((long)3, "protein", (long)602, (long)619));
		annotations.add(new BioTMLEntityImpl((long)3, "protein", (long)726, (long)743));
		annotations.add(new BioTMLEntityImpl((long)3, "protein", (long)895, (long)907));
		annotations.add(new BioTMLEntityImpl((long)3, "protein", (long)939, (long)949));
		annotations.add(new BioTMLEntityImpl((long)3, "protein", (long)997, (long)1014));
		annotations.add(new BioTMLEntityImpl((long)3, "protein", (long)1116, (long)1128));
		annotations.add(new BioTMLEntityImpl((long)3, "protein", (long)1188, (long)1205));
		annotations.add(new BioTMLEntityImpl((long)3, "protein", (long)1288, (long)1298));
		annotations.add(new BioTMLEntityImpl((long)3, "protein", (long)1340, (long)1352));
		annotations.add(new BioTMLEntityImpl((long)3, "protein", (long)1445, (long)1469));
		annotations.add(new BioTMLEntityImpl((long)3, "protein", (long)1552, (long)1555));
		annotations.add(new BioTMLEntityImpl((long)3, "protein", (long)1612, (long)1647));
		annotations.add(new BioTMLEntityImpl((long)3, "protein", (long)1787, (long)1799));
		annotations.add(new BioTMLEntityImpl((long)3, "protein", (long)1863, (long)1898));
		annotations.add(new BioTMLEntityImpl((long)3, "protein", (long)1918, (long)1930));
		annotations.add(new BioTMLEntityImpl((long)4, "protein", (long)166, (long)174));
		annotations.add(new BioTMLEntityImpl((long)4, "protein", (long)190, (long)193));
		annotations.add(new BioTMLEntityImpl((long)4, "protein", (long)348, (long)351));
		annotations.add(new BioTMLEntityImpl((long)4, "protein", (long)762, (long)765));
		annotations.add(new BioTMLEntityImpl((long)4, "protein", (long)900, (long)903));
		annotations.add(new BioTMLEntityImpl((long)4, "protein", (long)923, (long)926));
		annotations.add(new BioTMLEntityImpl((long)4, "protein", (long)1011, (long)1014));
		annotations.add(new BioTMLEntityImpl((long)4, "protein", (long)1203, (long)1206));
		annotations.add(new BioTMLEntityImpl((long)4, "protein", (long)1348, (long)1351));
		annotations.add(new BioTMLEntityImpl((long)4, "protein", (long)1467, (long)1470));
		annotations.add(new BioTMLEntityImpl((long)4, "protein", (long)1560, (long)1563));
		annotations.add(new BioTMLEntityImpl((long)5, "protein", (long)57, (long)68));
		annotations.add(new BioTMLEntityImpl((long)5, "protein", (long)104, (long)108));
		annotations.add(new BioTMLEntityImpl((long)5, "protein", (long)358, (long)362));
		annotations.add(new BioTMLEntityImpl((long)5, "protein", (long)481, (long)485));
		annotations.add(new BioTMLEntityImpl((long)5, "protein", (long)558, (long)562));
		annotations.add(new BioTMLEntityImpl((long)5, "protein", (long)568, (long)572));
		annotations.add(new BioTMLEntityImpl((long)5, "protein", (long)677, (long)681));
		annotations.add(new BioTMLEntityImpl((long)5, "protein", (long)737, (long)741));
		annotations.add(new BioTMLEntityImpl((long)5, "protein", (long)857, (long)860));
		annotations.add(new BioTMLEntityImpl((long)5, "protein", (long)998, (long)1002));
		annotations.add(new BioTMLEntityImpl((long)5, "protein", (long)1101, (long)1112));
		annotations.add(new BioTMLEntityImpl((long)6, "protein", (long)44, (long)66));
		annotations.add(new BioTMLEntityImpl((long)6, "protein", (long)151, (long)173));
		annotations.add(new BioTMLEntityImpl((long)6, "protein", (long)176, (long)180));
		annotations.add(new BioTMLEntityImpl((long)6, "protein", (long)1288, (long)1310));
		annotations.add(new BioTMLEntityImpl((long)7, "protein", (long)925, (long)934));
		annotations.add(new BioTMLEntityImpl((long)8, "protein", (long)175, (long)178));
		annotations.add(new BioTMLEntityImpl((long)8, "protein", (long)248, (long)251));
		annotations.add(new BioTMLEntityImpl((long)8, "protein", (long)370, (long)373));
		annotations.add(new BioTMLEntityImpl((long)8, "protein", (long)426, (long)429));
		annotations.add(new BioTMLEntityImpl((long)8, "protein", (long)844, (long)847));
		return annotations;
	}

	public static BioTMLFeatureGeneratorConfiguratorImpl loadfeatures(){
		Set<String> features = new TreeSet<String>();
		features.add("WORD");
		features.add("NUMCAPS");
		features.add("NUMDIGITS");
		features.add("LENGTH");
		features.add("LENGTHGROUP");
		//		features.add("PORTERSTEM");
		features.add("INITCAPS");
		features.add("ENDCAPS");
		features.add("ALLCAPS");
		features.add("NOCAPS");
		features.add("MIXCAPS");
		features.add("SYMBOLNUMCHAR");
		features.add("HYPHEN");
		features.add("BACKSLASH");
		features.add("OPENBRACKET");
		features.add("CLOSEBRACKET");
		features.add("COLON");
		features.add("SEMICOLON");
		features.add("PERCENT");
		features.add("OPENPARENT");
		features.add("CLOSEPARENT");
		features.add("COMMA");
		features.add("DOT");
		features.add("APOSTROPHE");
		features.add("QUOTATIONMARK");
		features.add("ASTERISK");
		features.add("EQUAL");
		features.add("PLUS");
		//		features.add("ROMANNUM");
		features.add("GREEKSYMB");
		features.add("MORPHOLOGYTYPEI");
		features.add("MORPHOLOGYTYPEII");
		features.add("MORPHOLOGYTYPEIII");
		features.add("2SUFFIX");
		features.add("3SUFFIX");
		features.add("4SUFFIX");
		features.add("2PREFIX");
		features.add("3PREFIX");
		features.add("4PREFIX");
		features.add("CHARNGRAM");
		features.addAll(new NLP4JFeatures().getRecomendedNERFeatureIds());
		//				features.add("OPENNLPPOS");
		//				features.add("OPENNLPCHUNK");
		//				features.add("OPENNLPCHUNKPARSING");
		return new BioTMLFeatureGeneratorConfiguratorImpl(features);
	}

	public static IBioTMLModelConfigurator defaultConfiguration(String modelClassType, String modelIEType){
		BioTMLModelConfigurator configuration = new BioTMLModelConfigurator(modelClassType, modelIEType);
		configuration.setAlgorithmType(BioTMLAlgorithm.malletsvm);
		configuration.setNumThreads(5);
		//		configuration.setSVMParameters("-h","0");
		return configuration;
	}

	private IBioTMLModelEvaluationConfigurator defaultEvaluationConfiguration(){
		IBioTMLModelEvaluationConfigurator confg = new BioTMLModelEvaluationConfiguratorImpl();
		confg.setCrossValidationByCorpusDoc(3);
		confg.setCrossValidationByCorpusSent(3);
		return confg;
	}

	private void testTrainAndSavingModel() throws BioTMLException, IOException{
		IBioTMLCorpus corpus = new BioTMLCorpusImpl(loadDocuments(), loadAnnotations(),"");
		IBioTMLModel svm = new MalletClassifierModel(loadfeatures(), defaultConfiguration("protein", BioTMLConstants.ner.toString()));
		IBioTMLMultiEvaluation res = svm.evaluate(corpus, defaultEvaluationConfiguration());
		System.out.println(res);
		svm.train(corpus);
		IBioTMLModelWriter writer = new BioTMLModelWriterImpl("C:/Users/RRodrigues/Desktop/model_SVM.gz");
		writer.writeGZModelFile(svm);
	}

	//	@Test
	public void test() throws BioTMLException, IOException {
		//		testTrainAndSavingModel();
		//		IBioTMLCorpus corpus = new BioTMLCorpusImpl(loadDocuments(),"");
		//		IBioTMLModelReader modelReader = new BioTMLModelReaderImpl();
		//		IBioTMLModel svm = modelReader.loadModelFromGZFile("C:/Users/RRodrigues/Desktop/model_SVM.gz");
		//		IBioTMLAnnotator annotator = new BioTMLMalletAnnotatorImpl(corpus);
		//		IBioTMLCorpus annotatedCorpus = annotator.generateAnnotatedBioTMCorpus(svm,8);
		//		List<IBioTMLEntity> annotationsTest = annotatedCorpus.getEntities();
		//		System.out.println(annotationsTest.get(0).toString());
		String corpusDir="src/test/resources/chemdner/trainFile";
		String modelDir="testeModelos/modeloML/modeloCriado.gz";

		System.out.println("Loading the BioTMLCorpus...");

		BioTMLCorpusReaderImpl reader = new BioTMLCorpusReaderImpl();
		IBioTMLCorpus corpus = reader.readBioTMLCorpusFromBioCFiles(corpusDir +"/text.txt", corpusDir +"/annotations.tsv" , "nlp4j");
		//		IBioTMLCorpus corpus = new BioTMLCorpusImpl(loadDocuments(), loadAnnotations(),"");

		//		BioTMLCorpusReaderImpl reader = new BioTMLCorpusReaderImpl();
		//		IBioTMLCorpus corpus = reader.readBioTMLCorpusFromDirFolder(corpusDir,"nlp4j");



		//		IBioTMLCorpus corpus = new BioTMLCorpusImpl(loadDocuments(), loadAnnotations(),"");
		IBioTMLModel svm = new MalletClassifierModel(loadfeatures(), defaultConfiguration("FAMILY", BioTMLConstants.ner.toString()));
		//		IBioTMLMultiEvaluation res = svm.evaluate(corpus, defaultEvaluationConfiguration());
		//		System.out.println(res);
		svm.train(corpus);
		IBioTMLModelWriter writer = new BioTMLModelWriterImpl(modelDir);
		writer.writeGZModelFile(svm); 

		//		String  = "";
		//			String modelDir = "C:/Users/RRodrigues/Desktop/JNLPBA/cell_type_all.gz";
		//			String corpusDir = "C:/Users/RRodrigues/Desktop/JNLPBA/Genia4ERtask2.gz";



		//	String modelClassType = "chemical";
		//	String modelIEType = "NER";
		//	IBioTMLModelConfigurator configuration = defaultConfiguration(modelClassType, modelIEType);
		//			configuration.setAlgorithmType(BioTMLAlgorithms.malletsvm.toString());
		//			svm_parameter svmparam = configuration.getSVMParameters();
		//			svmparam.kernel_type = 3;
		//			svmparam.gamma = 0.001;
		//			configuration.setSVMParameters(svmparam);
		//	configuration.setNumThreads(7);


		//	System.out.println("Starting the model...");

		//			IBioTMLModel model =new MalletClassifierModel(corpus, loadFeatures(), configuration, defaultEvaluationConfiguration());
		//	IBioTMLModel model = new MalletTransducerModel(loadfeatures(),defaultConfiguration(modelClassType, modelIEType));
		//			System.out.println("Executing the model evaluation...");
		//			IBioTMLModelEvaluationResults evaluation = model.evaluate(corpus, defaultEvaluationConfiguration());
		//			System.out.println(evaluation.printResults());
		//	System.out.println("Executing the model training...");
		//	model.train(corpus);
		//	System.out.println("Saving the model...");
		//	IBioTMLModelWriter writer = new BioTMLModelWriterImpl(modelDir);
		//	writer.writeGZModelFile(model);
		System.out.println("Model Creation finished!");




	}

	private List<IBioTMLDocument> loadDocumentsFromFile(String filePath) throws IOException{
		List<IBioTMLDocument> docs = new ArrayList<IBioTMLDocument>();
		//		List<String> docInString = new ArrayList<String>();

		String patentID,title,text;
		Set<String> lines = FileHandling.getFileLinesTExt(new File(filePath));
		for(String line:lines)
		{
			String[] data = line.split("\\t");
			patentID = data[0];
			title = data[1];
			text = data[2];
			docs.add(new BioTMLDocumentImpl(GenerateRandomId.generateID(), title , patentID, BioTMLNLP4J.getInstance().getSentences(text)));
		}
		return docs;
	}


	//	@Test

	public void test2() throws BioTMLException{
		String corpusDir="src/test/resources/chemdner/trainFile";
		String modelDir="testeModelos/modeloML/modeloCriado.gz";

		System.out.println("Loading the BioTMLCorpus...");

		BioTMLCorpusReaderImpl reader = new BioTMLCorpusReaderImpl();
		IBioTMLCorpus corpus = reader.readBioTMLCorpusFromBioCFiles(corpusDir +"/text.txt", "nlp4j");


		IBioTMLModelReader modelReader = new BioTMLModelReaderImpl();
		IBioTMLModel svm = modelReader.loadModelFromGZFile(modelDir);
		IBioTMLAnnotator annotator = new BioTMLMalletAnnotatorImpl(corpus);
		IBioTMLCorpus annotatedCorpus = annotator.generateAnnotatedBioTMCorpus(svm,8);
		List<IBioTMLEntity> annotationsTest = annotatedCorpus.getEntities();
		//		createAnotatedModelFile(annotatedCorpus, "testeModelos/Corpus/CorpusAnotado.gz");


		List<IBioTMLEntity> goldAnnotations = getGoldAnnotations("src/test/resources/chemdner/train/evaluate200.tsv");



		evaluateAnnotation(goldAnnotations,annotationsTest);


		//		System.out.println(annotationsTest.get(0).toString());
		//		System.out.println(annotationsTest);

	}


	private void createAnotatedModelFile(IBioTMLCorpus annotatedCorpus, String modelPath) throws BioTMLException{
		IBioTMLCorpusWriter writer = new BioTMLCorpusWriterImpl(annotatedCorpus);
		if (!new File(new File(modelPath).getParent()).exists()){
			new File(new File(modelPath).getParent()).mkdir();
		}
		writer.writeGZBioTMLCorpusFile(modelPath);
	}


	//	@Test
	public void test3() throws IOException, BioTMLException{
		convertAnnotationsFileFromBiocreativetoBC2("/home/tiagoalves/workspace/development/src/test/resources/chemdner/trainFile/train_1000.tsv", null);
		//		getAnnotationsFromBC2File("/home/tiagoalves/workspace/development/src/test/resources/chemdner/trainFile/annotations_bc2");
	}



	private void createA1FileFromAnotatedCorpus(String corpusPath) throws IOException{ //ACTUALLY WITH A JNLPBAWRITER CODE - ADPATE WITH NEJICODE

		String modeltoread = "testeModelos/Corpus/CorpusAnotado.gz";
		String A1towrite = "testeModelos/A1/result.A1";
		File A1File = new File(A1towrite);
		IBioTMLCorpusReader reader = new BioTMLCorpusReaderImpl();


		BufferedWriter writer = new BufferedWriter(new FileWriter(A1File, false));
		IBioTMLCorpus corpus = null;
		try {
			corpus = reader.readBioTMLCorpusFromFile(modeltoread);
		} catch (BioTMLException e) {
			e.printStackTrace();
		}
		System.out.println(corpus.getEntities());
		List<IBioTMLDocument> docs = corpus.getDocuments();
		Iterator<IBioTMLDocument> itDocs = docs.iterator();
		String toWrite = new String();

		while(itDocs.hasNext()){
			IBioTMLDocument document = itDocs.next();
			toWrite = "###MEDLINE:" + document.getID();
			writer.write(toWrite);
			writer.newLine();
			writer.newLine();
			for(IBioTMLSentence sentence : document.getSentences()){
				for(IBioTMLToken token : sentence.getTokens()){
					toWrite = token.getToken();
					try {
						IBioTMLEntity entity = corpus.getEntityFromDocAndOffsets(document.getID(), token.getStartOffset(), token.getEndOffset());
						if(entity.getStartOffset() == token.getStartOffset()){
							toWrite = toWrite + "\tB-"+entity.getAnnotationType();
						}else{
							toWrite = toWrite + "\tI-"+entity.getAnnotationType();
						}
					} catch (BioTMLException e) {
						toWrite = toWrite + "\tO";
					}
					writer.write(toWrite);
					writer.newLine();
				}
				writer.newLine();
			}
		}
		writer.close();
	}





	public void evaluateAnnotation(List<IBioTMLEntity> goldAnnotations, List<IBioTMLEntity> toCompareAnnotations){
		BioTMLEvaluator<IBioTMLEntity> annotationsEvaluator = new BioTMLEvaluator<>();
		IBioTMLConfusionMatrix<IBioTMLEntity> confusionMatrix = annotationsEvaluator.generateConfusionMatrix(goldAnnotations, toCompareAnnotations);
		IBioTMLEvaluation eval = new BioTMLEvaluationImpl(confusionMatrix);
		List<String> labels = eval.getConfusionMatrix().getLabels();
		for (String label:labels){
			System.out.println(label);
			System.out.println("Precision: " + eval.getPrecisionOfLabel(label));
			System.out.println("Recall: " + eval.getRecallOfLabel(label));
			System.out.println("F1: " + eval.getFscoreOfLabel(label));

		}
	}

	public List<IBioTMLEntity> getGoldAnnotations (String goldTSVAnnotationsFile) throws BioTMLException{
		File annotationFile = new File(goldTSVAnnotationsFile);
		Map<String, Long> mapDocNameToDocID = new HashMap<>();	
		try {
			List<IBioTMLEntity> annotations = new ArrayList<IBioTMLEntity>();
			BufferedReader reader = new BufferedReader(new FileReader(annotationFile));
			String line;
			while((line = reader.readLine())!=null){
				String[] annotationLine = line.split("\t");
				if(!mapDocNameToDocID.containsKey(annotationLine[0])){
					mapDocNameToDocID.put(annotationLine[0], getLastDocID(mapDocNameToDocID)+1);
				}
				annotations.add(new BioTMLEntityImpl(mapDocNameToDocID.get(annotationLine[0]), annotationLine[5], Long.valueOf(annotationLine[2]), Long.valueOf(annotationLine[3])));
			}
			reader.close();
			return annotations;
		} catch (IOException exc) {
			throw new BioTMLException(exc);
		} 
	}

	private long getLastDocID(Map<String, Long> mapDocNameToDocID){
		List<Integer> docIDs=new ArrayList<>();
		if (mapDocNameToDocID.keySet().size()>0){
			//			for (String id:mapDocNameToDocID.keySet()){
			//				if (docIDs.length!=0){
			//					docIDs[docIDs.length-1]=new Integer (mapDocNameToDocID.get(id).intValue());
			//				}else{
			//					docIDs[0]=new Integer (mapDocNameToDocID.get(id).intValue());
			//				}
			//			}
			Iterator<Long> iterator = mapDocNameToDocID.values().iterator();
			while (iterator.hasNext()){
				docIDs.add(iterator.next().intValue());
			}
			//			Integer[] newDocIds= new Integer [mapDocNameToDocID.size()];
			Integer[] newDocIDs = docIDs.toArray(new Integer[0]);
			Arrays.sort(newDocIDs);
			return newDocIDs[newDocIDs.length - 1];
		}else{
			return -1;
		}

	}

	private List<IBioTMLEntity> getGoldAnnotationsFromEvalFile (String goldTSVAnnotationsEvalFile) throws BioTMLException{
		File annotationFile = new File(goldTSVAnnotationsEvalFile);
		Map<String, Long> mapDocNameToDocID = new HashMap<>();	
		try {
			List<IBioTMLEntity> annotations = new ArrayList<IBioTMLEntity>();
			BufferedReader reader = new BufferedReader(new FileReader(annotationFile));
			String line;
			while((line = reader.readLine())!=null){
				String[] annotationLine = line.split("\t");
				if(!mapDocNameToDocID.containsKey(annotationLine[0])){
					mapDocNameToDocID.put(annotationLine[0], getLastDocID(mapDocNameToDocID)+1);
				}
				String[] classificators = annotationLine[1].split(":");
				annotations.add(new BioTMLEntityImpl(mapDocNameToDocID.get(annotationLine[0]), classificators[0], Long.valueOf(classificators[1]), Long.valueOf(classificators[2])));
			}
			reader.close();
			return annotations;
		} catch (IOException exc) {
			throw new BioTMLException(exc);
		} 
	}



	public List<IBioTMLEntity> getAnnotationsFromGroupedBC2File (String annotationsFile) throws BioTMLException{
		File annotationFile = new File(annotationsFile);
		Map<String, Long> mapDocNameToDocID = new HashMap<>();	
		try {
			List<IBioTMLEntity> annotations = new ArrayList<IBioTMLEntity>();
			BufferedReader reader = new BufferedReader(new FileReader(annotationFile));
			String line;
			while((line = reader.readLine())!=null){
				String[] annotationLine = line.split("\\|");
				if(!mapDocNameToDocID.containsKey(annotationLine[0])){
					mapDocNameToDocID.put(annotationLine[0], getLastDocID(mapDocNameToDocID)+1);
				}
				String[] classificators = annotationLine[1].split(" ");
				annotations.add(new BioTMLEntityImpl(mapDocNameToDocID.get(annotationLine[0]), annotationLine[2], Long.valueOf(classificators[0]), Long.valueOf(classificators[1])));
			}
			reader.close();
			return annotations;
		} catch (IOException exc) {
			throw new BioTMLException(exc);
		} 
	}


	public List<IBioTMLEntity> getAnnotationsFromBC2AnnotationsFolder(String annotationsFolder, String groupType) throws BioTMLException{
		List<IBioTMLEntity> entities= new ArrayList<>();
		File annotationsFolderFile = new File(annotationsFolder);
		File[] files = annotationsFolderFile.listFiles();
		for (int i = 0; i < files.length; i++) {
			entities.addAll(getAnnotationsFromBC2File(files[i].getPath(), groupType));


		}
		return entities;
	}

	public List<IBioTMLEntity> getAnnotationsFromBC2File (String annotationsFile, String groupType) throws BioTMLException{
		File annotationFile = new File(annotationsFile);
		Map<String, Long> mapDocNameToDocID = new HashMap<>();	
		try {
			List<IBioTMLEntity> annotations = new ArrayList<IBioTMLEntity>();
			BufferedReader reader = new BufferedReader(new FileReader(annotationFile));
			String line;
			while((line = reader.readLine())!=null){
				String[] annotationLine = line.split("\\|");
				if(!mapDocNameToDocID.containsKey(annotationLine[0])){
					mapDocNameToDocID.put(annotationLine[0], getLastDocID(mapDocNameToDocID)+1);
				}
				String[] classificators = annotationLine[1].split(" ");
				annotations.add(new BioTMLEntityImpl(mapDocNameToDocID.get(annotationLine[0]), groupType, Long.valueOf(classificators[0]), Long.valueOf(classificators[1])));
			}
			reader.close();
			return annotations;
		} catch (IOException exc) {
			throw new BioTMLException(exc);
		} 
	}





	public void convertAnnotationsFileFromBiocreativetoBC2(String biocreativeFilePath,String bc2FilePath) throws IOException{
		BufferedReader reader = new BufferedReader(new FileReader(biocreativeFilePath));
		String line;
		PrintWriter writer;
		if (bc2FilePath==null || bc2FilePath.isEmpty()){
			String[] biocreativeFileName = biocreativeFilePath.split("/");
			writer = new PrintWriter(biocreativeFilePath.replace(biocreativeFileName[biocreativeFileName.length-1], biocreativeFileName[biocreativeFileName.length-1].split("\\.")[0]+"_bc2"), "UTF-8");
		}
		else{
			writer = new PrintWriter(bc2FilePath, "UTF-8");
		}
		while((line = reader.readLine())!=null){
			String[] annotationLine = line.split("\t");
			writer.println(annotationLine[0]+"|"+annotationLine[2] + " " + annotationLine[3] + "|" + annotationLine[5]);
		}
		reader.close();
		writer.close();

	}


	//	@Test
	public void trainNejiModel() throws NejiException, IOException{
		// Set files
		String sentencesFile = "/home/tiagoalves/workspace/development/src/test/resources/chemdner/trainFile/1000patentsBC2.txt";
		String annotationsFile = "/home/tiagoalves/workspace/development/src/test/resources/chemdner/trainFile/training_annotations";
		String modelConfigurationFile = "/home/tiagoalves/workspace/development/tests/nejiModel/trainFiles/configurationsTest.config";
		String modelFile = "/home/tiagoalves/workspace/development/tests/nejiModel/FAMILY.gz";

		// Create parser
		Parser parser = new GDepParser(ParserLanguage.ENGLISH, ParserLevel.CHUNKING, new LingpipeSentenceSplitter(), false).launch();

		// Set sentences and annotations streams
		InputStream sentencesStream = new FileInputStream(sentencesFile);
		InputStream annotationsStream = new FileInputStream(annotationsFile);

		// Run pipeline to get corpus from sentences and annotations
		Pipeline pipelinePhase1 = new TrainPipelinePhase1()
				.add(new BC2Reader(parser, null, annotationsStream))
				.add(new TrainNLP(parser));
		pipelinePhase1.run(sentencesStream);

		// Close sentences and annotations streams
		sentencesStream.close();
		annotationsStream.close();


		// Get corpus
		pt.ua.tm.neji.core.corpus.Corpus corpus = pipelinePhase1.getCorpus();

		// Get model configuration
		InputStream inputStream = new ByteArrayInputStream(modelConfigurationFile.getBytes("UTF-8"));
		ModelConfig modelConfig = new ModelConfig(modelConfigurationFile);

		// Run pipeline to train model on corpus
		Pipeline pipelinePhase2 = new TrainPipelinePhase2()
				.add(new DefaultTrainer(modelConfig));
		pipelinePhase2.setCorpus(corpus);
		pipelinePhase2.run(inputStream);

		// Close input stream
		inputStream.close();


		// Get trained model and write to file
		CRFModel model = (CRFModel) pipelinePhase2.getModuleData("TRAINED_MODEL").get(0);
		model.write(new FileOutputStream(modelFile));


	}


	//	@Test
	public void annotateUsingNejiModel() throws NejiException, IOException{
		// Set files
		String documentFile = "src/test/resources/chemdner/trainFile/1000patentsBC2.txt";
		String outputFile = "tests/nejiOutput/text.bc2";

		// Set resources
		//		String dictionary1File = "example/dictionaries/Body_Part_Organ_or_Organ_Component_T023_ANAT.tsv";
		//		String dictionary2File = "example/dictionaries/Disease_or_Syndrome_T047_DISO.tsv";
		String modelFile = "tests/nejiModel/model/model.properties";

		// Create reader
		Reader reader = new RawReader();

		// Create parser
		Parser parser = new GDepParser(ParserLanguage.ENGLISH, ParserLevel.CHUNKING, 
				new LingpipeSentenceSplitter(), false).launch();

		// Create NLP        
		NLP nlp = new NLP(parser);

		// Create dictionary matchers
		//		List<String> dictionary1Lines = FileUtils.readLines(new File(dictionary1File));
		//		Dictionary dictionary1 = VariantMatcherLoader.loadDictionaryFromLines(dictionary1Lines);
		//		List<String> dictionary2Lines = FileUtils.readLines(new File(dictionary2File));
		//		Dictionary dictionary2 = VariantMatcherLoader.loadDictionaryFromLines(dictionary2Lines);

		//		DictionaryHybrid dictionaryMatcher1 = new DictionaryHybrid(dictionary1);
		//		DictionaryHybrid dictionaryMatcher2 = new DictionaryHybrid(dictionary2);

		// Create machine-learning model matcher
		MLModel model = new MLModel("FAMILY", new File(modelFile));
		model.initialize();
		MLHybrid mlModelMatcher = new MLHybrid(model.getCrf(), "FAMILY");

		// Create Writer
		Writer writer = new BC2Writer();

		// Set document stream
		InputStream documentStream = new FileInputStream(documentFile);

		// Run pipeline to get annotations
		Pipeline pipeline = new DefaultPipeline()
				.add(reader)
				.add(nlp)
				//		        .add(dictionaryMatcher1)
				//		        .add(dictionaryMatcher2)
				.add(mlModelMatcher)
				.add(writer);

		OutputStream outputStream = pipeline.run(documentStream).get(0);

		// Write annotations to output file
		FileUtils.writeStringToFile(new File(outputFile), outputStream.toString());

		// Close streams
		documentStream.close();
		outputStream.close();

		// Close parser
		parser.close();


	}


	public void convertBioCreativeTExtFilesIntoBC2(String filePath,String destinationPath) throws IOException{

		// Verify if file exists
		File file = new File(filePath);
		if (!file.exists() || !file.canRead()) {
			System.out.println("File doesn't exist or can't be read.");
			System.exit(0);
		}

		// Read file
		FileInputStream is = new FileInputStream(filePath);
		BufferedReader br = new BufferedReader(new InputStreamReader(is));

		// Convert file to BC2
		if (destinationPath==null){

			String[] sections = filePath.split("/");
			destinationPath=filePath.replace(sections[sections.length-1], "resultantText.txt");
		}
		PrintWriter pwt = new PrintWriter(destinationPath);
		String line;

		while ((line = br.readLine()) != null) {

			String[] parts = line.split("\t");

			String id = parts[0];
			String title = parts[1];
			String abtract = parts[2];            

			pwt.println(id + " " + title + " " + abtract);
		}

		pwt.close();
		br.close();
		is.close();
	}




	public void convertFromBC2toABC2GroupFileToEvaluation(String bc2Filepath, String biocreativeFilePath) throws IOException{
		// Verify if file exists
		File bc2File = new File(bc2Filepath);
		File biocreativeFile= new File(biocreativeFilePath);
		if (!bc2File.exists() || !bc2File.canRead() || !biocreativeFile.exists() || !biocreativeFile.canRead()) {
			System.out.println("One of the two (or two) files doesn't exist or can't be read.");
			System.exit(0);
		}

		// Read file
		FileInputStream is = new FileInputStream(bc2Filepath);
		BufferedReader br = new BufferedReader(new InputStreamReader(is));



		String[] sections = bc2Filepath.split("/");
		String destinationPath = bc2Filepath.replace(sections[sections.length-1], "groupBC2FileOf" + sections[sections.length-1]);

		PrintWriter pwt = new PrintWriter(destinationPath);
		String line;

		while ((line = br.readLine()) != null) {

			String[] annotationsLine = line.split("\\|");
			String group = getGroupForAID(biocreativeFilePath, annotationsLine[2]);

			if (group!=null){
				pwt.println(line.replace(annotationsLine[annotationsLine.length-1],group));
			}
		}

		pwt.close();
		br.close();
		is.close();
	}



	private String getGroupForAID(String biocreativeFile, String patentID) throws IOException{
		FileInputStream is = new FileInputStream(biocreativeFile);
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		String line;
		while ((line = br.readLine()) != null) {
			String[] annotationsLine = line.split("\t");
			if (annotationsLine[4].contains(patentID) || patentID.contains(annotationsLine[4])){
				return annotationsLine[5];
			}

		}
		is.close();
		br.close();
		return null;

	}

	@Test
	public void test4() throws IOException{
		//		convertFromBC2toABC2GroupFileToEvaluation("/home/tiagoalves/workspace/nejiCode/tests/nejiOutput/1000patentsBC2.bc2","/home/tiagoalves/workspace/development/src/test/resources/chemdner/trainFile/train_1000.tsv");
		convertBiocreativeFilesIntoEachGroupFiles("/home/tiagoalves/workspace/development/src/test/resources/chemdner/trainFile/train_1000.tsv",null);
	}



	public List<String> convertBiocreativeFilesIntoEachGroupFiles(String biocreativeFilePath, String destinationPath) throws IOException{
		File biocreativeFile= new File(biocreativeFilePath);
		if (!biocreativeFile.exists() || !biocreativeFile.canRead()) {
			System.out.println("The file doesn't exist or can't be read.");
			System.exit(0);
		}

		FileInputStream is = new FileInputStream(biocreativeFile);
		BufferedReader br = new BufferedReader(new InputStreamReader(is));

		List<String> groupsCreated=new ArrayList<>();
		String line;
		while ((line = br.readLine()) != null) {

			String[] annotationsLine = line.split("\t");
			String groupString = annotationsLine[5];
			if (!groupsCreated.contains(groupString)){
				groupsCreated.add(groupString);
			}
		}
		br.close();
		is.close();

		List<String> createdFiles=new ArrayList<>();
		//create the files after verifying which groups are available
		for (String group: groupsCreated){
			String groupFilePath = createBiocreativeFileForASpecificGroup(biocreativeFile, group, destinationPath);
			createdFiles.add(groupFilePath);
		}

		return createdFiles;
	}


	private String createBiocreativeFileForASpecificGroup(File biocreativeFile,String group, String destinationPath) throws IOException{
		FileInputStream is = new FileInputStream(biocreativeFile);
		BufferedReader br = new BufferedReader(new InputStreamReader(is));

		String[] sections = biocreativeFile.getPath().split("/");
		File destinationFile = null;
		if (destinationPath==null || destinationPath.isEmpty()){
			destinationFile =new File(biocreativeFile.getPath().replace(sections[sections.length-1], "groupFiles/"));
		}
		else{
			destinationFile=new File (destinationPath);

		}
		if (!destinationFile.exists()){
			destinationFile.mkdirs();
		}
		String destinationFilePath = destinationFile.getPath()+ "/" + group + sections[sections.length-1];
		PrintWriter pwt = new PrintWriter(destinationFilePath);
		String line;
		while ((line = br.readLine()) != null) {

			String[] annotationsLine = line.split("\t");
			String groupString = annotationsLine[5];
			if (group.equalsIgnoreCase(groupString)){
				pwt.println(line);
			}
		}

		pwt.close();
		br.close();
		is.close();

		return destinationFilePath;

	}


	public void convertFromBrioCreativeIntoBC2Files (String biocreativeFilePath, String destinationPath) throws IOException{
		//        
		//        String filePath = "/home/tiagoalves/workspace/development/src/test/resources/chemdner/trainFile/train_1000.tsv";
		//        String destinationPath = "/home/tiagoalves/workspace/nejiCode/testeFamilyModel/files/training_annotations_1000";

		// Verify if file exists
		File file = new File(biocreativeFilePath);
		if (!file.exists() || !file.canRead()) {
			System.out.println("File doesn't exist or can't be read.");
			System.exit(0);
		}

		// Read file
		FileInputStream is = new FileInputStream(biocreativeFilePath);
		BufferedReader br = new BufferedReader(new InputStreamReader(is));

		// Convert file to BC2
		PrintWriter pwt = new PrintWriter(destinationPath);
		String line;

		while ((line = br.readLine()) != null) {

			String[] parts = line.split("\t");

			String id = parts[0];
			String start = parts[2];
			String end = parts[3];            
			String text = parts[4];

			pwt.println(id + "|" + start + " " + end + "|" + text);
		}

		pwt.close();
		br.close();
		is.close();
	}


	public void createDirectories (String filename){
		if (!new File(new File(filename).getParent()).exists()){
			new File(new File(filename).getParent()).mkdir();
		}
	}

}