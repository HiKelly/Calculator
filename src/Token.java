/*
	词法记号类
*/
public class Token {
    Tag tag;
    Token(){}
    Token(Tag ttag){
        tag = ttag;
    }
    @Override
    public String toString() {
        return tag.toString();
    }
}

/*
	词法记号标签
*/
enum Tag{
    ERR,					//错误，异常
    ID,						//标识符
    NUM,				//常量
    ADD,SUB,MUL,DIV,	//算数运算符
    ASSIGN,                 //赋值
    LPAREN,RPAREN,			//()
};

/*
标识符记号类
*/
class Id extends Token {
    String name;
    Id(String n){
        name = n;
        tag = Tag.ID;
    };

    @Override
    public String toString() {
        return name;
    }
};

/*
数字记号类
*/
class Num extends Token {
    double val;
    Num(double v){
        val = v;
        tag = Tag.NUM;
    }

    @Override
    public String toString() {
        return String.valueOf(val);
    }
};


