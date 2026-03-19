package com.cookiegramstudios.cookiegram.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.junit.jupiter.api.Test;

public class NameParserTest {

	@Test
	void parseFullName_null_returnsEmptyParts() {
		String[] result = NameParser.parseFullName(null);

		assertEquals(2, result.length);
		assertEquals("", result[0]);
		assertEquals("", result[1]);
	}

	@Test
	void parseFullName_blank_returnsEmptyParts() {
		String[] result = NameParser.parseFullName("   ");

		assertEquals(2, result.length);
		assertEquals("", result[0]);
		assertEquals("", result[1]);
	}

	@Test
	void parseFullName_singleWord_returnsFirstAndEmptyLast() {
		String[] result = NameParser.parseFullName("Madonna");

		assertEquals("Madonna", result[0]);
		assertEquals("", result[1]);
	}

	@Test
	void parseFullName_twoWords_splitsCorrectly() {
		String[] result = NameParser.parseFullName("John Doe");

		assertEquals("John", result[0]);
		assertEquals("Doe", result[1]);
	}

	@Test
	void parseFullName_multipleWords_keepsRemainderAsLastName() {
		String[] result = NameParser.parseFullName("John Jacob Jingleheimer Schmidt");

		assertEquals("John", result[0]);
		assertEquals("Jacob Jingleheimer Schmidt", result[1]);
	}

	@Test
	void getFirstName_delegatesToParseFullName() {
		String result = NameParser.getFirstName("Jane Smith");

		assertEquals("Jane", result);
	}

	@Test
	void getLastName_delegatesToParseFullName() {
		String result = NameParser.getLastName("Jane Smith");

		assertEquals("Smith", result);
	}

	@Test
	void normalizeName_null_returnsEmpty() {
		assertEquals("", NameParser.normalizeName(null));
	}

	@Test
	void normalizeName_blank_returnsEmpty() {
		assertEquals("", NameParser.normalizeName("   "));
	}

	@Test
	void normalizeName_multipleSpaces_collapsesSpaces() {
		String result = NameParser.normalizeName("  John   Jacob   Smith  ");

		assertEquals("John Jacob Smith", result);
	}

	@Test
	void capitalizeWords_null_returnsEmpty() {
		assertEquals("", NameParser.capitalizeWords(null));
	}

	@Test
	void capitalizeWords_blank_returnsEmpty() {
		assertEquals("", NameParser.capitalizeWords("   "));
	}

	@Test
	void capitalizeWords_mixedCase_formatsEachWord() {
		String result = NameParser.capitalizeWords("jOhN doE");

		assertEquals("John Doe", result);
	}

	@Test
	void capitalizeWords_withExtraSpaces_normalizesThenCapitalizes() {
		String result = NameParser.capitalizeWords("  maRY   aNNe   smiTH ");

		assertEquals("Mary Anne Smith", result);
	}

	@Test
	void privateConstructor_throwsUnsupportedOperationException() throws Exception {
		Constructor<NameParser> constructor = NameParser.class.getDeclaredConstructor();
		constructor.setAccessible(true);

		InvocationTargetException ex = assertThrows(InvocationTargetException.class, constructor::newInstance);

		assertTrue(ex.getCause() instanceof UnsupportedOperationException);
		assertEquals("Utility class - do not instantiate", ex.getCause().getMessage());
	}

}
