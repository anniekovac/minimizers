using MinimizersCore.Tests;
using System;

namespace MinimizersCore
{
    class Program
    {
        static void Main(string[] args)
        {
            Console.WriteLine("Testing start!");

            if (args.Length == 3)
                ExtractorTest.Test(args);
            else if (args.Length == 4)
                HasherTest.Test(args);
            else if (args.Length == 5)
                HasherTest.Test2(args);
            else
                Console.WriteLine("Invalid number of arguments");

            //                                 w,   k , string
            //ExtractorTest.Test(new string[]{"3", "3", "231032101233101" });
            //ExtractorTest.Test(new string[]{"5", "3", "56324156321634126543516243" });

            //                               w,   k,   target,           result,     query          
            //HasherTest.Test1(new string[]{"5", "3", "database.fasta", "EKG" });
            //HasherTest.Test2(new string[] { "5", "3", "database.fasta", "Mappings.txt", "EKG" });
        }
    }
}
