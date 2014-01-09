/*
 * Copyright 2008 The Tongue-Tied Authors
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.tonguetied.datatransfer.importing;

import java.util.Map;

import org.tonguetied.datatransfer.common.FormatType;

/**
 * Factory to create an {@link Importer}.
 * 
 * @author bsion
 * 
 */
public class ImporterFactory {
    private Map<FormatType, Importer> importers;

    public void setImporters(Map<FormatType, Importer> importers) {
        this.importers = importers;
    }

    /**
     * Factory method to create the appropriate <code>Importer</code>.
     * 
     * @param formatType
     *            the input format of the data to process
     * @return The newly created <code>Importer</code>
     */
    public Importer getImporter(final FormatType formatType) {
        if (formatType == null) {
            throw new IllegalArgumentException("formatType cannot be null");
        }

        if (importers != null && importers.containsKey(formatType)) {
            return importers.get(formatType);
        }
        throw new IllegalStateException("No importer registered for format type: " + formatType);
    }
}
