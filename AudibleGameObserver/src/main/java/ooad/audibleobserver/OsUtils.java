package ooad.audibleobserver;

public class OsUtils {

    public static boolean isMac() {
        String osName = System.getProperty("os.name");
        return osName != null && osName.toLowerCase().contains("mac");
    }
}
