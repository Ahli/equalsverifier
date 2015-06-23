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

import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;

import nl.jqno.equalsverifier.util.ClassAccessor;
import nl.jqno.equalsverifier.util.PrefabValues;

public class Configuration<T> {
	private final Class<T> type;
	private final PrefabValues prefabValues;

	private final boolean allFieldsShouldBeUsed;
	private final Set<String> allFieldsShouldBeUsedExceptions;
	private final CachedHashCodeInitializer<T> cachedHashCodeInitializer;
	private final boolean usingGetClass;
	private final EnumSet<Warning> warningsToSuppress;

	private Configuration(Class<T> type, PrefabValues prefabValues, boolean allFieldsShouldBeUsed,
	                      Set<String> allFieldsShouldBeUsedExceptions, CachedHashCodeInitializer<T> cachedHashCodeInitializer,
	                      boolean usingGetClass, EnumSet<Warning> warningsToSuppress) {
		
		this.type = type;
		this.prefabValues = prefabValues;
		this.allFieldsShouldBeUsed = allFieldsShouldBeUsed;
		this.allFieldsShouldBeUsedExceptions = allFieldsShouldBeUsedExceptions;
		this.cachedHashCodeInitializer = cachedHashCodeInitializer;
		this.usingGetClass = usingGetClass;
		this.warningsToSuppress = warningsToSuppress;
	}
	
	public static <T> Configuration<T> of(Class<T> type) {
		return new Configuration<T>(type, new PrefabValues(), false, new HashSet<String>(),
				CachedHashCodeInitializer.<T>passthrough(), false, EnumSet.noneOf(Warning.class));
	}
	
	public Class<T> getType() {
		return type;
	}
	
	public PrefabValues getPrefabValues() {
		return prefabValues;
	}

	public Configuration<T> withAllFieldsShouldBeUsed() {
		return new Configuration<T>(type, prefabValues, true, allFieldsShouldBeUsedExceptions,
				cachedHashCodeInitializer, usingGetClass, warningsToSuppress);
	}

	public boolean isAllFieldsShouldBeUsed() {
		return allFieldsShouldBeUsed;
	}

	public Configuration<T> withAllFieldsShouldBeUsedExceptions(String[] value) {
		return new Configuration<T>(type, prefabValues, allFieldsShouldBeUsed, new HashSet<String>(Arrays.asList(value)),
				cachedHashCodeInitializer, usingGetClass, warningsToSuppress);
	}

	public Set<String> getAllFieldsShouldBeUsedExceptions() {
		return Collections.unmodifiableSet(allFieldsShouldBeUsedExceptions);
	}

	public Configuration<T> withCachedHashCodeInitializer(CachedHashCodeInitializer<T> value) {
		return new Configuration<T>(type, prefabValues, allFieldsShouldBeUsed, allFieldsShouldBeUsedExceptions, value,
				usingGetClass, warningsToSuppress);
	}
	
	public CachedHashCodeInitializer<T> getCachedHashCodeInitializer() {
		return cachedHashCodeInitializer;
	}
	
	public Configuration<T> withUsingGetClass() {
		return new Configuration<T>(type, prefabValues, allFieldsShouldBeUsed, allFieldsShouldBeUsedExceptions,
				cachedHashCodeInitializer, true, warningsToSuppress);
	}

	public boolean isUsingGetClass() {
		return usingGetClass;
	}

	public Configuration<T> withWarningsToSuppress(EnumSet<Warning> value) {
		return new Configuration<T>(type, prefabValues, allFieldsShouldBeUsed, allFieldsShouldBeUsedExceptions,
				cachedHashCodeInitializer, usingGetClass, value);
	}

	public EnumSet<Warning> getWarningsToSuppress() {
		return EnumSet.copyOf(warningsToSuppress);
	}

	public ClassAccessor<T> createClassAccessor() {
		return ClassAccessor.of(type, prefabValues, warningsToSuppress.contains(Warning.ANNOTATION));
	}
}
