module LFSR_Galois(
  input        clock,
  input        reset,
  input        io_seed_valid,
  output [3:0] io_rndNum
);
`ifdef RANDOMIZE_REG_INIT
  reg [31:0] _RAND_0;
  reg [31:0] _RAND_1;
  reg [31:0] _RAND_2;
  reg [31:0] _RAND_3;
`endif // RANDOMIZE_REG_INIT
  reg  shiftReg_0; // @[PRNG.scala 12:27]
  reg  shiftReg_1; // @[PRNG.scala 12:27]
  reg  shiftReg_2; // @[PRNG.scala 12:27]
  reg  shiftReg_3; // @[PRNG.scala 12:27]
  wire  _GEN_0 = io_seed_valid | shiftReg_1; // @[PRNG.scala 14:24 PRNG.scala 15:61 PRNG.scala 20:28]
  wire [1:0] lo = {shiftReg_1,shiftReg_0}; // @[PRNG.scala 27:27]
  wire [1:0] hi = {shiftReg_3,shiftReg_2}; // @[PRNG.scala 27:27]
  assign io_rndNum = {hi,lo}; // @[PRNG.scala 27:27]
  always @(posedge clock) begin
    if (reset) begin // @[PRNG.scala 12:27]
      shiftReg_0 <= 1'h0; // @[PRNG.scala 12:27]
    end else begin
      shiftReg_0 <= _GEN_0;
    end
    if (reset) begin // @[PRNG.scala 12:27]
      shiftReg_1 <= 1'h0; // @[PRNG.scala 12:27]
    end else if (io_seed_valid) begin // @[PRNG.scala 14:24]
      shiftReg_1 <= 1'h0; // @[PRNG.scala 15:61]
    end else begin
      shiftReg_1 <= shiftReg_2; // @[PRNG.scala 20:28]
    end
    if (reset) begin // @[PRNG.scala 12:27]
      shiftReg_2 <= 1'h0; // @[PRNG.scala 12:27]
    end else if (io_seed_valid) begin // @[PRNG.scala 14:24]
      shiftReg_2 <= 1'h0; // @[PRNG.scala 15:61]
    end else begin
      shiftReg_2 <= shiftReg_3 ^ shiftReg_0; // @[PRNG.scala 24:43]
    end
    if (reset) begin // @[PRNG.scala 12:27]
      shiftReg_3 <= 1'h0; // @[PRNG.scala 12:27]
    end else if (io_seed_valid) begin // @[PRNG.scala 14:24]
      shiftReg_3 <= 1'h0; // @[PRNG.scala 15:61]
    end else begin
      shiftReg_3 <= shiftReg_0; // @[PRNG.scala 20:28]
    end
  end
// Register and memory initialization
`ifdef RANDOMIZE_GARBAGE_ASSIGN
`define RANDOMIZE
`endif
`ifdef RANDOMIZE_INVALID_ASSIGN
`define RANDOMIZE
`endif
`ifdef RANDOMIZE_REG_INIT
`define RANDOMIZE
`endif
`ifdef RANDOMIZE_MEM_INIT
`define RANDOMIZE
`endif
`ifndef RANDOM
`define RANDOM $random
`endif
`ifdef RANDOMIZE_MEM_INIT
  integer initvar;
`endif
`ifndef SYNTHESIS
`ifdef FIRRTL_BEFORE_INITIAL
`FIRRTL_BEFORE_INITIAL
`endif
initial begin
  `ifdef RANDOMIZE
    `ifdef INIT_RANDOM
      `INIT_RANDOM
    `endif
    `ifndef VERILATOR
      `ifdef RANDOMIZE_DELAY
        #`RANDOMIZE_DELAY begin end
      `else
        #0.002 begin end
      `endif
    `endif
`ifdef RANDOMIZE_REG_INIT
  _RAND_0 = {1{`RANDOM}};
  shiftReg_0 = _RAND_0[0:0];
  _RAND_1 = {1{`RANDOM}};
  shiftReg_1 = _RAND_1[0:0];
  _RAND_2 = {1{`RANDOM}};
  shiftReg_2 = _RAND_2[0:0];
  _RAND_3 = {1{`RANDOM}};
  shiftReg_3 = _RAND_3[0:0];
`endif // RANDOMIZE_REG_INIT
  `endif // RANDOMIZE
end // initial
`ifdef FIRRTL_AFTER_INITIAL
`FIRRTL_AFTER_INITIAL
`endif
`endif // SYNTHESIS
endmodule
module PRNG(
  input        clock,
  input        reset,
  input        io_gen,
  output [3:0] io_puzzle_0,
  output [3:0] io_puzzle_1,
  output [3:0] io_puzzle_2,
  output [3:0] io_puzzle_3,
  output       io_ready
);
`ifdef RANDOMIZE_REG_INIT
  reg [31:0] _RAND_0;
  reg [31:0] _RAND_1;
  reg [31:0] _RAND_2;
  reg [31:0] _RAND_3;
  reg [31:0] _RAND_4;
  reg [31:0] _RAND_5;
  reg [31:0] _RAND_6;
  reg [31:0] _RAND_7;
`endif // RANDOMIZE_REG_INIT
  wire  lfsrInst_clock; // @[PRNG.scala 77:26]
  wire  lfsrInst_reset; // @[PRNG.scala 77:26]
  wire  lfsrInst_io_seed_valid; // @[PRNG.scala 77:26]
  wire [3:0] lfsrInst_io_rndNum; // @[PRNG.scala 77:26]
  reg [3:0] myReg; // @[PRNG.scala 74:28]
  reg [7:0] cnt; // @[PRNG.scala 75:26]
  reg [1:0] cnt2; // @[PRNG.scala 76:27]
  reg [3:0] lfsr_0; // @[PRNG.scala 78:23]
  reg [3:0] lfsr_1; // @[PRNG.scala 78:23]
  reg [3:0] lfsr_2; // @[PRNG.scala 78:23]
  reg [3:0] lfsr_3; // @[PRNG.scala 78:23]
  reg  state; // @[PRNG.scala 81:28]
  wire  _T = ~state; // @[Conditional.scala 37:30]
  wire [1:0] _GEN_0 = cnt2 == 2'h3 ? 2'h0 : cnt2; // @[PRNG.scala 90:35 PRNG.scala 91:30 PRNG.scala 76:27]
  wire  _GEN_1 = cnt2 == 2'h3 | state; // @[PRNG.scala 90:35 PRNG.scala 92:31 PRNG.scala 81:28]
  wire [1:0] _GEN_4 = _T ? _GEN_0 : cnt2; // @[Conditional.scala 40:58 PRNG.scala 76:27]
  wire  _T_5 = cnt == 8'h0; // @[PRNG.scala 103:25]
  wire [3:0] _GEN_6 = 2'h0 == cnt2 ? 4'h0 : lfsr_0; // @[PRNG.scala 111:54 PRNG.scala 111:54 PRNG.scala 78:23]
  wire [3:0] _GEN_7 = 2'h1 == cnt2 ? 4'h0 : lfsr_1; // @[PRNG.scala 111:54 PRNG.scala 111:54 PRNG.scala 78:23]
  wire [3:0] _GEN_8 = 2'h2 == cnt2 ? 4'h0 : lfsr_2; // @[PRNG.scala 111:54 PRNG.scala 111:54 PRNG.scala 78:23]
  wire [3:0] _GEN_9 = 2'h3 == cnt2 ? 4'h0 : lfsr_3; // @[PRNG.scala 111:54 PRNG.scala 111:54 PRNG.scala 78:23]
  wire [3:0] _GEN_10 = 2'h0 == cnt2 ? 4'h1 : lfsr_0; // @[PRNG.scala 112:59 PRNG.scala 112:59 PRNG.scala 78:23]
  wire [3:0] _GEN_11 = 2'h1 == cnt2 ? 4'h1 : lfsr_1; // @[PRNG.scala 112:59 PRNG.scala 112:59 PRNG.scala 78:23]
  wire [3:0] _GEN_12 = 2'h2 == cnt2 ? 4'h1 : lfsr_2; // @[PRNG.scala 112:59 PRNG.scala 112:59 PRNG.scala 78:23]
  wire [3:0] _GEN_13 = 2'h3 == cnt2 ? 4'h1 : lfsr_3; // @[PRNG.scala 112:59 PRNG.scala 112:59 PRNG.scala 78:23]
  wire [3:0] _GEN_14 = 2'h0 == cnt2 ? 4'h2 : lfsr_0; // @[PRNG.scala 113:59 PRNG.scala 113:59 PRNG.scala 78:23]
  wire [3:0] _GEN_15 = 2'h1 == cnt2 ? 4'h2 : lfsr_1; // @[PRNG.scala 113:59 PRNG.scala 113:59 PRNG.scala 78:23]
  wire [3:0] _GEN_16 = 2'h2 == cnt2 ? 4'h2 : lfsr_2; // @[PRNG.scala 113:59 PRNG.scala 113:59 PRNG.scala 78:23]
  wire [3:0] _GEN_17 = 2'h3 == cnt2 ? 4'h2 : lfsr_3; // @[PRNG.scala 113:59 PRNG.scala 113:59 PRNG.scala 78:23]
  wire [3:0] _GEN_18 = 2'h0 == cnt2 ? 4'h3 : lfsr_0; // @[PRNG.scala 114:59 PRNG.scala 114:59 PRNG.scala 78:23]
  wire [3:0] _GEN_19 = 2'h1 == cnt2 ? 4'h3 : lfsr_1; // @[PRNG.scala 114:59 PRNG.scala 114:59 PRNG.scala 78:23]
  wire [3:0] _GEN_20 = 2'h2 == cnt2 ? 4'h3 : lfsr_2; // @[PRNG.scala 114:59 PRNG.scala 114:59 PRNG.scala 78:23]
  wire [3:0] _GEN_21 = 2'h3 == cnt2 ? 4'h3 : lfsr_3; // @[PRNG.scala 114:59 PRNG.scala 114:59 PRNG.scala 78:23]
  wire [3:0] _GEN_22 = 2'h0 == cnt2 ? 4'h4 : lfsr_0; // @[PRNG.scala 115:59 PRNG.scala 115:59 PRNG.scala 78:23]
  wire [3:0] _GEN_23 = 2'h1 == cnt2 ? 4'h4 : lfsr_1; // @[PRNG.scala 115:59 PRNG.scala 115:59 PRNG.scala 78:23]
  wire [3:0] _GEN_24 = 2'h2 == cnt2 ? 4'h4 : lfsr_2; // @[PRNG.scala 115:59 PRNG.scala 115:59 PRNG.scala 78:23]
  wire [3:0] _GEN_25 = 2'h3 == cnt2 ? 4'h4 : lfsr_3; // @[PRNG.scala 115:59 PRNG.scala 115:59 PRNG.scala 78:23]
  wire [3:0] _GEN_26 = 2'h0 == cnt2 ? 4'h5 : lfsr_0; // @[PRNG.scala 116:59 PRNG.scala 116:59 PRNG.scala 78:23]
  wire [3:0] _GEN_27 = 2'h1 == cnt2 ? 4'h5 : lfsr_1; // @[PRNG.scala 116:59 PRNG.scala 116:59 PRNG.scala 78:23]
  wire [3:0] _GEN_28 = 2'h2 == cnt2 ? 4'h5 : lfsr_2; // @[PRNG.scala 116:59 PRNG.scala 116:59 PRNG.scala 78:23]
  wire [3:0] _GEN_29 = 2'h3 == cnt2 ? 4'h5 : lfsr_3; // @[PRNG.scala 116:59 PRNG.scala 116:59 PRNG.scala 78:23]
  wire [4:0] _GEN_113 = {{1'd0}, myReg}; // @[PRNG.scala 117:40]
  wire [3:0] _GEN_30 = 2'h0 == cnt2 ? 4'h6 : lfsr_0; // @[PRNG.scala 117:59 PRNG.scala 117:59 PRNG.scala 78:23]
  wire [3:0] _GEN_31 = 2'h1 == cnt2 ? 4'h6 : lfsr_1; // @[PRNG.scala 117:59 PRNG.scala 117:59 PRNG.scala 78:23]
  wire [3:0] _GEN_32 = 2'h2 == cnt2 ? 4'h6 : lfsr_2; // @[PRNG.scala 117:59 PRNG.scala 117:59 PRNG.scala 78:23]
  wire [3:0] _GEN_33 = 2'h3 == cnt2 ? 4'h6 : lfsr_3; // @[PRNG.scala 117:59 PRNG.scala 117:59 PRNG.scala 78:23]
  wire [3:0] _GEN_34 = 2'h0 == cnt2 ? myReg : lfsr_0; // @[PRNG.scala 118:47 PRNG.scala 118:47 PRNG.scala 78:23]
  wire [3:0] _GEN_35 = 2'h1 == cnt2 ? myReg : lfsr_1; // @[PRNG.scala 118:47 PRNG.scala 118:47 PRNG.scala 78:23]
  wire [3:0] _GEN_36 = 2'h2 == cnt2 ? myReg : lfsr_2; // @[PRNG.scala 118:47 PRNG.scala 118:47 PRNG.scala 78:23]
  wire [3:0] _GEN_37 = 2'h3 == cnt2 ? myReg : lfsr_3; // @[PRNG.scala 118:47 PRNG.scala 118:47 PRNG.scala 78:23]
  wire [3:0] _GEN_38 = _GEN_113 == 5'h10 ? _GEN_30 : _GEN_34; // @[PRNG.scala 117:48]
  wire [3:0] _GEN_39 = _GEN_113 == 5'h10 ? _GEN_31 : _GEN_35; // @[PRNG.scala 117:48]
  wire [3:0] _GEN_40 = _GEN_113 == 5'h10 ? _GEN_32 : _GEN_36; // @[PRNG.scala 117:48]
  wire [3:0] _GEN_41 = _GEN_113 == 5'h10 ? _GEN_33 : _GEN_37; // @[PRNG.scala 117:48]
  wire [3:0] _GEN_42 = myReg == 4'hf ? _GEN_26 : _GEN_38; // @[PRNG.scala 116:48]
  wire [3:0] _GEN_43 = myReg == 4'hf ? _GEN_27 : _GEN_39; // @[PRNG.scala 116:48]
  wire [3:0] _GEN_44 = myReg == 4'hf ? _GEN_28 : _GEN_40; // @[PRNG.scala 116:48]
  wire [3:0] _GEN_45 = myReg == 4'hf ? _GEN_29 : _GEN_41; // @[PRNG.scala 116:48]
  wire [3:0] _GEN_46 = myReg == 4'he ? _GEN_22 : _GEN_42; // @[PRNG.scala 115:48]
  wire [3:0] _GEN_47 = myReg == 4'he ? _GEN_23 : _GEN_43; // @[PRNG.scala 115:48]
  wire [3:0] _GEN_48 = myReg == 4'he ? _GEN_24 : _GEN_44; // @[PRNG.scala 115:48]
  wire [3:0] _GEN_49 = myReg == 4'he ? _GEN_25 : _GEN_45; // @[PRNG.scala 115:48]
  wire [3:0] _GEN_50 = myReg == 4'hd ? _GEN_18 : _GEN_46; // @[PRNG.scala 114:48]
  wire [3:0] _GEN_51 = myReg == 4'hd ? _GEN_19 : _GEN_47; // @[PRNG.scala 114:48]
  wire [3:0] _GEN_52 = myReg == 4'hd ? _GEN_20 : _GEN_48; // @[PRNG.scala 114:48]
  wire [3:0] _GEN_53 = myReg == 4'hd ? _GEN_21 : _GEN_49; // @[PRNG.scala 114:48]
  wire [3:0] _GEN_54 = myReg == 4'hc ? _GEN_14 : _GEN_50; // @[PRNG.scala 113:48]
  wire [3:0] _GEN_55 = myReg == 4'hc ? _GEN_15 : _GEN_51; // @[PRNG.scala 113:48]
  wire [3:0] _GEN_56 = myReg == 4'hc ? _GEN_16 : _GEN_52; // @[PRNG.scala 113:48]
  wire [3:0] _GEN_57 = myReg == 4'hc ? _GEN_17 : _GEN_53; // @[PRNG.scala 113:48]
  wire [3:0] _GEN_58 = myReg == 4'hb ? _GEN_10 : _GEN_54; // @[PRNG.scala 112:48]
  wire [3:0] _GEN_59 = myReg == 4'hb ? _GEN_11 : _GEN_55; // @[PRNG.scala 112:48]
  wire [3:0] _GEN_60 = myReg == 4'hb ? _GEN_12 : _GEN_56; // @[PRNG.scala 112:48]
  wire [3:0] _GEN_61 = myReg == 4'hb ? _GEN_13 : _GEN_57; // @[PRNG.scala 112:48]
  wire [3:0] _GEN_62 = myReg == 4'ha ? _GEN_6 : _GEN_58; // @[PRNG.scala 111:43]
  wire [3:0] _GEN_63 = myReg == 4'ha ? _GEN_7 : _GEN_59; // @[PRNG.scala 111:43]
  wire [3:0] _GEN_64 = myReg == 4'ha ? _GEN_8 : _GEN_60; // @[PRNG.scala 111:43]
  wire [3:0] _GEN_65 = myReg == 4'ha ? _GEN_9 : _GEN_61; // @[PRNG.scala 111:43]
  wire [1:0] _T_14 = cnt2 + 2'h1; // @[PRNG.scala 119:38]
  wire [3:0] _GEN_69 = _T_5 ? lfsr_0 : _GEN_62; // @[PRNG.scala 104:17 PRNG.scala 78:23]
  wire [3:0] _GEN_70 = _T_5 ? lfsr_1 : _GEN_63; // @[PRNG.scala 104:17 PRNG.scala 78:23]
  wire [3:0] _GEN_71 = _T_5 ? lfsr_2 : _GEN_64; // @[PRNG.scala 104:17 PRNG.scala 78:23]
  wire [3:0] _GEN_72 = _T_5 ? lfsr_3 : _GEN_65; // @[PRNG.scala 104:17 PRNG.scala 78:23]
  wire [7:0] _T_16 = cnt + 8'h1; // @[PRNG.scala 121:27]
  wire [3:0] _GEN_77 = _T ? _GEN_69 : lfsr_0; // @[PRNG.scala 102:29 PRNG.scala 78:23]
  wire [3:0] _GEN_78 = _T ? _GEN_70 : lfsr_1; // @[PRNG.scala 102:29 PRNG.scala 78:23]
  wire [3:0] _GEN_79 = _T ? _GEN_71 : lfsr_2; // @[PRNG.scala 102:29 PRNG.scala 78:23]
  wire [3:0] _GEN_80 = _T ? _GEN_72 : lfsr_3; // @[PRNG.scala 102:29 PRNG.scala 78:23]
  wire [3:0] _T_20 = lfsr_0 + 4'h1; // @[PRNG.scala 129:67]
  wire [3:0] _GEN_83 = lfsr_0 == lfsr_1 ? _T_20 : _GEN_77; // @[PRNG.scala 128:64 PRNG.scala 129:57]
  wire [3:0] _GEN_84 = lfsr_0 == lfsr_2 ? _T_20 : _GEN_83; // @[PRNG.scala 128:64 PRNG.scala 129:57]
  wire [3:0] _T_29 = lfsr_1 + 4'h1; // @[PRNG.scala 129:67]
  wire [3:0] _GEN_86 = lfsr_1 == lfsr_0 ? _T_29 : _GEN_78; // @[PRNG.scala 128:64 PRNG.scala 129:57]
  wire [3:0] _GEN_87 = lfsr_1 == lfsr_2 ? _T_29 : _GEN_86; // @[PRNG.scala 128:64 PRNG.scala 129:57]
  wire [3:0] _T_38 = lfsr_2 + 4'h1; // @[PRNG.scala 129:67]
  wire [3:0] _GEN_89 = lfsr_2 == lfsr_0 ? _T_38 : _GEN_79; // @[PRNG.scala 128:64 PRNG.scala 129:57]
  wire [3:0] _GEN_90 = lfsr_2 == lfsr_1 ? _T_38 : _GEN_89; // @[PRNG.scala 128:64 PRNG.scala 129:57]
  wire [3:0] _T_47 = lfsr_3 + 4'h1; // @[PRNG.scala 129:67]
  wire [3:0] _GEN_92 = lfsr_3 == lfsr_0 ? _T_47 : _GEN_80; // @[PRNG.scala 128:64 PRNG.scala 129:57]
  wire [3:0] _GEN_93 = lfsr_3 == lfsr_1 ? _T_47 : _GEN_92; // @[PRNG.scala 128:64 PRNG.scala 129:57]
  wire [3:0] _GEN_95 = io_gen ? lfsr_0 : 4'h0; // @[PRNG.scala 136:29 PRNG.scala 137:35 PRNG.scala 82:19]
  wire [3:0] _GEN_96 = io_gen ? lfsr_1 : 4'h0; // @[PRNG.scala 136:29 PRNG.scala 137:35 PRNG.scala 82:19]
  wire [3:0] _GEN_97 = io_gen ? lfsr_2 : 4'h0; // @[PRNG.scala 136:29 PRNG.scala 137:35 PRNG.scala 82:19]
  wire [3:0] _GEN_98 = io_gen ? lfsr_3 : 4'h0; // @[PRNG.scala 136:29 PRNG.scala 137:35 PRNG.scala 82:19]
  wire  _GEN_99 = io_gen ? 1'h0 : 1'h1; // @[PRNG.scala 136:29 PRNG.scala 138:34 PRNG.scala 134:26]
  LFSR_Galois lfsrInst ( // @[PRNG.scala 77:26]
    .clock(lfsrInst_clock),
    .reset(lfsrInst_reset),
    .io_seed_valid(lfsrInst_io_seed_valid),
    .io_rndNum(lfsrInst_io_rndNum)
  );
  assign io_puzzle_0 = state ? _GEN_95 : 4'h0; // @[PRNG.scala 124:31 PRNG.scala 82:19]
  assign io_puzzle_1 = state ? _GEN_96 : 4'h0; // @[PRNG.scala 124:31 PRNG.scala 82:19]
  assign io_puzzle_2 = state ? _GEN_97 : 4'h0; // @[PRNG.scala 124:31 PRNG.scala 82:19]
  assign io_puzzle_3 = state ? _GEN_98 : 4'h0; // @[PRNG.scala 124:31 PRNG.scala 82:19]
  assign io_ready = state & _GEN_99; // @[PRNG.scala 124:31 PRNG.scala 83:18]
  assign lfsrInst_clock = clock;
  assign lfsrInst_reset = reset;
  assign lfsrInst_io_seed_valid = _T & _T_5; // @[PRNG.scala 102:29 PRNG.scala 84:32]
  always @(posedge clock) begin
    if (reset) begin // @[PRNG.scala 74:28]
      myReg <= 4'h0; // @[PRNG.scala 74:28]
    end else if (_T) begin // @[PRNG.scala 102:29]
      if (!(_T_5)) begin // @[PRNG.scala 104:17]
        myReg <= lfsrInst_io_rndNum; // @[PRNG.scala 110:31]
      end
    end
    if (reset) begin // @[PRNG.scala 75:26]
      cnt <= 8'h0; // @[PRNG.scala 75:26]
    end else if (_T) begin // @[PRNG.scala 102:29]
      cnt <= _T_16; // @[PRNG.scala 121:21]
    end
    if (reset) begin // @[PRNG.scala 76:27]
      cnt2 <= 2'h0; // @[PRNG.scala 76:27]
    end else if (_T) begin // @[PRNG.scala 102:29]
      if (_T_5) begin // @[PRNG.scala 104:17]
        cnt2 <= _GEN_4;
      end else begin
        cnt2 <= _T_14; // @[PRNG.scala 119:30]
      end
    end else begin
      cnt2 <= _GEN_4;
    end
    if (reset) begin // @[PRNG.scala 78:23]
      lfsr_0 <= 4'h0; // @[PRNG.scala 78:23]
    end else if (state) begin // @[PRNG.scala 124:31]
      if (io_gen) begin // @[PRNG.scala 136:29]
        lfsr_0 <= 4'h0; // @[PRNG.scala 139:30]
      end else if (lfsr_0 == lfsr_3) begin // @[PRNG.scala 128:64]
        lfsr_0 <= _T_20; // @[PRNG.scala 129:57]
      end else begin
        lfsr_0 <= _GEN_84;
      end
    end else if (_T) begin // @[PRNG.scala 102:29]
      if (!(_T_5)) begin // @[PRNG.scala 104:17]
        lfsr_0 <= _GEN_62;
      end
    end
    if (reset) begin // @[PRNG.scala 78:23]
      lfsr_1 <= 4'h0; // @[PRNG.scala 78:23]
    end else if (state) begin // @[PRNG.scala 124:31]
      if (io_gen) begin // @[PRNG.scala 136:29]
        lfsr_1 <= 4'h0; // @[PRNG.scala 139:30]
      end else if (lfsr_1 == lfsr_3) begin // @[PRNG.scala 128:64]
        lfsr_1 <= _T_29; // @[PRNG.scala 129:57]
      end else begin
        lfsr_1 <= _GEN_87;
      end
    end else if (_T) begin // @[PRNG.scala 102:29]
      if (!(_T_5)) begin // @[PRNG.scala 104:17]
        lfsr_1 <= _GEN_63;
      end
    end
    if (reset) begin // @[PRNG.scala 78:23]
      lfsr_2 <= 4'h0; // @[PRNG.scala 78:23]
    end else if (state) begin // @[PRNG.scala 124:31]
      if (io_gen) begin // @[PRNG.scala 136:29]
        lfsr_2 <= 4'h0; // @[PRNG.scala 139:30]
      end else if (lfsr_2 == lfsr_3) begin // @[PRNG.scala 128:64]
        lfsr_2 <= _T_38; // @[PRNG.scala 129:57]
      end else begin
        lfsr_2 <= _GEN_90;
      end
    end else if (_T) begin // @[PRNG.scala 102:29]
      if (!(_T_5)) begin // @[PRNG.scala 104:17]
        lfsr_2 <= _GEN_64;
      end
    end
    if (reset) begin // @[PRNG.scala 78:23]
      lfsr_3 <= 4'h0; // @[PRNG.scala 78:23]
    end else if (state) begin // @[PRNG.scala 124:31]
      if (io_gen) begin // @[PRNG.scala 136:29]
        lfsr_3 <= 4'h0; // @[PRNG.scala 139:30]
      end else if (lfsr_3 == lfsr_2) begin // @[PRNG.scala 128:64]
        lfsr_3 <= _T_47; // @[PRNG.scala 129:57]
      end else begin
        lfsr_3 <= _GEN_93;
      end
    end else if (_T) begin // @[PRNG.scala 102:29]
      if (!(_T_5)) begin // @[PRNG.scala 104:17]
        lfsr_3 <= _GEN_65;
      end
    end
    if (reset) begin // @[PRNG.scala 81:28]
      state <= 1'h0; // @[PRNG.scala 81:28]
    end else if (_T) begin // @[Conditional.scala 40:58]
      state <= _GEN_1;
    end else if (state) begin // @[Conditional.scala 39:67]
      if (io_gen) begin // @[PRNG.scala 96:40]
        state <= 1'h0; // @[PRNG.scala 97:31]
      end
    end
  end
// Register and memory initialization
`ifdef RANDOMIZE_GARBAGE_ASSIGN
`define RANDOMIZE
`endif
`ifdef RANDOMIZE_INVALID_ASSIGN
`define RANDOMIZE
`endif
`ifdef RANDOMIZE_REG_INIT
`define RANDOMIZE
`endif
`ifdef RANDOMIZE_MEM_INIT
`define RANDOMIZE
`endif
`ifndef RANDOM
`define RANDOM $random
`endif
`ifdef RANDOMIZE_MEM_INIT
  integer initvar;
`endif
`ifndef SYNTHESIS
`ifdef FIRRTL_BEFORE_INITIAL
`FIRRTL_BEFORE_INITIAL
`endif
initial begin
  `ifdef RANDOMIZE
    `ifdef INIT_RANDOM
      `INIT_RANDOM
    `endif
    `ifndef VERILATOR
      `ifdef RANDOMIZE_DELAY
        #`RANDOMIZE_DELAY begin end
      `else
        #0.002 begin end
      `endif
    `endif
`ifdef RANDOMIZE_REG_INIT
  _RAND_0 = {1{`RANDOM}};
  myReg = _RAND_0[3:0];
  _RAND_1 = {1{`RANDOM}};
  cnt = _RAND_1[7:0];
  _RAND_2 = {1{`RANDOM}};
  cnt2 = _RAND_2[1:0];
  _RAND_3 = {1{`RANDOM}};
  lfsr_0 = _RAND_3[3:0];
  _RAND_4 = {1{`RANDOM}};
  lfsr_1 = _RAND_4[3:0];
  _RAND_5 = {1{`RANDOM}};
  lfsr_2 = _RAND_5[3:0];
  _RAND_6 = {1{`RANDOM}};
  lfsr_3 = _RAND_6[3:0];
  _RAND_7 = {1{`RANDOM}};
  state = _RAND_7[0:0];
`endif // RANDOMIZE_REG_INIT
  `endif // RANDOMIZE
end // initial
`ifdef FIRRTL_AFTER_INITIAL
`FIRRTL_AFTER_INITIAL
`endif
`endif // SYNTHESIS
endmodule
