## Lab 9-3-4 Software Design
## Testing assembly code to check whether CPU works correctly in Lab9-3

.data
## Martix A, B, C data as usual
mat_A:
#r1
.byte 1 0 0 0 
.byte 0 1 0 0 
.byte 0 0 1 0 
.byte 0 0 0 1

.byte 3 0 0 0
.byte 0 3 0 0
.byte 0 0 3 0
.byte 0 0 0 3

.byte 5 0 0 0
.byte 0 5 0 0
.byte 0 0 5 0
.byte 0 0 0 5

.byte 7 0 0 0
.byte 0 7 0 0
.byte 0 0 7 0
.byte 0 0 0 7
##r2
.byte 2 0 0 0
.byte 0 2 0 0
.byte 0 0 2 0
.byte 0 0 0 2

.byte 4 0 0 0 
.byte 0 4 0 0 
.byte 0 0 4 0 
.byte 0 0 0 4

.byte 6 0 0 0
.byte 0 6 0 0
.byte 0 0 6 0
.byte 0 0 0 6

.byte 8 0 0 0
.byte 0 8 0 0
.byte 0 0 8 0
.byte 0 0 0 8
##r3
.byte 4 0 0 0
.byte 0 4 0 0
.byte 0 0 4 0
.byte 0 0 0 4

.byte 3 0 0 0
.byte 0 3 0 0
.byte 0 0 3 0
.byte 0 0 0 3

.byte 2 0 0 0 
.byte 0 2 0 0 
.byte 0 0 2 0 
.byte 0 0 0 2

.byte 1 0 0 0
.byte 0 1 0 0
.byte 0 0 1 0
.byte 0 0 0 1
##r4
.byte 1 0 0 0
.byte 0 1 0 0
.byte 0 0 1 0
.byte 0 0 0 1

.byte 2 0 0 0
.byte 0 2 0 0
.byte 0 0 2 0
.byte 0 0 0 2

.byte 3 0 0 0
.byte 0 3 0 0
.byte 0 0 3 0
.byte 0 0 0 3

.byte 4 0 0 0 
.byte 0 4 0 0 
.byte 0 0 4 0 
.byte 0 0 0 4

mat_B:
#c1
.byte 2 0 0 0 
.byte 0 2 0 0 
.byte 0 0 2 0 
.byte 0 0 0 2

.byte 4 0 0 0
.byte 0 4 0 0
.byte 0 0 4 0
.byte 0 0 0 4

.byte 6 0 0 0
.byte 0 6 0 0
.byte 0 0 6 0
.byte 0 0 0 6

.byte 8 0 0 0
.byte 0 8 0 0
.byte 0 0 8 0
.byte 0 0 0 8
##c2
.byte 1 0 0 0
.byte 0 1 0 0
.byte 0 0 1 0
.byte 0 0 0 1

.byte 3 0 0 0 
.byte 0 3 0 0 
.byte 0 0 3 0 
.byte 0 0 0 3

.byte 5 0 0 0
.byte 0 5 0 0
.byte 0 0 5 0
.byte 0 0 0 5

.byte 7 0 0 0
.byte 0 7 0 0
.byte 0 0 7 0
.byte 0 0 0 7
##c3
.byte 1 0 0 0
.byte 0 1 0 0
.byte 0 0 1 0
.byte 0 0 0 1

.byte 2 0 0 0
.byte 0 2 0 0
.byte 0 0 2 0
.byte 0 0 0 2

.byte 3 0 0 0 
.byte 0 3 0 0 
.byte 0 0 3 0 
.byte 0 0 0 3

.byte 4 0 0 0
.byte 0 4 0 0
.byte 0 0 4 0
.byte 0 0 0 4
##c4
.byte 4 0 0 0
.byte 0 4 0 0
.byte 0 0 4 0
.byte 0 0 0 4

.byte 3 0 0 0
.byte 0 3 0 0
.byte 0 0 3 0
.byte 0 0 0 3

.byte 2 0 0 0
.byte 0 2 0 0
.byte 0 0 2 0
.byte 0 0 0 2

.byte 1 0 0 0 
.byte 0 1 0 0 
.byte 0 0 1 0 
.byte 0 0 0 1


mat_C:
#r1
.byte 0 0 0 0 
.byte 0 0 0 0 
.byte 0 0 0 0 
.byte 0 0 0 0

.byte 0 0 0 0
.byte 0 0 0 0
.byte 0 0 0 0
.byte 0 0 0 0

.byte 0 0 0 0
.byte 0 0 0 0
.byte 0 0 0 0
.byte 0 0 0 0

.byte 0 0 0 0
.byte 0 0 0 0
.byte 0 0 0 0
.byte 0 0 0 0
##r2
.byte 0 0 0 0
.byte 0 0 0 0
.byte 0 0 0 0
.byte 0 0 0 0

.byte 0 0 0 0 
.byte 0 0 0 0 
.byte 0 0 0 0 
.byte 0 0 0 0

.byte 0 0 0 0
.byte 0 0 0 0
.byte 0 0 0 0
.byte 0 0 0 0

.byte 0 0 0 0
.byte 0 0 0 0
.byte 0 0 0 0
.byte 0 0 0 0
##r3
.byte 0 0 0 0
.byte 0 0 0 0
.byte 0 0 0 0
.byte 0 0 0 0

.byte 0 0 0 0
.byte 0 0 0 0
.byte 0 0 0 0
.byte 0 0 0 0

.byte 0 0 0 0 
.byte 0 0 0 0 
.byte 0 0 0 0 
.byte 0 0 0 0

.byte 0 0 0 0
.byte 0 0 0 0
.byte 0 0 0 0
.byte 0 0 0 0
##r4
.byte 0 0 0 0
.byte 0 0 0 0
.byte 0 0 0 0
.byte 0 0 0 0

.byte 0 0 0 0
.byte 0 0 0 0
.byte 0 0 0 0
.byte 0 0 0 0

.byte 0 0 0 0
.byte 0 0 0 0
.byte 0 0 0 0
.byte 0 0 0 0

.byte 0 0 0 0 
.byte 0 0 0 0 
.byte 0 0 0 0 
.byte 0 0 0 0

bufffer:
.byte 0 0 0 0 
.byte 0 0 0 0 
.byte 0 0 0 0 
.byte 0 0 0 0

# 幫助清0
zero:
.byte 0 0 0 0 
.byte 0 0 0 0 
.byte 0 0 0 0 
.byte 0 0 0 0

## define parameter for memory space which accelerator can access
ACCEL_REG_BASE_ADDR:
.word 0x100000

ACCEL_MEM_BASE_ADDR:
.word 0x8000

REG_ROFFSET_ENABLE:
.word 0x0

REG_ROFFSET_STATUS:
.word 0x4

REG_OFFSET_MATA_MEM_ADDR:
.word 0x14

REG_OFFSET_MATB_MEM_ADDR:
.word 0x18

REG_OFFSET_MATC_MEM_ADDR:
.word 0x1c

REG_OFFSET_MAT_MEM_STRIDE:
.word 0x20



## inst start
.text
## s0 -> base address of A
la s0,mat_A

## s1 -> base address of B
la s1,mat_B

## s2 -> base address of C
la s2,mat_C

la s9,bufffer

la s10,zero

## s3 -> dimM
#li s3,4
li s3,16

## s4 -> dimK
#li s4,4
li s4,16

## s5 -> dimN
#li s5,4
li s5,16

## S6 -> 計次
li s6,0

li s8,0

######################
##                  ##
##      Step 1      ##
##                  ##
######################
## t0 for addrA which accelerator can access in SRAM
## addrA = ACCEL_MEM_BASE_ADDR + 0
la t6,ACCEL_MEM_BASE_ADDR
lw t0,0(t6)


## t1 for addrB which accelerator can access in SRAM
## calculate the size of matrix A
mul t3,s3,s4 					
## addrB = ACCEL_MEM_BASE_ADDR + M*K
add t1,t0,t3



## t2 for addrC which accelerator can access in SRAM
## calculate the size of matrix B
mul t3,s4,s5 				
## addrC = ACCEL_MEM_BASE_ADDR + M*K + K*N	
add t2,t1,t3					



## copy Matrix A to addrA 
## t3 for size of Matrix A = 4*4
mul t3,s3,s4                    
## t4 for offset
li t4,0                         
loop_A:
add t5,s0,t4                    
vle8_v v1,0(t5)
add t5,t0,t4
vse8_v v1,0(t5)
addi t4,t4,8
blt t4,t3,loop_A

## copy Matrix B to addrB 
## t3 for size of Matrix B = 4*4
mul t3,s4,s5  
## t4 for offset                  
li t4,0                         
loop_B:
add t5,s1,t4                    
vle8_v v2,0(t5)
add t5,t1,t4
vse8_v v2,0(t5)
addi t4,t4,8
blt t4,t3,loop_B

######################
##                  ##
##      Step 2      ##
##                  ##
######################
## After doing this, we move the original data in SRAM which "accelerator cannot access" to the memory space "accelerator can access".
## And the next thing we need to do is to write the information into ACCEL_REG to make sure that accelerator can work with correct spec.
BEGIN:
## t3 for REG_BASE_ADDR
la t6,ACCEL_REG_BASE_ADDR
lw t3,0(t6)



## t4 for REGADDR_MATA_MEM_ADDR (the register for accelerator)
la t6,REG_OFFSET_MATA_MEM_ADDR       
lw t4,0(t6)
add t4,t3,t4


## put addrA into REGADDR_MATA_MEM_ADDR
# 傳給mmio.addrA
sw t0,0(t4)                          

## t4 for REGADDR_MATB_MEM_ADDR (the register for accelerator)
la t6,REG_OFFSET_MATB_MEM_ADDR       
lw t4,0(t6)
add t4,t3,t4
## put addrB into REGADDR_MATB_MEM_ADDR
sw t1,0(t4)                          

## t4 for REGADDR_MATC_MEM_ADDR (the register for accelerator)
la t6,REG_OFFSET_MATC_MEM_ADDR       
lw t4,0(t6)
add t4,t3,t4
## put addrC into REGADDR_MATC_MEM_ADDR
sw t2,0(t4)                          

######################
##                  ##
##      Step 3      ##
##                  ##
######################
## information ready, pull up the ENABLE signal 
## t4 for REG_ROFFSET_ENABLE



la t6,REG_ROFFSET_ENABLE             
lw t4,0(t6)   
add t4,t3,t4
li t5,1
## set ENABLE
sw t5,0(t4)                          

## ... accelerator processing ...

## CPU hanging til the accelerator pull up the DONE signal
hang:
## t4 for REG_ROFFSET_STATUS
la t6,REG_ROFFSET_STATUS             
lw t4,0(t6)  
add t4,t3,t4
lw t5,0(t4)                          
## load STATUS, check "DONE" signal 
li t6,1


######################
##                  ##
##      Step 4      ##
##                  ##
######################
## CPU waiting for STATUS.Done signal.
## In software simulation, we will use emulator.cpp to write STATUS.Done signal automatically after detecting ENABLE signal.
## Notice that 

## If (t5 == t6) means it's done!
beq t5,t6,FINISH                     
j hang


######################
##                  ##
##      Step 5      ##
##                  ##
######################
FINISH:
## turn down the status reg
sw zero,0(t4)


# 讀write值
read_write_data:

# v3 v4存write的data
vle8_v v3,0(t2)
vle8_v v4,8(t2)

# v5 v6存buffer的值
vle8_v v5,0(s9)
vle8_v v6,8(s9)

# buffer累加
vadd_vv v5,v5,v3
vadd_vv v6,v6,v4

# 存回去buffer
vse8_v v5,0(s9)
vse8_v v6,8(s9)


# 計次
addi s6,s6,1
# mmio.addr往下加16
add t0,t0,s3
add t1,t1,s3


# 計次方塊內運算上限
li t6,4
beq s6,t6,next_C
j BEGIN

# s6 -> 計次方塊內運算
# s8 -> 計次完成幾個方塊

# 完成一個方塊內運算
next_C:

# 將buffer值放到C內
vle8_v v7,0(s9)
vle8_v v8,8(s9)

vse8_v v7,0(t2)
vse8_v v8,8(t2)


# 計次完成幾個方塊+1
addi s8,s8,1

# 計次方塊內運算清0
li s6,0

# 終止條件
li t6,16
beq s8,t6,END

li t6,12
beq s8,t6,NEXT_ROW_A
li t6,8
beq s8,t6,NEXT_ROW_A
li t6,4
beq s8,t6,NEXT_ROW_A


# mmio.a_Addr回到第一個矩陣
addi t0,t0,-64


# mmio.write往下加16
add t2,t2,s3


# 初始化buffer
vle8_v v5,0(s10)
vle8_v v6,8(s10)

vse8_v v5,0(s9)
vse8_v v6,8(s9)

j BEGIN


NEXT_ROW_A:

# mmio.B_Addr回到第一個矩陣
addi t1,t1,-256

# mmio.write往下加16
add t2,t2,s3

# 初始化buffer
vle8_v v5,0(s10)
vle8_v v6,8(s10)

vse8_v v5,0(s9)
vse8_v v6,8(s9)

j BEGIN

END:
add t2,t2,s3
addi t2,t2,-256
vle8_v v0,0(t2)
vle8_v v1,8(t2)

add t2,t2,s3
vle8_v v2,0(t2)
vle8_v v3,8(t2)

add t2,t2,s3
vle8_v v4,0(t2)
vle8_v v5,8(t2)

add t2,t2,s3
vle8_v v6,0(t2)
vle8_v v7,8(t2)

add t2,t2,s3
vle8_v v8,0(t2)
vle8_v v9,8(t2)

add t2,t2,s3
vle8_v v10,0(t2)
vle8_v v11,8(t2)

add t2,t2,s3
vle8_v v12,0(t2)
vle8_v v13,8(t2)

add t2,t2,s3
vle8_v v14,0(t2)
vle8_v v15,8(t2)

add t2,t2,s3
vle8_v v16,0(t2)
vle8_v v17,8(t2)

add t2,t2,s3
vle8_v v18,0(t2)
vle8_v v19,8(t2)

add t2,t2,s3
vle8_v v20,0(t2)
vle8_v v21,8(t2)

add t2,t2,s3
vle8_v v22,0(t2)
vle8_v v23,8(t2)

add t2,t2,s3
vle8_v v24,0(t2)
vle8_v v25,8(t2)

add t2,t2,s3
vle8_v v26,0(t2)
vle8_v v27,8(t2)

add t2,t2,s3
vle8_v v28,0(t2)
vle8_v v29,8(t2)

add t2,t2,s3
vle8_v v30,0(t2)
vle8_v v31,8(t2)

# ## calculate the base addr of mat_A in Accelerator
# la t4,ACCEL_MEM_BASE_ADDR
# lw t4,0(t4)

# ## calculate the base addr of mat_B in Accelerator
# mul t3,s3,s4
# add t4,t4,t3
# ## calculate the base addr of mat_C in Accelerator
# mul t3,s4,s5
# add t4,t4,t3

## load data from ACCEL_MEM_BASE_ADDR to chekout whether the data transfer is correct
#vle8_v v1,0(t2)
#addi t2,t2,8     
#vle8_v v2,0(t2)						
## load data from ACCEL_REG_BASE_ADDR to checkout the control signal is correct
# vle8_v v3,0(t3)


# ## load data from ACCEL_MEM_BASE_ADDR to reg(s0~3) to chekout whether the data transfer is correct
# lw s2,0(t2)
# lw s3,4(t2)
# lw s4,8(t2)
# lw s5,12(t2)



hcf