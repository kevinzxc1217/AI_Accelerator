# sudoku_2x2_asm.S
   
    .text                           # code section 
    .global sudoku_2x2_asm          # declare the asm function as a global function
    .type sudoku_2x2_asm, @function # define sum_asm as a function 
sudoku_2x2_asm:

sudoku_2x2_c:
    #先存ra和s0到stack
    addi    sp,sp,-48
    sw      ra,44(sp)
    sw      s0,40(sp)
    #a0=0
    #a1=輸入矩陣之addr
    addi    s0,sp,48
    sw      a0,-36(s0)
    lw      a1,-36(s0)
    li      a0,0
    jal     ra,solve
    sw      a0,-20(s0)
    nop
    lw      ra,44(sp)
    lw      s0,40(sp)
    addi    sp,sp,48
    ret

solve:
    #a0:陣列addr
    #將原始a0的值放到a4內
    addi    sp,sp,-48
    sw      ra,44(sp)
    sw      s0,40(sp)
    addi    s0,sp,48
    sw      a0,-36(s0)
    sw      a1,-40(s0)
    lw      a4,-36(s0)
    #if (index >= 16) return 1
    li      a5,15
    bge     a5,a4,solve+0x2c
    li      a5,1
    j       solve+0xe4
    lw      a5,-36(s0)
    lw      a4,-40(s0)
    add     a5,a4,a5
    lbu     a5,0(a5)
    beqz    a5,solve+0x5c
    lw      a5,-36(s0)
    addi    a5,a5,1
    lw      a1,-40(s0)
    mv      a0,a5
    #if (set[index] > 0) return solve(index + 1, set);
    jal     ra,solve
    mv      a5,a0
    j       solve+0xe4
    #index + 1
    li      a5,1
    sw      a5,-20(s0)
    #solve(index + 1
    j       solve+0xc4
    lw      a5,-36(s0)
    lw      a4,-40(s0)
    add     a5,a4,a5
    lw      a4,-20(s0)
    zext.b  a4,a4
    sb      a4,0(a5)
    lw      a1,-40(s0)
    lw      a0,-36(s0)
    #if (check(index,set) && solve(index + 1,set))
    jal     ra,check
    mv      a5,a0
    beqz    a5,solve+0xb8
    lw      a5,-36(s0)
    #index + 1
    addi    a5,a5,1
    lw      a1,-40(s0)
    mv      a0,a5
    jal     ra,solve
    mv      a5,a0
    #n <= 4
    beqz    a5,solve+0xb8
    #index + 1
    li      a5,1
    #solve(index + 1,set)
    j       solve+0xe4
    lw      a5,-20(s0)
    addi    a5,a5,1
    sw      a5,-20(s0)
    lw      a4,-20(s0)
    li      a5,4
    bge     a5,a4,solve+0x68
    lw      a5,-36(s0)
    lw      a4,-40(s0)
    add     a5,a4,a5
    sb      zero,0(a5)
    #set[index] = 0;
    li      a5,0
    mv      a0,a5
    lw      ra,44(sp)
    lw      s0,40(sp)
    addi    sp,sp,48
    ret


check:
    #if (col(index, set) && row(index, set) && box(index, set)) return 1;
    addi    sp,sp,-32
    sw      ra,28(sp)
    sw      s0,24(sp)
    addi    s0,sp,32
    sw      a0,-20(s0)
    sw      a1,-24(s0)
    #a0為陣列addr a1為index
    lw      a1,-24(s0)
    lw      a0,-20(s0)
    #col(index, set)
    jal     ra,col
    mv      a5,a0
    beqz    a5,check+0x5c
    lw      a1,-24(s0)
    lw      a0,-20(s0)
    #row(index, set)
    jal     ra,row
    mv      a5,a0
    beqz    a5,check+0x5c
    lw      a1,-24(s0)
    lw      a0,-20(s0)
    #box(index, set)
    jal     ra,box
    mv      a5,a0
    beqz    a5,check+0x5c
    #return 1;
    li      a5,1
    j       check+0x5c
    mv      a0,a5
    lw      ra,28(sp)
    lw      s0,24(sp)
    addi    sp,sp,32
    ret

col:
    addi    sp,sp,-48
    sw      s0,44(sp)
    addi    s0,sp,48
    sw      a0,-36(s0)
    sw      a1,-40(s0)
    ##a0為陣列addr a1為index
    lw      a4,-36(s0)
    #int col_num = index % 4;
    srai    a5,a4,0x1f
    srli    a5,a5,0x1e
    #for (int i = col_num ; i <= col_num +12; i = i + 4)
    add     a4,a4,a5
    andi    a4,a4,3
    sub     a5,a4,a5
    sw      a5,-24(s0)
    lw      a5,-24(s0)
    sw      a5,-20(s0)
    j       col+0x80
    lw      a4,-20(s0)
    lw      a5,-36(s0)
    #if (i != index)
    beq     a4,a5,col+0x74
    lw      a5,-36(s0)
    lw      a4,-40(s0)
    add     a5,a4,a5
    lbu     a4,0(a5)
    lw      a5,-20(s0)
    lw      a3,-40(s0)
    add     a5,a3,a5
    lbu     a5,0(a5)
    #if (set[index] == set[i])
    bne     a4,a5,col+0x74
    li      a5,0
    j       col+0x94
    lw      a5,-20(s0)
    #i = i + 4
    addi    a5,a5,4
    sw      a5,-20(s0)
    lw      a5,-24(s0)
    #i <= col_num +12
    addi    a5,a5,11
    lw      a4,-20(s0)
    bge     a5,a4,col+0x3c
    #return 1
    li      a5,1
    mv      a0,a5
    lw      s0,44(sp)
    addi    sp,sp,48
    ret

row:
    addi    sp,sp,-48
    sw      s0,44(sp)
    addi    s0,sp,48
    #a0為陣列addr a1為index
    sw      a0,-36(s0)
    sw      a1,-40(s0)
    lw      a5,-36(s0)
    #int row_num = (index / 4) * 4;
    srai    a4,a5,0x1f
    andi    a4,a4,3
    add     a5,a4,a5
    srai    a5,a5,0x2
    slli    a5,a5,0x2
    #for (i = row_num; i < row_num + 4; i++)
    sw      a5,-24(s0)
    lw      a5,-24(s0)
    sw      a5,-20(s0)
    #i < row_num + 4
    j       row+0x80
    lw      a4,-20(s0)
    lw      a5,-36(s0)
    #if (i != index)
    beq     a4,a5,row+0x74
    lw      a5,-36(s0)
    lw      a4,-40(s0)
    add     a5,a4,a5
    lbu     a4,0(a5)
    lw      a5,-20(s0)
    lw      a3,-40(s0)
    add     a5,a3,a5
    lbu     a5,0(a5)
    #i < row_num + 4
    bne     a4,a5,row+0x74
    li      a5,0
    j       row+0x94
    lw      a5,-20(s0)
    #i++
    addi    a5,a5,1
    sw      a5,-20(s0)
    lw      a5,-24(s0)
    addi    a5,a5,3
    lw      a4,-20(s0)
    bge     a5,a4,row+0x3c
    li      a5,1
    mv      a0,a5
    lw      s0,44(sp)
    addi    sp,sp,48
    ret


box:
    #    int box[16] = { 0,1,4,5,    2,3,6,7,
    #               8,9,12,13,  10,11,14,15 };
    #a5為變數
    addi    sp,sp,-112
    sw      ra,108(sp)
    sw      s0,104(sp)
    addi    s0,sp,112
    ###a0為陣列addr a1為index
    sw      a0,-100(s0)
    sw      a1,-104(s0)
    sw      zero,-20(s0)
    lui     a5,0x80001
    addi    a4,a5,-60
    addi    a5,s0,-92
    #for (int i = box_num; i < box_num + 4; i++)
    mv      a3,a4
    li      a4,64
    mv      a2,a4
    mv      a1,a3
    mv      a0,a5
    #break
    jal     ra,memcpy
    sw      zero,-24(s0)
    j       box+0x90
    lw      a5,-24(s0)
    slli    a5,a5,0x2
    #i < 16
    addi    a5,a5,-16
    add     a5,a5,s0
    lw      a5,-76(a5)
    lw      a4,-100(s0)
    #if (index == box[i])
    bne     a4,a5,box+0x84
    lw      a5,-24(s0)
    srai    a4,a5,0x1f
    andi    a4,a4,3
    add     a5,a4,a5
    srai    a5,a5,0x2
    slli    a5,a5,0x2
    sw      a5,-20(s0)
    j       box+0x9c
    lw      a5,-24(s0)
    addi    a5,a5,1
    sw      a5,-24(s0)
    lw      a4,-24(s0)
    li      a5,15
    #box_num = (i / 4) * 4;
    bge     a5,a4,box+0x48
    lw      a5,-20(s0)
    sw      a5,-28(s0)
    j       box+0x110
    lw      a5,-28(s0)
    slli    a5,a5,0x2
    addi    a5,a5,-16
    add     a5,a5,s0
    lw      a5,-76(a5)
    lw      a4,-100(s0)
    #i < box_num + 4
    beq     a4,a5,box+0x104
    lw      a5,-100(s0)
    lw      a4,-104(s0)
    add     a5,a4,a5
    lbu     a4,0(a5)
    lw      a5,-28(s0)
    # box_num = (i / 4) * 4;
    slli    a5,a5,0x2
    addi    a5,a5,-16
    add     a5,a5,s0
    lw      a5,-76(a5)
    mv      a3,a5
    lw      a5,-104(s0)
    add     a5,a5,a3
    lbu     a5,0(a5)
    # if (box[i] != index)
    bne     a4,a5,box+0x104
    li      a5,0
    j       box+0x124
    lw      a5,-28(s0)
    addi    a5,a5,1
    sw      a5,-28(s0)
    lw      a5,-20(s0)
    addi    a5,a5,3
    lw      a4,-28(s0)
    #if (set[index] == set[box[i]])
    bge     a5,a4,box+0xa8
    li      a5,1
    mv      a0,a5
    lw      ra,108(sp)
    lw      s0,104(sp)
    addi    sp,sp,112
    #return 1;
    ret

    .size sudoku_2x2_asm, .-sudoku_2x2_asm