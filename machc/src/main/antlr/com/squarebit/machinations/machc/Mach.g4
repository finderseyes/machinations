grammar Mach;

graphDeclaration
    :   GRAPH IDENTIFIER graphBody
    ;

graphBody
    :   LEFT_CURLY_BRACKET graphBodyDeclaration* RIGHT_CURLY_BRACKET
    ;

graphBodyDeclaration
    :   nodeDeclaration
    |   eventHookDeclaration
    ;

nodeDeclaration
    :   NODE IDENTIFIER (SEMI_COLON | nodeBody)
    ;

nodeBody
    :   LEFT_CURLY_BRACKET RIGHT_CURLY_BRACKET
    ;

eventHookDeclaration
    :   IDENTIFIER LEFT_PARENTHESIS RIGHT_PARENTHESIS eventHookBody
    ;

eventHookBody
    :   LEFT_CURLY_BRACKET statementDeclaration* RIGHT_CURLY_BRACKET
    ;

statementDeclaration
    : emptyStatement
    | statement
    ;

statement
    : block
    | transferDeclarationStatement
    | probabilisticDeclarationStatement
    | expressionStatement
    | ifThenStatement
    | ifThenElseStatement
    ;

emptyStatement
    : ';'
    ;

block
	: '{' blockStatements? '}'
	;

blockStatements
	: statementDeclaration+
	;

probabilisticDeclarationStatement
    : probabilisticDeclaration
    ;

probabilisticDeclaration
    : 'randomly' probabilisticStatementDeclaration+
    ;

probabilisticStatementDeclaration
    : probability statement
    ;

probability
    : expression
    | 'else'
    ;

transferDeclarationStatement
    : transferDeclaration
    ;

transferDeclaration
    :   'transfer' transferModifierList resourceFlowDeclaration (',' resourceFlowDeclaration)*
    ;

transferModifierList
    : transferModifier*
    ;

transferModifier
    : 'all'
    | 'any'
    ;

resourceFlowDeclaration
    : flowRate? connectionDeclaration
    ;

flowRate
    : expression
    ;

connectionDeclaration
    :   IDENTIFIER TO IDENTIFIER
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
	:   literal
	|   '(' expression ')'
	|   methodInvocation
	;

methodInvocation
	:	methodName '(' argumentList? ')'
	|	expressionName '.' IDENTIFIER '(' argumentList? ')'
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

literal
	: INTEGER
	| FLOATING_POINT
	| PERCENTAGE
	| RANDOM_NUMBER
	;

RANDOM_NUMBER
    : RANDOM_DRAW
    | RANDOM_DICE
    ;

expressionStatement
	:	statementExpression
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