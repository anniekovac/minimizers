def main():
	#my_string = "2310343"
	#k = 3
	my_string = "426472814751"
	k = 7
	window_size = 5

	my_string_copy = my_string
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
	minimizer = None
	for position, kmer in enumerate(kmers):
		if minimizer is None or kmer < minimizer:
			minimizer, position = kmer, position
	minimizers[minimizer] = position
	print("MINIMIZER:", minimizer)



if __name__ == "__main__":
	main()
