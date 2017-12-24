from pprint import pprint as pp


class Minimizer(object):
	def __init__(self, position, minimizer):
		self.minimizer = minimizer
		self.position = position
		self.sequence = None


def return_kmers(input_string, k, index_from_beginning):
	"""
	Returns kmers from given input string.
	Length of returned kmers is k.
	:param input_string: str (string in which you want to find kmers)
	:param k: int (length of kmers you want to extract from your string)
	:param index_from_beginning: int (index from which input_string starts in original string)
	:return: list [(str, int), (str, int) ...] where str is kmer and int is its position in string
	"""
	kmers = []
	i = 0
	if input_string == "426472":
		print("da")
	while True:
		kmer = input_string[i:i+k]  # slicing kmer out of input string
		if len(kmer) < k:  # if window is coming to the end
			break		   # break
		kmers.append((kmer, index_from_beginning + i))  # appending kmer and its index in original string
		i += 1
	return kmers


# TODO : if we are looking for minimizers in sequence, there should be sequence name included
def return_minizers(input_string, k, window_size):
	"""
	:param input_string: str (string in which you want to find minimizers in) 
	:param k: int (size of kmers)
	:param window_size: int (size of window which we consider)
	:return: 
	"""
	if window_size < k:
		raise ValueError("Your wanted window size ({}) is smaller than wanted k({})".format(window_size, k))
	minimizers = dict()
	window_counter = 0
	while True:
		substring = input_string[window_counter:window_counter + window_size]
		if len(substring) < window_size:
			break
		kmers_in_window = return_kmers(substring, k, window_counter)
		minimizer = None
		for kmer in kmers_in_window:
			if minimizer is None or kmer[0] < minimizer:
				minimizer, position = kmer
		minimizers[minimizer] = position
		window_counter += 1
	return minimizers


# TODO: parse fasta file that has more than one sequence in it
def parse_fasta_file(path_to_file):
	"""
	Function made for parsing FASTA files.
	:param path_to_file: str (path to file you want to parse)
	:return: dict ({sequence_name : sequence_string})
	"""
	sequence_dict = dict()
	with open(path_to_file, "r") as fasta:
		sequence_string = ""
		for line in fasta:
			if line.startswith(">"):
				sequence_name = line.strip(">").strip("\n")
			else:
				sequence_string += line.strip("\n")
	sequence_dict[sequence_name] = sequence_string
	return sequence_name, sequence_string

# TODO : searching for a string "string of choice" within file fasta.txt
if __name__ == "__main__":
	path = "fasta.txt"
	string_of_choice = "CAATATG"
	mini_in_string = return_minizers(string_of_choice, 5, 6)
	name, value = parse_fasta_file(path)
	print(mini_in_string)
	mini_in_fasta_file = return_minizers(value, 5, 6)
	pp(mini_in_fasta_file)