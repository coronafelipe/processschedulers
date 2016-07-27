import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;


/**
 * Proportional share scheduler
 * Take total shares to be 100.
 * A process will not run unless it is completely given the requested share.
 */
public class PSScheduler implements Scheduler {

  @Override
  public void schedule(String inputFile, String outputFile) {
    // implement your scheduler here.
    // write the scheduler output to {@code outputFile}
	  try
	  {
		  FileReader input = new FileReader(inputFile);
		  BufferedReader buffer = new BufferedReader(input);
		  String line = null;
		  long[][] processes = new long[1024][4];
		  
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
		  long arrivalTime[] = new long[numOfElements];
		  long waitingTime[] = new long[numOfElements];
		  long burstTime[] = new long[numOfElements];
		  long turnAroundTime[] = new long[numOfElements];
		  long finishTime[] = new long[numOfElements];
		  long processNum[] = new long[numOfElements];
		  long share[] = new long[numOfElements];
		  Queue<Integer> queue = new LinkedList<Integer>();
		  Queue<Integer> finished = new LinkedList<Integer>();
		  Queue<Integer> finished2 = new LinkedList<Integer>();
		  double avgWaitingTime = 0;
		  double avgTurnAroundTime = 0;
		  int checkSum = 0;
		  int selectedIndex = 0;
		  boolean flag = true;
		  boolean first = true;
		  
		  //populating the simplified arrays from the multidimensional array;
		  for(iterator = 1; iterator < numOfElements; iterator++)
		  {
			  processNum[iterator] = processes[iterator][0];
			  arrivalTime[iterator] = (int) processes[iterator][1];
			  burstTime[iterator] = (int) processes[iterator][2];
			  share[iterator] = processes[iterator][3];
			  //System.out.format("%d %d %d %d%n", processNum[iterator], arrivalTime[iterator], burstTime[iterator], share[iterator]);
		  }
		  
		  int sortedIndeces[] = new int[numOfElements];
		  long copy[] = Arrays.copyOf(arrivalTime, numOfElements);
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
			  queue.add(sortedIndeces[iterator]);
		  }
		  while(flag)
		  {
			  while(!queue.isEmpty() && (share[queue.peek()] + checkSum) <= 100)
			  {
				  selectedIndex = queue.remove();
				  checkSum += share[selectedIndex];
				  if(first)
				  {
					  finishTime[selectedIndex] = arrivalTime[selectedIndex] + burstTime[selectedIndex];
					  waitingTime[selectedIndex] = 0;  
					  turnAroundTime[selectedIndex] = burstTime[selectedIndex];
				  }
				  else
				  {
					  finishTime[selectedIndex] = finishTime[finished2.peek()] + burstTime[selectedIndex];
					  waitingTime[selectedIndex] = finishTime[finished2.peek()] - arrivalTime[selectedIndex];
					  turnAroundTime[selectedIndex] = finishTime[selectedIndex] - arrivalTime[selectedIndex]; 
				  }
				  finished.add(selectedIndex);
			  }
			  if(!finished2.isEmpty())
				  finished2.remove();
			  first = false;
			  if(!finished.isEmpty())
			  {
				  selectedIndex = finished.remove();
				  finished2.add(selectedIndex);
				  checkSum -= share[selectedIndex];
			  }
			  else
			  {
				  flag = false;
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
