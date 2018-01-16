using MinimizersCore.Models;
using System;
using System.Collections.Generic;

namespace MinimizersCore.Tests
{
    public class HasherTest
    {       
        /// <summary>
        /// Test for minimizer querying 
        /// </summary>
        /// <param name="args"></param>
        public static void Test(String[] args)
        {
            if (args.Length != 4)
            {
                Console.WriteLine("Invalid number of arguments");
                return;
            }

            int w = Int32.Parse(args[0]);
            int k = Int32.Parse(args[1]);
            string pathTarget = args[2];           
            string query = args[3];

            MinimizerHasher hasher = new MinimizerHasher();
            hasher.AnalyzeSequences(w, k, pathTarget);
            List<Minimizer> list = hasher.QueryMinimizers(query); 

            using (System.IO.StreamWriter file = new System.IO.StreamWriter("out.txt"))
            {                            
                foreach (Minimizer minimizer in list)
                {
                    file.WriteLine(minimizer.MinimizerString + " on position " + minimizer.Position + " in sequence named:   " + minimizer.Sequence.Name);
                }              
            }


        }

        /// <summary>
        /// Test for save and restore from file 
        /// </summary>
        /// <param name="args"></param>
        public static void Test2(String[] args)
        {
            if (args.Length != 5)
            {
                Console.WriteLine("Invalid number of arguments");
                return;
            }

            int w = Int32.Parse(args[0]);
            int k = Int32.Parse(args[1]);
            string pathTarget = args[2];
            string pathResult = args[3];
            string query = args[4];

            MinimizerHasher hasher = new MinimizerHasher();
            hasher.AnalyzeSequences(w, k, pathTarget);
            hasher.StoreMinimizerMappings(pathResult);
            hasher.RestoreMinimizerMappings(pathResult);
            List<Minimizer> list = hasher.QueryMinimizers(query);

            using (System.IO.StreamWriter file = new System.IO.StreamWriter("out.txt"))
            {
                foreach (Minimizer minimizer in list)
                {
                    file.WriteLine(minimizer.MinimizerString + " on position " + minimizer.Position + " in sequence named:   " + minimizer.Sequence.Name);
                }
            }


        }





    }
}
