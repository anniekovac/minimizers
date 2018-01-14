using MinimizersCore.Models;
using System;
using System.Collections.Generic;
using System.Text;

namespace MinimizersCore.Tests
{
    public class ExtractorTest
    {      
        private static String USAGE = "USAGE: java hr.fer.bioinf.minimizer.ExtractorDemo <w> <k> <string>";

        public static void main(String[] args)
        {
            if (args.Length != 3)
            {
                Console.WriteLine(USAGE);
                return;
            }

            int w = 0;
            int k = 0;
            string body = null;

            try
            {
                w = Int32.Parse(args[0]);
                k = Int32.Parse(args[1]);
                body = args[2];
            }
            catch (ArgumentException ex)
            {
                Console.WriteLine(USAGE);
                return;
            }

            List<Minimizer> minimizerList = Extractor.Extract(new GeneSequence("", body), w, k);


            using (System.IO.StreamWriter file = new System.IO.StreamWriter("out.txt"))
            {
                foreach (Minimizer minimizer in minimizerList)
                {
                    file.WriteLine("" + minimizer.MinimizerString + " on position " + minimizer.Position);                      
                }
            }               

        }
        
    }
}
