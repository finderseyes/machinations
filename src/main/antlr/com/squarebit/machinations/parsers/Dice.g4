grammar Dice;

program: term ((PLUS|MINUS) term)*;

term
    : diceTerm
    | numericTerm
    ;

diceTerm
    : INT?'D'INT?
    ;

numericTerm
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