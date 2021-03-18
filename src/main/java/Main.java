public class Main {
    public static void main(String[] args) {
      Executor executor=new Executor();
        String expression = "1.08-0.16/(0.6*2+0.8)";
   System.out.println(executor.convertExpression(expression));
//   System.out.println(res);
    }
}
