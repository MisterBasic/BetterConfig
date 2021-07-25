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
	
	static Pattern pattern;
	static ArrayList<ConfigProperty> constantPool;
	
	static {
		constantPool = new ArrayList<>();
		StringBuffer tokenPatternsBuffer = new StringBuffer();
		for(Token token : Token.values())
			tokenPatternsBuffer.append("|(?<" + token.name() + ">" + token.regex() + ")");
		String tokenPatternsString = tokenPatternsBuffer.toString().substring(1);
		pattern = Pattern.compile(tokenPatternsString);
	}
	
	Tok[] tokens;
	OutputStream dump = null;
	
	ArrayList<ConfigSection> sections;
	
	
	
	public ConfigParser(OutputStream output) {
		this.dump = output;
		this.sections = new ArrayList<>();
	}
	
	public ConfigSection[] getSections() {
		return this.sections.toArray(new ConfigSection[0]);
	}
	
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
		long end = System.currentTimeMillis();
		debug("Lex Time: " + (end - start) + "ms");
		this.tokens = tokens.toArray(new Tok[0]);
	}
	
	public void parse() {
		long start = System.currentTimeMillis();
		ConfigSection currentSection = null;
		String property = null;
		Stack<Tok> value = new Stack<Tok>();
		boolean readValue = false;
		for(Tok token : tokens) {
			if(readValue && token.t != Token.ENDL) {
				value.push(token);
			}
			else if(token.t == Token.ENDL) {
				ConfigProperty prop = null;
				if(!readValue) {
					if(value.isEmpty()) {
						prop = new ConfigProperty(property, "null");
					}
				}else{
					readValue = false;
					if(isInvalidValue(value)){
						configError("Invalid value while evaluating property \"" + property + "\"", token);
					}
					boolean isConstant = false;
					if(property.charAt(0) == '*') {
						property = property.substring(1);
						isConstant = true;
					}
					prop = evaluate(property, value);
					if(isConstant) {
						constantPool.add(prop);
					}
					if(currentSection.hasProperty(prop.getName())) {
						debug("[Parser] Property \"" + prop.getName() + "\" overwritten.");
					}
				}
				if(prop != null) {
					currentSection.addProperty(prop.getName(), prop);
					property = null;
					value.clear();
					debug("[Parser] Property \"" + prop.getName() + "\" created.");
				}
			}
			else {
				if(token.t == Token.SECTION) {
					if(property != null) configError("Invalid section start", token);
					if(currentSection != null) {
						this.sections.add(currentSection);
						debug("[Parser] Section \"" + currentSection.name + "\" defined.");
					}
					if(sectionHasParent(token.data())) {
						Pattern p = Pattern.compile("\\[([a-zA-Z_]+[a-zA-Z0-9_]*).*?[:].*?([a-zA-Z_]+[a-zA-Z0-9_]*)\\]");
						Matcher m = p.matcher(token.data());
						if(m.find()) {
							String sectionName = m.group(1);
							String parent = m.group(2);
							currentSection = new ConfigSection(sectionName, parent);
							for(ConfigSection section : this.sections) {
								if(section.getName().equals(parent)) {
									for(ConfigProperty prop : section.getProperties()) {
										currentSection.addProperty(prop);
										debug("[Parser] Inherited property \"" + prop.getName() + "\" added.");
									}
								}
							}
						}else{
							configError("Invalid parent in section " + token.data(), token);
						}
					}else {
						currentSection = new ConfigSection(token.data().substring(1, token.data().length()-1));
					}
				}
				else if(token.t == Token.PROPERTY) {
					if(property != null) {
						configError("Incorrect property definition", token);
					}
					property = token.data();
				}
				else if(token.t == Token.EQUALS) {
					if(readValue) configError("Too many '=' when assigning property value", token);
					readValue = true;
				}
			}
		}
		if(currentSection != null) {
			this.sections.add(currentSection);
			debug("[Parser] Section created \"" + currentSection.name + "\"");
		}
		long end = System.currentTimeMillis();
		debug("Parse Time: " + (end - start) + "ms");
	}
	
	private static ConfigProperty evaluate(String propName, Stack<Tok> tokens) {
		if(tokens.size() < 1) {
			return null;
		}
		System.out.println(tokens);
		if(tokens.firstElement().t == Token.LBRACE) {
			return evaluateArray(propName, tokens);
		}
		if(tokens.peek().t == Token.PROPERTY) {
			if(tokens.peek().data().startsWith("*")) {
				throw new ConfigParsingException("Invalid use of constants, '*' (STAR) should only be used when DEFINING constants.");
			}
			ConfigProperty p = getConstant(tokens.peek().data());
			if(p == null) {
				throw new ConfigParsingException("Unknown constant \"" + tokens.peek().data() + "\" at " + tokens.peek().index());
			}
			return p;
		}
		return new ConfigProperty(propName, tokens.peek().data());
	}
	
	private static ConfigProperty getConstant(String constant) {
		for(ConfigProperty p : constantPool) {
			if(p.getName().equals(constant)) {
				return p;
			}
		}
		return null;
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
		case PROPERTY:
			return false;
		default:
			return true;
		}
	}
	
	private static ConfigArray evaluateArray(String propName, Stack<Tok> tokens) {
		System.out.println(tokens.toString());
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
			System.out.println("Array not terminated!");
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
	
	private static boolean sectionHasParent(String name) {
		return name.matches("\\[[a-zA-Z_]+[a-zA-Z0-9_]*.*?[:].*?[a-zA-Z_]+[a-zA-Z0-9_]*\\]");
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
