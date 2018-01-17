import sys
from main import return_minizers, parse_fasta_file


def create_hashtable(minimizer_table, hash_file):
	"""
	Function that will receive data from minimizer_table
	and write it in the file on disk. This will include
	parsing of existing data in dictionary minimizer_table
	in specific format.
	:param minimizer_table: dict (dictionary of minimizers)
	:param hash_file: str (path to the file in which you want to save hash table
	:return: 
	"""
	with open(hash_file, "w") as file:
		for key, minimizer in minimizer_table.items():
			if isinstance(minimizer, list):
				file.write("{}:{}:{}\n".format(key, [item.position for item in minimizer], [item.sequence for item in minimizer]))


if __name__ == "__main__":
	"""
	Arguments to this script should be divided with ";"
	Arguments for this script should be:
	- k (int)
	- w (int)
	- path to file with saved sequence (str)
	- path to file where hashtable should be saved (str)

	For example:
	python MinimizerHashtableSave.py 3 6 fasta.txt hash.txt
	"""
	k, w, fasta_file, hash_file = sys.argv[1], sys.argv[2], sys.argv[3], sys.argv[4]
	sequence_dict = parse_fasta_file(fasta_file)
	mini_in_fasta_file = dict()
	for sequence_name, sequence_string in sequence_dict.items():
		mini_in_fasta_file.update(return_minizers(sequence_string, 5, 6, sequence_name=sequence_name))

	create_hashtable(mini_in_fasta_file, hash_file)
