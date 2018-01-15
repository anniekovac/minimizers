using MinimizersCore.Models;
using System;
using System.Collections.Generic;

namespace MinimizersCore
{
    /// <summary>
    /// Extractor that implements "2.3. A mixed strategy" from article: http://www.csri.utoronto.ca/~wayne/research/papers/minimizers.pdf <para/>
    /// End minimizers are extracted from windows up to w-1 in size, and the rest are extracted as interior minimizers
    /// </summary>
    public static class Extractor
    {     
        /// <summary>
        /// Static function that extracts minimizers from a given sequence
        /// </summary>
        /// <param name="sequence">Sequence from which to extract minimizers</param>
        /// <param name="w">Number of k-mers in one batch (window) of minimizer extraction</param>
        /// <param name="k">Minimizer size (character length of substrings (k-mers))</param>
        /// <returns>List of extracted minimizers</returns>
        public static List<Minimizer> Extract(GeneSequence sequence, int w, int k)
        {
            if (w < 0 || k < 0 || sequence == null || sequence.Body.Length < k)           
                throw new ArgumentException("Given arguments not valid");                

            List<Minimizer> minimizers = new List<Minimizer>();          

            ExtractBeginningMinimizers(minimizers, sequence, w, k);
            ExtractInteriorMinimizers(minimizers, sequence, w, k);        
            ExtractEndingMinimizers(minimizers, sequence, w, k);

            return minimizers;
        }


        private static void ExtractInteriorMinimizers(List<Minimizer> minimizers, GeneSequence sequence, int w, int k)
        {
            string minimal = sequence.Body.Substring(0, k);
            int posMinimal = 0;
        
            // moving a window, so last iteration has just enough space to fill w kmers
            for (int windowStart = 0; windowStart < (sequence.Body.Length + 2 - w - k); windowStart++)
            {
                if (posMinimal < windowStart)
                    minimal = null;

                // iterating through kmers inside a window
                for (int j = windowStart; j < windowStart + w; j++)
                {
                    string kmer = sequence.Body.Substring(j, k);

                    if (minimal == null || String.Compare(kmer, minimal) < 0)
                    {
                        minimal = kmer;
                        posMinimal = j;
                    }
                }

                if (minimizers.Count == 0 || posMinimal > minimizers[minimizers.Count - 1].Position)
                    minimizers.Add(new Minimizer(minimal, sequence, posMinimal));              
            }

        }       
      
     

       
        private static void ExtractBeginningMinimizers(List<Minimizer> minimizers, GeneSequence sequence, int w, int k)
        {           
            string minimal = sequence.Body.Substring(0, k);
            int posMinimal = 0;

            if (minimizers.Count == 0 || posMinimal > minimizers[minimizers.Count - 1].Position)
                minimizers.Add(new Minimizer(minimal, sequence, posMinimal));

            // since the first kmer is certainly added (it is the only kmer in a size 1 window), 
            // we can start at position 1, rather than 0, 
            // and since k has to be at least 1, we can set windowEnd to 2 for beginning 
            for (int windowEnd = 2; windowEnd < Math.Min(w, sequence.Body.Length - k + 2); windowEnd++)
            {

                // iterating through kmers inside a window
                for (int i = posMinimal + 1; i < windowEnd; i++)
                {
                    string kmer = sequence.Body.Substring(i, k);

                    if (String.Compare(kmer, minimal) < 0)
                    {
                        minimal = kmer;
                        posMinimal = i;
                    }
                }

                if (minimizers.Count == 0 || posMinimal > minimizers[minimizers.Count - 1].Position)
                    minimizers.Add(new Minimizer(minimal, sequence, posMinimal));
            }         
        }

     
        private static void ExtractEndingMinimizers(List<Minimizer> minimizers, GeneSequence sequence, int w, int k)
        {          
            int l = sequence.Body.Length;

            string minimal = null;
            int posMinimal = l - k;

            for (int windowStart = Math.Max(l - k - w, 0) + 1; windowStart <= l - k; windowStart++)
            {
                if (posMinimal < windowStart)
                {
                    minimal = null;
                }

                // iterating through kmers inside a window
                for (int i = windowStart; i <= l - k; i++)
                {
                    string kmer = sequence.Body.Substring(i, k);

                    if (minimal == null || String.Compare(kmer, minimal) < 0)
                    {
                        minimal = kmer;
                        posMinimal = i;
                    }
                }

                if (minimizers.Count == 0 || posMinimal > minimizers[minimizers.Count - 1].Position)
                    minimizers.Add(new Minimizer(minimal, sequence, posMinimal));
            }        
        }




    }    
}
