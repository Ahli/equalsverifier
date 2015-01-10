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
package nl.jqno.equalsverifier.util;

import java.lang.reflect.Field;

public class NonnullAnnotationChecker {
	public static boolean fieldIsNonnull(ClassAccessor<?> classAccessor, Field field) {
		if (classAccessor.fieldHasAnnotation(field, SupportedAnnotations.NONNULL)) {
			return true;
		}
		if (classAccessor.hasAnnotation(SupportedAnnotations.DEFAULT_ANNOTATION_NONNULL)) {
			return true;
		}
		return false;
	}
}
