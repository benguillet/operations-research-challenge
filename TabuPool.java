
import gproblem.GSupplyLinkProblem;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


/**
 *
 * @author Ira
 */

public class TabuPool extends ThreadPoolExecutor {

    private MethodeTabou[] tabus = new MethodeTabou[4];

    public TabuPool(GSupplyLinkProblem pb, MySolver solver) {
        super(2, 2, 0x7FFFFFFFFFFFFFFFL, TimeUnit.DAYS, new LinkedBlockingQueue<Runnable>());

        Solution sol = new Solution(pb);
        sol.generateInitialSolution5();
        sol.check();
        sol.evaluate();
		System.out.println(sol);
        this.tabus[0] = new MethodeTabou(sol, pb, solver);

        sol = new Solution(pb);
        sol.generateInitialSolution4();
        sol.check();
        sol.evaluate();
        this.tabus[1] = new MethodeTabou(sol, pb, solver);

        sol = new Solution(pb);
        sol.generateInitialSolution3();
        sol.check();
        sol.evaluate();
        this.tabus[2] = new MethodeTabou(sol, pb, solver);

        sol = new Solution(pb);
        sol.generateInitialSolution4();
        sol.check();
        sol.evaluate();
        this.tabus[3] = new MethodeTabou(sol, pb, solver);
    }

    public void go() {
        for(int i=0; i<2; ++i){
            this.execute(this.tabus[i]);
        }
    }

    public void stop() {
        for(int i=0; i<2; ++i){
            this.tabus[i].stop();
        }
        this.shutdown();
    }
}
