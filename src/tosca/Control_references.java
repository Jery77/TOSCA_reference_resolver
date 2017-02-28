package tosca;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import javax.xml.bind.JAXBException;

//unpack 
public class Control_references {

	// input CSAR file name
	private String CSAR;

	// folder containing extracted files
	private String folder;

	// extracted files
	private List<String> files;

	// architecture of packages
	private String architecture;
	
	private Integer resolving;
	public final static Integer REPLACEMENT = 1; 
	public final static Integer ADDITION = 2; 

	// Metafile description
	public MetaFile metaFile;
	
	private Downloader downloader;

	public static final String ArchitectureFileName = "arch";
	public static final String ResolvingFileName = "resolv";
	/**
	 * Constructor
	 */
	public Control_references() {
		metaFile = new MetaFile();
		downloader = new Downloader();
	}
	
	public String getPacket(String packet) throws JAXBException {
		return downloader.getPacket(packet, this);
	}

	/**
	 * init system
	 * 
	 * @param filename
	 *            , CSAR archive
	 * @throws IOException
	 */
	public Control_references(String filename) throws FileNotFoundException,
			IOException {
		metaFile = new MetaFile();
		init(filename);
		downloader = new Downloader();
	}

	/**
	 * extract archive and read architecture
	 * 
	 * @param filename
	 *            CSAR archive
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public void init(String filename) throws FileNotFoundException, IOException {
		if (filename == null)
			throw new NullPointerException();
		CSAR = filename;
		unpack();
		readArchitecture();
		readResolving();
	}

	/**
	 * List extracted files
	 * 
	 * @return list with files
	 */
	public List<String> getFiles() {
		List<String> fullFiles = new LinkedList<String>();
		for (String s : files)
			fullFiles.add(folder + s);
		return fullFiles;
	}

	/**
	 * Get folder containing extracted files
	 * 
	 * @return folder name
	 */
	public String getFolder() {
		return folder;
	}

	/**
	 * Unpack CSAR
	 * 
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	private void unpack() throws FileNotFoundException, IOException {
		folder = CSAR + "_temp_references_resolver";
		File folderfile = new File(folder);
		folder = folderfile + File.separator;
		zip.delete(new File(folder));
		files = zip.unZipIt(CSAR, folder);
		metaFile.init(folder);
	}

	/**
	 * Pack changed CSAR back to zip
	 * 
	 * @param filename
	 *            target archive filename
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public void pack(String filename) throws FileNotFoundException, IOException {
		metaFile.pack(folder);
		if (filename == null)
			throw new NullPointerException();
		zip.zipIt(filename, folder);
	}

	/**
	 * Get archive filename
	 * 
	 * @return archive filename
	 */
	public String getCSARname() {
		return CSAR;
	}

	/**
	 * Get current architecture
	 * 
	 * @return architecture
	 */
	public String getArchitecture() {
		return architecture;
	}

	public Integer getResolving() {
		return resolving;
	}
	/**
	 * reads Architecture from extracted data or from user input
	 * 
	 * @throws IOException
	 */
	// no need to close user input
	@SuppressWarnings("resource")
	public void readArchitecture() throws IOException {
		File arch = new File(folder + Resolver.folder + ArchitectureFileName);
		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(arch));
			String line = br.readLine();
			br.close();
			if (line != null && !line.equals(""))
				architecture = line;
			else {
				new File(folder + Resolver.folder + ArchitectureFileName).delete();
				throw new FileNotFoundException();
			}

		} catch (FileNotFoundException e) {
			new File(folder + Resolver.folder).mkdir();
			FileWriter bw = new FileWriter(arch);
			System.out.println("Please enter architecure.");
			System.out.println("Example: i386(default), amd64, arm.");
			System.out.print("architecture:");
			architecture = new Scanner(System.in).nextLine();
			if (architecture.equals(""))
				architecture = "i386";
			bw.write(architecture);
			bw.close();
		}
		metaFile.addFileToMeta(Resolver.folder + ArchitectureFileName, "text/txt");
	}
	
	private boolean isResolving(String input)
	{
		if(Integer.getInteger(input) == REPLACEMENT || Integer.getInteger(input) == ADDITION)
			return true;
		return false;
	}
	private boolean isResolving(Integer input)
	{
		if(input == REPLACEMENT || input == ADDITION)
			return true;
		return false;
	}
	
	/**
	 * reads Architecture from extracted data or from user input
	 * 
	 * @throws IOException
	 */
	// no need to close user input
	@SuppressWarnings("resource")
	public void readResolving() throws IOException {
		File resolv = new File(folder + Resolver.folder + ResolvingFileName);
		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(resolv));
			String line = br.readLine();
			br.close();
			if (line != null && !line.equals("") && isResolving(line))
				resolving = Integer.getInteger(line);
			else {
				new File(folder + Resolver.folder + ResolvingFileName).delete();
				throw new FileNotFoundException();
			}

		} catch (FileNotFoundException e) {
			new File(folder + Resolver.folder).mkdir();
			FileWriter bw = new FileWriter(resolv);
			System.out.println("Please enter resolving method.");
			System.out.println("Example: \n"+REPLACEMENT+") Replacement(default)\n"+ADDITION+") Addition");
			System.out.print("resolving:");
			String temp = new Scanner(System.in).nextLine();
			if (isResolving(temp))
				resolving = Integer.getInteger(temp);
			else
				resolving = REPLACEMENT;
			bw.write(resolving.toString());
			bw.close();
		}
		metaFile.addFileToMeta(Resolver.folder + ResolvingFileName, "text/txt");
	}

	/**
	 * Set specific architecture
	 * 
	 * @param arch
	 * @throws IOException
	 */
	public void setArchitecture(String arch) throws IOException {
		if (arch == null)
			throw new NullPointerException();
		architecture = arch;

		// delete old file
		File fArch = new File(folder + Resolver.folder + ArchitectureFileName);
		fArch.delete();

		// create new file
		FileWriter bw = new FileWriter(fArch);
		bw.write(arch);
		bw.flush();
		bw.close();
	}
	/**
	 * Set specific Resolving method
	 * 
	 * @param resolving
	 * @throws IOException
	 */
	public void setResolving(Integer resolving) throws IOException {
		if (resolving == null)
			throw new NullPointerException();
		if(!isResolving(resolving)){
			System.out.println("wrong resolving");
			return;
		}

		// delete old file
		File fResolv = new File(folder + Resolver.folder + ResolvingFileName);
		fResolv.delete();

		// create new file
		FileWriter bw = new FileWriter(fResolv);
		bw.write(resolving.toString());
		bw.flush();
		bw.close();
	}
}
