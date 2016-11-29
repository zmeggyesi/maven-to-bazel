package hu.skawa.migrator;

import java.io.File;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import hu.skawa.migrator.handler.POMHandler;

public class Migrator {
	public static void main(String[] args) {
		try {
			File inputFile = new File("pom.xml");
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser saxParser = factory.newSAXParser();
			POMHandler pomHandler = new POMHandler();
			saxParser.parse(inputFile, pomHandler);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
