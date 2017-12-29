package hr.fer.bioinf.minimizer;

import hr.fer.bioinf.minimizer.extractor.MinimizerExtractor;
import hr.fer.bioinf.minimizer.minimizer.Minimizer;
import hr.fer.bioinf.minimizer.sequence.Sequence;

import java.util.List;

public class ExtractorDemo {
    private final static String USAGE = "USAGE: java hr.fer.bioinf.minimizer.ExtractorDemo <w> <k> <string>";

    public static void main(String[] args) {
        if (args.length != 3) {
            System.out.println(USAGE);
            return;
        }

        int w = 0;
        int k = 0;
        String string = null;

        try {
            w = Integer.parseInt(args[0]);
            k = Integer.parseInt(args[1]);
            string = args[2];

        } catch (NumberFormatException ex) {
            System.out.println(USAGE);
            return;
        }

        List<Minimizer> minimizerList = MinimizerExtractor.extract(new Sequence("", string), w, k);

        for (Minimizer minimizer : minimizerList) {
            System.out.println("" + minimizer.getString() + " on position " + minimizer.getPos());
        }
    }
}
