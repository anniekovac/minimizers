namespace MinimizersCore.Models
{
    public class Minimizer
    {
        private string minimizerString;
        private GeneSequence sequence;
        private int position;

        public string MinimizerString { get => minimizerString; set => minimizerString = value; }
        public GeneSequence Sequence { get => sequence; set => sequence = value; }
        public int Position { get => position; set => position = value; }


        /// <summary>
        /// Constructs a minimizer with given parameters
        /// </summary>
        /// <param name="minimizerString">One extracted minimizer substring</param>
        /// <param name="sequence">Originating sequence for the minimizer substring</param>
        /// <param name="position">Position in sequence where the minimizer substring appears</param>
        public Minimizer(string minimizerString, GeneSequence sequence, int position)
        {
            MinimizerString = minimizerString;
            Sequence = sequence;
            Position = position;
        }
     
    }
}
