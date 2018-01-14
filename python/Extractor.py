import sys
from main import return_minizers

if __name__ == "__main__":
	a = ''.join(sys.argv[1:])
	minimizers = return_minizers(a, 3, 6)
	for key, minimizer in minimizers.items():
		if isinstance(minimizer, list):
			print("Position of minimizer {} is: {}".format(key, [item.position for item in minimizer]))
		else:
			print("Position of minimizer {} is: {}".format(key, minimizer.position))