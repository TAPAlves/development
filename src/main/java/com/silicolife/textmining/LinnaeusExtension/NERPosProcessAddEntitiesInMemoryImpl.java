package com.silicolife.textmining.LinnaeusExtension;

import java.util.ArrayList;
import java.util.List;

import com.silicolife.textmining.core.datastructures.documents.AnnotatedDocumentImpl;
import com.silicolife.textmining.core.interfaces.core.annotation.IEntityAnnotation;
import com.silicolife.textmining.core.interfaces.core.dataaccess.exception.ANoteException;
import com.silicolife.textmining.core.interfaces.core.document.IAnnotatedDocument;
import com.silicolife.textmining.core.interfaces.core.document.IPublication;
import com.silicolife.textmining.core.interfaces.process.IE.IIEProcess;
import com.silicolife.textmining.processes.ie.ner.datatstructures.INERPosProccessAddEntities;

public class NERPosProcessAddEntitiesInMemoryImpl implements INERPosProccessAddEntities{

	List<IAnnotatedDocument> annotatedDocs;

	public NERPosProcessAddEntitiesInMemoryImpl() {
		this.annotatedDocs=new ArrayList<>();	
	}

	@Override
	public void addAnnotatedDocumentEntities(IIEProcess process, IPublication document,
			List<IEntityAnnotation> newEntityAnnotations) throws ANoteException {
		IAnnotatedDocument annotedDocument = new AnnotatedDocumentImpl(document, process, process.getCorpus());
		annotedDocument.setEntities(newEntityAnnotations);
		this.annotatedDocs.add(annotedDocument);
	}

	public List<IAnnotatedDocument> getAnnotatedDocs(){
		return annotatedDocs;
	}

}

