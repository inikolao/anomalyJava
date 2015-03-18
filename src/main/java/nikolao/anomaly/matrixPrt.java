/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package nikolao.anomaly;

import java.util.ArrayList;
import java.util.List;
import static nikolao.anomaly.util.getNumFileNames;

/**
 *
 * @author nikolao {Ioannis Nikolaou AM 4504 mailto:Gianhspc@gmail.com}
 * 16/3/2015 Created{First write}
 */
public class matrixPrt {
    private final String[] filenames;
    private final int[] port;
    private final List<String> matrix=new ArrayList();
    private final int size;
    private int help;
    
    public matrixPrt()
    {
        this.size=getNumFileNames();
        this.help=getNumFileNames();
        this.filenames=new String[size];
        this.port=new int[size];
    }
    public String[] getFilename()
    {
        return filenames;
    }
    public int[] getPort()
    {
        return port;
    }
    public int getSize()
    {
        return size;
    }
    public List<String> getMatrix()
    {
        return matrix;
    }
    public void SetNewMember(String filename,int port)
    {
        //System.out.println("size   :::"+getSize());
        this.filenames [(size-help)]=filename;
        this.port [(size-help)]=port;
        this.help--;
        this.matrix.add(filename+","+port);
    }
    public int getMatrixSize(matrixPrt prt)
    {
    return prt.matrix.size();
    }
}
