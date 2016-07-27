import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Arrays;



/**
 * First comes first served scheduler.
 */
public class FCFSScheduler implements Scheduler {

  @Override
  public void schedule(String inputFile, String outputFile) {
    // implement your scheduler here.
    // write the scheduler output to {@code outputFile}
	  try
	  {
		  FileReader input = new FileReader(inputFile);
		  BufferedReader buffer = new BufferedReader(input);
		  String line = null;
		  long[][] processes = new long[1024][3];
		  
		  int numOfElements = 1;
		  int iterator;
		  //Populating the Processes Array by reading the file.
		  while((line = buffer.readLine()) != null)
		  {
			  iterator = 0;
			  for(String section : line.split(" "))
			  {
				  Long value = Long.valueOf(section);
				  processes[numOfElements][iterator++] = value;
			  }
			  numOfElements++;
			  
		  }
		  buffer.close();
		  
		  //Initializing all variables
		  int arrivalTime[] = new int[numOfElements];
		  long waitingTime[] = new long[numOfElements];
		  int burstTime[] = new int[numOfElements];
		  long turnAroundTime[] = new long[numOfElements];
		  long finishTime[] = new long[numOfElements];
		  long processNum[] = new long[numOfElements];

		  double avgWaitingTime = 0;
		  double avgTurnAroundTime = 0;
		  
		  //populating the simplified arrays from the multidimensional array;
		  for(iterator = 1; iterator < numOfElements; iterator++)
		  {
			  processNum[iterator] = processes[iterator][0];
			  arrivalTime[iterator] = (int) processes[iterator][1];
			  burstTime[iterator] =  (int) processes[iterator][2];	
		  }
		  
		  int sortedIndeces[] = new int[numOfElements];
		  int copy[] = Arrays.copyOf(arrivalTime, numOfElements);
		  Arrays.sort(copy);
		  boolean check[] = new boolean[numOfElements];
		  for(iterator = 1; iterator < numOfElements; iterator++)
		  {
			  for(int iterator2 = 1; iterator2 < numOfElements; iterator2++)
			  {
				  if(copy[iterator] == arrivalTime[iterator2])
				  {
						  if(!check[iterator2])
						  {
							  check[iterator2] = true;
							  sortedIndeces[iterator] = iterator2;
							  iterator2 = 0;
							  if(iterator < numOfElements - 1)
							  {
								  iterator++;
							  }
						  }
				  }
			  }
		  }
		  
		  for(iterator = 1; iterator < numOfElements; iterator++)
		  {
			  if(iterator == 1)
			  {
				  finishTime[sortedIndeces[iterator]] = burstTime[sortedIndeces[iterator]] + arrivalTime[sortedIndeces[iterator]]; 
				  waitingTime[sortedIndeces[iterator]] = 0;  
				  turnAroundTime[sortedIndeces[iterator]] = burstTime[sortedIndeces[iterator]]; 
			  }
			  else
			  {
				  finishTime[sortedIndeces[iterator]] = finishTime[sortedIndeces[iterator - 1]] + burstTime[sortedIndeces[iterator]];
				  waitingTime[sortedIndeces[iterator]] = finishTime[sortedIndeces[iterator - 1]] - arrivalTime[sortedIndeces[iterator]];
				  turnAroundTime[sortedIndeces[iterator]] = finishTime[sortedIndeces[iterator]] - arrivalTime[sortedIndeces[iterator]]; 
			  }
		  }
		  
		  long copy2[] = Arrays.copyOf(processNum, numOfElements);
		  Arrays.sort(copy2);
		  check = new boolean[numOfElements];
		  for(iterator = 1; iterator < numOfElements; iterator++)
		  {
			  for(int iterator2 = 1; iterator2 < numOfElements; iterator2++)
			  {
				  if(copy2[iterator] == processNum[iterator2])
				  {
						  if(!check[iterator2])
						  {
							  check[iterator2] = true;
							  sortedIndeces[iterator] = iterator2;
							  iterator2 = 0;
							  if(iterator < numOfElements - 1)
							  {
								  iterator++;
							  }
						  }
				  }
			  }
		  }
		  
		  FileWriter writer = new FileWriter(outputFile, false);
		  PrintWriter printer = new PrintWriter(writer);
		  for(iterator = 1; iterator < numOfElements; iterator++)
		  {
			  printer.format("%d %d %d %d%n", processNum[sortedIndeces[iterator]], finishTime[sortedIndeces[iterator]], waitingTime[sortedIndeces[iterator]], turnAroundTime[sortedIndeces[iterator]]);
			  avgWaitingTime += waitingTime[iterator];
			  avgTurnAroundTime += turnAroundTime[iterator]; 
		  }
		  
		  avgWaitingTime = avgWaitingTime / (numOfElements - 1);
		  avgTurnAroundTime = (avgTurnAroundTime / (numOfElements - 1));

		  printer.format("%d %d", (int) Math.round(avgWaitingTime), (int) Math.round(avgTurnAroundTime));

		  printer.close();
		  
	  }
	  catch(Exception e)
	  {
		  e.printStackTrace();
	  }
  }
}
