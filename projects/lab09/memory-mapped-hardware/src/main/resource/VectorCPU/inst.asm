lui x14, 0x00000000
addi x14, x14, 0x00000003
lui x15, 0x00000000
addi x15, x15, 0x00000003
lui x16, 0x00000000
addi x16, x16, 0x00000002
lui x08, 0x00000000
addi x08, x08, 0x00000006
lui x09, 0x00000000
addi x09, x09, 0x00000006
lui x18, 0x00000008
addi x18, x18, 0x00000000
lui x19, 0x00000008
addi x19, x19, 0x00000288
lui x20, 0x00000008
addi x20, x20, 0x0000029a
addi sp, sp, -324
addi s5, sp
addi t0, x0, 324
addi t1, x0, 0
bge t1, t0, for_loop_1_prologue
add t2, s5, t1
sb x0, 0(t2)
addi t1, t1, 1
j x0, initialize_mac_result_matrix
mul t0, s0, s1
addi t1, x0, 0
addi sp, sp, -8
addi s6, sp
sw x0, 4(s6)
sw x0, 0(s6)
bge t1, t0, for_loop_1_end
vle8_v v1, 0(s6)
addi t2, x0, 0
addi t3, x0, 0
bge t3, a6, for_loop_2_end
mul t4, t0, t3
add t4, t4, t1
mul t5, a4, a5
mul t4, t4, t5
add t4, s2, t4
vle8_v v2, 0(t4)
mul t4, t5, t3
add t4, s3, t4
vle8_v v3, 0(t4)
vmacc_vv v1, v2, v3
mul t4, a4, a5
mul t5, t0, t4
mul t5, t5, t3
mul t6, t4, t1
add t5, t5, t6
addi t5, t5, 8
add t5, s2, t5
lb t5, 0(t5)
mul t6, t4, t3
addi t6, t6, 8
add t6, s3, t6
lb t6, 0(t6)
mul t5, t5, t6
add t2, t2, t5
addi t3, t3, 1
j x0, for_loop_2_begin
mul t5, t4, t1
add t5, s5, t5
vse8_v v1, 0(t5)
sb t2, 8(t5)
addi t1, t1, 1
j x0, for_loop_1_begin
addi sp, sp, 8
addi t1, x0, 0
bge t1, t0, for_loop_3_end
mul t2, a4, a5
addi t3, x0, 0
bge t3, t2, for_loop_4_end
mul t4, t2, t1
add t4, t4, t3
add t4, s5, t4
lb t4, 0(t4)
add t5, s4, t1
lb t6, 0(t5)
add t6, t6, t4
sb t6, 0(t5)
addi t3, t3, 1
j x0, for_loop_4_begin
addi t1, t1, 1
j x0, for_loop_3_begin
addi sp, sp, 324
addi t0, s4
vle8_v v1, 0(t0)
addi t0, t0, 6
vle8_v v2, 0(t0)
addi t0, t0, 6
vle8_v v3, 0(t0)
addi t0, t0, 6
vle8_v v4, 0(t0)
addi t0, t0, 6
vle8_v v5, 0(t0)
addi t0, t0, 6
vle8_v v6, 0(t0)
hcf