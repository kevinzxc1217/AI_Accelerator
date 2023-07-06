# sudoku_2x2_asm.S
   
    .text                           # code section 
    .global sudoku_2x2_asm          # declare the asm function as a global function
    .type sudoku_2x2_asm, @function # define sum_asm as a function 
sudoku_2x2_asm:

sudoku_2x2_c:
	addi	sp,sp,-4 #­ì©laddr
    sw		ra,0(sp)
    li 		a1,0	#a1:index
	jal		solve
end:
    lw      ra,0(sp)
    addi    sp,sp,4
    jr      ra

solve:
    #a0:set     a1:index
	addi	sp,sp,-8
    sw		ra,0(sp)
    sw		a1,4(sp)
    ##if (index >= 16) 
	li		t0,16
	bge		a1,t0,ret_one
    #t2:set[index]
    add		t2,a0,a1
    lw		t2,0(t2)
    #set[index]>0
    li		t1,0
    bgt		t2,t1,solve_index
    #for (int n = 1; n <= 4; n++)
    #t0:n   t1:max
    li      t0,1
    li      t1,4
    #set[index] = n; 
    add		t2,a0,a1
    lw		t0,0(t2)
    ##if (check(index,set) && solve(index + 1,set))
    jal     check
    mv      t3,a1
    jal     solve_index
    mv      t4,a1
    and     t3,t3,t4
    beq     t3,t4,ret_one
    #t2:set[index]
    add		t2,a0,a1
    lw		x0,0(t2)
    j       ret_zero
	

col:
    #a1:index   a0:set
    #int col_num = index % 4;  
    #t0:col_num
    addi    t1,x0,4
    srl    t0,a1,4
    sll    t0,t0,4
    sub     t0,a1,t0
    #i:t3   max:t4
    addi    t3,t0,0
    addi    t4,t0,12

col_loop:
    #for (int i = col_num ; i < col_num +12; i = i + 4)
    #i < col_num +12;
    beq     t3,t4,ret_one
    ##if (i != index)
    beq     t3,a1,i_index
    #t5:set[index]
    sll    t5,a1,2
    add     t5,a1,t5
    lw      t5,0(a1)
    #t6:set[i]
    sll    t6,t3,2
    add     t6,a1,t6
    lw      t6,0(a1)
    ##if (set[index] == set[i])
    beq     t5,t6,ret_zero
    #i = i + 4
    j       i_index

check:
    ##if (col(index, set) && row(index, set) && box(index, set))
    li  t0,0


i_index:
    addi    t3,t3,4
    j       col_loop



solve_index:
	addi	a1,a1,1
    j		solve
    
    
ret_one:
	li		a1,1
    lw		ra,4(sp)
    addi	sp,sp,8
    jr		ra
    
ret_zero:
	li		a1,0
    lw		ra,4(sp)
    addi	sp,sp,8
    jr		ra

    .size sudoku_2x2_asm, .-sudoku_2x2_asm