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
package org.netbeans.modules.web.jsfapi.api;

/**
 * Interface to provide additional informations about JSF Tag.
 *
 * @author marekfukala
 */
public interface TagFeature {

    /**
     * Interface for "iterable" JSF Tags. 
     * This tags typically has attribute for a collection of items and attribute to specify name of scoped variable.
     * @author marekfukala
     */
    public interface IterableTagPattern extends TagFeature {
        
        /**
         * The name of a collection of items that this tag iterates over.
         * @return instance of tag attribute for collection of items. Null when no attribute of this kind.
         */
        Attribute getItems();
        
        /**
         * Name of the exported scoped variable for the current item of the iteration.
         * @return instance of tag attribute for name of scoped variable. Null when no attribute of this kind.
         */
        Attribute getVariable();
        
    }

    /**
     * Interface for JSF Tags which import the constants of a type into the EL scope,
     * e.g. {@code <f:importConstants type="com.example.Foo" var="Foo"/>}.
     * The constants are then accessible in EL as {@code #{Foo.CONSTANT_NAME}}.
     *
     * @since 2.15
     */
    public interface ImportConstantsTagPattern extends TagFeature {

        /**
         * The attribute holding the fully qualified name of the type whose constants are imported.
         * @return instance of tag attribute for the imported type. Null when no attribute of this kind.
         */
        Attribute getType();

        /**
         * The attribute holding the name under which the constants are exposed.
         * When the attribute is not used in the page, the simple name of the type is used.
         * @return instance of tag attribute for the variable name. Null when no attribute of this kind.
         */
        Attribute getVariable();

        /**
         * Whether only the enum constants of the type are imported (e.g. PrimeFaces {@code p:importEnum}),
         * instead of all its public static final fields.
         * @return {@code true} if only enum constants are imported
         */
        default boolean isEnumOnly() {
            return false;
        }

        /**
         * The name of an additional key which is always exposed and holds all the enum constants
         * of the type (e.g. {@code ALL_VALUES} for PrimeFaces {@code p:importEnum}).
         * @return name of the key or null if there is no such key.
         */
        default String getAllValuesKey() {
            return null;
        }

        /**
         * The attribute holding the name of a custom additional key holding all the enum constants
         * of the type (e.g. {@code allSuffix} for PrimeFaces {@code p:importEnum}).
         * @return instance of tag attribute for the custom key name. Null when no attribute of this kind.
         */
        default Attribute getAllValuesKeyAttribute() {
            return null;
        }
    }
}
