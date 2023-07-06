// Verilated -*- C++ -*-
// DESCRIPTION: Verilator output: Primary design header
//
// This header should be included by all source files instantiating the design.
// The class here is then constructed to instantiate the design.
// See the Verilator manual for examples.

#ifndef _VPRNG_H_
#define _VPRNG_H_  // guard

#include "verilated.h"

//==========

class VPRNG__Syms;
class VPRNG_VerilatedVcd;


//----------

VL_MODULE(VPRNG) {
  public:
    
    // PORTS
    // The application code writes and reads these signals to
    // propagate new values into/out from the Verilated model.
    VL_IN8(clock,0,0);
    VL_IN8(reset,0,0);
    VL_IN8(io_gen,0,0);
    VL_OUT8(io_puzzle_0,3,0);
    VL_OUT8(io_puzzle_1,3,0);
    VL_OUT8(io_puzzle_2,3,0);
    VL_OUT8(io_puzzle_3,3,0);
    VL_OUT8(io_ready,0,0);
    
    // LOCAL SIGNALS
    // Internals; generally not touched by application code
    CData/*0:0*/ PRNG__DOT__lfsrInst_io_seed_valid;
    CData/*3:0*/ PRNG__DOT__myReg;
    CData/*7:0*/ PRNG__DOT__cnt;
    CData/*1:0*/ PRNG__DOT__cnt2;
    CData/*3:0*/ PRNG__DOT__lfsr_0;
    CData/*3:0*/ PRNG__DOT__lfsr_1;
    CData/*3:0*/ PRNG__DOT__lfsr_2;
    CData/*3:0*/ PRNG__DOT__lfsr_3;
    CData/*0:0*/ PRNG__DOT__state;
    CData/*0:0*/ PRNG__DOT___T;
    CData/*0:0*/ PRNG__DOT___GEN_1;
    CData/*1:0*/ PRNG__DOT___GEN_4;
    CData/*3:0*/ PRNG__DOT___GEN_62;
    CData/*3:0*/ PRNG__DOT___GEN_63;
    CData/*3:0*/ PRNG__DOT___GEN_64;
    CData/*3:0*/ PRNG__DOT___GEN_65;
    CData/*1:0*/ PRNG__DOT___T_14;
    CData/*7:0*/ PRNG__DOT___T_16;
    CData/*3:0*/ PRNG__DOT___T_20;
    CData/*3:0*/ PRNG__DOT___GEN_84;
    CData/*3:0*/ PRNG__DOT___T_29;
    CData/*3:0*/ PRNG__DOT___GEN_87;
    CData/*3:0*/ PRNG__DOT___T_38;
    CData/*3:0*/ PRNG__DOT___GEN_90;
    CData/*3:0*/ PRNG__DOT___T_47;
    CData/*3:0*/ PRNG__DOT___GEN_93;
    CData/*0:0*/ PRNG__DOT__lfsrInst__DOT__shiftReg_0;
    CData/*0:0*/ PRNG__DOT__lfsrInst__DOT__shiftReg_1;
    CData/*0:0*/ PRNG__DOT__lfsrInst__DOT__shiftReg_2;
    CData/*0:0*/ PRNG__DOT__lfsrInst__DOT__shiftReg_3;
    
    // LOCAL VARIABLES
    // Internals; generally not touched by application code
    CData/*0:0*/ __Vclklast__TOP__clock;
    IData/*31:0*/ __Vm_traceActivity;
    
    // INTERNAL VARIABLES
    // Internals; generally not touched by application code
    VPRNG__Syms* __VlSymsp;  // Symbol table
    
    // CONSTRUCTORS
  private:
    VL_UNCOPYABLE(VPRNG);  ///< Copying not allowed
  public:
    /// Construct the model; called by application code
    /// The special name  may be used to make a wrapper with a
    /// single model invisible with respect to DPI scope names.
    VPRNG(const char* name = "TOP");
    /// Destroy the model; called (often implicitly) by application code
    ~VPRNG();
    /// Trace signals in the model; called by application code
    void trace(VerilatedVcdC* tfp, int levels, int options = 0);
    
    // API METHODS
    /// Evaluate the model.  Application must call when inputs change.
    void eval();
    /// Simulation complete, run final blocks.  Application must call on completion.
    void final();
    
    // INTERNAL METHODS
  private:
    static void _eval_initial_loop(VPRNG__Syms* __restrict vlSymsp);
  public:
    void __Vconfigure(VPRNG__Syms* symsp, bool first);
  private:
    static QData _change_request(VPRNG__Syms* __restrict vlSymsp);
  public:
    static void _combo__TOP__3(VPRNG__Syms* __restrict vlSymsp);
  private:
    void _ctor_var_reset() VL_ATTR_COLD;
  public:
    static void _eval(VPRNG__Syms* __restrict vlSymsp);
  private:
#ifdef VL_DEBUG
    void _eval_debug_assertions();
#endif  // VL_DEBUG
  public:
    static void _eval_initial(VPRNG__Syms* __restrict vlSymsp) VL_ATTR_COLD;
    static void _eval_settle(VPRNG__Syms* __restrict vlSymsp) VL_ATTR_COLD;
    static void _sequent__TOP__1(VPRNG__Syms* __restrict vlSymsp);
    static void _settle__TOP__2(VPRNG__Syms* __restrict vlSymsp) VL_ATTR_COLD;
    static void traceChgThis(VPRNG__Syms* __restrict vlSymsp, VerilatedVcd* vcdp, uint32_t code);
    static void traceChgThis__2(VPRNG__Syms* __restrict vlSymsp, VerilatedVcd* vcdp, uint32_t code);
    static void traceChgThis__3(VPRNG__Syms* __restrict vlSymsp, VerilatedVcd* vcdp, uint32_t code);
    static void traceChgThis__4(VPRNG__Syms* __restrict vlSymsp, VerilatedVcd* vcdp, uint32_t code);
    static void traceFullThis(VPRNG__Syms* __restrict vlSymsp, VerilatedVcd* vcdp, uint32_t code) VL_ATTR_COLD;
    static void traceFullThis__1(VPRNG__Syms* __restrict vlSymsp, VerilatedVcd* vcdp, uint32_t code) VL_ATTR_COLD;
    static void traceInitThis(VPRNG__Syms* __restrict vlSymsp, VerilatedVcd* vcdp, uint32_t code) VL_ATTR_COLD;
    static void traceInitThis__1(VPRNG__Syms* __restrict vlSymsp, VerilatedVcd* vcdp, uint32_t code) VL_ATTR_COLD;
    static void traceInit(VerilatedVcd* vcdp, void* userthis, uint32_t code);
    static void traceFull(VerilatedVcd* vcdp, void* userthis, uint32_t code);
    static void traceChg(VerilatedVcd* vcdp, void* userthis, uint32_t code);
} VL_ATTR_ALIGNED(VL_CACHE_LINE_BYTES);

//----------


#endif  // guard
