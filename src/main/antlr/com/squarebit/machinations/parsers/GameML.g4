grammar GameML;

// Resource connection.
resourceConnection
    : resourceConnectionLabel? TO IDENTIFIER resourceConnectionId?
    | IDENTIFIER TO (resourceConnectionLabel TO)? IDENTIFIER resourceConnectionId?
    ;

resourceConnectionId
    : ':' IDENTIFIER
    ;

resourceConnectionLabel
    : multipliedProbableFlowRate resourceName?
    | logicalExpression (',' multipliedProbableFlowRate)? resourceName?
    ;

multipliedProbableFlowRate
    : (INTEGER TIMES)? intervalFlowRate
    | (INTEGER TIMES)? probability (',' intervalFlowRate)?
    ;

probability
    : PERCENTAGE;

resourceName
    : LEFT_PARENTHESIS IDENTIFIER RIGHT_PARENTHESIS;

intervalFlowRate
    : integerExpression ('/' integerExpression)?
    | ALL ('/' integerExpression)?;

integerExpression
    : unaryIntegerExpression
    | groupIntegerExpression
    | binaryIntegerExpression;

unaryIntegerExpression
    : INTEGER
    | REAL
    | DICE
    | DRAW
    ;

groupIntegerExpression
    : LEFT_PARENTHESIS integerExpression RIGHT_PARENTHESIS;

binaryIntegerExpression
    : unaryIntegerExpression (PLUS|MINUS) integerExpression;

// Modifiers

modifier
    : modifierLabel? TO IDENTIFIER
    | IDENTIFIER TO (modifierLabel TO)? IDENTIFIER
    ;

modifierLabel
    : flowRateModifier
    | intervalModifier
    | multiplierModifier
    | probabilityModifier
    ;

flowRateModifier
    : (PLUS|MINUS)? (INTEGER|REAL);

intervalModifier
    : (PLUS|MINUS)? INTERVAL;

multiplierModifier
    : (PLUS|MINUS)? MULTIPLIER;

probabilityModifier
    : (PLUS|MINUS)? PERCENTAGE;


// Triggers
trigger
    : triggerProbability? TO IDENTIFIER
    ;

triggerProbability
    : PERCENTAGE
    | INTEGER
    | REAL
    ;

// Activators
activator
    : logicalExpression TO IDENTIFIER
    ;


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
INTERVAL: [0-9]+'i';
MULTIPLIER: [0-9]+'m';
ALL: 'all';
DRAW: 'draw'[0-9]+;
PERCENTAGE: [0-9]+'%';
IDENTIFIER: [a-zA-Z_]([a-zA-Z_0-9])*;
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