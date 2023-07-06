#include <stdio.h>
#include <stdlib.h>
#include <stdbool.h> 
#include "sudoku_2x2_c.h"
void sudoku_2x2_c(char* test_c_data) {
     solve(0, test_c_data);
}


int solve(int index, char* set) {
    if (index >= 16) {
        return 1;                                 // �p�G�ˬd���Ҧ�����l�A�^�� True
    }
    if (set[index] > 0) {                          // set�O�@���x�s�Ҧ���ƪ�array
        return solve(index + 1, set);                       // �p�G��l���w�g���ȤF�h�|���U�@��P�_
    }

    else {
        for (int n = 1; n <= 4; n++) {                         // �P�_�ثe�o��b 1~4�O�_���ŦX����
            set[index] = n;                                   // �p�G�����ܴN���U�@��@�P�_�]���j�^
            // ����C�@�泣�ŦX���󬰤�

            if (check(index,set) && solve(index + 1,set))  //DFS check function�Ψ��ˬd��e�o���J�o�ӼƭȬO�_���T
                //������A�פ�
                return 1;                         // solve(index+1) function�h�O�~��P�_�U�@�檺��     
        }
        set[index] = 0;                                  // returns the value to 0 to mark it as empty
        //return 0 �� check
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
            box_num = (i / 4) * 4;//��϶�
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