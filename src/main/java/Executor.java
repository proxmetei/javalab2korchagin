import javax.naming.NameNotFoundException;
import java.util.EmptyStackException;
import java.util.Locale;
import java.util.Scanner;
import java.util.Stack;

public class Executor {
    /**
     *
     * @param sb - символ
     * @return - возвращает true, если символ — оператор
     */
    private boolean isOperator(char sb){
        if("+-/*^()".indexOf(sb)!=-1){
            return true;
        }
        return false;
    }
    /**
     *
     * @param sb - символ
     * @return - возвращает true, если символ — оператор
     */
    private boolean isNum(char sb){
        if("0123456789".indexOf(sb)!=-1){
            return true;
        }
        return false;
    }
    /**
     *
     * @param sb - символ
     * @return - возвращает true, если символ — переменная
     */
    private boolean isVariable(char sb){
        if(sb>='a'&&sb<='z'){
            return true;
        }
        return false;
    }
    /**
     *
     * @param sb - символ
     * @return - возвращает приоритет оператора
     */
    private int getPriority(char sb){
        switch (sb){
            case '(': return 0;
            case ')': return 1;
            case '+':
            case '-': return 2;
            case '*':
            case '/': return 3;
            case '^': return 4;
            default: return 5;
        }
    }
    /**
     * Функция,возвращающая индекса конца выражения в скобках
     * @param expression-выражение
     * @param i-индекс открывающей скобки
     * @return преобразованное в инфиксную запись выражение
     */
    public int scopeInd (String expression,int i)
    {
        int count=1;
        int j=i+1;
        for(;j<expression.length()&&count!=0;j++)
        {
            if(expression.charAt(j)==')')
            {
                count--;
            }
            else if(expression.charAt(j)=='(')
            {
                count++;
            }
        }
        return j;
    }
    /**
     * Функция, преобразующая выражение в польскую инверсную запись
     * @param expression-выражение
     * @return выражение, преобразованное в польскую инверсную запись
     */
    public String convertExpression(String expression){
    String result="";
    Stack<Character> stack =new Stack<Character>();
    for(int i=0;i<expression.length();i++)
    {

            if(isNum(expression.charAt(i))) {
            while(isNum(expression.charAt(i))||expression.charAt(i)=='.')
            {
                result+=expression.charAt(i);
                i++;
                if(i==expression.length())
                {
                    break;
                }
            }
            i--;
            result+=' ';
            }
            if(isOperator(expression.charAt(i))) {
                if (expression.charAt(i) == ')') {
                    Character st = stack.pop();
                    while (st != '(') {
                        result += Character.toString(st) + ' ';
                        st = stack.pop();
                    }
                }
               else if (expression.charAt(i) == '(') {
                    stack.push('(');
                }
                else {
                    if (stack.size() != 0) {
                        if (getPriority(expression.charAt(i)) <= getPriority(stack.peek())) {
                            result += stack.pop().toString() + ' ';
                        }

                    }
                    stack.push(expression.charAt(i));
                }
            }


    }
        while (stack.size()!=0)
        {
            result+=stack.pop().toString()+' ';
        }
    return result;
    }
    /**
     * Функция, вычисляющая результат выражения в польской инверсной записи
     * @param expression-выражение в польской инверсной записи
     * @return результат выражения
     */
    public double count(String expression)
    {
        Stack<Double> nums= new Stack<Double>();

        for(int i=0;i<expression.length();i++)
        {
            if(isNum(expression.charAt(i)))
            {
                String temp="";
                while(isNum(expression.charAt(i))||expression.charAt(i)=='.')
                {
                    temp+=expression.charAt(i);
                    i++;
                    if(i==expression.length())
                    {
                        break;
                    }
                }
                i--;
                nums.push(Double.valueOf(temp));
            }
            else if(isOperator(expression.charAt(i)))
            {
                double operationResult = 0;
                double num2=nums.pop();
                double num1=nums.pop();
                switch (expression.charAt(i))
                {
                    case '+': operationResult=num1+num2; break;
                    case '*': operationResult=num1*num2; break;
                    case '-': operationResult=num1-num2; break;
                    case '/': operationResult=num1/num2; break;
                }
                nums.push(operationResult);
            }
        }
        return nums.pop();
    }
    /**
     * Функция, вычисляющая результат выражения
     * @param expression-выражение
     * @return результат выражения
     */
    public double execute(String expression)
    {
        double result=0.0;
        expression=insertVariables(expression);
        expression=unarMinusesSupport(expression);
        expression=convertExpression(expression);
        try
        {
            result=count(expression);
        }
        catch (EmptyStackException e)
        {
            System.out.println("Некорректные данные");
        }
        return result;
    }
    /**
     * Функция, осуществляющая подстановку пользователем значений переменных
     * @param expression-выражение
     * @return выражение с подстваленными значаниями переменных
     */
    public String insertVariables(String expression)
    {
        for(int i=0;i<expression.length();i++)
        {
            if(isVariable(expression.charAt(i)))
            {
                char [] arrayExp=expression.toCharArray();
                double input;
                Scanner in = new Scanner(System.in).useLocale(Locale.US);
                System.out.println("Enter variable " + expression.charAt(i));
                input = in.nextDouble();
                String inputArray=String.valueOf(input);
                char[] result=new char[inputArray.length()+expression.length()];
                for (int j=0;j<i;j++)
                {
                    result[j]=arrayExp[j];
                }

                for(int j=0;j<inputArray.length();j++)
                {
                    result[i+j]=inputArray.charAt(j);
                }
                for(int j=i+1;j<arrayExp.length;j++)
                {
                    result[j+inputArray.length()-1]=arrayExp[j];
                }
                expression=String.copyValueOf(result);
            }
        }
        return expression;
    }
    /**
     * Функция, осуществляющая поддержку унарных минусов
     * @param expression-выражение
     * @return выражение с преобразованными в бинарные минусами
     */
    public String unarMinusesSupport(String expression)
    {
        expression=insertVariables(expression);
        for(int i=0;i<expression.length();i++)
        {
            if(expression.charAt(i)=='-'&&(i==0||isOperator(expression.charAt(i-1))))
            {
                char[] result=new char[expression.length()+3];
                if(expression.charAt(i+1)!='(') {
                    for (int j = 0; j < i; j++) {
                        result[j] = expression.charAt(j);

                    }
                    result[i]='(';
                    result[i+1] = '0';
                    result[i+2]='-';
                    int j=i+1;
                    for (; (j < expression.length())&&(isNum(expression.charAt(j))||expression.charAt(j)=='.'); j++) {
                        result[j + 2] = expression.charAt(j);

                    }
                    result[j+2]=')';
                    for(int k=j;k<expression.length();k++)
                    {
                        result[k+3]=expression.charAt(k);

                    }
                }
                else
                {
                    for(int j=0;j<i;j++)
                    {
                        result[j]=expression.charAt(j);
                    }
                    result[i]='(';
                    result[i+1]='0';
                    int p=scopeInd(expression,i+1);
                    for(int j=i;j<p;j++)
                    {
                        result[j+2]=expression.charAt(j);
                    }
                    result[p+2]=')';
                    for(int j=p;j<expression.length();j++)
                    {
                        result[j+3]=expression.charAt(j);
                    }
                }
                expression=String.copyValueOf(result);
            }
        }
        return expression;
    }
}
