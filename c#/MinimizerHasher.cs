using MinimizersCore.Models;
using System;
using System.Collections.Generic;
using System.IO;
using System.Text;

namespace MinimizersCore
{
    /// <summary>
    /// Class which uses Extractor to extract minimizers from sequences parsed from FASTA files, and hashes them in a hashtable 
    /// </summary>
    class MinimizerHasher
    {
        private string pathTarget;
        private string pathResult;
        private int w;
        private int k;

        private List<GeneSequence> sequences;
        private Dictionary<string, List<Minimizer>> minimizerLedger;

        public string PathTarget { get => pathTarget; set => pathTarget = value; }
        public string PathResult { get => pathResult; set => pathResult = value; }
        public int W { get => w; set => w = value; }
        public int K { get => k; set => k = value; }
        private List<GeneSequence> Sequences { get => sequences; set => sequences = value; }
        public Dictionary<string, List<Minimizer>> MinimizerLedger { get => minimizerLedger; set => minimizerLedger = value; }

        /// <summary>
        /// Creates a new MinimizerHasher with given parameters
        /// </summary>
        /// <param name="w">Number of k-mers in one batch (window) of minimizer extraction</param>
        /// <param name="k">Minimizer size (character length of substrings (k-mers))</param>
        /// <param name="pathTarget">Path to file from which to extract sequences and minimizers</param>
        /// <param name="pathResult">Path to file where the hashtable is to be saved </param>
        public MinimizerHasher(int w, int k, string pathTarget, string pathResult)
        {
            W = w;
            K = k;
            PathTarget = pathTarget;
            PathResult = PathResult;
            Sequences = new List<GeneSequence>();
            MinimizerLedger = new Dictionary<string, List<Minimizer>>();
        }


        public void ExtractSequences()
        {
            try
            {  
                using (StreamReader sr = new StreamReader(PathTarget))
                {
                    foreach (FASTAEntry f in ParseFasta(sr))
                        Sequences.Add(new GeneSequence(f.Name, f.Sequence.ToString()));                                       
                }
            }
            catch (Exception e)
            {
                Console.WriteLine("The file could not be read:");
                Console.WriteLine(e.Message);
            }
        }


        public void ExtractMinimizers()
        {
            foreach (GeneSequence sequence in Sequences)
            {
                List<Minimizer> minimizerList = Extractor.Extract(sequence, w, k);
                MinimizerLedger.Add(sequence.Body, minimizerList); // this needs t obe changed, it should save minimizer string and then a list of all minimizers with it, not a sequence and all of its minimizers
            } 

        }


        public void CreateHashTable()
        {

        }







        private class FASTAEntry
        {
            public string Name { get; set; }
            public StringBuilder Sequence { get; set; }
        }

        // maybe this can just be private
        static IEnumerable<FASTAEntry> ParseFasta(StreamReader fastaFile)
        {
            FASTAEntry f = null;
            string line;
            while ((line = fastaFile.ReadLine()) != null)
            {
                // ignore comment lines
                if (line.StartsWith(";"))
                    continue;

                if (line.StartsWith(">"))
                {
                    if (f != null)
                        yield return f;
                    f = new FASTAEntry { Name = line.Substring(1), Sequence = new StringBuilder() };
                }
                else if (f != null)
                    f.Sequence.Append(line);
            }
            yield return f;
        }


    }
}
