//GAUR NALINAKSH    

import java.util.*;
import java.io.*;
import java.lang.*;
import static java.lang.Math.*;

public class hits3416 {

        int iter;
        int initval;
        String filename;
        int n;      // number of vertices in the graph
        int m;      // number of edges in the graph
        int[][] L;  // adjacency matrix 
        double[] h0;
        double[] a0;
        final double errorrate = 0.00001; 

    hits3416() {} //default constructor

    hits3416(int iter, int initval, String filename)     // 3 argument constructor to initialize class variables with provided command line arguments
    {
        this.iter = iter;
        this.initval = initval;
        this.filename = filename;
        try {        
            Scanner scanner = new Scanner(new File(filename));
            n = scanner.nextInt();
            m = scanner.nextInt();
            //System.out.println("n = " + n + " m = " + m);
            
            //Adjacency matrix representation of graph
            L = new int[n][n];
            for(int i = 0; i < n; i++)
             for(int j = 0; j < n; j++)
               L[i][j] = 0;

            while(scanner.hasNextInt())
            {
                L[scanner.nextInt()][scanner.nextInt()] = 1; 
                //System.out.println(scanner.nextInt());
            }
            
            /*for(int i = 0; i < n; i++) {
             System.out.println();
             System.out.print(i + ": ");
             for(int j = 0; j < n; j++)
               System.out.print(L[i][j] + " ");
            }*/

            h0 = new double[n];
            a0 = new double[n];
            switch(initval) {
            case 0:
              for(int i = 0; i < n; i++) {
                h0[i] = 0;
                a0[i] = 0;
              }
              break;
            case 1:
              for(int i = 0; i < n; i++) {
                h0[i] = 1;
                a0[i] = 1;
              }
              break;
            case -1:
              for(int i =0; i < n; i++) {
                h0[i] = 1.0/n;
                a0[i] = 1.0/n;
              }
              break;
            case -2:
              for(int i =0; i < n; i++) {
                h0[i] = 1.0/Math.sqrt(n);
                a0[i] = 1.0/Math.sqrt(n);
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
            System.out.println("Usage: hits3416 iterations initialvalue filename");
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

        hits3416 ht = new hits3416(iterations, initialvalue, filename);

        ht.hitsAlgo3416();
    }
 
    boolean isConverged(double[] p, double[] q)
    {
       for(int i = 0 ; i < n; i++) {
           if ( abs(p[i] - q[i]) > errorrate ) 
             return false;
       }
       return true;
    } 
    
    public void hitsAlgo3416()
    {
        double[] h = new double[n];
        double[] a = new double[n];
        double a_scale_factor = 0.0;
        double a_sum_square = 0.0;
        double h_scale_factor = 0.0;
        double h_sum_square = 0.0; 
        double[] aprev = new double[n]; //last iterations values of a, used for convergence
        double[] hprev = new double[n]; //last iterations values of h, used for convergence

        //If the graph has N greater than 10, then the values for iterations, initialvalue revert to 0 and -1 respectively
        if(n > 10) {
            iter = 0;
            for(int i =0; i < n; i++) {
                h[i] = 1.0/n;
                a[i] = 1.0/n;
                hprev[i] = h[i];
                aprev[i] = a[i];
            }
            
          int i = 0;
          do {  
               for(int r = 0; r < n; r++) {
                   aprev[r] = a[r];
                   hprev[r] = h[r];
               }

                //A step starts
                for(int p = 0; p < n; p++) {
                    a[p] = 0.0;
                }
            
                for(int j = 0; j < n; j++) {
                    for(int k = 0; k < n; k++) {
                        if(L[k][j] == 1) {
                            a[j] += h[k]; 
                        }
                    }
                }//A step ends

                //H step starts
                for(int p = 0; p < n; p++) {
                    h[p] = 0.0;
                }

                for(int j = 0; j < n; j++) {
                    for(int k = 0; k < n; k++) {
                        if(L[j][k] == 1) {
                            h[j] += a[k]; 
                        }
                    }
                }//H step ends

                //Scaling A starts
                a_scale_factor = 0.0;
                a_sum_square = 0.0;
                for(int l = 0; l < n; l++) {
                    a_sum_square += a[l]*a[l];    
                }
                a_scale_factor = Math.sqrt(a_sum_square); 
                for(int l = 0; l < n; l++) {
                    a[l] = a[l]/a_scale_factor;
                }//Scaling A ends  
 
                //Scaling H starts
                h_scale_factor = 0.0;
                h_sum_square = 0.0;
                for(int l = 0; l < n; l++) {
                    h_sum_square += h[l]*h[l];    
                }
                h_scale_factor = Math.sqrt(h_sum_square); 
                for(int l = 0; l < n; l++) {
                    h[l] = h[l]/h_scale_factor;
                }// Scaling H ends
                i++; // incr the interation counter
          } while( false == isConverged(a, aprev) || false == isConverged(h, hprev));
          System.out.println("Iter:    " + i);
          for(int l = 0; l < n; l++) {
              System.out.printf(" A/H[%d]=%.6f/%.6f\n",l,Math.round(a[l]*1000000.0)/1000000.0,Math.round(h[l]*1000000.0)/1000000.0); 
          }
          return;
        }

        //Initialization
        for(int i = 0; i < n; i++)
        {
            h[i] = h0[i];
            a[i] = a0[i];
            hprev[i] = h[i];
            aprev[i] = a[i]; 
        }
        
        //Base Case
        System.out.print("Base:    0 :");
        for(int i = 0; i < n; i++) {
          System.out.printf(" A/H[%d]=%.6f/%.6f",i,Math.round(a0[i]*1000000.0)/1000000.0,Math.round(h0[i]*1000000.0)/1000000.0); 
          //System.out.println("a0[" + i + "]= " + a0[i]); 
        }
        
        if (iter != 0) { 
            for(int i = 0; i < iter; i++) { //iteration starts
            
                //A step starts
                for(int p = 0; p < n; p++) {
                    a[p] = 0.0;
                }
            
                for(int j = 0; j < n; j++) {
                    for(int k = 0; k < n; k++) {
                        if(L[k][j] == 1) {
                            a[j] += h[k]; 
                        }
                    }
                }//A step ends

                //H step starts
                for(int p = 0; p < n; p++) {
                    h[p] = 0.0;
                }

                for(int j = 0; j < n; j++) {
                    for(int k = 0; k < n; k++) {
                        if(L[j][k] == 1) {
                            h[j] += a[k]; 
                        }
                    }
                }//H step ends

                //Scaling A starts
                a_scale_factor = 0.0;
                a_sum_square = 0.0;
                for(int l = 0; l < n; l++) {
                    a_sum_square += a[l]*a[l];    
                }
                a_scale_factor = Math.sqrt(a_sum_square); 
                for(int l = 0; l < n; l++) {
                    a[l] = a[l]/a_scale_factor;
                }//Scaling A ends  
 
                //Scaling H starts
                h_scale_factor = 0.0;
                h_sum_square = 0.0;
                for(int l = 0; l < n; l++) {
                    h_sum_square += h[l]*h[l];    
                }
                h_scale_factor = Math.sqrt(h_sum_square); 
                for(int l = 0; l < n; l++) {
                    h[l] = h[l]/h_scale_factor;
                }// Scaling H ends
            
                System.out.println();
                System.out.print("Iter:    " + (i+1) + " :");
                for(int l = 0; l < n; l++) {
                    System.out.printf(" A/H[%d]=%.6f/%.6f",l,Math.round(a[l]*1000000.0)/1000000.0,Math.round(h[l]*1000000.0)/1000000.0); 
                }
   
            }//iteration ends
        } // if iter != 0 ends
        else
        {
          int i = 0;
          do {  
                for(int r = 0; r < n; r++) {
                    aprev[r] = a[r];
                    hprev[r] = h[r];
                }

                //A step starts
                for(int p = 0; p < n; p++) {
                    a[p] = 0.0;
                }
            
                for(int j = 0; j < n; j++) {
                    for(int k = 0; k < n; k++) {
                        if(L[k][j] == 1) {
                            a[j] += h[k]; 
                        }
                    }
                }//A step ends

                //H step starts
                for(int p = 0; p < n; p++) {
                    h[p] = 0.0;
                }

                for(int j = 0; j < n; j++) {
                    for(int k = 0; k < n; k++) {
                        if(L[j][k] == 1) {
                            h[j] += a[k]; 
                        }
                    }
                }//H step ends

                //Scaling A starts
                a_scale_factor = 0.0;
                a_sum_square = 0.0;
                for(int l = 0; l < n; l++) {
                    a_sum_square += a[l]*a[l];    
                }
                a_scale_factor = Math.sqrt(a_sum_square); 
                for(int l = 0; l < n; l++) {
                    a[l] = a[l]/a_scale_factor;
                }//Scaling A ends  
 
                //Scaling H starts
                h_scale_factor = 0.0;
                h_sum_square = 0.0;
                for(int l = 0; l < n; l++) {
                    h_sum_square += h[l]*h[l];    
                }
                h_scale_factor = Math.sqrt(h_sum_square); 
                for(int l = 0; l < n; l++) {
                    h[l] = h[l]/h_scale_factor;
                }// Scaling H ends
                i++; // incr the interation counter
                System.out.println();
                System.out.print("Iter:    " + i + " :");
                for(int l = 0; l < n; l++) {
                    System.out.printf(" A/H[%d]=%.6f/%.6f",l,Math.round(a[l]*1000000.0)/1000000.0,Math.round(h[l]*1000000.0)/1000000.0); 
                }
          } while( false == isConverged(a, aprev) || false == isConverged(h, hprev));
        }
        System.out.println();
    }
}
