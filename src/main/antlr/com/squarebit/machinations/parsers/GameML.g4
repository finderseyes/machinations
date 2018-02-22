grammar GameML;

resourceConnection
    : resourceConnectionLabel? TO IDENTIFIER;

resourceConnectionLabel
    : flowRate
    | probability
    | logicalExpression
    | probability ',' flowRate
    | logicalExpression ',' probability
    | logicalExpression ',' flowRate
    | logicalExpression ',' probability ',' flowRate
    ;

probability
    : unitaryProbability
    | multipliedProbability;

unitaryProbability
    : PERCENTAGE;

multipliedProbability
    : INTEGER TIMES PERCENTAGE;

flowRate
    : unitaryFlowRate
    | multipliedFlowRate
    | allFlowRate
    ;

unitaryFlowRate
    : numberExpression ('/' numberExpression)?;

multipliedFlowRate
    : INTEGER TIMES unitaryFlowRate;

allFlowRate
    : ALL ('/' numberExpression)?;

numberExpression
    : unitaryNumberExpression
    | groupNumberExpression
    | compoundNumberExpression;

unitaryNumberExpression
    : INTEGER
    | REAL
    | DICE
    | DRAW
    ;

groupNumberExpression
    : LEFT_PARENTHESIS numberExpression RIGHT_PARENTHESIS;

compoundNumberExpression
    : unitaryNumberExpression (PLUS|MINUS) numberExpression;

// Logical expression
groupArithmeticExpression: LEFT_PARENTHESIS arithmeticExpression RIGHT_PARENTHESIS;
groupLogicalExpression: LEFT_PARENTHESIS logicalExpression RIGHT_PARENTHESIS;

unaryArithmeticExpression
    : INTEGER
    | REAL
    | IDENTIFIER
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
    : unaryArithmeticExpression (PLUS|MINUS) arithmeticExpression;

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

INTEGER: [0-9]+;
REAL: [0-9]*'.'[0-9]+;
DICE: [0-9]*'D'[0-9]*;
ALL: 'all';
DRAW: 'draw'[0-9]+;
PERCENTAGE: [0-9]+'%';
IDENTIFIER: [a-z_]([a-zA-Z_0-9])*;
TO: '-->';
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