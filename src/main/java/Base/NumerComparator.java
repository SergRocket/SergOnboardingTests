package Base;

import java.util.Comparator;
import com.google.common.collect.ComparisonChain.*;
import com.google.common.primitives.Chars;

public class NumerComparator implements Comparator<String> {

    private final String getChunk(String s, int slength, int marker) {
        StringBuilder chunk = new StringBuilder();
        char c = s.charAt(marker);
        chunk.append(c);
        marker++;
        if (isDigiT(c)) {
            while (marker < slength) {
                c = s.charAt(marker);
                if (!isDigiT(c))
                    break;
                chunk.append(c);
                marker++;
            }
        } else {
            while (marker < slength) {
                c = s.charAt(marker);
                if (isDigiT(c))
                    break;
                chunk.append(c);
                marker++;
            }
        }
        return chunk.toString();
    }

    private final boolean isDigiT(char c){
        return Character.isDigit(c);
    }

    @Override
    public int compare(String o1, String o2) {
        int thisMarker = 0;
        int thatMarker = 0;
        int o1Length = o1.length();
        int o2Length = o2.length();

        while (thisMarker < o1Length && thatMarker < o2Length) {
            String thisChunk = getChunk(o1, o1Length, thisMarker);
            thisMarker += thisChunk.length();

            String thatChunk = getChunk(o2, o2Length, thatMarker);
            thatMarker += thatChunk.length();

            int result = 0;
            if (isDigiT(thisChunk.charAt(0)) && isDigiT(thatChunk.charAt(0))) {
            int thisChunkLength = thisChunk.length();
            result = thisChunkLength - thatChunk.length();
            if (result == 0) {
                for (int i = 0; i < thisChunkLength; i++) {
                    result = thisChunk.charAt(i) - thatChunk.charAt(i);
                    if (result != 0) {
                        return result;
                    }
                }
            }
            } else {
            result = thisChunk.compareTo(thatChunk);
        }

        if (result != 0)
            return result;
    }

    return o1Length - o2Length;
  }

}
