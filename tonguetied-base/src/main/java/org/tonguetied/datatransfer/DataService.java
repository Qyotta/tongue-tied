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
package org.tonguetied.datatransfer;

import org.tonguetied.datatransfer.common.ExportParameters;
import org.tonguetied.datatransfer.common.ImportParameters;
import org.tonguetied.datatransfer.exporting.ExportException;
import org.tonguetied.datatransfer.importing.ImportException;

/**
 * This interface defines the events for processing data import and export.
 * 
 * @author bsion
 * 
 */
public interface DataService {
    /**
     * Create a snapshot of data from persistence and transform it into the
     * desired format.
     * 
     * @param parameters
     *            the parameters used to filter and format the data
     * @throws ExportException
     *             if an error occurs during the export data process
     */
    void exportData(final ExportParameters parameters) throws ExportException;

    /**
     * Perform a bulk update/insert of data from an external source.
     * 
     * @param parameters
     *            the parameters used to import data into the system
     * @throws ImportException
     *             if an error occurs during the import data process
     */
    void importData(final ImportParameters parameters);
}
