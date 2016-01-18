/*
 * Copyright 2014-2015 Jan Ouwens
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
package nl.jqno.equalsverifier.integration.extended_contract;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.testhelpers.Java8IntegrationTestBase;
import org.junit.Test;

public class Java8GenericTypesTest extends Java8IntegrationTestBase {
    @Test
    public void succeed_whenEqualsLooksAtObservableListFieldsGenericContent() {
        if (!isJava8Available()) {
            return;
        }

        Class<?> type = compile(JAVAFX_OBSERVABLELIST_CONTAINER_CLASS_NAME, JAVAFX_OBSERVABLELIST_CONTAINER_CLASS);
        EqualsVerifier.forClass(type)
                .verify();
    }

    // CHECKSTYLE: ignore DeclarationOrder for 2 lines.
    private static final String JAVAFX_OBSERVABLELIST_CONTAINER_CLASS_NAME = "JavaFXObservableListContainer";
    private static final String JAVAFX_OBSERVABLELIST_CONTAINER_CLASS =
            "\nimport static nl.jqno.equalsverifier.testhelpers.Util.defaultHashCode;" +
            "\nimport nl.jqno.equalsverifier.testhelpers.types.Point;" +
            "\nimport javafx.collections.ObservableList;" +
            "\n" +
            "\npublic final class JavaFXObservableListContainer {" +
            "\n    private final ObservableList<Point> list;" +
            "\n    " +
            "\n    public JavaFXObservableListContainer(ObservableList<Point> list) {" +
            "\n        this.list = list;" +
            "\n    }" +
            "\n    " +
            "\n    @Override" +
            "\n    public boolean equals(Object obj) {" +
            "\n        if (!(obj instanceof JavaFXObservableListContainer)) {" +
            "\n            return false;" +
            "\n        }" +
            "\n        JavaFXObservableListContainer other = (JavaFXObservableListContainer)obj;" +
            "\n        if (list == null || other.list == null) {" +
            "\n            return list == other.list;" +
            "\n        }" +
            "\n        if (list.size() != other.list.size()) {" +
            "\n            return false;" +
            "\n        }" +
            "\n        for (int i = 0; i < list.size(); i++) {" +
            "\n            Point x = list.get(i);" +
            "\n            Point y = other.list.get(i);" +
            "\n            if (!x.equals(y)) {" +
            "\n                return false;" +
            "\n            }" +
            "\n        }" +
            "\n        return true;" +
            "\n    }" +
            "\n    " +
            "\n    @Override" +
            "\n    public int hashCode() {" +
            "\n        return defaultHashCode(this);" +
            "\n    }" +
            "\n}";
}
