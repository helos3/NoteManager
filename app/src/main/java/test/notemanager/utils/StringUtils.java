package test.notemanager.utils;

public class StringUtils {

    public static String createBriefContent(String content) {
        String result;
        result = content.length() < 20 ? content : content.substring(0, 17) + "...";
        return result;
    }
}
