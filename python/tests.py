from main import return_minizers, return_kmers, parse_fasta_file
from pprint import pprint as pp


def test1_return_minimizers():
	my_string = "2310343"
	k = 3
	window_size = len(my_string)
	minimizers_should_be = {"034": 3}
	minimizers_are = return_minizers(my_string, k, window_size)
	keys = [key for key in minimizers_are]
	for key in keys:
		minimizers_are[key] = minimizers_are[key][0].position
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
	keys = [key for key in minimizers_are]
	for key in keys:
		minimizers_are[key] = minimizers_are[key][0].position
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
	keys = [key for key in minimizers_are]
	for key in keys:
		minimizers_are[key] = minimizers_are[key][0].position
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


def test_positions():

	string_of_choice = "AAGATGGCTAAGCAAGATTA"
	mini_in_string = return_minizers(string_of_choice, 5, 6)

	returned_positions = [item.position for item in mini_in_string["AAGAT"]]
	positions_should_be = [0, 13]
	assert returned_positions == positions_should_be, """Wrong positions returned for 'AAGAT',
														positions returned: {}, 
														positions should be: {}""".format(returned_positions, positions_should_be)

	returned_positions = [item.position for item in mini_in_string["AGATG"]]
	positions_should_be = [string_of_choice.find("AGATG")]
	assert returned_positions == positions_should_be, """Wrong positions returned for 'AGATG',
														positions returned: {}, 
														positions should be: {}""".format(returned_positions, positions_should_be)

	returned_positions = [item.position for item in mini_in_string["ATGGC"]]
	positions_should_be = [string_of_choice.find("ATGGC")]
	assert returned_positions == positions_should_be, """Wrong positions returned for 'ATGGC',
														positions returned: {}, 
														positions should be: {}""".format(returned_positions, positions_should_be)

	returned_positions = [item.position for item in mini_in_string["AAGCA"]]
	positions_should_be = [string_of_choice.find("AAGCA")]
	assert returned_positions == positions_should_be, """Wrong positions returned for 'AAGCA',
														positions returned: {}, 
														positions should be: {}""".format(returned_positions, positions_should_be)

	print("Test test_positions OK")


def test_searching_for_string_prefix():
	"""
	Testing search for string by using minimizers, but only when 
	key (minimizer is prefix in string of choice.
	Searching for string otherwise (if minimizer is in the middle of a string or at the end)
	IS NOT IMPLEMENTED YET.
	"""
	string_of_choice = "AAGAGTGTCTGATAGC"
	key = 'AAGAG'
	path = "fasta.txt"

	name, value = parse_fasta_file(path)
	mini_in_fasta_file = return_minizers(value, 5, 6, sequence_name=name)
	pos = mini_in_fasta_file[key][0].position
	found_string = value[pos:pos+len(string_of_choice)]
	assert found_string == string_of_choice, "Found string is {}, but string we searched for was {}".format(found_string, string_of_choice)
	print("Test test_searching_for_string_prefix OK")


if __name__ == "__main__":

	test1_return_minimizers()
	test2_return_minimizers()
	test3_return_minimizers()
	test1_return_kmers()
	test_positions()
	test_searching_for_string_prefix()