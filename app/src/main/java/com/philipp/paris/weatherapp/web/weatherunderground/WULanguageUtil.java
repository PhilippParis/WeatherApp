package com.philipp.paris.weatherapp.web.weatherunderground;

import java.util.Locale;

public class WULanguageUtil {
    public static String getLanguage(Locale locale) {
        switch(locale.getLanguage()) {
            case "sq": return "AL";
            case "be": return "BY";
            case "bg": return "BU";
            case "zh": return "CN";
            case "hr": return "CR";
            case "cs": return "CZ";
            case "da": return "DK";
            case "gl": return "GZ";
            case "de": return "DL";
            case "el": return "GR";
            case "ga": return "IR";
            case "ja": return "JP";
            case "ko": return "KR";
            case "nb": return "NO";
            case "pt": return "BR";
            case "es": return "SP";
            case "sw": return "SI";
            case "sv": return "SW";
            case "gsw": return "CH";
            case "uk": return "UA";
            case "vi": return "VU";
            default: return locale.getLanguage().toUpperCase();
        }
    }
}
