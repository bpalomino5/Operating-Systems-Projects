import org.apache.commons.io.FileUtils;
import java.io.File;
import java.io.IOException;

public class OS{
	CPU cpu;

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

		//have cpu read test file
		cpu = new CPU();
		cpu.readAddresses(file);
	}
}