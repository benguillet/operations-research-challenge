package gio;
// Package //-------------------------------------------------------------------
// Importation //---------------------------------------------------------------
import java.io.*;

// Classe  F i c h i e r S o r t i e //-----------------------------------------
public class FichierSortie {
	 //--------------------------------------------------------------------Attributs
	 protected BufferedWriter fichier;

	 public FichierSortie() {}
	 
	 @SuppressWarnings("deprecation")
	public static String getDateHourFilename() {
		java.util.Date d = new java.util.Date() ;
		
		String st = "" + (1900+d.getYear()) +"." ;
		if (d.getMonth()+1<10) 
			st = st + "0" ;
		st = st + (1+d.getMonth())+"." ;
		if (d.getDate()<10) 
			st = st + "0" ;
		st = st + d.getDate() ;

		st = st + "-" ;

		if (d.getHours()<10) 
			st = st + "0" ;
		st = st + d.getHours()+"." ;
		if (d.getMinutes()<10) 
			st = st + "0" ;
		
		st = st + d.getMinutes() + d.getSeconds()  ;
		
		return st ;
	 }
	 
	 //-----------------------------------------------------------------Constructeur
	 // Ouvre un fichier en mode ecriture.
	 public FichierSortie(String nom) {
		  try { fichier = new BufferedWriter(new FileWriter(nom)); }
		
		  catch(Exception exception) {
		   System.err.println("Erreur: Impossible d'ouvrir '"+nom+"'.");
		   System.exit(1);
		  }
	 }
	 public FichierSortie(String nom, boolean append) {
		  try { fichier = new BufferedWriter(new FileWriter(nom, append)); }
		
		  catch(Exception exception) {
		   System.err.println("Erreur: Impossible d'ouvrir '"+nom+"'.");
		   System.exit(1);
		  }
	 }
	 //-----------------------------------------------------------------------Fermer
	 // Ferme le fichier.
	 public void fermer() {
		  try { fichier.close(); }
		  catch(Exception exception) {}
	 }
	 //--------------------------------------------------------------Ecrire (Chaine)
	 // Ecrit la chaine dans le fichier.
	 public void ecrire(String chaine) {
		  try { fichier.write(chaine); }
		
		  catch(Exception exception) {
		   System.err.println("Erreur: Imposible d'ecrire la chaine dans le fichier.");
		   System.exit(1);
		  }
	 }      
	 //--------------------------------------------------------------Ecrire (Entier)
	 // Ecrit un entier dans le fichier.
	 public void ecrire(int entier) {
		  try { fichier.write(""+entier); }
		  
		  catch(Exception exception) {
		   System.err.println("Erreur: Imposible d'ecrire l'entier dans le fichier.");
		   System.exit(1);
		  }   
	 }
	 //----------------------------------------------------------------Ecrire (Reel)
	 // Ecrit un reel dans le fichier.
	 public void ecrire(double nombre) {
		  try { fichier.write(""+nombre); }
		   catch(Exception exception) {
		   System.err.println("Erreur: Imposible d'ecrire le reel dans le fichier.");
		   System.exit(1);
		  }
	 }
}
// Fin //-----------------------------------------------------------------------


