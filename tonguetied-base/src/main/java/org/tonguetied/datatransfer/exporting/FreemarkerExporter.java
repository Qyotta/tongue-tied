package org.tonguetied.datatransfer.exporting;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.tonguetied.datatransfer.common.ExportParameters;
import org.tonguetied.keywordmanagement.Translation;

import freemarker.core.Environment;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;

/**
 * A freemarker template based implementation of {@link Exporter}.
 */
public class FreemarkerExporter implements Exporter {
    private static final Logger LOGGER = Logger.getLogger(FreemarkerExporter.class);

    private Configuration configuration;
    private Map<String, TemplateDirectiveModel> customDirectives;

    private String templateName;
    private String outputEncoding;

    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }

    public void setCustomDirectives(Map<String, TemplateDirectiveModel> customDirectives) {
        this.customDirectives = customDirectives;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public void setOutputEncoding(String outputEncoding) {
        this.outputEncoding = outputEncoding;
    }

    public void exportData(ExportParameters parameters, List<Translation> data) throws ExportException {
        String outputFileName = parameters.getOutputResourceName() != null ? parameters.getOutputResourceName() : templateName.replace(
                ".ftl", "");
        outputFileName += "." + parameters.getFormatType().getFileExtension();

        processTemplate(createModel(parameters, data), parameters.getOutputPath(), outputFileName);
    }

    protected Map<String, Object> createModel(ExportParameters parameters, List<Translation> data) {
        Map<String, Object> model = new HashMap<String, Object>();
        model.put("translations", data);
        model.put("currentDate", new Date());
        if (customDirectives != null) {
            model.putAll(customDirectives);
        }
        return model;
    }

    protected void processTemplate(Map<String, Object> data, String outputPath, String outputFileName) throws ExportException {
        Writer writer = null;

        try {
            File outputDir = new File(outputPath);
            if (!outputDir.exists()) {
                if (!outputDir.mkdirs()) {
                    throw new ExportException("Cannot create output directory: " + outputDir);
                } else {
                    LOGGER.info("Created output directory: " + outputDir.getAbsolutePath());
                }
            }

            File outputFile = new File(outputPath, outputFileName);
            FileOutputStream out = new FileOutputStream(outputFile);
            writer = new OutputStreamWriter(out, outputEncoding);

            Template template = configuration.getTemplate(templateName);
            Environment env = template.createProcessingEnvironment(data, writer);
            env.setOutputEncoding(outputEncoding);
            env.process();

            writer.flush();
        } catch (IOException e) {
            throw new ExportException(e);
        } catch (TemplateException e) {
            throw new ExportException(e);
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    LOGGER.warn("Cannot close writer", e);
                }
            }
        }
    }

}
