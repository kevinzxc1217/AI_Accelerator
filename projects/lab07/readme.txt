testing:
  path: in lab7-group directory
	  //透過emulator.cpp產生obj資料夾
    code: $ make
	  //將example的hw2_inst.asm轉成inst.asm和m_code
          $ ./obj/emulator ./example_code/Hw2_inst.asm
	  //將所產生的指令和mhcode放到cpu跑
	  $ mill chiselModule.test.runMain aias_lab7.topTest
	  //將生成出的inst和cpu resource/hw2_inst.asm比較
  	  $ diff -s ./example_code/inst.asm ./CPU_resource/Hw2_inst.asm
          $ diff -s ./example_code/m_code.hex ./CPU_resource/Hw2_m_code.hex