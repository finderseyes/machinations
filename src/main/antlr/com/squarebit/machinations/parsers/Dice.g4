grammar Dice;

/*
    Resource and capacity declarations of a pool node.
*/
resourceExpression: singleResourceExpression (',' singleResourceExpression)*;
singleResourceExpression: INT IDENTIFIER?;

/*
    Label of a connection or a trigger.
*/
connectionLabel: implicitConnectionLabel | explicitConnectionLabel;
implicitConnectionLabel: expression? TO IDENTIFIER (LEFT_PARENTHESIS IDENTIFIER RIGHT_PARENTHESIS)?;
explicitConnectionLabel: IDENTIFIER (TO expression)? TO IDENTIFIER (LEFT_PARENTHESIS IDENTIFIER RIGHT_PARENTHESIS)?;

labelModifier: (PLUS|MINUS)INT?;

groupArithmeticExpression: LEFT_PARENTHESIS arithmeticExpression RIGHT_PARENTHESIS;
groupLogicalExpression: LEFT_PARENTHESIS logicalExpression RIGHT_PARENTHESIS;

unaryArithmeticExpression
    : number
    | identifier
    | groupArithmeticExpression
    | (PLUS|MINUS) arithmeticExpression;

unaryLogicalExpression
    : relationalExpression
    | leftImplicitRelationalExpression
    | rightImplicitRelationalExpression
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

relationalExpression
    : arithmeticExpression (GT|GTE|LT|LTE|EQ|NEQ) arithmeticExpression;

leftImplicitRelationalExpression
    : (GT|GTE|LT|LTE|EQ|NEQ) arithmeticExpression;

rightImplicitRelationalExpression
    : arithmeticExpression (GT|GTE|LT|LTE|EQ|NEQ);

logicalOrExpression
    : unaryLogicalExpression OR logicalExpression;

logicalAndExpression
    : unaryLogicalExpression AND logicalExpression;

number: DICE_TERM | INT | FRACTION | PERCENTAGE;

diceExpression: term ((PLUS|MINUS) term)*;

term
    : diceTerm
    | integer
    ;

diceTerm: DICE_TERM;
integer: INT;
fraction: FRACTION;
identifier: REFERENCE;

TO: '-->';
IDENTIFIER: [a-zA-Z_]([a-zA-Z_0-9])*;
REFERENCE: '$'[a-zA-Z_]([a-zA-Z_0-9])*;
DICE_TERM: [0-9]*'D'[0-9]*;
INT: [0-9]+;
FRACTION: [0-9]+'/'[0-9]+;
PERCENTAGE: [0-9]+'%';

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