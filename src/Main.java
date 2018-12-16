import java.util.*;

public class Main {

    public static Token[] t = new Token[100];
    public static Token[] newT = new Token[100];
    public static Map mp = new HashMap();
    public static int[] lch = new int[100];
    public static int[] rch = new int[100];
    public static Token[] op = new Token[100];

    public static void main(String[] args){
        Scanner input = new Scanner(System.in);
        System.out.println("Enter '$$' to quit calculator!");
        String s = input.nextLine();
        while(s != "$$"){
            int cnt = lex(s);
            /*for(int i = 0; i < cnt; i++){
                System.out.println(t[i]);
            }
            */
            grammer(cnt);
            t = new Token[100];
            newT = new Token[100];
            s = input.nextLine();
        }
    }

    static void grammer(int cnt){
        if(cnt == 1){
            if(t[0].tag.equals(Tag.ID)){
                System.out.println(mp.get(t[0].toString()));
            }
            else{
                System.out.println(t[0].toString());
            }
            return;
        }
        if(t[1].tag.equals(Tag.ASSIGN)){
            String varName = t[0].toString();
            mp.put(varName, cal(2, cnt));
        }
        else{
            System.out.println(cal(0, cnt));
        }
    }

    static int lex(String s){
        int cnt = 0;
        char ch;
        for (int i = 0; i < s.length(); i++) {
            ch = s.charAt(i);

		/*
			标识符
		*/
		if (ch >= 'a' && ch <= 'z' || ch >= 'A' && ch <= 'Z' || ch == '_') {
		    String name = "";
		    while (i < s.length() && ch >= 'a' && ch <= 'z' || ch >= 'A' && ch <= 'Z' || ch == '_' || ch >= '0' && ch <= '9'){
		        name += ch;			//记录字符
                i++;
                if(i >= s.length()) break;
                ch = s.charAt(i);					//读入字符
		    }
		    if(name != null){
		        t[cnt++] = new Id(name);
		        Token x = new Id(name);
		    }
		    i--;
		    continue;
		}
		/*
			常量
		*/
		if (ch >= '0' && ch <= '9') {
		    double val = 0;
		    if (ch != '0') {
		        while (i < s.length() && ch >= '0' && ch <= '9'){
		            val = val * 10 + ch - '0';
		            i++;
		            if(i >= s.length())     break;
		            ch = s.charAt(i);
		        }
		        if(ch == '.'){
		            double tmp = 0.1;
		            while(i < s.length() && ch >= '0' && ch <= '9'){
		                val += tmp * (ch - '0');
		                tmp /= 10;
                    }
                }
		    }
		    t[cnt++] = new Num(val);
		    i--;
		    continue;
		}

         /*
            空白符
          */
         if(ch == ' ' || ch == '\t' || ch == '\n')
             continue;

         /*
            界符
          */
         switch (ch){
             case '+':
                 t[cnt++] = new Token(Tag.ADD); break;
             case '-':
                 t[cnt++] = new Token(Tag.SUB); break;
             case '*':
                 t[cnt++] = new Token(Tag.MUL); break;
             case '/':
                 t[cnt++] = new Token(Tag.DIV); break;
             case '=':
                 t[cnt++] = new Token(Tag.ASSIGN);  break;
             case '(':
                 t[cnt++] = new Token(Tag.LPAREN);  break;
             case ')':
                 t[cnt++] = new Token(Tag.RPAREN);  break;
                 default:
                    t[cnt++] = new Token(Tag.ERR);
         }

        }
        return cnt;
    }

    static double cal(int i, int cnt){
        transform(i, cnt);
        /*for(int j = 0; j < newT.length; j++)
            if(newT[j] != null)
            System.out.println(newT[j].toString());*/
        return calAnswer();
    }

    static void transform(int i, int cnt){
        int count = 0;
        Stack<Token> op = new Stack<>();
        for(; i < cnt; i++){
            Token ch = t[i];
            if(ch.tag.equals(Tag.ID)){
                //String tmp = (String)mp.get(t[i].toString());
                t[i] = new Num((double)mp.get(t[i].toString()));
                ch = t[i];
            }
            if(ch.tag.equals(Tag.NUM))
                newT[count++] = ch;
            else if(ch.tag.equals(Tag.LPAREN))
                newT[count++] = ch;
            else if(ch.tag.equals(Tag.RPAREN)){
                while(!op.isEmpty()){
                    ch = op.pop();
                    if(ch.tag.equals(Tag.LPAREN))
                        break;
                    else
                        newT[count++] = ch;
                }
            }
            else{
                while(true){
                    if(op.isEmpty()){
                        op.push(ch);
                        break;
                    }
                    Token c = op.peek();
                    if(op.peek().tag.equals(Tag.LPAREN) || c.tag.equals(Tag.ADD) || c.tag.equals(Tag.SUB)
                    || ch.tag.equals(Tag.MUL) || ch.tag.equals(Tag.DIV)){
                        op.push(ch);
                        break;
                    }
                    newT[count++] = op.pop();
                }
            }
        }
        while(!op.isEmpty()){
            newT[count++] = op.pop();
        }
    }

    static double calAnswer(){
        Stack<Double> stack = new Stack<>();
        for(Token c: newT){
            if(c != null){
                switch (c.tag) {
                    case ADD: {
                        double op2 = stack.pop();
                        double op1 = stack.pop();
                        double result = op1 + op2;
                        stack.push(result);
                        break;
                    }
                    case SUB: {
                        double op2 = stack.pop();
                        double op1 = stack.pop();
                        double result = op1 - op2;
                        stack.push(result);
                        break;
                    }
                    case MUL: {
                        double op2 = stack.pop();
                        double op1 = stack.pop();
                        double result = op2 * op1;
                        stack.push(result);
                        break;
                    }
                    case DIV: {
                        double op2 = stack.pop();
                        double op1 = stack.pop();
                        double result = op1 / op2;
                        stack.push(result);
                        break;
                    }
                    case LPAREN:    break;
                    case RPAREN:    break;
                    default: {
                        double val = Double.valueOf(c.toString());
                        stack.push(val);
                        break;
                    }
                }
            }
        }
        return stack.pop();
    }
}
