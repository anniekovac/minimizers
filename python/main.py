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
	while True:
		kmer = input_string[i:i + k]  # slicing kmer out of input string
		# print(kmer)
		if len(kmer) < k:  # if window is coming to the end
			break  # break
		kmers.append((kmer, index_from_beginning + i))  # appending kmer and its index in original string
		i += 1
	return kmers


def return_minizers(input_string, k, window_size, sequence_name=None):
	"""
	:param input_string: str (string in which you want to find minimizers in) 
	:param k: int (size of kmers)
	:param window_size: int (size of window which we consider)
	:param sequence_name: str (name of a sequence)	
	:return: dict ({minimizer : Minimizer instance}) 
	"""
	# if window_size < k:  # if window size is smaller than k
	# 	raise ValueError("Your wanted window size ({}) is smaller than wanted k({})".format(window_size, k))
	minimizers = dict()
	window_counter = - window_size + 1
	while True:
		# getting substring from input_string
		# from window_counter (which is always amplified by one, as window moves)
		# to window_counter + window_size
		if window_counter < 0:
			substring = input_string[0:window_counter + window_size + k - 1]
			index_from_beginning = 0
		elif window_counter + window_size + k - 1 > len(input_string):
			substring = input_string[window_counter:]
			index_from_beginning = window_counter
		else:
			substring = input_string[window_counter:window_counter + window_size + k - 1]
			index_from_beginning = window_counter
		if len(substring) < k:
			break

		# get kmers in this selected substring
		kmers_in_window = return_kmers(substring, k, index_from_beginning)
		minimizer = None

		# check every kmer
		for kmer in kmers_in_window:

			# if minimizer is not set yet (None)
			# or minimizer is smaller than current one
			if minimizer is None or kmer[0] < minimizer.minimizer:

				# creating instance of Minimizer(position, value)
				# position = kmer[1] if kmer[1] >= 0 else 0
				mini_instance = Minimizer(kmer[1], kmer[0])
				if sequence_name:
					mini_instance.sequence = sequence_name
				minimizer = mini_instance

		# enlarging minimizers dictionary
		if minimizer.minimizer in minimizers:  # if minimizer already exists among keys
			mini_list = minimizers[minimizer.minimizer]  # list of minimizers found for this minimizer key
			positions = [mini.position for mini in mini_list]
			if minimizer.position not in positions:
				minimizers[minimizer.minimizer].append(minimizer)  # append minimizer to the list
		else:
			minimizers[minimizer.minimizer] = [minimizer]  # else, create new list and add minimizer to it

		window_counter += 1  # amplifiy window counter
	return minimizers


def parse_fasta_file(path_to_file):
	"""
	Function made for parsing FASTA files.
	:param path_to_file: str (path to file you want to parse)
	:return: dict ({sequence_name : sequence_string})
	"""
	sequence_dict = dict()
	with open(path_to_file, "r") as fasta:
		sequence_string = ""
		sequence_name = None
		for line in fasta:
			if line.startswith(">"):
				if sequence_name is not None:
					sequence_dict[sequence_name] = sequence_string
					sequence_string = ""
				sequence_name = line.strip(">").strip("\n")
			else:
				sequence_string += line.strip("\n")
		sequence_dict[sequence_name] = sequence_string
	return sequence_dict
	# return sequence_name, sequence_string
