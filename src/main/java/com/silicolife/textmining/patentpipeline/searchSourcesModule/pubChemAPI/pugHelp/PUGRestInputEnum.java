package com.silicolife.textmining.patentpipeline.searchSourcesModule.pubChemAPI.pugHelp;

public enum PUGRestInputEnum {

	compoundName{
		@Override
		public String toString() {
			return "name";
		}
	},
	compoundIdentifier{
		@Override
		public String toString() {
			return "cid";
		}
	},
	
	smiles{
		@Override
		public String toString() {
			return "smiles";
		}
	},

	inchikey{
		@Override
		public String toString() {
			return "inchikey";
		}
	}
	
}