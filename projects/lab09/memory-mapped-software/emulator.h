#ifndef EMULATOR_H__
#define EMULATOR_H__

#include <stdio.h>
#include <stdlib.h>
#include <stdint.h>
#include <string.h>
#include <ctype.h>
#include <errno.h>

// 64 KB
#define MEM_BYTES 0xffffffff
#define TEXT_OFFSET 0x0
#define DATA_OFFSET 0x8000

#define MAX_LABEL_COUNT 128
#define MAX_LABEL_LEN 32
#define MAX_SRC_LEN (1024*1024)

#define VECTOR_LEN 64
#define ELEMENT_WIDTH 8
#define VLMAX VECTOR_LEN/ELEMENT_WIDTH

// parameter for MM register for memory
#define REG_BASE_ADDRESS 0x100000
#define REG_STATUS 0x4 
#define REG_ENABLE 0x0

typedef struct {
	char* src;
	int offset;
} source;

typedef enum {
    UNIMPL = 0,
    MUL,
    CLZ,
    MIN,
    ROL,
    BEXT,
    BSETI,
    SH3ADD,
    XNOR,

    CLMULR,  //	clmulr rd, rs1, rs2
    MAXU,    //	maxu rd, rs1, rs2
    REV8,    //	rev8 rd, rs
    BCLRI,   // 	bclri rd, rs1, imm
    BSET,    //	bset rd, rs1, rs2
    SH2ADD,  //	sh2add rd, rs1, rs2

    CLMULH,  // clmulh rd, rs1, rs2
    MAX,     // max rd, rs1, rs2
    ORN,     // orn rd, rs1, rs2
    BCLR,    // bclr rd, rs1, rs2
    BINVI,   // binvi rd, rs1, imm
    SH1ADD,  // sh1add rd, rs1, rs2

    CLMUL,
    CTZ,
    ORC_B,
    RORI,
    BINV,
    SEXT_H,
    ADD,
    ADDI,
    AND,
    ANDI,
    AUIPC,
    BEQ,
    BGE,
    BGEU,
    BLT,
    BLTU,
    BNE,
    JAL,
    JALR,
    LB,
    LBU,
    LH,
    LHU,
    LUI,
    LW,
    OR,
    ORI,
    SB,
    SH,
    SLL,
    SLLI,
    SLT,
    SLTI,
    SLTIU,
    SLTU,
    SRA,
    SRAI,
    SRL,
    SRLI,
    SUB,
    SW,
    XOR,
    XORI,
    HCF,
    ANDN,
    CPOP,
    MINU,
    ROR,
    BEXTI,
    SEXT_B,
    ZEXT_H,
    VLE8_V,
    VSE8_V,
    VADD_VV,
    VMUL_VX,
    VMACC_VV
} instr_type;


typedef enum {
	OPTYPE_NONE, // more like "don't care"
	OPTYPE_REG,
	OPTYPE_IMM,
	OPTYPE_LABEL,
} operand_type;

typedef struct {
	operand_type type = OPTYPE_NONE;
	char label[MAX_LABEL_LEN];
	int reg;
	uint32_t imm;

} operand;

typedef struct {
	instr_type op;
	operand a1;
	operand a2;
	operand a3;
	char* psrc = NULL;
	int orig_line=-1;
	bool breakpoint = false;
} instr;

typedef struct {
	char label[MAX_LABEL_LEN];
	int loc = -1;
} label_loc;

uint32_t mem_read(uint8_t*, uint32_t, instr_type);

#endif