

if [ -n "$1" ]
then
    if [ "$1" = "Lab1" ]
    then
      cp ./Emulator/Resource/Lab8-1/inst.asm ./src/main/resource/inst.asm
      cp ./Emulator/Resource/Lab8-1/m_code.hex ./src/main/resource/m_code.hex
      cp ./Emulator/Resource/Lab8-1/data.hex ./src/main/resource/data.hex
    elif [ "$1" = "Lab2" ]
    then
      cp ./Emulator/Resource/Lab8-2/inst.asm ./src/main/resource/inst.asm
      cp ./Emulator/Resource/Lab8-2/m_code.hex ./src/main/resource/m_code.hex
      cp ./Emulator/Resource/Lab8-2/data.hex ./src/main/resource/data.hex
    elif [ "$1" = "Lab3" ]
    then
      cp ./Emulator/Resource/Lab8-3/inst.asm ./src/main/resource/inst.asm
      cp ./Emulator/Resource/Lab8-3/m_code.hex ./src/main/resource/m_code.hex
      cp ./Emulator/Resource/Lab8-3/data.hex ./src/main/resource/data.hex
    elif [ "$1" = "Hw" ]
    then
      cp ./Emulator/Resource/HW8/inst.asm ./src/main/resource/inst.asm
      cp ./Emulator/m_code.hex ./src/main/resource/m_code.hex
      cp ./Emulator/data.hex ./src/main/resource/data.hex
    else
      echo "[Error] parameter 1 is wrong"
      echo "[Error] parameter 1 is Lab1 / Lab2 / Lab3 /HW"
    fi
else
    echo "[Error] usage should be: ./test_data.sh <which Lab>"
fi
