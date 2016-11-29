package hu.skawa.migrator.handler;

import java.util.ArrayList;
import java.util.List;

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
	List<Dependency> resolvedDependencies = new ArrayList<Dependency>();

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
//			System.out.println(dep.toBazelDirective());
			resolvedDependencies.add(dep);
		}
	}
	
	@Override
	public void endDocument() throws SAXException {
		System.out.println("ALL DEPENDENICES FOUND");
		for (Dependency dep : resolvedDependencies) {
			System.out.println(dep.toBazelDirective());
		}
		System.out.println("REFERENCES FOR BUILDFILES");
		for (Dependency dep : resolvedDependencies) {
			System.out.println(dep.getArtifactId() + ": @" + dep.getBazelName() + "//jar");
		}
	};
	
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
