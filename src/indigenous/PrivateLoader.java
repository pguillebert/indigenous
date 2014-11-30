package indigenous;

/**
 * This class is reserved for internal use.
 * Copyright Gary Verhaegen.
 */
public final class PrivateLoader {
    public static void load(String path) {
        System.load(path);
    }
    private PrivateLoader() {};
}
