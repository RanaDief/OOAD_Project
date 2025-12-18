package ocrs.app;

import ocrs.data.DataStore;
import ocrs.ui.ConsoleUI;

public class Main {
    public static void main(String[] args) {
        DataStore store = DataStore.seedDemoData();
        ConsoleUI ui = new ConsoleUI(store);
        ui.run();
    }
}
