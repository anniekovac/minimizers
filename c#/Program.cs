using MinimizersCore.Tests;
using System;

namespace MinimizersCore
{
    class Program
    {
        static void Main(string[] args)
        {
            Console.WriteLine("Testing start!");
            //                               w,   k ,  string
            //ExtractorTest.Test(new string[]{"3", "3", "231032101233101" });
            //ExtractorTest.Test(new string[] { "5", "3", "56324156321634126543516243" });
            HasherTest.Test(new string[] {"5", "3", "database.fasta", "out.txt" });
        }
    }
}
