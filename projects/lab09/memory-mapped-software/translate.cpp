#include "translate.h"

#include <stdlib.h>
#include <string.h>

#define BIT_MASK(n) ((((uint32_t)1) << (n + 1)) - 1)
#define BIT_MASKS(begin, end) (BIT_MASK(begin) & ~BIT_MASK(end - 1))
#define GET_BITS(addr, begin, end) ((addr & BIT_MASKS(begin, end)) >> end)

namespace INSTR_FORMAT {
namespace R_TYPE {
size_t opcode = 0;
size_t rd = 7;
size_t funct3 = 12;
size_t rs1 = 15;
size_t rs2 = 20;
size_t funct7 = 25;
}  // namespace R_TYPE
namespace I_TYPE {
size_t opcode = 0;
size_t rd = 7;
size_t funct3 = 12;
size_t rs1 = 15;
size_t imm = 20;
}  // namespace I_TYPE
namespace S_TYPE {
size_t opcode = 0;
size_t imm_4_0 = 7;
size_t funct3 = 12;
size_t rs1 = 15;
size_t rs2 = 20;
size_t imm_11_5 = 25;
}  // namespace S_TYPE
namespace B_TYPE {
size_t opcode = 0;
size_t imm_11 = 7;
size_t imm_4_1 = 8;
size_t funct3 = 12;
size_t rs1 = 15;
size_t rs2 = 20;
size_t imm_10_5 = 25;
size_t imm_12 = 31;
}  // namespace B_TYPE
namespace U_TYPE {
size_t opcode = 0;
size_t rd = 7;
size_t imm_31_12 = 12;
}  // namespace U_TYPE
namespace J_TYPE {
size_t opcode = 0;
size_t rd = 7;
size_t imm_19_12 = 12;
size_t imm_11 = 20;
size_t imm_10_1 = 21;
size_t imm_20 = 31;
}  // namespace J_TYPE

namespace VL_TYPE {
size_t opcode = 0;
size_t vd = 7;
size_t width = 12;
size_t rs1 = 15;
size_t lumop = 20;
size_t vm = 25;
size_t mop = 26;
size_t mew = 28;
size_t nf = 29;
}  // namespace VL_TYPE

namespace VS_TYPE {
size_t opcode = 0;
size_t vs3 = 7;
size_t width = 12;
size_t rs1 = 15;
size_t sumop = 20;
size_t vm = 25;
size_t mop = 26;
size_t mew = 28;
size_t nf = 29;
}  // namespace VS_TYPE

namespace OPVV_TYPE {
size_t opcode = 0;
size_t vd = 7;
size_t funct3 = 12;
size_t vs1 = 15;
size_t vs2 = 20;
size_t vm = 25;
size_t funct6 = 26;
}  // namespace OPVV_TYPE

namespace OPVX_TYPE {
size_t opcode = 0;
size_t vd = 7;
size_t funct3 = 12;
size_t rs1 = 15;
size_t vs2 = 20;
size_t vm = 25;
size_t funct6 = 26;
}  // namespace OPVX_TYPE
}  // namespace INSTR_FORMAT

namespace OPCODE {
uint32_t OP = 0b0110011;
uint32_t OP_IMM = 0b0010011;
uint32_t LOAD = 0b0000011;
uint32_t STORE = 0b0100011;
uint32_t BRANCH = 0b1100011;
uint32_t JALR = 0b1100111;
uint32_t JAL = 0b1101111;
uint32_t AUIPC = 0b0010111;
uint32_t LUI = 0b0110111;
uint32_t VL = 0b0000111;
uint32_t VS = 0b0100111;
uint32_t OP_V = 0b1010111;
}  // namespace OPCODE

namespace FUNCT {  // with offset
// op
uint32_t ADD = (0b0 << 30) | (0b000 << 12);
uint32_t SUB = (0b1 << 30) | (0b000 << 12);
uint32_t SLL = (0b0 << 30) | (0b001 << 12);
uint32_t SLT = (0b0 << 30) | (0b010 << 12);
uint32_t SLTU = (0b0 << 30) | (0b011 << 12);
uint32_t XOR = (0b0 << 30) | (0b100 << 12);
uint32_t SRL = (0b0 << 30) | (0b101 << 12);
uint32_t SRA = (0b1 << 30) | (0b101 << 12);
uint32_t OR = (0b0 << 30) | (0b110 << 12);
uint32_t AND = (0b0 << 30) | (0b111 << 12);
uint32_t MULDIV = (0b01 << INSTR_FORMAT::R_TYPE::funct7) | (0b000 << INSTR_FORMAT::R_TYPE::funct3);

// load / store
uint32_t BYTE = 0b000 << INSTR_FORMAT::I_TYPE::funct3;
uint32_t HALF = 0b001 << INSTR_FORMAT::I_TYPE::funct3;
uint32_t WORD = 0b010 << INSTR_FORMAT::I_TYPE::funct3;
uint32_t BYTE_U = 0b100 << INSTR_FORMAT::I_TYPE::funct3;
uint32_t HALF_U = 0b101 << INSTR_FORMAT::I_TYPE::funct3;

// branch
uint32_t EQ = 0b000 << INSTR_FORMAT::B_TYPE::funct3;
uint32_t NE = 0b001 << INSTR_FORMAT::B_TYPE::funct3;
uint32_t LT = 0b100 << INSTR_FORMAT::B_TYPE::funct3;
uint32_t GE = 0b101 << INSTR_FORMAT::B_TYPE::funct3;
uint32_t LTU = 0b110 << INSTR_FORMAT::B_TYPE::funct3;
uint32_t GEU = 0b111 << INSTR_FORMAT::B_TYPE::funct3;

// bitmanip extension
uint32_t ANDN = (0b1 << 30) | (0b111 << 12);  // AND
uint32_t ORN = (0b1 << 30) | (0b110 << 12);   // OR
uint32_t XNOR = (0b1 << 30) | (0b100 << 12);  // XOR
uint32_t ROL = (0b11 << 29) | (0b001 << 12);  // SLL
uint32_t ROR = (0b11 << 29) | (0b101 << 12);  // SRA

uint32_t SH1ADD = (0x10 << INSTR_FORMAT::R_TYPE::funct7) | (0b010 << INSTR_FORMAT::R_TYPE::funct3);
uint32_t SH2ADD = (0x10 << INSTR_FORMAT::R_TYPE::funct7) | (0b100 << INSTR_FORMAT::R_TYPE::funct3);
uint32_t SH3ADD = (0x10 << INSTR_FORMAT::R_TYPE::funct7) | (0b110 << INSTR_FORMAT::R_TYPE::funct3);

uint32_t BCLR = (0x24 << INSTR_FORMAT::R_TYPE::funct7) | (0b001 << INSTR_FORMAT::R_TYPE::funct3);
uint32_t BSET = (0x14 << INSTR_FORMAT::R_TYPE::funct7) | (0b001 << INSTR_FORMAT::R_TYPE::funct3);
uint32_t BINV = (0x34 << INSTR_FORMAT::R_TYPE::funct7) | (0b001 << INSTR_FORMAT::R_TYPE::funct3);
uint32_t BEXT = (0x24 << INSTR_FORMAT::R_TYPE::funct7) | (0b101 << INSTR_FORMAT::R_TYPE::funct3);

uint32_t CLZ = (((0x30 << 5) | 0x0) << INSTR_FORMAT::I_TYPE::imm) | (0x1 << INSTR_FORMAT::I_TYPE::funct3);
uint32_t CTZ = (((0x30 << 5) | 0x1) << INSTR_FORMAT::I_TYPE::imm) | (0x1 << INSTR_FORMAT::I_TYPE::funct3);
uint32_t CPOP = (((0x30 << 5) | 0x2) << INSTR_FORMAT::I_TYPE::imm) | (0x1 << INSTR_FORMAT::I_TYPE::funct3);
uint32_t SEXT_B = (((0x30 << 5) | 0x4) << INSTR_FORMAT::I_TYPE::imm) | (0x1 << INSTR_FORMAT::I_TYPE::funct3);
uint32_t SEXT_H = (((0x30 << 5) | 0x5) << INSTR_FORMAT::I_TYPE::imm) | (0x1 << INSTR_FORMAT::I_TYPE::funct3);
uint32_t REV8 = (0x698 << INSTR_FORMAT::I_TYPE::imm) | (0x5 << INSTR_FORMAT::I_TYPE::funct3);
uint32_t ORC_B = (0x287 << INSTR_FORMAT::I_TYPE::imm) | (0x5 << INSTR_FORMAT::I_TYPE::funct3);

uint32_t ZEXT_H =
    (0x4 << INSTR_FORMAT::R_TYPE::funct7) | (0x0 << INSTR_FORMAT::R_TYPE::rs2) | (0x4 << INSTR_FORMAT::R_TYPE::funct3);

uint32_t CLMUL = (0x5 << INSTR_FORMAT::R_TYPE::funct7) | (0x1 << INSTR_FORMAT::R_TYPE::funct3);
uint32_t CLMULR = (0x5 << INSTR_FORMAT::R_TYPE::funct7) | (0x2 << INSTR_FORMAT::R_TYPE::funct3);
uint32_t CLMULH = (0x5 << INSTR_FORMAT::R_TYPE::funct7) | (0x3 << INSTR_FORMAT::R_TYPE::funct3);
uint32_t MIN = (0x5 << INSTR_FORMAT::R_TYPE::funct7) | (0x4 << INSTR_FORMAT::R_TYPE::funct3);
uint32_t MINU = (0x5 << INSTR_FORMAT::R_TYPE::funct7) | (0x5 << INSTR_FORMAT::R_TYPE::funct3);
uint32_t MAX = (0x5 << INSTR_FORMAT::R_TYPE::funct7) | (0x6 << INSTR_FORMAT::R_TYPE::funct3);
uint32_t MAXU = (0x5 << INSTR_FORMAT::R_TYPE::funct7) | (0x7 << INSTR_FORMAT::R_TYPE::funct3);
}  // namespace FUNCT

namespace FUNCT6 {  // without offset
// vector extension [ref: riscv-v-spec-1.0 (P.95)]
uint32_t VADD = 0b000000;  // V, X, I
uint32_t VMUL = 0b100101;  // V, X
uint32_t VMACC = 0b101101;
}  // namespace FUNCT6

namespace FUNCT3 {  // withoout offset
// vector extension [ref: riscv-v-spec-1.0 (P.42)]
uint32_t OPIVV = 0b000;
uint32_t OPIVX = 0b100;
}  // namespace FUNCT3

char *concat(const char *s1, const char *s2) {
    const size_t len1 = strlen(s1);
    const size_t len2 = strlen(s2);
    char *result = (char *)malloc(len1 + len2 + 1);  // +1 for the null-terminator
    // in real code you would check for errors in malloc here
    memcpy(result, s1, len1);
    memcpy(result + len1, s2, len2 + 1);  // +1 to copy the null-terminator
    return result;
}

void copy_path(char *argv1, char **path) {
    char *ptr;

    if (!(*path = (char *)malloc(sizeof(char) * strlen(argv1)))) {
        printf("malloc is failed in 'main', copy the argv[1] to char *path.\n");
        exit(2);
    }

    copy_str(*path, argv1);

    ptr = *path + strlen(*path);

    //find the last '/' in argv1
    while (ptr != *path) {
        if (*ptr == '/') {
            *ptr = '\0';
            break;
        }
        ptr--;
    }

    if (ptr == *path) **path = '\0';

    if (**path) strcat(*path, "/");
}

void copy_str(char *tgt, const char *src) {
    char *ptr;
    ptr = strncpy(tgt, src, strlen(src));
    ptr[strlen(src)] = '\0';
}

void write_data_hex(uint8_t *mem, FILE *data_file) {
    uint8_t ret = 0;

    //for (int i = 0; i < (1 << 10); i++) {  // 1kB for DataMem
    for (int i = 0; i < (1 << 16); i++) {  // 64kB for DataMem
        ret = (uint8_t)mem_read(mem, i + DATA_OFFSET, LBU);
        fprintf(data_file, "%02x\n", ret);
    }
}

void translate_to_machine_code(uint8_t *mem, instr *imem, char *argv1) {
    uint32_t inst_cnt = 0;
    bool dexit = false;

    const char *path = "../memory-mapped-hardware/src/main/resource/SystolicArray/";
    // copy_path(argv1, &path);

    FILE *mch_file = fopen(concat(path, "m_code.hex"), "w");
    FILE *inst_file = fopen(concat(path, "inst.asm"), "w");
    FILE *data_file = fopen(concat(path, "data.hex"), "w");

    while (!dexit) {
        instr i = imem[inst_cnt];
        uint32_t binary = 0;
        int offset = 0;

        // follow the ISA and combine the fragment information in binary form
        switch (i.op) {
            case ADD:
                // rf[i.a1.reg] = rf[i.a2.reg] + rf[i.a3.reg]; break;
                binary = (i.a3.reg << INSTR_FORMAT::R_TYPE::rs2) | (i.a2.reg << INSTR_FORMAT::R_TYPE::rs1) |
                         FUNCT::ADD | (i.a1.reg << INSTR_FORMAT::R_TYPE::rd) | OPCODE::OP;
                break;
            case SUB:
                // rf[i.a1.reg] = rf[i.a2.reg] + rf[i.a3.reg]; break;
                binary = (i.a3.reg << INSTR_FORMAT::R_TYPE::rs2) | (i.a2.reg << INSTR_FORMAT::R_TYPE::rs1) |
                         FUNCT::SUB | (i.a1.reg << INSTR_FORMAT::R_TYPE::rd) | OPCODE::OP;
                break;
            case SLL:
                // rf[i.a1.reg] = rf[i.a2.reg] << rf[i.a3.reg]; break;
                binary = (i.a3.reg << INSTR_FORMAT::R_TYPE::rs2) | (i.a2.reg << INSTR_FORMAT::R_TYPE::rs1) |
                         FUNCT::SLL | (i.a1.reg << INSTR_FORMAT::R_TYPE::rd) | OPCODE::OP;
                break;
            case SLT:
                // rf[i.a1.reg] = (*(int32_t*)&rf[i.a2.reg]) < (*(int32_t*)&rf[i.a3.reg]) ? 1 : 0; break;
                binary = (i.a3.reg << INSTR_FORMAT::R_TYPE::rs2) | (i.a2.reg << INSTR_FORMAT::R_TYPE::rs1) |
                         FUNCT::SLT | (i.a1.reg << INSTR_FORMAT::R_TYPE::rd) | OPCODE::OP;
                break;
            case SLTU:
                // rf[i.a1.reg] = rf[i.a2.reg] + rf[i.a3.reg]; break;
                binary = (i.a3.reg << INSTR_FORMAT::R_TYPE::rs2) | (i.a2.reg << INSTR_FORMAT::R_TYPE::rs1) |
                         FUNCT::SLTU | (i.a1.reg << INSTR_FORMAT::R_TYPE::rd) | OPCODE::OP;
                break;
            case XOR:
                // rf[i.a1.reg] = rf[i.a2.reg] ^ rf[i.a3.reg]; break;
                binary = (i.a3.reg << INSTR_FORMAT::R_TYPE::rs2) | (i.a2.reg << INSTR_FORMAT::R_TYPE::rs1) |
                         FUNCT::XOR | (i.a1.reg << INSTR_FORMAT::R_TYPE::rd) | OPCODE::OP;
                break;
            case SRL:
                // rf[i.a1.reg] = rf[i.a2.reg] >> rf[i.a3.reg]; break;
                binary = (i.a3.reg << INSTR_FORMAT::R_TYPE::rs2) | (i.a2.reg << INSTR_FORMAT::R_TYPE::rs1) |
                         FUNCT::SRL | (i.a1.reg << INSTR_FORMAT::R_TYPE::rd) | OPCODE::OP;
                break;
            case SRA:
                // rf[i.a1.reg] = (*(int32_t*)&rf[i.a2.reg]) >> rf[i.a3.reg]; break;
                binary = (i.a3.reg << INSTR_FORMAT::R_TYPE::rs2) | (i.a2.reg << INSTR_FORMAT::R_TYPE::rs1) |
                         FUNCT::SRA | (i.a1.reg << INSTR_FORMAT::R_TYPE::rd) | OPCODE::OP;
                break;
            case OR:
                // rf[i.a1.reg] = rf[i.a2.reg] | rf[i.a3.reg]; break;
                binary = (i.a3.reg << INSTR_FORMAT::R_TYPE::rs2) | (i.a2.reg << INSTR_FORMAT::R_TYPE::rs1) | FUNCT::OR |
                         (i.a1.reg << INSTR_FORMAT::R_TYPE::rd) | OPCODE::OP;
                break;
            case AND:
                // rf[i.a1.reg] = rf[i.a2.reg] & rf[i.a3.reg]; break;
                binary = (i.a3.reg << INSTR_FORMAT::R_TYPE::rs2) | (i.a2.reg << INSTR_FORMAT::R_TYPE::rs1) |
                         FUNCT::AND | (i.a1.reg << INSTR_FORMAT::R_TYPE::rd) | OPCODE::OP;
                break;

            case ADDI:
                // rf[i.a1.reg] = rf[i.a2.reg] + i.a3.imm; break;
                binary = (i.a3.imm << INSTR_FORMAT::I_TYPE::imm) | (i.a2.reg << INSTR_FORMAT::I_TYPE::rs1) |
                         FUNCT::ADD | (i.a1.reg << INSTR_FORMAT::I_TYPE::rd) | OPCODE::OP_IMM;
                break;
            case SLTI:
                // rf[i.a1.reg] = (*(int32_t*)&rf[i.a2.reg]) < (*(int32_t*)&(i.a3.imm)) ? 1 : 0; break;
                binary = (i.a3.imm << INSTR_FORMAT::I_TYPE::imm) | (i.a2.reg << INSTR_FORMAT::I_TYPE::rs1) |
                         FUNCT::SLT | (i.a1.reg << INSTR_FORMAT::I_TYPE::rd) | OPCODE::OP_IMM;
                break;
            case SLTIU:
                // rf[i.a1.reg] = rf[i.a2.reg] < i.a3.imm ? 1 : 0; break;
                binary = (i.a3.imm << INSTR_FORMAT::I_TYPE::imm) | (i.a2.reg << INSTR_FORMAT::I_TYPE::rs1) |
                         FUNCT::SLTU | (i.a1.reg << INSTR_FORMAT::I_TYPE::rd) | OPCODE::OP_IMM;
                break;
            case XORI:
                // rf[i.a1.reg] = rf[i.a2.reg] ^ i.a3.imm; break;
                binary = (i.a3.imm << INSTR_FORMAT::I_TYPE::imm) | (i.a2.reg << INSTR_FORMAT::I_TYPE::rs1) |
                         FUNCT::XOR | (i.a1.reg << INSTR_FORMAT::I_TYPE::rd) | OPCODE::OP_IMM;
                break;
            case ORI:
                // rf[i.a1.reg] = rf[i.a2.reg] | i.a3.imm; break;
                binary = (i.a3.imm << INSTR_FORMAT::I_TYPE::imm) | (i.a2.reg << INSTR_FORMAT::I_TYPE::rs1) | FUNCT::OR |
                         (i.a1.reg << INSTR_FORMAT::I_TYPE::rd) | OPCODE::OP_IMM;
                break;
            case ANDI:
                // rf[i.a1.reg] = rf[i.a2.reg] & i.a3.imm; break;
                binary = (i.a3.imm << INSTR_FORMAT::I_TYPE::imm) | (i.a2.reg << INSTR_FORMAT::I_TYPE::rs1) |
                         FUNCT::AND | (i.a1.reg << INSTR_FORMAT::I_TYPE::rd) | OPCODE::OP_IMM;
                break;
            case SLLI:
                // rf[i.a1.reg] = rf[i.a2.reg] << i.a3.imm; break;
                binary = (i.a3.imm << INSTR_FORMAT::I_TYPE::imm) | (i.a2.reg << INSTR_FORMAT::I_TYPE::rs1) |
                         FUNCT::SLL | (i.a1.reg << INSTR_FORMAT::I_TYPE::rd) | OPCODE::OP_IMM;
                break;
            case SRLI:
                // rf[i.a1.reg] = rf[i.a2.reg] >> i.a3.imm; break;
                binary = (i.a3.imm << INSTR_FORMAT::I_TYPE::imm) | (i.a2.reg << INSTR_FORMAT::I_TYPE::rs1) |
                         FUNCT::SRL | (i.a1.reg << INSTR_FORMAT::I_TYPE::rd) | OPCODE::OP_IMM;
                break;
            case SRAI:
                // rf[i.a1.reg] = (*(int32_t*)&rf[i.a2.reg]) >> i.a3.imm; break;
                binary = (i.a3.imm << INSTR_FORMAT::I_TYPE::imm) | (i.a2.reg << INSTR_FORMAT::I_TYPE::rs1) |
                         FUNCT::SRA | (i.a1.reg << INSTR_FORMAT::I_TYPE::rd) | OPCODE::OP_IMM;
                break;

            case LB:
                binary = (i.a3.imm << INSTR_FORMAT::I_TYPE::imm) | (i.a2.reg << INSTR_FORMAT::I_TYPE::rs1) |
                         FUNCT::BYTE | (i.a1.reg << INSTR_FORMAT::I_TYPE::rd) | OPCODE::LOAD;
                break;
            case LH:
                binary = (i.a3.imm << INSTR_FORMAT::I_TYPE::imm) | (i.a2.reg << INSTR_FORMAT::I_TYPE::rs1) |
                         FUNCT::HALF | (i.a1.reg << INSTR_FORMAT::I_TYPE::rd) | OPCODE::LOAD;
                break;
            case LW:
                binary = (i.a3.imm << INSTR_FORMAT::I_TYPE::imm) | (i.a2.reg << INSTR_FORMAT::I_TYPE::rs1) |
                         FUNCT::WORD | (i.a1.reg << INSTR_FORMAT::I_TYPE::rd) | OPCODE::LOAD;
                break;
            case LBU:
                binary = (i.a3.imm << INSTR_FORMAT::I_TYPE::imm) | (i.a2.reg << INSTR_FORMAT::I_TYPE::rs1) |
                         FUNCT::BYTE_U | (i.a1.reg << INSTR_FORMAT::I_TYPE::rd) | OPCODE::LOAD;
                break;
            case LHU:
                binary = (i.a3.imm << INSTR_FORMAT::I_TYPE::imm) | (i.a2.reg << INSTR_FORMAT::I_TYPE::rs1) |
                         FUNCT::HALF_U | (i.a1.reg << INSTR_FORMAT::I_TYPE::rd) | OPCODE::LOAD;
                break;

            case SB:
                binary = (GET_BITS(i.a3.imm, 11, 5) << INSTR_FORMAT::S_TYPE::imm_11_5) |
                         (i.a1.reg << INSTR_FORMAT::S_TYPE::rs2) | (i.a2.reg << INSTR_FORMAT::S_TYPE::rs1) |
                         FUNCT::BYTE | (GET_BITS(i.a3.imm, 4, 0) << INSTR_FORMAT::S_TYPE::imm_4_0) | OPCODE::STORE;
                break;
            case SH:
                binary = (GET_BITS(i.a3.imm, 11, 5) << INSTR_FORMAT::S_TYPE::imm_11_5) |
                         (i.a1.reg << INSTR_FORMAT::S_TYPE::rs2) | (i.a2.reg << INSTR_FORMAT::S_TYPE::rs1) |
                         FUNCT::HALF | (GET_BITS(i.a3.imm, 4, 0) << INSTR_FORMAT::S_TYPE::imm_4_0) | OPCODE::STORE;
                break;
            case SW:
                binary = (GET_BITS(i.a3.imm, 11, 5) << INSTR_FORMAT::S_TYPE::imm_11_5) |
                         (i.a1.reg << INSTR_FORMAT::S_TYPE::rs2) | (i.a2.reg << INSTR_FORMAT::S_TYPE::rs1) |
                         FUNCT::WORD | (GET_BITS(i.a3.imm, 4, 0) << INSTR_FORMAT::S_TYPE::imm_4_0) | OPCODE::STORE;
                break;

            case BEQ:
                // if ( rf[i.a1.reg] == rf[i.a2.reg] ) pc_next = i.a3.imm; break;
                offset = i.a3.imm - (inst_cnt << 2);
                binary = (GET_BITS(offset, 12, 12) << INSTR_FORMAT::B_TYPE::imm_12) |
                         (GET_BITS(offset, 10, 5) << INSTR_FORMAT::B_TYPE::imm_10_5) |
                         (i.a2.reg << INSTR_FORMAT::B_TYPE::rs2) | (i.a1.reg << INSTR_FORMAT::B_TYPE::rs1) | FUNCT::EQ |
                         (GET_BITS(offset, 4, 1) << INSTR_FORMAT::B_TYPE::imm_4_1) |
                         (GET_BITS(offset, 11, 11) << INSTR_FORMAT::B_TYPE::imm_11) | OPCODE::BRANCH;
                break;
            case BNE:
                // if ( rf[i.a1.reg] != rf[i.a2.reg] ) pc_next = i.a3.imm; break;
                offset = i.a3.imm - (inst_cnt << 2);
                binary = (GET_BITS(offset, 12, 12) << INSTR_FORMAT::B_TYPE::imm_12) |
                         (GET_BITS(offset, 10, 5) << INSTR_FORMAT::B_TYPE::imm_10_5) |
                         (i.a2.reg << INSTR_FORMAT::B_TYPE::rs2) | (i.a1.reg << INSTR_FORMAT::B_TYPE::rs1) | FUNCT::NE |
                         (GET_BITS(offset, 4, 1) << INSTR_FORMAT::B_TYPE::imm_4_1) |
                         (GET_BITS(offset, 11, 11) << INSTR_FORMAT::B_TYPE::imm_11) | OPCODE::BRANCH;
                break;
            case BLT:
                // if ( *(int32_t*)&rf[i.a1.reg] < *(int32_t*)&rf[i.a2.reg] ) pc_next = i.a3.imm; break;
                offset = i.a3.imm - (inst_cnt << 2);
                binary = (GET_BITS(offset, 12, 12) << INSTR_FORMAT::B_TYPE::imm_12) |
                         (GET_BITS(offset, 10, 5) << INSTR_FORMAT::B_TYPE::imm_10_5) |
                         (i.a2.reg << INSTR_FORMAT::B_TYPE::rs2) | (i.a1.reg << INSTR_FORMAT::B_TYPE::rs1) | FUNCT::LT |
                         (GET_BITS(offset, 4, 1) << INSTR_FORMAT::B_TYPE::imm_4_1) |
                         (GET_BITS(offset, 11, 11) << INSTR_FORMAT::B_TYPE::imm_11) | OPCODE::BRANCH;
                break;
            case BGE:
                // if ( *(int32_t*)&rf[i.a1.reg] >= *(int32_t*)&rf[i.a2.reg] ) pc_next = i.a3.imm; break;
                offset = i.a3.imm - (inst_cnt << 2);
                binary = (GET_BITS(offset, 12, 12) << INSTR_FORMAT::B_TYPE::imm_12) |
                         (GET_BITS(offset, 10, 5) << INSTR_FORMAT::B_TYPE::imm_10_5) |
                         (i.a2.reg << INSTR_FORMAT::B_TYPE::rs2) | (i.a1.reg << INSTR_FORMAT::B_TYPE::rs1) | FUNCT::GE |
                         (GET_BITS(offset, 4, 1) << INSTR_FORMAT::B_TYPE::imm_4_1) |
                         (GET_BITS(offset, 11, 11) << INSTR_FORMAT::B_TYPE::imm_11) | OPCODE::BRANCH;
                break;
            case BLTU:
                // if ( rf[i.a1.reg] < rf[i.a2.reg] ) pc_next = i.a3.imm; break;
                offset = i.a3.imm - (inst_cnt << 2);
                binary = (GET_BITS(offset, 12, 12) << INSTR_FORMAT::B_TYPE::imm_12) |
                         (GET_BITS(offset, 10, 5) << INSTR_FORMAT::B_TYPE::imm_10_5) |
                         (i.a2.reg << INSTR_FORMAT::B_TYPE::rs2) | (i.a1.reg << INSTR_FORMAT::B_TYPE::rs1) |
                         FUNCT::LTU | (GET_BITS(offset, 4, 1) << INSTR_FORMAT::B_TYPE::imm_4_1) |
                         (GET_BITS(offset, 11, 11) << INSTR_FORMAT::B_TYPE::imm_11) | OPCODE::BRANCH;
                break;
            case BGEU:
                // if ( rf[i.a1.reg] >= rf[i.a2.reg] ) pc_next = i.a3.imm;
                offset = i.a3.imm - (inst_cnt << 2);
                binary = (GET_BITS(offset, 12, 12) << INSTR_FORMAT::B_TYPE::imm_12) |
                         (GET_BITS(offset, 10, 5) << INSTR_FORMAT::B_TYPE::imm_10_5) |
                         (i.a2.reg << INSTR_FORMAT::B_TYPE::rs2) | (i.a1.reg << INSTR_FORMAT::B_TYPE::rs1) |
                         FUNCT::GEU | (GET_BITS(offset, 4, 1) << INSTR_FORMAT::B_TYPE::imm_4_1) |
                         (GET_BITS(offset, 11, 11) << INSTR_FORMAT::B_TYPE::imm_11) | OPCODE::BRANCH;
                break;

            case JAL:
                offset = i.a2.imm - (inst_cnt << 2);
                binary = (GET_BITS(offset, 20, 20) << INSTR_FORMAT::J_TYPE::imm_20) |
                         (GET_BITS(offset, 10, 1) << INSTR_FORMAT::J_TYPE::imm_10_1) |
                         (GET_BITS(offset, 11, 11) << INSTR_FORMAT::J_TYPE::imm_11) |
                         (GET_BITS(offset, 19, 12) << INSTR_FORMAT::J_TYPE::imm_19_12) |
                         (i.a1.reg << INSTR_FORMAT::J_TYPE::rd) | OPCODE::JAL;
                break;
            case JALR:
                binary = (i.a3.imm << INSTR_FORMAT::I_TYPE::imm) | (i.a2.reg << INSTR_FORMAT::I_TYPE::rs1) |
                         (i.a1.reg << INSTR_FORMAT::I_TYPE::rd) | OPCODE::JALR;
                break;
            case AUIPC:
                binary = (i.a2.imm << INSTR_FORMAT::U_TYPE::imm_31_12) | (i.a1.reg << INSTR_FORMAT::U_TYPE::rd) |
                         OPCODE::AUIPC;
                break;

            case LUI:
                // rf[i.a1.reg] = (i.a2.imm<<12);
                binary = (i.a2.imm << INSTR_FORMAT::U_TYPE::imm_31_12) | (i.a1.reg << INSTR_FORMAT::U_TYPE::rd) |
                         OPCODE::LUI;
                break;

            // M standard extension
            case MUL:
                binary = (i.a3.reg << INSTR_FORMAT::R_TYPE::rs2) | (i.a2.reg << INSTR_FORMAT::R_TYPE::rs1) |
                         FUNCT::MULDIV | (i.a1.reg << INSTR_FORMAT::R_TYPE::rd) | OPCODE::OP;
                break;

            // bitmanip extension
            case ANDN:
                binary = (i.a3.reg << INSTR_FORMAT::R_TYPE::rs2) | (i.a2.reg << INSTR_FORMAT::R_TYPE::rs1) |
                         FUNCT::ANDN | (i.a1.reg << INSTR_FORMAT::R_TYPE::rd) | OPCODE::OP;
                break;
            case ORN:
                binary = (i.a3.reg << INSTR_FORMAT::R_TYPE::rs2) | (i.a2.reg << INSTR_FORMAT::R_TYPE::rs1) |
                         FUNCT::ORN | (i.a1.reg << INSTR_FORMAT::R_TYPE::rd) | OPCODE::OP;
                break;
            case XNOR:
                binary = (i.a3.reg << INSTR_FORMAT::R_TYPE::rs2) | (i.a2.reg << INSTR_FORMAT::R_TYPE::rs1) |
                         FUNCT::XNOR | (i.a1.reg << INSTR_FORMAT::R_TYPE::rd) | OPCODE::OP;
                break;
            case ROL:
                binary = (i.a3.reg << INSTR_FORMAT::R_TYPE::rs2) | (i.a2.reg << INSTR_FORMAT::R_TYPE::rs1) |
                         FUNCT::ROL | (i.a1.reg << INSTR_FORMAT::R_TYPE::rd) | OPCODE::OP;
                break;
            case ROR:
                binary = (i.a3.reg << INSTR_FORMAT::R_TYPE::rs2) | (i.a2.reg << INSTR_FORMAT::R_TYPE::rs1) |
                         FUNCT::ROR | (i.a1.reg << INSTR_FORMAT::R_TYPE::rd) | OPCODE::OP;
                break;

            case SH1ADD:
                binary = (i.a3.reg << INSTR_FORMAT::R_TYPE::rs2) | (i.a2.reg << INSTR_FORMAT::R_TYPE::rs1) |
                         FUNCT::SH1ADD | (i.a1.reg << INSTR_FORMAT::R_TYPE::rd) | OPCODE::OP;
                break;
            case SH2ADD:
                binary = (i.a3.reg << INSTR_FORMAT::R_TYPE::rs2) | (i.a2.reg << INSTR_FORMAT::R_TYPE::rs1) |
                         FUNCT::SH2ADD | (i.a1.reg << INSTR_FORMAT::R_TYPE::rd) | OPCODE::OP;
                break;
            case SH3ADD:
                binary = (i.a3.reg << INSTR_FORMAT::R_TYPE::rs2) | (i.a2.reg << INSTR_FORMAT::R_TYPE::rs1) |
                         FUNCT::SH3ADD | (i.a1.reg << INSTR_FORMAT::R_TYPE::rd) | OPCODE::OP;
                break;

            case BCLR:
                binary = (i.a3.reg << INSTR_FORMAT::R_TYPE::rs2) | (i.a2.reg << INSTR_FORMAT::R_TYPE::rs1) |
                         FUNCT::BCLR | (i.a1.reg << INSTR_FORMAT::R_TYPE::rd) | OPCODE::OP;
                break;
            case BSET:
                binary = (i.a3.reg << INSTR_FORMAT::R_TYPE::rs2) | (i.a2.reg << INSTR_FORMAT::R_TYPE::rs1) |
                         FUNCT::BSET | (i.a1.reg << INSTR_FORMAT::R_TYPE::rd) | OPCODE::OP;
                break;
            case BINV:
                binary = (i.a3.reg << INSTR_FORMAT::R_TYPE::rs2) | (i.a2.reg << INSTR_FORMAT::R_TYPE::rs1) |
                         FUNCT::BINV | (i.a1.reg << INSTR_FORMAT::R_TYPE::rd) | OPCODE::OP;
                break;
            case BEXT:
                binary = (i.a3.reg << INSTR_FORMAT::R_TYPE::rs2) | (i.a2.reg << INSTR_FORMAT::R_TYPE::rs1) |
                         FUNCT::BEXT | (i.a1.reg << INSTR_FORMAT::R_TYPE::rd) | OPCODE::OP;
                break;

            case RORI:
                binary = (i.a3.imm << INSTR_FORMAT::I_TYPE::imm) | (i.a2.reg << INSTR_FORMAT::I_TYPE::rs1) |
                         FUNCT::ROR | (i.a1.reg << INSTR_FORMAT::I_TYPE::rd) | OPCODE::OP_IMM;
                break;

            case BCLRI:
                binary = (i.a3.imm << INSTR_FORMAT::I_TYPE::imm) | (i.a2.reg << INSTR_FORMAT::I_TYPE::rs1) |
                         FUNCT::BCLR | (i.a1.reg << INSTR_FORMAT::I_TYPE::rd) | OPCODE::OP_IMM;
                break;
            case BSETI:
                binary = (i.a3.imm << INSTR_FORMAT::I_TYPE::imm) | (i.a2.reg << INSTR_FORMAT::I_TYPE::rs1) |
                         FUNCT::BSET | (i.a1.reg << INSTR_FORMAT::I_TYPE::rd) | OPCODE::OP_IMM;
                break;
            case BINVI:
                binary = (i.a3.imm << INSTR_FORMAT::I_TYPE::imm) | (i.a2.reg << INSTR_FORMAT::I_TYPE::rs1) |
                         FUNCT::BINV | (i.a1.reg << INSTR_FORMAT::I_TYPE::rd) | OPCODE::OP_IMM;
                break;
            case BEXTI:
                binary = (i.a3.imm << INSTR_FORMAT::I_TYPE::imm) | (i.a2.reg << INSTR_FORMAT::I_TYPE::rs1) |
                         FUNCT::BEXT | (i.a1.reg << INSTR_FORMAT::I_TYPE::rd) | OPCODE::OP_IMM;
                break;

            case CLZ:
                binary = (i.a2.reg << INSTR_FORMAT::I_TYPE::rs1) | (i.a1.reg << INSTR_FORMAT::I_TYPE::rd) | FUNCT::CLZ |
                         OPCODE::OP_IMM;
                break;
            case CTZ:
                binary = (i.a2.reg << INSTR_FORMAT::I_TYPE::rs1) | (i.a1.reg << INSTR_FORMAT::I_TYPE::rd) | FUNCT::CTZ |
                         OPCODE::OP_IMM;
                break;
            case CPOP:
                binary = (i.a2.reg << INSTR_FORMAT::I_TYPE::rs1) | (i.a1.reg << INSTR_FORMAT::I_TYPE::rd) |
                         FUNCT::CPOP | OPCODE::OP_IMM;
                break;
            case SEXT_B:
                binary = (i.a2.reg << INSTR_FORMAT::I_TYPE::rs1) | (i.a1.reg << INSTR_FORMAT::I_TYPE::rd) |
                         FUNCT::SEXT_B | OPCODE::OP_IMM;
                break;
            case SEXT_H:
                binary = (i.a2.reg << INSTR_FORMAT::I_TYPE::rs1) | (i.a1.reg << INSTR_FORMAT::I_TYPE::rd) |
                         FUNCT::SEXT_H | OPCODE::OP_IMM;
                break;
            case REV8:
                binary = (i.a2.reg << INSTR_FORMAT::I_TYPE::rs1) | (i.a1.reg << INSTR_FORMAT::I_TYPE::rd) |
                         FUNCT::REV8 | OPCODE::OP_IMM;
                break;
            case ORC_B:
                binary = (i.a2.reg << INSTR_FORMAT::I_TYPE::rs1) | (i.a1.reg << INSTR_FORMAT::I_TYPE::rd) |
                         FUNCT::ORC_B | OPCODE::OP_IMM;
                break;

            case ZEXT_H:
                binary = (i.a2.reg << INSTR_FORMAT::R_TYPE::rs1) | (i.a1.reg << INSTR_FORMAT::R_TYPE::rd) |
                         FUNCT::ZEXT_H | OPCODE::OP;
                break;

            case CLMUL:
                binary = (i.a3.reg << INSTR_FORMAT::R_TYPE::rs2) | (i.a2.reg << INSTR_FORMAT::R_TYPE::rs1) |
                         FUNCT::CLMUL | (i.a1.reg << INSTR_FORMAT::R_TYPE::rd) | OPCODE::OP;
                break;
            case CLMULR:
                binary = (i.a3.reg << INSTR_FORMAT::R_TYPE::rs2) | (i.a2.reg << INSTR_FORMAT::R_TYPE::rs1) |
                         FUNCT::CLMULR | (i.a1.reg << INSTR_FORMAT::R_TYPE::rd) | OPCODE::OP;
                break;
            case CLMULH:
                binary = (i.a3.reg << INSTR_FORMAT::R_TYPE::rs2) | (i.a2.reg << INSTR_FORMAT::R_TYPE::rs1) |
                         FUNCT::CLMULH | (i.a1.reg << INSTR_FORMAT::R_TYPE::rd) | OPCODE::OP;
                break;
            case MIN:
                binary = (i.a3.reg << INSTR_FORMAT::R_TYPE::rs2) | (i.a2.reg << INSTR_FORMAT::R_TYPE::rs1) |
                         FUNCT::MIN | (i.a1.reg << INSTR_FORMAT::R_TYPE::rd) | OPCODE::OP;
                break;
            case MINU:
                binary = (i.a3.reg << INSTR_FORMAT::R_TYPE::rs2) | (i.a2.reg << INSTR_FORMAT::R_TYPE::rs1) |
                         FUNCT::MINU | (i.a1.reg << INSTR_FORMAT::R_TYPE::rd) | OPCODE::OP;
                break;
            case MAX:
                binary = (i.a3.reg << INSTR_FORMAT::R_TYPE::rs2) | (i.a2.reg << INSTR_FORMAT::R_TYPE::rs1) |
                         FUNCT::MAX | (i.a1.reg << INSTR_FORMAT::R_TYPE::rd) | OPCODE::OP;
                break;
            case MAXU:
                binary = (i.a3.reg << INSTR_FORMAT::R_TYPE::rs2) | (i.a2.reg << INSTR_FORMAT::R_TYPE::rs1) |
                         FUNCT::MAXU | (i.a1.reg << INSTR_FORMAT::R_TYPE::rd) | OPCODE::OP;
                break;

            // vector extension - load / store
            case VLE8_V:
                binary = (0b000 << INSTR_FORMAT::VL_TYPE::nf) | (0b0 << INSTR_FORMAT::VL_TYPE::mew) |
                         (0b00 << INSTR_FORMAT::VL_TYPE::mop) | (0b1 << INSTR_FORMAT::VL_TYPE::vm) |
                         (0b00000 << INSTR_FORMAT::VL_TYPE::lumop) | (i.a2.reg << INSTR_FORMAT::VL_TYPE::rs1) |
                         (0b000 << INSTR_FORMAT::VL_TYPE::width) | (i.a1.reg << INSTR_FORMAT::VL_TYPE::vd) | OPCODE::VL;
                break;
            case VSE8_V:
                binary = (0b000 << INSTR_FORMAT::VS_TYPE::nf) | (0b0 << INSTR_FORMAT::VS_TYPE::mew) |
                         (0b00 << INSTR_FORMAT::VS_TYPE::mop) | (0b1 << INSTR_FORMAT::VS_TYPE::vm) |
                         (0b00000 << INSTR_FORMAT::VS_TYPE::sumop) | (i.a2.reg << INSTR_FORMAT::VS_TYPE::rs1) |
                         (0b000 << INSTR_FORMAT::VS_TYPE::width) | (i.a1.reg << INSTR_FORMAT::VS_TYPE::vs3) |
                         OPCODE::VS;
                break;

            // vector extension - op
            case VADD_VV:
                binary = (FUNCT6::VADD << INSTR_FORMAT::OPVV_TYPE::funct6) | (0b1 << INSTR_FORMAT::OPVV_TYPE::vm) |
                         (i.a2.reg << INSTR_FORMAT::OPVV_TYPE::vs2) | (i.a3.reg << INSTR_FORMAT::OPVV_TYPE::vs1) |
                         (FUNCT3::OPIVV << INSTR_FORMAT::OPVV_TYPE::funct3) |
                         (i.a1.reg << INSTR_FORMAT::OPVV_TYPE::vd) | (OPCODE::OP_V << INSTR_FORMAT::OPVV_TYPE::opcode);
                break;
            case VMUL_VX:
                binary = (FUNCT6::VMUL << INSTR_FORMAT::OPVX_TYPE::funct6) | (0b1 << INSTR_FORMAT::OPVX_TYPE::vm) |
                         (i.a2.reg << INSTR_FORMAT::OPVX_TYPE::vs2) | (i.a3.reg << INSTR_FORMAT::OPVX_TYPE::rs1) |
                         (FUNCT3::OPIVX << INSTR_FORMAT::OPVV_TYPE::funct3) |
                         (i.a1.reg << INSTR_FORMAT::OPVX_TYPE::vd) | (OPCODE::OP_V << INSTR_FORMAT::OPVX_TYPE::opcode);
                break;
            case VMACC_VV:
                binary = (FUNCT6::VMACC << INSTR_FORMAT::OPVV_TYPE::funct6) | (0b1 << INSTR_FORMAT::OPVV_TYPE::vm) |
                         (i.a2.reg << INSTR_FORMAT::OPVV_TYPE::vs2) | (i.a3.reg << INSTR_FORMAT::OPVV_TYPE::vs1) |
                         (FUNCT3::OPIVV << INSTR_FORMAT::OPVV_TYPE::funct3) |
                         (i.a1.reg << INSTR_FORMAT::OPVV_TYPE::vd) | (OPCODE::OP_V << INSTR_FORMAT::OPVV_TYPE::opcode);
                break;

            // program terminating
            case HCF:
                binary = 0x0000000B;
                dexit = true;
                break;

            case UNIMPL:
            default:
                printf("Reached an unimplemented instruction!\n");
                if (i.psrc) printf("Instruction: %s\n", i.psrc);
                // printf( "inst: %6d pc: %6d src line: %d\n", inst_cnt, pc, i.orig_line );
                // print_regfile(rf);
                dexit = true;
                break;
        }

        if (i.psrc) fprintf(inst_file, "%s\n", i.psrc);

        fprintf(mch_file, "%02x\n", (binary >> 0) & 0xff);
        fprintf(mch_file, "%02x\n", (binary >> 8) & 0xff);
        fprintf(mch_file, "%02x\n", (binary >> 16) & 0xff);
        fprintf(mch_file, "%02x\n", (binary >> 24) & 0xff);

        inst_cnt++;
    }

    // write "hcf" in the inst_file
    fprintf(inst_file, "hcf");

    // write data to data.hex
    write_data_hex(mem, data_file);

    fclose(inst_file);
    fclose(mch_file);
    fclose(data_file);
}
