## fibonacci.S
## put input n in register x10 (a0)  
## put output fibonacci(n) in register x11 (a1)
## use Venus to test correctness

#演算法
#int fib(int n){
#	if(n==0) return 0
#   else if(n==1) return 1
#	else return fib(n-1)*fib(n-2)
#}
.text
main:
    li     a0, 16              # 暫存器 a0 儲存 n 值，這邊設定 n=16
    li     a1,0
    jal    fib               # 開始進行 fib 運算
    j      exit
fib:
    addi   sp, sp, -8        # 進行 stack pointer 的移動
    sw     ra, 0(sp)          # 儲存 return address
    sw     a0, 4(sp)          # 儲存 temp data
	
    
    li     t0, 1              
    li     t1, 2             
    blt    a0, t0, ret_zero   # if(n==0)return 0
    blt    a0, t1, ret_one    # if(n==1)return 1       		 

    addi   a0, a0, -1         # f(n-1)
    jal    fib       		  
    
    lw     a0, 4(sp)          # 逐一取出
    addi   a0, a0, -2         # f(n-2)
    jal    fib       		  
    j      done



ret_one:
    li     a2, 1
    lw     ra, 0(sp)         #取出ra地址
    addi   sp, sp, 8 
    add    a1, a2, a1 		
    jr     ra
    
ret_zero:
    li     a2, 0
    lw     ra, 0(sp)         #取出後，將block清掉
    addi   sp, sp, 8 
    add    a1, a2, a1 		
    jr     ra   
done:
    lw     ra, 0(sp)         #取出後，將block清掉
    addi   sp, sp, 8 
    jr     ra     
    
exit:
    addi a0,x0 ,1
    ecall
