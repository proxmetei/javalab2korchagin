import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ExecutorTest {

    Executor executor;
    @Before
    public void init(){
      executor=new Executor();
    }
    @Test
    public void convertExpressionTest(){
        String expression = "1.08-0.16/(0.6*2+0.8)";
        final String expected = "1.08 0.16 0.6 2 * 0.8 + / - ";
        String result = executor.convertExpression(expression);
        Assert.assertEquals(expected,result);
    }
    @Test
    public void countTest(){
        String expression = "1.08-0.15/-(0.7/2-0.8*3)";
        final double expected = 1.08-0.15/-(0.7/2-0.8*3);
        double result = executor.execute(expression);
        Assert.assertEquals(expected,result,0);
    }
}
