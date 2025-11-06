package view;

public class Menu {

    private String title;
    private MenuItem[] menuItems;

    public Menu(String title, MenuItem[] menuItems) {
        this.title = title;
        this.menuItems = menuItems;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public MenuItem[] getMenuItems() {
        return menuItems;
    }

    public void setMenuItems(MenuItem[] menuItems) {
        this.menuItems = menuItems;
    }
}
