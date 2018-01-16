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
              
    }
}
