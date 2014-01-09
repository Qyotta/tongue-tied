package org.tonguetied.datatransfer.exporting;

import java.util.List;

import org.tonguetied.datatransfer.common.ExportParameters;
import org.tonguetied.keywordmanagement.Translation;

public interface Exporter {

    void exportData(ExportParameters parameters, List<Translation> data) throws ExportException;

}