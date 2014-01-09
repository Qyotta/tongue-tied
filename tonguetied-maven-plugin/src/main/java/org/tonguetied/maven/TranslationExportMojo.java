package org.tonguetied.maven;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.tonguetied.datatransfer.exporting.ExportException;
import org.tonguetied.maven.internal.ExportService;

/**
 * Main Maven plugin class
 * 
 * @goal export
 */
public class TranslationExportMojo extends AbstractMojo {

    /**
     * The {@link ExportService}
     */
    private ExportService exportService;

    /**
     * The config paramters for the export
     * 
     * @parameter
     */
    private Resource[] resources;

    /**
     * The optional Jdbc URL
     * 
     * @parameter
     */
    private String jdbcURL;

    /**
     * The optional Jdbc User
     * 
     * @parameter
     */
    private String jdbcUser;

    /**
     * The optional Jdbc Password
     * 
     * @parameter
     */
    private String jdbcPassword;

    /**
     * Main constructor
     */
    public TranslationExportMojo() {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        exportService = new ExportService(getLog());
        logInputParameters();
        validateConfiguration();
        exportService.initSpringContext("classpath:/plugin-context.xml", jdbcURL, jdbcUser, jdbcPassword);

        getLog().debug("Exporting resources");
        for (Resource resource : resources) {
            try {
                exportResource(resource);
            } catch (ExportException e) {
                throw new MojoExecutionException("Error exporting resource", e);
            }
        }
    }

    /**
     * Log to DEBUG the input parameters
     */
    private void logInputParameters() {
        getLog().info("-----------------------------------------------------------------------");
        getLog().debug("======================================================================");
        getLog().debug("INPUT PARAMETERS :");
        for (Resource resource : resources) {
            getLog().debug(resource.toString());
        }
        getLog().debug("======================================================================");
    }

    /**
     * Validating input parameters
     * 
     * @throws MojoFailureException
     *             if the validation of parameters failed
     */
    private void validateConfiguration() throws MojoFailureException {
        getLog().debug("Validating plugin configuration");
        if (resources == null || resources.length == 0) {
            throw new MojoFailureException("No configured resources found");
        }

        if (jdbcURL != null || jdbcUser != null || jdbcPassword != null) {
            if (jdbcURL == null || jdbcUser == null || jdbcPassword == null) {
                throw new MojoFailureException("JDBC configuration parameters must all be set");
            }
        }

        for (Resource resource : resources) {
            if (resource.getResourceName() == null || resource.getResourceName().length() < 1) {
                throw new MojoFailureException("Resource has no name defined");
            }
            if (resource.getBundles() == null || resource.getBundles().length < 1) {
                throw new MojoFailureException("Resource with name " + resource.getResourceName() + " has no bundles defined");
            }
            if (resource.getFormatType() == null || resource.getFormatType().length() < 1) {
                throw new MojoFailureException("Resource with name " + resource.getResourceName() + " has no format type defined");
            }
        }
    }

    /**
     * Process the export for given resource.
     * 
     * @param resource
     *            the {@link Resource} which will be exported
     * @throws MojoExecutionException
     *             If an error occured during export
     */
    private void exportResource(Resource resource) throws MojoExecutionException {
        getLog().info("Exporting resource with name : " + resource.getResourceName());
        try {
            exportService.processExport(resource);
        } catch (ExportException e) {
            throw new MojoExecutionException("Error exporting resource with name : " + resource.getResourceName(), e);
        }
    }
}
