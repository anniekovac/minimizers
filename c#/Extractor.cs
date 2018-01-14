using MinimizersCore.Models;
using System;
using System.Collections.Generic;


namespace MinimizersCore
{
    public static class Extractor
    {     
        /// <summary>
        /// Static function that extracts minimizers from a given sequence
        /// </summary>
        /// <param name="sequence">Sequence from which to extract minimizers</param>
        /// <param name="w">Number of k-mers in one batch (window) of minimizer extraction</param>
        /// <param name="k">Minimizer size (character length of substrings (k-mers))</param>
        /// <returns></returns>
        public static List<Minimizer> Extract(GeneSequence sequence, int w, int k)
        {
            if (w < 0 || k < 0 || sequence == null || sequence.Body.Length < k)           
                throw new ArgumentException("Given arguments not valid");                
            
            string seqString = sequence.Body;

            List<Minimizer> minimizers = new List<Minimizer>();

            ExtractBeginningMinimizers(minimizers, sequence, w, k);

            string minimal = seqString.Substring(0, k);
            int posMinimal = 0;

            // moving a window
            for (int windowStart = 0; windowStart < (seqString.Length + 2 - w - k); windowStart++)
            {
                if (posMinimal < windowStart)
                {
                    minimal = null;
                }

                // iterating through kmers inside a window
                for (int j = Math.Max(windowStart, posMinimal + 1); j < windowStart + k; j++)
                {
                    string kmer = seqString.Substring(j, k);

                    if (minimal == null || String.Compare(kmer, minimal) < 0) 
                    {
                        minimal = kmer;
                        posMinimal = j;
                    }
                }

                AddIfLargerPosition(minimizers, new Minimizer(minimal, sequence, posMinimal));
            }

            ExtractEndingMinimizers(minimizers, sequence, w, k);

            return minimizers;
        }           

        /**
        * Adds a minimizer to a list of minimizers if the minimizer's position is larger than the last minimizer's position
        * or if the list is empty.
        * @param minimizers List of minimizers where the minimizer will be added
        * @param m Minimizer to add to the list
        */
        private static void AddIfLargerPosition(List<Minimizer> minimizers, Minimizer m)
        {
            if (minimizers.Count == 0 || m.Position > minimizers[minimizers.Count - 1].Position)           
                minimizers.Add(m);                 
        }

        /**
        * Extracts end-minimizers from the beginning of the sequence and appends them to the end of the given minimizer list
        * @param minimizers List of minimizers where the minimizers will be appended
        * @param seq Sequence from which the minimizers will be extracted
        * @param w Window size
        * @param k Minimizer length
        * @return List of minimizers with beginning end-minimizers appended
        */
        private static List<Minimizer> ExtractBeginningMinimizers(List<Minimizer> minimizers, GeneSequence seq, int w, int k)
        {
            string seqString = seq.Body;

            string minimal = seqString.Substring(0, k);
            int posMinimal = 0;
            AddIfLargerPosition(minimizers, new Minimizer(minimal, seq, posMinimal));
            int seqStringLength = seqString.Length;

            for (int windowEnd = 2; windowEnd < Math.Min(w, seqStringLength - k + 2); windowEnd++)
            {

                // iterating through kmers inside a window
                for (int i = posMinimal + 1; i < windowEnd; i++)
                {
                    string kmer = seqString.Substring(i, k);

                    if (String.Compare(kmer, minimal) < 0)
                    {
                        minimal = kmer;
                        posMinimal = i;
                    }
                }

                AddIfLargerPosition(minimizers, new Minimizer(minimal, seq, posMinimal));
            }

            return minimizers;
        }

        /**
        * Extracts end-minimizers from the ending of the sequence and appends them to the end of the given minimizer list.
        * @param minimizers List of minimizers where the minimizers will be appended
        * @param seq Sequence from which the minimizers will be extracted
        * @param w Window size
        * @param k Minimizer length  
        * @return List of minimizers with ending end-minimizers appended
        */
        private static List<Minimizer> ExtractEndingMinimizers(List<Minimizer> minimizers, GeneSequence seq, int w, int k)
        {
            string seqString = seq.Body;
            int l = seqString.Length;

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
                    string kmer = seqString.Substring(i, k);

                    if (minimal == null || String.Compare(kmer, minimal) < 0)
                    {
                        minimal = kmer;
                        posMinimal = i;
                    }
                }

                AddIfLargerPosition(minimizers, new Minimizer(minimal, seq, posMinimal));
            }

            return minimizers;
        }
    }
    
}
