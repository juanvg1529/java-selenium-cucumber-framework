package com.jdvg.automation.pages;

import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** Parses amounts such as "$29.99" or "Item total: $45.98". BigDecimal avoids floating point errors in money. */
final class Prices {

    private static final Pattern AMOUNT = Pattern.compile("\\$(\\d+\\.\\d{2})");

    private Prices() {
    }

    static BigDecimal parse(String text) {
        Matcher matcher = AMOUNT.matcher(text);
        if (!matcher.find()) throw new IllegalArgumentException("No amount found in: " + text);
        return new BigDecimal(matcher.group(1));
    }
}
