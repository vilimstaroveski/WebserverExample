package hr.fer.zemris.java.custom.scripting.demo;

import hr.fer.zemris.java.custom.scripting.nodes.DocumentNode;
import hr.fer.zemris.java.custom.scripting.nodes.EchoNode;
import hr.fer.zemris.java.custom.scripting.nodes.ForLoopNode;
import hr.fer.zemris.java.custom.scripting.nodes.INodeVisitor;
import hr.fer.zemris.java.custom.scripting.nodes.TextNode;
import hr.fer.zemris.java.custom.scripting.parser.SmartScriptParser;
import hr.fer.zemris.java.custom.scripting.tokens.Token;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
/**
 * Demonstration for {@link SmartScriptParser}. It writes tree structure of nodes on 
 * standard output.
 * @author Vilim Staroveški
 *
 */
public class TreeWriter {

	/**
	 * Method called at program start. 
	 * @param args command line arguments. Expects path to file 
	 * where smart script is located.
	 */
	public static void main(String[] args) {
		
		if(args.length != 1) {
			System.out.println("Expected one argument: path to file");
			System.exit(1);
		}
		String docBody = null;
		try {
			docBody = new String(
				Files.readAllBytes(Paths.get("smscrFiles/fibonacci.smscr")), StandardCharsets.UTF_8
			);
		} catch (IOException e) {
			System.err.println("Invalid path or problem reading file!");
			System.exit(-1);
		}
		
		SmartScriptParser p = new SmartScriptParser(docBody);
		WriterVisitor visitor = new WriterVisitor();
		p.getDocumentNode().accept(visitor);
	}
	
	/**
	 * Implementation of {@link INodeVisitor}. It visits nodes in node tree structure,
	 * and prints out their {@link String} representations.
	 * @author Vilim Staroveški
	 *
	 */
	public static class WriterVisitor implements INodeVisitor {

		@Override
		public void visitTextNode(TextNode node) {
			System.out.print(node.getText());
		}

		@Override
		public void visitForLoopNode(ForLoopNode node) {
			System.out.print("{$ FOR "+
					node.getVariable().asText()+" "+
					node.getStartExpression().asText()+" "+
					node.getEndExpression().asText()+ " "+
					(node.getStartExpression()!=null ? node.getStartExpression().asText() : "")+
					" $}");
			for(int i = 0; i < node.numberOfChildren(); i++) {
				node.getChild(i).accept(this);
			}
		}

		@Override
		public void visitEchoNode(EchoNode node) {
			Token[] tokens = node.getTokens();
			System.out.print("{$= ");
			for(Token t : tokens) {
				System.out.print(t.asText()+ " ");
			}
			System.out.print(" $}");
		}

		@Override
		public void visitDocumentNode(DocumentNode node) {
			
			for(int i = 0; i < node.numberOfChildren(); i++) {
				node.getChild(i).accept(this);
			}
		}
		
	}
}
