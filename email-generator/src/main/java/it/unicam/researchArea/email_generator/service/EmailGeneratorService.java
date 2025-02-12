package it.unicam.researchArea.email_generator.service;

import it.unicam.researchArea.email_generator.configuration.AppConfig;
import it.unicam.researchArea.email_generator.model.Section;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class EmailGeneratorService {
    private final List<Section> sections = new CopyOnWriteArrayList<>(); // Thread-safe list

    public String getIntroduction() throws IOException {
        InputStream introductionStream = readFile(AppConfig.INTRODUCTION_PATH);
        return getString(introductionStream);
    }

    public String getIntroductionForHtml() throws IOException {
        return getIntroduction().replace("\n", "<br />");
    }

    public String updateIntroduction(String content) {
        return writeFile(AppConfig.INTRODUCTION_PATH, content);
    }

    public String getConclusion() throws IOException {
        InputStream conclusionStream = readFile(AppConfig.CONCLUSION_PATH);
        return getString(conclusionStream);
    }

    public String getConclusionForHtml() throws IOException {
        return getConclusion().replace("\n", "<br />");
    }

    public String updateConclusion(String content) {
        return writeFile(AppConfig.CONCLUSION_PATH, content);
    }

    public String getFooter() throws IOException {
        InputStream footerStream = readFile(AppConfig.FOOTER_PATH);
        return getString(footerStream);
    }

    public String getFooterForHtml() throws IOException {
        return getFooter().replace("\n", "<br />");
    }

    public String updateFooter(String content) {
        return writeFile(AppConfig.FOOTER_PATH, content);
    }

    public String getContacts() throws IOException {
        InputStream contactsStream = readFile(AppConfig.CONTACT_INFO_PATH);
        return getString(contactsStream);
    }

    public String getContactsForHtml() throws IOException {
        return getContacts().replace("\n", "<br />");
    }

    public String updateContacts(String content) {
        return writeFile(AppConfig.CONTACT_INFO_PATH, content);
    }

    public List<Section> getSections() {
        return new ArrayList<>(sections); // Return a copy of the list
    }

    public List<Section> accessSections() {
        return sections;
    }

    public String addSection(Section section) {
        sections.add(section);
        return "Section added";
    }

    private InputStream readFile(ClassPathResource path) {
        try {
            return path.getInputStream();
        } catch (Exception e) {
            throw new RuntimeException("Error reading file: " + path, e);
        }
    }

    private String getString(InputStream input) throws IOException {
        return new String(input.readAllBytes());
    }

    private String writeFile(ClassPathResource path, String content) {
        try {
            Files.writeString(Paths.get(path.getURI()), content);
            return "File updated";
        } catch (Exception e) {
            throw new RuntimeException("Error writing file: " + path, e);
        }
    }
}