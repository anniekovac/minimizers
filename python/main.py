# TODO: fix returning kmers for substring 123310 - WHATS WRONG ???


def return_kmers(input_string, k, index_from_beginning):
	"""
	Returns kmers from given input string.
	Length of returned kmers is k.
	:param input_string: str (string in which you want to find kmers)
	:param k: int (length of kmers you want to extract from your string)
	:param index_from_beginning: int (index from which input_string starts in original string)
	:return: list [(str, int), (str, int) ...] where str is kmer and int is its position in string
	"""
	my_string_copy = input_string
	kmers = []
	i = 0
	while my_string_copy:
		kmer = input_string[i:i+k]
		if len(kmer) < k:
			break
		kmers.append((kmer, index_from_beginning + i))
		my_string_copy = my_string_copy.split(my_string[i:i+k], 1)[-1]
		i += 1
	return kmers


def main(input_string, k, window_size):
	"""
	:param input_string: str (string in which you want to find minimizers in) 
	:param k: int (size of kmers)
	:param window_size: int (size of window which we consider)
	:return: 
	"""
	minimizers = dict()
	window_counter = 0
	while True:
		substring = input_string[window_counter:window_counter + window_size]
		if len(substring) < window_size:
			break
		kmers_in_window = return_kmers(substring, k, window_counter)
		print("substring", substring, "kmers in window:", kmers_in_window)
		minimizer = None
		for kmer in kmers_in_window:
			if minimizer is None or kmer[0] < minimizer:
				minimizer, position = kmer
		minimizers[minimizer] = position
		window_counter += 1
		#print("MINIMIZER:", minimizer)
	print(minimizers)


if __name__ == "__main__":
	#my_string = "2310343"
	#k = 3

	#my_string = "426472814751"
	#k = 7

	my_string = "231032101233101"
	k = 3
	window_size = 6

	main(my_string, k, window_size)
