import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Objects;

public class CalculatorMainListener extends CalculatorBaseListener{

    Deque<Double> numbers = new ArrayDeque<>();

    @Override
    public void exitIntegralExpression(CalculatorParser.IntegralExpressionContext ctx) {
        if (ctx.MINUS() != null) {
            numbers.add((-1 * Double.parseDouble(ctx.INT().toString())));
        } else {
            numbers.add( Double.parseDouble(ctx.INT().toString()));
        }
        super.exitIntegralExpression(ctx);
    }
    @Override
    public void exitEveryRule(ParserRuleContext ctx) {
        System.out.println("exitEveryRule:"+ctx.getText());
        super.exitEveryRule(ctx);
    }

    @Override
    public void exitExpression(CalculatorParser.ExpressionContext ctx) {
        Double value = numbers.pollLast();
        for (int i = 1; i< ctx.getChildCount(); i = i+2){
            if(Objects.equals(ctx.getChild(i).getText(), "+")){
                value = value + numbers.pollLast();
            } else {
                value =  numbers.pollLast() - value;
            }
        }
        numbers.add(value);
        super.exitExpression(ctx);
    }

    @Override
    public void exitMultiplyingExpression(CalculatorParser.MultiplyingExpressionContext ctx) {
        Double value = numbers.pollLast();
        for (int i = 1; i< ctx.getChildCount(); i = i+2){
            if(Objects.equals(ctx.getChild(i).getText(), "*")){
                value = value * numbers.pollLast();
            } else if(Objects.equals(ctx.getChild(i).getText(), "/")) {
                if(numbers.getLast() == 0) throw new ArithmeticException("Nie można dzielić przez 0");
                value =  numbers.pollLast() / value;
            }
        }
        numbers.add(value);
        super.exitMultiplyingExpression(ctx);
    }

    @Override
    public void exitPowerExpression(CalculatorParser.PowerExpressionContext ctx) {
        if (ctx.POW().size() != 0){
            Double val1 = numbers.pollLast();
            Double val2 = numbers.pollLast();
            double value = Math.pow(val2, val1);

            numbers.add(value);
        }
        super.exitPowerExpression(ctx);
    }
    @Override
    public void exitSqrtExpression(CalculatorParser.SqrtExpressionContext ctx) {
        if(ctx.SQRT() != null){
            double value = Math.sqrt(Integer.parseInt(ctx.INT().toString()));
            numbers.add(value);
        }
        super.exitSqrtExpression(ctx);
        System.out.println("exitSqrtExpression:"+ctx.getText()+" ; numbers:"+numbers);
    }

    private Double getResult(){
        return numbers.peek();
    }

    public static void main(String[] args) throws Exception {
       // CharStream charStreams = CharStreams.fromFileName("./example.txt");
        Double result = calc("2-1");
        System.out.println("Result = " + result);
    }

    public static Double calc(String expression) {
        return calc(CharStreams.fromString(expression));
    }

    public static Double calc(CharStream charStream) {
        CalculatorLexer lexer = new CalculatorLexer(charStream);
        CommonTokenStream tokens = new CommonTokenStream(lexer);

        CalculatorParser parser = new CalculatorParser(tokens);
        ParseTree tree = parser.expression();

        ParseTreeWalker walker = new ParseTreeWalker();
        CalculatorMainListener mainListener = new CalculatorMainListener();
        walker.walk(mainListener, tree);
        return mainListener.getResult();
    }

}
