import org.apache.commons.io.FileUtils;
import java.io.File;
import java.io.IOException;

public class OS{
	CPU cpu;
	PhysicalMemory memory;
	VPageTable pagetable;
	static int clockPointer;

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

		clockPointer=0;

		//have cpu read test file
		cpu = new CPU();
		memory = new PhysicalMemory();
		pagetable = new VPageTable();
		cpu.readAddresses(file);
	}

	public static void ClockReplacement(TLB tlb, String address, int pageFrameNumber){
		String page = address.substring(0,2);
		while(true){
			if(tlb.entries[clockPointer].R == 0){
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
}