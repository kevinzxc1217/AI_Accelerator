.text
main:
addi x1,x1,1
addi x2,x2,2
add x1,x1,x2
vadd.vv v3,v1,v2,0
vadd.vv v3,v1,v2,0
vmul.vx v3,v1,x2,0
add x1,x1,x2
hcf
