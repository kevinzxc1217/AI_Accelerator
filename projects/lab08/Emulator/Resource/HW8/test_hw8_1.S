.data 
## input data size = 2x8x8 
input_data:
.byte 0 1 2 3 4 5 6 7
.byte 0 1 2 3 4 5 6 7
.byte 0 1 2 3 4 5 6 7
.byte 0 1 2 3 4 5 6 7
.byte 0 1 2 3 4 5 6 7
.byte 0 1 2 3 4 5 6 7
.byte 0 1 2 3 4 5 6 7
.byte 0 1 2 3 4 5 6 7

.byte 0 1 2 3 4 5 6 7
.byte 0 1 2 3 4 5 6 7
.byte 0 1 2 3 4 5 6 7
.byte 0 1 2 3 4 5 6 7
.byte 0 1 2 3 4 5 6 7
.byte 0 1 2 3 4 5 6 7
.byte 0 1 2 3 4 5 6 7
.byte 0 1 2 3 4 5 6 7

## kernel size 2x3x3
kernel_data:
.byte 0 1 2
.byte 0 1 2
.byte 0 1 2

.byte 0 1 2
.byte 0 1 2
.byte 0 1 2

## output data size 1x6x6
output_data:
.byte 0 0 0 0 0 0 0 0
.byte 0 0 0 0 0 0 0 0
.byte 0 0 0 0 0 0 0 0
.byte 0 0 0 0 0 0 0 0
.byte 0 0 0 0 0 0 0 0
.byte 0 0 0 0 0 0 0 0

.text

## N -> matrix size CHANNEL_SIZE x INPUT_SIZE x INPUT_SIZE (input)
## M -> matrix size CHANNEL_SIZE x KERNEL_SIZE x KERNEL_SIZE (input)
## P -> matrix size CHANNEL_SIZE x OUTPUT_SIZE (output result)

main:
prologue:
li sp,0x10000

## testing parameter//
## s0 -> base address of N
## s1 -> base address of M
## s2 -> base address of P
la s0,input_data
la s1,kernel_data
la s2,output_data

## s3 -> INPUT_SIZE
## s4 -> OUTPUT_SIZE
## s5 -> KERNEL_SIZE
## s6 -> CHANNEL_SIZE
li s3,8
li s4,6
li s5,3
li s6,2

## parameter initialize
## s8 -> cc
## s9 -> ii
## s10 -> jj
## t0 -> i
## t1 -> j
## t2 -> m
## t3 -> n
li s8,0
li s9,0
li s10,0
li t0,0
li t1,0
li t2,0
li t3,0


## other 未用t4
## t5 -> kernel_center =  KERNEL_SIZE / 2
## s7 -> N[index]
## s11 -> M[index]
## t6-> P[index]
## t4 -> buffer
srli t5,s5,1
li s7,0
li s11,0
li t6,0
li t4,0

# OUTPUT_SIZE_row for loop
OUTPUT_SIZE_row:
bge t0,s4,epilogue
li t1,0
jal OUTPUT_SIZE_col
addi t0,t0,1
j OUTPUT_SIZE_row

## OUTPUT_SIZE_col for loop
OUTPUT_SIZE_col:
## 超過範圍跳出迴圈
bge t1,s4,return
## 每次跳都要先存原本for的addr
addi sp,sp,-4
sw ra,0(sp)
## 下個for的index重置為0
li t2,0
## 跳到下個for
jal KERNEL_SIZE_row
## 下個for結束後，return回來
lw ra,0(sp)
## 刪除該回stack內的addr
addi sp,sp,4
## 該for迴圈的index+1
addi t1,t1,1
j OUTPUT_SIZE_col

# KERNEL_SIZE_row for loop
KERNEL_SIZE_row:
bge t2,s5,return
addi sp,sp,-4
sw ra,0(sp)
li t3,0
jal KERNEL_SIZE_col
lw ra,0(sp)
addi sp,sp,4
addi t2,t2,1
j KERNEL_SIZE_row

## KERNEL_SIZE_col for loop
KERNEL_SIZE_col:
bge t3,s5,return
addi sp,sp,-4
sw ra,0(sp)
li s8,0
jal CHANNEL_SIZE
lw ra,0(sp)
addi sp,sp,4
addi t3,t3,1
j KERNEL_SIZE_col


## CHANNEL_SIZE for loop
CHANNEL_SIZE:
bge s8,s6,return
addi sp,sp,-4
sw ra,0(sp)
jal mult
lw ra,0(sp)
addi sp,sp,4
addi s8,s8,1
j CHANNEL_SIZE

## calculate
mult:
## ii = (m - kernel_center)
sub s9,t2,t5
## ii = i + (m - kernel_center)
add s9,s9,t0
# ii = i + (m - kernel_center) +1;
addi s9,s9,1 

## jj = (n - kernel_center)
sub s10,t3,t5
## jj = j + (n - kernel_center)
add s10,s10,t1
## jj = j + (n - kernel_center) +1;
addi s10,s10,1

## P[i][j] += N[cc][ii][jj] * M[cc][m][n];
# s7         s11             t6
## N[cc][ii][jj] = N長 * N寬 * cc + ii * N寬 + jj 
## s11             s3     s3    s8   s9   s3    s10
mul t4,s3,s3
mul t4,t4,s8
mul s11,s9,s3
add s11,s11,t4
add s11,s11,s10
add s11,s0,s11
lb s11,0(s11)

## M[cc][m][n] = M長 * M寬 * cc + m * M寬 + n
## t6            s5    s5    s8   t2  s5    t3
mul t4,s5,s5
mul t4,t4,s8
mul t6,t2,s5
add t6,t6,t4
add t6,t6,t3
add t6,s1,t6
lb t6,0(t6)

mul t4,s11,t6

## P[i][j] = P寬 * i + j
## s2        s4    t0  t1
mul s7,s4,t0
add s7,s7,t1
add s7,s2,s7
lb s11,0(s7)
add s11,t4,s11
sb s11,0(s7)
ret

return:
ret

epilogue:
hcf
## Terminate