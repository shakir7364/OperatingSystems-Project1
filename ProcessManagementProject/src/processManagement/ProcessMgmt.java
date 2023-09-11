package processManagement;

import java.util.LinkedList;
import java.util.Random;

public class ProcessMgmt 
{

	public static void main(String args[]) 	
	{
		//#010	Initialization of fields and data structures	///////////////
		int QREADY__T	= 25 ;		
		final int BLOCKIO	= 3 ;		final int BLOCKPAGE	= 4 ;		
		final int INTERRUPT	= 2 ;		final int COMPLETED	= 1 ; 
				
		Random random__X	= new Random();
		CPU_event event		= new CPU_event();
		
		int CPU_runtime ;		int event__X = 0;
		int waitCounter = 0;
		int numIOEvents = 0;
  
		//#005 Create the List for QReady
		LinkedList<PCB> QReady = new LinkedList<PCB>();
		
		//#006 Create the List for QWaiting
		LinkedList<PCB> QWaiting = new LinkedList<PCB>();
		
		PCB PCB_Running	= null ; 		//#020 Create the field for: PCB_Running 
	
		//#030	
		
		for (int ii = 0; ii < QREADY__T; ii++)
		{
			//#040 Add a new PCB object onto the LL
			QReady.add(new PCB());
		}

		///////////////////////////////////////////////////////////////////////
		//#080	===> end of Initialization 
		
		System.out.println("\n*****\t\t\tReady Queue\t\t\t*****");  
		for (PCB pcbLoop : QReady)		//#090 Loop that executes based on the no. of nodes in the LL
			//#095 Print the PCB for an LL node
			System.out.println(pcbLoop.showPCB());
  
		//#0100	Process until the active processes are all completed	///////
		
		while ( !QReady.isEmpty() || !QWaiting.isEmpty())	//#120	change to iterate until both QReady and QWait are empty
		{
			
			//#0105	Next process to Run
			//#0140 Assign the first node from QReady to Running
			
			//Variables to find the PCB that has the longest wait time since it has the highest priority
			int largestWaitTime = 0;
			int indexOfLargestWaitTime = 0;
			
			//Check through QReady list for PCB with longest wait time
			for(int i = 0; i < QReady.size(); i++)
				if(QReady.get(i).get_timeWaiting() >= largestWaitTime)
				{
					largestWaitTime = QReady.get(i).get_timeWaiting();
					indexOfLargestWaitTime = i;
				}
			//Assign PCB with longest wait time to PCB_Running
			PCB_Running = QReady.remove(indexOfLargestWaitTime);
			
			
			System.out.println("\n*****Running Process " + PCB_Running.get_ID() + "*****");
			
			//#0145 Set the state value to "Running"
			PCB_Running.set_state("Running");
			
			CPU_runtime	= random__X.nextInt(20) + 1 ;	//#0150 Get a random no. between 1 and 20
			
			//#0160 Tally and set the CPU used for the process
			PCB_Running.set_CPU_used(PCB_Running.get_CPU_used() + (random__X.nextInt(100) + 1));
			
			//Show PCB
			System.out.println(PCB_Running.showPCB());
			
			//#0180 Increment the wait times for all other processes
			for(PCB pcbLoop : QReady)
			{
				pcbLoop.set_timeWaiting(pcbLoop.get_timeWaiting() + CPU_runtime);
			}
			for(PCB pcbLoop : QWaiting)
			{
				pcbLoop.set_timeWaiting(pcbLoop.get_timeWaiting() + CPU_runtime);
			}
			
			if(PCB_Running.get_CPU_used() >= PCB_Running.get_CPU_max())
			{	//#0190 IF statement for termination based on CPU Max
				System.out.println("\n*****\t\t\tProcess Completed\t\t\t*****");   	  
				System.out.println(PCB_Running.showPCB());
				//continue;	// iterate to the next in the WHILE loop
			}
			else
			{
				//#0200 Simulate the type of Block on the Process (I/O Block, Memory Paging Block, Interrupt)
				event__X	= event.get_CPU_event() ;
				
				if (event__X == COMPLETED)
				{
					System.out.println("\n*****\t\t\tProcess Completed\t\t\t*****CPU event*****");   	  
					System.out.println(PCB_Running.showPCB());		
				}
				else
				{
					//#230 Set the state to "Ready" from "Running"
					PCB_Running.set_state("Running");
					
					switch (event__X)
					{
						case INTERRUPT :
						{
							System.out.println("\n*****\t\t\tContext Switch 2\t\t\t*****");
							//#240 Add to QReady
							PCB_Running.set_state("Ready");
							QReady.add(PCB_Running);
							break;
						}				
						case BLOCKPAGE :
						{	
							System.out.println("\n*****\t\t\tContext Switch 3\t\t\t*****");
							//#242 Add to QReady
							PCB_Running.set_state("Ready");
							QReady.add(QReady.size() / 2, PCB_Running);
							break;
						}
						case BLOCKIO :
						{
							System.out.println("\n*****\t\t\tContext Switch 4\t\t\t*****");
							//#244 Add to QWait	
							PCB_Running.set_state("Waiting");
							QWaiting.add(PCB_Running);
							break;
						}
						default :
						{
							System.out.println("\n*****\t\t\tContext Switch 5\t\t\t*****");
							//#246 Add to QReady
							PCB_Running.set_state("Ready");
							QReady.addFirst(PCB_Running);;
							break;
						}
					}
				}
				
				//Increment waitCounter
				waitCounter++;
				if(waitCounter >= (QReady.size() / 4) && !QWaiting.isEmpty())
				{
					QWaiting.get(QWaiting.size() - 1).set_state("Ready");
					QReady.add(QWaiting.removeLast());
					waitCounter = 0;
					numIOEvents++;
					System.out.println("\n>>>>>\t\t\tI/O event completed: " + numIOEvents + "\t\t\t<<<<<");
				}
				
				System.out.println("\n*****\t\t\t\tReady Queue\t\t\t*****");   
				//#300 print out PCB's in both QReady and QWait
				for(PCB pcbLoop : QReady)
					System.out.println(pcbLoop.showPCB());
				
				System.out.println("\n*****\t\t\tWaiting Queue\t\t\t*****");  
				for(PCB pcbLoop : QWaiting)
					System.out.println(pcbLoop.showPCB());
			}
		}	
	}
}
