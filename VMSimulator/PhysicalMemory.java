import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

public class PhysicalMemory{
	static String[][] blocks;
	static int pointer;
	
	public PhysicalMemory(){
		this.blocks = new String[16][];
		this.pointer = 0;
	}
	public static String getDatafromPageFrame(int pageFrame, String hexOffset){
		Integer offset = Integer.parseInt(hexOffset, 16);
		if(offset>0) offset-=1;
		return blocks[pageFrame][offset];
	}

	public static void storeDatafromPageFrame(int pageFrame, String hexOffset, String data){
		Integer offset = Integer.parseInt(hexOffset, 16);
		if(offset>0) offset-=1;
		blocks[pageFrame][offset]=data;
	}

	public static String addPage(String address){
		//Storing page to memory
		//Getting data from DISK
		return getDataFromDisk(address);
	}

	public static int getLastPageFrameNumber(){
		if(pointer==0) return 15;
		else return pointer-1;
	}

	public static String getDataFromDisk(String address){
		blocks[pointer] = new String[256]; //From 0x00-0xFF
		String page = address.substring(0,2);
		String hex = address.substring(2,4);
		Integer offset = Integer.parseInt(hex, 16);
		String data;

		//store page from disk to memory array
		storeDiskFileToMemory(page);
		data = blocks[pointer][offset-1];	//offset - 1 because array is 0 based

		//handle pointer
		if(pointer==15) pointer=0;
		else pointer++;

		return data;
	}

	public static void storeDiskFileToMemory(String page){
		File file = new File("pages/"+page+".pg");
		try{
			Scanner input = new Scanner(file);
			int i=0;
			String value;
			while(input.hasNextLine()){
				value = input.nextLine();
				blocks[pointer][i] = value;
				i++;
			}
			input.close();
		}catch(FileNotFoundException e){
			e.printStackTrace();
		}
	}
} 