#include <stdio.h>
#include <stdlib.h>
#include <stdint.h>
#include <string.h>
#include <ctype.h>
#include <errno.h>

#ifndef EMULATOR_H__
#define EMULATOR_H__

// 64 KB
#define MEM_BYTES 0x10000
#define TEXT_OFFSET 0
#define DATA_OFFSET 32768

#define MAX_LABEL_COUNT 128
#define MAX_LABEL_LEN 32
#define MAX_SRC_LEN (1024*1024)

typedef struct {
	char* src;
	int offset;
} source;

typedef enum {
	UNIMPL = 0,

	//hcc_gitlab0_0//
    ANDN,
    CLZ,
    CPOP,
    CTZ,
    MAX,
    MAXU,
    MIN,
	// hako0Lin //
	ORCB, 		// Bitwise OR-Combine, byte granule
	ORN,		// OR with inverted operand
	REV8,		// Byte-reverse register
	ROL,		// Rotate left (Register)
	ROR,		// Rotate right (Register)
	RORI,		// Rotate right (Immediate)
    // kevin1217 //
	SEXTB,
	SEXTH,
	ZEXTH,
	CLMUL,
	CLMULH,
	CLMULR,
	///////////////////////////////////

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
	// id:s8942352
	MUL,
	MULHU,
	REM,
	REMU,
	SH1ADD,
	SH2ADD,
	SH3ADD,
	BCLR,
	BCLRI,
	BEXT,
	BEXTI,
    ///////////////////////////////
	//mt19937
	MINU,
	XNOR,
	BINV,
	BINVI,
	BSET,
	BSETI,
    ////////////////////////////////
	HCF
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