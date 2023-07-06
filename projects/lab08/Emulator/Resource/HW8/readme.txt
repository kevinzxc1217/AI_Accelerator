hw8-1
執行.cpp
$  cd /Resource/HW8
$  g++ ./convolution_2D.cpp -o conv
$  ./conv
執行.S
$ cd /Emulator
$ ./obj/emulator ./Resource/HW8/test_hw8_1.S

hw8-2
$ cd /Emulator
$ ./obj/emulator ./Resource/HW8/test_hw8_2.S
$ ./obj/emulator ./Resource/Lab8-2/test_mat_mul_vec.S

hw8-3
$ make
$ cd /Emulator
$ ./obj/emulator ./Resource/HW8/test_hw8_2_byHCC.S
$ ./obj/emulator ./Resource/Lab8-3/test_vadd_vv.S
$ ./obj/emulator ./Resource/Lab8-2/test_mat_mul_scalar.S
$ ./obj/emulator ./Resource/Lab8-2/test_basic_operation.S


$  cd /workspace/projects/lab08
$ mill chiselModule.test.runMain aias_lab8.topTest