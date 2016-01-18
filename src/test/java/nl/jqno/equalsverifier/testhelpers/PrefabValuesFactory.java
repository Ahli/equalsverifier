/*
 * Copyright 2014 Jan Ouwens
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

import nl.jqno.equalsverifier.internal.StaticFieldValueStash;
import nl.jqno.equalsverifier.internal.prefabvalues.SimpleFactory;

public final class PrefabValuesFactory {
    private PrefabValuesFactory() {}

    public static nl.jqno.equalsverifier.internal.PrefabValues withPrimitives(StaticFieldValueStash stash) {
        nl.jqno.equalsverifier.internal.PrefabValues result = new nl.jqno.equalsverifier.internal.PrefabValues(stash);
        result.put(boolean.class, true, false);
        result.put(byte.class, (byte)1, (byte)2);
        result.put(char.class, 'a', 'b');
        result.put(double.class, 0.5D, 1.0D);
        result.put(float.class, 0.5F, 1.0F);
        result.put(int.class, 1, 2);
        result.put(long.class, 1L, 2L);
        result.put(short.class, (short)1, (short)2);
        return result;
    }

    public static nl.jqno.equalsverifier.internal.prefabvalues.PrefabValues withPrimitiveFactories(StaticFieldValueStash stash) {
        nl.jqno.equalsverifier.internal.prefabvalues.PrefabValues result = new nl.jqno.equalsverifier.internal.prefabvalues.PrefabValues(stash);
        result.addFactory(boolean.class, new SimpleFactory<>(true, false));
        result.addFactory(byte.class, new SimpleFactory<>((byte)1, (byte)2));
        result.addFactory(char.class, new SimpleFactory<>('a', 'b'));
        result.addFactory(double.class, new SimpleFactory<>(0.5D, 1.0D));
        result.addFactory(float.class, new SimpleFactory<>(0.5F, 1.0F));
        result.addFactory(int.class, new SimpleFactory<>(1, 2));
        result.addFactory(long.class, new SimpleFactory<>(1L, 2L));
        result.addFactory(short.class, new SimpleFactory<>((short)1, (short)2));
        return result;
    }
}
