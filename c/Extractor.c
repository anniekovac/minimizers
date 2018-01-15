#include <stdio.h>
#include <stdlib.h>
#include <string.h>

typedef struct mmzr {
	char mmStr[100];
	long int pos;
} Minimizer;

int main (int argc, char *argv[]) {
	
	long int w, k, windowPos, i, j, numOfKmers, minimPos;
	int totalLength, minimIndex;
	char *targetStr;
	char *ptr;
	char *Minim;
	char *currentStr;
	char *targetStrPtr;
	FILE *outFile;
	Minimizer Minims[2000];
	
	if (argc != 4) printf("%s\n", "Invalid number of arguments");
	
	w = strtol(argv[1], &ptr, 10);
	k = strtol(argv[2], &ptr, 10);
	targetStr = argv[3];
	
	totalLength = strlen(targetStr);
	
	Minim = (char *) malloc(2*k*sizeof(char));
	currentStr = (char *) malloc(2*k*sizeof(char));
	
	outFile = fopen("out.txt", "w");
	
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
	
	targetStrPtr = targetStr;
	
	
	while (strlen(targetStrPtr) >= (w + k - 1)) {
		strncpy(Minim, targetStrPtr, k);
		Minim[k] = '\0';
		numOfKmers = 0;
		i = 0;
		while (numOfKmers < w) {
			strncpy(currentStr, &targetStrPtr[i], k);
			if (strcmp(Minim, currentStr) > 0) {
				strcpy(Minim, currentStr);
				Minim[k] = '\0';
				minimPos = (long int) &targetStrPtr[i] - (long int) targetStr;
			}
			numOfKmers++;
			i++;
		}
		if (strcmp(Minim, Minims[minimIndex].mmStr) != 0) {
			minimIndex++;
			strcpy(Minims[minimIndex].mmStr, Minim);
			Minims[minimIndex].pos = minimPos;
		}
		targetStrPtr++;
	}
	
	for (i = 0; i <= minimIndex; i++) {
		fprintf(outFile, "Minimizer %s at position %ld\n", Minims[i].mmStr, Minims[i].pos);
	}
	
	fclose(outFile);
	
	return 0;
}