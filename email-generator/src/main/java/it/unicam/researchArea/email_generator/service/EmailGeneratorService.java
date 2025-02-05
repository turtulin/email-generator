package it.unicam.researchArea.email_generator.service;

import it.unicam.researchArea.email_generator.configuration.AppConfig;
import it.unicam.researchArea.email_generator.model.Section;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Service
public class EmailGeneratorService {
    private final List<Section> sections = new ArrayList<>();

    public String getIntroduction() {
        return readFile(AppConfig.INTRODUCTION_PATH);
    }

    public String getIntroductionForHtml() {
        return getIntroduction().replace("\n", "<br />");
    }

    public String updateIntroduction(String content) {
        return writeFile(AppConfig.INTRODUCTION_PATH, content);
    }

    public String getConclusion() {
        return readFile(AppConfig.CONCLUSION_PATH);
    }

    public String getConclusionForHtml() {
        return getConclusion().replace("\n", "<br />");
    }

    public String updateConclusion(String content) {
        return writeFile(AppConfig.CONCLUSION_PATH, content);
    }

    public String getFooter() {
        return readFile(AppConfig.FOOTER_PATH);
    }

    public String getFooterForHtml() {
        return getFooter().replace("\n", "<br />");
    }

    public String updateFooter(String content) {
        return writeFile(AppConfig.FOOTER_PATH, content);
    }

    public String getContacts() {
        return readFile(AppConfig.CONTACT_INFO_PATH);
    }

    public String getContactsForHtml() {
        return getContacts().replace("\n", "<br />");
    }

    public String updateContacts(String content) {
        return writeFile(AppConfig.CONTACT_INFO_PATH, content);
    }

    public List<Section> getSections() {
        return sections;
    }

    public String addSection(Section section) {
        sections.add(section);
        return "Section added";
    }

    private String readFile(String path) {
        try {
            return Files.readString(Paths.get(path));
        } catch (Exception e) {
            throw new RuntimeException("Error reading file: " + path, e);
        }
    }

    private String writeFile(String path, String content) {
        try {
            Files.writeString(Paths.get(path), content);
            return "File updated";
        } catch (Exception e) {
            throw new RuntimeException("Error writing to file: " + path, e);
        }
    }
}