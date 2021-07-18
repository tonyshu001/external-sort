import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Scanner;

//written by Tony Shu (ID: 1356141) and Jiwei Wang (ID: 1360206)


//perform a balanced-k way merge sort and print the final result to standard output
public class MergeRuns
{

    public static void main(String[] args)
    {
        String END="---end_of_run---";
        if(args.length!=1)
        {
            System.err.println("Invalid input");
            return;
        }

        int size=Integer.parseInt(args[0]);
        if(size<=1)
        {
            System.err.println("Invalid input");
            return;
        }


        try
        {
            //declare variables
            DistributeRuns distri=new DistributeRuns(size);
            distri.distribute();

            MyMinHeap heap=new MyMinHeap(size+1);

            boolean endOfSort=false;
            int count=0;

            //as long as we do not finish sorting
            while(!endOfSort)
            {
                //perform an alternate operation until the end of sorting
                if(count%2==0)
                {
                    endOfSort=readFromLeft(size, heap);
                }
                else
                {
                    endOfSort=readFromRight(size,heap);
                }
                count++;
            }
            System.err.println("The number of pass: "+count);
        }
        catch (Exception e)
        {
            System.err.println(e);
        }


    }

    //return true if finish reading all files at each pass, false otherwise
    public static boolean allFilesEnd(fileReader[] readers)
    {
        boolean result=true;
        for(int i=1;i<readers.length;i++)
        {
            if(!readers[i].getEndOfFile())
            {
                readers[i].setEndOfRun(false);
                result=false;
            }

        }
        return result;
    }

    //read data from the left side files(e.g. 1 to n, where n is the program argument)
    //write it out to the right side files(e.g. n+1 to 2n)
    public static boolean readFromLeft(int size, MyMinHeap heap)
    {
        try
        {
            //declare variables
            int lastMove=-1;
            int endNum=0;
            fileReader[] readers=new fileReader[size+1];
            BufferedWriter[] writers=new BufferedWriter[size+1];
            String END="---end_of_run---";
            boolean endOfPass;
            String data;

            //initialise file readers and writers, each reader is responsible for only one specific file
            //and so are writers
            for(int i = 1; i<=size; i++)
            {
                readers[i]=new fileReader("T"+i);
                int num=size+i;
                writers[i]=new BufferedWriter(new FileWriter("T"+num));
                data =readers[i].getData();
                if(!readers[i].getEndOfFile())
                {
                    if(!readers[i].getEndOfRun())
                    {
                        heap.insert(data,String.valueOf(i));
                    }
                }
            }

            //check if we finish reading all files
            endOfPass = allFilesEnd(readers);
            int mergeNum=0;
            //if we still have work to do in this pass
            while(!endOfPass)
            {
                //calculate which file the writer should write data to
                int writeTo=1+(mergeNum%size);

                //write the data
                while(!heap.isEmpty())
                {
                    int addr=Integer.parseInt(heap.peekRootAddr());
                    String input = readers[addr].getData();
                    String output;
                    if(!input.equals(END))
                    {
                        output =heap.replace(input,String.valueOf(addr));
                        writers[writeTo].write(output +"\n");
                    }
                    else
                    {
                        output =heap.remove();
                        writers[writeTo].write(output +"\n");
                    }

                }

                //write the symbol for the end of run and record how many times we write it
                writers[writeTo].write(END+"\n");
                endNum++;
                //record the last move so when we finish sorting, we know where to get the sorted data
                lastMove=writeTo;

                //get the heap and readers ready for the next iteration
                for(int i=1;i<=size;i++)
                {
                    data =readers[i].getData();
                    if(!readers[i].getEndOfFile())
                    {
                        readers[i].setEndOfRun(false);
                        heap.insert(data,String.valueOf(i));
                    }
                }

                mergeNum++;
                endOfPass =allFilesEnd(readers);
            }

            //close all readers and writers
            for(int i=1;i<=size;i++)
            {
                readers[i].close();
                writers[i].close();
            }

            //if we only have one sorted run
            //we are done!
            //write the data to standard output
            if(endNum<2)
            {
                String s=null;
                lastMove+=size;
                Scanner scanner=new Scanner(new File("T"+lastMove));
                while (scanner.hasNextLine())
                {
                    data=scanner.nextLine();
                    if(!data.equals(END))
                    {
                        System.out.println(data);
                    }
                }
                scanner.close();
            }
            return endNum<2;
        }
        catch (Exception e)
        {
            System.err.println(e);
            return false;
        }
    }

    //read data from the right side files(e.g. n+1 to 2n, where n is the program argument)
    //write it out to the left side files(e.g. 1 to n)
    public static boolean readFromRight(int size, MyMinHeap heap)
    {
        try
        {
            //declare variables
            int lastMove=-1;
            int endNum=0;
            fileReader[] readers=new fileReader[size+1];
            BufferedWriter[] writers=new BufferedWriter[size+1];
            String END="---end_of_run---";
            boolean endOfPass;
            String data;

            //initialise file readers and writers, each reader is responsible for only one specific file
            //and so are writers
            for(int i = 1; i<=size; i++)
            {
                int num=size+i;
                readers[i]=new fileReader("T"+num);

                writers[i]=new BufferedWriter(new FileWriter("T"+i));
                data =readers[i].getData();
                if(!readers[i].getEndOfFile())
                {
                    if(!readers[i].getEndOfRun())
                    {
                        heap.insert(data,String.valueOf(num));
                    }
                }
            }

            //check if we finish reading all files
            endOfPass = allFilesEnd(readers);
            int mergeNum=0;

            //if we still have work to do in this pass
            while(!endOfPass)
            {
                //calculate which file the writer should write data to
                int writeTo=1+(mergeNum%size);

                //write the data
                while(!heap.isEmpty())
                {
                    int addr=Integer.parseInt(heap.peekRootAddr());
                    String input = readers[addr-size].getData();
                    String output;
                    if(!input.equals(END))
                    {
                        output =heap.replace(input,String.valueOf(addr));
                        writers[writeTo].write(output +"\n");
                    }
                    else
                    {
                        output =heap.remove();
                        writers[writeTo].write(output +"\n");
                    }

                }

                //write the symbol for the end of run and record how many times we write it
                writers[writeTo].write(END+"\n");
                endNum++;
                //record the last move so when we finish sorting, we know where to get the sorted data
                lastMove=writeTo;

                //get the heap and readers ready for the next iteration
                for(int i=1;i<=size;i++)
                {
                    data =readers[i].getData();
                    if(!readers[i].getEndOfFile())
                    {
                        readers[i].setEndOfRun(false);
                        heap.insert(data,String.valueOf(i+size));
                    }
                }

                mergeNum++;
                endOfPass =allFilesEnd(readers);
            }


            //close all readers and writers
            for(int i=1;i<=size;i++)
            {
                readers[i].close();
                writers[i].close();
            }

            //if we only have one sorted run
            //we are done!
            //write the data to standard output
            if(endNum<2)
            {
                String s=null;
                Scanner scanner=new Scanner(new File("T"+lastMove));
                while (scanner.hasNextLine())
                {
                    data=scanner.nextLine();
                    if(!data.equals(END))
                    {
                        System.out.println(data);
                    }
                }
                scanner.close();
            }
            return endNum<2;
        }
        catch (Exception e)
        {
            System.err.println(e);
            return false;
        }
    }

}