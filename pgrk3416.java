//GAUR NALINAKSH    

import java.util.*;
import java.io.*;
import java.lang.*;
import static java.lang.Math.*;

public class pgrk3416 {

        int iter;
        int initval;
        String filename;
        int n;      // number of vertices in the graph
        int m;      // number of edges in the graph
        int[][] L;  // adjacency matrix 
        final double d = 0.85;
        final double errorrate = 0.00001;
        double[] Src;
        int[] C; // array to contain number of outgoing links for page 'Ti' 

    pgrk3416() {} //default constructor

    pgrk3416(int iter, int initval, String filename)     // 3 argument constructor to initialize class variables with provided command line arguments
    {
        this.iter = iter;
        this.initval = initval;
        this.filename = filename;
        int p = 0;
        int q = 0;
        try {        
            Scanner scanner = new Scanner(new File(filename));
            n = scanner.nextInt();
            m = scanner.nextInt();
            
            //Adjacency matrix representation of graph
            L = new int[n][n];
            for(int i = 0; i < n; i++)
             for(int j = 0; j < n; j++)
               L[i][j] = 0;
            
            while(scanner.hasNextInt())
            {
                p = scanner.nextInt();
                q = scanner.nextInt();
                L[p][q] = 1;
            }
            

            //C[i] --> number of outgoing links of page 'Ti'
            C = new int[n];
            for(int i = 0; i < n; i++) {
                C[i] = 0;
                for(int j = 0; j < n; j++) {
                    C[i] += L[i][j];
                }
            }

            Src = new double[n];
            switch(initval) {
            case 0:
              for(int i = 0; i < n; i++) {
                Src[i] = 0;
              }
              break;
            case 1:
              for(int i = 0; i < n; i++) {
                Src[i] = 1;
              }
              break;
            case -1:
              for(int i =0; i < n; i++) {
                Src[i] = 1.0/n;
              }
              break;
            case -2:
              for(int i =0; i < n; i++) {
                Src[i] = 1.0/Math.sqrt(n);
              }
              break;
            }
        }
        catch(FileNotFoundException fnfe)
        {
        }
    }
    
    public static void main(String[] args)
    {
        if(args.length != 3) {
            System.out.println("Usage: pgrk3416 iterations initialvalue filename");
            return;
        }
        //command line arguments
        int iterations = Integer.parseInt(args[0]);
        int initialvalue = Integer.parseInt(args[1]);
        String filename = args[2];

        if( !(initialvalue >= -2 && initialvalue <= 1) ) {
            System.out.println("Enter -2, -1, 0 or 1 for initialvalue");
            return;
        }

        pgrk3416 pr = new pgrk3416(iterations, initialvalue, filename);

        pr.pgrkAlgo3416();
    }

    boolean isConverged3416(double[] src, double[] target)
    {
        for(int i = 0; i < n; i++)
        {
          if ( abs(src[i] - target[i]) > errorrate )
            return false;
        }
        return true;
    }

    void pgrkAlgo3416() {
        double[] D = new double[n];
        boolean flag = true;
        // If the graph has N greater than 10, then the values for iterations, initialvalue revert to 0 and -1 respectively.
        if(n > 10) {
            iter = 0;
            for(int i =0; i < n; i++) {
              Src[i] = 1.0/n;
            }
            int i = 0;
          do {
               if(flag == true)
               {
                  flag = false;
               }
               else
               {
                 for(int l = 0; l < n; l++) {
                   Src[l] = D[l];
                 }
               }
               for(int l = 0; l < n; l++) {
                 D[l] = 0.0;
               }

               for(int j = 0; j < n; j++) {
                 for(int k = 0; k < n; k++)
                 {
                    if(L[k][j] == 1) {
                        D[j] += Src[k]/C[k];
                    }
                 }
               }

               //Compute and print pagerank of all pages
               for(int l = 0; l < n; l++) {
                 D[l] = d*D[l] + (1-d)/n;
               }
               i++;
             } while (isConverged3416(Src, D) != true);

             // print pageranks at the stopping iteration 
             System.out.println("Iter: " + i);
             for(int l = 0 ; l < n; l++) {
                 System.out.printf("P[" + l + "] = %.6f\n",Math.round(D[l]*1000000.0)/1000000.0);
             }
             return;
        }
        //Base Case
        System.out.print("Base    : 0");
        for(int j = 0; j < n; j++) {
            System.out.printf(" :P[" + j + "]=%.6f",Math.round(Src[j]*1000000.0)/1000000.0);
        }

        if (iter != 0) {
          for(int i = 0; i < iter; i++)
          {
              for(int l = 0; l < n; l++) {
                D[l] = 0.0;
              }
            
              for(int j = 0; j < n; j++) {
                for(int k = 0; k < n; k++)
                {
                    if(L[k][j] == 1) {
                        D[j] += Src[k]/C[k];
                    } 
                }
              }

              //Compute and print pagerank of all pages
              System.out.println();
              System.out.print("Iter    : " + (i+1));
              for(int l = 0; l < n; l++) {
                D[l] = d*D[l] + (1-d)/n;
                System.out.printf(" :P[" + l + "]=%.6f",Math.round(D[l]*1000000.0)/1000000.0);
              }
            
              for(int l = 0; l < n; l++) {
                Src[l] = D[l]; 
              } 
          }
          System.out.println();
        }
        else 
        { //number of iterations is 0, try convergence
          int i = 0;
          do {
               if(flag == true)
               {
                  flag = false;
               }
               else
               {
                 for(int l = 0; l < n; l++) {
                   Src[l] = D[l];
                 }
               }
               for(int l = 0; l < n; l++) {
                 D[l] = 0.0;
               }

               for(int j = 0; j < n; j++) {
                 for(int k = 0; k < n; k++)
                 {
                    if(L[k][j] == 1) {
                        D[j] += Src[k]/C[k];
                    }
                 }
               }

               //Compute and print pagerank of all pages
               System.out.println(); 
               System.out.print("Iter    : " + (i+1));
               for(int l = 0; l < n; l++) {
                 D[l] = d*D[l] + (1-d)/n;
                 System.out.printf(" :P[" + l + "]=%.6f",Math.round(D[l]*1000000.0)/1000000.0);
               }
               i++;  
             } while (isConverged3416(Src, D) != true);  
        System.out.println(); 
        }
    }
}
