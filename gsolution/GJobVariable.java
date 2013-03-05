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


/**
 * Variables for each job
 * 
 * @author Olivier Grunder
 * @version 0.02
 * @date 13 avril 2011
 *
 */
public class GJobVariable {
	/**
	 * Batch indice of the job
	 */
	private int batchIndice ; 
	
	/**
	 * Completion (or Delivery) time of the job
	 */
	private double deliveryCompletionTime ;
	
	/**
	 * @param batchNumber
	 * @param deliveryCompletionTime
	 * @param productionStartingDate
	 * @param productionCompletionTime
	 * @param transportationCompletionTime
	 */
	public GJobVariable(int batchNumber,
			double deliveryCompletionTime, double productionStartingDate,
			double productionCompletionTime, double transportationCompletionTime) {
		this.batchIndice = batchNumber;
		this.deliveryCompletionTime = deliveryCompletionTime;
	}

	public GJobVariable() {	}

	/**
	 * @return the batchNumber
	 */
	public int getBatchNumber() {
		return batchIndice;
	}

	/**
	 * @param batchNumber the batchNumber to set
	 */
	public void setBatchIndice(int batchNumber) {
		this.batchIndice = batchNumber;
	}

	/**
	 * @return the deliveryCompletionTime
	 */
	public double getDeliveryCompletionTime() {
		return deliveryCompletionTime;
	}

	/**
	 * @param deliveryCompletionTime the deliveryCompletionTime to set
	 */
	public void setDeliveryCompletionTime(double deliveryCompletionTime) {
		this.deliveryCompletionTime = deliveryCompletionTime;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "GJobVariables [batchNumber=" + batchIndice
				+ ", deliveryCompletionTime=" + deliveryCompletionTime + "]";
	}
	
	
}
