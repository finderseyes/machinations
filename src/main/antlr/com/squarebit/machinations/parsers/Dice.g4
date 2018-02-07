grammar Dice;

labelModifierExpression: (PLUS|MINUS)INT?;

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

PLUS
    : '+'
    ;

MINUS
    : '-'
    ;

WS
    : [ \r\t\n]+ -> skip
    ;