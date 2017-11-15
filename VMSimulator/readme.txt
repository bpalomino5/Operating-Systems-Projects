README: VMSimulator

Description:
VMSimulator is a project for simulating Virtual Memory as it behaves in the CPU, OS, Virtual Page Table, and TLB Cache.
This project uses supported page_files and test_files which are tailored specifically for the design of the simulator.
That being said those files shall not be modified!

Instructions:
1) Navigate to the VMSimulator project root directory in the terminal
2) Compile: javac -cp .:lib/* -sourcepath src -d build src/Simulator.java
3) Run: java -cp .:build:lib/* Simulator test_files/test_1.txt
   Optionally swap the test files
4) Check the output_files directory for finalized csv files of VMSimulator results