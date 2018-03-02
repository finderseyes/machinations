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
    :   LEFT_CURLY_BRACKET eventHookBodyDeclaration* RIGHT_CURLY_BRACKET
    ;

eventHookBodyDeclaration
    :   statement SEMI_COLON
    ;

statement
    :   transferStatement
    |   ifThenStatement
    ;

transferStatement
    :   TRANSFER connectionDeclaration;

connectionDeclaration
    :   IDENTIFIER TO IDENTIFIER
    ;

ifThenStatement
    :   IF LEFT_PARENTHESIS expression RIGHT_PARENTHESIS statement
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
	:	INTEGER
	|	REAL
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
REAL: [0-9]*'.'[0-9]+;

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