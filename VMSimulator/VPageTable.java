public class VPageTable{
	VPageEntry[] entries;
	public VPageTable(){
		//init Table
		entries = new VPageEntry[256];
		for(int i=0;i<entries.length;i++)
			this.entries[i] = new VPageEntry();
	}
}

class VPageEntry{
	int V,R,D;
	String PageFrameNumber;

	public VPageEntry(){
		this.V=0;
		this.R=0;
		this.D=0;
		this.PageFrameNumber="";
	}
}