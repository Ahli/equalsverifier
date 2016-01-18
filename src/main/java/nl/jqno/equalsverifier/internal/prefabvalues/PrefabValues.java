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
package nl.jqno.equalsverifier.internal.prefabvalues;

public class PrefabValues {
    private final Cache cache = new Cache();
    private final FactoryCache factoryCache = new FactoryCache();

    public <T> void addFactory(Class<T> type, PrefabValueFactory<T> factory) {
        factoryCache.put(type, factory);
    }

    public <T> T giveRed(TypeTag tag) {
        return this.<T>giveTuple(tag).getRed();
    }

    public <T> T giveBlack(TypeTag tag) {
        return this.<T>giveTuple(tag).getBlack();
    }

    private <T> Tuple<T> giveTuple(TypeTag tag) {
        Class<T> type = tag.getType();

        if (!cache.contains(tag)) {
            if (factoryCache.contains(type)) {
                PrefabValueFactory<T> factory = factoryCache.get(type);
                Tuple<T> tuple = factory.createValues(tag, this);
                cache.put(tag, tuple.getRed(), tuple.getBlack());
            }
            else {
                throw new IllegalStateException();
            }
        }


        return cache.getTuple(tag);
    }
}
