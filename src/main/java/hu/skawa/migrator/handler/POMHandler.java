package hu.skawa.migrator.handler;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import hu.skawa.migrator.model.Dependency;

public class POMHandler extends DefaultHandler {
	
	@SuppressWarnings("unused")
	private boolean inDependencies = false;
	private boolean inSingleDependency = false;
	private String element;
	private Dependency dep;

	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes)
			throws SAXException {
		if ("dependencies".equalsIgnoreCase(qName)) {
			inDependencies = true;
			System.out.println("REACHED START OF DEPENDENCIES BLOCK");
		}
		if ("dependency".equalsIgnoreCase(qName)) {
			inSingleDependency = true;
			dep = new Dependency();
		}
		element = qName;
	}
	
	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		if ("dependencies".equalsIgnoreCase(qName)) {
			inDependencies = false;
			System.out.println("REACHED END OF DEPENDENCIES BLOCK");
		}
		if ("dependency".equalsIgnoreCase(qName)) {
			inSingleDependency = false;
//			System.out.println(dep.toString());
			System.out.println(dep.toBazelDirective());
		}
	}
	
	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		if (inSingleDependency) {
			String content = new String(ch, start, length).trim();
			if (content.length() == 0) {
				return;
			}
			switch (element) {
				case "groupId": 
					dep.setGroupId(content);
					break;
				case "artifactId": 
					dep.setArtifactId(content);
					break;
				case "version": 
					dep.setVersion(content);
					break;
			}
		}
	}
	
}
