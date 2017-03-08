package tosca.xml_definitions;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import tosca.Control_references;

public class RR_DependsOn {

	/**
	 * @author Yaroslav package dependencies relation XML description
	 */
	@XmlRootElement(name = "tosca:Definitions")
	@XmlAccessorType(XmlAccessType.PUBLIC_MEMBER)
	public static class Definitions {

		@XmlElement(name = "RelationshipType", required = true)
		public RelationshipType relationshipType;

		@XmlAttribute(name = "xmlns:tosca", required = true)
		public static final String tosca="http://docs.oasis-open.org/tosca/ns/2011/12";
		@XmlAttribute(name = "xmlns:winery", required = true)
		public static final String winery="http://www.opentosca.org/winery/extensions/tosca/2013/02/12";
		@XmlAttribute(name = "xmlns:ns1", required = true)
		public static final String ns1="http://www.eclipse.org/winery/model/selfservice";
		@XmlAttribute(name = "id", required = true)
		public static final String id="winery-defs-RR_DependsOn_Realation";
		@XmlAttribute(name = "targetNamespace", required = true)
		public static final String targetNamespace="http://docs.oasis-open.org/tosca/ns/2011/12/ToscaBaseTypes"; //TODO
		
		public Definitions() {
			relationshipType = new RelationshipType();
		}

		public static class RelationshipType {
			@XmlAttribute(name = "name", required = true)
			public static final String name = "RR_DependsOn";
			@XmlAttribute(name = "targetNamespace", required = true)
			public static final String targetNamespace="http://docs.oasis-open.org/tosca/ns/2011/12/ToscaBaseTypes"; //TODO

			RelationshipType() {
			}
		}
	}

	// output filename
	public static final String filename = "RR_DependsOn.tosca";

	/**
	 * Create PackageType xml description
	 * 
	 * @param cr
	 * @throws JAXBException
	 * @throws IOException
	 */
	public static void init(Control_references cr) throws JAXBException,
			IOException {
		File dir = new File(cr.getFolder() + Control_references.Definitions);
		dir.mkdirs();
		File temp = new File(cr.getFolder() + Control_references.Definitions + filename);
		if (temp.exists())
			temp.delete();
		temp.createNewFile();
		OutputStream output = new FileOutputStream(cr.getFolder()
				+ Control_references.Definitions + filename);

		JAXBContext jc = JAXBContext.newInstance(Definitions.class);

		Definitions shema = new Definitions();

		Marshaller marshaller = jc.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		marshaller.marshal(shema, output);
		cr.metaFile.addFileToMeta(Control_references.Definitions + filename, "application/vnd.oasis.tosca.definitions");
	}
}
