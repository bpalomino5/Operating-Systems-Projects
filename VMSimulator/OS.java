public class OS{
	CPU cpu;

	public OS(String file){
		cpu = new CPU();
		cpu.readAddresses(file);
	}
}