package hr.fer.bioinf.minimizer.minimizer;

import hr.fer.bioinf.minimizer.sequence.Sequence;

/**
 * Represents a single minimizer from a single sequence at a position.
 */
public class Minimizer {
    private String string;
    private Sequence seq;
    private int pos;

    /**
     * Constructs a Minimizer with the given string, from a given sequence at the given position.
     * @param string Minimizer string.
     * @param seq Sequence in which the minimizer was found.
     * @param pos Position at which the minimizer was found.
     */
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
    public String toString() {
        return string + " on position " + pos + " in " + seq.getName();
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;

        Minimizer minimizer = (Minimizer) other;
        return (seq == minimizer.seq && pos == minimizer.pos);
    }
}
