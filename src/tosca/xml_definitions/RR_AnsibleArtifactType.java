package tosca.xml_definitions;

/*-
 * #%L
 * TOSCA_RR
 * %%
 * Copyright (C) 2017 Stuttgart Uni, IAAS
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
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

import tosca.CSAR_handler;
import tosca.Resolver;

public class RR_AnsibleArtifactType {

	/**
	 * @author Yaroslav Script Artifact Type XML description
	 */
	@XmlRootElement(name = "tosca:Definitions")
	@XmlAccessorType(XmlAccessType.PUBLIC_MEMBER)
	public static class Definitions {

		@XmlElement(name = "tosca:Import", required = true)
		public static final RR_Import import_impl = new RR_Import("http://opentosca.org/artifacttypes/propertiesdefinition/winery",
				Resolver.folder + "ansible_properties.xsd", "http://www.w3.org/2001/XMLSchema");
		
		@XmlElement(name = "tosca:ArtifactType", required = true)
		public ArtifactType artifactType;

		@XmlAttribute(name = "xmlns:tosca", required = true)
		public static final String tosca="http://docs.oasis-open.org/tosca/ns/2011/12";
		@XmlAttribute(name = "xmlns:winery", required = true)
		public static final String winery="http://www.opentosca.org/winery/extensions/tosca/2013/02/12";
		@XmlAttribute(name = "xmlns:ns0", required = true)
		public static final String ns0="http://www.eclipse.org/winery/model/selfservice";
		@XmlAttribute(name = "id", required = true)
		public static final String id="winery-defs-for_tbt-RR_AnsibleArtifact";
		@XmlAttribute(name = "targetNamespace", required = true)
		public static final String targetNamespace="http://opentosca.org/artifacttypes"; 
		
		public Definitions() {
			artifactType = new ArtifactType();
		}

		public static class ArtifactType {
			@XmlAttribute(name = "name", required = true)
			public static final String name = "RR_AnsibleArtifact";
			@XmlAttribute(name = "targetNamespace", required = true)
			public static final String targetNamespace="http://opentosca.org/artifacttypes"; 

			@XmlElement(name = "winery:PropertiesDefinition", required = true)
			public PropertiesDefinition propertiesDefinition; 
			
			@XmlElement(name = "tosca:PropertiesDefinition", required = true)
			public PropertiesDefinition2 propertiesDefinition2; 
			
			ArtifactType() {
				propertiesDefinition = new PropertiesDefinition();
				propertiesDefinition2 = new PropertiesDefinition2();
			}
			
			public static class PropertiesDefinition {
				@XmlAttribute(name = "elementname", required = true)
				public static final String elementname = "Properties";
				@XmlAttribute(name = "namespace", required = true)
				public static final String namespace="http://opentosca.org/artifacttypes/propertiesdefinition/winery"; 

				@XmlElement(name = "winery:properties", required = true)
				public Properties properties;
				@XmlElement(name = "winery:properties", required = true)
				public Properties properties2;
				
				public PropertiesDefinition(){
					properties = new Properties("Playbook", "xsd:string");
					properties2 = new Properties("Variables", "xsd:string");
				}
				public static class Properties{
					@XmlElement(name = "winery:key", required = true)
					public String key;
					@XmlElement(name = "winery:type", required = true)
					public String type;
					public Properties(String key, String type){
						this.key = key;
						this.type = type;
					}
				}
			}
			public static class PropertiesDefinition2 {
				@XmlAttribute(name = "xmlns:ns21", required = true)
				public static final String ns21 = "http://opentosca.org/artifacttypes/propertiesdefinition/winery";
				@XmlAttribute(name = "type", required = true)
				public static final String type="ns21:Properties"; 
			}
		}
	}

	// output filename
	public static final String filename = "RR_AnsibleArtifact.tosca";

	/**
	 * Create ScriptType xml description
	 * 
	 * @param ch
	 * @throws JAXBException
	 * @throws IOException
	 */
	public static void init(CSAR_handler ch) throws JAXBException,
			IOException {
		File dir = new File(ch.getFolder() + CSAR_handler.Definitions);
		dir.mkdirs();
		File temp = new File(ch.getFolder() + CSAR_handler.Definitions + filename);
		if (temp.exists())
			temp.delete();
		temp.createNewFile();
		OutputStream output = new FileOutputStream(ch.getFolder()
				+ CSAR_handler.Definitions + filename);

		JAXBContext jc = JAXBContext.newInstance(Definitions.class);

		Definitions shema = new Definitions();

		FileWriter file_writer = new FileWriter(new File(ch.getFolder() + Resolver.folder + "ansible_properties.xsd"));
		file_writer.write(ansible_prop);
		file_writer.flush();
		file_writer.close();
		ch.metaFile.addFileToMeta(Resolver.folder + "ansible_properties.xsd", "text/xml");
		
		Marshaller marshaller = jc.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		marshaller.marshal(shema, output);
		ch.metaFile.addFileToMeta(CSAR_handler.Definitions + filename, "application/vnd.oasis.tosca.definitions");
	}
	
	private static final String ansible_prop = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><schema attributeFormDefault=\"unqualified\" elementFormDefault=\"qualified\" targetNamespace=\"http://opentosca.org/artifacttypes/propertiesdefinition/winery\" xmlns=\"http://www.w3.org/2001/XMLSchema\"><element name=\"Properties\"><complexType><sequence xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"><element name=\"Playbook\" type=\"xsd:string\"/><element name=\"Variables\" type=\"xsd:string\"/></sequence></complexType></element></schema>";
}
