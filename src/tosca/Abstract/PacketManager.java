package tosca.Abstract;

import java.io.FileNotFoundException;
import java.io.IOException;

import javax.xml.bind.JAXBException;

import tosca.Control_references;

/**
 * Package manager used by language
 * 
 * @author jery
 *
 */
public abstract class PacketManager {

	// Name of manager
	static public String Name;

	/**
	 * Proceed given file
	 * 
	 * @param filename
	 *            file to proceed
	 * @param cr
	 *            CSAR manager
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws JAXBException
	 */
	protected abstract void proceed(String filename, Control_references cr)
			throws FileNotFoundException, IOException, JAXBException;
}
