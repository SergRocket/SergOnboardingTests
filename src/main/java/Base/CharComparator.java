package Base;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.google.common.collect.Comparators;
import com.google.common.collect.ComparisonChain;
import com.google.common.primitives.Chars;

public class CharComparator{
private enum CharComparators implements Comparator<Character> {

    UPPER_DIGIT_TO_LOWER {
        @Override
        int compare(char c1, char c2) {
            return ComparisonChain.start()
                    .compareTrueFirst(Character.isUpperCase(c1), Character.isUpperCase(c2))
                    .compareTrueFirst(Character.isDigit(c1), Character.isDigit(c2))
                    .compareTrueFirst(Character.isLowerCase(c1), Character.isLowerCase(c2))
                    .compare(c1, c2)
                    .result();
        }
    },

    LOWER_DIGIT_TO_UPPER{
        @Override
        int compare(char c1, char c2) {
            return ComparisonChain.start()
                    .compareTrueFirst(Character.isLowerCase(c1), Character.isLowerCase(c2))
                    .compareTrueFirst(Character.isDigit(c1), Character.isDigit(c2))
                    .compareTrueFirst(Character.isUpperCase(c1), Character.isUpperCase(c2))
                    .compare(c1, c2)
                    .result();
        }
    };

    @Override
    public int compare(Character c1, Character c2) {
        return compare(c1.charValue(), c2.charValue());
    }

    abstract int compare(char c1, char c2);
}

    private static String sortChars(String str, Comparator<Character> cmp) {
        List<Character> chars = Chars.asList(str.toCharArray());
        Collections.sort(chars, cmp);
        return new String(Chars.toArray(chars));
    }

        /*
        public static void main(String[] args) {
        String name = "h498y948759hrh98A722hjDF94yugerTEr892ur48y";
        System.out.println(sortChars(name, Comparators.UPPER_DIGIT_TO_LOWER));
        System.out.println(sortChars(name, Comparators.LOWER_DIGIT_TO_UPPER));
    }
         */
}

