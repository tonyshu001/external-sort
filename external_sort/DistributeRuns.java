import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Scanner;

//written by Tony Shu (ID: 1356141) and Jiwei Wang (ID: 1360206)



//distribute sorted runs into a number of temporary files specified by the program arguments
public class DistributeRuns
{
    private int fileNum;
    private int runs;
    private ArrayList<BufferedWriter> writers;
    Scanner myReader;
    private String data="";
    final String End="---end_of_run---";

    public DistributeRuns(int num)
    {
        try
        {
            if(num<=1)
            {
                fileNum=2;
            }
            else
            {
                fileNum=num;
            }

            //declare variables
            writers=new ArrayList<BufferedWriter>();

            myReader = new Scanner(System.in);

            //using a list bufferedWriter to control temporary files
            for(int i=1;i<=fileNum;i++)
            {
                writers.add(new BufferedWriter(new FileWriter("T"+i)));
            }
        }
        catch (Exception e)
        {
            System.err.println(e);
        }

        runs=0;
    }

    //distribute sorted runs to several files
    public void distribute()
    {
        try
        {

            //use a simple modulo division to decide which writer should take
            //care of the current run
            while(myReader.hasNextLine())
            {
                data=myReader.nextLine();
                writers.get(runs%fileNum).write(data+"\n");
                if(data.equals(End))
                {
                    runs++;
                }

            }

            //close all writers
            for(int i=0;i<writers.size();i++)
            {
                writers.get(i).close();
            }
        }
        catch (Exception e)
        {
            System.err.println(e);
        }

    }

    public int getFileNum()
    {
        return fileNum;
    }


}