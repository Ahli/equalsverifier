/*
 * Copyright 2015 Jan Ouwens
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
package nl.jqno.equalsverifier;

import static nl.jqno.equalsverifier.util.Assert.assertEquals;
import static nl.jqno.equalsverifier.util.Assert.assertFalse;
import static nl.jqno.equalsverifier.util.Assert.fail;

import java.util.EnumSet;

import nl.jqno.equalsverifier.util.Formatter;

public class CachedHashCodeChecker<T> implements Checker {
	private final CachedHashCodeInitializer<T> cachedHashCodeInitializer;
	private final EnumSet<Warning> warningsToSuppress;
	
	public CachedHashCodeChecker(CachedHashCodeInitializer<T> cachedHashCodeInitializer, EnumSet<Warning> warningsToSuppress) {
		this.cachedHashCodeInitializer = cachedHashCodeInitializer;
		this.warningsToSuppress = warningsToSuppress;
	}
	
	@Override
	public void check() {
		if (cachedHashCodeInitializer.isPassthrough()) {
			return;
		}
		if (warningsToSuppress.contains(Warning.NONFINAL_FIELDS)) {
			fail(Formatter.of("EqualsVerifier can only check cached hashCodes for immutable classes."));
		}
		
		T reference = cachedHashCodeInitializer.getExample();
		int actualHashCode = reference.hashCode();
		assertFalse(Formatter.of("example.hashCode() cannot be zero. Please choose a different example."),
				actualHashCode == 0);
		
		int recomputedHashCode = cachedHashCodeInitializer.getInitializedHashCode(reference);
		assertEquals(Formatter.of("cachedHashCode is not properly initialized."), actualHashCode, recomputedHashCode);
	}
}
