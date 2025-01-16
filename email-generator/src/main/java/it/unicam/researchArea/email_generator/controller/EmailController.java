package it.unicam.researchArea.email_generator.controller;

import it.unicam.researchArea.email_generator.model.Section;
import it.unicam.researchArea.email_generator.service.EmailGeneratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;

@RestController
@RequestMapping("/api/email")
public class EmailController {

    @Autowired
    private EmailGeneratorService emailGeneratorService;

    @GetMapping("/introduction")
    public String getIntroduction() {
        return emailGeneratorService.getIntroduction();
    }

    @PostMapping("/introduction")
    public String updateIntroduction(@RequestBody String introduction) {
        return emailGeneratorService.updateIntroduction(introduction);
    }

    @GetMapping("/conclusion")
    public String getConclusion() {
        return emailGeneratorService.getConclusion();
    }

    @PostMapping("/conclusion")
    public String updateConclusion(@RequestBody String conclusion) {
        return emailGeneratorService.updateConclusion(conclusion);
    }

    @GetMapping("/footer")
    public String getFooter() {
        return emailGeneratorService.getFooter();
    }

    @PostMapping("/footer")
    public String updateFooter(@RequestBody String footer) {
        return emailGeneratorService.updateFooter(footer);
    }

    @GetMapping("/contacts")
    public String getContacts() {
        return emailGeneratorService.getContacts();
    }

    @PostMapping("/contacts")
    public String updateContacts(@RequestBody String contacts) {
        return emailGeneratorService.updateContacts(contacts);
    }

    @GetMapping("/sections")
    public List<Section> getSections() {
        return emailGeneratorService.getSections();
    }

    private String loadTemplate(String fileName) throws IOException {
        ClassPathResource resource = new ClassPathResource("templates/" + fileName);
        return new String(Files.readAllBytes(resource.getFile().toPath()), StandardCharsets.UTF_8);
    }

    private String generateSectionsHtml() {
        List<Section> sections = emailGeneratorService.getSections();

        StringBuilder bandiHtml = new StringBuilder("<h2>BANDI</h2>");
        StringBuilder bandiGiovaniHtml = new StringBuilder();
        StringBuilder newsHtml = new StringBuilder("<h2>NEWS</h2>");
        StringBuilder newsGiovaniHtml = new StringBuilder();
        StringBuilder altroHtml = new StringBuilder("<h2>ALTRO</h2>");
        StringBuilder altroGiovaniHtml = new StringBuilder();

        for (Section section : sections) {
            String formattedSection = getFormattedSection(section);

            switch (section.getSection()) {
                case "Bandi":
                    if (section.isYoung()) {
                        bandiGiovaniHtml.append(formattedSection);
                    } else {
                        bandiHtml.append(formattedSection);
                    }
                    break;
                case "News":
                    if (section.isYoung()) {
                        newsGiovaniHtml.append(formattedSection);
                    } else {
                        newsHtml.append(formattedSection);
                    }
                    break;
                case "Altro":
                    if (section.isYoung()) {
                        altroGiovaniHtml.append(formattedSection);
                    } else {
                        altroHtml.append(formattedSection);
                    }
                    break;
            }
        }

        return bandiHtml.append(bandiGiovaniHtml).append(newsHtml).append(newsGiovaniHtml).append(altroHtml).append(altroGiovaniHtml).toString();
    }

    private static String getFormattedSection(Section section) {
        String sectionHtml = "<div class='section'><h2>{EMOJI} {TITLE}</h2><p><strong>Scadenza:</strong> {DEADLINE}</p><p>Descrizione: {DESCRIPTION}</p><a href='{LINK}' class='btn'>Vai al Bando</a></div>";
        String formattedSection = sectionHtml
                .replace("{TITLE}", section.getTitle())
                .replace("{DEADLINE}", section.getDeadline() != null ? section.getDeadline() : "N/A")
                .replace("{DESCRIPTION}", section.getDescription())
                .replace("{LINK}", section.getLink())
                .replace("{EMOJI}", section.getType().equals("Internazionale") ? "🇪🇺" : "🇮🇹");
        return formattedSection;
    }


    @PostMapping("/sections")
    public ResponseEntity<String> addSection(@RequestBody Section section) {
        String response = emailGeneratorService.addSection(section);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/generate-email")
    public String generateEmailTemplate() {
        try {
            String htmlTemplate = loadTemplate("emailTemplate.html");

            String introduction = emailGeneratorService.getIntroductionForHtml();
            String sectionsHtml = generateSectionsHtml();
            String conclusion = emailGeneratorService.getConclusionForHtml();
            String contactInfo = emailGeneratorService.getContactsForHtml();
            String footer = emailGeneratorService.getFooterForHtml();

            String html = htmlTemplate
                    .replace("[[INTRODUCTION]]", introduction != null ? introduction : "")
                    .replace("[[SECTIONS]]", sectionsHtml)
                    .replace("[[CONCLUSION]]", conclusion != null ? conclusion : "")
                    .replace("[[CONTACT_INFO]]", contactInfo != null ? contactInfo : "")
                    .replace("[[FOOTER]]", footer != null ? footer : "");

            return html;

        } catch (IOException e) {
            e.printStackTrace();
            return "Error loading template";
        }
    }

}

