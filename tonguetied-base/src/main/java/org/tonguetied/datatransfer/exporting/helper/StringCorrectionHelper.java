package org.tonguetied.datatransfer.exporting.helper;

public class StringCorrectionHelper {

    public static String correctString(String inputString) {
        String result = correctDoublePercent(inputString);
        return correctParameters(result);
    }

    public static String correctStringNLS(String inputString) {
        String result = correctDoublePercent(inputString);
        result = correctParameters(result);
        return correctHTMLTags(result);
    }

    private static String correctDoublePercent(String inputString) {
        return inputString.replaceAll("%%", "%");
    }

    private static String correctParameters(String inputString) {
        String result = inputString.replaceAll("%1", "{0}");
        result = result.replaceAll("%2", "{1}");
        result = result.replaceAll("%3", "{2}");
        result = result.replaceAll("%4", "{3}");
        result = result.replaceAll("%5", "{4}");
        result = result.replaceAll("%6", "{5}");
        result = result.replaceAll("%7", "{6}");
        result = result.replaceAll("%8", "{7}");
        result = result.replaceAll("%9", "{8}");
        return result;
    }

    private static String correctHTMLTags(String inputString) {
        String result = inputString;

        // if (result.contains("</") || result.contains("<br")) {
        // result = "${HTML}" + result;
        // }
        result = result.replaceAll("<br>", "<br />");
        result = result.replaceAll("<br/>", "<br />");
        result = result.replaceAll(new String(new char[]{'\r', '\n'}), "<br />");
        result = result.replaceAll(Character.toString('\n'), "<br />");
        return result;
    }
}
