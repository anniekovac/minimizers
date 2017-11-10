package hr.fer.bioinf.minimizer.extractor;

import hr.fer.bioinf.minimizer.minimizer.Minimizer;
import hr.fer.bioinf.minimizer.sequence.Sequence;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class MinimizerExtractor {
    public static List<Minimizer> extract(Sequence seq, int w, int k, Comparator<String> comp) {
        String seqString = seq.getString();

        if (seqString.length() < k) {
            throw new IllegalArgumentException(
                    "Can't extract (w,k)-minimizer if the given sequence is smaller than k");
        }

        List<Minimizer> minimizers = new ArrayList<>((int) (seqString.length() * 2 * 1.5 / (w + 1)));

        String minimal = seqString.substring(0, k);
        int posMinimal = 0;
        for (int i = 1; i < Math.min(w, seqString.length() - k + 1); i++) {
            String kmer = seqString.substring(i, i + k);

            if (comp.compare(kmer, minimal) < 0) {
                minimal = kmer;
                posMinimal = i;
            }
        }
        addIfNotEqualToLast(minimizers, new Minimizer(minimal, seq, posMinimal));

        // moving a window
        for (int windowStart = 1; windowStart < (seqString.length() + 2 - w - k); windowStart++) {
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

        return minimizers;

    }

    private static List<Minimizer> addIfNotEqualToLast(List<Minimizer> minimizers, Minimizer m) {
        if (minimizers.isEmpty() || m.getPos() != minimizers.get(minimizers.size() - 1).getPos()) {
            minimizers.add(m);
        }

        return minimizers;
    }

    //private static List<String> extractBeginningMinimizers(Sequence seq, )
}
