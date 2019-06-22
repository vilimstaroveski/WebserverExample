package hr.fer.zemris.java.custom.scripting.parser;

import hr.fer.zemris.java.custom.collections.ObjectStack;
import hr.fer.zemris.java.custom.scripting.nodes.DocumentNode;
import hr.fer.zemris.java.custom.scripting.nodes.EchoNode;
import hr.fer.zemris.java.custom.scripting.nodes.EndNode;
import hr.fer.zemris.java.custom.scripting.nodes.ForLoopNode;
import hr.fer.zemris.java.custom.scripting.nodes.Node;
import hr.fer.zemris.java.custom.scripting.nodes.TextNode;
import hr.fer.zemris.java.custom.scripting.tokens.Token;
import hr.fer.zemris.java.custom.scripting.tokens.TokenVariable;

import java.util.List;

/**
 * Parser that parses smart scripts. It creates a tree structure
 * with {@link Node} elements representing parsed document.
 * @author Vilim Staroveški
 *
 */
public class SmartScriptParser {
	/**
	 * Root node of tree structure.
	 */
	private DocumentNode docBody;
	/**
	 * Stack used as utility for creating a {@link Node} tree.
	 */
	private ObjectStack stack;
	/**
	 * Creates a new {@link SmartScriptParser} and starts parsing originalDocBody
	 * @param originalDocBody text that will be parsed.
	 */
	public SmartScriptParser(String originalDocBody) {
		
		this.docBody = new DocumentNode();
		this.stack = new ObjectStack();
		stack.push(docBody);
		parsing(originalDocBody);
	}
	/**
	 * Parses a document given as parameter. If given document
	 * is invalid, it will throw a {@link SmartScriptParserException} and 
	 * document will not be parsed.
	 * @param originalDocBody document for parsing.
	 */
	private void parsing(String originalDocBody) {
		
		String copyOfDocBody = originalDocBody;
		
		int indexOfBracket = 0;
		int indexOfSecondBracket=0;
		
		String tag;
		
		while (copyOfDocBody.contains("{$")) {		
			
			indexOfBracket = copyOfDocBody.indexOf("{$");
																																							 
			if (Tools.isEscaped(indexOfBracket, copyOfDocBody) || Tools.isNotValidBeginOfTag(indexOfBracket, copyOfDocBody)){
				
				textNodeFactory(copyOfDocBody.substring(0, indexOfBracket+2));
				copyOfDocBody = copyOfDocBody.substring(indexOfBracket+2);
				continue;
			}
																																							 
			textNodeFactory(copyOfDocBody.substring(0, indexOfBracket));
																																					 
			tag = copyOfDocBody.substring(indexOfBracket);
			
			if (tag.startsWith("{$}")) {	
				
				copyOfDocBody = copyOfDocBody.substring(indexOfBracket+3);
				continue;																														
			}
			
			if (!tag.contains("$}")) {
				
				throw new SmartScriptParserException();
			}
			
			indexOfSecondBracket = tag.indexOf("$}");
			tagFactory(tag.substring(0, indexOfSecondBracket+2));
																																							 
			copyOfDocBody = copyOfDocBody.substring(indexOfSecondBracket+indexOfBracket+2);
		}
		
		if (!copyOfDocBody.isEmpty()) {
			
			textNodeFactory(copyOfDocBody);
		}
	}

	/**
	 * Method that creates tags. Tags are parts of script between
	 * characters "{$" and "$}"
	 * @param substring string representing a tag.
	 */
	private void tagFactory(String substring) {
		String unWrappedTag = Tools.cutOffSymbols(substring);
		if(unWrappedTag.length() < 2) {
			textNodeFactory(unWrappedTag);
			return;
		}
		if(unWrappedTag.startsWith("=")) {
			
			echoNodeFactory(unWrappedTag);
			return;
		}
		String first3SymbolsOfTag = unWrappedTag.substring(0, 3);
		if(first3SymbolsOfTag.equalsIgnoreCase("FOR")) {
			
			forLoopNodeFactory(unWrappedTag);
			return;
		}
		
		if(first3SymbolsOfTag.equalsIgnoreCase("END")) {
			
			endTagFactory(unWrappedTag);
			return;
		}
	}

	/**
	 * Creates a end node and stores it as child of peek element on stack.
	 * @param unWrappedTag string repsenting a tag, without "{$" and "$}" symbols.
	 */
	private void endTagFactory(String unWrappedTag) {

		unWrappedTag = Tools.removeTag(unWrappedTag, "END");
		EndNode end = new EndNode();
		Node stackPointer = (Node) stack.peek();
		stackPointer.addChildNode(end);
		stack.pop();
	}
	/**
	 * Creates a {@link ForLoopNode} and stores it as child of peek element on stack as
	 * well as pushing created node on stack.
	 * @param unWrappedTag string repsenting a tag, without "{$" and "$}" symbols.
	 */
	private void forLoopNodeFactory(String unWrappedTag) {

		unWrappedTag = Tools.removeTag(unWrappedTag, "FOR");
		List<String> arguments = Tools.extractArgs(unWrappedTag);
		List<Token> tokens = Tools.parseToTokens(arguments);
		
		ForLoopNode forLoop = new ForLoopNode((TokenVariable) tokens.get(0), 
												tokens.get(1), 
												tokens.get(2), 
												tokens.size() > 3 ? tokens.get(3) : null);
		Node stackPointer = (Node) stack.peek();
		stackPointer.addChildNode(forLoop);
		stack.push(forLoop);
	}
	/**
	 * Creates a {@link EchoNode} and stores it as child of peek element on stack.
	 * @param unWrappedTag string repsenting a tag, without "{$" and "$}" symbols.
	 */
	private void echoNodeFactory(String unWrappedTag) {

		unWrappedTag = Tools.removeTag(unWrappedTag, "=");
		List<String> arguments = Tools.extractArgs(unWrappedTag);
		List<Token> tokens = Tools.parseToTokens(arguments);
		
		Token[] array = new Token[tokens.size()];
		int i = 0;
		for(Token t : tokens) {
			array[i++] = t;
		}
		EchoNode echo = new EchoNode(array);
		Node stackPointer = (Node) stack.peek();
		stackPointer.addChildNode(echo);
	}

	/**
	 * Creates new {@link TextNode} and stores it as child of peek element on stack.
	 * @param substring text of new {@link TextNode}
	 */
	private void textNodeFactory(String substring) {
		if (substring.isEmpty()) {
			return;
		}
		TextNode textNode = new TextNode(substring);
		Node stackPointer = (Node) stack.peek();
		stackPointer.addChildNode(textNode);
	}

	/**
	 * Returns document node of parsed script.
	 * @return document node of parsed script.
	 */
	public DocumentNode getDocumentNode() {
		return this.docBody;
	}
}
