public class VPageTable{
	static VPageEntry[] entries;
	static int found;

	public VPageTable(){
		//init Table
		entries = new VPageEntry[256];
		for(int i=0;i<entries.length;i++)
			this.entries[i] = new VPageEntry();
	}

	public static boolean isPresent(String page){
		Integer index = Integer.parseInt(page, 16);
		if(entries[index].PageFrameNumber!=-1){
			found=index;
			return true;
		}
		return false;
	}

	public static int getPageFrameNumber(){
		return entries[found].PageFrameNumber;
	}

	public static void addPageEntry(String page, int pageFrameNumber){
		Integer index = Integer.parseInt(page, 16);
		entries[index].V=1;
		entries[index].R=1;
		entries[index].PageFrameNumber=pageFrameNumber;
	}
}

class VPageEntry{
	int V,R,D;
	int PageFrameNumber;

	public VPageEntry(){
		this.V=0;
		this.R=0;
		this.D=0;
		this.PageFrameNumber=-1;
	}
}