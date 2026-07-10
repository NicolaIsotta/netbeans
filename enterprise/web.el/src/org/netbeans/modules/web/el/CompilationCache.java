/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.netbeans.modules.web.el;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author marekfukala
 */
public class CompilationCache {

    private static final Object NULL_RESULT = new Object();

    private final Map<Key, Object> map = new ConcurrentHashMap<>();

    public Object getOrCache(Key key, ValueProvider<?> valueProvider) {
        // computeIfAbsent cannot be used here, it may cause a "java.lang.IllegalStateException: Recursive update"
        Object cached = map.get(key);
        if (cached != null) {
            return cached == NULL_RESULT ? null : cached;
        }

        Object value = valueProvider.get();
        Object toStore = value == null ? NULL_RESULT : value;
        Object existing = map.putIfAbsent(key, toStore);
        if (existing != null) {
            return existing == NULL_RESULT ? null : existing;
        }
        return value;
    }

    public static Key createKey(Object... items) {
        return new Key(items);
    }

    public static interface ValueProvider<T> {
        public T get();
    }

    public static class Key {

        private final Object[] keys;

        private Key(Object... keys) {
            this.keys = Arrays.copyOf(keys, keys.length);
        }

        @Override
        public boolean equals(Object obj) {
            return this == obj || (obj instanceof Key other && Arrays.equals(keys, other.keys));
        }

        @Override
        public int hashCode() {
            return Arrays.hashCode(keys);
        }

        @Override
        public String toString() {
            return "Key{keys=" + Arrays.toString(keys) + '}';
        }
    }
}
