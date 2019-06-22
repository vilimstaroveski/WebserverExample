package hr.fer.zemris.java.custom.scripting.parser;

import hr.fer.zemris.java.custom.scripting.tokens.Token;
import hr.fer.zemris.java.custom.scripting.tokens.TokenConstantDouble;
import hr.fer.zemris.java.custom.scripting.tokens.TokenConstantInteger;
import hr.fer.zemris.java.custom.scripting.tokens.TokenFunction;
import hr.fer.zemris.java.custom.scripting.tokens.TokenOperator;
import hr.fer.zemris.java.custom.scripting.tokens.TokenString;
import hr.fer.zemris.java.custom.scripting.tokens.TokenVariable;

import java.util.ArrayList;
import java.util.List;

public class Tools {

	/**
	 * Method that tells if tag is valid or not.
	 * @param indexOfBracket position of bracket "{" in given string.
	 * @param copyOfDocBody copy of text that is parsed
	 * @return true if tag is valid, false otherwise.
	 */
	public static boolean isNotValidBeginOfTag(int indexOfBracket, String copyOfDocBody) {
		
		if (copyOfDocBody.length() <= indexOfBracket) {
			return true;
		}
		
		if (copyOfDocBody.charAt(indexOfBracket+1) != '$') {
			return true;
		}
		
		return false;
	}
	
	/**
	 * Tells if bracket "{" is escaped. 
	 * @param indexOfBracket position of bracket in text
	 * @param copyOfDocBody copy of text that is parsed
	 * @return true if bracket is escaped, false otherwise
	 */
	public static boolean isEscaped(int indexOfBracket, String copyOfDocBody) {
		
		if (indexOfBracket == 0) return false;//ako nema niceg prije zagrade, nemoze biti escapena
		if (copyOfDocBody.charAt(indexOfBracket-1) == '\\') return true; 
		if (indexOfBracket > 1) {//ako ima vise od 1 znaka iza zagrade, moze se nalaziti escapean escape
			if (copyOfDocBody.charAt(indexOfBracket-1) == '\\' && copyOfDocBody.charAt(indexOfBracket-2) == '\\') return false;
		}
		return false;
	}
	
	/**
	 * Method that removes symbols for tag from given parameter {@code String}
	 * @param tagFromText tag from text
	 * @return {@link String} representing a tag without symbols for it.
	 */
	public static String cutOffSymbols(String tagFromText) {
		
		if (!tagFromText.contains("$")) {
			
			return null;
		}
		
		int indexOfFirst$ = tagFromText.indexOf("$");
		tagFromText = tagFromText.substring(indexOfFirst$+1);
		
		if (!tagFromText.contains("$")) {
			
			return null;
		}
		int indexOfLast$ = tagFromText.indexOf("$"); 
		tagFromText =tagFromText.substring(0, indexOfLast$);
		return tagFromText.trim();
	}

	/**
	 * Removes tag name from given tag and lefts only tag parameters.
	 * @param unWrappedTag tag with parameters.
	 * @param tag name of tag
	 * @return tags parameters.
	 */
	public static String removeTag(String unWrappedTag, String tag) {

		int lengthOfTag = tag.length();
		String string = unWrappedTag.substring(lengthOfTag);
		return string.trim();
	}
	
	/**
	 * Extracts arguments from tag and returns them in list
	 * @param tag tags parameters.
	 * @return parameters in a list.
	 */
	public static List<String> extractArgs(String tag) {

		tag = hideEscaped(tag);
		
		tag = tag.replace("\"", " \" ");
		tag = tag.replace("■", " ■ ");
		List<String> arguments = new ArrayList<String>();
		String normalArg = "";
		for(int i = 0; i < tag.length(); i++) {
			
			if(tag.charAt(i) == '"' || tag.charAt(i) == '■' ) {
				String quotedArg = ""+tag.charAt(i);
				i++;
				while(tag.charAt(i) != '"' && tag.charAt(i) != '■') {
					quotedArg += tag.charAt(i);
					i++;
				}
				arguments.add(quotedArg+tag.charAt(i++));
			}
			if(Character.isWhitespace(tag.charAt(i))) {
				arguments.add(normalArg);
				normalArg = "";
			}
			normalArg += tag.charAt(i);
		}
		if(!normalArg.isEmpty()) {
			arguments.add(normalArg);
		}
		
		List<String> realArguments = new ArrayList<String>();
		for(String arg : arguments) {
			arg = arg.trim();
			if(arg.isEmpty()) {
				continue;
			}
			realArguments.add(arg);
		}
		
		return realArguments;
	}

	/**
	 * Hides escaped symbols in tag
	 * @param tag tag with symbols
	 * @return tag with hidden escaped symbols
	 */
	private static String hideEscaped(String tag) {
		
		String tagModified = tag.replace("\\\"", "§");
		tagModified = tagModified.replace("\\§", "■");
		tagModified = tagModified.replace("\\■", "ń");
		
		return tagModified;
	}

	/**
	 * Parses arguments and creates suitable tokens for them
	 * @param arguments arguments of a tag.
	 * @return list of tokens representing arguments.
	 */
	public static List<Token> parseToTokens(List<String> arguments) {
		
		List<Token> tokens = new ArrayList<Token>();
		for(String arg : arguments) {
			arg = arg.trim();
			if(isString(arg)) {
				tokens.add(makeSringToken(arg));
				continue;
			}
			if(isOperator(arg)) {
				tokens.add(makeTokenOperator(arg));
				continue;
			}
			if(isInteger(arg)) {
				tokens.add(makeTokenInteger(arg));
				continue;
			}
			if(isDouble(arg)) {
				tokens.add(makeTokenDouble(arg));
				continue;
			}
			if(isFunction(arg)) {
				tokens.add(makeTokenFunction(arg));
				continue;
			}
			else {
				tokens.add(makeTokenVariable(arg));
			}
			
		}
		return tokens;
	}

	/**
	 * Returns true if arg is {@link String} element in node structure. False otherwise.
	 * @param arg tested string
	 * @return true if arg is {@link String} element in node structure. False otherwise.
	 */
	private static boolean isString(String arg) {
		return arg.contains("\"") || arg.contains("■");
	}

	/**
	 * Creates a {@link TokenVariable} representing a arg parameter.
	 * @param arg parameter representing a token.
	 * @return new {@link TokenVariable}
	 */
	private static Token makeTokenVariable(String arg) {
		arg = returnToNormal(arg);
		return new TokenVariable(arg);
	}

	/**
	 * Creates {@link TokenFunction} from arg parameter.
	 * @param arg parameter representing a {@link TokenFunction}
	 * @return new {@link TokenFunction}
	 */
	private static Token makeTokenFunction(String arg) {
		arg = returnToNormal(arg);
		arg = arg.substring(1);
		return new TokenFunction(arg);
	}

	/**
	 * Creates {@link TokenConstantDouble} from arg parameter.
	 * @param arg parameter representing a token
	 * @return new {@link TokenConstantDouble}
	 */
	private static Token makeTokenDouble(String arg) {
		
		return new TokenConstantDouble(Double.parseDouble(arg));
	}

	/**
	 * Creates {@link TokenConstantInteger} from arg parameter.
	 * @param arg parameter representing a token
	 * @return new {@link TokenConstantInteger}
	 */
	private static Token makeTokenInteger(String arg) {
		
		return new TokenConstantInteger(Integer.parseInt(arg));
	}
	/**
	 * Creates {@link TokenOperator} from arg parameter.
	 * @param arg parameter representing a token
	 * @return new {@link TokenOperator}
	 */
	private static Token makeTokenOperator(String arg) {
		
		return new TokenOperator(arg);
	}

	/**
	 * Tells if given arg parameter is token representing a {@link TokenOperator}
	 * @param arg parameter representing a token
	 * @return true if given arg parameter is token representing a {@link TokenOperator}. False otherwise
	 */
	private static boolean isOperator(String arg) {
		if(arg.length() > 1) {
			return false;
		}
		if(arg.equals("*") || arg.equals("+") || arg.equals("-") || arg.equals("/") || arg.equals("%")) {
			return true;
		}
		return false;
	}

	/**
	 * Tells if given arg parameter is token representing a {@link TokenConstantInteger}
	 * @param arg parameter representing a token
	 * @return true if given arg parameter is token representing a {@link TokenConstantInteger}. False otherwise
	 */
	private static boolean isInteger(String arg) {
		try {
			Integer.parseInt(arg);
		} catch(NumberFormatException e) {
			return false;
		}
		return true;
	}

	/**
	 * Tells if given arg parameter is token representing a {@link TokenConstantDouble}
	 * @param arg parameter representing a token
	 * @return true if given arg parameter is token representing a {@link TokenConstantDouble}. False otherwise
	 */
	private static boolean isDouble(String arg) {
		try {
			Double.parseDouble(arg);
		} catch(NumberFormatException e) {
			return false;
		}
		return true;
	}

	/**
	 * Tells if given arg parameter is token representing a {@link TokenFunction}
	 * @param arg parameter representing a token
	 * @return true if given arg parameter is token representing a {@link TokenFunction}. False otherwise
	 */
	private static boolean isFunction(String arg) {
		return arg.startsWith("@");
	}

	/**
	 * Creates new {@link TokenString} from given arg.
	 * @param arg representing a token
	 * @return new {@link TokenString}
	 */
	private static Token makeSringToken(String arg) {
		
		arg = returnToNormal(arg);
		return new TokenString(arg);
	}

	/**
	 * Unhides escaped elements in given argument text
	 * @param arg text as string
	 * @return converted text
	 */
	private static String returnToNormal(String arg) {
		
		arg = arg.replace('"', ' ').trim();
		arg = arg.replace("■", "\\");
		arg = arg.replace("ń", "\\\\\"");
		arg = arg.replace("§", "\\\"");
		
		return arg;
	}
}
