grammar Dice;

program: term ((PLUS|MINUS) term)*;

term
    : dice_term
    | numeric_term
    ;

dice_term
    : INT?'D'INT?
    ;

numeric_term
    : INT
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