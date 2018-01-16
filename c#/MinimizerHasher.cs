using MinimizersCore.Models;
using System;
using System.Collections.Generic;
using System.IO;
using System.Text;

namespace MinimizersCore
{
    /// <summary>
    /// Class which extracts, stores and restores minimizers from sequences parsed from FASTA files
    /// </summary>
    class MinimizerHasher
    {              
        private List<GeneSequence> sequences;
        private Dictionary<string, List<Minimizer>> minimizerLedger;
              
        private List<GeneSequence> Sequences { get => sequences; set => sequences = value; }
        private Dictionary<string, List<Minimizer>> MinimizerLedger { get => minimizerLedger; set => minimizerLedger = value; }

        /// <summary>
        /// Creates a new MinimizerHasher 
        /// </summary>
        public MinimizerHasher()
        {}

        /// <summary>
        /// Extract sequences from given file, then extract minimizers from every found sequence
        /// </summary>
        /// <param name="w">Number of k-mers in one batch (window) of minimizer extraction</param>
        /// <param name="k">Minimizer size (character length of substrings (k-mers))</param>
        /// <param name="path">Path to file from which to extract sequences and minimizers</param>
        public void AnalyzeSequences(int w, int k, string path)
        {                    
            Sequences = new List<GeneSequence>();
            MinimizerLedger = new Dictionary<string, List<Minimizer>>();

            ExtractSequences(path);
            ExtractMinimizers(w, k);        
        }

        private void ExtractSequences(string path)
        {           
            try
            {  
                using (StreamReader sr = new StreamReader(path))
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


        private void ExtractMinimizers(int w, int k)
        {          
            foreach (GeneSequence sequence in Sequences)
            {            
                List<Minimizer> minimizerList = Extractor.Extract(sequence, w, k);

                foreach (Minimizer minimizer in minimizerList)
                {
                    if (!MinimizerLedger.ContainsKey(minimizer.MinimizerString))
                        MinimizerLedger.Add(minimizer.MinimizerString, new List<Minimizer>() { minimizer });
                    else
                        MinimizerLedger[minimizer.MinimizerString].Add(minimizer);
                } 
            } 
        }

        /// <summary>
        /// Saves minimizers and their mappings currently held in memory to a file
        /// </summary>
        /// <param name="path">Path to file where the mappings are to be saved</param>
        public void StoreMinimizerMappings(string path)
        {
            try
            {
                using (StreamWriter file = new StreamWriter(path))
                {
                    foreach (string minimizerString in MinimizerLedger.Keys)
                    {
                        List<Minimizer> current = MinimizerLedger[minimizerString];

                        file.WriteLine(":" + minimizerString);

                        foreach (Minimizer minimizer in current)
                        {
                            file.WriteLine(">" + minimizer.Sequence.Name);
                            file.WriteLine("#" + minimizer.Position.ToString());
                        }
                    }
                }
            }

            catch (Exception e)
            {
                Console.WriteLine("The file could not be opened:");
                Console.WriteLine(e.Message);
            }

        }

        /// <summary>
        /// Reads minimizers and their mappings from file to memory, allowing for queries against them
        /// <para>Note, files must have the same content format as specified by StoreMinimizerMappings()</para>
        /// </summary>
        /// <param name="path">Path to file where minimizer mappings are saved</param>
        public void RestoreMinimizerMappings(string path)
        {
            try
            {
                using (StreamReader file = new StreamReader(path))
                {
                    //Reset existing structures
                    Sequences = new List<GeneSequence>();
                    MinimizerLedger = new Dictionary<string, List<Minimizer>>();

                    // A dictionary that maps a minimizer string to a list of pairs; 
                    // The pairs consist of the name of the sequence where the minimizer string was found, and the position at which it was found  
                    Dictionary<string, List<Tuple<string, int>>> tarball = new Dictionary<string, List<Tuple<string, int>>>();

                    string newMinimizer = "";
                    while (file.Peek() >= 0)
                    {
                        char current = (char)file.Read();
                        switch (current)
                        {
                            case ':':
                                newMinimizer = file.ReadLine();
                                tarball.Add(newMinimizer, new List<Tuple<string, int>>());
                                break;
                            case '>':
                                string sequenceName = file.ReadLine();
                                file.Read(); // Eat the #
                                string position = file.ReadLine();
                                tarball[newMinimizer].Add(new Tuple<string, int>(sequenceName, Int32.Parse(position)));
                                break;
                        }
                    }

                    foreach (string minimizerString in tarball.Keys)
                    {
                        MinimizerLedger.Add(minimizerString, new List<Minimizer>());
                        List<Tuple<string, int>> current = tarball[minimizerString];
                        foreach (Tuple<string, int> pair in current)
                        {
                            MinimizerLedger[minimizerString].Add(new Minimizer(minimizerString, new GeneSequence(pair.Item1, ""), pair.Item2));
                        }

                    }
                   
                }
            }
            catch (Exception e)
            {
                Console.WriteLine("The file could not be read:");
                Console.WriteLine(e.Message);
            }
        }


        /// <summary>
        /// Queries a string, and returns all matching minimizers complete with positions and sequences they were found in
        /// </summary>
        /// <param name="query"></param>
        /// <returns></returns>
        public List<Minimizer> QueryMinimizers(string query)
        {
            return MinimizerLedger[query];
        }



        private class FASTAEntry
        {
            public string Name { get; set; }
            public StringBuilder Sequence { get; set; }
        }

        // maybe this can just be private
        private IEnumerable<FASTAEntry> ParseFasta(StreamReader fastaFile)
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
