import java.util.Scanner;

//written by Tony Shu (ID: 1356141) and Jiwei Wang (ID: 1360206)


//create initial runs using a heap to perform a replacement selection
//print the output as standard output
public class CreateRuns
{
    public static void main(String args[])
    {
        try
        {
            if(args.length!=1)
            {
                System.err.println("Invalid input");
                return;
            }

            //declare variables
            MyMinHeap heap;
            int size=Integer.parseInt(args[0]);
            boolean endOfFile=false;
            String lastOutput;
            String takeOver=null;
            int lines=0;

            if(size<2)
            {
                heap=new MyMinHeap(32);
            }
            else
            {
                heap=new MyMinHeap(size);
            }

            Scanner myReader = new Scanner(System.in);

            //as long as it is not the end of input
            while(!endOfFile)
            {
                lines=0;
                String data=null;
                //load everything into an array
                endOfFile=heap.load(myReader);
                //make it in heap order
                heap.reheap();

                //get what the last output is
                if(takeOver!=null)
                {
                    lastOutput=takeOver;
                }
                else
                {
                    lastOutput=heap.peek();
                }

                //as long as the heap is not empty
                while (!heap.isEmpty())
                {
                    String s=heap.peek();
                    //if the root can go to the current run
                    if(s.compareTo(lastOutput)>=0)
                    {
                        //print it and update the last output
                        System.out.println(s);
                        lastOutput=s;
                        //if there is more upcoming input
                        if(myReader.hasNextLine())
                        {
                            //replace the root of our heap with the next input
                            data=myReader.nextLine();
                            heap.replace(data,null);
                        }
                        else
                        {
                            //otherwise we only need to worry about what is being kept in our heap
                            //as there is no more data coming in
                            endOfFile=true;
                            heap.delete();
                            lines=0;
                            //print out data kept in the heap
                            while(!heap.isEmpty())
                            {
                                if(heap.peek().compareTo(lastOutput)<0)
                                {
                                    System.out.println("---end_of_run---");
                                }
                                lastOutput=heap.delete();
                                System.out.println(lastOutput);
                            }

                            if(!heap.isEmptyAtEnd())
                            {
                                while(!heap.isEmpty())
                                {
                                    if(heap.peek().compareTo(lastOutput)<0)
                                    {
                                        System.out.println("---end_of_run---");
                                    }
                                    lastOutput=heap.remove();
                                    System.out.println(lastOutput);
                                }
                            }

                            break;
                        }
                    }
                    else
                    {
                        heap.remove();
                        lines++;
                    }

                }

                System.out.println("---end_of_run---");

                //change the heap size by the number of times that remove occurs
                heap.changeSize(lines);
                heap.reheap();
                while (!heap.isEmpty())
                {
                    lastOutput=heap.remove();
                    System.out.println(lastOutput);
                }

                takeOver=lastOutput;

            }
            myReader.close();
            if(lines>0)
            {
                {System.out.println("---end_of_run---");}
            }

        }
        catch (Exception e)
        {
            System.err.println(e);
        }

    }
}