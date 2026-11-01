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
package org.netbeans.modules.web.jsf20;

import java.util.Collection;
import java.util.Collections;
import org.netbeans.modules.web.jsfapi.api.Attribute;
import org.netbeans.modules.web.jsfapi.api.DefaultLibraryInfo;
import org.netbeans.modules.web.jsfapi.api.Library;
import org.netbeans.modules.web.jsfapi.api.LibraryInfo;
import org.netbeans.modules.web.jsfapi.api.Tag;
import org.netbeans.modules.web.jsfapi.api.TagFeature;
import org.netbeans.modules.web.jsfapi.spi.TagFeatureProvider;
import org.openide.util.lookup.ServiceProvider;

/**
 * {@link TagFeatureProvider} for the tags importing constants of a type into EL:
 * <ul>
 * <li>Jakarta Faces {@code <f:importConstants type="..." var="..."/>} (JSF 2.3+)</li>
 * <li>OmniFaces {@code <o:importConstants type="..." var="..."/>}</li>
 * </ul>
 */
@ServiceProvider(service = TagFeatureProvider.class)
public class ImportConstantsTagFeatureProvider implements TagFeatureProvider {

    static final String IMPORT_CONSTANTS = "importConstants"; //NOI18N
    static final String TYPE = "type"; //NOI18N
    static final String VAR = "var"; //NOI18N

    @Override
    public <T extends TagFeature> Collection<T> getFeatures(final Tag tag, Library library, Class<T> clazz) {
        if (clazz.equals(TagFeature.ImportConstantsTagPattern.class) && isImportConstantsTag(tag, library)) {
            return Collections.singleton(clazz.cast(new TagFeature.ImportConstantsTagPattern() {
                @Override
                public Attribute getType() {
                    return getAttribute(tag, TYPE);
                }

                @Override
                public Attribute getVariable() {
                    return getAttribute(tag, VAR);
                }
            }));
        }
        return Collections.emptyList();
    }

    private static boolean isImportConstantsTag(Tag tag, Library library) {
        if (!IMPORT_CONSTANTS.equals(tag.getName())) {
            return false;
        }
        String libraryNamespace = library.getNamespace();
        if (libraryNamespace == null) {
            return false;
        }
        LibraryInfo libraryInfo = DefaultLibraryInfo.forNamespace(libraryNamespace);
        return libraryInfo == DefaultLibraryInfo.JSF_CORE || libraryInfo == DefaultLibraryInfo.OMNIFACES;
    }

    private static Attribute getAttribute(Tag tag, String name) {
        Attribute attribute = tag.getAttribute(name);
        // the tag metadata may not describe the attribute, the name is what matters
        return attribute != null ? attribute : new Attribute.DefaultAttribute(name, null, false);
    }
}
