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
		if(tlb.isPresent(address)){
			//get from physical
		}
		getValue(address);

	}
	public void write(String address, String value){
		//System.out.println(address);
		//System.out.println(value);

	}

	public static void getValue(String address){
		String page = address.substring(0,2);
		String hex = address.substring(2,4);
		Integer offset = Integer.parseInt(hex, 16);

		File file = new File("page_files/"+page+".pg");
		try{	//access page and traverse until offset is reached
			Scanner input = new Scanner(file);
			int i=1;
			while(i < offset && input.hasNextLine()){
				input.nextLine();
				i++;
			}
			System.out.println(input.nextLine());	//get offset
		}catch(FileNotFoundException e){
			e.printStackTrace();
		}
	}
}

class TLB{
	TLBEntry[] entries;

	public TLB(){
		//Setting up TLB with empty entries
		this.entries = new TLBEntry[8];
		for(int i=0;i<8;i++)
			this.entries[i] = new TLBEntry();
	}

	public boolean isPresent(String address){
		String page = address.substring(0,2);
		for(int i=0;i<8;i++){
			if(this.entries[i].VPageNumber.equals(page)){
				return true;
			}
		}
		return false;
	}
}

class TLBEntry{	//TLB Entry object for supporting TLB array
	String VPageNumber;
	int V,R,D;
	String PageFrameNumber;

	public TLBEntry(){
		this.VPageNumber="";
		this.V=0;
		this.R=0;
		this.D=0;
		this.PageFrameNumber="";
	}
}