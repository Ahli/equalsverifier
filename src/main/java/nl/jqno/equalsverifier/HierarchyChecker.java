/*
 * Copyright 2009-2011, 2013, 2015 Jan Ouwens
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

import static nl.jqno.equalsverifier.internal.Assert.assertEquals;
import static nl.jqno.equalsverifier.internal.Assert.assertFalse;
import static nl.jqno.equalsverifier.internal.Assert.assertTrue;
import static nl.jqno.equalsverifier.internal.Assert.fail;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import nl.jqno.equalsverifier.internal.ClassAccessor;
import nl.jqno.equalsverifier.internal.Formatter;
import nl.jqno.equalsverifier.internal.ObjectAccessor;

class HierarchyChecker<T> implements Checker {
    private final Configuration<T> config;
    private final Class<T> type;
    private final ClassAccessor<T> classAccessor;
    private final Class<? extends T> redefinedSubclass;
    private final boolean typeIsFinal;
    private final CachedHashCodeInitializer<T> cachedHashCodeInitializer;

    public HierarchyChecker(Configuration<T> config) {
        this.config = config;

        if (config.getWarningsToSuppress().contains(Warning.STRICT_INHERITANCE) &&
                config.getRedefinedSubclass() != null) {
            fail(Formatter.of("withRedefinedSubclass and weakInheritanceCheck are mutually exclusive."));
        }

        this.type = config.getType();
        this.classAccessor = config.createClassAccessor();
        this.redefinedSubclass = config.getRedefinedSubclass();
        this.typeIsFinal = Modifier.isFinal(type.getModifiers());
        this.cachedHashCodeInitializer = config.getCachedHashCodeInitializer();
    }

    @Override
    public void check() {
        checkSuperclass();
        checkSubclass();

        checkRedefinedSubclass();
        if (!config.getWarningsToSuppress().contains(Warning.STRICT_INHERITANCE)) {
            checkFinalEqualsMethod();
        }
    }

    private void checkSuperclass() {
        ClassAccessor<? super T> superAccessor = classAccessor.getSuperAccessor();
        if (superAccessor.isEqualsInheritedFromObject()) {
            return;
        }

        if (config.hasRedefinedSuperclass() || config.isUsingGetClass()) {
            T reference = classAccessor.getRedObject();
            Object equalSuper = getEqualSuper(reference);

            assertFalse(Formatter.of("Redefined superclass:\n  %%\nshould not equal superclass instance\n  %%\nbut it does.", reference, equalSuper),
                    reference.equals(equalSuper) || equalSuper.equals(reference));
        }
        else {
            checkSuperProperties(classAccessor.getRedAccessor());
            checkSuperProperties(classAccessor.getDefaultValuesAccessor());
        }
    }

    private void checkSuperProperties(ObjectAccessor<T> referenceAccessor) {
        T reference = referenceAccessor.get();
        Object equalSuper = getEqualSuper(reference);

        T shallow = referenceAccessor.copy();
        ObjectAccessor.of(shallow).shallowScramble(classAccessor.getPrefabValues());

        assertTrue(Formatter.of("Symmetry:\n  %%\ndoes not equal superclass instance\n  %%", reference, equalSuper),
                reference.equals(equalSuper) && equalSuper.equals(reference));

        assertTrue(Formatter.of("Transitivity:\n  %%\nand\n  %%\nboth equal superclass instance\n  %%\nwhich implies they equal each other.", reference, shallow, equalSuper),
                reference.equals(shallow) || reference.equals(equalSuper) != equalSuper.equals(shallow));

        int referenceHashCode = cachedHashCodeInitializer.getInitializedHashCode(reference);
        int equalSuperHashCode = cachedHashCodeInitializer.getInitializedHashCode(equalSuper);
        assertTrue(Formatter.of("Superclass: hashCode for\n  %% (%%)\nshould be equal to hashCode for superclass instance\n  %% (%%)", reference, referenceHashCode, equalSuper, equalSuperHashCode),
                referenceHashCode == equalSuperHashCode);
    }

    private Object getEqualSuper(T reference) {
        return ObjectAccessor.of(reference, type.getSuperclass()).copy();
    }

    private void checkSubclass() {
        if (typeIsFinal) {
            return;
        }

        ObjectAccessor<T> referenceAccessor = classAccessor.getRedAccessor();
        T reference = referenceAccessor.get();
        T equalSub = referenceAccessor.copyIntoAnonymousSubclass();

        if (config.isUsingGetClass()) {
            assertFalse(Formatter.of("Subclass: object is equal to an instance of a trivial subclass with equal fields:\n  %%\nThis should not happen when using getClass().", reference),
                    reference.equals(equalSub));
        }
        else {
            assertTrue(Formatter.of("Subclass: object is not equal to an instance of a trivial subclass with equal fields:\n  %%\nConsider making the class final.", reference),
                    reference.equals(equalSub));
        }
    }

    private void checkRedefinedSubclass() {
        if (typeIsFinal || redefinedSubclass == null) {
            return;
        }

        if (methodIsFinal("equals", Object.class)) {
            fail(Formatter.of("Subclass: %% has a final equals method.\nNo need to supply a redefined subclass.", type.getSimpleName()));
        }

        ObjectAccessor<T> referenceAccessor = classAccessor.getRedAccessor();
        T reference = referenceAccessor.get();
        T redefinedSub = referenceAccessor.copyIntoSubclass(redefinedSubclass);
        assertFalse(Formatter.of("Subclass:\n  %%\nequals subclass instance\n  %%", reference, redefinedSub),
                reference.equals(redefinedSub));
    }

    private void checkFinalEqualsMethod() {
        if (typeIsFinal || redefinedSubclass != null) {
            return;
        }

        boolean equalsIsFinal = methodIsFinal("equals", Object.class);
        boolean hashCodeIsFinal = methodIsFinal("hashCode");

        if (config.isUsingGetClass()) {
            assertEquals(Formatter.of("Finality: equals and hashCode must both be final or both be non-final."),
                    equalsIsFinal, hashCodeIsFinal);
        }
        else {
            assertTrue(Formatter.of("Subclass: equals is not final.\nSupply an instance of a redefined subclass using withRedefinedSubclass if equals cannot be final."),
                    equalsIsFinal);
            assertTrue(Formatter.of("Subclass: hashCode is not final.\nSupply an instance of a redefined subclass using withRedefinedSubclass if hashCode cannot be final."),
                    hashCodeIsFinal);
        }
    }

    private boolean methodIsFinal(String methodName, Class<?>... parameterTypes) {
        try {
            Method method = type.getMethod(methodName, parameterTypes);
            return Modifier.isFinal(method.getModifiers());
        }
        catch (SecurityException e) {
            throw new AssertionError("Security error: cannot access equals method for class " + type);
        }
        catch (NoSuchMethodException e) {
            throw new AssertionError("Impossible: class " + type + " has no equals method.");
        }

    }
}
