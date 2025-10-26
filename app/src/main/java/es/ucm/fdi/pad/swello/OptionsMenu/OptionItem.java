package es.ucm.fdi.pad.swello.OptionsMenu;

public class OptionItem {
    private String title;
    private int iconRes;

    public OptionItem(String title, int iconRes) {
        this.title = title;
        this.iconRes = iconRes;
    }

    public String getTitle() { return title; }
    public int getIconRes() { return iconRes; }
}

