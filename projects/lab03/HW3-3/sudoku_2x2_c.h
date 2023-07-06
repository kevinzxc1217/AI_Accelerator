#ifndef sudoku_2x2_c_h
#define sudoku_2x2_c_h
void sudoku_2x2_c(char *test_c_data);

void sudoku_2x2_asm(char* test_asm_data); // TODO, sudoku_2x2_asm.S

int solve(int index, char* set);

int check(int index, char* set);

int col(int index, char* set);

int row(int index, char* set);

int box(int index, char* set);
#endif