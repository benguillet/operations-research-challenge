
import gproblem.GSupplyLinkProblem;
import gsolver.GSolver;

/**
 * A solver
 *
 * @author Olivier Grunder
 * @version 0.01
 * @date 14 avril 2011
 *
 */
public class MySolver extends GSolver {

    private static final Object _lock = new Object();
    private Solution best = null;
    private TabuPool pool = null;

    private MySolver() {
        super();
    }

    /**
     * @param problem
     */
    public MySolver(GSupplyLinkProblem problem) {
        super(problem);
        this.pool = new TabuPool(problem, this);
        this.best = new Solution(problem);
        bestSolution = best;
    }

    /**
     * @param problem
     */
    public MySolver(GSupplyLinkProblem problem, int solvingTime) {
        super(problem);
        this.solvingTime = solvingTime;
        this.pool = new TabuPool(problem, this);
        this.best = new Solution(problem);
        //TODO : affiner ça..
        this.best.setBadTimeConstraints(1000000000);
        bestSolution = best;
    }

    /**
     * solves the problem
     */
    @Override
    protected void solve() {
        // MT
        this.pool.go();
    }

    public Solution getBest() {
        return this.best;
    }

    
    public void setBest(Solution sol) {
        this.best.clone(sol);
        //this.bestSolution = this.best;
    }

    @Override
    protected void stopSolver() {
        this.pool.stop();
        this.stop();
        log.println("END OF THE SOLVING TIME : " + getElapsedTimeString());
        log.println("bestSolution = " + bestSolution);
    }
}
