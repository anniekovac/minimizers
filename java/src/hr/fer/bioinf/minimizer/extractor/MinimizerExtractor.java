package hr.fer.bioinf.minimizer.extractor;

import hr.fer.bioinf.minimizer.minimizer.Minimizer;
import hr.fer.bioinf.minimizer.sequence.Sequence;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.ListIterator;

/**
 * Class used to extract Minimizers from a sequence.
 */
public class MinimizerExtractor {
    /**
     * Extracts Minimizers from a sequence using the alphabetic comparison for strings.
     * @param seq Sequence from which to extract minimizers.
     * @param w Window size for minimizer extraction.
     * @param k Size of minimizer.
     * @return List of extracted minimizers in the order of their appearance.
     */
    public static List<Minimizer> extract(Sequence seq, int w, int k) {
        return extract(seq, w, k, null);
    }

    /**
     * Extracts Minimizers from a sequence. Uses the given Comparator to establish an ordering of Strings.
     * @param seq Sequence from which to extract minimizers.
     * @param w Window size for minimizer extraction.
     * @param k Size of minimizer.
     * @param comp Comparator used to compare strings in the algorithm. If given null, method will use the default
     *             (alphabetic) comparison.
     * @return List of extracted minimizers in the order of their appearance.
     */
    public static List<Minimizer> extract(Sequence seq, int w, int k, Comparator<String> comp) {
        if (w < 0) {
            throw new IllegalArgumentException("w must be positive");
        }

        if (k < 0) {
            throw new IllegalArgumentException("k must be positive");
        }

        if (seq == null) {
            throw new IllegalArgumentException("seq cannot be null");
        }

        if (comp == null) {
            comp = Comparator.naturalOrder();
        }

        String seqString = seq.getString();
        if (seqString.length() < k) {
            throw new IllegalArgumentException(
                    "Can't extract (w,k)-minimizer if the given sequence is smaller than k");
        }

        List<Minimizer> minimizers = new ArrayList<>((int) (seqString.length() * 2 * 1.5 / (w + 1)));

        extractBeginningMinimizers(minimizers, seq, w, k, comp);

        String minimal = seqString.substring(0, k);
        int posMinimal = 0;

        // moving a window
        for (int windowStart = 0; windowStart < (seqString.length() + 2 - w - k); windowStart++) {
            if (posMinimal < windowStart) {
                minimal = null;
            }

            // iterating through kmers inside a window
            for (int j = Math.max(windowStart, posMinimal + 1); j < windowStart + w; j++) {
                String kmer = seqString.substring(j, j + k);

                if (minimal == null || comp.compare(kmer, minimal) < 0) {
                    minimal = kmer;
                    posMinimal = j;
                }
            }

            addIfLargerPosition(minimizers, new Minimizer(minimal, seq, posMinimal));
        }

        extractEndingMinimizers(minimizers, seq, w, k, comp);

        return minimizers;

    }

    /**
     * Adds a minimizer to a list of minimizers if the minimizer's position is larger than the last minimizer's position
     * or if the list is empty.
     * @param minimizers List of minimizers where the minimizer will be added
     * @param m Minimizer to add to the list
     * @return Reference to 'minimizers' list
     */
    private static List<Minimizer> addIfLargerPosition(List<Minimizer> minimizers, Minimizer m) {
        if (minimizers.isEmpty() || m.getPos() > minimizers.get(minimizers.size() - 1).getPos()) {
            minimizers.add(m);
        }

        return minimizers;
    }

    /**
     * Extracts end-minimizers from the beginning of the sequence and appends them to the end of the given minimizer
     * list. Uses the given Comparator to establish an ordering of Strings.
     * @param minimizers List of minimizers where the minimizers will be appended
     * @param seq Sequence from which the minimizers will be extracted
     * @param w Window size
     * @param k Minimizer length
     * @param comp Comparator used to establish an ordering of Strings.
     * @return List of minimizers with beginning end-minimizers appended
     */
    private static List<Minimizer> extractBeginningMinimizers(List<Minimizer> minimizers, Sequence seq, int w, int k,
                                                           Comparator<String> comp) {
        String seqString = seq.getString();

        String minimal = seqString.substring(0, k);
        int posMinimal = 0;
        addIfLargerPosition(minimizers, new Minimizer(minimal, seq, posMinimal));
        int seqStringLength = seqString.length();

        for (int windowEnd = 2; windowEnd < Math.min(w, seqStringLength - k + 2); windowEnd++) {

            // iterating through kmers inside a window
            for (int i = posMinimal + 1; i < windowEnd; i++) {
                String kmer = seqString.substring(i, i + k);

                if (comp.compare(kmer, minimal) < 0) {
                    minimal = kmer;
                    posMinimal = i;
                }
            }

            addIfLargerPosition(minimizers, new Minimizer(minimal, seq, posMinimal));
        }

        return minimizers;
    }

    /**
     * Extracts end-minimizers from the ending of the sequence and appends them to the end of the given minimizer
     * list. Uses the given Comparator to establish an ordering of Strings.
     * @param minimizers List of minimizers where the minimizers will be appended
     * @param seq Sequence from which the minimizers will be extracted
     * @param w Window size
     * @param k Minimizer length
     * @param comp Comparator used to establish an ordering of Strings.
     * @return List of minimizers with ending end-minimizers appended
     */
    private static List<Minimizer> extractEndingMinimizers(List<Minimizer> minimizers, Sequence seq, int w, int k,
                                                           Comparator<String> comp) {
        String seqString = seq.getString();
        int l = seqString.length();

        String minimal = null;
        int posMinimal = l - k;

        for (int windowStart = Math.max(l - k - w, 0) + 1; windowStart <= l - k; windowStart++) {
            if (posMinimal < windowStart) {
                minimal = null;
            }

            // iterating through kmers inside a window
            for (int i = windowStart; i <= l - k; i++) {
                String kmer = seqString.substring(i, i + k);

                if (minimal == null || comp.compare(kmer, minimal) < 0) {
                    minimal = kmer;
                    posMinimal = i;
                }
            }

            addIfLargerPosition(minimizers, new Minimizer(minimal, seq, posMinimal));
        }

        return minimizers;
    }
}
