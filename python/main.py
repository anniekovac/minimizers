def main(input_string, k, window_size):
	"""
	:param input_string: str (string in which you want to find minimizers in) 
	:param k: int (size of kmers)
	:param window_size: int (size of window which we consider)
	:return: 
	"""
	my_string_copy = input_string
	kmers = []
	i = 0
	while my_string_copy:
		kmer = my_string[i:i+k]
		if len(kmer) < k:
			break
		kmers.append(kmer)
		print(kmer)
		my_string_copy = my_string_copy.split(my_string[i:i+k], 1)[-1]
		i += 1

	minimizers = dict()
	window_counter = 0
	for brojac in range(0, 15):
		kmers_in_window = kmers[window_counter:window_counter + window_size]
		minimizer = None
		for position, kmer in enumerate(kmers_in_window):
			if minimizer is None or kmer < minimizer:
				minimizer, position = kmer, position
		minimizers[minimizer] = position
		window_counter += 1
		print("MINIMIZER:", minimizer)
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
