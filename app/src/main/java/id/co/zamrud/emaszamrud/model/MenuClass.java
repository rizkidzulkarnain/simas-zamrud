package id.co.zamrud.emaszamrud.model;

public class MenuClass {
    private String title;
    private int iconMenu;
    private int id;

    public MenuClass() {
    }

    public MenuClass(int id, String title, int iconMenu) {
        this.id = id;
        this.title = title;
        this.iconMenu = iconMenu;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getIconMenu() {
        return iconMenu;
    }

    public void setIconMenu(int iconMenu) {
        this.iconMenu = iconMenu;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
