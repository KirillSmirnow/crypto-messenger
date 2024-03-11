package cryptomessenger.desktop.utility;

import javafx.scene.media.AudioClip;

public class Player {

    public static void playAudio(String resourcePath) {
        new AudioClip(getResourceLocation(resourcePath)).play();
    }

    /**
     * Returns the location of the resource as 'jar:file:/path/to/jar!/path/in/jar'.
     * Note that {@link Class#getResource(String)} returns the location as 'jar:nested:/path/to/jar/!path/in/!/jar'.
     */
    private static String getResourceLocation(String resourcePath) {
        return Player.class.getResource(resourcePath).toString()
                .replaceFirst("nested", "file")
                .replaceFirst("/!", "!/")
                .replaceFirst("/!/", "/");
    }
}
