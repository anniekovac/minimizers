using System;

namespace MinimizersCore.Models
{
    public class GeneSequence
    {
        private string name;
        private string body;

        public string Name { get => name; set => name = value; }
        public string Body { get => body; set => body = value; }

        /// <summary>
        /// Constructor for GeneSequence with given name and body
        /// </summary>
        /// <param name="name">The name of the sequence</param>
        /// <param name="body">The content of the sequence that is to be analyzed for minimizers</param>
        public GeneSequence(string name, string body)
        {
            Name = name;
            Body = body;
        }
        
        //?
        public override bool Equals(Object o)
        {
            if (this == o) return true;
            if (o == null || this.GetType() != o.GetType()) return false;  // GetType in c# is the same as getClass() in java, returns runtime type

            GeneSequence sequence = (GeneSequence)o;

            if (Name != sequence.Name) return false;
            return Body.Equals(sequence.Body);
        }

        //?
        public override int GetHashCode()
        {
            int result = Name.GetHashCode();
            result = 31 * result + Body.GetHashCode();
            return result;
        }
    }
}
