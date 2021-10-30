package net.blixate.config;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.blixate.config.error.ConfigParsingException;
import net.blixate.config.parser.Tok;
import net.blixate.config.parser.Token;

public class ConfigParser {
	
	static final Pattern PARENT_SEPERATE_PATTERN =
			Pattern.compile("\\[([a-zA-Z_]+[a-zA-Z0-9_]*).*?[:].*?([a-zA-Z_]+[a-zA-Z0-9_]*)\\]");
	
	static Pattern pattern;
	
	static {
		StringBuffer tokenPatternsBuffer = new StringBuffer();
		for(Token token : Token.values())
			tokenPatternsBuffer.append("|(?<" + token.name() + ">" + token.regex() + ")");
		String tokenPatternsString = tokenPatternsBuffer.toString().substring(1);
		pattern = Pattern.compile(tokenPatternsString);
	}
	
	/**
	 * A list of tokens, which is filled when {@link ConfigParser#lex(String)} is called.
	 */
	Tok[] tokens;
	
	
	/**
	 * Default value is null. This is where output should be printed to.
	 * Use {@link System#out} for console output, or a {@code FileOutputStream} to output to a file.
	 */
	OutputStream dump = null;
	
	
	
	ArrayList<ConfigSection> sections;
	
	public ConfigParser(OutputStream output) {
		this.dump = output;
		this.sections = new ArrayList<>();
	}
	
	public ConfigSection[] getSections() {
		return this.sections.toArray(new ConfigSection[0]);
	}
	/**
	 * Lexical analysis of the file. Output is saved to {@link ConfigParser#tokens}.
	 */
	public void lex(String content) {
		long start = System.currentTimeMillis();
		Matcher matcher = pattern.matcher(content);
		ArrayList<Tok> tokens = new ArrayList<>();
		Tok tok = null;
		while(matcher.find()) {
			String data = matcher.group();
			for(Token t : Token.values()) {
				if(data.matches(t.regex())) {
					tok = new Tok(t, data, matcher.start());
					tokens.add(tok);
					debug("[Lexer] " + tok.toString());
					break;
				}
			}
		}
		this.tokens = tokens.toArray(new Tok[0]);
		long end = System.currentTimeMillis();
		debug("Lex Time: " + (end - start) + "ms");
	}
	
	/**
	 * Parse the file's contents and load the relevant data into classes.
	 * Before you call this function, you must call {@link ConfigParser#lex(String)}
	 */
	public void parse() {
		long start = System.currentTimeMillis();
		ConfigSection currentSection = new ConfigSection(ConfigFile.GLOBAL_SECTION);
		String property = null;
		Stack<Tok> value = new Stack<Tok>();
		boolean readValue = false;
		Tok token;
		for(int i = 0; i < tokens.length + 1; i++) {
			if(i >= tokens.length)
				token = new Tok(Token.ENDL, ";", tokens[i-1].index()); // Fake a token for terminator
			else
				token = tokens[i];
			
			if(readValue) {
				if(isEndOfPropertyDefinition(token)) {
					ConfigProperty prop = null;
					
					if(!readValue) {
						
						if(value.isEmpty()) {
							prop = new ConfigProperty(property, "null");
						}
						
					} else {
						if(isInvalidValue(value)){
							configError("Invalid value while evaluating property \"" + property + "\"", token);
						}
						
						readValue = false;
						prop = evaluate(property, value);
						if(currentSection.hasProperty(prop.getName())) {
							debug("[Parser] Property \"" + prop.getName() + "\" overwritten.");
						}
					}
					
					if(prop != null) {
						currentSection.addProperty(prop.getName(), prop);
						property = null;
						value.clear();
						i--; // move backwards
						debug("[Parser] Property \"" + prop.getName() + "\" created.");
					}
				} else {
					value.push(token);
				}
			}
			else {
				if(token.t == Token.SECTION) {
					if(property != null) configError("Invalid section start", token);
					this.sections.add(currentSection);
					debug("[Parser] Section \"" + currentSection.name + "\" defined.");
					currentSection = new ConfigSection(token.data().substring(1, token.data().length()-1));
				}
				else if(token.t == Token.PROPERTY) {
					if(property != null) {
						configError("Incorrect property definition", token);
					}
					property = token.data();
				}
				else if(token.t == Token.EQUALS) {
					if(readValue) configError("'"+token.data()+"' is not a value.", token);
					readValue = true;
				}
			}
		}
		if(currentSection != null) {
			this.sections.add(currentSection);
			debug("[Parser] Section \"" + currentSection.name + "\" defined. (EOF-terminated)");
		}
		long end = System.currentTimeMillis();
		debug("Parse Time: " + (end - start) + "ms");
	}
	
	/**
	 * Roughly figures out if this is the end of a property's value
	 */
	private boolean isEndOfPropertyDefinition(Tok token) {
		return token.t == Token.ENDL || token.t == Token.PROPERTY || token.t == Token.SECTION;
	}

	private static ConfigProperty evaluate(String propName, Stack<Tok> tokens) {
		if(tokens.size() < 1) {
			return null;
		}
		if(tokens.firstElement().t == Token.LBRACE) {
			return evaluateArray(propName, tokens);
		}
		return new ConfigProperty(propName, tokens.peek().data());
	}
	
	private static boolean isInvalidValue(Stack<Tok> value) {
		switch(value.firstElement().t) {
		case COMMENT:
		case LBRACE:
		case RBRACE:
		case NUMBER:
		case STRING:
		case NULL:
		case BOOLEAN:
			return false;
		default:
			return true;
		}
	}
	
	private static ConfigArray evaluateArray(String propName, Stack<Tok> tokens) {
		int nestingLevel = 0;
		Stack<Tok> elements = new Stack<Tok>();
		ConfigArray array = new ConfigArray(propName, null);
		int index = 0;
		for(Tok t : tokens){
			switch(t.t) {
			case LBRACE:
				nestingLevel++;
				if(nestingLevel > 1) {
					elements.push(t);
				}
				break;
			case RBRACE:
				nestingLevel--;
				if(nestingLevel != 0) { elements.push(t); }
				if(!elements.isEmpty()){
					array.add(evaluate(propName + "[" + index + "]", elements));
					elements.clear();
				}
				break;
			case COMMA:
				if(nestingLevel == 1) {
					if(!elements.isEmpty()) {
						array.add(evaluate(propName + "[" + index + "]", elements));
					}
					elements.clear();
					index++;
					break;
				}
			default:
				elements.push(t);
				break;
			}
		}
		if(nestingLevel != 0) {
			throw new ConfigParsingException("Array not terminated!");
		}
		return array;
	}
	
	private void configError(String s, Tok t) {
		if(this.dump != null) {
			debug("[Configuration Error] " + s + " (At index "+t.index()+")");
		}else{
			throw new ConfigParsingException(s + " (At index "+t.index()+")");
		}
	}
	private void debug(String...strings) {
		if(this.dump == null) return;
		String line = String.join("", strings) + "\n";
		try {
			this.dump.write(line.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
