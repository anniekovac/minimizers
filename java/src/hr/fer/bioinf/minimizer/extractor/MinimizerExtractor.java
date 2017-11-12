package hr.fer.bioinf.minimizer.extractor;

import hr.fer.bioinf.minimizer.minimizer.Minimizer;
import hr.fer.bioinf.minimizer.sequence.Sequence;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.ListIterator;

public class MinimizerExtractor {
    public static List<Minimizer> extract(Sequence seq, int w, int k) {
        return extract(seq, w, k, null);
    }

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
            for (int j = Math.max(windowStart, posMinimal + 1); j < windowStart + k; j++) {
                String kmer = seqString.substring(j, j + k);

                if (minimal == null || comp.compare(kmer, minimal) < 0) {
                    minimal = kmer;
                    posMinimal = j;
                }
            }

            addIfNotEqualToLast(minimizers, new Minimizer(minimal, seq, posMinimal));
        }

        extractEndingMinimizers(minimizers, seq, w, k, comp);

        return minimizers;

    }

    private static List<Minimizer> addIfNotEqualToLast(List<Minimizer> minimizers, Minimizer m) {
        if (minimizers.isEmpty() || m.getPos() != minimizers.get(minimizers.size() - 1).getPos()) {
            minimizers.add(m);
        }

        return minimizers;
    }

    private static List<Minimizer> extractBeginningMinimizers(List<Minimizer> minimizers, Sequence seq, int w, int k,
                                                           Comparator<String> comp) {
        String seqString = seq.getString();

        String minimal = seqString.substring(0, k);
        int posMinimal = 0;
        addIfNotEqualToLast(minimizers, new Minimizer(minimal, seq, posMinimal));

        for (int windowEnd = 2; windowEnd < w; windowEnd++) {

            // iterating through kmers inside a window
            for (int i = posMinimal + 1; i < windowEnd; i++) {
                String kmer = seqString.substring(i, i + k);

                if (comp.compare(kmer, minimal) < 0) {
                    minimal = kmer;
                    posMinimal = i;
                }
            }

            addIfNotEqualToLast(minimizers, new Minimizer(minimal, seq, posMinimal));
        }

        return minimizers;
    }

    private static List<Minimizer> extractEndingMinimizers(List<Minimizer> minimizers, Sequence seq, int w, int k,
                                                           Comparator<String> comp) {
        String seqString = seq.getString();
        int l = seqString.length();
        List<Minimizer> reverseEndMinimizers = new ArrayList<>(w);

        String minimal = seqString.substring(l - k);
        int posMinimal = l - k;
        addIfNotEqualToLast(reverseEndMinimizers, new Minimizer(minimal, seq, posMinimal));

        for (int windowStart = l - k - 1; windowStart > l - k - w; windowStart--) {

            // iterating through kmers inside a window
            for (int i = posMinimal - 1; i >= windowStart; i--) {
                String kmer = seqString.substring(i, i + k);

                if (comp.compare(kmer, minimal) < 0) {
                    minimal = kmer;
                    posMinimal = i;
                }
            }

            addIfNotEqualToLast(reverseEndMinimizers, new Minimizer(minimal, seq, posMinimal));
        }

        // reverse iteration
        ListIterator<Minimizer> li = reverseEndMinimizers.listIterator(reverseEndMinimizers.size());
        while (li.hasPrevious()) {
            Minimizer m = li.previous();
            addIfNotEqualToLast(minimizers, m);
        }

        return minimizers;
    }
}
