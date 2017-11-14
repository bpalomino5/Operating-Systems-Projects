import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

public class CPU{
	public MMU mmu;

	public CPU(){
		mmu = new MMU();
	}

	public void readAddresses(String datafile){
		System.out.println(datafile);	//print file name for testing
		File file = new File(datafile);
		try{
			Scanner input = new Scanner(file);
			while(input.hasNextLine()){		//handle file
				String condition = input.nextLine();
				String address = "", value = "";

				switch (condition) {
					case "0": 	// MMU Fetch
						address = input.nextLine();
						mmu.fetch(address);
						break;
					case "1": 	// MMU Write
						address = input.nextLine();
						value = input.nextLine();
						mmu.write(address, value);
						break;
				}
			}
			input.close();
		}
		catch(FileNotFoundException e){
			e.printStackTrace();
		}
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
		if(tlb.isPresent(address)){	//IS IN TLB
			int pageFrame = tlb.getPageFrameNumber();
			data = PhysicalMemory.getDatafromPageFrame(pageFrame, hexOffset);	//Get Data from Physical Memory
			System.out.println(data);
		}
		else{	// NOT IN TLB
			if(VPageTable.isPresent(page)){ 	//IS IN VPageTable
				//get page frame from the table
				int pageFrame = VPageTable.getPageFrameNumber();
				data = PhysicalMemory.getDatafromPageFrame(pageFrame, hexOffset);
			}
			else{	//IS NOT IN VPageTABLE
				data = PhysicalMemory.addPage(address); //get page from disk (Page file)
				int PageFrameNumber = PhysicalMemory.getLastPageFrameNumber();
				VPageTable.addPageEntry(page, PageFrameNumber);	//update VPageTable
			}
			OS.ClockReplacement(tlb, address, PhysicalMemory.getLastPageFrameNumber());
			System.out.println(data);
		}
	}

	public void write(String address, String value){
		//System.out.println(address);
		//System.out.println(value);

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

	public int getPageFrameNumber(){
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