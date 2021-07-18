import java.io.File;
import java.util.Scanner;


//written by Tony Shu (ID: 1356141) and Jiwei Wang (ID: 1360206)


//use object-oriented programming methodology to solve problems that occur in MergeRuns
//the class is designed for handling reading data for MergeRuns
//it only has two properties: endOfRun and endOfFile
public class fileReader
{
    private Scanner sc;
    private boolean endOfRun;
    private boolean endOfFile;
    private String END="---end_of_run---";

    //constructor for fileReader
    //takes one parameter: which file this object is going to read
    public fileReader(String fileName)
    {
        try
        {
            sc=new Scanner(new File(fileName));
            endOfFile=false;
            endOfRun=false;
        }
        catch (Exception e)
        {
            System.err.println(e);
        }

    }

    //set endOfRun to a boolean value
    public void setEndOfRun(boolean b)
    {
        endOfRun=b;
    }

    //set endOfFile to a boolean value
    public void setEndOfFile(boolean b)
    {
        endOfFile=b;
    }

    //get endOfRun
    public boolean getEndOfRun()
    {
        return endOfRun;
    }

    //get endOfFile
    public boolean getEndOfFile()
    {
        return endOfFile;
    }


    //return the next line of data stored in files
    public String getData()
    {
        String data=null;
        if(sc.hasNextLine())
        {
            data=sc.nextLine();
            //if this is the end of run, set a flag to it
            if(data.equals(END))
            {
                endOfRun=true;
            }
            return data;
        }
        else
        {
            //set a endOfFile to it
            endOfFile=true;
            return null;
        }
    }

    //close the reader
    public void close()
    {
        sc.close();
    }
}
