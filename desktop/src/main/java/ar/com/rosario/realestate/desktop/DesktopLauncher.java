package ar.com.rosario.realestate.desktop;

/**
 * Thin launcher outside the module path — required because JavaFX Application
 * subclass cannot be the main class when using the module system with unnamed modules.
 */
public class DesktopLauncher {

    public static void main(String[] args) {
        DesktopApp.main(args);
    }
}
