grammar Mach;

unitDeclaration
    : structureDeclaration*
    ;

structureDeclaration
    : graphDeclaration
    ;

graphDeclaration
    :   'graph' IDENTIFIER graphBody
    ;

graphBody
    :   '{' graphBodyDeclaration* '}'
    ;

graphBodyDeclaration
    : nodeDeclaration
    | connectionDeclaration
    | memberVariableDeclaration
    | functionDeclaration
    ;

memberVariableDeclaration
    : variableDeclaration ';'
    ;

variableDeclaration
    : 'let' variableDeclarationList
    ;

variableDeclarationList
    : variableDeclarator (',' variableDeclarator)*
    ;

variableDeclarator
    : registerId ('=' variableInitializer)?
    ;

variableInitializer
    : expression
    ;

registerId
    : IDENTIFIER
    ;


connectionDeclaration
    : 'connection' connectionDeclarationList ';'
    ;

connectionDeclarationList
    : connectionDeclarator (',' connectionDeclarator)*
    ;

connectionDeclarator
    : connectionId '=' connectionDescriptor
    ;

connectionDescriptor
    : nodeId resourceFlowExpression? TO nodeId
    ;

connectionId
    : IDENTIFIER
    ;

resourceFlowExpression
    : ('(' flowRate ')')
    ;

nodeDeclaration
    : nodeModifiers? 'node' nodeDeclaratorList ';'
    ;

nodeModifiers
    : nodeModifier
    ;

nodeModifier
    : 'transitive'
    | 'source'
    | 'drain'
    | 'converter'
    | 'end'
    ;

nodeDeclaratorList
    : nodeDeclarator (',' nodeDeclarator)*
    ;

nodeDeclarator
    : nodeId ('=' nodeInitializer)?
    ;

nodeId
    : IDENTIFIER
    ;

nodeInitializer
    : sourceNodeInitializer
    | drainNodeInitializer
    | resourceSetNodeInitializer
    ;

sourceNodeInitializer
    : 'source'
    ;

drainNodeInitializer
    : 'drain'
    ;

resourceSetNodeInitializer
    : resourceSetExpression
    ;

resourceSetExpression
    : resourceDescriptor
    | '{' resourceDescriptor (',' resourceDescriptor)* '}'
    ;

resourceDescriptor
    : expression ('/' INTEGRAL_NUMBER)? resourceName?
    ;

resourceName
    : IDENTIFIER
    ;

functionDeclaration
    : functionModifier? 'function' functionName LEFT_PARENTHESIS RIGHT_PARENTHESIS functionBody
    ;

functionModifier
    : 'start'
    | 'automatic'
    | 'interactive'
    ;

functionName
    : IDENTIFIER
    ;

functionBody
    :   LEFT_CURLY_BRACKET blockStatements* RIGHT_CURLY_BRACKET
    ;

statement
    : emptyStatement
    | block
//    | transferDeclarationStatement
//    | probabilisticDeclarationStatement
//    | activationDeclarationStatement
//    | delayDeclarationStatement
//    | intervalDeclarationStatement
    | expressionStatement
    | ifThenStatement
    | ifThenElseStatement
    ;

emptyStatement
    : ';'
    ;

intervalDeclarationStatement
    : intervalDeclaration
    ;

intervalDeclaration
    : 'for' 'every' intervalSteps 'steps' ':' statement
    ;

intervalSteps
    : expression
    ;

delayDeclarationStatement
    : delayDeclaration
    ;

delayDeclaration
    : 'delay' 'for' delaySteps 'steps' ':' statement
    ;

delaySteps
    : expression
    ;

activationDeclarationStatement
    : activationDeclaration ';'
    ;

activationDeclaration
    : ('activate' | 'deactivate') activationList
    ;

activationList
    : activationNodeId (',' activationNodeId)*
    ;

activationNodeId
    : IDENTIFIER
    ;

block
	: '{' blockStatements? '}'
	;

blockStatements
	: blockStatement+
	;

blockStatement
    : localVariableDeclarationStatement
    | statement
    ;

localVariableDeclarationStatement
    : variableDeclaration ';'
    ;

probabilisticDeclarationStatement
    : probabilisticDeclaration
    ;

probabilisticDeclaration
    : 'randomly' '{' probabilisticStatementDeclaration+ '}'
    ;

probabilisticStatementDeclaration
    : probability ':' statement
    ;

probability
    : expression
    | 'else'
    ;

transferDeclarationStatement
    : transferDeclaration ';'
    ;

transferDeclaration
    : 'transfer' transferModifierList resourceFlowDeclarator (',' resourceFlowDeclarator)*
    ;

transferModifierList
    : transferMode*
    ;

resourceConnectionId
    : IDENTIFIER
    ;

flowRate
    : expression resourceName?
    ;

ifThenStatement
    : 'if' '(' expression ')' statement
    ;

ifThenElseStatement
    : 'if' '(' expression ')' statement 'else' statement
    ;


///////////////////////////////////////////////////////////////////////////////
// Expression
expression
    : conditionalExpression
    | assignment
    ;

assignment
	:	leftHandSide assignmentOperator expression
	;

leftHandSide
	:	IDENTIFIER
	;

assignmentOperator
	:	'='
	|	'*='
	|	'/='
	|	'%='
	|	'+='
	|	'-='
	|	'<<='
	|	'>>='
	|	'>>>='
	|	'&='
	|	'^='
	|	'|='
	;

conditionalExpression
	:	conditionalOrExpression
	|	conditionalOrExpression '?' expression ':' conditionalExpression
	;

conditionalOrExpression
	:	conditionalAndExpression
	|	conditionalOrExpression '||' conditionalAndExpression
	;

conditionalAndExpression
	:	inclusiveOrExpression
	|	conditionalAndExpression '&&' inclusiveOrExpression
	;

inclusiveOrExpression
	:	exclusiveOrExpression
	|	inclusiveOrExpression '|' exclusiveOrExpression
	;

exclusiveOrExpression
	:	andExpression
	|	exclusiveOrExpression '^' andExpression
	;

andExpression
	:	equalityExpression
	|	andExpression '&' equalityExpression
	;

equalityExpression
	:	relationalExpression
	|	equalityExpression '==' relationalExpression
	|	equalityExpression '!=' relationalExpression
	;

relationalExpression
	:	additiveExpression
	|	relationalExpression '<' additiveExpression
	|	relationalExpression '>' additiveExpression
	|	relationalExpression '<=' additiveExpression
	|	relationalExpression '>=' additiveExpression
	;

additiveExpression
	:	multiplicativeExpression
	|	additiveExpression '+' multiplicativeExpression
	|	additiveExpression '-' multiplicativeExpression
	;

multiplicativeExpression
	:	unaryExpression
	|	multiplicativeExpression '*' unaryExpression
	|	multiplicativeExpression '/' unaryExpression
	|	multiplicativeExpression '%' unaryExpression
	;

unaryExpression
	:	preIncrementExpression
	|	preDecrementExpression
	|	'+' unaryExpression
	|	'-' unaryExpression
	|	unaryExpressionNotPlusMinus
	;

preIncrementExpression
	:	'++' unaryExpression
	;

preDecrementExpression
	:	'--' unaryExpression
	;

unaryExpressionNotPlusMinus
	:   postfixExpression
	|	'~' unaryExpression
	|	'!' unaryExpression
	;

postfixExpression
	:	( primary | expressionName)
		( postIncrementExpression_lf_postfixExpression | postDecrementExpression_lf_postfixExpression )*
	;

postIncrementExpression
	:	postfixExpression '++'
	;

postIncrementExpression_lf_postfixExpression
	:	'++'
	;

postDecrementExpression
	:	postfixExpression '--'
	;

postDecrementExpression_lf_postfixExpression
	:	'--'
	;

primary
	: literal
	| '(' expression ')'
	| methodInvocation
	| propertyAccess
	;

propertyAccess
	: IDENTIFIER '.' IDENTIFIER
	;

methodInvocation
	: methodName '(' argumentList? ')'
	| expressionName '.' IDENTIFIER '(' argumentList? ')'
	| graphicalMethodInvocation
	;

methodName
	:	IDENTIFIER
	;

argumentList
	:	expression (',' expression)*
	;

expressionName
	:	IDENTIFIER
	;

graphicalMethodInvocation
    : transferInvocation
    | distributionInvocation
    | randomInvocation
    ;

transferInvocation
    : 'transfer' transferMode? resourceFlowDeclaratorList
    ;

resourceFlowDeclaratorList
    : resourceFlowDeclarator ('and' resourceFlowDeclarator)*
    ;

resourceFlowDeclarator
    : resourceSetExpression? 'via' flowDirection
    ;

transferMode
    : 'allornone'
    ;

flowDirection
    : connectionId
    | connectionDescriptor
    ;

distributionInvocation
    : 'distribute' resourceSetExpression? nodeId 'via' distributionList
    ;

distributionList
    : distributionDescriptor ('or' distributionDescriptor)*
    ;

distributionDescriptor
    : ('(' expression ')')? flowDirection
    ;

randomInvocation
    : 'randomly' 'do' randomInvocationCaseList
    ;

randomInvocationCaseList
    : randomInvocationCase ('or' randomInvocationCase)*
    ;

randomInvocationCase
    : ('(' expression ')')? (statementExpression | block)
    ;

literal
	: INTEGRAL_NUMBER
	| FLOATING_POINT
	| PERCENTAGE
	| RANDOM_INTEGRAL_NUMBER
	| BOOLEAN_VALUE
	;

INTEGRAL_NUMBER
    : INTEGER
    ;

RANDOM_INTEGRAL_NUMBER
    : RANDOM_DRAW
    | RANDOM_DICE
    ;

BOOLEAN_VALUE
    : 'true'
    | 'false'
    ;

expressionStatement
	:	statementExpression ';'
	;

statementExpression
	:	assignment
	|	preIncrementExpression
	|	preDecrementExpression
	|	postIncrementExpression
	|	postDecrementExpression
	|	methodInvocation
	;

///////////////////////////////////////////////////////////////////////////////
// Tokens

// Keywords
GRAPH: 'graph';
NODE: 'node';
TRANSFER: 'transfer';
IF: 'if';

// Numerical
INTEGER: [0-9]+;
FLOATING_POINT: [0-9]*'.'[0-9]+;
RANDOM_DRAW: 'draw'[0-9]+;
RANDOM_DICE: [0-9]*'D'[0-9]*;
PERCENTAGE: [0-9]+'%';

// Boolean
TRUE: 'true';
FALSE: 'false';

// Math operator
PLUS: '+';
MINUS: '-';
TIMES: '*';
DIVIDE: '/';
MODULO: '%';

// Others
TO: '->';
LEFT_PARENTHESIS: '(';
RIGHT_PARENTHESIS: ')';
LEFT_CURLY_BRACKET: '{';
RIGHT_CURLY_BRACKET: '}';
SEMI_COLON: ';';
COMMA: ',';

IDENTIFIER: [a-zA-Z_]([a-zA-Z_0-9])*;

WS: [ \r\t\n]+ -> skip;
COMMENT
    :   '/*' .*? '*/' -> skip
    ;

LINE_COMMENT
    :   '//' ~[\r\n]* -> skip
    ;