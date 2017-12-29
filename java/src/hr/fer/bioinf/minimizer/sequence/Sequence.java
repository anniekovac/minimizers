package hr.fer.bioinf.minimizer.sequence;

/**
 * Represent a single gene or protein sequence, usually extracted from a FASTA file. Sequences usually have a name and
 * a body (a sequence) but those can be set to an empty string if they are not required.
 */
public class Sequence {

    private final String name;
    private final String string;

    /**
     * Constructs a Sequence from the given name and body.
     * @param name Name of the sequence.
     * @param string Body of the sequence.
     */
    public Sequence(String name, String string) {
        this.name = name;
        this.string = string;
    }

    public String getName() {
        return name;
    }

    public String getString() {
        return string;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Sequence sequence = (Sequence) o;

        if (!name.equals(sequence.name)) return false;
        return string.equals(sequence.string);
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + string.hashCode();
        return result;
    }
}
