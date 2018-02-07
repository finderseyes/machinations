grammar Dice;

labelModifier: (PLUS|MINUS)INT?;

program: expression EOF;

primaryExpression
    : number
    | groupArithmeticExpression
    | groupLogicalExpression;

groupExpression: LEFT_PARENTHESIS expression RIGHT_PARENTHESIS;
groupArithmeticExpression: LEFT_PARENTHESIS arithmeticExpression RIGHT_PARENTHESIS;
groupLogicalExpression: LEFT_PARENTHESIS logicalExpression RIGHT_PARENTHESIS;

unaryExpression
    : primaryExpression
    | unaryArithmeticExpression
    | unaryLogicalExpression
    ;

unaryArithmeticExpression
    : number
    | groupArithmeticExpression
    | (PLUS|MINUS) arithmeticExpression;

unaryLogicalExpression
    : relationExpression
    | groupLogicalExpression
    | NOT logicalExpression;

expression
    : arithmeticExpression
    | logicalExpression
    ;

arithmeticExpression
    : unaryArithmeticExpression
    | additiveExpression
    | multiplicativeExpression
    ;

logicalExpression
    : unaryLogicalExpression
    | logicalAndExpression
    | logicalOrExpression
    ;


multiplicativeExpression
    : unaryArithmeticExpression TIMES arithmeticExpression;

additiveExpression
    : unaryArithmeticExpression PLUS arithmeticExpression;

relationExpression
    : arithmeticExpression (GT|GTE|LT|LTE|EQ|NEQ) arithmeticExpression;

logicalOrExpression
    : unaryLogicalExpression OR logicalExpression;

logicalAndExpression
    : unaryLogicalExpression AND logicalExpression;

number: integer | diceTerm;

diceExpression: term ((PLUS|MINUS) term)*;

term
    : diceTerm
    | integer
    ;

diceTerm
    : INT?'D'INT?
    ;

integer
    : (PLUS|MINUS)?INT
    ;

INT
    : [0-9]+
    ;

PLUS: '+';
MINUS: '-';
TIMES: '*';
GT: '>';
GTE: '>=';
LT: '<';
LTE: '<=';
EQ: '==';
NEQ: '!=';

AND: '&&';
OR: '||';
NOT: '!';

LEFT_PARENTHESIS: '(';
RIGHT_PARENTHESIS: ')';
WS: [ \r\t\n]+ -> skip;