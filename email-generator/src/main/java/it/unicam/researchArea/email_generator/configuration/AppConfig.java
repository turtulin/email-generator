package it.unicam.researchArea.email_generator.configuration;

import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    public static final String INTRODUCTION_PATH = "src/main/resources/static/introduction.txt";
    public static final String CONCLUSION_PATH = "src/main/resources/static/conclusion.txt";
    public static final String CONTACT_INFO_PATH = "src/main/resources/static/contactInfo.txt";
    public static final String FOOTER_PATH = "src/main/resources/static/footer.txt";
}
