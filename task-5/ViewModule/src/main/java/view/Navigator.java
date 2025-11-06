package view;

public class Navigator {

    private Menu currentMenu;

    public Navigator(Menu currentMenu) {
        this.currentMenu = currentMenu;
    }

    public Menu getCurrentMenu() {
        return currentMenu;
    }

    public void setCurrentMenu(Menu currentMenu) {
        this.currentMenu = currentMenu;
    }

    public void printMenu() {
        System.out.println("\n--- " + currentMenu.getTitle() + " ---");
        for (int i = 0; i < currentMenu.getMenuItems().length; i++) {
            System.out.println((i + 1) + ". " + currentMenu.getMenuItems()[i].getTitle());
        }
    }

    public void navigate(int index) {
        if (index < 1 || index > currentMenu.getMenuItems().length) {
            System.out.println("Invalid operation code");
            return;
        }

        MenuItem item = currentMenu.getMenuItems()[index - 1];
        if (item.getAction() != null) {
            item.getAction().execute();
        }
        if (item.getNextMenu() != null) {
            currentMenu = item.getNextMenu();
        }
    }
}
