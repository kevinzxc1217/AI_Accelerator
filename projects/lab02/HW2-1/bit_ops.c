#include <stdio.h>
#include <stdint.h>

typedef enum {
    LUI,
    AUIPC,
    JAL,
    JALR,
    BEQ,
    BNE,
    BLT,
    BGE,
    BLTU,
    BGEU,
    LB,
    LH,
    LW,
    LBU,
    LHU,
    SB,
    SH,
    SW,
    ADDI,
    SLTI,
    SLTIU,
    XORI,
    ORI,
    ANDI,
    SLLI,
    SRLI,
    SRAI,
    ADD,
    SUB,
    SLL,
    SLT,
    SLTU,
    XOR,
    SRL,
    SRA,
    OR,
    AND,
    FENCE,
    FENCEI,
    ECALL,
    EBREAK,
    CSRRW,
    CSRRS,
    CSRRC,
    CSRRWI,
    CSRRSI,
    CSRRCI,
    UNDEFINED
}OPCODE;

// Return the nth bit of x.
// Assume 0 <= n <= 31
unsigned get_bit(unsigned x,
    unsigned n) {
    //假設x = 0011 n = 1
    //mask = 0010
    int mask = 1 << n;
    //做&後bit為0010，可找出該位置的bit為何
    unsigned bit = x & mask;
    //bit = 0001
    bit = bit >> n;
    return bit;
}

// Set the nth bit of the value of x to v.
// Assume 0 <= n <= 31, and v is 0 or 1
void set_bit(unsigned* x,
    unsigned n,
    unsigned v) {
    //假設
    int mask = 1 << n;
    unsigned bit = *x & mask;
    bit = bit >> n;
    //替換的值(v)可能為1跟0，有不同的方法執行
    if (v == 0)
    {
        //
        int mask = ~(1 << n);
        *x &= mask;
    }
    else
    {
        int mask = v << n;
        *x |= mask;
    }

}
// Flip the nth bit of the value of x.
// Assume 0 <= n <= 31
void flip_bit(unsigned* x,
    unsigned n) {
    // YOUR CODE HERE
    int mask = 1 << n;
    *x = *x ^ mask;

}

//Encoding the instruction
OPCODE decode_riscv_inst(uint32_t inst) {
    // YOUR CODE HERE
    
#define BIT_MASK(n) ((((uint32_t)1)<< ((n) < 32 ? (n) : 31)) -1)
#define BIT_MASK2(start, end) (BIT_MASK(start) & ~BIT_MASK(end))
#define GET_ADDR_BITS(addr, start, end)  (((addr) & BIT_MASK2(start, end)) >> (end))

    uint32_t mask = GET_ADDR_BITS(inst, 7, 0);
    uint32_t mask2 = GET_ADDR_BITS(inst, 15, 12);
    uint32_t mask3 = GET_ADDR_BITS(inst, 32, 25);

    //for (int i = 31; i >= 0; i--)
    //{
    //    printf("%d", mask >> i & 1);
    //}
    //printf("-> %d\n", mask);// 一般顯示
    if (mask == 0b0110111)
    {
        return LUI;
    }
    else if (mask == 0b0010111)
    {
        return AUIPC;
    }
    else if (mask == 0b1101111)
    {
        return JAL;
    }
    else if (mask == 0b1100111)
    {
        return JALR;
    }
    else if (mask == 0b1100011)
    {
        if (mask2 == 0b000)
        {
            return BEQ;
        }
        else if (mask2 == 0b001)
        {
            return BNE;
        }
        else if (mask2 == 0b100)
        {
            return BLT;
        }
        else if (mask2 == 0b101)
        {
            return BGE;
        }
        else if (mask2 == 0b110)
        {
            return BLTU;
        }
        else if (mask2 == 0b111)
        {
            return BGEU;
        }
    }
    else if (mask == 0b0000011)
    {
        if (mask2 == 0b000)
        {
            return LB;
        }
        else if (mask2 == 0b001)
        {
            return LH;
        }
        else if (mask2 == 0b010)
        {
            return LW;
        }
        else if (mask2 == 0b100)
        {
            return LBU;
        }
        else if (mask2 == 0b101)
        {
            return LHU;
        }
    }
    else if (mask == 0b0100011)
    {
        if (mask2 == 0b000)
        {
            return SB;
        }
        else if (mask2 == 0b001)
        {
            return SH;
        }
        else if (mask2 == 0b010)
        {
            return SW;
        }
    }
    else if (mask == 0b0010011)
    {
        if (mask2 == 000)
        {
            return ADDI;
        }
        else if (mask2 == 0b010)
        {
            return SLTI;
        }
        else if (mask2 == 0b011)
        {
            return SLTIU;
        }
        else if (mask2 == 0b100)
        {
            return XORI;
        }
        else if (mask2 == 0b110)
        {
            return ORI;
        }
        else if (mask2 == 0b111)
        {
            return ANDI;
        }

        ////////////////////例外
        else if (mask2 == 0b001)
        {
            return SLLI;
        }
        else if (mask2 == 0b101)
        {
            if (mask3 == 0b0000000)
            {
                return SRLI;
            }
            else if (mask3 == 0b0100000)
            {
                return SRAI;
            }
        }
    }
    //注意
    else if (mask == 0b0110011)
    {
        if (mask2 == 0b000)
        {
            if (mask3 == 0b0000000)
            {
                return ADD;
            }
            else if (mask3 == 0b0100000)
            {
                return SUB;
            }
        }
        else if (mask2 == 0b001)
        {
            return SLL;
        }
        else if (mask2 == 0b010)
        {
            return SLT;
        }
        else if (mask2 == 0b011)
        {
            return SLTU;
        }
        else if (mask2 == 0b100)
        {
            return XOR;
        }
        else if (mask2 == 0b101)
        {
            if (mask3 == 0b0000000)
            {
                return SRL;
            }
            else if (mask3 == 0b0100000)
            {
                return SRA;
            }
        }
        else if (mask2 == 0b110)
        {
            return OR;
        }
        else if (mask2 == 0b111)
        {
            return AND;
        }
    }
    else if (mask == 0b0001111)
    {
        if (mask2 == 0b000)
        {
            return FENCE;
        }
        else if (mask2 == 0b001)
        {
            return FENCEI;
        }
    }
    //要改
    else if (mask == 0b1110011)
    {
        if (mask2 == 0b000)
        {
            if (mask3 == 0b0000000)
            {
                return ECALL;
            }
            else if (mask3 == 0b0000001)
            {
                return EBREAK;
            }
        }
        else if (mask2 == 0b001)
        {
            return CSRRW;
        }
        else if (mask2 == 0b010)
        {
            return CSRRS;
        }
        else if (mask2 == 0b011)
        {
            return CSRRC;
        }
        else if (mask2 == 0b101)
        {
            return CSRRWI;
        }
        else if (mask2 == 0b110)
        {
            return CSRRSI;
        }
        else if (mask2 == 0b111)
        {
            return CSRRCI;
        }
    }
    else
    {
        return UNDEFINED;
    }
}


/*
 * YOU CAN IGNORE THE REST OF THIS FILE
 */

void test_get_bit(unsigned x,
    unsigned n,
    unsigned expected) {
    unsigned a = get_bit(x, n);
    if (a != expected) {
        //%08x,0x1234輸出00001234，即輸出16進制佔8位元
        printf("get_bit(0x%08x,%u): 0x%08x, expected 0x%08x\n", x, n, a, expected);
    }
    else {
        printf("get_bit(0x%08x,%u): 0x%08x, correct\n", x, n, a);
    }
}
void test_set_bit(unsigned x,
    unsigned n,
    unsigned v,
    unsigned expected) {
    unsigned o = x;
    set_bit(&x, n, v);
    if (x != expected) {
        printf("set_bit(0x%08x,%u,%u): 0x%08x, expected 0x%08x\n", o, n, v, x, expected);
    }
    else {
        printf("set_bit(0x%08x,%u,%u): 0x%08x, correct\n", o, n, v, x);
    }
}
void test_flip_bit(unsigned x,
    unsigned n,
    unsigned expected) {
    unsigned o = x;
    flip_bit(&x, n);
    if (x != expected) {
        printf("flip_bit(0x%08x,%u): 0x%08x, expected 0x%08x\n", o, n, x, expected);
    }
    else {
        printf("flip_bit(0x%08x,%u): 0x%08x, correct\n", o, n, x);
    }
}
_Bool test_decode_riscv_inst() {
    uint32_t inst;
    inst = 0x37;
    if (decode_riscv_inst(inst) != LUI) return 0;
    inst = 0x00010597;
    if (decode_riscv_inst(inst) != AUIPC) return 0;
    inst = 0x054000ef;
    if (decode_riscv_inst(inst) != JAL) return 0;
    inst = 0x00008067;
    if (decode_riscv_inst(inst) != JALR) return 0;
    inst = 0x0262e863;
    if (decode_riscv_inst(inst) != BLTU) return 0;
    inst = 0x00012403;
    if (decode_riscv_inst(inst) != LW) return 0;
    inst = 0xffe40c23;
    if (decode_riscv_inst(inst) != SB) return 0;
    inst = 0x2013;
    if (decode_riscv_inst(inst) != SLTI) return 0;
    inst = 0x1f093;
    if (decode_riscv_inst(inst) != ANDI) return 0;
    inst = 0xd113;
    if (decode_riscv_inst(inst) != SRLI) return 0;
    inst = 0x005303b3;
    if (decode_riscv_inst(inst) != ADD) return 0;
    inst = 0x40638333;
    if (decode_riscv_inst(inst) != SUB) return 0;
    inst = 0x0062b2b3;
    if (decode_riscv_inst(inst) != SLTU) return 0;
    inst = 0x0062c2b3;
    if (decode_riscv_inst(inst) != XOR) return 0;
    inst = 0x0110000f;
    if (decode_riscv_inst(inst) != FENCE) return 0;
    inst = 0x300332f3;
    if (decode_riscv_inst(inst) != CSRRC) return 0;
    inst = 0x11110000;
    if (decode_riscv_inst(inst) != UNDEFINED) return 0;

    return 1;

}
int main(int argc,
    const char* argv[]) {

    printf("\nTesting get_bit()\n\n");
    test_get_bit(0b1001110, 0, 0);
    test_get_bit(0b1001110, 1, 1);
    test_get_bit(0b1001110, 5, 0);
    test_get_bit(0b11011, 3, 1);
    test_get_bit(0b11011, 2, 0);
    test_get_bit(0b11011, 9, 0);
    printf("\nTesting set_bit()\n\n");
    test_set_bit(0b1001110, 2, 0, 0b1001010);
    test_set_bit(0b1101101, 0, 0, 0b1101100);
    test_set_bit(0b1001110, 2, 1, 0b1001110);
    test_set_bit(0b1101101, 0, 1, 0b1101101);
    test_set_bit(0b1001110, 9, 0, 0b1001110);
    test_set_bit(0b1101101, 4, 0, 0b1101101);
    test_set_bit(0b1001110, 9, 1, 0b1001001110);
    test_set_bit(0b1101101, 7, 1, 0b11101101);
    printf("\nTesting flip_bit()\n\n");
    test_flip_bit(0b1001110, 0, 0b1001111);
    test_flip_bit(0b1001110, 1, 0b1001100);
    test_flip_bit(0b1001110, 2, 0b1001010);
    test_flip_bit(0b1001110, 5, 0b1101110);
    test_flip_bit(0b1001110, 9, 0b1001001110);
    printf("\n");

    printf("\nTesting decode_riscv_inst()\n\n");
    if (test_decode_riscv_inst()) printf("Your decode function is correct.\n");
    else printf("Your decode function may have something wrong.\n");

    return 0;
}
