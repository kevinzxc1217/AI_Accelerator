# s0: base address of array
# s1: array size
# s2: the search element
# return index at $a5,if search element not found , put -1 in a5

.data
success1: .string "found  "
success2: .string " at index "
fail_string1: .string "search element "
fail_string2: .string " not found "
array:   .word 1, 3, 4, 6, 9, 12, 14, 15, 17, 19, 24
array_size: .word 11
search_element:.word 13
.text
main:
    # binarySearch(int arr[], int l, int r, int x)  
    addi t0, zero, 0    #int l
    la   t1, array_size  
    lw   t1, 0(t1)      #int r
    addi t1, t1, -1
    add  s10,t1,zero   # s10 used to check if search is go out of bound of array
    addi a5, zero, -1
    la   s2, search_element
    lw   s2, 0(s2)              #s2 stands for search value
    la   s0, array           # load the base address to $s0
    jal  ra, binary_search      # Jump-and-link to the 'binarySearch' label
    bltz a5, Fail				#check if found or not
    j exit
binary_search:
    # a2 stand for mid = (l + r)/2
    add  a2, t0, t1
    srli a2, a2, 1
    #check if mid > array_size
    blt s10,a2,Fail
    #check if mid < 0
    bltz a2,Fail
    # check if array[mid]== search_element
    add  t2, a2, zero
    slli t2, t2, 2 # t2=t2*4
    add  t2, t2, s0 
    lw   t2, 0(t2)
    beq  t2, s2, Find
    # check if to == t1 and still not equal
    beq t0,t1,Fail
    #not equal then adjust l,r depends on the value of array[mid]
    blt  s2, t2, less
greater: #elseif target > array[mid] : l = mid + 1
    addi t0,a2,1
    j binary_search
less: # @if target<array[mid] : r = mid-1
    addi t1,a2,-1
    j binary_search
    ret
Fail:
    addi a5,zero,-1
    la  a1, fail_string1
    li  a0, 4
    ecall

    mv       a1, s2
    li       a0, 1
    ecall

    la  a1, fail_string2
    li  a0, 4
    ecall
    j exit
Find:
    add a5,a2,zero
    la  a1, success1
    li  a0, 4
    ecall

    mv       a1, s2
    li       a0, 1
    ecall

    la  a1, success2
    li  a0, 4
    ecall

    mv       a1, a5
    li       a0, 1
    ecall
    
    j exit
exit:
