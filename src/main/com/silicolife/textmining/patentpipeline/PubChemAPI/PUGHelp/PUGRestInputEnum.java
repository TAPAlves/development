package main.com.silicolife.textmining.patentpipeline.PubChemAPI.PUGHelp;

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
	}

}
