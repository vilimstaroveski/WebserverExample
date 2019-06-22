package hr.fer.zemris.java.custom.scripting.exec;

import hr.fer.zemris.java.custom.scripting.exec.functions.AddFunction;
import hr.fer.zemris.java.custom.scripting.exec.functions.DecFmtFunction;
import hr.fer.zemris.java.custom.scripting.exec.functions.DivFunction;
import hr.fer.zemris.java.custom.scripting.exec.functions.DupFunction;
import hr.fer.zemris.java.custom.scripting.exec.functions.IFunction;
import hr.fer.zemris.java.custom.scripting.exec.functions.MulFunction;
import hr.fer.zemris.java.custom.scripting.exec.functions.ParamGetFunction;
import hr.fer.zemris.java.custom.scripting.exec.functions.PparamDelFunction;
import hr.fer.zemris.java.custom.scripting.exec.functions.PparamGetFunction;
import hr.fer.zemris.java.custom.scripting.exec.functions.PparamSetFunction;
import hr.fer.zemris.java.custom.scripting.exec.functions.SetMimeTypeFunction;
import hr.fer.zemris.java.custom.scripting.exec.functions.SinFunction;
import hr.fer.zemris.java.custom.scripting.exec.functions.SubFunction;
import hr.fer.zemris.java.custom.scripting.exec.functions.SwapFunction;
import hr.fer.zemris.java.custom.scripting.exec.functions.TparamDelFunction;
import hr.fer.zemris.java.custom.scripting.exec.functions.TparamGetFunction;
import hr.fer.zemris.java.custom.scripting.exec.functions.TparamSetFunction;
import hr.fer.zemris.java.custom.scripting.nodes.DocumentNode;
import hr.fer.zemris.java.custom.scripting.nodes.EchoNode;
import hr.fer.zemris.java.custom.scripting.nodes.ForLoopNode;
import hr.fer.zemris.java.custom.scripting.nodes.INodeVisitor;
import hr.fer.zemris.java.custom.scripting.nodes.TextNode;
import hr.fer.zemris.java.custom.scripting.parser.SmartScriptParser;
import hr.fer.zemris.java.custom.scripting.tokens.Token;
import hr.fer.zemris.java.custom.scripting.tokens.TokenConstantDouble;
import hr.fer.zemris.java.custom.scripting.tokens.TokenConstantInteger;
import hr.fer.zemris.java.custom.scripting.tokens.TokenFunction;
import hr.fer.zemris.java.custom.scripting.tokens.TokenOperator;
import hr.fer.zemris.java.custom.scripting.tokens.TokenString;
import hr.fer.zemris.java.custom.scripting.tokens.TokenVariable;
import hr.fer.zemris.java.webserver.RequestContext;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

/**
 * Class that runs a smart script. It uses {@link SmartScriptParser} to parse document 
 * and then runs the script.
 * @author Vilim Staroveški
 *
 */
public class SmartScriptEngine {
	/**
	 * Supported functions that scripts can have.
	 */
	private Map<String, IFunction> supportedFunctions;

	/**
	 * Root of the node tree structure. It contains all created nodes as its children.
	 */
	private DocumentNode documentNode;
	
	/**
	 * {@link RequestContext} 
	 * 
	 */
	private RequestContext requestContext;
	
	/**
	 * Multistack used in this engine.
	 */
	private ObjectMultistack multistack;
	
	/**
	 * {@link INodeVisitor} that applys executable operation on node.
	 */
	private INodeVisitor visitor = new INodeVisitor() {

		@Override
		public void visitTextNode(TextNode node) {
			try {
				requestContext.write(node.getText());
			} catch (IOException ignorable) {
				System.err.println(ignorable.getMessage());
			}
		}

		@Override
		public void visitForLoopNode(ForLoopNode node) {
			
			multistack.push(node.getVariable().asText(), new ValueWrapper(
					node.getStartExpression().asText()
					));
			
			Object end = new ValueWrapper(node.getEndExpression().asText()).getValue();
			Object step = new ValueWrapper(node.getStepExpression().asText()).getValue();
			while(multistack.peek(node.getVariable().asText()).numCompare(end) < 1) {
				for(int i = 0; i < node.numberOfChildren(); i++) {
					node.getChild(i).accept(this);
				}
				multistack.peek(node.getVariable().asText()).increment(step != null ? step : 1);
			}
			multistack.pop(node.getVariable().asText());
		}

		@Override
		public void visitEchoNode(EchoNode node) {
			Stack<Object> tempStack = new Stack<Object>();
			for(Token token : node.getTokens()) {
				if(token instanceof TokenConstantInteger) {
					tempStack.push(((TokenConstantInteger) token).getValue());
				}
				if(token instanceof TokenConstantDouble) {
					tempStack.push(((TokenConstantDouble) token).getValue());
				}
				if(token instanceof TokenString) {
					tempStack.push(((TokenString) token).getValue());
				}
				if(token instanceof TokenVariable) {
					ValueWrapper wrapper = multistack.peek(((TokenVariable) token).getName());
					tempStack.push(wrapper.getValue());
				}
				if(token instanceof TokenOperator) {
					supportedFunctions.get(((TokenOperator) token).getSymbol()).apply(tempStack, requestContext);
				}
				if(token instanceof TokenFunction) {
					supportedFunctions.get(((TokenFunction) token).getName()).apply(tempStack, requestContext);
				}
			}
			List<Object> leftOvers = new ArrayList<Object>();
			while(tempStack.size() > 0) {
				leftOvers.add(tempStack.pop());
			}
			Collections.reverse(leftOvers);
			for(Object o : leftOvers) {
				try {
					requestContext.write(o.toString());
				} catch (IOException ignorable) {
				}
			}
		}

		@Override
		public void visitDocumentNode(DocumentNode node) {
			
			for(int i = 0; i < node.numberOfChildren(); i++) {
				node.getChild(i).accept(this);
			}
		}
	};

	/**
	 * Creates new {@link SmartScriptEngine} with defined {@link DocumentNode} and
	 * {@link RequestContext}
	 * @param documentNode root of node tree structure.
	 * @param requestContext {@link RequestContext} for this engine.
	 */
	public SmartScriptEngine(DocumentNode documentNode,
			RequestContext requestContext) {
		
		this.multistack = new ObjectMultistack();
		
		this.documentNode = documentNode;
		
		this.requestContext = requestContext;
		
		supportedFunctions = new HashMap<String, IFunction>();
		
		supportedFunctions.put("+", new AddFunction());
		supportedFunctions.put("-", new SubFunction());
		supportedFunctions.put("*", new MulFunction());
		supportedFunctions.put("/", new DivFunction());
		
		supportedFunctions.put("sin", new SinFunction());
		supportedFunctions.put("decfmt", new DecFmtFunction());
		supportedFunctions.put("dup", new DupFunction());
		supportedFunctions.put("swap", new SwapFunction());
		supportedFunctions.put("setMimeType", new SetMimeTypeFunction());
		supportedFunctions.put("paramGet", new ParamGetFunction());
		supportedFunctions.put("pparamGet", new PparamGetFunction());
		supportedFunctions.put("pparamSet", new PparamSetFunction());
		supportedFunctions.put("pparamDel", new PparamDelFunction());
		supportedFunctions.put("tparamGet", new TparamGetFunction());
		supportedFunctions.put("tparamSet", new TparamSetFunction());
		supportedFunctions.put("tparamDel", new TparamDelFunction());
		
		
	}

	/**
	 * Executes the node tree.
	 */
	public void execute() {
		documentNode.accept(visitor);
	}

}
