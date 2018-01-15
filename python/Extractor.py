import sys
from main import return_minizers

if __name__ == "__main__":
	a = ''.join(sys.argv[1:])
	print(a)
	minimizers = return_minizers(a, 4, 4)
	for key, minimizer in minimizers.items():
		if isinstance(minimizer, list):
			print("Position of minimizer {} is: {}".format(key, [item.position for item in minimizer]))
		else:
			print("Position of minimizer {} is: {}".format(key, minimizer.position))