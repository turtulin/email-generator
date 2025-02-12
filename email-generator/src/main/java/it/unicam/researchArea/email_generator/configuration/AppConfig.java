package it.unicam.researchArea.email_generator.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

@Configuration
public class AppConfig {
    public static final ClassPathResource TEMPLATE_PATH = new ClassPathResource("templates/emailTemplate.html");
    public static final ClassPathResource INTRODUCTION_PATH = new ClassPathResource("static/introduction.txt");
    public static final ClassPathResource CONCLUSION_PATH = new ClassPathResource("static/conclusion.txt");
    public static final ClassPathResource CONTACT_INFO_PATH = new ClassPathResource("static/contactInfo.txt");
    public static final ClassPathResource FOOTER_PATH = new ClassPathResource("static/footer.txt");
}
