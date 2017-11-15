import java.io.File;
import java.io.IOException;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.Scanner;

//Author: Brandon Palomino
//Date: 11/12/17
//Simulator Class which runs Virtual Memory Simulation using various components

public class Simulator{
	public static void main(String[] args) {
		if (args.length == 1){ //file inputed
			OS os = new OS(args[0]);
			writeOutputToFile(args[0]);
			System.out.println("DONE...");
		}
		else{ //Exit invalid input
			System.out.println("Error, please supply input file!");
			System.exit(1);
		}
	}

	public static void writeOutputToFile(String file){
		String value = file.replaceAll("\\D+","");
		System.out.println("Writing to output file: output"+value+".csv");
		
		try{
			FileWriter writer = new FileWriter("output_files/output"+value+".csv", false);
			BufferedWriter bw = new BufferedWriter(writer);

			for(int i=0;i<OS.next;i++){
				bw.write(OS.output[i]);
				bw.newLine();
				bw.flush();
			}
			bw.close();
		}
		catch(IOException e){
			e.printStackTrace();
		}
	}
}
