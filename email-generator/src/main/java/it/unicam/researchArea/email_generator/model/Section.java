package it.unicam.researchArea.email_generator.model;

public class Section {
    private String section;
    private String title;
    private String deadline;
    private String description;
    private String link;
    private String type;
    private boolean isYoung;

    public String getSection() { return section; }

    public void setSection(String section) { this.section = section; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getLink() { return link; }
    public void setLink(String link) { this.link = link; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public boolean isYoung() { return isYoung; }
    public void setIsYoung(boolean isYoung) { this.isYoung = isYoung; }

}

