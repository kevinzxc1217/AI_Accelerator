#include <iostream>
#include <stdio.h>
#include <iomanip>
using namespace std;

#define INPUT_SIZE 8
#define KERNEL_SIZE 3
#define OUTPUT_SIZE 6
#define CHANNEL_SIZE 2

// void convolution_2D(int N[][INPUT_SIZE][INPUT_SIZE], int M[][KERNEL_SIZE][KERNEL_SIZE], int P[][OUTPUT_SIZE]) {
// 	int kernel_center_X = KERNEL_SIZE / 2;
// 	int kernel_center_Y = KERNEL_SIZE / 2;

// 	for (int i = 0; i < OUTPUT_SIZE; ++i)              // rows
// 	{
// 	    for (int j = 0; j < OUTPUT_SIZE; ++j)          // columns
// 	    {
// 	        for (int m = 0; m < KERNEL_SIZE; ++m)     // kernel rows
// 	        {
// 	            for (int n = 0; n < KERNEL_SIZE; ++n) // kernel columns
// 	            {
//                     for (int cc = 0; cc < CHANNEL_SIZE; ++cc ){
//                         // Calculate index of input signal
//                         int ii = i + (m - kernel_center_X) +1; //i+(m-1)+1
//                         int jj = j + (n - kernel_center_Y) +1; //j+(n-1)+1

//                         P[i][j] += N[cc][ii][jj] * M[cc][m][n];

//                     }
// 	            }
// 	        }
//             // cout<<"\n";
// 	    }
// 	}
// }

void convolution_2D(int N[][INPUT_SIZE][INPUT_SIZE], int M[][KERNEL_SIZE][KERNEL_SIZE], int P[][OUTPUT_SIZE]) {

	for (int i = 0; i < OUTPUT_SIZE; ++i)              // rows
	{
        for (int m = 0; m < KERNEL_SIZE; ++m)     // kernel rows
        {
            for (int n = 0; n < KERNEL_SIZE; ++n) // kernel columns
            {
                for (int cc = 0; cc < 2; ++cc ){
                    // Calculate index of input signal
                //P[i][0] += N[cc][i+m][j+n] * M[cc][m][n];
                P[i][0] += N[cc][i+m][n] * M[cc][m][n];
                P[i][1] += N[cc][i+m][1+n] * M[cc][m][n];
                P[i][2] += N[cc][i+m][2+n] * M[cc][m][n];
                P[i][3] += N[cc][i+m][3+n] * M[cc][m][n];
                P[i][4] += N[cc][i+m][4+n] * M[cc][m][n];
                P[i][5] += N[cc][i+m][5+n] * M[cc][m][n];
                }
            }
        }
        // cout<<"\n";
	    
	}
}
// void convolution_2D(int N[][INPUT_SIZE][INPUT_SIZE], int M[][KERNEL_SIZE][KERNEL_SIZE], int P[][OUTPUT_SIZE]) {

//     for (int m = 0; m < KERNEL_SIZE; ++m)     // kernel rows
//     {
//         for (int n = 0; n < KERNEL_SIZE; ++n) // kernel columns
//         {
//             for (int cc = 0; cc < CHANNEL_SIZE; ++cc)
//             {
//                 for (int i = 0; i < OUTPUT_SIZE; ++i)              // rows
//                 {
//                     //P[i][0] += N[cc][i+m][j+n] * M[cc][m][n];
//                     P[i][0] += N[cc][i+m][n] * M[cc][m][n];
//                     P[i][1] += N[cc][i+m][1+n] * M[cc][m][n];
//                     P[i][2] += N[cc][i+m][2+n] * M[cc][m][n];
//                     P[i][3] += N[cc][i+m][3+n] * M[cc][m][n];
//                     P[i][4] += N[cc][i+m][4+n] * M[cc][m][n];
//                     P[i][5] += N[cc][i+m][5+n] * M[cc][m][n];
//                 }
//             }
//         }
//     }
//         // cout<<"\n";
// }

int main(){
    int   n[CHANNEL_SIZE][INPUT_SIZE ][INPUT_SIZE];
    int   m[CHANNEL_SIZE][INPUT_SIZE ][INPUT_SIZE];
    int ker[CHANNEL_SIZE][KERNEL_SIZE][KERNEL_SIZE];
    int ans[OUTPUT_SIZE][OUTPUT_SIZE];
    for(int c = 0; c<CHANNEL_SIZE;++c)
        for(int i=0; i<INPUT_SIZE;++i)
            for(int j=0;j<INPUT_SIZE;++j)
                n[c][i][j] = j, m[c][i][j] = j;

    for(int c = 0; c<CHANNEL_SIZE;++c)
        for(int i=0; i<KERNEL_SIZE;++i)
            for(int j=0;j<KERNEL_SIZE;++j)
                ker[c][i][j] = j;

    for(int i=0; i<OUTPUT_SIZE;++i)
        for(int j=0;j<OUTPUT_SIZE;++j)
            ans[i][j] = 0;


    //print
    printf("------ INPUT ------\n");
    for(int c = 0; c<CHANNEL_SIZE;++c){
        for(int i=0; i<INPUT_SIZE;++i){
            for(int j=0;j<INPUT_SIZE;++j)
                cout<<" "<<n[c][i][j];
            cout<<"\n";
        }
        cout<<"\n";
    }
    printf("\n------ KERNEL ------\n");
    for(int c = 0; c<CHANNEL_SIZE;++c){
        for(int i=0; i<KERNEL_SIZE;++i){
            for(int j=0;j<KERNEL_SIZE;++j)
                cout<< hex <<" "<<ker[c][i][j];
            cout<<"\n";
        }
        cout<<"\n";
    }
    printf("\n------ OUTPUT ------\n");
    for(int i=0; i<OUTPUT_SIZE;++i){
        for(int j=0;j<OUTPUT_SIZE;++j)
            cout<< hex <<" "<<ans[i][j];
        cout<<"\n";
    }
    
    // ------ CALCULATE ------
    convolution_2D(n,ker,ans);
    printf("\n------ ANSWER ------\n");
    for(int i=0; i<OUTPUT_SIZE;++i){
        for(int j=0;j<OUTPUT_SIZE;++j)
            cout<< hex <<" "<<ans[i][j];
        cout<<"\n";
    }
    return 0;
}
