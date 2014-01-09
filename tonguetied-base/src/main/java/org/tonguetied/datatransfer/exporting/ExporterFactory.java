package org.tonguetied.datatransfer.exporting;

import java.util.Map;

import org.tonguetied.datatransfer.common.FormatType;

public class ExporterFactory {
    private Map<FormatType, Exporter> exporters;

    public void setExporters(Map<FormatType, Exporter> exporters) {
        this.exporters = exporters;
    }

    public Exporter getExporter(FormatType formatType) {
        if (formatType == null) {
            throw new IllegalArgumentException("formatType cannot be null");
        }

        if (exporters != null && exporters.containsKey(formatType)) {
            return exporters.get(formatType);
        }
        throw new IllegalStateException("No exporter registered for format type: " + formatType);
    }
}
