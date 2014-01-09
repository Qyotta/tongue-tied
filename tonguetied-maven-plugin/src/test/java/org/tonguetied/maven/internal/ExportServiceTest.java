package org.tonguetied.maven.internal;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugin.logging.SystemStreamLog;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.context.SecurityContext;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.security.providers.UsernamePasswordAuthenticationToken;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;
import org.tonguetied.keywordmanagement.Bundle;
import org.tonguetied.keywordmanagement.BundleRepository;
import org.tonguetied.keywordmanagement.Country;
import org.tonguetied.keywordmanagement.CountryRepository;
import org.tonguetied.keywordmanagement.Keyword;
import org.tonguetied.keywordmanagement.KeywordRepository;
import org.tonguetied.keywordmanagement.Language;
import org.tonguetied.keywordmanagement.LanguageRepository;
import org.tonguetied.keywordmanagement.Translation;
import org.tonguetied.keywordmanagement.Country.CountryCode;
import org.tonguetied.keywordmanagement.Language.LanguageCode;
import org.tonguetied.keywordmanagement.Translation.TranslationState;
import org.tonguetied.maven.Resource;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/plugin-test-context.xml"})
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class})
public class ExportServiceTest {
    private static final String DE_CSV_FILE_NAME = "./Codex.csv";
    private static final String DE_FILE_NAME = "./Codex.properties";
    private static final String FR_FILE_NAME = "./Codex_fr.properties";
    private static final String IT_FILE_NAME = "./Codex_it.properties";
    private static final String EN_FILE_NAME = "./Codex_en.properties";

    private ExportService exportService;

    @Autowired
    private BundleRepository bundleRepository;

    @Autowired
    private CountryRepository countryRepository;

    @Autowired
    private LanguageRepository languageRepository;

    @Autowired
    private KeywordRepository keywordRepository;

    @Before
    public void init() throws Exception {
        Log logger = new SystemStreamLog();
        exportService = new ExportService(logger);
        exportService.initSpringContext("classpath:/plugin-test-context.xml", null, null, null);
        SecurityContext secureContext = SecurityContextHolder.getContext();
        secureContext.setAuthentication(new UsernamePasswordAuthenticationToken("principal", "creditential"));
        initBasicData();
    }

    @Test
    @Transactional
    public void exportOnlyDeTest() throws Exception {
        Resource currentResource = new Resource();
        currentResource.setBundles(new String[]{"codex-messages"});
        currentResource.setResourceName("Codex");
        currentResource.setFormatType("csv");
        currentResource.setOutputDirectory("./");
        currentResource.setGeneratePlaceholders(false);
        exportService.processExport(currentResource);

        File exportFileDe = new File("./Codex.csv");
        assertThat(exportFileDe.exists(), is(Boolean.TRUE));
        BufferedReader reader = new BufferedReader(new FileReader(exportFileDe));
        String readLine = reader.readLine();
        reader.close();
        assertThat(
                readLine,
                is(equalTo("keyword_only_German,\"keyword_only_German\",DEFAULT,Deutsch,DEFAULT,Switzerland,Codex Bundle,VERIFIED,\"keyword_only_German_de\"")));
    }

    @Test
    @Transactional
    public void exportGenerationTest() throws Exception {
        Resource currentResource = new Resource();
        currentResource.setBundles(new String[]{"codex-messages"});
        currentResource.setResourceName("Codex");
        currentResource.setFormatType("properties");
        currentResource.setOutputDirectory("./");
        currentResource.setGeneratePlaceholders(true);
        exportService.processExport(currentResource);

        File exportFileDe = new File("./Codex.properties");
        File exportFileFr = new File("./Codex_fr.properties");
        File exportFileIt = new File("./Codex_it.properties");
        File exportFileEn = new File("./Codex_en.properties");

        assertThat(exportFileDe.exists(), is(Boolean.TRUE));
        assertThat(exportFileFr.exists(), is(Boolean.TRUE));
        assertThat(exportFileIt.exists(), is(Boolean.TRUE));
        assertThat(exportFileEn.exists(), is(Boolean.TRUE));

        BufferedReader reader = new BufferedReader(new FileReader(exportFileDe));
        String readLine = reader.readLine();
        reader.close();
        assertThat(readLine, is(equalTo("keyword_only_German=keyword_only_German_de")));

        reader = new BufferedReader(new FileReader(exportFileFr));
        readLine = reader.readLine();
        reader.close();
        assertThat(readLine, is(equalTo("keyword_only_German=fr_keyword_only_German_de")));

        reader = new BufferedReader(new FileReader(exportFileIt));
        readLine = reader.readLine();
        reader.close();
        assertThat(readLine, is(equalTo("keyword_only_German=it_keyword_only_German_de")));

        reader = new BufferedReader(new FileReader(exportFileEn));
        readLine = reader.readLine();
        reader.close();
        assertThat(readLine, is(equalTo("keyword_only_German=en_keyword_only_German_de")));
    }

    @After
    public void deleteFiles() throws Exception {
        new File(DE_FILE_NAME).delete();
        new File(FR_FILE_NAME).delete();
        new File(IT_FILE_NAME).delete();
        new File(EN_FILE_NAME).delete();
        new File(DE_CSV_FILE_NAME).delete();
    }

    private void initBasicData() throws Exception {
        Bundle bundle = new Bundle();
        bundle.setDefault(true);
        bundle.setName("Codex Bundle");
        bundle.setResourceName("codex-messages");
        bundle.setDescription("Codex bundle description");
        bundle.setGlobal(false);
        bundleRepository.saveOrUpdate(bundle);

        Country country = new Country();
        country.setCode(CountryCode.DEFAULT);
        country.setName("Switzerland");
        countryRepository.saveOrUpdate(country);

        Language de = new Language();
        de.setCode(LanguageCode.DEFAULT);
        de.setName("Deutsch");
        languageRepository.saveOrUpdate(de);

        Language fr = new Language();
        fr.setCode(LanguageCode.fr);
        fr.setName("Français");
        languageRepository.saveOrUpdate(fr);

        Language it = new Language();
        it.setCode(LanguageCode.it);
        it.setName("Italiano");
        languageRepository.saveOrUpdate(it);

        Language en = new Language();
        en.setCode(LanguageCode.en);
        en.setName("English");
        languageRepository.saveOrUpdate(en);

        Keyword keyword1 = new Keyword();
        keyword1.setContext("keyword_only_German");
        keyword1.setKeyword("keyword_only_German");

        Translation translation_de = new Translation();
        translation_de.setBundle(bundle);
        translation_de.setCountry(country);
        translation_de.setKeyword(keyword1);
        translation_de.setValue("keyword_only_German_de");
        translation_de.setLanguage(de);
        translation_de.setState(TranslationState.VERIFIED);
        keyword1.addTranslation(translation_de);

        keywordRepository.saveOrUpdate(keyword1);
    }
}
