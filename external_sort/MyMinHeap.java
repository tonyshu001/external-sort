import java.util.Scanner;

//written by Tony Shu (ID: 1356141) and Jiwei Wang (ID: 1360206)


//an abstract data structure that can be used to sort lines of text
public class MyMinHeap
{
    //in this particular assignment, a heap consists of multiple nodes
    //each node has two properties---the actual text to be sorted and what file it comes from
    class Node
    {
        //declare variables
        private String content;
        private String addr;

        //constructor of Node, takes two parameters: text and its address
        public Node(String line,String s)
        {
            content=line;
            addr=s;
        }

        //change the text stored in the node
        public void setContent(String s)
        {
            content=s;
        }

        //change the address stored in the node
        public void setAddr(String s)
        {
            addr=s;
        }

        //get the text stored in the node
        public String getContent()
        {
            return content;
        }

        //get the address stored in the node
        public String getAddr()
        {
            return addr;
        }
    }

    //declare variables
    private Node[] sArray;
    private int size;
    private int capacity;

    //constructor of MyMinHeap, takes one parameter: how large it is
    public MyMinHeap(int i)
    {
        //if the input is not valid, use a default size of 32
        if(i<=0)
        {
            capacity=32;
        }
        else
        {
            capacity=i;
        }

        //create an array to simulate the heap
        sArray=new Node[capacity];
        //heap has a default size of zero
        size=0;
    }

    //insert a node into the heap while keeping its heap order
    //takes two parameters: node's content and node's address
    public void insert(String s,String addr)
    {
        //if the heap is full, display an error message
        if(size+1>=capacity)
        {
            System.err.println("running out of spaces");
            return;
        }

        //add a new node to the end
        sArray[size+1]=new Node(s,addr);
        //increase size by one
        size++;
        //keep the heap order
        upHeap(size);
    }

    //remove the root of the heap while keeping its heap order
    //return the node's text that has been removed
    public String remove()
    {
        //if there is nothing to remove, we are done
        if(size<1)
        {
            return null;
        }

        //get the root's content
        String s=sArray[1].getContent();
        //swap the last node and the root of the heap
        swap(1,size);
        //reduce size by one
        size--;
        //keep the heap order
        downHeap(1);
        return s;
    }

    //replace the root of the heap while keeping its heap order
    //takes two parameters: new node's content and its address
    //return the node's content that has been replaced by a new node
    public String replace(String s,String addr)
    {
        //if the heap is empty, just insert a new node and we are done
        if(size<1)
        {
            insert(s,addr);
            return null;
        }

        //keep the content of the current root
        String oldRoot=sArray[1].getContent();
        //change the current root to the new node
        sArray[1].setContent(s);
        sArray[1].setAddr(addr);
        //keep the heap order
        downHeap(1);
        return oldRoot;
    }

    //take a look at the root and return its content
    public String peek()
    {
        //if the heap is empty, return null
        if(size==0)
        {
            return null;
        }
        else
        {
            return sArray[1].getContent();
        }
    }

    //make an array(not in any particular order) in heap order
    public void reheap()
    {
        //start from the last node's parent, up to the root
        for(int i=size/2;i>=1;i--)
        {
            //perform a downHeap operation to make it in heap order
            downHeap(i);
        }

    }

    //load lines of text in to an array
    //return true if we reaches the end of input while the array is still not full
    //otherwise return false
    //takes one parameter: the scanner to read text as input
    public boolean load(Scanner myReader)
    {
        size=0;
        String line;
        boolean endOfFile=false;

        try
        {
            String data="";

            //for each node in the array
            for (int i = 1; i < capacity; i++)
            {
                //if it is not the end of input
                if(myReader.hasNextLine())
                {
                    if ((data = myReader.nextLine())!=null)
                    {
                        //put the data into array
                        sArray[i]=new Node(data,null);
                        size++;
                    }
                }
                else
                {
                    endOfFile=true;
                }
            }


            return endOfFile;
        }
        catch (Exception e)
        {
            System.out.println(e);
            return true;
        }
    }

    //change the heap size to a value specified by the parameter
    public void changeSize(int s)
    {
        size=s;
    }

    //the method is designed specifically for the situation when there is no more upcoming input
    //unlike what the remove method does, this method will not store the data in the back of our heap
    //instead it makes the data null to avoid having duplicated output which will badly affect the sorting
    //returns what has been deleted
    public String delete()
    {
        //store root's data
        String s=sArray[1].getContent();
        //swap the root and the last node in our heap
        sArray[1]=sArray[size];
        //make the last node null
        sArray[size]=null;
        //reduce size by one
        size--;
        //keep the heap order
        reheap();
        return s;
    }

    //return true if the heap is full, false otherwise
    public boolean isFull()
    {
        return size+1<capacity;

    }

    //return if the heap is empty, false otherwise
    public boolean isEmpty()
    {
        return size==0;
    }

    //return true if there is any hole in our heap(null values generated by the delete method)
    public boolean isEmptyAtEnd()
    {
        boolean result=true;
        for(int i=1;i<capacity;i++)
        {
            //if the current node is not null, put it back into our heap
            if(sArray[i]!=null)
            {
                insert(sArray[i].getContent(),null);
                result=false;
            }
        }
        return result;
    }

    //swap two nodes in our heap
    //takes two parameters: node's index that need to be swapped
    private void swap(int index1,int index2)
    {
        Node temp=sArray[index1];
        sArray[index1]=sArray[index2];
        sArray[index2]=temp;
    }

    //a private method that is used to maintain the structure of the heap by searching up
    //typically used for inserting
    //the argument passed in is used to determine the start point
    private void upHeap(int index)
    {
        //as long as the node has no parent or the node is greater than its parent
        //we are done
        if(index/2<1||sArray[index].getContent().compareTo(sArray[index/2].getContent())>0)
        {
            return;
        }

        //otherwise swap the node and its parent and keep doing the work
        swap(index,index/2);
        upHeap(index/2);
    }

    //a private method that is used to maintain the structure of the heap by searching down
    //typically used for removing
    //the argument passed in is used to determine the start point
    private void downHeap(int index)
    {
        //declare variables
        int smallerIndex;
        int left=index*2;
        int right=index*2+1;

        //if the right child does not exist
        if(right>size)
        {
            //if the left child does not exist as well
            if(left>size)
            {
                //it means this node has no child, we are done
                return;
            }
            else
            {
                //if the node has only a left child, we assume it is smaller than the node for now
                smallerIndex=left;
            }
        }
        else
        {
            //if the node has two children
            //find the smaller child
            if(sArray[left].getContent().compareTo(sArray[right].getContent())<=0)
            {
                smallerIndex=left;
            }
            else
            {
                smallerIndex=right;
            }
        }

        //check if the smaller child we have found is really smaller than the node
        if(sArray[index].getContent().compareTo(sArray[smallerIndex].getContent())>0)
        {
            //if it is, swap and continue to finish the downHeap
            swap(index,smallerIndex);
            downHeap(smallerIndex);
        }
        else
        {
            //otherwise we are done
            return;
        }

    }

    //return the root's address
    public String peekRootAddr()
    {
        return sArray[1].getAddr();
    }

}