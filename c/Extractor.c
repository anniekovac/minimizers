#include <stdio.h>
#include <stdlib.h>
#include <string.h>

typedef struct mmzr {
	char mmStr[100];
	long int pos;
} Minimizer;

int main (int argc, char *argv[]) {

	long int w, k, windowPos, i, j, numOfKmers, minimPos;
	int totalLength, minimIndex, backMinimIndex;
	char *targetStr;
	char *ptr;
	char *Minim;
	char *backMinim;
	char *currentStr;
	char *targetStrPtr;
	FILE *outFile;
	Minimizer Minims[2000];
	Minimizer *backMinims;
	Minimizer lastPrinted;

	if (argc != 4) printf("%s\n", "Invalid number of arguments");
	
	// Reading arguments from console

	w = strtol(argv[1], &ptr, 10);
	k = strtol(argv[2], &ptr, 10);
	targetStr = argv[3];

	totalLength = strlen(targetStr);

	// Preparing necessary data holders
	
	Minim = (char *) malloc(2*k*sizeof(char));
	backMinim = (char *) malloc(2*k*sizeof(char));
	currentStr = (char *) malloc(2*k*sizeof(char));

	outFile = fopen("out.txt", "w");

	// Extract beginning minimizers
	
	targetStrPtr = targetStr;
	strncpy(Minim, targetStr, k);
	Minim[k] = '\0';

	minimIndex = 0;
	strcpy(Minims[minimIndex].mmStr, Minim);
	Minims[minimIndex].pos = (long int) targetStrPtr - (long int) targetStr;

	numOfKmers = 1;

	while ( numOfKmers <= w-1 ) {

		targetStrPtr++;
		strncpy(currentStr, targetStrPtr, k);
		currentStr[k] = '\0';
		if (strcmp(Minim, currentStr) > 0) {
			strcpy(Minim, currentStr);
			Minim[k] = '\0';
			minimIndex++;
			strcpy(Minims[minimIndex].mmStr, Minim);
			Minims[minimIndex].pos = (long int) targetStrPtr - (long int) targetStr;
		}
		numOfKmers++;
	}

	// Extract internal minimizers
	
	targetStrPtr = targetStr;

	while (strlen(targetStrPtr) >= (w + k - 1)) {
		strncpy(Minim, targetStrPtr, k);
		minimPos = (long int) targetStrPtr - (long int) targetStr;
		Minim[k] = '\0';
		numOfKmers = 0;
		i = 0;
		while (numOfKmers < w) {
			strncpy(currentStr, &targetStrPtr[i], k);
			currentStr[k] = '\0';
			if (strcmp(Minim, currentStr) > 0) {
				strcpy(Minim, currentStr);
				Minim[k] = '\0';
				minimPos = (long int) &targetStrPtr[i] - (long int) targetStr;
			}
			numOfKmers++;
			i++;
		}
		if ((strcmp(Minim, Minims[minimIndex].mmStr) != 0) ||
			(minimPos != Minims[minimIndex].pos)) {
			minimIndex++;
			strcpy(Minims[minimIndex].mmStr, Minim);
			Minims[minimIndex].pos = minimPos;
		}
		targetStrPtr++;
	}
	
	// Extract end minimizers

	targetStrPtr = &targetStr[totalLength-1-k+1];

	backMinims = (Minimizer *) malloc(2 * (w - 1) * sizeof(Minimizer));
	strncpy(backMinim, targetStrPtr, k);
	backMinim[k] = '\0';
	minimPos = (long int) targetStrPtr - (long int) targetStr;
	backMinimIndex = 0;
	backMinims[backMinimIndex].pos = minimPos;
	strcpy(backMinims[backMinimIndex].mmStr, backMinim);

	j = 0;
	while (strlen(targetStrPtr) < (w + k - 1)) {
		strncpy(backMinim, targetStrPtr, k);
		minimPos = (long int) targetStrPtr - (long int) targetStr;
		backMinim[k] = '\0';
		numOfKmers = 0;
		i = 0;
		j++;
		while (i < j) {
			strncpy(currentStr, &targetStrPtr[i], k);
			currentStr[k] = '\0';
			if (strcmp(backMinim, currentStr) > 0) {
				strcpy(backMinim, currentStr);
				backMinim[k] = '\0';
				minimPos = (long int) &targetStrPtr[i] - (long int) targetStr;
			}
			numOfKmers++;
			i++;
		}
		if ((strcmp(backMinim, backMinims[backMinimIndex].mmStr) != 0) ||
			(minimPos != backMinims[backMinimIndex].pos)) {
			backMinimIndex++;
			strcpy(backMinims[backMinimIndex].mmStr, backMinim);
			backMinims[backMinimIndex].pos = minimPos;
		}
		targetStrPtr--;
	}

	// Ensure end minimizers are written in the proper order

	for (i = backMinimIndex; i >= 0; i--) {
		minimIndex++;
		Minims[minimIndex].pos = backMinims[i].pos;
		strcpy(Minims[minimIndex].mmStr, backMinims[i].mmStr);
	}

	// Print located minimizers while removing duplicates
	
	strcpy(lastPrinted.mmStr, "+\0");
	lastPrinted.pos = -1;
	for (i = 0; i <= minimIndex; i++) {
		if (strcmp(Minims[i].mmStr, lastPrinted.mmStr) == 0 &&
			lastPrinted.pos == Minims[i].pos) continue;
		fprintf(outFile, "Minimizer %s at position %ld\n", Minims[i].mmStr, Minims[i].pos);
		strcpy(lastPrinted.mmStr, Minims[i].mmStr);
		lastPrinted.pos = Minims[i].pos;
	}

	fclose(outFile);

	return 0;
}
