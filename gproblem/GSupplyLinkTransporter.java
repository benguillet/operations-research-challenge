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

import java.util.Random;



/**
 * 
 * Transporter 
 * 
 * @author Olivier Grunder
 * @version 0.02
 * @date 13 avril 2011
 *
 */
public class GSupplyLinkTransporter {

	/**
	 * transporter capacity
	 */
	private double capacity ;

	/**
	 * delivery cost
	 */
	private double deliveryCost ;

	/**
	 * delivery time
	 */
	private double deliveryTime ;

	/**
	 * @param capacity
	 * @param deliveryCost
	 * @param deliveryTime
	 */
	public GSupplyLinkTransporter(double capacity, double deliveryCost,
			double deliveryTime) {
		this.capacity = capacity;
		this.deliveryCost = deliveryCost;
		this.deliveryTime = deliveryTime;
	}

	public GSupplyLinkTransporter() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @return the capacity
	 */
	public double getCapacity() {
		return capacity;
	}

	/**
	 * @param capacity the capacity to set
	 */
	public void setCapacity(double capacity) {
		this.capacity = capacity;
	}

	/**
	 * @return the deliveryCost
	 */
	public double getDeliveryCost() {
		return deliveryCost;
	}

	/**
	 * @param deliveryCost the deliveryCost to set
	 */
	public void setDeliveryCost(double deliveryCost) {
		this.deliveryCost = deliveryCost;
	}

	/**
	 * @return the deliveryTime
	 */
	public double getDeliveryTime() {
		return deliveryTime;
	}

	/**
	 * @param deliveryTime the deliveryTime to set
	 */
	public void setDeliveryTime(double deliveryTime) {
		this.deliveryTime = deliveryTime;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "GSupplyLinkTransporter [capacity=" + capacity
				+ ", deliveryCost=" + deliveryCost + ", deliveryTime="
				+ deliveryTime + "]";
	}

}
