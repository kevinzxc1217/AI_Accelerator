lui x08, 0x00000008
addi x08, x08, 0x00000000
lui x09, 0x00000008
addi x09, x09, 0x00000050
lui x18, 0x00000008
addi x18, x18, 0x000000f0
lui x25, 0x00000008
addi x25, x25, 0x00000110
lui x26, 0x00000008
addi x26, x26, 0x00000120
lui x19, 0x00000000
addi x19, x19, 0x00000004
lui x20, 0x00000000
addi x20, x20, 0x00000014
lui x21, 0x00000000
addi x21, x21, 0x00000008
lui x22, 0x00000000
addi x22, x22, 0x00000000
lui x24, 0x00000000
addi x24, x24, 0x00000000
lui x23, 0x00000000
addi x23, x23, 0x00000010
lui x31, 0x00000008
addi x31, x31, 0x00000134
lw t0, 0(t6)
mul t3, s3, s4
add t1, t0, t3
mul t3, s4, s5
add t2, t1, t3
mul t3, s3, s5
lui x29, 0x00000000
addi x29, x29, 0x00000000
add t5, s0, t4
vle8_v v1, 0(t5)
add t5, t0, t4
vse8_v v1, 0(t5)
addi t4, t4, 8
blt t4, t3, loop_a
mul t3, s4, s5
lui x29, 0x00000000
addi x29, x29, 0x00000000
add t5, s1, t4
vle8_v v2, 0(t5)
add t5, t1, t4
vse8_v v2, 0(t5)
addi t4, t4, 8
blt t4, t3, loop_b
lui x31, 0x00000008
addi x31, x31, 0x00000130
lw t3, 0(t6)
lui x31, 0x00000008
addi x31, x31, 0x00000140
lw t4, 0(t6)
add t4, t3, t4
sw t0, 0(t4)
lui x31, 0x00000008
addi x31, x31, 0x00000144
lw t4, 0(t6)
add t4, t3, t4
sw t1, 0(t4)
lui x31, 0x00000008
addi x31, x31, 0x00000148
lw t4, 0(t6)
add t4, t3, t4
sw t2, 0(t4)
lui x31, 0x00000008
addi x31, x31, 0x00000138
lw t4, 0(t6)
add t4, t3, t4
lui x30, 0x00000000
addi x30, x30, 0x00000001
sw t5, 0(t4)
lui x31, 0x00000008
addi x31, x31, 0x0000013c
lw t4, 0(t6)
add t4, t3, t4
lw t5, 0(t4)
lui x31, 0x00000000
addi x31, x31, 0x00000001
beq t5, t6, finish
j x0, hang
sw zero, 0(t4)
vle8_v v3, 0(t2)
vle8_v v4, 8(t2)
vle8_v v5, 0(s9)
vle8_v v6, 8(s9)
vadd_vv v5, v5, v3
vadd_vv v6, v6, v4
vse8_v v5, 0(s9)
vse8_v v6, 8(s9)
addi s6, s6, 1
add t0, t0, s7
add t1, t1, s7
lui x31, 0x00000000
addi x31, x31, 0x00000005
beq s6, t6, next_c
j x0, begin
vle8_v v7, 0(s9)
vle8_v v8, 8(s9)
vse8_v v7, 0(t2)
vse8_v v8, 8(t2)
addi s8, s8, 1
lui x22, 0x00000000
addi x22, x22, 0x00000000
lui x31, 0x00000000
addi x31, x31, 0x00000002
beq s8, t6, end
addi t0, t0, -80
add t2, t2, s7
vle8_v v5, 0(s10)
vle8_v v6, 8(s10)
vse8_v v5, 0(s9)
vse8_v v6, 8(s9)
j x0, begin
lb t3, 0(t2)
lb t4, 1(t2)
addi t2, t2, -16
sb t3, 4(t2)
sb t4, 5(t2)
vle8_v v0, 0(t2)
vle8_v v1, 8(t2)
hcf