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


def return_minizers(input_string, k, window_size):
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
		# print("substring:", substring)
		# print("window_counter:", window_counter)
		# print("window_size:", window_size)
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


def test1():
	my_string = "2310343"
	k = 3
	window_size = len(my_string)
	minimizers_should_be = {"034": 3}
	minimizers_are = return_minizers(my_string, k, window_size)
	assert minimizers_are == minimizers_should_be, """Test test1 failed! Minimizers that code returned:
													{}
													Minimizers that code should have returned:
													{}""".format(minimizers_are, minimizers_should_be)


def test2():
	my_string = "426472814751"
	k = 7
	window_size = len(my_string)
	minimizers_should_be = {"2647281":1}
	minimizers_are = return_minizers(my_string, k, window_size)
	assert minimizers_are == minimizers_should_be, """Test test2 failed! Minimizers that code returned:
													{}
													Minimizers that code should have returned:
													{}""".format(minimizers_are, minimizers_should_be)


def test3():
	my_string = "231032101233101"
	k = 3
	window_size = 6
	minimizers_should_be = {'032': 3, '012': 7, '123': 8, '101': 12}
	minimizers_are = return_minizers(my_string, k, window_size)
	assert minimizers_are == minimizers_should_be, """Test test3 failed! Minimizers that code returned:
													{}
													Minimizers that code should have returned:
													{}""".format(minimizers_are, minimizers_should_be)


if __name__ == "__main__":

	test1()
	test2()