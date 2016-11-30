package hu.skawa.migrator;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import hu.skawa.migrator.model.Dependency;

public class Migrator {
	public static void main(String[] args) throws IOException {
		String pomLocation = System.getProperty("pom");
		File pom = new File(pomLocation);
		
		List<Dependency> dependencies = new ArrayList<Dependency>();
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(pom);
			XPathFactory xpf = XPathFactory.newInstance();
			XPath path = xpf.newXPath();
			XPathExpression exp = path.compile("/project/dependencies/dependency[*]");
			NodeList rawDependencies = (NodeList) exp.evaluate(doc, XPathConstants.NODESET);
			for (int index = 0; index < rawDependencies.getLength(); index++) {
				Node dependencyNode = rawDependencies.item(index);
				Dependency dep = new Dependency();
				NodeList dependencyData = dependencyNode.getChildNodes();
				for (int dataIndex = 0; dataIndex<dependencyData.getLength(); dataIndex++) {
					Node dependencyDataNode = dependencyData.item(dataIndex);
					String nodeName = dependencyDataNode.getNodeName();
					switch (nodeName) {
						case "groupId":
							dep.setGroupId(dependencyDataNode.getTextContent());
							continue;
						case "artifactId":
							dep.setArtifactId(dependencyDataNode.getTextContent());
							continue;
						case "version":
							dep.setVersion(dependencyDataNode.getTextContent());
							continue;
					}
				}
				dependencies.add(dep);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		String outputFilePrefix = System.getProperty("outputFilePrefix");
		if (outputFilePrefix != null) {
			Boolean outputDirectives = Boolean.parseBoolean(System.getProperty("outputDirectives"));
			Boolean outputReferences = Boolean.parseBoolean(System.getProperty("outputReferences"));
			if (outputDirectives) {
				File directives = new File(outputFilePrefix + "-directives");
				FileWriter writer = new FileWriter(directives);
				for (Dependency dep : dependencies) {
					writer.write(dep.toBazelDirective());
					writer.write("\n");
				}
				writer.close();
			}
			if (outputReferences) {
				File references = new File(outputFilePrefix + "-references");
				FileWriter writer = new FileWriter(references);
				for (Dependency dep : dependencies) {
					writer.write(dep.getArtifactId() + ": @" + dep.getBazelName() + "//jar");
					writer.write("\n");
				}
				writer.close();
			}
		}
		
	}
}
