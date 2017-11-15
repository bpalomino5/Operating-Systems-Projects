import org.apache.commons.io.FileUtils;
import java.io.File;
import java.util.Scanner;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.lang.StringBuilder;

public class OS{
	CPU cpu;
	PhysicalMemory memory;
	VPageTable pagetable;
	static int clockPointer;
	static String[] output;
	static StringBuilder result;
	static int next;

	public OS(String file){
		//copy page files to pages directory for simulator usage
		File source = new File("page_files/");
		File target = new File("pages/");
		try{
			FileUtils.copyDirectory(source, target);
		}
		catch(IOException e){
			e.printStackTrace();
		}

		//objects
		cpu = new CPU();
		memory = new PhysicalMemory();
		pagetable = new VPageTable();

		//helper parts
		clockPointer=0;
		output = new String[1000];
		output[0] = "Address,Read/Write,Soft Miss,Hard Miss,Hit,Dirty,Value";
		next=1;
		result = new StringBuilder();

		//start reading from file
		readfromFile(file);
	}

	public void readfromFile(String datafile){
		System.out.println("Reading file: " + datafile);
		File file = new File(datafile);
		try{
			Scanner input = new Scanner(file);
			while(input.hasNextLine()){		//handle file
				String condition = input.nextLine();
				String address = "", value = "";
				address = input.nextLine();
				result.append(address+","+condition+",");

				switch (condition) {
					case "0": 	// MMU Fetch
						cpu.handleProcess(0,address,value);
						break;
					case "1": 	// MMU Write
						value = input.nextLine();
						cpu.handleProcess(1,address,value);
						break;
				}
				addResultToOutput();
			}
			input.close();
		}
		catch(FileNotFoundException e){
			e.printStackTrace();
		}
	}

	public static void ClockReplacement(TLB tlb, String address, int pageFrameNumber, int write){
		String page = address.substring(0,2);
		while(true){
			if(tlb.entries[clockPointer].R == 0){
				if(write==1 && tlb.entries[clockPointer].V==1){ //write page back to disk
					writePageBacktoDisk(tlb.entries[clockPointer].VPageNumber, tlb.entries[clockPointer].PageFrameNumber);

					//resetting dirty bit
					tlb.entries[clockPointer].D=0;
					Integer index = Integer.parseInt(tlb.entries[clockPointer].VPageNumber, 16);
					VPageTable.entries[index].D=0;
				}
				//replace current page
				tlb.entries[clockPointer].VPageNumber=page;
				tlb.entries[clockPointer].V=1;
				tlb.entries[clockPointer].PageFrameNumber=pageFrameNumber;
				break;
			}
			else{
				tlb.entries[clockPointer].R = 0;
			}
			//move clockPointer
			if(clockPointer==7) clockPointer=0;
			else clockPointer++;
		}
	}

	public static void writePageBacktoDisk(String VPageNumber,int pageFrame){
		try{
			FileWriter writer = new FileWriter("pages/"+VPageNumber+".pg", false);
			BufferedWriter bw = new BufferedWriter(writer);

			for(int i=0;i<PhysicalMemory.blocks[pageFrame].length;i++){
				bw.write(PhysicalMemory.blocks[pageFrame][i]);
				bw.newLine();
				bw.flush();
			}
			bw.close();
		}
		catch(IOException e){
			e.printStackTrace();
		}
	}

	public void addResultToOutput(){
		output[next]= result.toString();
		next++;
		result.setLength(0);
	}
}