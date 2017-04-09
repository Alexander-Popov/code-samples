package nstu.geo.core.infrastructure.service.string;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.*;

public class RangeFromStringConverter {
    public static Boolean isValidIntRangeInput(String text) {
        Pattern re_valid = Pattern.compile(
                "# Validate comma separated integers/integer ranges.\n" +
                        "^             # Anchor to start of string.         \n" +
                        "[0-9]+        # Integer of 1st value (required).   \n" +
                        "(?:           # Range for 1st value (optional).    \n" +
                        "  -           # Dash separates range integer.      \n" +
                        "  [0-9]+      # Range integer of 1st value.        \n" +
                        ")?            # Range for 1st value (optional).    \n" +
                        "(?:           # Zero or more additional values.    \n" +
                        "  ,           # Comma separates additional values. \n" +
                        "  [0-9]+      # Integer of extra value (required). \n" +
                        "  (?:         # Range for extra value (optional).  \n" +
                        "    -         # Dash separates range integer.      \n" +
                        "    [0-9]+    # Range integer of extra value.      \n" +
                        "  )?          # Range for extra value (optional).  \n" +
                        ")*            # Zero or more additional values.    \n" +
                        "$             # Anchor to end of string.           ",
                Pattern.COMMENTS);
        Matcher m = re_valid.matcher(text);
        return m.matches();
    }

    public static Set<Integer> getIntRanges(String text) {
        Pattern re_next_val = Pattern.compile(
                "# extract next integers/integer range value.    \n" +
                        "([0-9]+)      # $1: 1st integer (Base).         \n" +
                        "(?:           # Range for value (optional).     \n" +
                        "  -           # Dash separates range integer.   \n" +
                        "  ([0-9]+)    # $2: 2nd integer (Range)         \n" +
                        ")?            # Range for value (optional). \n" +
                        "(?:,|$)       # End on comma or string end.",
                Pattern.COMMENTS);
        Matcher m = re_next_val.matcher(text);
        String msg;

        Set<Integer> values = new HashSet<Integer>();
        while (m.find()) {
            Integer fistValueInPart = Integer.valueOf(m.group(1));
            values.add(fistValueInPart);
            if (m.group(2) != null) {
                Integer lastValueInPart = Integer.valueOf(m.group(2));
                for (int i = fistValueInPart; i <= lastValueInPart; i++) {
                    values.add(i);
                }
            }
        }
        return values;
    }
}