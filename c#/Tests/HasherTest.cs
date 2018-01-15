using MinimizersCore.Models;
using System;
using System.Collections.Generic;
using System.Text;

namespace MinimizersCore.Tests
{
    public static class HasherTest
    {

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
            string pathResult = args[3];

            MinimizerHasher hasher = new MinimizerHasher(w, k, pathTarget, pathResult);
            hasher.ExtractSequences();
            hasher.ExtractMinimizers();

            using (System.IO.StreamWriter file = new System.IO.StreamWriter(pathResult))
            {
                foreach (string sequence in hasher.MinimizerLedger.Keys)
                {
                    file.WriteLine(sequence);
                    foreach (Minimizer minimizer in hasher.MinimizerLedger[sequence])
                    {
                        file.WriteLine("" + minimizer.MinimizerString + " on position " + minimizer.Position);
                    }
                }
            }


        }



    }
}
