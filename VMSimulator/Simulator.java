//Author: Brandon Palomino
//Date: 11/12/17
//Simulator Class which runs Virtual Memory Simulation using various components
public class Simulator{
	public static void main(String[] args) {
		//Simulator Objects
		CPU cpu = new CPU();
	
		if (args.length == 1){ //file inputed
			cpu.readAddresses(args[0]);
		}
		else{ //Exit invalid input
			System.out.println("Error, please supply input file!");
			System.exit(1);
		}
	}
}
