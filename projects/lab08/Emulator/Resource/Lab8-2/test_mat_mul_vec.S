.data
A:
.byte 0 1 2 3 4 5 0 0

B:
.byte 0 1 2 0 0 0 0 0
.byte 3 4 5 0 0 0 0 0

C:
.byte 0 0 0 0 0 0 0 0
.byte 0 0 0 0 0 0 0 0
.byte 0 0 0 0 0 0 0 0

.text
## A -> matrix size MxK (input)
## B -> matrix size KxN (input)
## C -> matrix size MxN (output result)
## s0 -> base address of A
## s1 -> base address of B
## s2 -> base address of C
la s0,A
la s1,B
la s2,C

## a0 -> dimM -->i 
## a1 -> dimK -->k
li a0,3
li a1,2

## parameter initialize
## t1 -> k
li t1,0

loop_k:
bge t1,a1,end
## parameter initialize
## t0 -> i
li t0,0
jal loop_i
## k++
addi t1,t1,1
j loop_k

loop_i:
bge t0,a0,return
## i*dimK     ->(for A matrix)
mul t2,t0,a1
## i*dimK + k ->(get offset)
add t2,t2,t1
## base address of A + offset
add t2,s0,t2
## get data
lb t2,0(t2)

## offset = k*8 ->(for B matrix vector operation)
## dimK -->k
## B -> matrix size KxN (input)
## t1 -> k
addi t3,x0,8
mul t3,t1,t3
## base address of B + offset
add t3,t3,s1
vle8.v v1,(t3),0
##test
##addi t2,x0,1
##vmul.vx v3,v1,t2,0
##
vmul.vx v1,v1,t2,0

## offset = M*8 ->(for C matrix vector operation)
## dimM -->i 
## C -> matrix size MxN (output result)
## t0 -> i
addi t3,x0,8
mul t3,t0,t3
## base address of C + offset
add t3,t3,s2
vle8.v v2,(t3),0
vadd.vv v2,v1,v2,0
vse8.v v2,(t3),0

vle8.v v20,(s2),0
addi s2,s2,8
vle8.v v21,(s2),0
addi s2,s2,8
vle8.v v22,(s2),0
addi s2,s2,-16

## i++
addi t0,t0,1
j loop_i

return:
ret

end:
vle8.v v20,(s2),0
addi s2,s2,8
vle8.v v21,(s2),0
addi s2,s2,8
vle8.v v22,(s2),0
addi s2,s2,-16
hcf
