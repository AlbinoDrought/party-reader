package party.minge.reddit.menu;

public interface MenuItem {
    String getText();
    String getIcon();
    int getId();

    void onClick();
}
