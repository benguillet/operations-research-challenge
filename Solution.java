
import gproblem.GSupplyLinkProblem;
import gsolution.GSupplyLinkSolution;
import gproblem.GJobData;
import gsolution.GJobVariable;

public final class Solution extends GSupplyLinkSolution implements Cloneable {

    private double[] volumes;
    private double[] deliveryDate;
    private double eval;
    private int badConstraints;
    private int badTimeConstraints;
    private int[] batchSize = null;

    public Solution(GSupplyLinkProblem problem) {
        super(problem);
        int nbJobs = problem.getN();
        this.volumes = new double[nbJobs];
        this.deliveryDate = new double[nbJobs];
        this.batchSize = new int[nbJobs];
    }

    public Solution(GSupplyLinkProblem problem, GJobVariable[] processingSchedule, int nbrBatch, double evaluation, int nbBadConstraints, double evaluationTransporter, double evaluationCustomer) {
        super(problem);
//		this.batchSize = new int[problem.getN()];
        this.setProcessingSchedule(processingSchedule);
        this.setNbrBatch(nbrBatch);
        this.eval = evaluation;
        this.setEvaluation(eval);
        this.setEvaluationCustomer(evaluationCustomer);
        this.setEvaluationTransporter(evaluationTransporter);
        int nbJobs = problem.getN();
        this.volumes = new double[nbJobs];
        this.deliveryDate = new double[nbJobs];
        this.check();
        this.evaluate();
    }

    public void affSizeBatch() {
        for (int i = 0; i < this.getNbrBatch(); ++i) {
            System.out.print(this.batchSize[i] + " ");
        }
        System.out.println();
    }

    @Override
    public final double evaluate() {
        int nbJobs = this.getSupplyLinkProblem().getN();
        this.build();

        this.eval = this.getNbrBatch() * this.getSupplyLinkProblem().getTransporter(0).getDeliveryCost();
        this.setEvaluationTransporter(this.eval);
        double evalCustomer = 0.d;
        for (int i = 0; i < nbJobs; ++i) {
            double val = this.getSupplyLinkProblem().getJobData()[i].getHoldingCost() * (this.getSupplyLinkProblem().getJobData()[i].getDueDate() - this.getProcessingSchedule()[i].getDeliveryCompletionTime());
            evalCustomer += (val >= 0 ? val : -val);
        }
        this.setEvaluationCustomer(evalCustomer);
        this.eval += evalCustomer;
        this.setEvaluation(this.eval);
        return this.eval;
    }

    public final int check() {
        //this.reInitCheckConstraints();
        double maxVolume = this.getSupplyLinkProblem().getTransporter(0).getCapacity();
        int nbBatch = this.getNbrBatch();
        int nbJobs = this.getSupplyLinkProblem().getN();
        GJobData[] jobsData = this.getSupplyLinkProblem().getJobData();

        for (int i = 0; i < nbBatch; ++i) {
            this.volumes[i] = 0.d;
        }

        this.badConstraints = 0;
        for (int i = 0; i < nbJobs; ++i) {
            this.volumes[this.getProcessingSchedule()[i].getBatchNumber()] += jobsData[i].getSize();
            if (this.volumes[this.getProcessingSchedule()[i].getBatchNumber()] > maxVolume) {
                ++this.badConstraints;
            }
        }

        return this.badConstraints;
    }
    
    private final void build() {
        int nbJobs = this.getSupplyLinkProblem().getN();
        int nbBatch = this.getNbrBatch();
        double deliveryTime = this.getSupplyLinkProblem().getTransporter(0).getDeliveryTime();

        this.badTimeConstraints = 0;
        for (int i = 0; i < nbBatch; ++i) {
            this.deliveryDate[i] = Double.POSITIVE_INFINITY;
        }

        GJobData[] jobsData = this.getSupplyLinkProblem().getJobData();

        for (int i = 0; i < nbJobs; ++i) {
            int batchIndice = this.getProcessingSchedule()[i].getBatchNumber();

            if (this.deliveryDate[batchIndice] > jobsData[i].getDueDate()) {
                this.deliveryDate[batchIndice] = jobsData[i].getDueDate();
            }
        }

        // Trier les dates de depart par ordre de numéro de batch
        double delDate[] = new double[nbJobs];
        boolean done[] = new boolean[nbJobs];
        double date = Double.NEGATIVE_INFINITY;
        int indDate = 0;
        double lastDate = Double.POSITIVE_INFINITY;

        for (int i = 0; i < nbBatch; ++i) {

            // Prendre la derniere date
            for (int j = 0; j < nbBatch; ++j) {
                if (!done[j] && this.deliveryDate[j] > date) {
                    date = this.deliveryDate[j];
                    indDate = j;
                }
            }

            // L'assigner au dernier batch
            done[indDate] = true;
            if (date + this.getSupplyLinkProblem().getTransporter(0).getDeliveryTime() <= lastDate) {
                delDate[indDate] = date;
                lastDate = date;
            } else {
                delDate[indDate] = lastDate - this.getSupplyLinkProblem().getTransporter(0).getDeliveryTime();
                lastDate = delDate[indDate];
            }

            if (delDate[indDate] < deliveryTime || delDate[indDate] > this.deliveryDate[indDate]) { // car c'est le temps d'arrivé ?
                if (this.batchSize != null) {
                    this.badTimeConstraints += this.batchSize[i];
                } else {
                    ++this.badTimeConstraints;
                }
            }
            date = Double.NEGATIVE_INFINITY;
        }

        for (int i = 0; i < nbJobs; ++i) {
            this.getProcessingSchedule()[i].setDeliveryCompletionTime(delDate[this.getProcessingSchedule()[i].getBatchNumber()]);
        }
    }

    @Override
    public final double getEvaluation() {
        return this.eval;
    }

    public final int getBadConstraints() {
        return this.badConstraints;
    }

    public final int getBadTimeConstraints() {
        return this.badTimeConstraints;
    }

    public final int getAllConstraints() {
        return this.badConstraints + this.badTimeConstraints;
    }

    public final boolean isBetter(Solution s) {
        /*Paramètres*/
        boolean result;
        if (s == null) {
            result = true;
        } // Les solutions sont évalués avant l'appel
        // Si une solution viole moins de contraintes, elle est meilleure, quelque soit sont eval
        else if ((s.getAllConstraints() > this.getAllConstraints())
                || (s.getEvaluation() > this.getEvaluation() && s.getAllConstraints() >= this.getAllConstraints())) {
            result = true; // ? true;
        } else {
            result = false; // ? false;
        }
        return result;
    }

    @Override
    public final Solution clone() {
        int nbJobs = this.getSupplyLinkProblem().getN();
        GJobVariable[] ps = new GJobVariable[nbJobs];
        for (int i = 0; i < nbJobs; ++i) {
            ps[i] = new GJobVariable(this.getProcessingSchedule()[i].getBatchNumber(), this.getProcessingSchedule()[i].getDeliveryCompletionTime(), 0.d, 0.d, 0.d);
        }
        return new Solution(this.getSupplyLinkProblem(), ps, this.getNbrBatch(), this.eval, this.badConstraints, this.getEvaluationTransporter(), this.getEvaluationCustomer());
    }

    public final void clone(Solution solution) {
        this.eval = solution.eval;
        this.badConstraints = solution.badConstraints;
        this.badTimeConstraints = solution.badTimeConstraints;
        this.setEvaluation(eval);
        this.setEvaluationCustomer(solution.getEvaluationCustomer());
        this.setEvaluationTransporter(solution.getEvaluationTransporter());
        int nbJobs = this.getSupplyLinkProblem().getN();
        for (int i = 0; i < nbJobs; ++i) {
            this.getProcessingSchedule()[i].setDeliveryCompletionTime(solution.getProcessingSchedule()[i].getDeliveryCompletionTime());
            this.getProcessingSchedule()[i].setBatchIndice(solution.getProcessingSchedule()[i].getBatchNumber());
        }
        this.setNbrBatch(solution.getNbrBatch());
        System.arraycopy(solution.batchSize, 0, this.batchSize, 0, this.getNbrBatch());   
    }

    public final void cloneLight(Solution solution){
       this.eval = solution.eval;
        this.badConstraints = solution.badConstraints;
        this.badTimeConstraints = solution.badTimeConstraints;
        this.setEvaluation(eval);
        this.setEvaluationCustomer(solution.getEvaluationCustomer());
        this.setEvaluationTransporter(solution.getEvaluationTransporter());
        this.setNbrBatch(solution.getNbrBatch());
    }

    /*Première fonction de génération de solution initiale*/
    public final void generateInitialSolution1() {
        int nbJobs = this.getSupplyLinkProblem().getN();
        int dueDate[] = new int[nbJobs];
        for (int i = 0; i < nbJobs; ++i) {
            dueDate[i] = i; //nbJobs-1-i;
        }

        // Tri
        // ...
        GJobData[] jobs = this.getSupplyLinkProblem().getJobData();
        GJobVariable[] batchs = this.getProcessingSchedule();
        double capa = this.getSupplyLinkProblem().getTransporter(0).getCapacity();
        double volume = 0.d;
        double lastDate = jobs[dueDate[0]].getDueDate();
        double voyage = this.getSupplyLinkProblem().getTransporter(0).getDeliveryTime();
        int nbBatch = 0;
        for (int i = 0; i < nbJobs; ++i) {

            // Si le job ne peut pas etre packé
            if ((jobs[dueDate[i]].getDueDate() >= lastDate && volume + jobs[dueDate[i]].getSize() <= capa)) {
            } else {
                volume = 0.d;
                ++nbBatch;

                // Nouvelle date
                if (lastDate - jobs[dueDate[i]].getDueDate() < voyage) {
                    lastDate -= voyage;
                } else {
                    lastDate = jobs[dueDate[i]].getDueDate();
                }
            }
            volume += jobs[dueDate[i]].getSize();
            ++this.batchSize[nbBatch];
            batchs[dueDate[i]].setBatchIndice(nbBatch);
        }
        this.setNbrBatch(nbBatch + 1);
    }

    /*Seconde fonction de génération de solution initiale*/
    public final void generateInitialSolution2() {
        int nbJobs = this.getSupplyLinkProblem().getN();
        this.setNbrBatch(1);
        //this.batchSize = new int[nbJobs];
        for (int i = 0; i < nbJobs; ++i) {
            this.getProcessingSchedule()[i].setBatchIndice(0);
        }
        this.batchSize[0] = nbJobs;
    }

    /*Troisième fonction de génération de solution initiale*/
    public final void generateInitialSolution3() {
        int nbJobs = this.getSupplyLinkProblem().getN();
        int j = 0;
        int volTmp = 0;
        double maxVolume = this.getSupplyLinkProblem().getTransporter(0).getCapacity();
        GJobData jobData[] = sortOnDueDates(this.getSupplyLinkProblem().getJobData());
        //on trie les jobs selon leur date de livraison
        for (int i = 0; i < nbJobs; ++i) {
            //Affectation a un nouveau batch si plus de place
            if ((maxVolume - volTmp) < jobData[i].getSize()) {
                ++j;
                volTmp = 0;
            }
            this.getProcessingSchedule()[i].setBatchIndice(j);
            this.batchSize[j]++;
            volTmp += jobData[i].getSize();
        }
        this.setNbrBatch(j + 1);
    }

    public final void generateInitialSolution4() {
        int nbJobs = this.getSupplyLinkProblem().getN();
        GJobData jobData[] = sortOnDueDates(this.getSupplyLinkProblem().getJobData());
        int iteration = 0;
        double bestScore;
        double maxVolume = this.getSupplyLinkProblem().getTransporter(0).getCapacity();
        int volTmp = 0;
        int higherBatchVolume = 0;
        int higherBatchVolumeIndice = 0;
        int volumecoeff = 1;
        double datecoeff = 1;
        int indiceSplitBatch = 0;
        boolean iterate = true;

        //touts les jobs dans un batch 0...
        for (int i = 0; i < nbJobs; ++i) {
            this.getProcessingSchedule()[i].setBatchIndice(0);
        }
        this.batchSize[0] = nbJobs;

        //on trie les jobs selon leur date de livraison
        //sortOnDueDates(jobData);
        while (iterate && iteration < nbJobs) {
            /*Reinitialisation des paramètres pour un nouveau passage de boucle*/
            iterate = false;
            bestScore = -1;

            /*Boucle qui determine quel batch splitter en fonction des volumes*/
            volTmp = 0;
            higherBatchVolume = 0;
            for (int i = 0; i < nbJobs; ++i) {
                //ajout du volume du job i au volume du batch actuellement a l'étude                
                volTmp += jobData[i].getSize();
                //Si c'est le dernier job du batch..
                if (i == nbJobs - 1) {
                    if (volTmp > higherBatchVolume) {
                        higherBatchVolume = volTmp;
                        higherBatchVolumeIndice = this.getProcessingSchedule()[i].getBatchNumber();
                    }
                    volTmp = 0;
                } else if (this.getProcessingSchedule()[i].getBatchNumber() != this.getProcessingSchedule()[i + 1].getBatchNumber()) {
                    if (volTmp > higherBatchVolume) {
                        higherBatchVolume = volTmp;
                        higherBatchVolumeIndice = this.getProcessingSchedule()[i].getBatchNumber();
                    }
                    volTmp = 0;
                }

            }
            volTmp = 0;
            int splitAfter = 0;
            for (int i = 0; i < nbJobs; ++i) {
                if (this.getProcessingSchedule()[i].getBatchNumber() == higherBatchVolumeIndice) {
                    volTmp += jobData[i].getSize();
                }
                if (volTmp > maxVolume) {
                    splitAfter = i - 1;
                    break;
                }
            }
            /*Boucle pour trouver le meilleur endroit ou splitter pour séparer en deux batchs*/
            for (int i = 0; i < nbJobs - 1; ++i) {
                //Ne calculer que le score entre deux jobs qui sont d'un même batch
                if (this.getProcessingSchedule()[i].getBatchNumber() == this.getProcessingSchedule()[i + 1].getBatchNumber()) {
                    /*Calcul du coefficiant qui prend en compte le batch de plus gros volume*/
                    if (higherBatchVolume > maxVolume) {
                        //Coefficiant égal à x>1 si c'est optimal de splitter ici d'un point de vue *volumes*
                        if (i == splitAfter) {
                            volumecoeff = 2;
                        } else {
                            volumecoeff = 1;
                        }
                    }
                    /*Calcul du coefficiant qui prend en compte l'écart des dates*/
                    if (jobData[i + 1].getDueDate() - jobData[i].getDueDate() == 0) {
                        datecoeff = 1;
                    } else {
                        datecoeff = jobData[i + 1].getDueDate() - jobData[i].getDueDate();
                    }
                    //indice = ecart de "due date" entre deux jobs * cout de stockage du deuxième job * coefficiant du volume
                    double score = datecoeff * jobData[i + 1].getHoldingCost() * volumecoeff;
                    //on mémorise le score le plus élevé et surtout ou séparer en deux batchs
                    if (score > bestScore) {
                        bestScore = score;
                        indiceSplitBatch = i;
                    }
                }
            }
            /*Boucle qui parcours tout les jobs qui passent dans un nouveau batch*/
            for (int i = indiceSplitBatch + 1; i < nbJobs; ++i) {
                //on les passe dans leur batch +1
                if(iteration < nbJobs - 1) {
                    this.batchSize[this.getProcessingSchedule()[i].getBatchNumber()]--;
                    this.getProcessingSchedule()[i].setBatchIndice(this.getProcessingSchedule()[i].getBatchNumber() + 1);
                    this.batchSize[(this.getProcessingSchedule()[i].getBatchNumber())]++;
                }
            }
            if (bestScore > 26) {
                iterate = true;
            }
            iteration++;
        }
        this.setNbrBatch(this.getProcessingSchedule()[nbJobs - 1].getBatchNumber());
    }

    public final void generateInitialSolution5() {
        int nbJobs = this.getSupplyLinkProblem().getN();
        GJobData[] jobs = this.getSupplyLinkProblem().getJobData();

        int[] tabJobs = new int[nbJobs];
        boolean[] done = new boolean[nbJobs];
        double volume = Double.POSITIVE_INFINITY, dueDate = Double.NEGATIVE_INFINITY;
        int indJob = 0;
        int currentBatch = 0;
        final double maxVolume = this.getSupplyLinkProblem().getTransporter(0).getCapacity();

        // Tri par dueDate croissante et volume décroissant
        for (int i = 0; i < nbJobs; ++i) {
            volume = Double.POSITIVE_INFINITY;
            dueDate = Double.NEGATIVE_INFINITY;
            for (int j = 0; j < nbJobs; ++j) {
                if (!done[j] && jobs[j].getDueDate() > dueDate) {
                    volume = jobs[j].getSize();
                    dueDate = jobs[j].getDueDate();
                    indJob = j;
                } else if (!done[j] && jobs[j].getDueDate() == dueDate && jobs[j].getSize() < volume) {
                    volume = jobs[j].getSize();
                    indJob = j;
                }
            }
            tabJobs[i] = indJob;
            done[indJob] = true;
        }

        volume = 0.d;
        for (int i = 0; i < nbJobs; ++i) {
            if (volume + jobs[i].getSize() > maxVolume) {
                ++currentBatch;
                volume = 0.d;
            }
            volume += jobs[i].getSize();
            ++this.batchSize[currentBatch];
            this.getProcessingSchedule()[i].setBatchIndice(currentBatch);
        }

        this.setNbrBatch(currentBatch + 1);
    }

    /**
     * @param eval the eval to set
     */
    public final void setEval(double eval) {
        this.eval = eval;
        this.setEvaluation(eval);
    }

    /**
     * @param badTimeConstraints the badTimeConstraints to set
     */
    public final void setBadTimeConstraints(int badTimeConstraints) {
        this.badTimeConstraints = badTimeConstraints;
    }

    /**
     * @return the batchSize
     */
    public int[] getBatchSize() {
        return batchSize;
    }

    /**
     * Algo de tri, implémentation du BubbleSort
     * @return sorted tab
     */
    public final GJobData[] sortOnDueDates(GJobData jobData[]) {
        boolean swapped = true;
        int j = 0;
        GJobData tmp;
        GJobData result[] = new GJobData[jobData.length];
        for (int i = 0; i < jobData.length; i++) {
            result[i] = new GJobData(jobData[i]);
        }
        while (swapped) {
            swapped = false;
            j++;
            for (int i = 0; i < result.length - j; i++) {
                if (result[i].getDueDate() > result[i + 1].getDueDate()) {
                    tmp = result[i];
                    result[i] = result[i + 1];
                    result[i + 1] = tmp;
                    swapped = true;
                }
            }
        }
        return result;
    }

    public boolean equals(Solution sol) {
        if (sol == null) {
            return false;
        }
        return this.eval == sol.eval && this.badConstraints == sol.badConstraints && this.badTimeConstraints == sol.badTimeConstraints;
    }
}
