package org.tonguetied.maven;

import java.util.Arrays;

/**
 * Main DTO class for parameters
 */
public class Resource {
    /**
     * The resource (output file) name
     * 
     * @parameter
     */
    private String resourceName;

    /**
     * The output directory
     * 
     * @parameter
     */
    private String outputDirectory = "target/classes";

    /**
     * The Bundles to export
     * 
     * @parameter
     */
    private String[] bundles;

    /**
     * The export file type
     * 
     * @parameter
     */
    private String formatType;

    /**
     * The languages to export
     * 
     * @parameter
     */
    private String[] languages = {"DEFAULT", "FR", "EN", "IT"};

    /**
     * The states of transalations to export
     * 
     * @parameter
     */
    private String[] states = {"VERIFIED"};

    /**
     * If missing translations have to be generated
     * 
     * @parameter
     */
    private boolean generatePlaceholders = true;

    /**
     * @return the resource name
     */
    public String getResourceName() {
        return resourceName;
    }

    /**
     * @param resourceName
     *            the resource name to set
     */
    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    /**
     * @return the ouput directory
     */
    public String getOutputDirectory() {
        return outputDirectory;
    }

    /**
     * @param outputDirectory
     *            the output directory to set
     */
    public void setOutputDirectory(String outputDirectory) {
        this.outputDirectory = outputDirectory;
    }

    /**
     * @return the list of bundles to export
     */
    public String[] getBundles() {
        return bundles;
    }

    /**
     * @param bundles
     *            the of bundles to export
     */
    public void setBundles(String[] bundles) {
        this.bundles = bundles;
    }

    /**
     * @return the format type
     */
    public String getFormatType() {
        return formatType;
    }

    /**
     * @param formatType
     *            the format type to set
     */
    public void setFormatType(String formatType) {
        this.formatType = formatType;
    }

    /**
     * @return the languages to process
     */
    public String[] getLanguages() {
        return languages;
    }

    /**
     * @param languages
     *            the languages to process
     */
    public void setLanguages(String[] languages) {
        this.languages = languages;
    }

    /**
     * @return the states to process
     */
    public String[] getStates() {
        return states;
    }

    /**
     * @param states
     *            the states to process
     */
    public void setStates(String[] states) {
        this.states = states;
    }

    /**
     * @return if missing placeholders have to be generated
     */
    public boolean isGeneratePlaceholders() {
        return generatePlaceholders;
    }

    /**
     * @param generatePlaceholders
     *            if the placeholders have to be generated
     */
    public void setGeneratePlaceholders(boolean generatePlaceholders) {
        this.generatePlaceholders = generatePlaceholders;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "Resource [bundles=" + Arrays.toString(bundles) + ", formatType=" + formatType + ", generatePlaceholders="
                + generatePlaceholders + ", languages=" + Arrays.toString(languages) + ", outputDirectory=" + outputDirectory
                + ", resourceName=" + resourceName + ", states=" + Arrays.toString(states) + "]";
    }
}
