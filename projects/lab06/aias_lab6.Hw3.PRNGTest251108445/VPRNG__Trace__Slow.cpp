// Verilated -*- C++ -*-
// DESCRIPTION: Verilator output: Tracing implementation internals
#include "verilated_vcd_c.h"
#include "VPRNG__Syms.h"


//======================

void VPRNG::trace(VerilatedVcdC* tfp, int, int) {
    tfp->spTrace()->addCallback(&VPRNG::traceInit, &VPRNG::traceFull, &VPRNG::traceChg, this);
}
void VPRNG::traceInit(VerilatedVcd* vcdp, void* userthis, uint32_t code) {
    // Callback from vcd->open()
    VPRNG* t = (VPRNG*)userthis;
    VPRNG__Syms* __restrict vlSymsp = t->__VlSymsp;  // Setup global symbol table
    if (!Verilated::calcUnusedSigs()) {
        VL_FATAL_MT(__FILE__, __LINE__, __FILE__,
                        "Turning on wave traces requires Verilated::traceEverOn(true) call before time 0.");
    }
    vcdp->scopeEscape(' ');
    t->traceInitThis(vlSymsp, vcdp, code);
    vcdp->scopeEscape('.');
}
void VPRNG::traceFull(VerilatedVcd* vcdp, void* userthis, uint32_t code) {
    // Callback from vcd->dump()
    VPRNG* t = (VPRNG*)userthis;
    VPRNG__Syms* __restrict vlSymsp = t->__VlSymsp;  // Setup global symbol table
    t->traceFullThis(vlSymsp, vcdp, code);
}

//======================


void VPRNG::traceInitThis(VPRNG__Syms* __restrict vlSymsp, VerilatedVcd* vcdp, uint32_t code) {
    VPRNG* __restrict vlTOPp VL_ATTR_UNUSED = vlSymsp->TOPp;
    int c = code;
    if (0 && vcdp && c) {}  // Prevent unused
    vcdp->module(vlSymsp->name());  // Setup signal names
    // Body
    {
        vlTOPp->traceInitThis__1(vlSymsp, vcdp, code);
    }
}

void VPRNG::traceFullThis(VPRNG__Syms* __restrict vlSymsp, VerilatedVcd* vcdp, uint32_t code) {
    VPRNG* __restrict vlTOPp VL_ATTR_UNUSED = vlSymsp->TOPp;
    int c = code;
    if (0 && vcdp && c) {}  // Prevent unused
    // Body
    {
        vlTOPp->traceFullThis__1(vlSymsp, vcdp, code);
    }
    // Final
    vlTOPp->__Vm_traceActivity = 0U;
}

void VPRNG::traceInitThis__1(VPRNG__Syms* __restrict vlSymsp, VerilatedVcd* vcdp, uint32_t code) {
    VPRNG* __restrict vlTOPp VL_ATTR_UNUSED = vlSymsp->TOPp;
    int c = code;
    if (0 && vcdp && c) {}  // Prevent unused
    // Body
    {
        vcdp->declBit(c+129,"clock", false,-1);
        vcdp->declBit(c+137,"reset", false,-1);
        vcdp->declBit(c+145,"io_gen", false,-1);
        vcdp->declBus(c+153,"io_puzzle_0", false,-1, 3,0);
        vcdp->declBus(c+161,"io_puzzle_1", false,-1, 3,0);
        vcdp->declBus(c+169,"io_puzzle_2", false,-1, 3,0);
        vcdp->declBus(c+177,"io_puzzle_3", false,-1, 3,0);
        vcdp->declBit(c+185,"io_ready", false,-1);
        vcdp->declBit(c+129,"PRNG clock", false,-1);
        vcdp->declBit(c+137,"PRNG reset", false,-1);
        vcdp->declBit(c+145,"PRNG io_gen", false,-1);
        vcdp->declBus(c+153,"PRNG io_puzzle_0", false,-1, 3,0);
        vcdp->declBus(c+161,"PRNG io_puzzle_1", false,-1, 3,0);
        vcdp->declBus(c+169,"PRNG io_puzzle_2", false,-1, 3,0);
        vcdp->declBus(c+177,"PRNG io_puzzle_3", false,-1, 3,0);
        vcdp->declBit(c+185,"PRNG io_ready", false,-1);
        vcdp->declBit(c+129,"PRNG lfsrInst_clock", false,-1);
        vcdp->declBit(c+137,"PRNG lfsrInst_reset", false,-1);
        vcdp->declBit(c+1,"PRNG lfsrInst_io_seed_valid", false,-1);
        vcdp->declBus(c+9,"PRNG lfsrInst_io_rndNum", false,-1, 3,0);
        vcdp->declBus(c+17,"PRNG myReg", false,-1, 3,0);
        vcdp->declBus(c+25,"PRNG cnt", false,-1, 7,0);
        vcdp->declBus(c+33,"PRNG cnt2", false,-1, 1,0);
        vcdp->declBus(c+41,"PRNG lfsr_0", false,-1, 3,0);
        vcdp->declBus(c+49,"PRNG lfsr_1", false,-1, 3,0);
        vcdp->declBus(c+57,"PRNG lfsr_2", false,-1, 3,0);
        vcdp->declBus(c+65,"PRNG lfsr_3", false,-1, 3,0);
        vcdp->declBit(c+73,"PRNG state", false,-1);
        vcdp->declBit(c+129,"PRNG lfsrInst clock", false,-1);
        vcdp->declBit(c+137,"PRNG lfsrInst reset", false,-1);
        vcdp->declBit(c+1,"PRNG lfsrInst io_seed_valid", false,-1);
        vcdp->declBus(c+9,"PRNG lfsrInst io_rndNum", false,-1, 3,0);
        vcdp->declBit(c+81,"PRNG lfsrInst shiftReg_0", false,-1);
        vcdp->declBit(c+89,"PRNG lfsrInst shiftReg_1", false,-1);
        vcdp->declBit(c+97,"PRNG lfsrInst shiftReg_2", false,-1);
        vcdp->declBit(c+105,"PRNG lfsrInst shiftReg_3", false,-1);
        vcdp->declBus(c+113,"PRNG lfsrInst lo", false,-1, 1,0);
        vcdp->declBus(c+121,"PRNG lfsrInst hi", false,-1, 1,0);
    }
}

void VPRNG::traceFullThis__1(VPRNG__Syms* __restrict vlSymsp, VerilatedVcd* vcdp, uint32_t code) {
    VPRNG* __restrict vlTOPp VL_ATTR_UNUSED = vlSymsp->TOPp;
    int c = code;
    if (0 && vcdp && c) {}  // Prevent unused
    // Body
    {
        vcdp->fullBit(c+1,(vlTOPp->PRNG__DOT__lfsrInst_io_seed_valid));
        vcdp->fullBus(c+9,(((((IData)(vlTOPp->PRNG__DOT__lfsrInst__DOT__shiftReg_3) 
                              << 3U) | ((IData)(vlTOPp->PRNG__DOT__lfsrInst__DOT__shiftReg_2) 
                                        << 2U)) | (
                                                   ((IData)(vlTOPp->PRNG__DOT__lfsrInst__DOT__shiftReg_1) 
                                                    << 1U) 
                                                   | (IData)(vlTOPp->PRNG__DOT__lfsrInst__DOT__shiftReg_0)))),4);
        vcdp->fullBus(c+17,(vlTOPp->PRNG__DOT__myReg),4);
        vcdp->fullBus(c+25,(vlTOPp->PRNG__DOT__cnt),8);
        vcdp->fullBus(c+33,(vlTOPp->PRNG__DOT__cnt2),2);
        vcdp->fullBus(c+41,(vlTOPp->PRNG__DOT__lfsr_0),4);
        vcdp->fullBus(c+49,(vlTOPp->PRNG__DOT__lfsr_1),4);
        vcdp->fullBus(c+57,(vlTOPp->PRNG__DOT__lfsr_2),4);
        vcdp->fullBus(c+65,(vlTOPp->PRNG__DOT__lfsr_3),4);
        vcdp->fullBit(c+73,(vlTOPp->PRNG__DOT__state));
        vcdp->fullBit(c+81,(vlTOPp->PRNG__DOT__lfsrInst__DOT__shiftReg_0));
        vcdp->fullBit(c+89,(vlTOPp->PRNG__DOT__lfsrInst__DOT__shiftReg_1));
        vcdp->fullBit(c+97,(vlTOPp->PRNG__DOT__lfsrInst__DOT__shiftReg_2));
        vcdp->fullBit(c+105,(vlTOPp->PRNG__DOT__lfsrInst__DOT__shiftReg_3));
        vcdp->fullBus(c+113,((((IData)(vlTOPp->PRNG__DOT__lfsrInst__DOT__shiftReg_1) 
                               << 1U) | (IData)(vlTOPp->PRNG__DOT__lfsrInst__DOT__shiftReg_0))),2);
        vcdp->fullBus(c+121,((((IData)(vlTOPp->PRNG__DOT__lfsrInst__DOT__shiftReg_3) 
                               << 1U) | (IData)(vlTOPp->PRNG__DOT__lfsrInst__DOT__shiftReg_2))),2);
        vcdp->fullBit(c+129,(vlTOPp->clock));
        vcdp->fullBit(c+137,(vlTOPp->reset));
        vcdp->fullBit(c+145,(vlTOPp->io_gen));
        vcdp->fullBus(c+153,(vlTOPp->io_puzzle_0),4);
        vcdp->fullBus(c+161,(vlTOPp->io_puzzle_1),4);
        vcdp->fullBus(c+169,(vlTOPp->io_puzzle_2),4);
        vcdp->fullBus(c+177,(vlTOPp->io_puzzle_3),4);
        vcdp->fullBit(c+185,(vlTOPp->io_ready));
    }
}
