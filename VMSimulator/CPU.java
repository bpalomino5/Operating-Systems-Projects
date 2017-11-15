public class CPU{
	public MMU mmu;

	public CPU(){
		mmu = new MMU();
	}

	public void handleProcess(int condition, String address, String value){
		if(condition==0) mmu.fetch(address);
		else if(condition==1) mmu.write(address,value);
		else System.exit(1);
	}
}

class MMU{
	public TLB tlb;

	public MMU(){
		//Creating TLB for usage throughout CPU lifetime
		this.tlb = new TLB();
	}

	public void fetch(String address){
		String page = address.substring(0,2);
		String hexOffset = address.substring(2,4);
		String data="";

		//first check if address in TLB
		if(tlb.isPresent(address)){	//IS IN TLB | HIT
			int pageFrame = tlb.getPageFrameNumber(0);
			data = PhysicalMemory.getDatafromPageFrame(pageFrame, hexOffset);	//Get Data from Physical Memory
			OS.result.append("0,0,1,0,"+data);
		}
		else{	// NOT IN TLB 
			if(VPageTable.isPresent(page)){ 	//IS IN VPageTable | SOFT MISS
				//get page frame from the table
				int pageFrame = VPageTable.getPageFrameNumber(0);
				data = PhysicalMemory.getDatafromPageFrame(pageFrame, hexOffset);
				OS.result.append("1,0,0,0,"+data);
			}
			else{	//NOT IN VPageTABLE | HARD MISS
				data = PhysicalMemory.addPage(address); //get page from disk (Page file)
				int pageFrame = PhysicalMemory.getLastPageFrameNumber();
				VPageTable.addPageEntry(page, pageFrame,0);	//update VPageTable
				OS.result.append("0,1,0,0,"+data);
			}
			OS.ClockReplacement(tlb, address, PhysicalMemory.getLastPageFrameNumber(),0);
		}
	}

	public void write(String address, String data){
		String page = address.substring(0,2);
		String hexOffset = address.substring(2,4);

		//first check if address in TLB
		if(tlb.isPresent(address)){	//IS IN TLB | HIT
			int pageFrame = tlb.getPageFrameNumber(1);
			PhysicalMemory.storeDatafromPageFrame(pageFrame, hexOffset, data);	//store Data to Physical Memory
			OS.result.append("0,0,1,0,"+data);
		}
		else{	// NOT IN TLB 
			if(VPageTable.isPresent(page)){ 	//IS IN VPageTable | SOFT MISS
				int pageFrame = VPageTable.getPageFrameNumber(1);
				PhysicalMemory.storeDatafromPageFrame(pageFrame, hexOffset, data);	//store data to Physical Mem
				OS.result.append("1,0,0,1,"+data);

			}
			else{	//NOT IN VPageTABLE | HARD MISS
				PhysicalMemory.addPage(address); //get page from disk (Page file)
				int pageFrame = PhysicalMemory.getLastPageFrameNumber();
				PhysicalMemory.storeDatafromPageFrame(pageFrame, hexOffset, data); //store data to Physical MEM
				VPageTable.addPageEntry(page, pageFrame,1);	//update VPageTable
				OS.result.append("0,1,0,1,"+data);
			}
			OS.ClockReplacement(tlb, address, PhysicalMemory.getLastPageFrameNumber(), 1);
		}
	}
}

class TLB{
	TLBEntry[] entries;
	int nextAvailable;
	int foundEntry;

	public TLB(){
		this.nextAvailable=0;
		this.foundEntry=0;
		//Setting up TLB with empty entries
		this.entries = new TLBEntry[8];
		for(int i=0;i<8;i++)
			this.entries[i] = new TLBEntry();
	}

	public boolean isPresent(String address){
		String page = address.substring(0,2);
		for(int i=0;i<8;i++){
			if(this.entries[i].VPageNumber.equals(page)){
				this.foundEntry=i;
				return true;
			}
		}
		return false;
	}

	public int getPageFrameNumber(int write){
		if(write==1) this.entries[foundEntry].D=1;
		this.entries[foundEntry].R=1;
		return this.entries[foundEntry].PageFrameNumber;
	}
}

class TLBEntry{	//TLB Entry object for supporting TLB array
	String VPageNumber;
	int V,R,D;
	int PageFrameNumber;

	public TLBEntry(){
		this.VPageNumber="";
		this.V=0;
		this.R=0;
		this.D=0;
		this.PageFrameNumber=-1;
	}
}