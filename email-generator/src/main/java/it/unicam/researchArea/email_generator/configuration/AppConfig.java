package it.unicam.researchArea.email_generator.configuration;

import org.springframework.context.annotation.Configuration;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class AppConfig {
    public static final String DATA_DIRECTORY = "/opt/app/data";

    public static final Path INTRODUCTION_PATH = Paths.get(DATA_DIRECTORY, "introduction.txt");
    public static final Path CONCLUSION_PATH = Paths.get(DATA_DIRECTORY, "conclusion.txt");
    public static final Path FOOTER_PATH = Paths.get(DATA_DIRECTORY, "footer.txt");
    public static final Path CONTACT_INFO_PATH = Paths.get(DATA_DIRECTORY, "contacts.txt");
    public static final Path TEMPLATE_PATH = Paths.get(DATA_DIRECTORY, "template.html");
}
