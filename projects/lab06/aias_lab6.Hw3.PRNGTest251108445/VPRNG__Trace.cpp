// Verilated -*- C++ -*-
// DESCRIPTION: Verilator output: Tracing implementation internals
#include "verilated_vcd_c.h"
#include "VPRNG__Syms.h"


//======================

void VPRNG::traceChg(VerilatedVcd* vcdp, void* userthis, uint32_t code) {
    // Callback from vcd->dump()
    VPRNG* t = (VPRNG*)userthis;
    VPRNG__Syms* __restrict vlSymsp = t->__VlSymsp;  // Setup global symbol table
    if (vlSymsp->getClearActivity()) {
        t->traceChgThis(vlSymsp, vcdp, code);
    }
}

//======================


void VPRNG::traceChgThis(VPRNG__Syms* __restrict vlSymsp, VerilatedVcd* vcdp, uint32_t code) {
    VPRNG* __restrict vlTOPp VL_ATTR_UNUSED = vlSymsp->TOPp;
    int c = code;
    if (0 && vcdp && c) {}  // Prevent unused
    // Body
    {
        if (VL_UNLIKELY((1U & (vlTOPp->__Vm_traceActivity 
                               | (vlTOPp->__Vm_traceActivity 
                                  >> 1U))))) {
            vlTOPp->traceChgThis__2(vlSymsp, vcdp, code);
        }
        if (VL_UNLIKELY((2U & vlTOPp->__Vm_traceActivity))) {
            vlTOPp->traceChgThis__3(vlSymsp, vcdp, code);
        }
        vlTOPp->traceChgThis__4(vlSymsp, vcdp, code);
    }
    // Final
    vlTOPp->__Vm_traceActivity = 0U;
}

void VPRNG::traceChgThis__2(VPRNG__Syms* __restrict vlSymsp, VerilatedVcd* vcdp, uint32_t code) {
    VPRNG* __restrict vlTOPp VL_ATTR_UNUSED = vlSymsp->TOPp;
    int c = code;
    if (0 && vcdp && c) {}  // Prevent unused
    // Body
    {
        vcdp->chgBit(c+1,(vlTOPp->PRNG__DOT__lfsrInst_io_seed_valid));
    }
}

void VPRNG::traceChgThis__3(VPRNG__Syms* __restrict vlSymsp, VerilatedVcd* vcdp, uint32_t code) {
    VPRNG* __restrict vlTOPp VL_ATTR_UNUSED = vlSymsp->TOPp;
    int c = code;
    if (0 && vcdp && c) {}  // Prevent unused
    // Body
    {
        vcdp->chgBus(c+9,(((((IData)(vlTOPp->PRNG__DOT__lfsrInst__DOT__shiftReg_3) 
                             << 3U) | ((IData)(vlTOPp->PRNG__DOT__lfsrInst__DOT__shiftReg_2) 
                                       << 2U)) | (((IData)(vlTOPp->PRNG__DOT__lfsrInst__DOT__shiftReg_1) 
                                                   << 1U) 
                                                  | (IData)(vlTOPp->PRNG__DOT__lfsrInst__DOT__shiftReg_0)))),4);
        vcdp->chgBus(c+17,(vlTOPp->PRNG__DOT__myReg),4);
        vcdp->chgBus(c+25,(vlTOPp->PRNG__DOT__cnt),8);
        vcdp->chgBus(c+33,(vlTOPp->PRNG__DOT__cnt2),2);
        vcdp->chgBus(c+41,(vlTOPp->PRNG__DOT__lfsr_0),4);
        vcdp->chgBus(c+49,(vlTOPp->PRNG__DOT__lfsr_1),4);
        vcdp->chgBus(c+57,(vlTOPp->PRNG__DOT__lfsr_2),4);
        vcdp->chgBus(c+65,(vlTOPp->PRNG__DOT__lfsr_3),4);
        vcdp->chgBit(c+73,(vlTOPp->PRNG__DOT__state));
        vcdp->chgBit(c+81,(vlTOPp->PRNG__DOT__lfsrInst__DOT__shiftReg_0));
        vcdp->chgBit(c+89,(vlTOPp->PRNG__DOT__lfsrInst__DOT__shiftReg_1));
        vcdp->chgBit(c+97,(vlTOPp->PRNG__DOT__lfsrInst__DOT__shiftReg_2));
        vcdp->chgBit(c+105,(vlTOPp->PRNG__DOT__lfsrInst__DOT__shiftReg_3));
        vcdp->chgBus(c+113,((((IData)(vlTOPp->PRNG__DOT__lfsrInst__DOT__shiftReg_1) 
                              << 1U) | (IData)(vlTOPp->PRNG__DOT__lfsrInst__DOT__shiftReg_0))),2);
        vcdp->chgBus(c+121,((((IData)(vlTOPp->PRNG__DOT__lfsrInst__DOT__shiftReg_3) 
                              << 1U) | (IData)(vlTOPp->PRNG__DOT__lfsrInst__DOT__shiftReg_2))),2);
    }
}

void VPRNG::traceChgThis__4(VPRNG__Syms* __restrict vlSymsp, VerilatedVcd* vcdp, uint32_t code) {
    VPRNG* __restrict vlTOPp VL_ATTR_UNUSED = vlSymsp->TOPp;
    int c = code;
    if (0 && vcdp && c) {}  // Prevent unused
    // Body
    {
        vcdp->chgBit(c+129,(vlTOPp->clock));
        vcdp->chgBit(c+137,(vlTOPp->reset));
        vcdp->chgBit(c+145,(vlTOPp->io_gen));
        vcdp->chgBus(c+153,(vlTOPp->io_puzzle_0),4);
        vcdp->chgBus(c+161,(vlTOPp->io_puzzle_1),4);
        vcdp->chgBus(c+169,(vlTOPp->io_puzzle_2),4);
        vcdp->chgBus(c+177,(vlTOPp->io_puzzle_3),4);
        vcdp->chgBit(c+185,(vlTOPp->io_ready));
    }
}
