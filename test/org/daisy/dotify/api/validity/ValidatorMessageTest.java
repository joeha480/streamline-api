package org.daisy.dotify.api.validity;

import static org.junit.Assert.assertEquals;

import org.daisy.dotify.api.validity.ValidatorMessage.Type;
import org.junit.Test;

@SuppressWarnings("javadoc")
public class ValidatorMessageTest {

	@Test
	public void testToString_01() {
		String actual = ValidatorMessage.with(Type.NOTICE)
			.lineNumber(1)
			.columnNumber(2)
			.message("Something went wrong.")
			.exception(new RuntimeException("Problem occured."))
			.build()
			.toString();
		assertEquals("NOTICE (at line 1, column 2): Something went wrong. [java.lang.RuntimeException: Problem occured.]", actual);
	}
	
	@Test
	public void testToString_02() {
		String actual = ValidatorMessage.with(Type.WARNING)
				.lineNumber(5)
				.columnNumber(1)
				.message("Something went wrong.")
				.build()
				.toString();
		assertEquals("WARNING (at line 5, column 1): Something went wrong.", actual);
	}
	
	@Test
	public void testToString_03() {
		String actual = ValidatorMessage.with(Type.WARNING)
				.lineNumber(5)
				.message("Something went wrong.")
				.build()
				.toString();
		assertEquals("WARNING (at line 5): Something went wrong.", actual);
	}
	
	@Test
	public void testToString_04() {
		String actual = ValidatorMessage.with(Type.ERROR)
				.build()
				.toString();
		assertEquals("ERROR: [No message]", actual);
	}
	
	@Test
	public void testToString_05() {
		String actual = ValidatorMessage.with(Type.FATAL_ERROR)
				.lineNumber(5)
				.columnNumber(0)
				.build()
				.toString();
		assertEquals("FATAL_ERROR (at line 5, column 0): [No message]", actual);
	}

}
