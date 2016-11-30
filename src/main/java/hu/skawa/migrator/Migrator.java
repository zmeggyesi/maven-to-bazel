package hu.skawa.migrator;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import hu.skawa.migrator.handler.POMHandler;
import hu.skawa.migrator.model.Dependency;

public class Migrator {
	public static void main(String[] args) throws IOException {
		String pomLocation = System.getProperty("pom");
		File pom = new File(pomLocation);
		
		List<Dependency> dependencies = new ArrayList<Dependency>();
		try {
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser saxParser = factory.newSAXParser();
			POMHandler pomHandler = new POMHandler(dependencies);
			saxParser.parse(pom, pomHandler);
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
