package c4w2;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

public class TSP
{
    static int nC;
  
    static double[][] cM;
  
    static double[][] cMPi;
    static Node fNode = new Node();
  
  
    public static void main(String[] args)
    {
        List<City> citiesArray = CitiesArray();

        createDistMatrix(citiesArray);
 
        System.out.println("min cost " + pTSP());
    }
    
  
    private static void createDistMatrix(List<City> citiesArray)
    {
        cM = new double[nC][nC];
        
        for(int i = 0; i < nC; i++)
        {
            City city = citiesArray.get(i);
            
            for(int j = 0; j < nC; j++)
            {
                City otherCity = citiesArray.get(j);
                
                cM[i][j] = city.calcDist(otherCity);
            }
        }
    }
    private static List<City> CitiesArray()
    {
        ArrayList<City> citiesArray = new ArrayList<City>();
        
        try
        {
            BufferedReader br =  new BufferedReader(new FileReader("C:\\Users\\MOJO-JOJO\\Desktop\\tsp.txt"));
        
            nC = Integer.parseInt(br.readLine());
            
            for(int i = 0; i < nC; i++)
            {
                String line = br.readLine();
                
                double x  = Double.valueOf(line.split(" ")[0]);
                double y  = Double.valueOf(line.split(" ")[1]);
                
                citiesArray.add(new City(x , y ));
            }
        }
        catch(Exception e){
        }
        
        return citiesArray;
    }
 
    private static double pTSP() 
    {
        fNode.lB = Double.MAX_VALUE;
        Node cNode = new Node();
        cNode.ex = new boolean[nC][nC];
        cMPi = new double[nC][nC];
        cNode.computeHK();
        
        PriorityQueue<Node> pq = new PriorityQueue<Node>(nC, new pointC());
        
        do{
            do{
                int i = -1;
                for(int j = 0; j < nC; j++) 
                {
                    if(cNode.de[j] > 2 && (i < 0 || cNode.de[j] < cNode.de[i])){
                        i = j;
                    }
                }
                
                if(i < 0){
                    if(cNode.lB < fNode.lB) {
                        fNode = cNode;
                    }
                    break;
                }
                
                PriorityQueue<Node> decendant = new PriorityQueue<Node>(nC, new pointC());
                decendant.add(cNode.exclude(i, cNode.pa[i]));
                
                for(int j = 0; j < nC; j++) 
                {
                    if(cNode.pa[j] == i) {
                        decendant.add(cNode.exclude(i, j));
                    }
                }
                
                cNode = decendant.poll();
                pq.addAll(decendant);
            } while(cNode.lB < fNode.lB);
            
            cNode = pq.poll();
        } while (cNode != null && cNode.lB < fNode.lB);
        
        return fNode.lB;
    }
    
    static class Node 
    {
        boolean[][] ex;
        double[] pie;
        double lB;
        int[] de;
        int[] pa;
             
        public void computeHK() 
        {
            this.pie = new double[nC];
            this.lB = Double.MIN_VALUE;
            this.de = new int[nC];
            this.pa = new int[nC];
            
            double phi = 0.1;
            while(phi > 1e-06) 
            {
                double prevB = this.lB;
                computeOneTree();
                
                if(!(this.lB < fNode.lB)) {
                    return;
                }
                
                if(!(this.lB < prevB)) {
                    phi *= 0.9;
                }
                
                int den = 0;
                for(int i = 1; i < nC; i++) 
                {
                    int d = this.de[i] - 2;
                    den += d * d;
                }
                
                if(den == 0){
                    return;
                }
                
                double t = phi * this.lB / den;
                for(int i = 1; i < nC; i++){
                    this.pie[i] += t * (this.de[i] - 2);
                }
            }
        }
        private Node exclude(int i, int j)
        {
            Node child = new Node();
            child.ex = this.ex.clone();
            child.ex[i] = this.ex[i].clone();
            child.ex[j] = this.ex[j].clone();
            child.ex[i][j] = true;
            child.ex[j][i] = true;

            child.computeHK();

            return child;
        }
        
        private void addEdge(int i, int j)
        {
            double q = this.lB;
            this.lB += cMPi[i][j];
            this.de[i]++;
            this.de[j]++;
        }
        
        private void computeOneTree() 
        {
            this.lB = 0.0;
            Arrays.fill(this.de, 0);
            for (int i = 0; i < nC; i++) {
                for (int j = 0; j < nC; j++) {
                    cMPi[i][j] = this.ex[i][j] ? Double.MAX_VALUE : cM[i][j] + this.pie[i] + this.pie[j];
                }
            }

            int firstN;
            int secondN;

            if (cMPi[0][2] < cMPi[0][1]) {
                firstN = 2;
                secondN = 1;
            } else {
                firstN = 1;
                secondN = 2;
            }

            for (int j = 3; j < nC; j++) {
                if (cMPi[0][j] < cMPi[0][secondN]) {
                    if (cMPi[0][j] < cMPi[0][firstN]) {
                        secondN = firstN;
                        firstN = j;
                    } else {
                        secondN = j;
                    }
                }
            }

            addEdge(0, firstN);
            Arrays.fill(this.pa, firstN);
            this.pa[firstN] = 0;

            double[] minC = cMPi[firstN].clone();

            for (int k = 2; k < nC; k++) {
                int i;
                for (i = 1; i < nC; i++) {
                    if (this.de[i] == 0) {
                        break;
                    }
                }

                for (int j = i + 1; j < nC; j++) {
                    if (this.de[j] == 0 && minC[j] < minC[i]) {
                        i = j;
                    }
                }

                addEdge(this.pa[i], i);
                for (int j = 1; j < nC; j++) {
                    if (this.de[j] == 0 && cMPi[i][j] < minC[j]) {
                        minC[j] = cMPi[i][j];
                        this.pa[j] = i;
                    }
                }
            }

            addEdge(0, secondN);
            this.pa[0] = secondN;
        }
    }

    static class pointC implements Comparator<Node>
    {
        @Override
        public int compare(Node a, Node b) {
            return Double.compare(a.lB, b.lB);
        }
    }

    static class City
    {
        double x ;
        double y ;
        
        public City(double x , double y ){
            this.x  = x ;
            this.y  = y ;
        }

        public double calcDist(City other){
            return Math.sqrt(Math.pow(x  - other.x , 2) + Math.pow(y  - other.y , 2));
        }
    }
}
