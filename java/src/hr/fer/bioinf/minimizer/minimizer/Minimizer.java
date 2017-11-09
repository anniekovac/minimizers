package hr.fer.bioinf.minimizer.minimizer;

import hr.fer.bioinf.minimizer.sequence.Sequence;

public class Minimizer {
    private String string;
    private Sequence seq;
    private int pos;

    public Minimizer(String string, Sequence seq, int pos) {
        this.string = string;
        this.seq = seq;
        this.pos = pos;
    }

    public String getString() {
        return string;
    }

    public Sequence getSeq() {
        return seq;
    }

    public int getPos() {
        return pos;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;

        Minimizer minimizer = (Minimizer) other;
        return (seq == minimizer.seq && pos == minimizer.pos);
    }
}
