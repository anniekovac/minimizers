#include <stdio.h>
#include <stdlib.h>
#include <string.h>
 
typedef struct mmzr {
	char mmStr[100];
	long int pos;
} Minimizer;
 
int main(int argc, char *argv[])
{
	FILE * fp;
	char * line = NULL;
	size_t len = 0;
	ssize_t read;
	long int w, k, windowPos, i, j, numOfKmers, minimPos;
	char *ptr;
	FILE *outFile;
	int totalLength, minimIndex, backMinimIndex;
	char *targetStr;
	char *Minim;
	char *backMinim;
	char *currentStr;
	char *targetStrPtr;
	Minimizer *Minims;
	Minimizer *backMinims;
	Minimizer lastPrinted;
	
	if (argc != 5) {
		printf("Invalid number of arguments.\n");
		return -1;
	}
	
	w = strtol(argv[1], &ptr, 10);
	k = strtol(argv[2], &ptr, 10);
 
	fp = fopen(argv[3], "r");
	outFile = fopen(argv[4], "w");
	
	if (fp == NULL)
		return -1;
	
	if (outFile == NULL)
		return -1;
	
	Minim = (char *) malloc(2*k*sizeof(char));
	backMinim = (char *) malloc(2*k*sizeof(char));
	currentStr = (char *) malloc(2*k*sizeof(char));
	Minims = (Minimizer *) malloc(100001*sizeof(Minimizer));
 
	int state = 0;
	while ((read = getline(&line, &len, fp)) != -1) {
		/* Delete trailing newline */
		if (line[read - 1] == '\n')
			line[read - 1] = 0;
		/* Handle comment lines*/
		if (line[0] == '>') {
			if (state == 1)
				printf("\n");
			fprintf(outFile, "%s: \n", line+1);
			state = 1;
		} else {
			/* Print everything else */
			//printf("%s", line);
			targetStr = line;
			totalLength = strlen(targetStr);
			
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
			
			for (i = backMinimIndex; i >= 0; i--) {
				minimIndex++;
				Minims[minimIndex].pos = backMinims[i].pos;
				strcpy(Minims[minimIndex].mmStr, backMinims[i].mmStr);
			}
			
			strcpy(lastPrinted.mmStr, "+\0");
			lastPrinted.pos = -1;
			for (i = 0; i <= minimIndex; i++) {
				if (strcmp(Minims[i].mmStr, lastPrinted.mmStr) == 0 &&
					lastPrinted.pos == Minims[i].pos) continue;
				fprintf(outFile, "Minimizer %s at position %ld\n", Minims[i].mmStr, Minims[i].pos);
				strcpy(lastPrinted.mmStr, Minims[i].mmStr);
				lastPrinted.pos = Minims[i].pos;
			}
		}
	}
	printf("\n");
 
	fclose(fp);
	fclose(outFile);
	if (line)
		free(line);
	return 0;
}