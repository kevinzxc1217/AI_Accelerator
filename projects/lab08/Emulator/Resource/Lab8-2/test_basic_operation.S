## Testing Assembly for Emulator with Vector Extension

.data
A:
    .byte	0 1 2 3 4 5 6 7

B:
    .byte	0 1 2 3 4 5 6 7

C:
    .byte	0 0 0 0 0 0 0 0

.text
main:
la a0,A
vle8.v v1,(a0),0

la a1,C
vse8.v v1,(a1),0
vle8.v v2,(a1),0

vadd.vv v3,v1,v2,0

li t4,4
vmul.vx v4,v1,t4,0

hcf
