// Verilated -*- C++ -*-
// DESCRIPTION: Verilator output: Design implementation internals
// See VPRNG.h for the primary calling header

#include "VPRNG.h"
#include "VPRNG__Syms.h"

//==========

VL_CTOR_IMP(VPRNG) {
    VPRNG__Syms* __restrict vlSymsp = __VlSymsp = new VPRNG__Syms(this, name());
    VPRNG* __restrict vlTOPp VL_ATTR_UNUSED = vlSymsp->TOPp;
    // Reset internal values
    
    // Reset structure values
    _ctor_var_reset();
}

void VPRNG::__Vconfigure(VPRNG__Syms* vlSymsp, bool first) {
    if (0 && first) {}  // Prevent unused
    this->__VlSymsp = vlSymsp;
}

VPRNG::~VPRNG() {
    delete __VlSymsp; __VlSymsp=NULL;
}

void VPRNG::eval() {
    VL_DEBUG_IF(VL_DBG_MSGF("+++++TOP Evaluate VPRNG::eval\n"); );
    VPRNG__Syms* __restrict vlSymsp = this->__VlSymsp;  // Setup global symbol table
    VPRNG* __restrict vlTOPp VL_ATTR_UNUSED = vlSymsp->TOPp;
#ifdef VL_DEBUG
    // Debug assertions
    _eval_debug_assertions();
#endif  // VL_DEBUG
    // Initialize
    if (VL_UNLIKELY(!vlSymsp->__Vm_didInit)) _eval_initial_loop(vlSymsp);
    // Evaluate till stable
    int __VclockLoop = 0;
    QData __Vchange = 1;
    do {
        VL_DEBUG_IF(VL_DBG_MSGF("+ Clock loop\n"););
        vlSymsp->__Vm_activity = true;
        _eval(vlSymsp);
        if (VL_UNLIKELY(++__VclockLoop > 100)) {
            // About to fail, so enable debug to see what's not settling.
            // Note you must run make with OPT=-DVL_DEBUG for debug prints.
            int __Vsaved_debug = Verilated::debug();
            Verilated::debug(1);
            __Vchange = _change_request(vlSymsp);
            Verilated::debug(__Vsaved_debug);
            VL_FATAL_MT("PRNG.v", 101, "",
                "Verilated model didn't converge\n"
                "- See DIDNOTCONVERGE in the Verilator manual");
        } else {
            __Vchange = _change_request(vlSymsp);
        }
    } while (VL_UNLIKELY(__Vchange));
}

void VPRNG::_eval_initial_loop(VPRNG__Syms* __restrict vlSymsp) {
    vlSymsp->__Vm_didInit = true;
    _eval_initial(vlSymsp);
    vlSymsp->__Vm_activity = true;
    // Evaluate till stable
    int __VclockLoop = 0;
    QData __Vchange = 1;
    do {
        _eval_settle(vlSymsp);
        _eval(vlSymsp);
        if (VL_UNLIKELY(++__VclockLoop > 100)) {
            // About to fail, so enable debug to see what's not settling.
            // Note you must run make with OPT=-DVL_DEBUG for debug prints.
            int __Vsaved_debug = Verilated::debug();
            Verilated::debug(1);
            __Vchange = _change_request(vlSymsp);
            Verilated::debug(__Vsaved_debug);
            VL_FATAL_MT("PRNG.v", 101, "",
                "Verilated model didn't DC converge\n"
                "- See DIDNOTCONVERGE in the Verilator manual");
        } else {
            __Vchange = _change_request(vlSymsp);
        }
    } while (VL_UNLIKELY(__Vchange));
}

VL_INLINE_OPT void VPRNG::_sequent__TOP__1(VPRNG__Syms* __restrict vlSymsp) {
    VL_DEBUG_IF(VL_DBG_MSGF("+    VPRNG::_sequent__TOP__1\n"); );
    VPRNG* __restrict vlTOPp VL_ATTR_UNUSED = vlSymsp->TOPp;
    // Variables
    CData/*3:0*/ __Vdly__PRNG__DOT__lfsr_0;
    CData/*3:0*/ __Vdly__PRNG__DOT__lfsr_1;
    CData/*3:0*/ __Vdly__PRNG__DOT__lfsr_2;
    CData/*3:0*/ __Vdly__PRNG__DOT__lfsr_3;
    CData/*0:0*/ __Vdly__PRNG__DOT__state;
    CData/*0:0*/ __Vdly__PRNG__DOT__lfsrInst__DOT__shiftReg_2;
    CData/*0:0*/ __Vdly__PRNG__DOT__lfsrInst__DOT__shiftReg_3;
    // Body
    __Vdly__PRNG__DOT__lfsrInst__DOT__shiftReg_3 = vlTOPp->PRNG__DOT__lfsrInst__DOT__shiftReg_3;
    __Vdly__PRNG__DOT__lfsrInst__DOT__shiftReg_2 = vlTOPp->PRNG__DOT__lfsrInst__DOT__shiftReg_2;
    __Vdly__PRNG__DOT__lfsr_3 = vlTOPp->PRNG__DOT__lfsr_3;
    __Vdly__PRNG__DOT__lfsr_2 = vlTOPp->PRNG__DOT__lfsr_2;
    __Vdly__PRNG__DOT__lfsr_1 = vlTOPp->PRNG__DOT__lfsr_1;
    __Vdly__PRNG__DOT__lfsr_0 = vlTOPp->PRNG__DOT__lfsr_0;
    __Vdly__PRNG__DOT__state = vlTOPp->PRNG__DOT__state;
    __Vdly__PRNG__DOT__lfsrInst__DOT__shiftReg_2 = 
        ((~ (IData)(vlTOPp->reset)) & ((~ (IData)(vlTOPp->PRNG__DOT__lfsrInst_io_seed_valid)) 
                                       & ((IData)(vlTOPp->PRNG__DOT__lfsrInst__DOT__shiftReg_3) 
                                          ^ (IData)(vlTOPp->PRNG__DOT__lfsrInst__DOT__shiftReg_0))));
    __Vdly__PRNG__DOT__lfsrInst__DOT__shiftReg_3 = 
        ((~ (IData)(vlTOPp->reset)) & ((~ (IData)(vlTOPp->PRNG__DOT__lfsrInst_io_seed_valid)) 
                                       & (IData)(vlTOPp->PRNG__DOT__lfsrInst__DOT__shiftReg_0)));
    vlTOPp->PRNG__DOT__cnt2 = ((IData)(vlTOPp->reset)
                                ? 0U : ((IData)(vlTOPp->PRNG__DOT__state)
                                         ? (IData)(vlTOPp->PRNG__DOT___GEN_4)
                                         : ((0U == (IData)(vlTOPp->PRNG__DOT__cnt))
                                             ? (IData)(vlTOPp->PRNG__DOT___GEN_4)
                                             : (IData)(vlTOPp->PRNG__DOT___T_14))));
    if (vlTOPp->reset) {
        vlTOPp->PRNG__DOT__myReg = 0U;
    } else {
        if ((1U & (~ (IData)(vlTOPp->PRNG__DOT__state)))) {
            if ((0U != (IData)(vlTOPp->PRNG__DOT__cnt))) {
                vlTOPp->PRNG__DOT__myReg = ((((IData)(vlTOPp->PRNG__DOT__lfsrInst__DOT__shiftReg_3) 
                                              << 3U) 
                                             | ((IData)(vlTOPp->PRNG__DOT__lfsrInst__DOT__shiftReg_2) 
                                                << 2U)) 
                                            | (((IData)(vlTOPp->PRNG__DOT__lfsrInst__DOT__shiftReg_1) 
                                                << 1U) 
                                               | (IData)(vlTOPp->PRNG__DOT__lfsrInst__DOT__shiftReg_0)));
            }
        }
    }
    if (vlTOPp->reset) {
        __Vdly__PRNG__DOT__lfsr_0 = 0U;
    } else {
        if (vlTOPp->PRNG__DOT__state) {
            __Vdly__PRNG__DOT__lfsr_0 = ((IData)(vlTOPp->io_gen)
                                          ? 0U : (((IData)(vlTOPp->PRNG__DOT__lfsr_0) 
                                                   == (IData)(vlTOPp->PRNG__DOT__lfsr_3))
                                                   ? (IData)(vlTOPp->PRNG__DOT___T_20)
                                                   : (IData)(vlTOPp->PRNG__DOT___GEN_84)));
        } else {
            if (vlTOPp->PRNG__DOT___T) {
                if ((0U != (IData)(vlTOPp->PRNG__DOT__cnt))) {
                    __Vdly__PRNG__DOT__lfsr_0 = vlTOPp->PRNG__DOT___GEN_62;
                }
            }
        }
    }
    if (vlTOPp->reset) {
        __Vdly__PRNG__DOT__lfsr_1 = 0U;
    } else {
        if (vlTOPp->PRNG__DOT__state) {
            __Vdly__PRNG__DOT__lfsr_1 = ((IData)(vlTOPp->io_gen)
                                          ? 0U : (((IData)(vlTOPp->PRNG__DOT__lfsr_1) 
                                                   == (IData)(vlTOPp->PRNG__DOT__lfsr_3))
                                                   ? (IData)(vlTOPp->PRNG__DOT___T_29)
                                                   : (IData)(vlTOPp->PRNG__DOT___GEN_87)));
        } else {
            if (vlTOPp->PRNG__DOT___T) {
                if ((0U != (IData)(vlTOPp->PRNG__DOT__cnt))) {
                    __Vdly__PRNG__DOT__lfsr_1 = vlTOPp->PRNG__DOT___GEN_63;
                }
            }
        }
    }
    if (vlTOPp->reset) {
        __Vdly__PRNG__DOT__lfsr_2 = 0U;
    } else {
        if (vlTOPp->PRNG__DOT__state) {
            __Vdly__PRNG__DOT__lfsr_2 = ((IData)(vlTOPp->io_gen)
                                          ? 0U : (((IData)(vlTOPp->PRNG__DOT__lfsr_2) 
                                                   == (IData)(vlTOPp->PRNG__DOT__lfsr_3))
                                                   ? (IData)(vlTOPp->PRNG__DOT___T_38)
                                                   : (IData)(vlTOPp->PRNG__DOT___GEN_90)));
        } else {
            if (vlTOPp->PRNG__DOT___T) {
                if ((0U != (IData)(vlTOPp->PRNG__DOT__cnt))) {
                    __Vdly__PRNG__DOT__lfsr_2 = vlTOPp->PRNG__DOT___GEN_64;
                }
            }
        }
    }
    if (vlTOPp->reset) {
        __Vdly__PRNG__DOT__lfsr_3 = 0U;
    } else {
        if (vlTOPp->PRNG__DOT__state) {
            __Vdly__PRNG__DOT__lfsr_3 = ((IData)(vlTOPp->io_gen)
                                          ? 0U : (((IData)(vlTOPp->PRNG__DOT__lfsr_3) 
                                                   == (IData)(vlTOPp->PRNG__DOT__lfsr_2))
                                                   ? (IData)(vlTOPp->PRNG__DOT___T_47)
                                                   : (IData)(vlTOPp->PRNG__DOT___GEN_93)));
        } else {
            if (vlTOPp->PRNG__DOT___T) {
                if ((0U != (IData)(vlTOPp->PRNG__DOT__cnt))) {
                    __Vdly__PRNG__DOT__lfsr_3 = vlTOPp->PRNG__DOT___GEN_65;
                }
            }
        }
    }
    if (vlTOPp->reset) {
        __Vdly__PRNG__DOT__state = 0U;
    } else {
        if (vlTOPp->PRNG__DOT___T) {
            __Vdly__PRNG__DOT__state = vlTOPp->PRNG__DOT___GEN_1;
        } else {
            if (vlTOPp->PRNG__DOT__state) {
                if (vlTOPp->io_gen) {
                    __Vdly__PRNG__DOT__state = 0U;
                }
            }
        }
    }
    vlTOPp->PRNG__DOT__lfsrInst__DOT__shiftReg_3 = __Vdly__PRNG__DOT__lfsrInst__DOT__shiftReg_3;
    vlTOPp->PRNG__DOT__lfsr_3 = __Vdly__PRNG__DOT__lfsr_3;
    vlTOPp->PRNG__DOT__lfsr_2 = __Vdly__PRNG__DOT__lfsr_2;
    vlTOPp->PRNG__DOT__lfsr_0 = __Vdly__PRNG__DOT__lfsr_0;
    vlTOPp->PRNG__DOT__lfsr_1 = __Vdly__PRNG__DOT__lfsr_1;
    vlTOPp->PRNG__DOT___T_14 = (3U & ((IData)(1U) + (IData)(vlTOPp->PRNG__DOT__cnt2)));
    vlTOPp->PRNG__DOT__lfsrInst__DOT__shiftReg_0 = 
        ((~ (IData)(vlTOPp->reset)) & ((IData)(vlTOPp->PRNG__DOT__lfsrInst_io_seed_valid) 
                                       | (IData)(vlTOPp->PRNG__DOT__lfsrInst__DOT__shiftReg_1)));
    vlTOPp->PRNG__DOT___T_47 = (0xfU & ((IData)(1U) 
                                        + (IData)(vlTOPp->PRNG__DOT__lfsr_3)));
    vlTOPp->PRNG__DOT___GEN_65 = ((0xaU == (IData)(vlTOPp->PRNG__DOT__myReg))
                                   ? ((3U == (IData)(vlTOPp->PRNG__DOT__cnt2))
                                       ? 0U : (IData)(vlTOPp->PRNG__DOT__lfsr_3))
                                   : ((0xbU == (IData)(vlTOPp->PRNG__DOT__myReg))
                                       ? ((3U == (IData)(vlTOPp->PRNG__DOT__cnt2))
                                           ? 1U : (IData)(vlTOPp->PRNG__DOT__lfsr_3))
                                       : ((0xcU == (IData)(vlTOPp->PRNG__DOT__myReg))
                                           ? ((3U == (IData)(vlTOPp->PRNG__DOT__cnt2))
                                               ? 2U
                                               : (IData)(vlTOPp->PRNG__DOT__lfsr_3))
                                           : ((0xdU 
                                               == (IData)(vlTOPp->PRNG__DOT__myReg))
                                               ? ((3U 
                                                   == (IData)(vlTOPp->PRNG__DOT__cnt2))
                                                   ? 3U
                                                   : (IData)(vlTOPp->PRNG__DOT__lfsr_3))
                                               : ((0xeU 
                                                   == (IData)(vlTOPp->PRNG__DOT__myReg))
                                                   ? 
                                                  ((3U 
                                                    == (IData)(vlTOPp->PRNG__DOT__cnt2))
                                                    ? 4U
                                                    : (IData)(vlTOPp->PRNG__DOT__lfsr_3))
                                                   : 
                                                  ((0xfU 
                                                    == (IData)(vlTOPp->PRNG__DOT__myReg))
                                                    ? 
                                                   ((3U 
                                                     == (IData)(vlTOPp->PRNG__DOT__cnt2))
                                                     ? 5U
                                                     : (IData)(vlTOPp->PRNG__DOT__lfsr_3))
                                                    : 
                                                   ((3U 
                                                     == (IData)(vlTOPp->PRNG__DOT__cnt2))
                                                     ? (IData)(vlTOPp->PRNG__DOT__myReg)
                                                     : (IData)(vlTOPp->PRNG__DOT__lfsr_3))))))));
    vlTOPp->PRNG__DOT___T_38 = (0xfU & ((IData)(1U) 
                                        + (IData)(vlTOPp->PRNG__DOT__lfsr_2)));
    vlTOPp->PRNG__DOT___GEN_64 = ((0xaU == (IData)(vlTOPp->PRNG__DOT__myReg))
                                   ? ((2U == (IData)(vlTOPp->PRNG__DOT__cnt2))
                                       ? 0U : (IData)(vlTOPp->PRNG__DOT__lfsr_2))
                                   : ((0xbU == (IData)(vlTOPp->PRNG__DOT__myReg))
                                       ? ((2U == (IData)(vlTOPp->PRNG__DOT__cnt2))
                                           ? 1U : (IData)(vlTOPp->PRNG__DOT__lfsr_2))
                                       : ((0xcU == (IData)(vlTOPp->PRNG__DOT__myReg))
                                           ? ((2U == (IData)(vlTOPp->PRNG__DOT__cnt2))
                                               ? 2U
                                               : (IData)(vlTOPp->PRNG__DOT__lfsr_2))
                                           : ((0xdU 
                                               == (IData)(vlTOPp->PRNG__DOT__myReg))
                                               ? ((2U 
                                                   == (IData)(vlTOPp->PRNG__DOT__cnt2))
                                                   ? 3U
                                                   : (IData)(vlTOPp->PRNG__DOT__lfsr_2))
                                               : ((0xeU 
                                                   == (IData)(vlTOPp->PRNG__DOT__myReg))
                                                   ? 
                                                  ((2U 
                                                    == (IData)(vlTOPp->PRNG__DOT__cnt2))
                                                    ? 4U
                                                    : (IData)(vlTOPp->PRNG__DOT__lfsr_2))
                                                   : 
                                                  ((0xfU 
                                                    == (IData)(vlTOPp->PRNG__DOT__myReg))
                                                    ? 
                                                   ((2U 
                                                     == (IData)(vlTOPp->PRNG__DOT__cnt2))
                                                     ? 5U
                                                     : (IData)(vlTOPp->PRNG__DOT__lfsr_2))
                                                    : 
                                                   ((2U 
                                                     == (IData)(vlTOPp->PRNG__DOT__cnt2))
                                                     ? (IData)(vlTOPp->PRNG__DOT__myReg)
                                                     : (IData)(vlTOPp->PRNG__DOT__lfsr_2))))))));
    vlTOPp->PRNG__DOT___T_20 = (0xfU & ((IData)(1U) 
                                        + (IData)(vlTOPp->PRNG__DOT__lfsr_0)));
    vlTOPp->PRNG__DOT___GEN_62 = ((0xaU == (IData)(vlTOPp->PRNG__DOT__myReg))
                                   ? ((0U == (IData)(vlTOPp->PRNG__DOT__cnt2))
                                       ? 0U : (IData)(vlTOPp->PRNG__DOT__lfsr_0))
                                   : ((0xbU == (IData)(vlTOPp->PRNG__DOT__myReg))
                                       ? ((0U == (IData)(vlTOPp->PRNG__DOT__cnt2))
                                           ? 1U : (IData)(vlTOPp->PRNG__DOT__lfsr_0))
                                       : ((0xcU == (IData)(vlTOPp->PRNG__DOT__myReg))
                                           ? ((0U == (IData)(vlTOPp->PRNG__DOT__cnt2))
                                               ? 2U
                                               : (IData)(vlTOPp->PRNG__DOT__lfsr_0))
                                           : ((0xdU 
                                               == (IData)(vlTOPp->PRNG__DOT__myReg))
                                               ? ((0U 
                                                   == (IData)(vlTOPp->PRNG__DOT__cnt2))
                                                   ? 3U
                                                   : (IData)(vlTOPp->PRNG__DOT__lfsr_0))
                                               : ((0xeU 
                                                   == (IData)(vlTOPp->PRNG__DOT__myReg))
                                                   ? 
                                                  ((0U 
                                                    == (IData)(vlTOPp->PRNG__DOT__cnt2))
                                                    ? 4U
                                                    : (IData)(vlTOPp->PRNG__DOT__lfsr_0))
                                                   : 
                                                  ((0xfU 
                                                    == (IData)(vlTOPp->PRNG__DOT__myReg))
                                                    ? 
                                                   ((0U 
                                                     == (IData)(vlTOPp->PRNG__DOT__cnt2))
                                                     ? 5U
                                                     : (IData)(vlTOPp->PRNG__DOT__lfsr_0))
                                                    : 
                                                   ((0U 
                                                     == (IData)(vlTOPp->PRNG__DOT__cnt2))
                                                     ? (IData)(vlTOPp->PRNG__DOT__myReg)
                                                     : (IData)(vlTOPp->PRNG__DOT__lfsr_0))))))));
    vlTOPp->PRNG__DOT___T_29 = (0xfU & ((IData)(1U) 
                                        + (IData)(vlTOPp->PRNG__DOT__lfsr_1)));
    vlTOPp->PRNG__DOT___GEN_63 = ((0xaU == (IData)(vlTOPp->PRNG__DOT__myReg))
                                   ? ((1U == (IData)(vlTOPp->PRNG__DOT__cnt2))
                                       ? 0U : (IData)(vlTOPp->PRNG__DOT__lfsr_1))
                                   : ((0xbU == (IData)(vlTOPp->PRNG__DOT__myReg))
                                       ? ((1U == (IData)(vlTOPp->PRNG__DOT__cnt2))
                                           ? 1U : (IData)(vlTOPp->PRNG__DOT__lfsr_1))
                                       : ((0xcU == (IData)(vlTOPp->PRNG__DOT__myReg))
                                           ? ((1U == (IData)(vlTOPp->PRNG__DOT__cnt2))
                                               ? 2U
                                               : (IData)(vlTOPp->PRNG__DOT__lfsr_1))
                                           : ((0xdU 
                                               == (IData)(vlTOPp->PRNG__DOT__myReg))
                                               ? ((1U 
                                                   == (IData)(vlTOPp->PRNG__DOT__cnt2))
                                                   ? 3U
                                                   : (IData)(vlTOPp->PRNG__DOT__lfsr_1))
                                               : ((0xeU 
                                                   == (IData)(vlTOPp->PRNG__DOT__myReg))
                                                   ? 
                                                  ((1U 
                                                    == (IData)(vlTOPp->PRNG__DOT__cnt2))
                                                    ? 4U
                                                    : (IData)(vlTOPp->PRNG__DOT__lfsr_1))
                                                   : 
                                                  ((0xfU 
                                                    == (IData)(vlTOPp->PRNG__DOT__myReg))
                                                    ? 
                                                   ((1U 
                                                     == (IData)(vlTOPp->PRNG__DOT__cnt2))
                                                     ? 5U
                                                     : (IData)(vlTOPp->PRNG__DOT__lfsr_1))
                                                    : 
                                                   ((1U 
                                                     == (IData)(vlTOPp->PRNG__DOT__cnt2))
                                                     ? (IData)(vlTOPp->PRNG__DOT__myReg)
                                                     : (IData)(vlTOPp->PRNG__DOT__lfsr_1))))))));
    if (vlTOPp->reset) {
        vlTOPp->PRNG__DOT__cnt = 0U;
    } else {
        if ((1U & (~ (IData)(vlTOPp->PRNG__DOT__state)))) {
            vlTOPp->PRNG__DOT__cnt = vlTOPp->PRNG__DOT___T_16;
        }
    }
    vlTOPp->PRNG__DOT__state = __Vdly__PRNG__DOT__state;
    vlTOPp->PRNG__DOT__lfsrInst__DOT__shiftReg_1 = 
        ((~ (IData)(vlTOPp->reset)) & ((~ (IData)(vlTOPp->PRNG__DOT__lfsrInst_io_seed_valid)) 
                                       & (IData)(vlTOPp->PRNG__DOT__lfsrInst__DOT__shiftReg_2)));
    vlTOPp->PRNG__DOT___T_16 = (0xffU & ((IData)(1U) 
                                         + (IData)(vlTOPp->PRNG__DOT__cnt)));
    vlTOPp->PRNG__DOT___T = (1U & (~ (IData)(vlTOPp->PRNG__DOT__state)));
    vlTOPp->PRNG__DOT___GEN_1 = ((3U == (IData)(vlTOPp->PRNG__DOT__cnt2)) 
                                 | (IData)(vlTOPp->PRNG__DOT__state));
    vlTOPp->PRNG__DOT___GEN_4 = ((IData)(vlTOPp->PRNG__DOT__state)
                                  ? (IData)(vlTOPp->PRNG__DOT__cnt2)
                                  : ((3U == (IData)(vlTOPp->PRNG__DOT__cnt2))
                                      ? 0U : (IData)(vlTOPp->PRNG__DOT__cnt2)));
    vlTOPp->PRNG__DOT___GEN_84 = (0xfU & (((IData)(vlTOPp->PRNG__DOT__lfsr_0) 
                                           == (IData)(vlTOPp->PRNG__DOT__lfsr_2))
                                           ? ((IData)(1U) 
                                              + (IData)(vlTOPp->PRNG__DOT__lfsr_0))
                                           : (((IData)(vlTOPp->PRNG__DOT__lfsr_0) 
                                               == (IData)(vlTOPp->PRNG__DOT__lfsr_1))
                                               ? ((IData)(1U) 
                                                  + (IData)(vlTOPp->PRNG__DOT__lfsr_0))
                                               : ((IData)(vlTOPp->PRNG__DOT__state)
                                                   ? (IData)(vlTOPp->PRNG__DOT__lfsr_0)
                                                   : 
                                                  ((0U 
                                                    == (IData)(vlTOPp->PRNG__DOT__cnt))
                                                    ? (IData)(vlTOPp->PRNG__DOT__lfsr_0)
                                                    : (IData)(vlTOPp->PRNG__DOT___GEN_62))))));
    vlTOPp->PRNG__DOT___GEN_87 = (0xfU & (((IData)(vlTOPp->PRNG__DOT__lfsr_1) 
                                           == (IData)(vlTOPp->PRNG__DOT__lfsr_2))
                                           ? ((IData)(1U) 
                                              + (IData)(vlTOPp->PRNG__DOT__lfsr_1))
                                           : (((IData)(vlTOPp->PRNG__DOT__lfsr_1) 
                                               == (IData)(vlTOPp->PRNG__DOT__lfsr_0))
                                               ? ((IData)(1U) 
                                                  + (IData)(vlTOPp->PRNG__DOT__lfsr_1))
                                               : ((IData)(vlTOPp->PRNG__DOT__state)
                                                   ? (IData)(vlTOPp->PRNG__DOT__lfsr_1)
                                                   : 
                                                  ((0U 
                                                    == (IData)(vlTOPp->PRNG__DOT__cnt))
                                                    ? (IData)(vlTOPp->PRNG__DOT__lfsr_1)
                                                    : (IData)(vlTOPp->PRNG__DOT___GEN_63))))));
    vlTOPp->PRNG__DOT___GEN_90 = (0xfU & (((IData)(vlTOPp->PRNG__DOT__lfsr_2) 
                                           == (IData)(vlTOPp->PRNG__DOT__lfsr_1))
                                           ? ((IData)(1U) 
                                              + (IData)(vlTOPp->PRNG__DOT__lfsr_2))
                                           : (((IData)(vlTOPp->PRNG__DOT__lfsr_2) 
                                               == (IData)(vlTOPp->PRNG__DOT__lfsr_0))
                                               ? ((IData)(1U) 
                                                  + (IData)(vlTOPp->PRNG__DOT__lfsr_2))
                                               : ((IData)(vlTOPp->PRNG__DOT__state)
                                                   ? (IData)(vlTOPp->PRNG__DOT__lfsr_2)
                                                   : 
                                                  ((0U 
                                                    == (IData)(vlTOPp->PRNG__DOT__cnt))
                                                    ? (IData)(vlTOPp->PRNG__DOT__lfsr_2)
                                                    : (IData)(vlTOPp->PRNG__DOT___GEN_64))))));
    vlTOPp->PRNG__DOT___GEN_93 = (0xfU & (((IData)(vlTOPp->PRNG__DOT__lfsr_3) 
                                           == (IData)(vlTOPp->PRNG__DOT__lfsr_1))
                                           ? ((IData)(1U) 
                                              + (IData)(vlTOPp->PRNG__DOT__lfsr_3))
                                           : (((IData)(vlTOPp->PRNG__DOT__lfsr_3) 
                                               == (IData)(vlTOPp->PRNG__DOT__lfsr_0))
                                               ? ((IData)(1U) 
                                                  + (IData)(vlTOPp->PRNG__DOT__lfsr_3))
                                               : ((IData)(vlTOPp->PRNG__DOT__state)
                                                   ? (IData)(vlTOPp->PRNG__DOT__lfsr_3)
                                                   : 
                                                  ((0U 
                                                    == (IData)(vlTOPp->PRNG__DOT__cnt))
                                                    ? (IData)(vlTOPp->PRNG__DOT__lfsr_3)
                                                    : (IData)(vlTOPp->PRNG__DOT___GEN_65))))));
    vlTOPp->PRNG__DOT__lfsrInst__DOT__shiftReg_2 = __Vdly__PRNG__DOT__lfsrInst__DOT__shiftReg_2;
    vlTOPp->PRNG__DOT__lfsrInst_io_seed_valid = ((~ (IData)(vlTOPp->PRNG__DOT__state)) 
                                                 & (0U 
                                                    == (IData)(vlTOPp->PRNG__DOT__cnt)));
}

void VPRNG::_settle__TOP__2(VPRNG__Syms* __restrict vlSymsp) {
    VL_DEBUG_IF(VL_DBG_MSGF("+    VPRNG::_settle__TOP__2\n"); );
    VPRNG* __restrict vlTOPp VL_ATTR_UNUSED = vlSymsp->TOPp;
    // Body
    vlTOPp->PRNG__DOT___T_14 = (3U & ((IData)(1U) + (IData)(vlTOPp->PRNG__DOT__cnt2)));
    vlTOPp->PRNG__DOT___T_20 = (0xfU & ((IData)(1U) 
                                        + (IData)(vlTOPp->PRNG__DOT__lfsr_0)));
    vlTOPp->PRNG__DOT___T_29 = (0xfU & ((IData)(1U) 
                                        + (IData)(vlTOPp->PRNG__DOT__lfsr_1)));
    vlTOPp->PRNG__DOT___T_38 = (0xfU & ((IData)(1U) 
                                        + (IData)(vlTOPp->PRNG__DOT__lfsr_2)));
    vlTOPp->PRNG__DOT___T_47 = (0xfU & ((IData)(1U) 
                                        + (IData)(vlTOPp->PRNG__DOT__lfsr_3)));
    vlTOPp->PRNG__DOT___GEN_62 = ((0xaU == (IData)(vlTOPp->PRNG__DOT__myReg))
                                   ? ((0U == (IData)(vlTOPp->PRNG__DOT__cnt2))
                                       ? 0U : (IData)(vlTOPp->PRNG__DOT__lfsr_0))
                                   : ((0xbU == (IData)(vlTOPp->PRNG__DOT__myReg))
                                       ? ((0U == (IData)(vlTOPp->PRNG__DOT__cnt2))
                                           ? 1U : (IData)(vlTOPp->PRNG__DOT__lfsr_0))
                                       : ((0xcU == (IData)(vlTOPp->PRNG__DOT__myReg))
                                           ? ((0U == (IData)(vlTOPp->PRNG__DOT__cnt2))
                                               ? 2U
                                               : (IData)(vlTOPp->PRNG__DOT__lfsr_0))
                                           : ((0xdU 
                                               == (IData)(vlTOPp->PRNG__DOT__myReg))
                                               ? ((0U 
                                                   == (IData)(vlTOPp->PRNG__DOT__cnt2))
                                                   ? 3U
                                                   : (IData)(vlTOPp->PRNG__DOT__lfsr_0))
                                               : ((0xeU 
                                                   == (IData)(vlTOPp->PRNG__DOT__myReg))
                                                   ? 
                                                  ((0U 
                                                    == (IData)(vlTOPp->PRNG__DOT__cnt2))
                                                    ? 4U
                                                    : (IData)(vlTOPp->PRNG__DOT__lfsr_0))
                                                   : 
                                                  ((0xfU 
                                                    == (IData)(vlTOPp->PRNG__DOT__myReg))
                                                    ? 
                                                   ((0U 
                                                     == (IData)(vlTOPp->PRNG__DOT__cnt2))
                                                     ? 5U
                                                     : (IData)(vlTOPp->PRNG__DOT__lfsr_0))
                                                    : 
                                                   ((0U 
                                                     == (IData)(vlTOPp->PRNG__DOT__cnt2))
                                                     ? (IData)(vlTOPp->PRNG__DOT__myReg)
                                                     : (IData)(vlTOPp->PRNG__DOT__lfsr_0))))))));
    vlTOPp->PRNG__DOT___GEN_63 = ((0xaU == (IData)(vlTOPp->PRNG__DOT__myReg))
                                   ? ((1U == (IData)(vlTOPp->PRNG__DOT__cnt2))
                                       ? 0U : (IData)(vlTOPp->PRNG__DOT__lfsr_1))
                                   : ((0xbU == (IData)(vlTOPp->PRNG__DOT__myReg))
                                       ? ((1U == (IData)(vlTOPp->PRNG__DOT__cnt2))
                                           ? 1U : (IData)(vlTOPp->PRNG__DOT__lfsr_1))
                                       : ((0xcU == (IData)(vlTOPp->PRNG__DOT__myReg))
                                           ? ((1U == (IData)(vlTOPp->PRNG__DOT__cnt2))
                                               ? 2U
                                               : (IData)(vlTOPp->PRNG__DOT__lfsr_1))
                                           : ((0xdU 
                                               == (IData)(vlTOPp->PRNG__DOT__myReg))
                                               ? ((1U 
                                                   == (IData)(vlTOPp->PRNG__DOT__cnt2))
                                                   ? 3U
                                                   : (IData)(vlTOPp->PRNG__DOT__lfsr_1))
                                               : ((0xeU 
                                                   == (IData)(vlTOPp->PRNG__DOT__myReg))
                                                   ? 
                                                  ((1U 
                                                    == (IData)(vlTOPp->PRNG__DOT__cnt2))
                                                    ? 4U
                                                    : (IData)(vlTOPp->PRNG__DOT__lfsr_1))
                                                   : 
                                                  ((0xfU 
                                                    == (IData)(vlTOPp->PRNG__DOT__myReg))
                                                    ? 
                                                   ((1U 
                                                     == (IData)(vlTOPp->PRNG__DOT__cnt2))
                                                     ? 5U
                                                     : (IData)(vlTOPp->PRNG__DOT__lfsr_1))
                                                    : 
                                                   ((1U 
                                                     == (IData)(vlTOPp->PRNG__DOT__cnt2))
                                                     ? (IData)(vlTOPp->PRNG__DOT__myReg)
                                                     : (IData)(vlTOPp->PRNG__DOT__lfsr_1))))))));
    vlTOPp->PRNG__DOT___GEN_64 = ((0xaU == (IData)(vlTOPp->PRNG__DOT__myReg))
                                   ? ((2U == (IData)(vlTOPp->PRNG__DOT__cnt2))
                                       ? 0U : (IData)(vlTOPp->PRNG__DOT__lfsr_2))
                                   : ((0xbU == (IData)(vlTOPp->PRNG__DOT__myReg))
                                       ? ((2U == (IData)(vlTOPp->PRNG__DOT__cnt2))
                                           ? 1U : (IData)(vlTOPp->PRNG__DOT__lfsr_2))
                                       : ((0xcU == (IData)(vlTOPp->PRNG__DOT__myReg))
                                           ? ((2U == (IData)(vlTOPp->PRNG__DOT__cnt2))
                                               ? 2U
                                               : (IData)(vlTOPp->PRNG__DOT__lfsr_2))
                                           : ((0xdU 
                                               == (IData)(vlTOPp->PRNG__DOT__myReg))
                                               ? ((2U 
                                                   == (IData)(vlTOPp->PRNG__DOT__cnt2))
                                                   ? 3U
                                                   : (IData)(vlTOPp->PRNG__DOT__lfsr_2))
                                               : ((0xeU 
                                                   == (IData)(vlTOPp->PRNG__DOT__myReg))
                                                   ? 
                                                  ((2U 
                                                    == (IData)(vlTOPp->PRNG__DOT__cnt2))
                                                    ? 4U
                                                    : (IData)(vlTOPp->PRNG__DOT__lfsr_2))
                                                   : 
                                                  ((0xfU 
                                                    == (IData)(vlTOPp->PRNG__DOT__myReg))
                                                    ? 
                                                   ((2U 
                                                     == (IData)(vlTOPp->PRNG__DOT__cnt2))
                                                     ? 5U
                                                     : (IData)(vlTOPp->PRNG__DOT__lfsr_2))
                                                    : 
                                                   ((2U 
                                                     == (IData)(vlTOPp->PRNG__DOT__cnt2))
                                                     ? (IData)(vlTOPp->PRNG__DOT__myReg)
                                                     : (IData)(vlTOPp->PRNG__DOT__lfsr_2))))))));
    vlTOPp->PRNG__DOT___GEN_65 = ((0xaU == (IData)(vlTOPp->PRNG__DOT__myReg))
                                   ? ((3U == (IData)(vlTOPp->PRNG__DOT__cnt2))
                                       ? 0U : (IData)(vlTOPp->PRNG__DOT__lfsr_3))
                                   : ((0xbU == (IData)(vlTOPp->PRNG__DOT__myReg))
                                       ? ((3U == (IData)(vlTOPp->PRNG__DOT__cnt2))
                                           ? 1U : (IData)(vlTOPp->PRNG__DOT__lfsr_3))
                                       : ((0xcU == (IData)(vlTOPp->PRNG__DOT__myReg))
                                           ? ((3U == (IData)(vlTOPp->PRNG__DOT__cnt2))
                                               ? 2U
                                               : (IData)(vlTOPp->PRNG__DOT__lfsr_3))
                                           : ((0xdU 
                                               == (IData)(vlTOPp->PRNG__DOT__myReg))
                                               ? ((3U 
                                                   == (IData)(vlTOPp->PRNG__DOT__cnt2))
                                                   ? 3U
                                                   : (IData)(vlTOPp->PRNG__DOT__lfsr_3))
                                               : ((0xeU 
                                                   == (IData)(vlTOPp->PRNG__DOT__myReg))
                                                   ? 
                                                  ((3U 
                                                    == (IData)(vlTOPp->PRNG__DOT__cnt2))
                                                    ? 4U
                                                    : (IData)(vlTOPp->PRNG__DOT__lfsr_3))
                                                   : 
                                                  ((0xfU 
                                                    == (IData)(vlTOPp->PRNG__DOT__myReg))
                                                    ? 
                                                   ((3U 
                                                     == (IData)(vlTOPp->PRNG__DOT__cnt2))
                                                     ? 5U
                                                     : (IData)(vlTOPp->PRNG__DOT__lfsr_3))
                                                    : 
                                                   ((3U 
                                                     == (IData)(vlTOPp->PRNG__DOT__cnt2))
                                                     ? (IData)(vlTOPp->PRNG__DOT__myReg)
                                                     : (IData)(vlTOPp->PRNG__DOT__lfsr_3))))))));
    vlTOPp->PRNG__DOT___T = (1U & (~ (IData)(vlTOPp->PRNG__DOT__state)));
    vlTOPp->PRNG__DOT___T_16 = (0xffU & ((IData)(1U) 
                                         + (IData)(vlTOPp->PRNG__DOT__cnt)));
    vlTOPp->PRNG__DOT___GEN_1 = ((3U == (IData)(vlTOPp->PRNG__DOT__cnt2)) 
                                 | (IData)(vlTOPp->PRNG__DOT__state));
    vlTOPp->PRNG__DOT___GEN_4 = ((IData)(vlTOPp->PRNG__DOT__state)
                                  ? (IData)(vlTOPp->PRNG__DOT__cnt2)
                                  : ((3U == (IData)(vlTOPp->PRNG__DOT__cnt2))
                                      ? 0U : (IData)(vlTOPp->PRNG__DOT__cnt2)));
    vlTOPp->io_ready = ((IData)(vlTOPp->PRNG__DOT__state) 
                        & (~ (IData)(vlTOPp->io_gen)));
    vlTOPp->io_puzzle_0 = ((IData)(vlTOPp->PRNG__DOT__state)
                            ? ((IData)(vlTOPp->io_gen)
                                ? (IData)(vlTOPp->PRNG__DOT__lfsr_0)
                                : 0U) : 0U);
    vlTOPp->io_puzzle_1 = ((IData)(vlTOPp->PRNG__DOT__state)
                            ? ((IData)(vlTOPp->io_gen)
                                ? (IData)(vlTOPp->PRNG__DOT__lfsr_1)
                                : 0U) : 0U);
    vlTOPp->io_puzzle_2 = ((IData)(vlTOPp->PRNG__DOT__state)
                            ? ((IData)(vlTOPp->io_gen)
                                ? (IData)(vlTOPp->PRNG__DOT__lfsr_2)
                                : 0U) : 0U);
    vlTOPp->io_puzzle_3 = ((IData)(vlTOPp->PRNG__DOT__state)
                            ? ((IData)(vlTOPp->io_gen)
                                ? (IData)(vlTOPp->PRNG__DOT__lfsr_3)
                                : 0U) : 0U);
    vlTOPp->PRNG__DOT__lfsrInst_io_seed_valid = ((~ (IData)(vlTOPp->PRNG__DOT__state)) 
                                                 & (0U 
                                                    == (IData)(vlTOPp->PRNG__DOT__cnt)));
    vlTOPp->PRNG__DOT___GEN_84 = (0xfU & (((IData)(vlTOPp->PRNG__DOT__lfsr_0) 
                                           == (IData)(vlTOPp->PRNG__DOT__lfsr_2))
                                           ? ((IData)(1U) 
                                              + (IData)(vlTOPp->PRNG__DOT__lfsr_0))
                                           : (((IData)(vlTOPp->PRNG__DOT__lfsr_0) 
                                               == (IData)(vlTOPp->PRNG__DOT__lfsr_1))
                                               ? ((IData)(1U) 
                                                  + (IData)(vlTOPp->PRNG__DOT__lfsr_0))
                                               : ((IData)(vlTOPp->PRNG__DOT__state)
                                                   ? (IData)(vlTOPp->PRNG__DOT__lfsr_0)
                                                   : 
                                                  ((0U 
                                                    == (IData)(vlTOPp->PRNG__DOT__cnt))
                                                    ? (IData)(vlTOPp->PRNG__DOT__lfsr_0)
                                                    : (IData)(vlTOPp->PRNG__DOT___GEN_62))))));
    vlTOPp->PRNG__DOT___GEN_87 = (0xfU & (((IData)(vlTOPp->PRNG__DOT__lfsr_1) 
                                           == (IData)(vlTOPp->PRNG__DOT__lfsr_2))
                                           ? ((IData)(1U) 
                                              + (IData)(vlTOPp->PRNG__DOT__lfsr_1))
                                           : (((IData)(vlTOPp->PRNG__DOT__lfsr_1) 
                                               == (IData)(vlTOPp->PRNG__DOT__lfsr_0))
                                               ? ((IData)(1U) 
                                                  + (IData)(vlTOPp->PRNG__DOT__lfsr_1))
                                               : ((IData)(vlTOPp->PRNG__DOT__state)
                                                   ? (IData)(vlTOPp->PRNG__DOT__lfsr_1)
                                                   : 
                                                  ((0U 
                                                    == (IData)(vlTOPp->PRNG__DOT__cnt))
                                                    ? (IData)(vlTOPp->PRNG__DOT__lfsr_1)
                                                    : (IData)(vlTOPp->PRNG__DOT___GEN_63))))));
    vlTOPp->PRNG__DOT___GEN_90 = (0xfU & (((IData)(vlTOPp->PRNG__DOT__lfsr_2) 
                                           == (IData)(vlTOPp->PRNG__DOT__lfsr_1))
                                           ? ((IData)(1U) 
                                              + (IData)(vlTOPp->PRNG__DOT__lfsr_2))
                                           : (((IData)(vlTOPp->PRNG__DOT__lfsr_2) 
                                               == (IData)(vlTOPp->PRNG__DOT__lfsr_0))
                                               ? ((IData)(1U) 
                                                  + (IData)(vlTOPp->PRNG__DOT__lfsr_2))
                                               : ((IData)(vlTOPp->PRNG__DOT__state)
                                                   ? (IData)(vlTOPp->PRNG__DOT__lfsr_2)
                                                   : 
                                                  ((0U 
                                                    == (IData)(vlTOPp->PRNG__DOT__cnt))
                                                    ? (IData)(vlTOPp->PRNG__DOT__lfsr_2)
                                                    : (IData)(vlTOPp->PRNG__DOT___GEN_64))))));
    vlTOPp->PRNG__DOT___GEN_93 = (0xfU & (((IData)(vlTOPp->PRNG__DOT__lfsr_3) 
                                           == (IData)(vlTOPp->PRNG__DOT__lfsr_1))
                                           ? ((IData)(1U) 
                                              + (IData)(vlTOPp->PRNG__DOT__lfsr_3))
                                           : (((IData)(vlTOPp->PRNG__DOT__lfsr_3) 
                                               == (IData)(vlTOPp->PRNG__DOT__lfsr_0))
                                               ? ((IData)(1U) 
                                                  + (IData)(vlTOPp->PRNG__DOT__lfsr_3))
                                               : ((IData)(vlTOPp->PRNG__DOT__state)
                                                   ? (IData)(vlTOPp->PRNG__DOT__lfsr_3)
                                                   : 
                                                  ((0U 
                                                    == (IData)(vlTOPp->PRNG__DOT__cnt))
                                                    ? (IData)(vlTOPp->PRNG__DOT__lfsr_3)
                                                    : (IData)(vlTOPp->PRNG__DOT___GEN_65))))));
}

VL_INLINE_OPT void VPRNG::_combo__TOP__3(VPRNG__Syms* __restrict vlSymsp) {
    VL_DEBUG_IF(VL_DBG_MSGF("+    VPRNG::_combo__TOP__3\n"); );
    VPRNG* __restrict vlTOPp VL_ATTR_UNUSED = vlSymsp->TOPp;
    // Body
    vlTOPp->io_ready = ((IData)(vlTOPp->PRNG__DOT__state) 
                        & (~ (IData)(vlTOPp->io_gen)));
    vlTOPp->io_puzzle_0 = ((IData)(vlTOPp->PRNG__DOT__state)
                            ? ((IData)(vlTOPp->io_gen)
                                ? (IData)(vlTOPp->PRNG__DOT__lfsr_0)
                                : 0U) : 0U);
    vlTOPp->io_puzzle_1 = ((IData)(vlTOPp->PRNG__DOT__state)
                            ? ((IData)(vlTOPp->io_gen)
                                ? (IData)(vlTOPp->PRNG__DOT__lfsr_1)
                                : 0U) : 0U);
    vlTOPp->io_puzzle_2 = ((IData)(vlTOPp->PRNG__DOT__state)
                            ? ((IData)(vlTOPp->io_gen)
                                ? (IData)(vlTOPp->PRNG__DOT__lfsr_2)
                                : 0U) : 0U);
    vlTOPp->io_puzzle_3 = ((IData)(vlTOPp->PRNG__DOT__state)
                            ? ((IData)(vlTOPp->io_gen)
                                ? (IData)(vlTOPp->PRNG__DOT__lfsr_3)
                                : 0U) : 0U);
}

void VPRNG::_eval(VPRNG__Syms* __restrict vlSymsp) {
    VL_DEBUG_IF(VL_DBG_MSGF("+    VPRNG::_eval\n"); );
    VPRNG* __restrict vlTOPp VL_ATTR_UNUSED = vlSymsp->TOPp;
    // Body
    if (((IData)(vlTOPp->clock) & (~ (IData)(vlTOPp->__Vclklast__TOP__clock)))) {
        vlTOPp->_sequent__TOP__1(vlSymsp);
        vlTOPp->__Vm_traceActivity = (2U | vlTOPp->__Vm_traceActivity);
    }
    vlTOPp->_combo__TOP__3(vlSymsp);
    // Final
    vlTOPp->__Vclklast__TOP__clock = vlTOPp->clock;
}

void VPRNG::_eval_initial(VPRNG__Syms* __restrict vlSymsp) {
    VL_DEBUG_IF(VL_DBG_MSGF("+    VPRNG::_eval_initial\n"); );
    VPRNG* __restrict vlTOPp VL_ATTR_UNUSED = vlSymsp->TOPp;
    // Body
    vlTOPp->__Vclklast__TOP__clock = vlTOPp->clock;
}

void VPRNG::final() {
    VL_DEBUG_IF(VL_DBG_MSGF("+    VPRNG::final\n"); );
    // Variables
    VPRNG__Syms* __restrict vlSymsp = this->__VlSymsp;
    VPRNG* __restrict vlTOPp VL_ATTR_UNUSED = vlSymsp->TOPp;
}

void VPRNG::_eval_settle(VPRNG__Syms* __restrict vlSymsp) {
    VL_DEBUG_IF(VL_DBG_MSGF("+    VPRNG::_eval_settle\n"); );
    VPRNG* __restrict vlTOPp VL_ATTR_UNUSED = vlSymsp->TOPp;
    // Body
    vlTOPp->_settle__TOP__2(vlSymsp);
    vlTOPp->__Vm_traceActivity = (1U | vlTOPp->__Vm_traceActivity);
}

VL_INLINE_OPT QData VPRNG::_change_request(VPRNG__Syms* __restrict vlSymsp) {
    VL_DEBUG_IF(VL_DBG_MSGF("+    VPRNG::_change_request\n"); );
    VPRNG* __restrict vlTOPp VL_ATTR_UNUSED = vlSymsp->TOPp;
    // Body
    // Change detection
    QData __req = false;  // Logically a bool
    return __req;
}

#ifdef VL_DEBUG
void VPRNG::_eval_debug_assertions() {
    VL_DEBUG_IF(VL_DBG_MSGF("+    VPRNG::_eval_debug_assertions\n"); );
    // Body
    if (VL_UNLIKELY((clock & 0xfeU))) {
        Verilated::overWidthError("clock");}
    if (VL_UNLIKELY((reset & 0xfeU))) {
        Verilated::overWidthError("reset");}
    if (VL_UNLIKELY((io_gen & 0xfeU))) {
        Verilated::overWidthError("io_gen");}
}
#endif  // VL_DEBUG

void VPRNG::_ctor_var_reset() {
    VL_DEBUG_IF(VL_DBG_MSGF("+    VPRNG::_ctor_var_reset\n"); );
    // Body
    clock = VL_RAND_RESET_I(1);
    reset = VL_RAND_RESET_I(1);
    io_gen = VL_RAND_RESET_I(1);
    io_puzzle_0 = VL_RAND_RESET_I(4);
    io_puzzle_1 = VL_RAND_RESET_I(4);
    io_puzzle_2 = VL_RAND_RESET_I(4);
    io_puzzle_3 = VL_RAND_RESET_I(4);
    io_ready = VL_RAND_RESET_I(1);
    PRNG__DOT__lfsrInst_io_seed_valid = VL_RAND_RESET_I(1);
    PRNG__DOT__myReg = VL_RAND_RESET_I(4);
    PRNG__DOT__cnt = VL_RAND_RESET_I(8);
    PRNG__DOT__cnt2 = VL_RAND_RESET_I(2);
    PRNG__DOT__lfsr_0 = VL_RAND_RESET_I(4);
    PRNG__DOT__lfsr_1 = VL_RAND_RESET_I(4);
    PRNG__DOT__lfsr_2 = VL_RAND_RESET_I(4);
    PRNG__DOT__lfsr_3 = VL_RAND_RESET_I(4);
    PRNG__DOT__state = VL_RAND_RESET_I(1);
    PRNG__DOT___T = VL_RAND_RESET_I(1);
    PRNG__DOT___GEN_1 = VL_RAND_RESET_I(1);
    PRNG__DOT___GEN_4 = VL_RAND_RESET_I(2);
    PRNG__DOT___GEN_62 = VL_RAND_RESET_I(4);
    PRNG__DOT___GEN_63 = VL_RAND_RESET_I(4);
    PRNG__DOT___GEN_64 = VL_RAND_RESET_I(4);
    PRNG__DOT___GEN_65 = VL_RAND_RESET_I(4);
    PRNG__DOT___T_14 = VL_RAND_RESET_I(2);
    PRNG__DOT___T_16 = VL_RAND_RESET_I(8);
    PRNG__DOT___T_20 = VL_RAND_RESET_I(4);
    PRNG__DOT___GEN_84 = VL_RAND_RESET_I(4);
    PRNG__DOT___T_29 = VL_RAND_RESET_I(4);
    PRNG__DOT___GEN_87 = VL_RAND_RESET_I(4);
    PRNG__DOT___T_38 = VL_RAND_RESET_I(4);
    PRNG__DOT___GEN_90 = VL_RAND_RESET_I(4);
    PRNG__DOT___T_47 = VL_RAND_RESET_I(4);
    PRNG__DOT___GEN_93 = VL_RAND_RESET_I(4);
    PRNG__DOT__lfsrInst__DOT__shiftReg_0 = VL_RAND_RESET_I(1);
    PRNG__DOT__lfsrInst__DOT__shiftReg_1 = VL_RAND_RESET_I(1);
    PRNG__DOT__lfsrInst__DOT__shiftReg_2 = VL_RAND_RESET_I(1);
    PRNG__DOT__lfsrInst__DOT__shiftReg_3 = VL_RAND_RESET_I(1);
    __Vm_traceActivity = 0;
}
