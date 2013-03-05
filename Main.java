/**
 * 
 *     This file is part of ag41-print11-challenge.
 *     
 *     ag41-print11-challenge is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *     
 *     ag41-print11-challenge is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *     
 *     You should have received a copy of the GNU General Public License
 *     along with ag41-print11-challenge.  If not, see <http://www.gnu.org/licenses/>.
 *     
 */


import gproblem.GSupplyLinkProblem;
import gsolver.GSolver;

/**
 * 
 * @author Olivier Grunder
 * @version 0.03
 * @date 22 avril 2011
 *
 */
public class Main {
	
	private static GSolver getSolver(GSupplyLinkProblem pb, long solvingtime) {
		return new MySolver(pb,(int) solvingtime) ;
//		return new OGSolverRandom(pb, solvingtime) ;
	}
	private static final int MAX_CHALLENGE = 3;

	// challenge file names
	public static final String[] challengeFilenames = {
		"data/challenge050a.txt",
		"data/challenge050b.txt",
		"data/challenge050c.txt",
		"data/challenge050d.txt",

		"data/challenge100a.txt",
		"data/challenge100b.txt",
		"data/challenge100c.txt",
		"data/challenge100d.txt",

		"data/challenge200a.txt",
		"data/challenge200b.txt",
		"data/challenge200c.txt",
		"data/challenge200d.txt"
} ;
		
	//	- 10 secondes pour les petites instances (50 jobs)
	//	- 30 secondes pour les moyennes instances (100 jobs)
	//	- 1 minutes pour les grandes instances (200 jobs)
	public static final long[] solvingTime = {
		10000,
		10000,
		10000,
		10000,
		30000,
		30000,
		30000,
		30000,
		60000,
		60000,
		60000,
		60000
        } ;

	public static void main(String args[]) {
		System.out.println("args[0]="+args[0]) ;
		int c = new Integer(args[0]).intValue() ;
		
		System.out.println ("Loading challenge file : "+challengeFilenames[c] ) ;
		GSupplyLinkProblem pb = new GSupplyLinkProblem(challengeFilenames[c]) ;
		// New solver
		GSolver solv = getSolver(pb, solvingTime[c]) ;
		solv.setSolvingTime(solvingTime[c]);
		solv.start() ;
		

		// Loading of a solution
//		GSupplyLinkSolution sol2 = new GSupplyLinkSolution(pb, "data/instance007a-sol02.txt") ;
//		sol2.evaluate() ;
//		System.out.println (sol2.toString()+"\n") ;
		
		// Generation of challenge instances
//		for (int n=50;n<=200;n*=2) {
//			for (int i=0;i<10;i++ ) {
//				GSupplyLinkProblem pb = GSupplyLinkProblem.generateRandom(n) ;
//				//System.out.println ("slpb="+pb.toString()) ;
//				char c = (char) ('a'+i) ;
//				String name = "data/challenge0"+n+c+".txt" ;
//				System.out.println("name="+name) ;
//				pb.save(name) ;
//			}
//		}
	}


}
