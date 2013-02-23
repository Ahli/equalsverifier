/*
 * Copyright 2009-2010, 2013 Jan Ouwens
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
package nl.jqno.equalsverifier.util;

import static nl.jqno.equalsverifier.testhelpers.Util.assertAssertionError;

import org.junit.Test;

public class AssertTest {
	private static final Formatter FAIL = Formatter.of("fail");
	@Test
	public void assertEqualsObjectSuccess() {
		String red = new String("text");
		String black = new String("text");
		Assert.assertEquals(FAIL, red, black);
	}
	
	@Test
	public void assertEqualsObjectFailure() {
		Runnable r = new Runnable() {
			public void run() {
				Assert.assertEquals(FAIL, "one", "two");
			}
		};
		assertAssertionError(r, "fail");
	}
	
	@Test
	public void assertFalseSuccess() {
		Assert.assertFalse(FAIL, false);
	}
	
	@Test
	public void assertFalseFailure() {
		Runnable r = new Runnable() {
			public void run() {
				Assert.assertFalse(FAIL, true);
			}
		};
		assertAssertionError(r, "fail");
	}
	
	@Test
	public void assertTrueSuccess() {
		Assert.assertTrue(FAIL, true);
	}
	
	@Test
	public void assertTrueFailure() {
		Runnable r = new Runnable() {
			public void run() {
				Assert.assertTrue(FAIL, false);
			}
		};
		assertAssertionError(r, "fail");
	}
	
	@Test
	public void failFailure() {
		Runnable r = new Runnable() {
			public void run() {
				Assert.fail(FAIL);
			}
		};
		assertAssertionError(r, "fail");
	}
}
