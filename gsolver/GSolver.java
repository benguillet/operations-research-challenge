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
package gsolver;

import gio.GLog;
import gproblem.GSupplyLinkProblem;
import gsolution.GSupplyLinkSolution;

import java.util.Calendar;
import java.util.Date;
import java.util.Random;

/**
 * A solver
 * 
 * @author Olivier Grunder
 * @version 0.02
 * @date 14 avril 2011
 *
 */
public class GSolver extends Thread {

	
	protected GSupplyLinkProblem problem ;
	protected GSupplyLinkSolution bestSolution ;
	
	private long startTime=0;
	private long endTime=0;
	private long elapsedTime=0;
	
	/**
	 * maximum time to run the solver
	 */
	protected long solvingTime=60000;
	
	/**
	 * random generator to use for stochastic techniques
	 */
	protected Random rand ;
	
	static public GLog log = new GLog("gsolver") ;
 

	/**
	 * Constructor
	 */
	public GSolver() {
		rand = new Random() ;
	}

	/**
	 * Constructor
	 * @param problem
	 */
	public GSolver(GSupplyLinkProblem problem) {
		this.problem = problem ;
		rand = new Random() ;
	}

	/**
	 * Constructor
	 * @param problem
	 * @param bestSolution
	 */
	public GSolver(GSupplyLinkProblem problem, GSupplyLinkSolution bestSolution) {
		this.problem = problem ; this.bestSolution = bestSolution ;
		rand = new Random() ;
	}
	
	/**
	 * @return the timeElapse
	 */
	public long getElapsedTime() {
		long time = new Date().getTime() ; 
		elapsedTime = time-startTime ;
		return elapsedTime;
	}

	/**
	 * @return the solvingTime
	 */
	public long getSolvingTime() {
		return solvingTime;
	}

	/**
	 * @param solvingTime the solvingTime to set
	 */
	public void setSolvingTime(long solvingTime) {
		this.solvingTime = solvingTime;
	}

	/**
	 * @return the problem
	 */
	public GSupplyLinkProblem getProblem() {
		return problem;
	}


	/**
	 * @param problem the problem to set
	 */
	public void setProblem(GSupplyLinkProblem problem) {
		this.problem = problem;
	}


	/**
	 * @return the bestSolution
	 */
	public GSupplyLinkSolution getBestSolution() {
		return bestSolution;
	}


	/**
	 * @param bestSolution the bestSolution to set
	 */
	public void setBestSolution(GSupplyLinkSolution bestSolution) {
		this.bestSolution = bestSolution;
	}


	/**
	 * @return the rand
	 */
	public Random getRand() {
		return rand;
	}


	/**
	 * @param rand the rand to set
	 */
	public void setRand(Random rand) {
		this.rand = rand;
	}
	
	/**
	 * Stops the solver after a given duration
	 * 
	 * @author ogrunder
	 *
	 */
	class GStopSolver extends Thread {
		public void run() {
			boolean encore=true ;
			while (encore) {
				long elapsedTime = getElapsedTime() ;
				if (elapsedTime>=solvingTime) 
					encore = false ;
				else {
					try {
						Thread.currentThread().sleep(1000) ;
					} catch (Exception e) {}						
				}				
			}
			stopSolver() ;
		}
	}

	@Override
	public void run() {
		startTime = new Date().getTime() ; 
		new GStopSolver().start() ;
		solve() ;
	}

	/**
	 * solves the problem
	 */
	protected void solve() {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * stops the solver
	 */
	protected void stopSolver() {
		this.stop();
		log.println ("END OF THE SOLVING TIME : " + getElapsedTimeString()) ;
		log.println ("bestSolution = "+bestSolution);
	}
	
	/**
	 * Get the elapsed time in the format hr min sec 
	 * @return the 
	 */
	public String getElapsedTimeString() {
		return getTimeString(getElapsedTime()) ;
	  }
	
	/**
	 * Get a string from a duration in milliseconds
	 * 
	 * @param ms
	 * @return string
	 */
	public String getTimeString(long ms) {
	    long sec  = ms / 1000 ;
	    ms -= sec * 1000 ;
	  
	    if (sec==0) return new String (ms+"ms") ;
	  
	    long min =  sec / 60 ;
	    sec -= min * 60 ;
	    if (min==0) return new String (sec + "s " + ms+"ms") ;
	  
	    long heure = min / 60 ;
	    min -= heure * 60 ;
	    if (heure==0) return new String (min + "m " + sec + "s") ;
	
	    long jour = heure / 24 ;
	    heure -= jour * 24 ;
	    if (jour==0) return new String (heure + "h " + min + "m" ) ;
	    
	    return new String (jour + "d " + heure + "h" ) ;
	}



}
