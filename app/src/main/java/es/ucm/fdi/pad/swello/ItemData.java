package es.ucm.fdi.pad.swello;

public class ItemData {
    private String title;
    private String description;

    public ItemData(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }
}
