from main import return_minizers, return_kmers


def test1_return_minimizers():
	my_string = "2310343"
	k = 3
	window_size = len(my_string)
	minimizers_should_be = {"034": 3}
	minimizers_are = return_minizers(my_string, k, window_size)
	assert minimizers_are == minimizers_should_be, """Test test1 failed! Minimizers that code returned:
													{}
													Minimizers that code should have returned:
													{}""".format(minimizers_are, minimizers_should_be)
	print("Test test1_return_minimizers OK")


def test2_return_minimizers():
	my_string = "426472814751"
	k = 7
	window_size = len(my_string)
	minimizers_should_be = {"2647281":1}
	minimizers_are = return_minizers(my_string, k, window_size)
	assert minimizers_are == minimizers_should_be, """Test test2 failed! Minimizers that code returned:
													{}
													Minimizers that code should have returned:
													{}""".format(minimizers_are, minimizers_should_be)
	print("Test test2_return_minimizers OK")


def test3_return_minimizers():
	my_string = "231032101233101"
	k = 3
	window_size = 6
	minimizers_should_be = {'032': 3, '012': 7, '123': 8, '101': 12}
	minimizers_are = return_minizers(my_string, k, window_size)
	assert minimizers_are == minimizers_should_be, """Test test3 failed! Minimizers that code returned:
													{}
													Minimizers that code should have returned:
													{}""".format(minimizers_are, minimizers_should_be)
	print("Test test3_return_minimizers OK")


def test1_return_kmers():
	my_string = "2310343"
	k = 3
	window_counter = 0
	kmers = return_kmers(my_string, k, window_counter)
	kmers_should_be = [('231', 0), ('310', 1), ('103', 2), ('034', 3), ('343', 4)]
	assert kmers == kmers_should_be, """Test test3 failed! KMERs that code returned:
										{}
										KMERS that code should have returned:
										{}""".format(kmers, kmers_should_be)
	print("Test test1_return_kmers OK")


def test2_return_kmers():
	my_string = "2310343"
	k = 3
	window_counter = 0
	kmers = return_kmers(my_string, k, window_counter)
	kmers_should_be = [('231', 0), ('310', 1), ('103', 2), ('034', 3), ('343', 4)]
	assert kmers == kmers_should_be, """Test test3 failed! KMERs that code returned:
										{}
										KMERS that code should have returned:
										{}""".format(kmers, kmers_should_be)
	print("Test test1_return_kmers OK")


if __name__ == "__main__":

	test1_return_minimizers()
	test2_return_minimizers()
	test3_return_minimizers()
	test1_return_kmers()