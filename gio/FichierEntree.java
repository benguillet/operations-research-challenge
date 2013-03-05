package gio;
// Importation //---------------------------------------------------------------
import java.io.*;

// Classe  F i c h i e r E n t r e e //-----------------------------------------
public class FichierEntree {
 //--------------------------------------------------------------------Attributs
 private BufferedReader  fichier;
 private StreamTokenizer flux;
 //-----------------------------------------------------------------Constructeur
 // Ouvre un fichier en lecture.
 public FichierEntree(String nom) {
  try {
   fichier = new BufferedReader(new FileReader(nom));
   flux = new StreamTokenizer(fichier);
   flux.wordChars(33,65535);
   flux.parseNumbers() ;
  }
   catch(Exception exception) {
   System.err.println("Erreur: Impossible d'ouvrir '"+nom+"'.");
//   System.exit(1);
  }
 }
 //-----------------------------------------------------------------------Fermer
 // Ferme le fichier.
 public void fermer() {
  try {
   flux = null;
   fichier.close();
  }
 
  catch(Exception exception) {}
 }
 //-------------------------------------------------------------------LireChaine
 // Lit la chaine suivante dans le fichier.
 public String lireChaine() {
  try { if (flux.nextToken()==StreamTokenizer.TT_WORD) return (flux.sval); }
  
  catch(Exception exception) {
   System.err.println("Erreur: Impossible de lire une chaine dans le fichier.");
   System.exit(1);
  }
  
  return (new String()); 
 }      
 //--------------------------------------------------------------------LireLigne
 // Lit la ligne suivante dans le fichier.
 public String lireLigne() {
  String ligne;
  
  try {
   do
    { ligne = fichier.readLine(); }
   while (ligne.equals(""));
 
   return ligne;
  }
  catch(Exception exception) {}
  
  return (null);
 }      
 //-------------------------------------------------------------------LireEntier
 // Lit l'entier suivant dans le fichier.
 public int lireEntier() {
  try {
   if (flux.nextToken()==StreamTokenizer.TT_NUMBER)
    return ((int)flux.nval);
  }
  
  catch(Exception exception) {
   System.err.println("Erreur: Impossible de lire un entier dans le fichier.");
   System.exit(1);
  }

  return 0;  
 }
 //---------------------------------------------------------------------LireReel
 // Lit le reel suivant dans le fichier.
 public double lireReel() {
  try { if (flux.nextToken()==StreamTokenizer.TT_NUMBER) return (flux.nval); }
  
  catch(Exception exception) {
   System.err.println("Erreur: Impossible de lire un reel dans le fichier.");
   System.exit(1);
  }

  return 0.0;
 }
}
// Fin //-----------------------------------------------------------------------


