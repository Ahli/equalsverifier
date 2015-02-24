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
package nl.jqno.equalsverifier.integration.extra_features;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import nl.jqno.equalsverifier.testhelpers.IntegrationTestBase;

import org.junit.Test;

import javax.annotation.Nonnull;

/**
 * @author Niall Gallagher, Jan Ouwens
 */
public class CachedHashCodeTest extends IntegrationTestBase {
	private static final String SOME_NAME = "some name";
	private static final String MALFORMED_CALCULATEHASHCODEMETHOD = "Could not find calculateHashCodeMethod: must be 'private int";
	private static final String MALFORMED_CACHEDHASHCODEFIELD = "Could not find cachedHashCodeField: must be 'private int";
	
	@Test
	public void fail_whenCachedHashCodeIsValid_givenWithCachedHashCodeIsNotUsed() {
		expectFailure("Significant fields", "equals relies on", "name", "but hashCode does not");
		EqualsVerifier.forClass(ObjectWithValidCachedHashCode.class)
				.verify();
	}
	
	@Test
	public void succeed_whenCachedHashCodeIsValid_givenWithCachedHashCodeIsUsed() {
		EqualsVerifier.forClass(ObjectWithValidCachedHashCode.class)
				.withCachedHashCode("cachedHashCode", "calcHashCode", new ObjectWithValidCachedHashCode(SOME_NAME))
				.verify();
	}
	
	@Test
	public void succeed_whenCachedHashCodeIsValidAndLocatedInSuperclass_givenWithCachedHashCodeIsUsed() {
		EqualsVerifier.forClass(Subclass.class)
				.withCachedHashCode("cachedHashCode", "calcHashCode", new Subclass(SOME_NAME))
				.verify();
	}
	
	@Test
	public void fail_whenCachedHashCodeIsInvalid_givenWithCachedHashCodeIsUsed() {
		expectFailure("Significant fields", "equals relies on", "name", "but hashCode does not");
		EqualsVerifier.forClass(ObjectWithInvalidCachedHashCode.class)
				.withCachedHashCode("cachedHashCode", "calcHashCode", new ObjectWithInvalidCachedHashCode(SOME_NAME))
				.verify();
	}
	
	@Test
	public void fail_whenCachedHashCodeFieldDoesNotExist() {
		expectException(IllegalArgumentException.class, "Could not find cachedHashCodeField", "doesNotExist");
		EqualsVerifier.forClass(ObjectWithValidCachedHashCode.class)
				.withCachedHashCode("doesNotExist", "calcHashCode", new ObjectWithValidCachedHashCode(SOME_NAME));
	}
	
	@Test
	public void fail_whenCalculateHashCodeMethodDoesNotExist() {
		expectException(IllegalArgumentException.class, "Could not find calculateHashCodeMethod", "doesNotExist");
		EqualsVerifier.forClass(ObjectWithValidCachedHashCode.class)
				.withCachedHashCode("cachedHashCode", "doesNotExist", new ObjectWithValidCachedHashCode(SOME_NAME));
	}
	
	@Test
	public void fail_whenCachedHashCodeFieldIsNotPrivate() {
		expectException(IllegalArgumentException.class, MALFORMED_CACHEDHASHCODEFIELD, "notPrivate");
		EqualsVerifier.forClass(InvalidCachedHashCodeFieldContainer.class)
				.withCachedHashCode("notPrivate", "calculateHashCode", new InvalidCachedHashCodeFieldContainer());
	}
	
	@Test
	public void fail_whenCachedHashCodeFieldIsNotAnInt() {
		expectException(IllegalArgumentException.class, MALFORMED_CACHEDHASHCODEFIELD, "notAnInt");
		EqualsVerifier.forClass(InvalidCachedHashCodeFieldContainer.class)
				.withCachedHashCode("notAnInt", "calculateHashCode", new InvalidCachedHashCodeFieldContainer());
	}
	
	@Test
	public void fail_whenCalculateHashCodeMethodIsNotPrivate() {
		expectException(IllegalArgumentException.class, MALFORMED_CALCULATEHASHCODEMETHOD, "notPrivate");
		EqualsVerifier.forClass(InvalidCalculateHashCodeMethodsContainer.class)
				.withCachedHashCode("cachedHashCode", "notPrivate", new InvalidCalculateHashCodeMethodsContainer());
	}
	
	@Test
	public void fail_whenCalculateHashCodeMethodDoesNotReturnInt() {
		expectException(IllegalArgumentException.class, MALFORMED_CALCULATEHASHCODEMETHOD, "notAnInt");
		EqualsVerifier.forClass(InvalidCalculateHashCodeMethodsContainer.class)
				.withCachedHashCode("cachedHashCode", "notAnInt", new InvalidCalculateHashCodeMethodsContainer());
	}
	
	@Test
	public void fail_whenCalculateHashCodeMethodTakesParamters() {
		expectException(IllegalArgumentException.class, MALFORMED_CALCULATEHASHCODEMETHOD, "takesParameters");
		EqualsVerifier.forClass(InvalidCalculateHashCodeMethodsContainer.class)
				.withCachedHashCode("cachedHashCode", "takesParameters", new InvalidCalculateHashCodeMethodsContainer());
	}
	
	@Test
	public void fail_whenCachedHashCodeFieldIsNotInitialized() {
		expectFailure("cachedHashCode is not properly initialized.");
		EqualsVerifier.forClass(ObjectWithUninitializedCachedHashCode.class)
				.withCachedHashCode("cachedHashCode", "calcHashCode", new ObjectWithUninitializedCachedHashCode(SOME_NAME))
				.verify();
	}
	
	@Test
	public void fail_whenExampleIsNull() {
		expectException(NullPointerException.class, "example");
		EqualsVerifier.forClass(ObjectWithUninitializedCachedHashCode.class)
				.withCachedHashCode("cachedHashCode", "calcHashCode", null);
	}
	
	@Test
	public void fail_whenHashCodeIsZero() {
		expectFailure("example.hashCode() cannot be zero. Please choose a different example.");
		EqualsVerifier.forClass(ObjectWithLegitimatelyZeroHashCode.class)
				.withCachedHashCode("cachedHashCode", "calcHashCode", new ObjectWithLegitimatelyZeroHashCode(1))
				.verify();
	}
	
	@Test
	public void fail_whenCachedHashCodeIsValid_givenWithCachedHashCodeIsUsedAndWarningNonfinalFieldsIsSuppressed() {
		expectFailure("EqualsVerifier can only check cached hashCodes for immutable classes.");
		EqualsVerifier.forClass(ObjectWithValidCachedHashCode.class)
				.withCachedHashCode("cachedHashCode", "calcHashCode", new ObjectWithValidCachedHashCode(SOME_NAME))
				.suppress(Warning.NONFINAL_FIELDS)
				.verify();
	}
	
	@Test
	public void succeed_whenCachedHashCodeIsLazilyInitialized_givenItIsValid() {
		EqualsVerifier.forClass(ObjectWithLazilyInitializedCachedHashCode.class)
				.withCachedHashCode("cachedHashCode", "calcHashCode", new ObjectWithLazilyInitializedCachedHashCode(SOME_NAME))
				.verify();
	}
	
	static class ObjectWithValidCachedHashCode {
		@Nonnull private final String name;
		private final int cachedHashCode;
		
		public ObjectWithValidCachedHashCode(String name) {
			this.name = name;
			this.cachedHashCode = calcHashCode();
		}
		
		@Override
		public final boolean equals(Object obj) {
			if (!(obj instanceof ObjectWithValidCachedHashCode)) {
				return false;
			}
			ObjectWithValidCachedHashCode that = (ObjectWithValidCachedHashCode) obj;
			return name.equals(that.name);
		}
		
		@Override
		public final int hashCode() {
			return cachedHashCode;
		}
		
		private int calcHashCode() {
			return name.hashCode();
		}
	}
	
	static final class Subclass extends ObjectWithValidCachedHashCode {
		public Subclass(String name) { super(name); }
	}
	
	static class ObjectWithInvalidCachedHashCode {
		@Nonnull private final String name;
		private final int cachedHashCode;
		
		public ObjectWithInvalidCachedHashCode(String name) {
			this.name = name;
			this.cachedHashCode = calcHashCode();
		}
		
		@Override
		public final boolean equals(Object obj) {
			if (!(obj instanceof ObjectWithInvalidCachedHashCode)) {
				return false;
			}
			ObjectWithInvalidCachedHashCode that = (ObjectWithInvalidCachedHashCode) obj;
			return name.equals(that.name);
		}
		
		@Override
		public final int hashCode() {
			return cachedHashCode;
		}
		
		private int calcHashCode() {
			return 3;
		}
	}
	
	@SuppressWarnings("unused")
	static class InvalidCachedHashCodeFieldContainer {
		public int notPrivate;
		private String notAnInt;
		
		private int calculateHashCode() { return -1; }
	}
	
	@SuppressWarnings("unused")
	static class InvalidCalculateHashCodeMethodsContainer {
		private int cachedHashCode;
		
		public int notPrivate() { return -1; }
		private String notAnInt() { return "wrong"; }
		private int takesParameters(int x) { return x; }
	}
	
	static class ObjectWithUninitializedCachedHashCode {
		@Nonnull private final String name;
		private final int cachedHashCode;
		
		public ObjectWithUninitializedCachedHashCode(String name) {
			this.name = name;
			this.cachedHashCode = 0;
		}
		
		@Override
		public final boolean equals(Object obj) {
			if (!(obj instanceof ObjectWithUninitializedCachedHashCode)) {
				return false;
			}
			ObjectWithUninitializedCachedHashCode that = (ObjectWithUninitializedCachedHashCode) obj;
			return name.equals(that.name);
		}
		
		@Override
		public final int hashCode() {
			return cachedHashCode;
		}
		
		@SuppressWarnings("unused")
		private int calcHashCode() {
			return name.hashCode();
		}
	}
	
	static class ObjectWithLegitimatelyZeroHashCode {
		@Nonnull private final int number;
		private final int cachedHashCode;
		
		public ObjectWithLegitimatelyZeroHashCode(int number) {
			this.number = number;
			this.cachedHashCode = calcHashCode();
		}
		
		@Override
		public final boolean equals(Object obj) {
			if (!(obj instanceof ObjectWithLegitimatelyZeroHashCode)) {
				return false;
			}
			ObjectWithLegitimatelyZeroHashCode that = (ObjectWithLegitimatelyZeroHashCode) obj;
			return number == that.number;
		}
		
		@Override
		public final int hashCode() {
			return cachedHashCode;
		}
		
		private int calcHashCode() {
			return number - 1;
		}
	}
	
	static class ObjectWithLazilyInitializedCachedHashCode {
		@Nonnull private final String name;
		private int cachedHashCode;
		
		public ObjectWithLazilyInitializedCachedHashCode(String name) {
			this.name = name;
		}
		
		@Override
		public final boolean equals(Object obj) {
			if (!(obj instanceof ObjectWithLazilyInitializedCachedHashCode)) {
				return false;
			}
			ObjectWithLazilyInitializedCachedHashCode that = (ObjectWithLazilyInitializedCachedHashCode) obj;
			return name.equals(that.name);
		}
		
		@Override
		public final int hashCode() {
			if (cachedHashCode == 0) {
				cachedHashCode = calcHashCode();
			}
			return cachedHashCode;
		}
		
		private int calcHashCode() {
			return name.hashCode();
		}
	}
}
