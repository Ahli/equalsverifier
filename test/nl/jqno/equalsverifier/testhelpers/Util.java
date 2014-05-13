/*
 * Copyright 2010, 2013-2014 Jan Ouwens
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package nl.jqno.equalsverifier.testhelpers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.junit.matchers.JUnitMatchers.containsString;

import java.lang.reflect.Field;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.util.FieldIterable;

public class Util {
	public static void assertFailure(EqualsVerifier<?> equalsVerifier, String first, String... more) {
		assertFailure(equalsVerifier, null, first, more);
	}
	
	public static void assertFailure(EqualsVerifier<?> equalsVerifier, Class<? extends Throwable> cause, String first, String... more) {
		try {
			equalsVerifier.verify();
		}
		catch (AssertionError e) {
			assertMessageContainsAll(e, first, more);
			assertCause(e, cause);
			return;
		}
		catch (Throwable e) {
			e.printStackTrace();
			fail("Wrong exception thrown: " + e.getClass());
		}
		fail("Assertion didn't fail");
	}
	
	public static void assertMessageContainsAll(Throwable e, String first, String... more) {
		String message = e.getMessage();
		assertThat(message, containsString(first));
		for (String s : more) {
			assertThat(message, containsString(s));
		}
	}
	
	private static void assertCause(Throwable e, Class<? extends Throwable> cause) {
		if (cause != null) {
			assertEquals(cause, e.getCause().getClass());
		}
	}
	
	public static boolean defaultEquals(Object here, Object there) {
		Class<?> type = here.getClass();
		if (!here.getClass().isAssignableFrom(type)) {
			return false;
		}
		boolean equals = true;
		try {
			for (Field f : FieldIterable.of(type)) {
				f.setAccessible(true);
				Object x = f.get(here);
				Object y = there == null ? null : f.get(there);
				equals &= nullSafeEquals(x, y);
			}
		}
		catch (IllegalAccessException e) {
			fail(e.toString());
		}
		return equals;
	}
	
	public static int defaultHashCode(Object x) {
		int hash = 59;
		try {
			for (Field f : FieldIterable.of(x.getClass())) {
				f.setAccessible(true);
				Object val = f.get(x);
				hash += 59 * nullSafeHashCode(val);
			}
		}
		catch (IllegalAccessException e) {
			fail(e.toString());
		}
		return hash;
	}
	
	public static boolean nullSafeEquals(Object x, Object y) {
		return x == null ? y == null : x.equals(y);
	}
	
	public static int nullSafeHashCode(Object x) {
		return x == null ? 0 : x.hashCode();
	}
}
