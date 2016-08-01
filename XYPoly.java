//Kevyn Jaremko
//kmj22
//Output located in comment at bottom

import java.lang.Math;
import java.util.Random;
import java.math.BigInteger;

public class XYPoly {
    
	// DO NOT CHANGE OR REMOVE THIS LINE (UNTIL STEP 3)
    //private PolyList list = new DLLPolyList();
	
	private PolyList list = new ArrayPolyList();

	private static long startTime;
	private static long endTime;
	
    public XYPoly(String s) {
		int c, i, j, scale = 1;
		int xIndex, yIndex;
		boolean hasX, hasY;
		
        //split string by white space
		String[] tokens = s.split(" ");
		
		//if first term is negative ie -4x + 3
		if (tokens[0].charAt(0)=='-'){
			scale = -1;
			tokens[0]= tokens[0].substring(1);
		}
		
		//iterate through tokens
		for (int a = 0; a < tokens.length; a++){
			if (tokens[a].equals("-")){
				scale = -1;
			}
			else if(tokens[a].equals("+")){
				scale = 1;
			}
			else{
				hasX = hasY = false;
				c = i = j = 0;
				
				//if there is an x, find it's position and c
				if ((xIndex = tokens[a].indexOf("x")) != -1){
					if (xIndex != 0)
						c = Integer.parseInt(tokens[a].substring(0,xIndex)) * scale;
					else{
						c = scale;
					}
					hasX = true;
				}
				
				//if there is a y, find it's position and j
				if ((yIndex = tokens[a].indexOf("y")) != -1){
					if (yIndex + 1 < tokens[a].length())
						j = Integer.parseInt(tokens[a].substring(yIndex + 1));
					else
						j = 1;
					hasY = true;
				}
				
				//based on whether there's an x, y, both , or neither,
				//find what's missing
				if (hasX && hasY){
					if (xIndex + 1 != yIndex)
						i = Integer.parseInt(tokens[a].substring(xIndex + 1, yIndex));
					else
						i = 1;						
				}
				else if (hasX){
					if (xIndex + 1 < tokens[a].length()) 
						i = Integer.parseInt(tokens[a].substring(xIndex + 1));
					else
						i = 1;
				}
				else if (hasY){
					if (yIndex != 0)
						c = Integer.parseInt(tokens[a].substring(0,yIndex)) * scale;
					else
						c = scale;
				}
				else{
					c = Integer.parseInt(tokens[a]) * scale;
				}

				list.insertTerm(c,i,j);
			}
		}
    }
	
	public XYPoly() {
	
	}
	
	public XYPoly(XYPoly p) {
		//scalarMultuply() returns a copy multiplied by a scalar
		//if the scalar is 1, it simply returns a copy
		XYPoly copy = p.scalarMultiply(1);
		this.list = copy.getList();
	}
	  
   
    public void output() {
		if (list.isEmpty()){
			System.out.println(0);
			return;
		}
		
		Position curr = list.first();
		int size = list.size();
		int c, i, j;
		String result = "";
		
		//add a negative sign at front if necessary
		if (curr.getC() < 0) 
			result += "-";
		
		for (int a = 0; a < size; a++){	
			c = Math.abs(curr.getC());
			i = curr.getI();
			j = curr.getJ();
			
			//print the constant
			if (c > 1 || (c == 1 && i == 0 && j == 0)){
				result += c;
			}
			else if (c == 0){
				continue;
			}
			
			//print x and degree if it's above 1
			if (i > 1)
				result += "x^" + i;
			else if (i == 1)
				result += "x";
			
			//print y and degree if it's above 1
			if (j > 1) 
				result += "y^" + j;
			else if (j == 1){
				result += "y";
			}
			
			//determine sign of next number
			if (!list.isLast(curr)){
				curr = list.after(curr);
				if (curr.getC() < 0) 
					result += " - "; 
				else if (curr.getC() > 0)
					result += " + ";
			}
		}
		if (result.length() > 0)
			System.out.println(result);
		else
			System.out.println(0);
    }
	     
    public boolean equalTo(XYPoly p) {
		//get p's list
		PolyList list2 = p.getList();
		int size = list.size();
		
		//if different sizes, they are not equal
		if (size != list2.size()){
			return false;
		}
		//if both are equal size and one is empty, they are equal
		else if (list.isEmpty()){
			return true;
		}
		
        Position curr1 = list.first();
        Position curr2 = list2.first();
		
		for (int a = 0; a < size; a++){
			if (curr1.getC() != curr2.getC() || 
				curr1.getI() != curr2.getI() || 
				curr1.getJ() != curr2.getJ()){
					return false;
			}
			if (!list.isLast(curr1)){	
				curr1 = list.after(curr1);
				curr2 = list2.after(curr2);
			}
		}
		return true;
    }
    
	public XYPoly scalarMultiply(int c) {
		int size = list.size();
		Position curr = list.first();
		
		XYPoly result = new XYPoly();
		PolyList newList = result.getList();
		
		//insert each term from original list into new list, multiplying constants by c
		for (int a = 0; a < size; a++){
			newList.insertTerm(curr.getC()*c,curr.getI(),curr.getJ());
		
			if (!list.isLast(curr))
				curr = list.after(curr);
		}
		return result;
	}
    
    public XYPoly add(XYPoly p) {
		//result - start with a deep copy of self
		XYPoly result = new XYPoly(this);
		PolyList newList = result.getList();
		
		//get position for p
		PolyList addList = p.getList();
		Position curr = addList.first();
		
		int size = addList.size();
		
		//insert the terms from p
		//insertTerm() handles addition if degrees are the same
		for (int a = 0; a < size; a++){
			newList.insertTerm(curr.getC(),curr.getI(),curr.getJ());

			if (!addList.isLast(curr))
				curr = addList.after(curr);
		}
		return result;
    }
    
    public XYPoly subtract(XYPoly p) {
        XYPoly negP = p.scalarMultiply(-1);
		
		//result - start with a deep copy of self
		XYPoly result = new XYPoly(this);
		PolyList newList = result.getList();
		
		//get position for negP
		PolyList addList = negP.getList();
		Position curr = addList.first();
		
		int size = addList.size();
		
		//insert the terms from negP
		//insertTerm() handles addition if degrees are the same
		for (int a = 0; a < size; a++){
			newList.insertTerm(curr.getC(),curr.getI(),curr.getJ());

			if (!addList.isLast(curr))
				curr = addList.after(curr);
		}
		return result;
    }
	
    
    public XYPoly multiply(XYPoly p) {
		XYPoly result = new XYPoly();
		PolyList newList = result.getList();
		
		PolyList list1 = this.getList();
		PolyList list2 = p.getList();
		Position curr1, curr2; 
		
		int size1 = list1.size();
		int size2 = list2.size();
		
		curr1 = list1.first();
				
		//iterate through first poly		
		for (int a = 0; a < size1; a++){
			//reset curr2 to first term of second poly
			curr2 = list2.first();
		
			//multiply the current term of poly1 by all terms in poly2
			for (int b = 0; b < size2; b++){
				newList.insertTerm(curr1.getC()*curr2.getC(),curr1.getI()+curr2.getI(),curr1.getJ()+curr2.getJ());
				
				if (!list2.isLast(curr2))
					curr2 = list2.after(curr2);
			}
			
			if (!list1.isLast(curr1))
				curr1 = list1.after(curr1);
		}
		return result;
    }
	
    public XYPoly power(int p) {
        if (p == 0){
			return new XYPoly("1");
		}
		else if (p == 1){
			return this;
		}
		else if (p == 2){
			return this.multiply(this);
		}
		else if (p % 2 == 1){
			XYPoly x = this.power((p - 1)/2);
			return this.multiply(x.multiply(x));
		}
		else {//if (p % 2 == 0){
			XYPoly x = this.power(p / 2);
			return x.multiply(x);
		}
		
    }
	
	//function for handling integer powers in evaluate
	private int power(int x, int p){
		if (p == 0){
			return 1;
		}
		else if (p == 1){
			return x;
		}
		else if (p % 2 == 1){
			int half = power(x,(p - 1) / 2);
			return half * half * x;
		}
		else{
			int half = power(x,p / 2);
			return half * half;
		}
	}
	
	public BigInteger evaluate(int x, int y) {
		BigInteger result = BigInteger.valueOf(0);
		int c, i, j;
		
		int size = list.size();
		int count = 0; 
		int GCX = Integer.MAX_VALUE, GCY = Integer.MAX_VALUE;
		
		if (size == 0){
			return result;
		}

		Position curr = list.first();
		XYPoly pFactor = new XYPoly();
		XYPoly pRest = new XYPoly();
		PolyList factorList = pFactor.getList();
		PolyList restList = pRest.getList();
		
		//iterate through list, finding greatest common degree of x and y
		for (int a = 0; a < size; a++){
			c = curr.getC();
			i = curr.getI();
			j = curr.getJ();
			
			//factoring from a constant won't help much
			if (j == 0 && i == 0)
				break;
			
			//GCX and GCY contain the greatest commmon degree of x and y in the poly
			if (GCX > i){
				GCX = i;
			}
			if (GCY > j){
				GCY = j;
			}
			
			//keep track of terms being factored
			if (!(GCX == 0 && GCY == 0)){
				count++;
			}

			//go to next item in list
			if (!list.isLast(curr))
				curr = list.after(curr);
		}
		
		curr = list.first();
	
		//if the poly is worth factoring
		if (count > 1){
			//add factored terms to pFactor			
			for (int a = 0; a < count; a++){
				factorList.insertTerm(curr.getC(),curr.getI()-GCX,curr.getJ()-GCY);
				
				if (!list.isLast(curr))
					curr = list.after(curr);
			} 	
			
			//insert the rest of the terms (that won't be factored) into pRest			
			for (int a = count; a < size; a++){
				restList.insertTerm(curr.getC(),curr.getI(),curr.getJ());
				
				if (!list.isLast(curr))
					curr = list.after(curr);
			} 			

			//x^GCX * y^GCY * pFactor.evaluate(x,y) + pRest.evaluate(x,y)
			result = result.add(BigInteger.valueOf(power(x,GCX)));
			result = result.multiply(BigInteger.valueOf(power(y,GCY)));
			result = result.multiply(pFactor.evaluate(x,y));
			result = result.add(pRest.evaluate(x,y));
		}
		
		//otherwise, just evaluate the polynomial
		//this acts as a base case
		else{
			for (int a = 0; a < size; a++){
				c = curr.getC();
				i = curr.getI();
				j = curr.getJ();
				
				result =  result.add(BigInteger.valueOf(c * power(x,i) * power(y,j)));

				if (!list.isLast(curr))
					curr = list.after(curr);				
			}
		}
		
		return result;
	}
	
	public PolyList getList(){
		return list;
	}
	
	public static void startTimer(){
		startTime = System.nanoTime();
	}
	
	public static void endTimer(){
		endTime = System.nanoTime();
		System.out.println("Task took " + (endTime - startTime) + " nanoseconds to complete.");
	}
	
    public static void main(String[] args){
		
		boolean val;
		XYPoly poly;
		BigInteger eval;
		
		//init test polys		
		XYPoly P = new XYPoly("5x4y3 - 2x3y4 + xy - 6");
		XYPoly Q = new XYPoly("-8x3y5 + 4x2y2 - x + y + 1");
		XYPoly R = new XYPoly("8x3 + 6x2y - 4x + 2");
		XYPoly S = new XYPoly("3xy - 5y5 + 3y3 - y");

		XYPoly T = new XYPoly();
		XYPoly[] cases = {P, Q, R, S, T};
		String[] names = {"P", "Q", "R", "S", "T"};
		
		//get random values for T
		PolyList tList = T.getList();
		Random rand = new Random();
		int c, i, j;
		
		for (int a = 0; a < 1000; a++){
			c = rand.nextInt(1000);
			i = Math.abs(rand.nextInt(1000));
			j = Math.abs(rand.nextInt(1000));
			tList.insertTerm(c,i,j);
		}
		
		for (int a = 0; a < 5; a++){
			System.out.println(names[a] + ":");
			cases[a].output();
			System.out.println();
		}
		
		//-------Step 1 Test Cases----------//
		System.out.println("******Step 1 Test Cases******");
				
		//For P-T perform the following, iterate through cases[]
		for (int a = 0; a < 5; a++){
			System.out.println("--------------");
			System.out.println("Performing Test Cases for " + names[a] + "\n");
			
			//make a copy using XYPoly() and print to stdout
			System.out.println("Here's a deep copy of " + names[a] + ":");
			
			startTimer();
			XYPoly copy = new XYPoly(cases[a]);
			endTimer();
			
			copy.output();

			//compare poly to every other poly and print result
			System.out.println("\nNow comparing each polynomial: \n");
			for (int b = 0; b < 5; b++){
				System.out.print(names[a] + " = " + names[b] + "? ");
				
				startTimer();
				val = cases[a].equalTo(cases[b]);
				endTimer();
				
				System.out.println(val);
			}
			
			//Scalar multiply by 5 and print result
			System.out.println("\nMultiplying " + names[a] + "  by 5 gives you: ");
			
			startTimer();
			poly = cases[a].scalarMultiply(5);
			endTimer();
			
			poly.output();
			
			//add/subtract every poly and print result
			System.out.println("\nNow add and subtract each polynomial: \n");			
			for (int b = 0; b < 5; b++){
				System.out.println("Adding " + names[a] + " + " + names[b]);
				
				startTimer();
				poly = cases[a].add(cases[b]);
				endTimer();
				
				poly.output();
				
				System.out.println("Subtracting " + names[a] + " - " + names[b]);
				
				startTimer();
				poly = cases[a].subtract(cases[b]);
				endTimer();
				
				poly.output();
			}
		}
		
		//-------Step 2 Test Cases----------//
		System.out.println("******Step 2 Test Cases******");		
				
		//Test cases will not be applied to XYPoly T
		for (int a = 0; a < 4; a++){
			System.out.println("--------------");
			System.out.println("Performing Test Cases for " + names[a] + "\n");
			cases[a].output();
			
			//multiply every poly and print result
			System.out.println("\nMultiplying by each polynomial: \n");			
			for (int b = 0; b < 4; b++){
				System.out.println("Multiplying: (" + names[a] + ") * (" + names[b] + ")");
				
				startTimer();
				poly = cases[a].multiply(cases[b]);
				endTimer();
				
				poly.output();
			}
			
			//Raise each poly to 5th, 10th, 20th, and 30th power
			System.out.println("\nRaise the polynomial to 5th, 10th, 20th, and 30th power:\n Only the result of raising to the 5th will be printed:\n");			
			System.out.println("(" + names[a] + ")^5");
			startTimer();
			poly = cases[a].power(5);
			endTimer();
			poly.output();
			
			System.out.println("(" + names[a] + ")^10");
			startTimer();
			poly = cases[a].power(10);
			endTimer();
			
			System.out.println("(" + names[a] + ")^20");
			startTimer();
			poly = cases[a].power(20);
			endTimer();
			
			System.out.println("(" + names[a] + ")^30");
			startTimer();
			poly = cases[a].power(30);
			endTimer();
			
			//Evaluate for (1, 1) , (2, 2), (-4, 3), and (-2, -1)
			System.out.println("\nEvaluating the polynomial for: \n");
			cases[a].output();
			
			System.out.println(names[a] + "(1,1) = ");
			startTimer();
			eval = cases[a].evaluate(1,1);
			endTimer();
			System.out.println(eval);
			
			System.out.println(names[a] + "(2,2) = ");
			startTimer();
			eval = cases[a].evaluate(2,2);
			endTimer();
			System.out.println(eval);
			
			System.out.println(names[a] + "(-4,3) = ");
			startTimer();
			eval = cases[a].evaluate(-4,3);
			endTimer();
			System.out.println(eval);
			
			System.out.println(names[a] + "(-2,-1) = ");
			startTimer();
			eval = cases[a].evaluate(-2,-1);
			endTimer();
			System.out.println(eval);
		}		
	}
}
/*------------***************************************-----------*/
/*-------------Running Time for Array Implementation------------*/
/*------------***************************************-----------*/
/*
******Step 1 Test Cases******
--------------
Performing Test Cases for P

Here's a deep copy of P:
Task took 18603 nanoseconds to complete.

Now comparing each polynomial:

P = P? Task took 22452 nanoseconds to complete.
P = Q? Task took 962 nanoseconds to complete.
P = R? Task took 1603 nanoseconds to complete.
P = S? Task took 1924 nanoseconds to complete.
P = T? Task took 641 nanoseconds to complete.

Multiplying P  by 5 gives you:
Task took 4811 nanoseconds to complete.

Now add and subtract each polynomial:

Adding P + P
Task took 12508 nanoseconds to complete.
Subtracting P - P
Task took 20848 nanoseconds to complete.
Adding P + Q
Task took 6415 nanoseconds to complete.
Subtracting P - Q
Task took 8018 nanoseconds to complete.
Adding P + R
Task took 5453 nanoseconds to complete.
Subtracting P - R
Task took 7377 nanoseconds to complete.
Adding P + S
Task took 5452 nanoseconds to complete.
Subtracting P - S
Task took 7377 nanoseconds to complete.
Adding P + T
Task took 5233849 nanoseconds to complete.
Subtracting P - T
Task took 3658691 nanoseconds to complete.
--------------
Performing Test Cases for Q

Here's a deep copy of Q:
Task took 5773 nanoseconds to complete.

Now comparing each polynomial:

Q = P? Task took 962 nanoseconds to complete.
Q = Q? Task took 3207 nanoseconds to complete.
Q = R? Task took 641 nanoseconds to complete.
Q = S? Task took 962 nanoseconds to complete.
Q = T? Task took 962 nanoseconds to complete.

Multiplying Q  by 5 gives you:
Task took 16999 nanoseconds to complete.

Now add and subtract each polynomial:

Adding Q + P
Task took 6415 nanoseconds to complete.
Subtracting Q - P
Task took 6736 nanoseconds to complete.
Adding Q + Q
Task took 9943 nanoseconds to complete.
Subtracting Q - Q
Task took 16358 nanoseconds to complete.
Adding Q + R
Task took 5132 nanoseconds to complete.
Subtracting Q - R
Task took 8981 nanoseconds to complete.
Adding Q + S
Task took 8019 nanoseconds to complete.
Subtracting Q - S
Task took 7377 nanoseconds to complete.
Adding Q + T
Task took 1099500 nanoseconds to complete.
Subtracting Q - T
Task took 2325050 nanoseconds to complete.
--------------
Performing Test Cases for R

Here's a deep copy of R:
Task took 17641 nanoseconds to complete.

Now comparing each polynomial:

R = P? Task took 1604 nanoseconds to complete.
R = Q? Task took 642 nanoseconds to complete.
R = R? Task took 2886 nanoseconds to complete.
R = S? Task took 962 nanoseconds to complete.
R = T? Task took 962 nanoseconds to complete.

Multiplying R  by 5 gives you:
Task took 17000 nanoseconds to complete.

Now add and subtract each polynomial:

Adding R + P
Task took 15716 nanoseconds to complete.
Subtracting R - P
Task took 6414 nanoseconds to complete.
Adding R + Q
Task took 20527 nanoseconds to complete.
Subtracting R - Q
Task took 21490 nanoseconds to complete.
Adding R + R
Task took 5774 nanoseconds to complete.
Subtracting R - R
Task took 7377 nanoseconds to complete.
Adding R + S
Task took 7056 nanoseconds to complete.
Subtracting R - S
Task took 9302 nanoseconds to complete.
Adding R + T
Task took 1044012 nanoseconds to complete.
Subtracting R - T
Task took 2039270 nanoseconds to complete.
--------------
Performing Test Cases for S

Here's a deep copy of S:
Task took 5773 nanoseconds to complete.

Now comparing each polynomial:

S = P? Task took 1604 nanoseconds to complete.
S = Q? Task took 1283 nanoseconds to complete.
S = R? Task took 1925 nanoseconds to complete.
S = S? Task took 3207 nanoseconds to complete.
S = T? Task took 962 nanoseconds to complete.

Multiplying S  by 5 gives you:
Task took 73450 nanoseconds to complete.

Now add and subtract each polynomial:

Adding S + P
Task took 7056 nanoseconds to complete.
Subtracting S - P
Task took 8339 nanoseconds to complete.
Adding S + Q
Task took 6736 nanoseconds to complete.
Subtracting S - Q
Task took 6094 nanoseconds to complete.
Adding S + R
Task took 5132 nanoseconds to complete.
Subtracting S - R
Task took 5132 nanoseconds to complete.
Adding S + S
Task took 4170 nanoseconds to complete.
Subtracting S - S
Task took 8981 nanoseconds to complete.
Adding S + T
Task took 1041446 nanoseconds to complete.
Subtracting S - T
Task took 2052742 nanoseconds to complete.
--------------
Performing Test Cases for T

Here's a deep copy of T:
Task took 1078331 nanoseconds to complete.

Now comparing each polynomial:

T = P? Task took 1603 nanoseconds to complete.
T = Q? Task took 1283 nanoseconds to complete.
T = R? Task took 962 nanoseconds to complete.
T = S? Task took 962 nanoseconds to complete.
T = T? Task took 197256 nanoseconds to complete.

Multiplying T  by 5 gives you:
Task took 1145365 nanoseconds to complete.

Now add and subtract each polynomial:

Adding T + P
Task took 1027333 nanoseconds to complete.
Subtracting T - P
Task took 1013862 nanoseconds to complete.
Adding T + Q
Task took 1019635 nanoseconds to complete.
Subtracting T - Q
Task took 1027333 nanoseconds to complete.
Adding T + R
Task took 1017390 nanoseconds to complete.
Subtracting T - R
Task took 1003919 nanoseconds to complete.
Adding T + S
Task took 1008730 nanoseconds to complete.
Subtracting T - S
Task took 1001673 nanoseconds to complete.
Adding T + T
Task took 1495614 nanoseconds to complete.
Subtracting T - T
Task took 2153134 nanoseconds to complete.
******Step 2 Test Cases******
--------------
Performing Test Cases for P

5x^4y^3 - 2x^3y^4 + xy - 6

Multiplying by each polynomial:

Multiplying: (P) * (P)
Task took 12830 nanoseconds to complete.
Multiplying: (P) * (Q)
Task took 8660 nanoseconds to complete.
Multiplying: (P) * (R)
Task took 16999 nanoseconds to complete.
Multiplying: (P) * (S)
Task took 17320 nanoseconds to complete.

Raise the polynomial to 5th, 10th, 20th, and 30th power:
 Only the result of raising to the 5th will be printed:

(P)^5
Task took 85317 nanoseconds to complete.
(P)^10
Task took 965430 nanoseconds to complete.
(P)^20
Task took 45541670 nanoseconds to complete.
(P)^30
Task took 903041971 nanoseconds to complete.

Evaluating the polynomial for:

5x^4y^3 - 2x^3y^4 + xy - 6
P(1,1) =
Task took 675481 nanoseconds to complete.
P(2,2) =
Task took 23414 nanoseconds to complete.
P(-4,3) =
Task took 28546 nanoseconds to complete.
P(-2,-1) =
Task took 33998 nanoseconds to complete.
--------------
Performing Test Cases for Q

-8x^3y^5 + 4x^2y^2 - x + y + 1

Multiplying by each polynomial:

Multiplying: (Q) * (P)
Task took 16679 nanoseconds to complete.
Multiplying: (Q) * (Q)
Task took 604597 nanoseconds to complete.
Multiplying: (Q) * (R)
Task took 13150 nanoseconds to complete.
Multiplying: (Q) * (S)
Task took 2565 nanoseconds to complete.

Raise the polynomial to 5th, 10th, 20th, and 30th power:
 Only the result of raising to the 5th will be printed:

(Q)^5
Task took 64148 nanoseconds to complete.
(Q)^10
Task took 3478435 nanoseconds to complete.
(Q)^20
Task took 110759507 nanoseconds to complete.
(Q)^30
Task took 653728516 nanoseconds to complete.

Evaluating the polynomial for:

-8x^3y^5 + 4x^2y^2 - x + y + 1
Q(1,1) =
Task took 96223 nanoseconds to complete.
Q(2,2) =
Task took 58054 nanoseconds to complete.
Q(-4,3) =
Task took 62224 nanoseconds to complete.
Q(-2,-1) =
Task took 41055 nanoseconds to complete.
--------------
Performing Test Cases for R

8x^3 + 6x^2y - 4x + 2

Multiplying by each polynomial:

Multiplying: (R) * (P)
Task took 35923 nanoseconds to complete.
Multiplying: (R) * (Q)
Task took 26942 nanoseconds to complete.
Multiplying: (R) * (R)
Task took 2566 nanoseconds to complete.
Multiplying: (R) * (S)
Task took 2245 nanoseconds to complete.

Raise the polynomial to 5th, 10th, 20th, and 30th power:
 Only the result of raising to the 5th will be printed:

(R)^5
Task took 22131 nanoseconds to complete.
(R)^10
Task took 240235 nanoseconds to complete.
(R)^20
Task took 1621987 nanoseconds to complete.
(R)^30
Task took 1969990 nanoseconds to complete.

Evaluating the polynomial for:

8x^3 + 6x^2y - 4x + 2
R(1,1) =
Task took 89487 nanoseconds to complete.
R(2,2) =
Task took 47149 nanoseconds to complete.
R(-4,3) =
Task took 52601 nanoseconds to complete.
R(-2,-1) =
Task took 47470 nanoseconds to complete.
--------------
Performing Test Cases for S

3xy - 5y^5 + 3y^3 - y

Multiplying by each polynomial:

Multiplying: (S) * (P)
Task took 3528 nanoseconds to complete.
Multiplying: (S) * (Q)
Task took 2887 nanoseconds to complete.
Multiplying: (S) * (R)
Task took 3528 nanoseconds to complete.
Multiplying: (S) * (S)
Task took 2245 nanoseconds to complete.

Raise the polynomial to 5th, 10th, 20th, and 30th power:
 Only the result of raising to the 5th will be printed:

(S)^5
Task took 27905 nanoseconds to complete.
(S)^10
Task took 186671 nanoseconds to complete.
(S)^20
Task took 4706550 nanoseconds to complete.
(S)^30
Task took 48396584 nanoseconds to complete.

Evaluating the polynomial for:

3xy - 5y^5 + 3y^3 - y
S(1,1) =
Task took 77940 nanoseconds to complete.
S(2,2) =
Task took 64790 nanoseconds to complete.
S(-4,3) =
Task took 14754 nanoseconds to complete.
S(-2,-1) =
Task took 33036 nanoseconds to complete.
*/
/*------------***********************************--------------*/
/*------------Running Time for DLL Implementation--------------*/
/*------------***********************************--------------*/
/*
******Step 1 Test Cases******
--------------
Performing Test Cases for P

Here's a deep copy of P:
Task took 16358 nanoseconds to complete.

Now comparing each polynomial:

P = P? Task took 20207 nanoseconds to complete.
P = Q? Task took 962 nanoseconds to complete.
P = R? Task took 1603 nanoseconds to complete.
P = S? Task took 1283 nanoseconds to complete.
P = T? Task took 642 nanoseconds to complete.

Multiplying P  by 5 gives you:
Task took 3849 nanoseconds to complete.

Now add and subtract each polynomial:

Adding P + P
Task took 11867 nanoseconds to complete.
Subtracting P - P
Task took 22452 nanoseconds to complete.
Adding P + Q
Task took 4811 nanoseconds to complete.
Subtracting P - Q
Task took 6735 nanoseconds to complete.
Adding P + R
Task took 4811 nanoseconds to complete.
Subtracting P - R
Task took 5774 nanoseconds to complete.
Adding P + S
Task took 4490 nanoseconds to complete.
Subtracting P - S
Task took 5452 nanoseconds to complete.
Adding P + T
Task took 3763893 nanoseconds to complete.
Subtracting P - T
Task took 1591516 nanoseconds to complete.
--------------
Performing Test Cases for Q

Here's a deep copy of Q:
Task took 3849 nanoseconds to complete.

Now comparing each polynomial:

Q = P? Task took 962 nanoseconds to complete.
Q = Q? Task took 3528 nanoseconds to complete.
Q = R? Task took 641 nanoseconds to complete.
Q = S? Task took 641 nanoseconds to complete.
Q = T? Task took 962 nanoseconds to complete.

Multiplying Q  by 5 gives you:
Task took 2887 nanoseconds to complete.

Now add and subtract each polynomial:

Adding Q + P
Task took 5132 nanoseconds to complete.
Subtracting Q - P
Task took 6415 nanoseconds to complete.
Adding Q + Q
Task took 8981 nanoseconds to complete.
Subtracting Q - Q
Task took 19886 nanoseconds to complete.
Adding Q + R
Task took 4170 nanoseconds to complete.
Subtracting Q - R
Task took 4491 nanoseconds to complete.
Adding Q + S
Task took 5773 nanoseconds to complete.
Subtracting Q - S
Task took 4811 nanoseconds to complete.
Adding Q + T
Task took 836492 nanoseconds to complete.
Subtracting Q - T
Task took 1083463 nanoseconds to complete.
--------------
Performing Test Cases for R

Here's a deep copy of R:
Task took 6736 nanoseconds to complete.

Now comparing each polynomial:

R = P? Task took 1925 nanoseconds to complete.
R = Q? Task took 641 nanoseconds to complete.
R = R? Task took 2566 nanoseconds to complete.
R = S? Task took 962 nanoseconds to complete.
R = T? Task took 962 nanoseconds to complete.

Multiplying R  by 5 gives you:
Task took 2566 nanoseconds to complete.

Now add and subtract each polynomial:

Adding R + P
Task took 4491 nanoseconds to complete.
Subtracting R - P
Task took 4811 nanoseconds to complete.
Adding R + Q
Task took 11226 nanoseconds to complete.
Subtracting R - Q
Task took 6094 nanoseconds to complete.
Adding R + R
Task took 4490 nanoseconds to complete.
Subtracting R - R
Task took 6094 nanoseconds to complete.
Adding R + S
Task took 6094 nanoseconds to complete.
Subtracting R - S
Task took 7698 nanoseconds to complete.
Adding R + T
Task took 819172 nanoseconds to complete.
Subtracting R - T
Task took 962864 nanoseconds to complete.
--------------
Performing Test Cases for S

Here's a deep copy of S:
Task took 3207 nanoseconds to complete.

Now comparing each polynomial:

S = P? Task took 1604 nanoseconds to complete.
S = Q? Task took 1283 nanoseconds to complete.
S = R? Task took 1603 nanoseconds to complete.
S = S? Task took 2566 nanoseconds to complete.
S = T? Task took 641 nanoseconds to complete.

Multiplying S  by 5 gives you:
Task took 25018 nanoseconds to complete.

Now add and subtract each polynomial:

Adding S + P
Task took 15716 nanoseconds to complete.
Subtracting S - P
Task took 5773 nanoseconds to complete.
Adding S + Q
Task took 6094 nanoseconds to complete.
Subtracting S - Q
Task took 5132 nanoseconds to complete.
Adding S + R
Task took 3208 nanoseconds to complete.
Subtracting S - R
Task took 4490 nanoseconds to complete.
Adding S + S
Task took 3207 nanoseconds to complete.
Subtracting S - S
Task took 12830 nanoseconds to complete.
Adding S + T
Task took 810191 nanoseconds to complete.
Subtracting S - T
Task took 968317 nanoseconds to complete.
--------------
Performing Test Cases for T

Here's a deep copy of T:
Task took 142729 nanoseconds to complete.

Now comparing each polynomial:

T = P? Task took 642 nanoseconds to complete.
T = Q? Task took 641 nanoseconds to complete.
T = R? Task took 321 nanoseconds to complete.
T = S? Task took 641 nanoseconds to complete.
T = T? Task took 194049 nanoseconds to complete.

Multiplying T  by 5 gives you:
Task took 141126 nanoseconds to complete.

Now add and subtract each polynomial:

Adding T + P
Task took 148182 nanoseconds to complete.
Subtracting T - P
Task took 147221 nanoseconds to complete.
Adding T + Q
Task took 148182 nanoseconds to complete.
Subtracting T - Q
Task took 148503 nanoseconds to complete.
Adding T + R
Task took 147220 nanoseconds to complete.
Subtracting T - R
Task took 146258 nanoseconds to complete.
Adding T + S
Task took 144654 nanoseconds to complete.
Subtracting T - S
Task took 143692 nanoseconds to complete.
Adding T + T
Task took 1958444 nanoseconds to complete.
Subtracting T - T
Task took 546222 nanoseconds to complete.
******Step 2 Test Cases******
--------------
Performing Test Cases for P

5x^4y^3 - 2x^3y^4 + xy - 6

Multiplying by each polynomial:

Multiplying: (P) * (P)
Task took 12189 nanoseconds to complete.
Multiplying: (P) * (Q)
Task took 7698 nanoseconds to complete.
Multiplying: (P) * (R)
Task took 5773 nanoseconds to complete.
Multiplying: (P) * (S)
Task took 16037 nanoseconds to complete.

Raise the polynomial to 5th, 10th, 20th, and 30th power:
 Only the result of raising to the 5th will be printed:

(P)^5
Task took 117391 nanoseconds to complete.
(P)^10
Task took 1376620 nanoseconds to complete.
(P)^20
Task took 277925461 nanoseconds to complete.
(P)^30
Task took 3556904556 nanoseconds to complete.

Evaluating the polynomial for:

5x^4y^3 - 2x^3y^4 + xy - 6
P(1,1) =
Task took 597861 nanoseconds to complete.
P(2,2) =
Task took 30150 nanoseconds to complete.
P(-4,3) =
Task took 17641 nanoseconds to complete.
P(-2,-1) =
Task took 17320 nanoseconds to complete.
--------------
Performing Test Cases for Q

-8x^3y^5 + 4x^2y^2 - x + y + 1

Multiplying by each polynomial:

Multiplying: (Q) * (P)
Task took 26301 nanoseconds to complete.
Multiplying: (Q) * (Q)
Task took 607483 nanoseconds to complete.
Multiplying: (Q) * (R)
Task took 1283 nanoseconds to complete.
Multiplying: (Q) * (S)
Task took 963 nanoseconds to complete.

Raise the polynomial to 5th, 10th, 20th, and 30th power:
 Only the result of raising to the 5th will be printed:

(Q)^5
Task took 40734 nanoseconds to complete.
(Q)^10
Task took 6107547 nanoseconds to complete.
(Q)^20
Task took 319125263 nanoseconds to complete.
(Q)^30
Task took 2085859755 nanoseconds to complete.

Evaluating the polynomial for:

-8x^3y^5 + 4x^2y^2 - x + y + 1
Q(1,1) =
Task took 74733 nanoseconds to complete.
Q(2,2) =
Task took 28225 nanoseconds to complete.
Q(-4,3) =
Task took 27904 nanoseconds to complete.
Q(-2,-1) =
Task took 25018 nanoseconds to complete.
--------------
Performing Test Cases for R

8x^3 + 6x^2y - 4x + 2

Multiplying by each polynomial:

Multiplying: (R) * (P)
Task took 1604 nanoseconds to complete.
Multiplying: (R) * (Q)
Task took 1604 nanoseconds to complete.
Multiplying: (R) * (R)
Task took 962 nanoseconds to complete.
Multiplying: (R) * (S)
Task took 962 nanoseconds to complete.

Raise the polynomial to 5th, 10th, 20th, and 30th power:
 Only the result of raising to the 5th will be printed:

(R)^5
Task took 12509 nanoseconds to complete.
(R)^10
Task took 328118 nanoseconds to complete.
(R)^20
Task took 1172629 nanoseconds to complete.
(R)^30
Task took 1667853 nanoseconds to complete.

Evaluating the polynomial for:

8x^3 + 6x^2y - 4x + 2
R(1,1) =
Task took 26301 nanoseconds to complete.
R(2,2) =
Task took 25659 nanoseconds to complete.
R(-4,3) =
Task took 24698 nanoseconds to complete.
R(-2,-1) =
Task took 37206 nanoseconds to complete.
--------------
Performing Test Cases for S

3xy - 5y^5 + 3y^3 - y

Multiplying by each polynomial:

Multiplying: (S) * (P)
Task took 1603 nanoseconds to complete.
Multiplying: (S) * (Q)
Task took 1283 nanoseconds to complete.
Multiplying: (S) * (R)
Task took 962 nanoseconds to complete.
Multiplying: (S) * (S)
Task took 1604 nanoseconds to complete.

Raise the polynomial to 5th, 10th, 20th, and 30th power:
 Only the result of raising to the 5th will be printed:

(S)^5
Task took 9302 nanoseconds to complete.
(S)^10
Task took 141446 nanoseconds to complete.
(S)^20
Task took 9368519 nanoseconds to complete.
(S)^30
Task took 152390385 nanoseconds to complete.

Evaluating the polynomial for:

3xy - 5y^5 + 3y^3 - y
S(1,1) =
Task took 129579 nanoseconds to complete.
S(2,2) =
Task took 35923 nanoseconds to complete.
S(-4,3) =
Task took 31112 nanoseconds to complete.
S(-2,-1) =
Task took 23093 nanoseconds to complete.
*/