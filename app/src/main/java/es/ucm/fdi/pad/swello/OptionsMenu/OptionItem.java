package es.ucm.fdi.pad.swello.OptionsMenu;

public class OptionItem {
    private String title;
    private int iconRes;

    private boolean isSubOption;

    public OptionItem(String title, int iconRes, boolean isSubOption) {
        this.title = title;
        this.iconRes = iconRes;
        this.isSubOption = isSubOption;
    }
    public boolean isSubOption() { return isSubOption; }
    public String getTitle() { return title; }
    public int getIconRes() { return iconRes; }
}

