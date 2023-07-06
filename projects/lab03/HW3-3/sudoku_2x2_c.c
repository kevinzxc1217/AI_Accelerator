#include <stdio.h>
#include <stdlib.h>
#include <stdbool.h> 
#include "sudoku_2x2_c.h"
void sudoku_2x2_c(char* test_c_data) {
     solve(0, test_c_data);
}


int solve(int index, char* set) {
    if (index >= 16) {
        return 1;                                 // 如果檢查完所有的格子，回傳 True
    }
    if (set[index] > 0) {                          // set是一個儲存所有資料的array
        return solve(index + 1, set);                       // 如果格子中已經有值了則會往下一格判斷
    }

    else {
        for (int n = 1; n <= 4; n++) {                         // 判斷目前這格在 1~4是否有符合條件
            set[index] = n;                                   // 如果有的話就往下一格作判斷（遞迴）
            // 直到每一格都符合條件為止

            if (check(index,set) && solve(index + 1,set))  //DFS check function用來檢查當前這格放入這個數值是否正確
                //全都對，終止
                return 1;                         // solve(index+1) function則是繼續判斷下一格的值     
        }
        set[index] = 0;                                  // returns the value to 0 to mark it as empty
        //return 0 至 check
        return 0;
    }
                                   // no solution
}

int check(int index,char *set)
{
    if (col(index, set) && row(index, set) && box(index, set))
    {
        return 1;
    }
    else
    {
        return 0;
    }
}

int col(int index, char* set)
{
    int col_num = index % 4;
    for (int i = col_num ; i <= col_num +12; i = i + 4)
    {
        if (i != index)
        {
            if (set[index] == set[i])
            {
                return 0;
            }
        }
    }
    return 1;
}


int row(int index, char* set)
{
    int row_num = (index / 4) * 4;
    int i;
    for (i = row_num; i < row_num + 4; i++)
    {
        if (i != index)
        {
            if (set[index] == set[i])
            {
                return 0;
            }
        }
    }

    return 1;
}


int box(int index, char* set)
{
    int box_num = 0;
    int box[16] = { 0,1,4,5,    2,3,6,7,
                    8,9,12,13,  10,11,14,15 };
    for (int i = 0; i < 16; i++)
    {
        if (index == box[i])
        {
            box_num = (i / 4) * 4;//找區間
            break;
        }
    }
    for (int i = box_num; i < box_num + 4; i++)
    {

        if (box[i] != index)
        {
            if (set[index] == set[box[i]])
            {
                return 0;
            }
        }
    }

    return 1;
}