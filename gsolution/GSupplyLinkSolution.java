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
package gsolution;

import gio.FichierEntree;
import gproblem.GJobData;
import gproblem.GSupplyLinkProblem;
import gproblem.GSupplyLinkTransporter;

import java.util.*;

/**
 * A Solution for the optimisation problem
 * 
 * @author Olivier Grunder
 * @version 0.02
 * @date 13 avril 2011
 *
 */
public class GSupplyLinkSolution {

	GSupplyLinkProblem supplyLinkProblem;

	/*
	 * Structure of a feasible schedule ;
	 */
	private GJobVariable[] processingSchedule;
	

	/*
	 * Number of batches
	 */
	int nbrBatch;

	private double evaluation;
	private double evaluationTransporter;
	private double evaluationCustomer;

	public static final double MOINS_INFINI = -1000000.0;
	public static final double PLUS_INFINI  = 1000000.0;

	/**
	 * To print debug information for the checkConstraints method 
	 */
	private boolean debugConstraints; 

	/*
	 * 
	 */
	public GSupplyLinkSolution() {
		setSupplyLinkProblem(null) ;
	}

	public GSupplyLinkSolution(GSupplyLinkProblem problem) {
		setSupplyLinkProblem(problem) ;
	}

	public GSupplyLinkSolution(GSupplyLinkProblem pb, String string) {
		setSupplyLinkProblem(pb) ;
		open (string) ;
	}

	/**
	 * @param slpb
	 * @param processingSchedule
	 * @param nbrBatch
	 * @param makespanForProduction
	 * @param makespanForTransportation
	 * @param solValueOfTransportationProblem
	 * @param evaluationManufacturer
	 * @param evaluationTransporter
	 * @param evaluationCustomer
	 * @param dateTransporterDeparture
	 * @param dateProductionDeparture
	 */
	public GSupplyLinkSolution(GSupplyLinkProblem supplyLinkProblem,
			GJobVariable[] processingSchedule, int nbrBatch,
			double evaluationManufacturer, double evaluationTransporter,
			double evaluationCustomer) {
		this.supplyLinkProblem = supplyLinkProblem;
		this.processingSchedule = processingSchedule;
		this.nbrBatch = nbrBatch;
		this.evaluation = evaluationManufacturer;
		this.evaluationTransporter = evaluationTransporter;
		this.evaluationCustomer = evaluationCustomer;
	}

	public boolean isDebugConstraints() {
		return debugConstraints;
	}

	public void setDebugConstraints(boolean debugConstraints) {
		this.debugConstraints = debugConstraints;
	}

	/**
	 * @return the slpb
	 */
	public GSupplyLinkProblem getSupplyLinkProblem() {
		return supplyLinkProblem;
	}

	/**
	 * @param slpb
	 *            the slpb to set
	 */
	public void setSupplyLinkProblem(GSupplyLinkProblem slpb) {
		this.supplyLinkProblem = slpb;
		if (this.supplyLinkProblem.getN()>0) {
			processingSchedule = new GJobVariable[this.supplyLinkProblem.getN()] ;
			for (int i=0;i<this.supplyLinkProblem.getN();++i) 
				processingSchedule[i] = new GJobVariable();
		}
	}

	/**
	 * @return the processingSchedule
	 */
	public GJobVariable[] getProcessingSchedule() {
		return processingSchedule;
	}

	/**
	 * @param processingSchedule
	 *            the processingSchedule to set
	 */
	public void setProcessingSchedule(GJobVariable[] processingSchedule) {
		this.processingSchedule = processingSchedule;
	}

	/**
	 * @return the nbrBatch
	 */
	public int getNbrBatch() {
		return nbrBatch;
	}

	/**
	 * @param nbrBatch
	 *            the nbrBatch to set
	 */
	public void setNbrBatch(int nbrBatch) {
		this.nbrBatch = nbrBatch;
	}

	/**
	 * @return the evaluationManufacturer
	 */
	public double getEvaluation() {
		return evaluation;
	}

	/**
	 * @param evaluationManufacturer
	 *            the evaluationManufacturer to set
	 */
	public void setEvaluation(double evaluation) {
		this.evaluation = evaluation;
	}

	/**
	 * @return the evaluationTransporter
	 */
	public double getEvaluationTransporter() {
		return evaluationTransporter;
	}

	/**
	 * @param evaluationTransporter
	 *            the evaluationTransporter to set
	 */
	public void setEvaluationTransporter(double evaluationTransporter) {
		this.evaluationTransporter = evaluationTransporter;
	}

	/**
	 * @return the evaluationCustomer
	 */
	public double getEvaluationCustomer() {
		return evaluationCustomer;
	}

	/**
	 * @param evaluationCustomer
	 *            the evaluationCustomer to set
	 */
	public void setEvaluationCustomer(double evaluationCustomer) {
		this.evaluationCustomer = evaluationCustomer;
	}

	/**
	 * @return the moinsInfini
	 */
	public static double getMoinsInfini() {
		return MOINS_INFINI;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		String st = new String() ;
		st = st + "[[nbrBatch="+nbrBatch+", " + "evaluation="+evaluation+", evaluationCustomer=" + evaluationCustomer				
				+ ", evaluationTransporter=" + evaluationTransporter  ;
		st = st + "\n  bi=[" ;
		for (int i=0;i<supplyLinkProblem.getN();i++) {
			st = st + processingSchedule[i].getBatchNumber() ;
			if (i<supplyLinkProblem.getN()-1) st = st + ", "; else st = st + "]" ;
		}
		st = st + "\n  Ci=[" ;
		for (int i=0;i<supplyLinkProblem.getN();i++) {
			st = st + processingSchedule[i].getDeliveryCompletionTime() ;
			if (i<supplyLinkProblem.getN()-1) st = st + ", "; else st = st + "]" ;
		}
		st = st + "]" ;
		
		return st;
	}

	@Override
	public GSupplyLinkSolution clone() {
		return new GSupplyLinkSolution(supplyLinkProblem, processingSchedule,
				nbrBatch, evaluation, evaluationTransporter, evaluationCustomer);
	}

	public void setBatchIndice(int j, int bi) {
		processingSchedule[j].setBatchIndice(bi);
	}

	public void setCompletionTimes(int j, double c) {
		processingSchedule[j].setDeliveryCompletionTime(c);
	}

	public int open(String filename) {
		if (filename == null)
			return 1;

		FichierEntree fe = new FichierEntree(filename);

		int encore = 1;
		while (encore == 1) {

			String s = fe.lireLigne();
			if (s == null) {
				break;
			}

			StringTokenizer st = new StringTokenizer(s, new String(":="));
			String sg = st.nextToken().trim();

			if (sg.compareToIgnoreCase("NBR_BATCH") == 0)
				setNbrBatch(Integer.parseInt(st.nextToken().trim()));

			if (sg.compareToIgnoreCase("BATCH_INDICES") == 0) {
				StringTokenizer st2 = new StringTokenizer(st.nextToken().trim(), new String(";"));
				for (int j = 0; j < supplyLinkProblem.getN(); j++) {
					setBatchIndice(j, Integer.parseInt(st2.nextToken().trim()));
				}
			}

			if (sg.compareToIgnoreCase("COMPLETION_TIMES") == 0) {
				StringTokenizer st2 = new StringTokenizer(st.nextToken().trim(), new String(";"));
				for (int j = 0; j < supplyLinkProblem.getN(); j++) {
					setCompletionTimes(j, (new Double(st2.nextToken())).doubleValue() );
				}
			}

			if (sg.compareToIgnoreCase("EOF") == 0) {
				encore = 0;
			}

		}

		fe.fermer();

		return 0;

	}

	/**
	 * evaluates the solution
	 * 
	 * @return evaluation
	 */
	public double evaluate() {
		evaluation = -1 ;
		if (!checkConstraints()) return -1 ;
		evaluationTransporter = nbrBatch * supplyLinkProblem.getTransporter(0).getDeliveryCost() ;
		evaluationCustomer = 0 ;
		for (int i=0;i<supplyLinkProblem.getN();++i) {
			double jobcustcost = supplyLinkProblem.getJobData()[i].getHoldingCost() ;
			double jobduedate = supplyLinkProblem.getJobData()[i].getDueDate() ;
			double jobcomptime =  processingSchedule[i].getDeliveryCompletionTime() ;
			evaluationCustomer += jobcustcost*(jobduedate-jobcomptime) ;
		}
		evaluation = evaluationTransporter + evaluationCustomer ;
		return evaluation;
	}

    public void call() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

	/**
	 * Batch Information collected to check the constraints
	 * 
	 * @author ogrunder
	 *
	 */
	class BatchInformation {
		private double size ;
		private double completionTime ;
		
		/**
		 * 
		 */
		public BatchInformation() {
			size = 0 ; completionTime = PLUS_INFINI ;
		}
	}
	
	BatchInformation[] tabBatchInformation ;

	/**
	 * checks if the solution satisfies the constraints
	 * 
	 * @return true if the constraints are satisfied
	 */
	private boolean checkConstraints() {
		if (getNbrBatch()<=0) {
			if (debugConstraints) System.out.println("constraints not satisfied: getNbrBatch()<=0") ;
			return false ;
		}
		
		tabBatchInformation = new BatchInformation[getNbrBatch()] ;
		for (int b=0;b<getNbrBatch();++b) tabBatchInformation[b] = new BatchInformation() ;
		
		for (int i=0;i<supplyLinkProblem.getN();++i) {
			int batchIndice = processingSchedule[i].getBatchNumber() - 1 ;
			
			if (batchIndice<0 || batchIndice>=getNbrBatch()) {
				if (debugConstraints) System.out.println("constraints not satisfied: batchIndice<0 || batchIndice>=getNbrBatch()") ;
				return false ;
			}
			
			// Constraint (3) : Add the size of job i to the batch where job is is assigned
			tabBatchInformation[batchIndice].size += supplyLinkProblem.getJobData()[i].getSize() ;
			
			// Constraint (2) : Update the completion time of the batch if the due date of the job is less 
			if (tabBatchInformation[batchIndice].completionTime > supplyLinkProblem.getJobData()[i].getDueDate()) {
				tabBatchInformation[batchIndice].completionTime = supplyLinkProblem.getJobData()[i].getDueDate() ;
			}
		}
		
		double lastDeliveryDate = PLUS_INFINI ;
		for (int b=getNbrBatch()-1;b>=0;--b) {
			// Constraint (3) : capacity constraint check
			if (tabBatchInformation[b].size>supplyLinkProblem.getTransporter(0).getCapacity()){
				if (debugConstraints) System.out.println("constraints not satisfied: transporter capacity exceeded") ;
				return false ;
			}
						
			// Constraint (5) : the interval between two successive delivery should be greater or equal to the delivery time 
			if (tabBatchInformation[b].completionTime > lastDeliveryDate-supplyLinkProblem.getTransporter(0).getDeliveryTime()) {
				tabBatchInformation[b].completionTime = lastDeliveryDate-supplyLinkProblem.getTransporter(0).getDeliveryTime() ;
			}
			lastDeliveryDate = tabBatchInformation[b].completionTime ;
			
		}

		// Constraint (4) : check if all th jobs in a batch have the same completion time as the batch
		for (int i=0;i<supplyLinkProblem.getN();++i) {
			int batchnumber = processingSchedule[i].getBatchNumber()-1 ;
			
			// Constraint (2) : Update the completion time of the batch if the due date of the job is less 
			if (tabBatchInformation[batchnumber].completionTime != processingSchedule[i].getDeliveryCompletionTime()) {
				processingSchedule[i].setDeliveryCompletionTime (tabBatchInformation[batchnumber].completionTime) ;
			}
		}
		
		return true;
	}

}