package it.unicam.researchArea.email_generator.controller;

import it.unicam.researchArea.email_generator.model.Section;
import it.unicam.researchArea.email_generator.service.EmailGeneratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

@RestController
@RequestMapping("/api/email")
public class EmailController {

    @Autowired
    private EmailGeneratorService emailGeneratorService;

    @GetMapping("/introduction")
    public ResponseEntity<String> getIntroduction() {
        return ResponseEntity.ok(emailGeneratorService.getIntroduction());
    }

    @PostMapping("/introduction")
    public ResponseEntity<String> updateIntroduction(@RequestBody String introduction) {
        return ResponseEntity.ok(emailGeneratorService.updateIntroduction(introduction));
    }

    @GetMapping("/conclusion")
    public ResponseEntity<String> getConclusion() {
        return ResponseEntity.ok(emailGeneratorService.getConclusion());
    }

    @PostMapping("/conclusion")
    public ResponseEntity<String> updateConclusion(@RequestBody String conclusion) {
        return ResponseEntity.ok(emailGeneratorService.updateConclusion(conclusion));
    }

    @GetMapping("/footer")
    public ResponseEntity<String> getFooter() {
        return ResponseEntity.ok(emailGeneratorService.getFooter());
    }

    @PostMapping("/footer")
    public ResponseEntity<String> updateFooter(@RequestBody String footer) {
        return ResponseEntity.ok(emailGeneratorService.updateFooter(footer));
    }

    @GetMapping("/contacts")
    public ResponseEntity<String> getContacts() {
        return ResponseEntity.ok(emailGeneratorService.getContacts());
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
        emailGeneratorService.getSections().clear();
        return ResponseEntity.ok("All sections have been cleared");
    }

    @GetMapping("/generate-email")
    public ResponseEntity<String> generateEmailTemplate() {
        try {
            String htmlTemplate = loadTemplate();
            String introduction = emailGeneratorService.getIntroductionForHtml();
            String sectionsHtml = generateSectionsHtml();
            String conclusion = emailGeneratorService.getConclusionForHtml();
            String contactInfo = emailGeneratorService.getContactsForHtml();
            String footer = emailGeneratorService.getFooterForHtml();

            String emailHtml = htmlTemplate
                    .replace("[[INTRODUCTION]]", introduction != null ? introduction : "")
                    .replace("[[SECTIONS]]", sectionsHtml)
                    .replace("[[CONCLUSION]]", conclusion != null ? conclusion : "")
                    .replace("[[CONTACT_INFO]]", contactInfo != null ? contactInfo : "")
                    .replace("[[FOOTER]]", footer != null ? footer : "");

            return ResponseEntity.ok(emailHtml);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error loading template");
        }
    }

    private String loadTemplate() throws IOException {
        ClassPathResource resource = new ClassPathResource("templates/emailTemplate.html");
        return Files.readString(resource.getFile().toPath());
    }

    private String generateSectionsHtml() {
        List<Section> sections = emailGeneratorService.getSections();
        sections.sort((s1, s2) -> Boolean.compare(s2.isUrgent(), s1.isUrgent()));

        StringBuilder html = new StringBuilder();
        for (Section section : sections) {
            html.append(getFormattedSection(section));
        }
        return html.toString();
    }

    private static String getFormattedSection(Section section) {
        String sectionHtml = "<div class='section {CLASS}'><h2>{EMOJI} {TITLE}</h2>{DEADLINE}{DESCRIPTION}{LINK}{DIRECT_APPLICATION_LINK}</div>";
        String sectionClass = section.isUrgent() ? "urgent" : "normal";
        String deadline = section.deadline() != null && !section.deadline().isEmpty() ? "<p><strong>Scadenza:</strong> " + section.deadline() + "</p>" : "";
        String description = section.description() != null && !section.description().isEmpty() ? "<p>Descrizione: " + section.description() + "</p>" : "";
        String link = section.link() != null && !section.link().isEmpty() ? "<a href='" + section.link() + "' class='btn'>Vai al Bando</a>" : "";
        String directApplicationLink = section.directApplicationLink() != null && !section.directApplicationLink().isEmpty() ?
                "<br /><a href='" + section.directApplicationLink() + "' class='btn'>Candidati</a>" : "";
        return sectionHtml
                .replace("{TITLE}", section.title())
                .replace("{DEADLINE}", deadline)
                .replace("{DESCRIPTION}", description)
                .replace("{LINK}", link)
                .replace("{DIRECT_APPLICATION_LINK}", directApplicationLink)
                .replace("{EMOJI}", section.type().equals("Internazionale") ? "🇪🇺" : "🇮🇹")
                .replace("{CLASS}", sectionClass);
    }
}