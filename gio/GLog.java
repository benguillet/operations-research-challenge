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
package gio;


/**
 * @author Olivier Grunder
 * @version 0.02
 *
 * log
 */
public class GLog extends FichierSortie {
	protected String defaultLogFilename = null ;
	
	protected FichierSortie logfile = null ;
	
	public GLog (String filename) {
		defaultLogFilename = new String ("log/") ;
		defaultLogFilename = defaultLogFilename + filename + ".log" ;
		
		FichierSortie fs = new FichierSortie(defaultLogFilename) ;
		fs.ecrire("Log:\n\n") ;
		fs.fermer() ;
	}
	
	public void append(String st) {
		logfile = new FichierSortie(defaultLogFilename, true) ;
		logfile.ecrire(st) ;
		
		logfile.fermer() ;
	}
	
	public void print(String st) {
		System.out.print (st) ;
		append(st) ;}
	public void println(String st) {
		System.out.println (st) ;
		append(st+"\n") ;
	}

}
 