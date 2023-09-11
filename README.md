# Operating Systems Process Management Project 1
This is the first Process Management Project from my Operating Systems class where we build upon our PCB assignment by adding CPU events through the addition of the CPU_event class. Like the previous PCB assignment, this one has us generate a list of processes to iterate through as they run. This time, however, we add a waiting list, running the PCB with the largest wait time, and a series of CPU event interrupts that act as context switches. The events for this project:

Context Switch 1, "Completed" : Process is completed and does not need to be added to the waiting or ready list.

Context Switch 2, "Interrupt" : Return process to the end of the ready list.

Context Switch 3, "Blockpage" : Return process to the middle of the ready list.

Context Switch 4, "BlockIO" : Set state as waiting then add the process to the waiting list.

Context Switch 5, default : Return process to the start of the ready list.

I have also included a sample output text file named SampleOutput.txt. The Java files are located under ProcessManagementProject/src/processManagement folder.

I completed this assignment using Eclipse and have exported the project as an Eclipse archive zip file. To run the project yourself, simply import the archive named ProcessManagementProject.zip into your workspace using Eclipse.