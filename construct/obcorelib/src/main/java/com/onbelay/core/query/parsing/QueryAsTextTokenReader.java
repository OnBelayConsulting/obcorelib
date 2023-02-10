/*
 Copyright 2019, OnBelay Consulting Ltd.

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.  
*/
package com.onbelay.core.query.parsing;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

/**
 * Reads a String and provides methods to peek at the next character to determine how to process the string.
 * The read pointer is advanced with a read method.
 * @author lefeu
 *
 */
public class QueryAsTextTokenReader {
	
	private static final Character OPEN_BRACKET = new Character('(');
	private static final Character CLOSE_BRACKET = new Character(')');
	private static final Character PERIOD = new Character('.');
	private static final Character EQUALS = new Character('=');
	private static final Character BANG = new Character('!');
	private static final Character GREATER_THAN = new Character('>');
	private static final Character LESS_THAN = new Character('<');
	private static final Character SINGLE_QUOTE = new Character('\'');
	private static final Character UNDERSCORE = new Character('_');
	
	public static final int UNKNOWN = 0;
	public static final int IS_WHITE_SPACE = 1;
	public static final int IS_LETTER = 3;
	public static final int IS_NUMBER = 4;
	public static final int IS_PERIOD = 5;
	public static final int IS_BRACKET = 10;
	public static final int IS_SINGLE_QUOTE = 11;
	public static final int IS_COMMA = 12;
	public static final int IS_OPEN_BRACKET = 15;
	public static final int IS_CLOSE_BRACKET = 16;
	public static final int IS_EQUALS = 20;
	public static final int IS_GREATER_THAN = 21;
	public static final int IS_LESS_THAN = 22;
	public static final int IS_BANG = 23;
	
	
	private static HashMap<Character, Integer> charMap = new HashMap<>();
	
	private static HashMap<Integer, Predicate<Character>> peekMap = new HashMap<Integer, Predicate<Character>>();
	
	static {
		
		charMap.put(
				PERIOD,
				IS_PERIOD);
		charMap.put(
				EQUALS,
				IS_EQUALS);
		charMap.put(
				SINGLE_QUOTE,
				IS_SINGLE_QUOTE);
		charMap.put(
				GREATER_THAN,
				IS_GREATER_THAN);
		charMap.put(
				LESS_THAN,
				IS_LESS_THAN);
		charMap.put(
				BANG,
				IS_BANG);
		charMap.put(
				OPEN_BRACKET,
				IS_OPEN_BRACKET);
		charMap.put(
				CLOSE_BRACKET,
				IS_CLOSE_BRACKET);
		
		
		peekMap.put(
				IS_WHITE_SPACE,
				Character::isWhitespace);
		peekMap.put(
				IS_LETTER,
				Character::isLetter);
		peekMap.put(
				IS_NUMBER,
				Character::isDigit);
	}

	private StringBuilder expressionBuffer;
	private int currentPosition = 0;
	
	public QueryAsTextTokenReader(String expressionText) {
		this.expressionBuffer = new StringBuilder(expressionText);
	}

	public QueryAsTextTokenReader(StringBuilder expressionText) {
		this.expressionBuffer = expressionText;
	}

	public boolean peekIsSingleQuote() {
		return SINGLE_QUOTE.equals(expressionBuffer.charAt(currentPosition));
	}
	
	public boolean peekIsLetterOrDigit() {
		 return (Character.isLetterOrDigit(expressionBuffer.charAt(currentPosition)) || 
				 UNDERSCORE.equals(peek()));
	}
	
	public boolean peekIsDigitOrPeriod() {
		 return (Character.isDigit(expressionBuffer.charAt(currentPosition)) || PERIOD.equals(peek()));
	}
	
	public Character peek() {
		return new Character(expressionBuffer.charAt(currentPosition));
	}
	
	public Integer peekNext() {
		
		Integer type = charMap.get(peek());
		
		if (type != null)
			return type;
		
		
		for (Map.Entry<Integer, Predicate<Character>> entry : peekMap.entrySet()) {
			
			if (entry.getValue().test(expressionBuffer.charAt(currentPosition)))
				return entry.getKey();
		}
		return UNKNOWN;
		
	}
	
	public String readQuotedString() {
		read();
		StringBuilder buffer = new StringBuilder();
		while (hasNext()) {
			if (peekIsSingleQuote()) {
				read();
				break;
			}
			buffer.append(read());
			
		}
		return buffer.toString();
	}
	
	public String readString() {
		StringBuilder buffer = new StringBuilder();
		while (hasNext()) {
			if (peekIsLetterOrDigit())
				buffer.append(read());
			else
				break;
			
		}
		return buffer.toString();
	}
	
	public BigDecimal readNumber() {
		StringBuilder buffer = new StringBuilder();
		while (hasNext()) {
			if (peekIsDigitOrPeriod())
				buffer.append(read());
			else
				break;
			
		}
		return new BigDecimal(buffer.toString());
	}
	
	public boolean hasNext() {
		return currentPosition < expressionBuffer.length();
	}
	
	public char read() {
		return expressionBuffer.charAt(currentPosition++);
	}
	
}
