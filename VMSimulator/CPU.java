import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

public class CPU{
	public TLB tlb;

	public CPU(){
		this.tlb = new TLB();
		for(TLBEntry t : tlb.entries){
			System.out.println("hello");
		}
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
						MMU.fetch(address);
						break;
					case "1": 	// MMU Write
						address = input.nextLine();
						value = input.nextLine();
						MMU.write(address, value);
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
	public static void fetch(String address){
		//getValue(address);

	}
	public static void write(String address, String value){
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
		this.entries = new TLBEntry[8];
	}
}

class TLBEntry{
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