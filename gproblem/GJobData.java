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
package gproblem;

/**
 * Given data for each job
 * 
 * @author Olivier Grunder
 * @version 0.02
 * @date 13 avril 2011
 *
 */
public class GJobData {
	private double size ;
	private double dueDate ;
	private double holdingCost ;
	
	public GJobData() { 	}
	
	/**
	 * @param size
	 * @param dueDate
	 * @param holdingCost
	 */
	public GJobData(double size, double dueDate, double holdingCost) {
		this.size = size;
		this.dueDate = dueDate;
		this.holdingCost = holdingCost;
	}

	public GJobData(GJobData gJobData) {
		size = gJobData.size ;
		dueDate = gJobData.dueDate ;
		holdingCost = gJobData.holdingCost ;
	}

	/**
	 * @return the size
	 */
	public double getSize() {
		return size;
	}

	/**
	 * @param size the size to set
	 */
	public void setSize(double size) {
		this.size = size;
	}

	/**
	 * @return the dueDate
	 */
	public double getDueDate() {
		return dueDate;
	}

	/**
	 * @param dueDate the dueDate to set
	 */
	public void setDueDate(double dueDate) {
		this.dueDate = dueDate;
	}

	/**
	 * @return the holdingCost
	 */
	public double getHoldingCost() {
		return holdingCost;
	}

	/**
	 * @param holdingCost the holdingCost to set
	 */
	public void setHoldingCost(double holdingCost) {
		this.holdingCost = holdingCost;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "GJobData [dueDate=" + dueDate + ", holdingCost=" + holdingCost
				+ ", size=" + size + "]";
	}

	
}
