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
	where hashtable is saved and string which you want to
	be found (which is a minimizer saved in hash.txt).
	For example:
	python MinimizerHashTableQuery.py hash.txt;TTTCA
	"""
	arguments = ''.join(sys.argv[1:])
	hash_file, string_to_find = arguments.split(";")
	minimizer_dictionary = load_hashtable(hash_file)
	output_file = "out.txt"
	try:
		with open(output_file, "w") as out:
			out.write("Found string: {}, Positions: {}".format(string_to_find, minimizer_dictionary[string_to_find].position))
			#print(minimizer_dictionary[string_to_find].position)
	except KeyError:
		with open(output_file, "w") as out:
			out.write("Minimizer string {} not found".format(string_to_find))
	# TTTCA:[4, 5795, 10555, 11256, 11399]