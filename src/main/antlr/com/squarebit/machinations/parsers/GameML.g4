grammar GameML;

resourceExpression: singleResourceExpression (',' singleResourceExpression)*;
singleResourceExpression: INTEGER IDENTIFIER?;

// Resource connection.
resourceConnection
    : resourceConnectionLabel? TO IDENTIFIER elementId?
    | IDENTIFIER TO (resourceConnectionLabel TO)? IDENTIFIER elementId?
    ;

elementId
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
    : (IDENTIFIER TO)? modifierLabel TO IDENTIFIER elementId?
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
    : triggerLabel? TO IDENTIFIER elementId? properties?
    | IDENTIFIER TO (triggerLabel TO)? IDENTIFIER elementId? properties?
    ;

triggerLabel
    : triggerProbability
    | logicalExpression (',' triggerProbability)?
    ;

// trigger probability or distribution.
triggerProbability
    : PERCENTAGE
    | INTEGER
    | REAL
    ;

// connection or trigger properties
properties
    : LEFT_CURLY_BRACKET property* RIGHT_CURLY_BRACKET;

property
    : propertyName '=' (booleanValue | numericValue);

propertyName
    : IDENTIFIER;

booleanValue
    : TRUE | FALSE;

numericValue
    : INTEGER | REAL;

// Activators
activator
    : (IDENTIFIER TO)? activatorLabel TO IDENTIFIER elementId?
    ;

activatorLabel
    : logicalExpression
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
    : unaryArithmeticExpression (TIMES arithmeticExpression)?;

additiveExpression
    : multiplicativeExpression (PLUS|MINUS) arithmeticExpression;

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

TRUE: 'true';
FALSE: 'false';
ALL: 'all';
DRAW: 'draw'[0-9]+;

INTEGER: [0-9]+;
REAL: [0-9]*'.'[0-9]+;
DICE: [0-9]*'D'[0-9]*;
INTERVAL: [0-9]+'i';
MULTIPLIER: [0-9]+'m';
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
LEFT_CURLY_BRACKET: '{';
RIGHT_CURLY_BRACKET: '}';
WS: [ \r\t\n]+ -> skip;