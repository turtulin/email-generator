package it.unicam.researchArea.email_generator.service;

import it.unicam.researchArea.email_generator.configuration.AppConfig;
import it.unicam.researchArea.email_generator.model.Section;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class EmailGeneratorService {
    private final List<Section> sections = new CopyOnWriteArrayList<>(); // Thread-safe list

    public String getIntroduction() throws IOException {
        return Files.readString(Paths.get(AppConfig.INTRODUCTION_PATH.toUri()));
    }

    public String getIntroductionForHtml() throws IOException {
        return getIntroduction().replace("\n", "<br />");
    }

    public String updateIntroduction(String content) {
        return writeFile(AppConfig.INTRODUCTION_PATH, content);
    }

    public String getConclusion() throws IOException {
        return Files.readString(Paths.get(AppConfig.CONCLUSION_PATH.toUri()));
    }

    public String getConclusionForHtml() throws IOException {
        return getConclusion().replace("\n", "<br />");
    }

    public String updateConclusion(String content) {
        return writeFile(AppConfig.CONCLUSION_PATH, content);
    }

    public String getFooter() throws IOException {
        return Files.readString(Paths.get(AppConfig.FOOTER_PATH.toUri()));
    }

    public String getFooterForHtml() throws IOException {
        return getFooter().replace("\n", "<br />");
    }

    public String updateFooter(String content) {
        return writeFile(AppConfig.FOOTER_PATH, content);
    }

    public String getContacts() throws IOException {
        return Files.readString(Paths.get(AppConfig.CONTACT_INFO_PATH.toUri()));
    }

    public String getContactsForHtml() throws IOException {
        return getContacts().replace("\n", "<br />");
    }

    public String updateContacts(String content) {
        return writeFile(AppConfig.CONTACT_INFO_PATH, content);
    }

    public String getTemplate() throws IOException {
        return Files.readString(Paths.get(AppConfig.TEMPLATE_PATH.toUri()));
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

    private String writeFile(Path path, String content) {
        try {
            Files.writeString(path, content);
            return "File updated";
        } catch (IOException e) {
            throw new RuntimeException("Error writing file: " + path, e);
        }
    }
}