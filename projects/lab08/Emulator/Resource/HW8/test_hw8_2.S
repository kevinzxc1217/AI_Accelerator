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
## t0 -> i
## t1 -> j
## t2 -> m
## t3 -> n
li s8,0
li t0,0
li t1,0
li t2,0
li t3,0


## other 未用t5
## t4 -> N[index]
## t6 -> M[index]
## s7-> P[index]
## s10 -> buffer
## s9 -> 8
li s7,0
li s11,0
li t6,0
li t4,0
li s9,8
li s10,0

# loop_i for loop
loop_i:
bge t0,s4,epilogue
li t2,0
jal loop_m
addi t0,t0,1
j loop_i

# loop_m for loop
loop_m:
bge t2,s5,return
addi sp,sp,-4
sw ra,0(sp)
li t3,0
jal loop_n
lw ra,0(sp)
addi sp,sp,4
addi t2,t2,1
j loop_m

## loop_n for loop
loop_n:
bge t3,s5,return
addi sp,sp,-4
sw ra,0(sp)
li s8,0
jal loop_cc
lw ra,0(sp)
addi sp,sp,4
addi t3,t3,1
j loop_n


## loop_cc for loop
loop_cc:
bge s8,s6,return

## N[cc][i+m][n+x] = N[cc][i+m][n開始](vec) = N長 * cc + (i+m) + n
## t4                                        s3   s8      t0t2   t3
mul t4,s3,s8
add t4,t4,t0
add t4,t4,t2
mul t4,t4,s9
add t4,s0,t4
add t4,t4,t3
vle8.v v1,(t4),0

## M[cc][m][n] = M長 * M寬 * cc + m * M寬 + n
## t6            s5    s5    s8   t2  s5    t3
mul s10,s5,s5
mul s10,s10,s8
mul t6,t2,s5
add t6,t6,s10
add s10,t6,t3
add t6,s1,s10
lb t6,0(t6)


## P += N * M
vmul.vx v1,v1,t6,0

## P[i](vec) = i
## s7          t0 
mul s7,t0,s9
add s7,s7,s2
vle8.v v2,(s7),0
vadd.vv v2,v1,v2,0
vse8.v v2,(s7),0

## cc++
addi s8,s8,1
j loop_cc

return:
ret

epilogue:
vle8.v v20,(s2),0
addi s2,s2,8
vle8.v v21,(s2),0
addi s2,s2,8
vle8.v v22,(s2),0
addi s2,s2,8
vle8.v v23,(s2),0
addi s2,s2,8
vle8.v v24,(s2),0
addi s2,s2,8
vle8.v v25,(s2),0
addi s2,s2,8
vle8.v v26,(s2),0
addi s2,s2,-48
hcf
## Terminate