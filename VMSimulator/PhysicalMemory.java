import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

public class PhysicalMemory{
	static String[][] blocks;
	static int nextAvailable;
	
	public PhysicalMemory(){
		this.blocks = new String[16][];
		this.nextAvailable = 0;
	}
	public static String getDatafromPageFrame(int pageFrame, String hexOffset){
		Integer offset = Integer.parseInt(hexOffset, 16);
		//System.out.println("Getting data from memory");
		return blocks[pageFrame][offset];
	}

	public static String addPage(String address){
		//System.out.println("Storing page to memory");
		//System.out.println("Getting data from DISK");
		return getDataFromDisk(address);
	}

	public static int getLastPageFrameNumber(){
		return nextAvailable-1;
	}

	public static String getDataFromDisk(String address){
		blocks[nextAvailable] = new String[256];
		String page = address.substring(0,2);
		String hex = address.substring(2,4);
		Integer offset = Integer.parseInt(hex, 16);

		File file = new File("pages/"+page+".pg");
		try{	//access page and traverse until offset is reached
			Scanner input = new Scanner(file);
			int i=1;
			String value;
			while(i < offset && input.hasNextLine()){
				value = input.nextLine();
				blocks[nextAvailable][i-1] = value;
				i++;
			}
			value = input.nextLine(); 
			blocks[nextAvailable][i]=value;
			nextAvailable++;
			return value;
		}catch(FileNotFoundException e){
			e.printStackTrace();
		}
		return "Error page not found!";
	}
} 