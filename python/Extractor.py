import sys
from main import return_minizers

if __name__ == "__main__":
	a = ''.join(sys.argv[1:])
	# print(a)
	minimizers = return_minizers(a, 4, 5)
	with open("out.txt", "w") as out:
		for key, minimizer in minimizers.items():
			if isinstance(minimizer, list):
				# print("Position of minimizer {} is: {}".format(key, [item.position for item in minimizer]))
				out.write("Position of minimizer {} is: {}".format(key, [item.position for item in minimizer]))
			else:
				out.write("Position of minimizer {} is: {}".format(key, minimizer.position))
				# print("Position of minimizer {} is: {}".format(key, minimizer.position))