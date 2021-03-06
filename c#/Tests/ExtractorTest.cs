﻿using MinimizersCore.Models;
using System;
using System.Collections.Generic;

namespace MinimizersCore.Tests
{
    public class ExtractorTest
    {      
        /// <summary>
        /// Test that extracts minimizers from a given string and writes output to out.txt
        /// </summary>
        /// <param name="args"></param>
        public static void Test(String[] args)
        {
            if (args.Length != 3)
            {
                Console.WriteLine("Invalid number of arguments");
                return;
            }

            int w = Int32.Parse(args[0]);
            int k = Int32.Parse(args[1]);
            string body = args[2];
           

            List<Minimizer> minimizerList = Extractor.Extract(new GeneSequence("TestSequence", body), w, k);


            using (System.IO.StreamWriter file = new System.IO.StreamWriter("out.txt"))
            {
                foreach (Minimizer minimizer in minimizerList)
                {
                    file.WriteLine(minimizer.MinimizerString + " on position " + minimizer.Position);                      
                }
            }               

        }
        





    }
}
