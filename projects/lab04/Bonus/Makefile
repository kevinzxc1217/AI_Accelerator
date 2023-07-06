all:
	@mkdir -p obj
	@g++ -o obj/emulator emulator.cpp -g -std=c++11
	@./obj/emulator benchmark.txt
	@g++ -o obj/emulator_accelerate emulator_accelerate.cpp -g -std=c++11
	@./obj/emulator_accelerate benchmark.txt