import sys
from main import return_minizers, parse_fasta_file, Minimizer


def load_hashtable(hash_file):
	"""
	Function that will receive data from minimizer_table
	and write it in the file on disk. This will include
	parsing of existing data in dictionary minimizer_table
	in specific format.
	:param minimizer_table: dict (dictionary of minimizers)
	:param hash_file: str (path to the file in which you want to save hash table
	:return: 
	"""
	minimizer_table = dict()
	with open(hash_file, "r") as file:
		for line in file:
			parsed_line = line.split(":")
			key = parsed_line[0]
			positions = parsed_line[1]
			mini = Minimizer(positions, key)
			minimizer_table[key] = mini
	return minimizer_table

if __name__ == "__main__":
	"""
	Argument for this script should be path to the file
	where hashtable is saved.
	For example:
	python MinimizerHashTableQuery.py hash.txt
	"""
	arguments = ''.join(sys.argv[1:])
	hash_file = arguments
	minimizer_dictionary = load_hashtable(hash_file)
	print(minimizer_dictionary["TTTCA"].position)
	# TTTCA:[4, 5795, 10555, 11256, 11399]