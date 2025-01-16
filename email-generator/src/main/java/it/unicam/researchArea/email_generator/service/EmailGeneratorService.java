package it.unicam.researchArea.email_generator.service;

import it.unicam.researchArea.email_generator.model.Section;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Service
public class EmailGeneratorService {
    private final List<Section> sections = new ArrayList<>();
    private final String introPath = "src/main/resources/static/introduction.txt";
    private final String conclusionPath = "src/main/resources/static/conclusion.txt";
    private final String contactInfoPath = "src/main/resources/static/contactInfo.txt";
    private final String footerPath = "src/main/resources/static/footer.txt";

    public String getIntroduction() {
        return readFile(introPath);  // Return plain text
    }

    public String getIntroductionForHtml() {
        String intro = readFile(introPath);
        return intro.replace("\n", "<br />");
    }

    public String updateIntroduction(String content) { return writeFile(introPath, content); }

    public String getConclusion() {
        return readFile(conclusionPath);  // Return plain text
    }

    public String getConclusionForHtml() {
        String conclusion = readFile(conclusionPath);
        return conclusion.replace("\n", "<br />");
    }

    public String updateConclusion(String content) { return writeFile(conclusionPath, content); }

    public String getFooter() {
        return readFile(footerPath);  // Return plain text
    }

    public String getFooterForHtml() {
        String footer = readFile(footerPath);
        return footer.replace("\n", "<br />");
    }
    public String updateFooter(String footer) { return writeFile(footerPath, footer); }

    public String getContacts() {
        return readFile(contactInfoPath);
    }

    public String getContactsForHtml() {
        String contacts = readFile(contactInfoPath);
        return contacts.replace("\n", "<br />");
    }
    public String updateContacts(String contacts) { return writeFile(contactInfoPath, contacts); }

    public List<Section> getSections() { return sections; }

    public String addSection(Section section) {
        sections.add(section);
        return "Section added";
    }

    private String readFile(String path) {
        try { return Files.readString(Paths.get(path)); }
        catch (Exception e) { e.printStackTrace(); return ""; }
    }

    private String writeFile(String path, String content) {
        try { Files.writeString(Paths.get(path), content); return "File updated"; }
        catch (Exception e) { e.printStackTrace(); return "Error updating file"; }
    }
}
