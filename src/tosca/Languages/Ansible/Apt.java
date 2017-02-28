package tosca.Languages.Ansible;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.bind.JAXBException;

import tosca.Control_references;
import tosca.Utils;
import tosca.Abstract.PacketManager;

public class Apt extends PacketManager {

	// package manager name
	static public final String Name = "apt";

	@Override
	public void proceed(String filename, Control_references cr)
			throws FileNotFoundException, IOException, JAXBException {
		String prefix = "    - ";
		for (int i = 0; i < Utils.getPathLength(filename) - 1; i++)
			prefix = prefix + "../";
		if (cr == null)
			throw new NullPointerException();
		System.out.println(Name + " proceed " + filename);
		BufferedReader br = new BufferedReader(new FileReader(filename));
		boolean isChanged = false;
		String line = null;
		String newFile = "";
		int State = 0;
		while ((line = br.readLine()) != null) {
			switch(State){
			case 0:
				//Search for task name = start of tast
				if(line.matches("-\\s*name.*"))
					State = 1;
				newFile += line + '\n';
				break;
			case 1:
				//task name found, 
				//search for different possibilities of apt 
				if(line.matches("\\s*apt:.*pkg\\s*=\\s*\\{.*item.*\\}.*")){
					//apt = { item }
					State = 2;
					newFile += "  apt:\n    deb={{ item }}" + '\n'; 
				}
				else if(line.matches("\\s*apt:.* pkg=.*")){
					Pattern p = Pattern.compile("(apt:.*pkg=)\\s*(\\w*)\\s*.*");
					Matcher m = p.matcher(line);
					if (m.find()) {
						System.out.println("Found packet: " + m.group(2));
						newFile += "  apt: pkg={{ item }}" + '\n' + "  with_items:" + '\n';
						for(String packet: cr.getPacket(m.group(2)).split("\\s+"))
							newFile += prefix + packet + '\n';
						State = 0;
					}
					break;
				} else if(line.matches("\\s*apt:.*")){
					newFile += line + '\n';   //TODO   !!!!!!!!
				} else
					newFile += line + '\n';   
				break;
			case 2:
				if(line.matches("\\s*with_items:\\s*")){
					State = 3;
					newFile += line + '\n';
				}
				break;
			case 3:
				//TODO
				if(line.matches("-\\s*name.*")){
					//next task
					State = 1;
					newFile += line + '\n'; 
				} 
				else {
					String[] words = line.split("\\s+");
					int i = 0;
					if(words.length > 0){
						if(words[0].equals(""))
							i = 1;
						if(words.length == 2 + i && words[i].equals("-")){
							for(String packet: cr.getPacket(words[i+1]).split("\\s+" ))
								newFile += prefix + packet + '\n'; //TOCHECK 
							isChanged = true;	
						} else 
							newFile += line + '\n'; 
					} else
						newFile += line + '\n'; 
				}
				break;
			}

		}
		br.close();
		if (isChanged) {
			// references found, need to replace file
			// delete old
			File file = new File(filename);
			file.delete();

			// create new file
			FileWriter wScript = new FileWriter(file);
			wScript.write(newFile, 0, newFile.length());
			wScript.close();
		}
	}
}
