package it.unicam.researchArea.email_generator.controller;

import it.unicam.researchArea.email_generator.configuration.AppConfig;
import it.unicam.researchArea.email_generator.model.Section;
import it.unicam.researchArea.email_generator.service.EmailGeneratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

@RestController
@RequestMapping("/api/email")
public class EmailController {

    @Autowired
    private EmailGeneratorService emailGeneratorService;

    @GetMapping("/introduction")
    public ResponseEntity<String> getIntroduction() {
        try {
            return ResponseEntity.ok(emailGeneratorService.getIntroduction());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/introduction")
    public ResponseEntity<String> updateIntroduction(@RequestBody String introduction) {
        return ResponseEntity.ok(emailGeneratorService.updateIntroduction(introduction));
    }

    @GetMapping("/conclusion")
    public ResponseEntity<String> getConclusion() {
        try {
            return ResponseEntity.ok(emailGeneratorService.getConclusion());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/conclusion")
    public ResponseEntity<String> updateConclusion(@RequestBody String conclusion) {
        return ResponseEntity.ok(emailGeneratorService.updateConclusion(conclusion));
    }

    @GetMapping("/footer")
    public ResponseEntity<String> getFooter() {
        try {
            return ResponseEntity.ok(emailGeneratorService.getFooter());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/footer")
    public ResponseEntity<String> updateFooter(@RequestBody String footer) {
        return ResponseEntity.ok(emailGeneratorService.updateFooter(footer));
    }

    @GetMapping("/contacts")
    public ResponseEntity<String> getContacts() {
        try {
            return ResponseEntity.ok(emailGeneratorService.getContacts());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/contacts")
    public ResponseEntity<String> updateContacts(@RequestBody String contacts) {
        return ResponseEntity.ok(emailGeneratorService.updateContacts(contacts));
    }

    @GetMapping("/sections")
    public ResponseEntity<List<Section>> getSections() {
        return ResponseEntity.ok(emailGeneratorService.getSections());
    }

    @PostMapping("/sections")
    public ResponseEntity<String> addSection(@RequestBody Section section) {
        return ResponseEntity.ok(emailGeneratorService.addSection(section));
    }

    @PostMapping("/clear-sections")
    public ResponseEntity<String> clearSections() {
        emailGeneratorService.accessSections().clear();
        return ResponseEntity.ok("All sections have been cleared");
    }

    @GetMapping("/generate-email")
    public String generateEmailTemplate() {
        try {
            String htmlTemplate = loadTemplate();
            String introduction = emailGeneratorService.getIntroductionForHtml();
            String sectionsHtml = generateSectionsHtml();
            String conclusion = emailGeneratorService.getConclusionForHtml();
            String contactInfo = emailGeneratorService.getContactsForHtml();
            String footer = emailGeneratorService.getFooterForHtml();

            return htmlTemplate
                    .replace("[[INTRODUCTION]]", introduction != null ? introduction : "")
                    .replace("[[SECTIONS]]", sectionsHtml)
                    .replace("[[CONCLUSION]]", conclusion != null ? conclusion : "")
                    .replace("[[CONTACT_INFO]]", contactInfo != null ? contactInfo : "")
                    .replace("[[FOOTER]]", footer != null ? footer : "");

        } catch (IOException e) {
            e.printStackTrace();
            return "Error loading template";
        }
    }

    private String generateSectionsHtml() {
        List<Section> sections = emailGeneratorService.getSections();
        sections.sort((s1, s2) -> Boolean.compare(s2.isUrgent(), s1.isUrgent()));

        StringBuilder finalHtml = new StringBuilder();

        appendSection(finalHtml, "BANDI", sections, "Bandi");
        appendSection(finalHtml, "NEWS", sections, "News");
        appendSection(finalHtml, "INIZIATIVE FORMATIVE", sections, "Iniziative Formative");

        return finalHtml.toString();
    }

    private void appendSection(StringBuilder htmlBuilder, String sectionTitle, List<Section> sections, String sectionName) {
        StringBuilder regularHtml = new StringBuilder();
        StringBuilder youngHtml = new StringBuilder();

        for (Section section : sections) {
            if (section.section().equals(sectionName)) {
                String formattedSection = getFormattedSection(section);
                if (section.isYoung()) {
                    youngHtml.append(formattedSection);
                } else {
                    regularHtml.append(formattedSection);
                }
            }
        }

        if (!regularHtml.isEmpty() || !youngHtml.isEmpty()) {
            htmlBuilder.append("<h2>").append(sectionTitle).append("</h2>").append(regularHtml);
            if (!youngHtml.isEmpty()) {
                htmlBuilder.append("<hr /><h3>🌱 Early Career Researchers</h3>").append(youngHtml);
            }
        }
    }

    private static String getFormattedSection(Section section) {
        String sectionHtml = "<div class='section {TYPE}'><h2>{EMOJI} {TITLE}</h2>{DEADLINE}{DESCRIPTION}{LINK}{DIRECT_APPLICATION_LINK}</div>";
        String typeClass = section.isUrgent() ? "urgent" : "normal";
        String deadline = section.deadline() != null && !section.deadline().isEmpty() ? "<p><strong>Scadenza:</strong> " + section.deadline() + "</p>" : "";
        String description = section.description() != null && !section.description().isEmpty() ? section.description() + "</p>" : "";
        String link = section.link() != null && !section.link().isEmpty() ?
                "<a href='" + section.link() + "' style='display: inline-block; background-color: #1a3b5c; color: white; padding: 8px 12px; text-decoration: none; border-radius: 5px; margin-right: 10px;'>Vai al bando</a>"
                : "";

        String directApplicationLink = section.directApplicationLink() != null && !section.directApplicationLink().isEmpty() ?
                "<a href='" + section.directApplicationLink() + "' style='display: inline-block; background-color: #ed1c24; color: white; padding: 8px 12px; text-decoration: none; border-radius: 5px;'>Candidati</a>"
                : "";
        return sectionHtml
                .replace("{TITLE}", section.title())
                .replace("{DEADLINE}", deadline)
                .replace("{DESCRIPTION}", description)
                .replace("{LINK}", link)
                .replace("{DIRECT_APPLICATION_LINK}", directApplicationLink)
                .replace("{EMOJI}", section.type().equals("Internazionale") ? "🇪🇺" : "🇮🇹")
                .replace("{TYPE}", typeClass);
    }

    private String loadTemplate() throws IOException {
        ClassPathResource resource = AppConfig.TEMPLATE_PATH;
        try (InputStream inputStream = resource.getInputStream()) {
            return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        }
    }
}