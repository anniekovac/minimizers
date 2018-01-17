import sys
from main import return_minizers

if __name__ == "__main__":
	"""
	This program is made for searching minimizers in
	particular string. These minimizers will be shown in 
	out.txt directory. Example of using this program:
	
	python Extractor.py TTGACGTTGCATTACCGG k w
	
	python Extractor.py TTGACGTTGCATTACCGG 4 5
	"""
	string_of_choice, k, w = sys.argv[1], sys.argv[2], sys.argv[3]
	k = int(k)
	w = int(w)
	minimizers = return_minizers(string_of_choice, k, w)
	with open("out.txt", "w") as out:
		for key, minimizer in minimizers.items():
			if isinstance(minimizer, list):
				out.write("Position of minimizer {} is: {}\n".format(key, [item.position for item in minimizer]))
			else:
				out.write("Position of minimizer {} is: {}\n".format(key, minimizer.position))
