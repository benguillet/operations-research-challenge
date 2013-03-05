
import gproblem.GSupplyLinkProblem;

/**
 * Implémentation en Java de la méthode Tabou
 * @author Benjamin GUILLET <benjamin.guillet@utbm.fr>
 * @author Ronan FONTENAY <ronan.fontenay@utbm.fr>
 * @author Samuel LE GUELVOUIT <samuel.le-guelvouit@utbm.fr>
 */
public final class MethodeTabou implements Runnable {

    private static final Object _lock = new Object();
    private Solution tbSol, solutionRepereCycle, lastBestSolution;
    private int jobsNb = 0;
    private static final int minTabu = 1;
    private int tabuList[][][];
    private int tabuDelay = MethodeTabou.minTabu;
    private boolean stop = false;
    private MySolver solver;
    private static final int changeLimit = 50;
    private int timeWithoutChange = 0;

    public MethodeTabou(Solution sol, GSupplyLinkProblem pb, MySolver solver) {
        tbSol = sol;
        jobsNb = pb.getN();
        this.tabuList = new int[jobsNb][jobsNb][jobsNb];
        this.solver = solver;
        solutionRepereCycle = new Solution(pb);
    }

    public void stop() {
        this.stop = true;
    }

    @Override
    public final void run() {
        int currentIter = 0;
        this.lastBestSolution = this.tbSol.clone();

        while (!stop) {
            if (!FindNeighbor(currentIter)) {
                System.out.println("Pas de meilleur voisin trouvé !");
                break;
            }
            ++this.timeWithoutChange;

            // Maj de la best interne pour les cycles
            if (this.tbSol.isBetter(this.solutionRepereCycle)) {
                this.solutionRepereCycle.cloneLight(this.tbSol);
                //this.solutionRepereCycle = this.tbSol.clone();
                this.timeWithoutChange = 0;
                this.tabuDelay = MethodeTabou.minTabu;
            } // Cycle spotted
            else if (this.tbSol.equals(this.solutionRepereCycle)) {
                //System.out.println("cycle");
                this.solutionRepereCycle.cloneLight(this.tbSol);
                //this.solutionRepereCycle = this.tbSol.clone();
                this.timeWithoutChange = 0;
                ++this.tabuDelay;
            }

            synchronized (MethodeTabou._lock) {
                if (this.tbSol.isBetter(this.solver.getBest())) {
                    this.solver.setBest(tbSol);
                    this.solutionRepereCycle.cloneLight(this.tbSol);
                    //this.solutionRepereCycle = this.tbSol.clone();
                    this.timeWithoutChange = 0;
                    //System.out.println("iteration " + currentIter + " eval " + this.tbSol.getEvaluation() + " contraintes " + this.tbSol.getAllConstraints());
                }
            }
     
            if (this.timeWithoutChange >= MethodeTabou.changeLimit) {
                this.timeWithoutChange = 0;
                this.solutionRepereCycle.cloneLight(this.tbSol);
                //this.solutionRepereCycle = this.tbSol.clone();
            }
            ++currentIter;
        }
    }

    public final boolean FindNeighbor(int currentIter) {

        int indA = -1;
        int indB = -1;
        double evalBase = Double.MAX_VALUE;
        int constBase = Integer.MAX_VALUE;
        int nbBatch = this.tbSol.getNbrBatch();
        int batchCurrentJob = 0, bestBatchCurrentJob = 0;
        boolean batchDeleted = false;

        // Pour chaque job
        for (int i = 0; i < jobsNb; ++i) {

            // Récupérer son batch actuel, et décrémenter la taille de ce batch
            batchCurrentJob = this.tbSol.getProcessingSchedule()[i].getBatchNumber();
            --this.tbSol.getBatchSize()[batchCurrentJob];

            // Si la taille du batch est nulle on le supprime;
            // tous les jobs ayant un n° de batch suppérieur a celui supprimé sont décrémentés
            // et les tailles de batchs sont décalées;
            if (this.tbSol.getBatchSize()[batchCurrentJob] == 0) {
                for (int j = 0; j < this.jobsNb; ++j) {
                    if (this.tbSol.getProcessingSchedule()[j].getBatchNumber() > batchCurrentJob) {
                        this.tbSol.getProcessingSchedule()[j].setBatchIndice(this.tbSol.getProcessingSchedule()[j].getBatchNumber() - 1);
                    }
                }
                for (int k = batchCurrentJob; k < nbBatch - 1; ++k) { 
                    this.tbSol.getBatchSize()[k] = this.tbSol.getBatchSize()[k + 1];
                }
                this.tbSol.setNbrBatch(nbBatch - 1);
                batchDeleted = true;
            } else {
                batchDeleted = false;
            }

            // maxBatch est la valeur max du batch a tester pour le job courant
            int maxBatch = nbBatch;

            // Batch supprimé, on n'autorise pas la création d'un nouveau batch
            if (batchDeleted) {
                --maxBatch;
            } // Limite de batch non atteinte, on tente d'en créer un
            else if (nbBatch != this.jobsNb) {
                ++maxBatch;
            }

            // Pour chaque batch différent possible pour le job courant
            for (int j = 0; j < maxBatch; ++j) {
                // On change le batch du job si c'est pas tabou
                if (batchCurrentJob != j && tabuList[i][batchCurrentJob][j] <= currentIter) {
                    // Modif du batch
                    this.tbSol.getProcessingSchedule()[i].setBatchIndice(j);

                    // Maj du nombre de batch si besoin (création d'un batch)
                    if (j == nbBatch) {
                        this.tbSol.setNbrBatch(maxBatch);
                    }

                    // Tests sur "this.tbSol" modifiée
                    // Si la nouvelle solution brise moins de contraintes...
                    if (this.tbSol.check() <= constBase) {
                        // La nouvelle solution est meilleure si elle viole moins de contraintes (quelques soit son eval)
                        // Ou si elle a une meilleure eval pour le meme nombre de contraintes violées.
                        this.tbSol.evaluate();
                        if ((this.tbSol.getAllConstraints() < constBase)
                                || (this.tbSol.getEvaluation() <= evalBase && this.tbSol.getAllConstraints() == constBase)) {
                            //la nouvelle this.tbSol est meilleur que notre meilleur voisin..
                            indA = i;
                            indB = j;
                            evalBase = this.tbSol.getEvaluation();
                            constBase = this.tbSol.getAllConstraints();
                            bestBatchCurrentJob = batchCurrentJob;
                        }
                    }
                    // On réinit le bon nombre de batchs de la solution
                    this.tbSol.setNbrBatch(nbBatch);
                }
            }

            // On réinit le bon nombre de batchs
            this.tbSol.setNbrBatch(nbBatch);

            // Si un batch a été supprimé, on le recrée
            // Décalage dans l'autre sens etc..
            if (batchDeleted) {
                for (int k = 0; k < this.jobsNb; ++k) {
                    if (this.tbSol.getProcessingSchedule()[k].getBatchNumber() >= batchCurrentJob) {
                        this.tbSol.getProcessingSchedule()[k].setBatchIndice(this.tbSol.getProcessingSchedule()[k].getBatchNumber() + 1);
                    }
                }
                for (int k = nbBatch - 1; k > batchCurrentJob; --k) {
                    this.tbSol.getBatchSize()[k] = this.tbSol.getBatchSize()[k - 1];
                }
                this.tbSol.getBatchSize()[batchCurrentJob] = 0;
            }

            // On lui redonne sa valeur d'origine
            ++this.tbSol.getBatchSize()[batchCurrentJob];
            this.tbSol.getProcessingSchedule()[i].setBatchIndice(batchCurrentJob);
        }

        // Si aucun voisin n'existe (taille tabou trop grande ?)
        if (indA == -1 && indB == -1) {
            System.out.println("false");
            return false;
        } else {
            // On inverse pour retourner le meilleur voisin
            // Maj de la taille des batchs aussi
            this.tbSol.getProcessingSchedule()[indA].setBatchIndice(indB);
            if (--this.tbSol.getBatchSize()[bestBatchCurrentJob] == 0) {
                for (int j = 0; j < this.jobsNb; ++j) {
                    if (this.tbSol.getProcessingSchedule()[j].getBatchNumber() > bestBatchCurrentJob) {
                        this.tbSol.getProcessingSchedule()[j].setBatchIndice(this.tbSol.getProcessingSchedule()[j].getBatchNumber() - 1);
                    }
                }
                for (int k = bestBatchCurrentJob; k < nbBatch - 1; ++k) {
                    this.tbSol.getBatchSize()[k] = this.tbSol.getBatchSize()[k + 1];
                }
                this.tbSol.setNbrBatch(nbBatch - 1);
            } else if (indB == nbBatch) {
                this.tbSol.setNbrBatch(nbBatch + 1);
            }

            ++this.tbSol.getBatchSize()[indB];

            // Maj de la liste tabou
            tabuList[indA][bestBatchCurrentJob][indB] = currentIter + tabuDelay + 1;
            this.tbSol.check();
            this.tbSol.evaluate();
            return true;
        }
    }
}
