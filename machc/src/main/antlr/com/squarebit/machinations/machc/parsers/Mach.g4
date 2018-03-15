grammar Mach;

unitDeclaration
    : unitStructureDeclaration*
    ;

unitStructureDeclaration
    : graphDeclaration
    ;

graphDeclaration
    : graphModifier? 'graph' graphType baseGraphDescriptor? graphBody
    ;

graphModifier
    : 'default'
    ;

graphBody
    :   '{' graphBodyDeclaration* '}'
    ;

graphBodyDeclaration
    : graphFieldDeclaration
    | methodDeclaration
    ;

graphFieldDeclaration
    : nodeDeclaration
    | connectionDeclaration
    | fieldDeclaration
    ;

baseGraphDescriptor
    : 'extends' graphType
    ;

// Field declaration shares the same rules with local variable declaration
fieldDeclaration
    : variableDeclaration ';'
    ;

variableDeclaration
    : 'let' variableDeclarationList
    ;

variableDeclarationList
    : variableDeclarator (',' variableDeclarator)*
    ;

variableDeclarator
    : variableName ('=' variableInitializer)?
    ;

variableInitializer
    : expression
    ;

variableName
    : IDENTIFIER
    ;


connectionDeclaration
    : 'connection' connectionDeclarationList ';'
    ;

connectionDeclarationList
    : connectionDeclarator (',' connectionDeclarator?)*
    ;

connectionDeclarator
    : connectionId '=' connectionDescriptor
    ;

connectionDescriptor
    : flowDescriptor? directionDescriptor
    ;

connectionId
    : IDENTIFIER
    ;

flowDescriptor
    : setDescriptor ':'
    ;

directionDescriptor
    : fromDefaultSourceDirectionDescriptor
    | toDefaultDrainDirectionDescriptor
    | normalDirectionDescriptor
    ;

fromDefaultSourceDirectionDescriptor
    : TO nodeId
    ;

toDefaultDrainDirectionDescriptor
    : nodeId TO
    ;

normalDirectionDescriptor
    : nodeId TO nodeId
    ;

nodeDeclaration
    : nodeModifier? (nodeType | nodeArrayType) nodeDeclaratorList ';'
    ;

nodeModifier
    : 'input'
    | 'output'
    ;

nodeArrayType
    : nodeType dims
    ;

dims
    : oneDim+
    ;

oneDim
    : '[' integralLiteral ']'
    ;

nodeType
    : builtinNodeTypeName | graphType
    ;

builtinNodeTypeName
    : 'pool'
    | 'transitive'
    | 'source'
    | 'drain'
    | 'converter'
    | 'end'
    ;

graphType
    : IDENTIFIER
    ;

nodeDeclaratorList
    : nodeDeclarator (',' nodeDeclarator?)*
    ;

nodeDeclarator
    : nodeId ('=' nodeInitializer)?
    ;

nodeId
    : IDENTIFIER
    ;

nodeInitializer
    : resourceSetNodeInitializer
    ;

resourceSetNodeInitializer
    : setDescriptor
    ;

setDescriptor
    : bracketSetDescriptor | implicitSetDescriptor
    ;

bracketSetDescriptor
    : '{' multipleElementDescriptor (',' multipleElementDescriptor)* '}'
    ;

implicitSetDescriptor
    : expression ('/' integralLiteral)? setElementType?
    ;

multipleElementDescriptor
    : expression ('/' integralLiteral)? setElementType?
    ;

setElementType
    : IDENTIFIER
    ;

methodDeclaration
    : methodModifier? methodName LEFT_PARENTHESIS RIGHT_PARENTHESIS methodBody
    ;

methodModifier
    : startMethodModifier
    | automaticMethodModifier
    | interactiveMethodModifier
    ;

startMethodModifier
    : 'start'
    ;

automaticMethodModifier
    : 'automatic'
    ;

interactiveMethodModifier
    : 'interactive' ('(' interactionCondition ')')?
    ;

interactionCondition
    : expression
    ;

methodName
    : IDENTIFIER
    ;

methodBody
    :   LEFT_CURLY_BRACKET blockStatements* RIGHT_CURLY_BRACKET
    ;

statement
    : emptyStatement
    | block
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
	: blockStatement+
	;

blockStatement
    : localVariableDeclarationStatement
    | statement
    ;

localVariableDeclarationStatement
    : variableDeclaration ';'
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
	| arrayAccess
	| bracketSetDescriptor
	| setOperations
	;

setOperations
    : setCardinality
    ;

setCardinality
    : '|' expression '|'
    ;

arrayAccess
    : expressionName ('[' expression ']')+
    ;

propertyAccess
	: IDENTIFIER '.' IDENTIFIER
	;

methodInvocation
	: methodName '(' argumentList? ')'
	| expressionName '.' IDENTIFIER '(' argumentList? ')'
	| graphicalMethodInvocation
	;

argumentList
	:	expression (',' expression)*
	;

expressionName
	:	IDENTIFIER ('.' IDENTIFIER)*
	;

graphicalMethodInvocation
    : transferInvocation
    | distributionInvocation
    | randomInvocation
    | delayInvocation
    | intervalInvocation
    ;

transferInvocation
    : ':transfer' transferMode? '(' transferDescriptorList ')'
    ;

transferDescriptorList
    : transferDescriptor (',' transferDescriptor)*
    ;

transferDescriptor
    : transferViaNamedConnection
    | transferViaImplicitConnection
    ;

transferViaNamedConnection
    : flowDescriptor? connectionId
    ;

transferViaImplicitConnection
    : connectionDescriptor
    ;

transferMode
    : 'allornone'
    ;

flowDirection
    : connectionId
    | connectionDescriptor
    ;

distributionInvocation
    : ':distribute' distributionAmount? nodeId '(' distributionList ')'
    ;

distributionAmount
    : setDescriptor
    ;

distributionList
    : distributionDescriptor (',' distributionDescriptor)*
    ;

distributionDescriptor
    : distributeViaNamedConnection
    | distributeViaImplicitConnection
    ;

distributeViaNamedConnection
    : (expression ':')? connectionId
    ;

distributeViaImplicitConnection
    : (expression ':')? flowDirection
    ;

randomInvocation
    : ':randomly' 'do' '{' randomInvocationCaseList '}'
    ;

randomInvocationCaseList
    : randomInvocationCase (',' randomInvocationCase)*
    ;

randomInvocationCase
    : randomInvocationCaseProbability ':' (statementExpression | block)
    ;

randomInvocationCaseProbability
    : 'by' expression
    | 'else'
    ;

intervalInvocation
    : ':every' intervalSteps 'steps' 'do' (statementExpression | block)
    ;

intervalSteps
    : expression
    ;

delayInvocation
    : ':delay' delaySteps 'steps' 'then' (statementExpression | block)
    ;

delaySteps
    : expression
    ;

literal
	: integralLiteral
	| floatingPointLiteral
	| randomIntegralLiteral
	| booleanLiteral
	| stringLiteral
	;

integralLiteral
    : INTEGER
    ;

randomIntegralLiteral
    : RANDOM_DRAW
    | RANDOM_DICE
    ;

floatingPointLiteral
    : FLOATING_POINT
    | PERCENTAGE
    ;

booleanLiteral
    : 'true'
    | 'false'
    ;

stringLiteral
    : STRING_LITERAL
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

// String Literals
STRING_LITERAL
    : '"' STRING_CHARACTERS? '"'
    ;

fragment
STRING_CHARACTERS
    : STRING_CHARACTER+
	;

fragment
STRING_CHARACTER
	:	~["\\\r\n]
	;


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