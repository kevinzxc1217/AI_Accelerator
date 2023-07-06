// lab2-3 main.c

#include <stdlib.h>
#include <stdio.h>
int main(void)
{ 
    char *line = NULL;
    size_t size = 0;
    /* The loop below leaks memory as fast as it can */
    for(;;) { 
        getline(&line, &size, stdin); /* New memory implicitly allocated */
        /* <do whatever> */
        line = NULL;
    }
    return 0;
 }
    